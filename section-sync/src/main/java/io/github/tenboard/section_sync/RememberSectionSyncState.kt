package io.github.tenboard.section_sync

import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.flow.distinctUntilChanged


internal data class GridSnapshot(
    val firstVisibleItemIndex: Int,
    val canScrollForward: Boolean,
    val canScrollBackward: Boolean,
    val isProgrammaticScroll: Boolean,
)

@Composable
fun <K : Any> rememberSectionSyncState(
    anchors: List<SectionAnchor<K>>,
    gridState: LazyGridState,
    sectionSyncOptions: SectionSyncOptions = SectionSyncOptions(),
): SectionSyncState<K> {
    val state = remember(anchors, gridState, sectionSyncOptions) {
        DefaultSectionSyncState(
            anchors = anchors,
            gridState = gridState,
            sectionSyncOptions = sectionSyncOptions,
        )
    }

    if (anchors.isEmpty()) return state

    LaunchedEffect(state) {
        snapshotFlow {
            if (gridState.layoutInfo.visibleItemsInfo.isEmpty()) {
                return@snapshotFlow null
            } else {
                GridSnapshot(
                    firstVisibleItemIndex = gridState.firstVisibleItemIndex,
                    canScrollForward = gridState.canScrollForward,
                    canScrollBackward = gridState.canScrollBackward,
                    isProgrammaticScroll = state.isProgrammaticScroll
                )
            }
        }
            .distinctUntilChanged()
            .collect { snapshot ->
                if (snapshot == null || snapshot.isProgrammaticScroll) return@collect

                val visibleGridItem = VisibleGridItem(
                    index = snapshot.firstVisibleItemIndex
                )

                resolveActiveSection(
                    anchors = state.anchors,
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
