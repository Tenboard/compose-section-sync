package io.github.tenboard.section_sync

import android.util.Log
import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.gestures.stopScroll
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.job

interface SectionSyncState<K : Any> {
    val activePath: SectionPath<K>?

    /**
     * Call from a coroutine with a Compose frame clock, such as one launched using
     * `rememberCoroutineScope` or `LaunchedEffect`.
     * The request is cancelled if the anchor mapping changes, another request supersedes it,
     * or this state leaves the Composition.
     */
    suspend fun scrollToSection(path: SectionPath<K>)

    /**
     * Call from a coroutine with a Compose frame clock, such as one launched using
     * `rememberCoroutineScope` or `LaunchedEffect`.
     * The request is cancelled if the anchor mapping changes, another request supersedes it,
     * or this state leaves the Composition.
     */
    suspend fun animateScrollToSection(path: SectionPath<K>)
}

/**
 * Returns the key at the zero-based [level],
 * or `null` when the level is out of bounds.
 */
fun <K : Any> SectionSyncState<K>.activeKeyAtOrNull(
    level: Int,
): K? {
    return activePath?.keyAtOrNull(level)
}

internal class DefaultSectionSyncState<K : Any>(
    anchors: List<SectionAnchor<K>>,
    private val gridState: LazyGridState,
    sectionSyncOptions: SectionSyncOptions,
) : SectionSyncState<K> {

    var anchors by mutableStateOf(anchors.toList().also { validateAnchors(it) })
        private set

    private var sectionSyncOptions by mutableStateOf(sectionSyncOptions)
    private var retainedPosition: Pair<Int, Int>? = null
    private var activeScrollJob: Job? = null
    private var isDisposed = false

    private var mutableActivePath by mutableStateOf<SectionPath<K>?>(null)
    override val activePath: SectionPath<K>?
        get() = mutableActivePath

    var isProgrammaticScroll by mutableStateOf(false)
        private set

    private var latestScrollRequestId: Long = 0L

    fun gridSnapshot() = GridSnapshot(
        anchors = anchors,
        hasVisibleItems = gridState.layoutInfo.visibleItemsInfo.isNotEmpty(),
        firstVisibleItemIndex = gridState.firstVisibleItemIndex,
        firstVisibleItemScrollOffset = gridState.firstVisibleItemScrollOffset,
        canScrollForward = gridState.canScrollForward,
        canScrollBackward = gridState.canScrollBackward,
        isProgrammaticScroll = isProgrammaticScroll,
        options = sectionSyncOptions,
    )

    fun syncActivePath() {
        if (isDisposed || isProgrammaticScroll) return

        // Read current inputs: a collected snapshot can predate a completed request or input update.
        val snapshot = gridSnapshot()
        if (snapshot.anchors.isEmpty() || !snapshot.hasVisibleItems) {
            retainedPosition = null
            mutableActivePath = null
            return
        }

        val position = snapshot.firstVisibleItemIndex to snapshot.firstVisibleItemScrollOffset
        if (retainedPosition == position) return
        retainedPosition = null
        mutableActivePath = resolveActiveSectionValidated(
            anchors = snapshot.anchors,
            visibleGridItem = VisibleGridItem(snapshot.firstVisibleItemIndex),
            canScrollForward = snapshot.canScrollForward,
            canScrollBackward = snapshot.canScrollBackward,
            options = snapshot.options,
        )?.path
    }

    fun updateInputs(
        anchors: List<SectionAnchor<K>>,
        sectionSyncOptions: SectionSyncOptions,
    ) {
        if (isDisposed) return

        val anchorsChanged = this.anchors != anchors
        if (anchorsChanged) validateAnchors(anchors)
        val selectionChanged =
            this.sectionSyncOptions.shortContentSelection != sectionSyncOptions.shortContentSelection ||
                this.sectionSyncOptions.endOfContentSelection != sectionSyncOptions.endOfContentSelection ||
                this.sectionSyncOptions.selectionAfterScroll != sectionSyncOptions.selectionAfterScroll

        this.sectionSyncOptions = sectionSyncOptions
        if (anchorsChanged) {
            cancelScrollRequest()
            this.anchors = anchors.toList()

            if (this.anchors.none { it.path == activePath }) {
                mutableActivePath = null
            }
        }
        if (anchorsChanged || selectionChanged) {
            retainedPosition = null
            syncActivePath()
        }
    }

    fun dispose() {
        if (isDisposed) return

        isDisposed = true
        cancelScrollRequest()
        retainedPosition = null
        mutableActivePath = null
    }

    private fun cancelScrollRequest() {
        latestScrollRequestId++
        activeScrollJob?.cancel()
    }

    override suspend fun scrollToSection(path: SectionPath<K>) {
        scrollToPath(path, animated = false)
    }

    override suspend fun animateScrollToSection(path: SectionPath<K>) {
        scrollToPath(path, animated = true)
    }

    private suspend fun scrollToPath(
        path: SectionPath<K>,
        animated: Boolean,
    ) = coroutineScope {
        if (isDisposed) return@coroutineScope
        if (anchors.none { it.path == path }) {
            Log.w("SectionSyncState", "Section scroll failed: anchor not found")
            return@coroutineScope
        }

        val requestId = ++latestScrollRequestId
        val wasScrolling = gridState.isScrollInProgress
        val scrollBehavior = sectionSyncOptions.ongoingScrollBehavior
        val previousJob = activeScrollJob
        val requestJob = currentCoroutineContext().job

        // This scope owns only this request; cancelling it does not cancel the caller's parent Job.
        activeScrollJob = requestJob
        isProgrammaticScroll = true
        retainedPosition = null
        var completed = false

        try {
            previousJob?.cancelAndJoin()

            if (wasScrolling) {
                gridState.stopScroll(MutatePriority.PreventUserInput)

                when (scrollBehavior) {
                    OngoingScrollBehavior.InterruptAndDiscardRequest -> {
                        return@coroutineScope
                    }

                    OngoingScrollBehavior.InterruptAndProceed -> {
                        withFrameNanos { }
                    }
                }
            }

            ensureActive()
            if (isDisposed || requestId != latestScrollRequestId) {
                return@coroutineScope
            }

            val anchor = anchors.find { it.path == path } ?: return@coroutineScope
            mutableActivePath = anchor.path

            if (animated) {
                gridState.animateScrollToItem(anchor.firstItemIndex)
            } else {
                gridState.scrollToItem(anchor.firstItemIndex)
            }
            ensureActive()
            completed = true
        } finally {
            if (activeScrollJob === requestJob) {
                activeScrollJob = null
                isProgrammaticScroll = false
                if (completed && requestId == latestScrollRequestId && !isDisposed &&
                    sectionSyncOptions.selectionAfterScroll ==
                    SelectionAfterScroll.KeepRequestedUntilPositionChanges
                ) {
                    retainedPosition = gridState.firstVisibleItemIndex to gridState.firstVisibleItemScrollOffset
                }
                syncActivePath()
            }
        }
    }
}
