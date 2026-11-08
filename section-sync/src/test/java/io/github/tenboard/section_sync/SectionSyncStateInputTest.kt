package io.github.tenboard.section_sync

import androidx.compose.foundation.lazy.grid.LazyGridState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertThrows
import org.junit.Test

// Rendering and request cancellation are covered by SectionSyncGapTest on a laid-out grid.
class SectionSyncStateInputTest {
    private val a = SectionAnchor(SectionPath.of("A"), 0)
    private val b = SectionAnchor(SectionPath.of("B"), 10)
    private val anchors = listOf(a, b)
    private val options = SectionSyncOptions()

    @Test
    fun invalidInitialMappingIsRejected() {
        for (indices in listOf(setOf(-1), setOf(10))) {
            assertThrows(IllegalArgumentException::class.java) {
                DefaultSectionSyncState(anchors, LazyGridState(), options, indices)
            }
        }
        for (count in listOf(-1, 10)) {
            assertThrows(IllegalArgumentException::class.java) {
                DefaultSectionSyncState(anchors, LazyGridState(), options,
                    gapItemCountBeforeSection = count)
            }
        }
    }

    @Test
    fun constructorAndUpdatesCopyTheMappingCollections() {
        val suppliedAnchors = anchors.toMutableList()
        val suppliedIndices = mutableSetOf(8, 9)
        val state = DefaultSectionSyncState(suppliedAnchors, LazyGridState(), options, suppliedIndices)
        suppliedAnchors.clear()
        suppliedIndices.clear()
        assertEquals(anchors, state.gridSnapshot().anchors)
        assertEquals(setOf(8, 9), state.gridSnapshot().nonSelectableItemIndices)

        suppliedAnchors.add(a)
        suppliedIndices.add(3)
        state.updateInputs(suppliedAnchors, options, suppliedIndices)
        suppliedAnchors.clear()
        suppliedIndices.clear()
        assertEquals(listOf(a), state.gridSnapshot().anchors)
        assertEquals(setOf(3), state.gridSnapshot().nonSelectableItemIndices)
    }

    @Test
    fun invalidUpdateLeavesMappingOptionsAndSelectionUntouched() {
        val state = DefaultSectionSyncState(anchors, LazyGridState(), options, setOf(8, 9))
        val before = state.gridSnapshot()
        for (invalid in listOf(setOf(-1), setOf(10))) {
            assertThrows(IllegalArgumentException::class.java) {
                state.updateInputs(anchors, options.copy(
                    nonSelectableItemSelection = NonSelectableItemSelection.NextSection,
                ), invalid)
            }
            assertEquals(before, state.gridSnapshot())
            assertEquals(null, state.activePath)
            assertFalse(state.isProgrammaticScroll)
        }
        for (count in listOf(-1, 10)) {
            assertThrows(IllegalArgumentException::class.java) {
                state.updateInputs(anchors, options.copy(
                    nonSelectableItemSelection = NonSelectableItemSelection.NextSection,
                ), gapItemCountBeforeSection = count)
            }
            assertEquals(before, state.gridSnapshot())
        }
    }

    @Test
    fun gapCountUpdatesKeepManualIndicesAndAnchorsUnchanged() {
        val state = DefaultSectionSyncState(anchors, LazyGridState(), options, setOf(4, 9))
        assertEquals(0, state.gridSnapshot().gapItemCountBeforeSection)
        state.updateInputs(anchors, options, setOf(4, 9), gapItemCountBeforeSection = 2)
        assertEquals(anchors, state.gridSnapshot().anchors)
        assertEquals(setOf(4, 9), state.gridSnapshot().nonSelectableItemIndices)
        assertEquals(2, state.gridSnapshot().gapItemCountBeforeSection)
        state.updateInputs(anchors, options, setOf(4, 9))
        assertEquals(0, state.gridSnapshot().gapItemCountBeforeSection)
        assertEquals(setOf(4, 9), state.gridSnapshot().nonSelectableItemIndices)
    }
}
