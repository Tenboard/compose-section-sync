package io.github.tenboard.section_sync

import android.util.Log
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.flow.distinctUntilChanged


data class GridSnapshot(
    val firstVisibleItemIndex: Int,
    val canScrollForward: Boolean,
    val canScrollBackward: Boolean,
)

@Composable
fun <K : Any> rememberSectionSyncState(
    anchors: List<SectionAnchor<K>>,
    gridState: LazyGridState,
): SectionSyncState<K> {
    val state: SectionSyncState<K> = remember(anchors, gridState) {
        DefaultSectionSyncState(
            anchors = anchors,
            gridState = gridState
        )
    }

    if (anchors.isEmpty()) return state

    LaunchedEffect(state) {
        snapshotFlow {
            if (gridState.layoutInfo.visibleItemsInfo.isEmpty()) {
                Log.w("RememberSectionSyncState", "visibleItemsInfo is Empty")
                return@snapshotFlow null
            } else {
                GridSnapshot(
                    firstVisibleItemIndex = gridState.firstVisibleItemIndex,
                    canScrollForward = gridState.canScrollForward,
                    canScrollBackward = gridState.canScrollBackward,
                )
            }
        }
            .distinctUntilChanged()
            .collect { snapshot ->
                if (snapshot == null) return@collect

                val visibleGridItem = VisibleGridItem(
                    index = snapshot.firstVisibleItemIndex
                )

                resolveActiveSection(
                    anchors = anchors,
                    visibleGridItem = visibleGridItem,
                    canScrollForward = snapshot.canScrollForward,
                    canScrollBackward = snapshot.canScrollBackward
                )?.let { resolveSection ->
                    state.updateActivePath(resolveSection.path)
                }
            }
    }

    return state
}