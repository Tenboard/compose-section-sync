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
    val firstVisibleItemScrollOffset: Int,
    val canScrollForward: Boolean,
    val canScrollBackward: Boolean,
    val isProgrammaticScroll: Boolean,
    val options: SectionSyncOptions,
)

/**
 * Retains synchronization state while [gridState] remains the same.
 *
 * Provide [anchors] for the same item order rendered by the grid. Replace the list when that
 * mapping changes, and keep section paths and their keys immutable.
 * Each anchor must point to an existing grid item. Omit sections with no rendered items.
 * Indices must be non-negative and strictly increasing, and paths must be unique.
 * Anchor changes cancel the current section scroll request without restarting it;
 * an empty list also clears the active path. Selection-policy changes clear retained selection
 * and re-evaluate the viewport (after the current request ends if one is running).
 * Updated ongoing-scroll behavior applies to subsequent requests.
 *
 * @throws IllegalArgumentException if anchor indices are negative, repeated, or out of order,
 * or if paths are repeated.
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
        snapshotFlow { state.gridSnapshot() }
            .collect {
                state.syncActivePath()
            }
    }

    return state
}
