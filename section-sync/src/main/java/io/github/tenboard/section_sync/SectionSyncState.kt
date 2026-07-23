package io.github.tenboard.section_sync

import android.util.Log
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

interface SectionSyncState<K : Any> {
    val gridState: LazyGridState
    val anchors: List<SectionAnchor<K>>
    var activePath: SectionPath<K>?

    suspend fun updateActivePath(path: SectionPath<K>)
    suspend fun scrollToSection(path: SectionPath<K>)
}

internal class DefaultSectionSyncState<K : Any>(
    override val anchors: List<SectionAnchor<K>>,
    override val gridState: LazyGridState,
) : SectionSyncState<K> {

    override var activePath by mutableStateOf<SectionPath<K>?>(null)

    override suspend fun updateActivePath(path: SectionPath<K>) {
        activePath = path
        Log.d("SectionSyncState", "SectionSyncState activePath=$activePath")
    }

    override suspend fun scrollToSection(path: SectionPath<K>) {
        val findAnchor = anchors.find { it.path == path }

        if (findAnchor != null) {
            gridState.scrollToItem(findAnchor.firstItemIndex)
        } else {
            Log.w("SectionSyncState", "SectionSyncState scrollToSection Fail - Anchor Not Found")
        }
    }
}