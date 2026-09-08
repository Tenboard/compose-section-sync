package io.github.tenboard.section_sync

import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow


internal data class GridSnapshot<K : Any>(
    val anchors: List<SectionAnchor<K>>,
    val hasVisibleItems: Boolean,
    val firstVisibleItemIndex: Int,
    val canScrollForward: Boolean,
    val canScrollBackward: Boolean,
    val isProgrammaticScroll: Boolean,
)

/**
 * Retains synchronization state while [gridState] remains the same.
 *
 * Provide [anchors] for the same item order rendered by the grid. Replace the list when that
 * mapping changes, and keep section paths and their keys immutable.
 * Anchor changes cancel the current section scroll request without restarting it;
 * an empty list also clears the active path. Updated options apply to subsequent requests.
 */
@Composable
fun <K : Any> rememberSectionSyncState(
    anchors: List<SectionAnchor<K>>,
    gridState: LazyGridState,
    sectionSyncOptions: SectionSyncOptions = SectionSyncOptions(),
): SectionSyncState<K> {
    val state = remember(gridState) {
        DefaultSectionSyncState(
            anchors = anchors,
            gridState = gridState,
            sectionSyncOptions = sectionSyncOptions,
        )
    }

    SideEffect {
        state.updateInputs(anchors, sectionSyncOptions)
    }

    DisposableEffect(state) {
        onDispose {
            state.dispose()
        }
    }

    LaunchedEffect(state) {
        snapshotFlow {
            GridSnapshot(
                anchors = state.anchors,
                hasVisibleItems = gridState.layoutInfo.visibleItemsInfo.isNotEmpty(),
                firstVisibleItemIndex = gridState.firstVisibleItemIndex,
                canScrollForward = gridState.canScrollForward,
                canScrollBackward = gridState.canScrollBackward,
                isProgrammaticScroll = state.isProgrammaticScroll,
            )
        }
            .collect { snapshot ->
                if (snapshot.isProgrammaticScroll) return@collect

                val activePath = if (snapshot.hasVisibleItems) {
                    resolveActiveSection(
                        anchors = snapshot.anchors,
                        visibleGridItem = VisibleGridItem(snapshot.firstVisibleItemIndex),
                        canScrollForward = snapshot.canScrollForward,
                        canScrollBackward = snapshot.canScrollBackward,
                    )?.path
                } else {
                    null
                }
                state.updateActivePath(activePath)
            }
    }

    return state
}
