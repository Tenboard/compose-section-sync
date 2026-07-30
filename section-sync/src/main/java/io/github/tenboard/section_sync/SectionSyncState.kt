package io.github.tenboard.section_sync

import android.util.Log
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.compose.runtime.setValue

interface SectionSyncState<K : Any> {
    val gridState: LazyGridState
    val anchors: List<SectionAnchor<K>>
    var activePath: SectionPath<K>?

    val isProgrammaticScroll: State<Boolean>

    suspend fun updateActivePath(path: SectionPath<K>)
    suspend fun scrollToSection(path: SectionPath<K>)

    suspend fun updateTabWithScroll(targetDepth: Int, index: Int)
    suspend fun updateTabWithScrollAnimation(targetDepth: Int, index: Int)

    fun selectedTabIndexAt(depth: Int): Int
}

internal class DefaultSectionSyncState<K : Any>(
    override val anchors: List<SectionAnchor<K>>,
    override val gridState: LazyGridState,
) : SectionSyncState<K> {

    override var activePath by mutableStateOf<SectionPath<K>?>(null)

    private val _isProgrammaticScroll = mutableStateOf(false)
    override val isProgrammaticScroll: State<Boolean> = _isProgrammaticScroll

    private var latestScrollRequestId: Long = 0L

    override suspend fun updateActivePath(path: SectionPath<K>) {
        activePath = path
        Log.d("SectionSyncState", "SectionSyncState activePath=$activePath")
    }

    override suspend fun scrollToSection(path: SectionPath<K>) {
        val findAnchor = anchors.find { it.path == path }

        if (findAnchor != null) {
            scrollToAnchor(findAnchor, false)
        } else {
            Log.w("SectionSyncState", "SectionSyncState scrollToSection Fail - Anchor Not Found")
        }
    }

    override suspend fun updateTabWithScroll(targetDepth: Int, index: Int) {
        val anchor = updatedTabAnchor(targetDepth, index) ?: return

        scrollToAnchor(
            anchor = anchor,
            animated = false
        )
    }

    override suspend fun updateTabWithScrollAnimation(targetDepth: Int, index: Int) {
        val anchor = updatedTabAnchor(targetDepth, index) ?: return

        scrollToAnchor(
            anchor = anchor,
            animated = true
        )
    }

    override fun selectedTabIndexAt(depth: Int): Int {
        try {
            return activePath?.tabInfo[depth] ?: 0
        } catch (e: IndexOutOfBoundsException) {
            Log.e("SectionSyncState", "SectionSyncState selected tab out of bounds", e)
            return 0
        }
    }

    private suspend fun scrollToAnchor(
        anchor: SectionAnchor<K>,
        animated: Boolean,
    ) {
        val requestId = ++latestScrollRequestId
        _isProgrammaticScroll.value = true

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
                _isProgrammaticScroll.value = false
            }
        }
    }

    private fun updatedTabAnchor(targetDepth: Int, index: Int): SectionAnchor<K>? {
        val nowTabInfo = activePath?.tabInfo?.toMutableList() ?: return null

        if (targetDepth !in nowTabInfo.indices || index < 0) return null

        nowTabInfo.forEachIndexed { nowDepth, _ ->
            when {
                nowDepth == targetDepth -> {
                    nowTabInfo[nowDepth] = index
                }

                nowDepth > targetDepth -> {
                    nowTabInfo[nowDepth] = 0
                }
            }
        }

        return anchors.find { it.path.tabInfo == nowTabInfo }
    }
}