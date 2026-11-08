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
    val nonSelectableItemIndices: Set<Int>,
    val gapItemCountBeforeSection: Int,
)

/**
 * Retains synchronization state while [gridState] remains the same.
 *
 * Provide [anchors] for the same item order rendered by the grid. Replace the list when that
 * mapping changes, and keep section paths and their keys immutable.
 * Each anchor must point to an existing grid item. Omit sections with no rendered items.
 * Indices must be non-negative and strictly increasing, and paths must be unique.
 * [nonSelectableItemIndices] identifies rendered spacer items, not padding or arrangement spacing.
 * These indices must be non-negative and must not overlap anchor indices. Use full-span spacers
 * between sections, count them in the grid mapping, and replace the set when the mapping changes.
 * The first visible item's index determines whether the excluded-item policy applies.
 * Both collections are copied; in-place mutations are not observed.
 * [gapItemCountBeforeSection] additionally excludes that many items immediately before every
 * anchor except the first, combined with [nonSelectableItemIndices]. It defaults to zero and
 * must be non-negative; an automatic gap must not include the preceding anchor. This does not
 * insert spacers or adjust anchor indices: render and count the actual gap items in the grid.
 * Mapping changes cancel the current section scroll request without restarting it;
 * an empty list also clears the active path. Selection-policy changes clear retained selection
 * and re-evaluate the viewport (after the current request ends if one is running).
 * Updated ongoing-scroll behavior applies to subsequent requests.
 *
 * @throws IllegalArgumentException if anchor indices are negative, repeated, or out of order,
 * if paths are repeated, if an excluded index is negative or overlaps an anchor, or if the gap
 * count is negative or causes an automatic gap to include the preceding anchor.
 */
@Composable
fun <K : Any> rememberSectionSyncState(
    anchors: List<SectionAnchor<K>>,
    gridState: LazyGridState,
    sectionSyncOptions: SectionSyncOptions = SectionSyncOptions(),
    nonSelectableItemIndices: Set<Int> = emptySet(),
    gapItemCountBeforeSection: Int = 0,
): SectionSyncState<K> {
    val state = remember(gridState) {
        DefaultSectionSyncState(
            anchors = anchors,
            gridState = gridState,
            sectionSyncOptions = sectionSyncOptions,
            nonSelectableItemIndices = nonSelectableItemIndices,
            gapItemCountBeforeSection = gapItemCountBeforeSection,
        )
    }

    SideEffect {
        state.updateInputs(anchors, sectionSyncOptions, nonSelectableItemIndices, gapItemCountBeforeSection)
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
