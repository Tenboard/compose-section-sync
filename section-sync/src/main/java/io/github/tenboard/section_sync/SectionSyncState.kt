package io.github.tenboard.section_sync

import android.util.Log
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

interface SectionSyncState<K : Any> {
    val activePath: SectionPath<K>?

    suspend fun scrollToSection(path: SectionPath<K>)
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
    val anchors: List<SectionAnchor<K>>,
    private val gridState: LazyGridState,
) : SectionSyncState<K> {

    private var mutableActivePath by mutableStateOf<SectionPath<K>?>(null)
    override val activePath: SectionPath<K>?
        get() = mutableActivePath

    var isProgrammaticScroll by mutableStateOf(false)
        private set

    private var latestScrollRequestId: Long = 0L

    fun updateActivePath(path: SectionPath<K>) {
        mutableActivePath = path
    }

    override suspend fun scrollToSection(path: SectionPath<K>) {
        val findAnchor = anchors.find { it.path == path }

        if (findAnchor != null) {
            scrollToAnchor(findAnchor, false)
        } else {
            Log.w("SectionSyncState", "SectionSyncState scrollToSection Fail - Anchor Not Found")
        }
    }

    override suspend fun animateScrollToSection(path: SectionPath<K>) {
        val findAnchor = anchors.find { it.path == path }

        if (findAnchor != null) {
            scrollToAnchor(findAnchor, true)
        } else {
            Log.w(
                "SectionSyncState",
                "SectionSyncState animateScrollToSection Fail - Anchor Not Found"
            )
        }
    }

    private suspend fun scrollToAnchor(
        anchor: SectionAnchor<K>,
        animated: Boolean,
    ) {
        val requestId = ++latestScrollRequestId
        isProgrammaticScroll = true

        try {
            updateActivePath(anchor.path)

            if (animated) {
                gridState.animateScrollToItem(
                    anchor.firstItemIndex,
                )
            } else {
                gridState.scrollToItem(
                    anchor.firstItemIndex,
                )
            }
        } finally {
            if (requestId == latestScrollRequestId) {
                isProgrammaticScroll = false
            }
        }
    }
}
