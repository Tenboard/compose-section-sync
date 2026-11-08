package io.github.tenboard.section_sync

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertThrows
import org.junit.Test

class SectionResolverTest {
    private val a = SectionAnchor(SectionPath.of("parent", "A"), 2)
    private val b = SectionAnchor(SectionPath.of("parent", "B"), 10)
    private val c = SectionAnchor(SectionPath.of("other", "C"), 20)
    private val anchors = listOf(a, b, c)
    private val gaps = setOf(0, 1, 8, 9, 18, 19, 24)

    @Test
    fun defaultPolicyKeepsTheSectionFromEitherScrollDirection() {
        assertEquals(NonSelectableItemSelection.KeepCurrent, SectionSyncOptions().nonSelectableItemSelection)
        for (index in listOf(8, 9)) {
            assertEquals(a, resolve(index, previous = a.path))
            assertEquals(b, resolve(index, previous = b.path))
        }
    }

    @Test
    fun previousAndNextPoliciesChooseTheirNeighborForConsecutiveGaps() {
        for (index in listOf(8, 9)) {
            assertEquals(a, resolve(index, NonSelectableItemSelection.PreviousSection, b.path))
            assertEquals(b, resolve(index, NonSelectableItemSelection.NextSection, a.path))
        }
    }

    @Test
    fun keepCurrentDoesNotInventAnInitialOrRemovedSelection() {
        assertNull(resolve(8))
        assertNull(resolve(8, previous = SectionPath.of("removed")))
        assertNull(resolveActiveSection(emptyList(), VisibleGridItem(8), false, false,
            nonSelectableItemIndices = gaps, previousActivePath = a.path))
    }

    @Test
    fun missingNeighborKeepsOnlyAValidPreviousSection() {
        assertEquals(b, resolve(0, NonSelectableItemSelection.PreviousSection, b.path))
        assertEquals(a, resolve(24, NonSelectableItemSelection.NextSection, a.path))
        assertNull(resolve(0, NonSelectableItemSelection.PreviousSection))
        assertNull(resolve(24, NonSelectableItemSelection.NextSection))
    }

    @Test
    fun explicitGapPolicyOverridesEveryShortAndEndPolicy() {
        for (short in BoundarySelection.entries) {
            for (end in BoundarySelection.entries) {
                for (canScrollBackward in listOf(false, true)) {
                    for (policy in NonSelectableItemSelection.entries) {
                        val expected = if (policy == NonSelectableItemSelection.NextSection) b else a
                        assertEquals(expected, resolveActiveSection(
                            anchors, VisibleGridItem(9), false, canScrollBackward,
                            SectionSyncOptions(shortContentSelection = short,
                                endOfContentSelection = end, nonSelectableItemSelection = policy),
                            gaps, a.path,
                        ))
                    }
                }
            }
        }
    }

    @Test
    fun ordinaryItemsStillFollowTheViewportAndBoundaryPolicies() {
        for (policy in NonSelectableItemSelection.entries) {
            assertEquals(b, resolve(11, policy, a.path))
            assertEquals(b, resolve(10, policy, a.path))
        }
        assertEquals(a, resolveActiveSection(anchors, VisibleGridItem(11), false, false))
        assertEquals(c, resolveActiveSection(anchors, VisibleGridItem(11), false, true))
        assertNull(resolveActiveSection(anchors, VisibleGridItem(1), true, false))
    }

    @Test
    fun emptyExcludedSetPreservesExistingBoundaryPrecedence() {
        assertEquals(c, resolveActiveSection(anchors, VisibleGridItem(9), false, false,
            SectionSyncOptions(shortContentSelection = BoundarySelection.LastAnchor,
                endOfContentSelection = BoundarySelection.FirstAnchor)))
        assertEquals(a, resolveActiveSection(anchors, VisibleGridItem(9), false, true,
            SectionSyncOptions(endOfContentSelection = BoundarySelection.FirstAnchor)))
    }

    @Test
    fun invalidExcludedIndicesAreRejectedEvenWithNoAnchors() {
        assertThrows(IllegalArgumentException::class.java) {
            resolveActiveSection(anchors, VisibleGridItem(3), true, true,
                nonSelectableItemIndices = setOf(-1))
        }
        assertThrows(IllegalArgumentException::class.java) {
            resolveActiveSection(anchors, VisibleGridItem(3), true, true,
                nonSelectableItemIndices = setOf(b.firstItemIndex))
        }
        assertThrows(IllegalArgumentException::class.java) {
            resolveActiveSection<String>(emptyList(), VisibleGridItem(0), false, false,
                nonSelectableItemIndices = setOf(-1))
        }
    }

    @Test
    fun automaticGapsCombineWithManualIndicesForEveryPolicy() {
        for (index in listOf(5, 8, 9, 18, 19)) {
            for (policy in NonSelectableItemSelection.entries) {
                val expected = when (policy) {
                    NonSelectableItemSelection.PreviousSection -> if (index < 10) a else b
                    NonSelectableItemSelection.NextSection -> if (index < 10) b else c
                    NonSelectableItemSelection.KeepCurrent -> c
                }
                assertEquals(expected, resolveActiveSection(
                    anchors, VisibleGridItem(index), true, true,
                    SectionSyncOptions(nonSelectableItemSelection = policy),
                    nonSelectableItemIndices = setOf(5, 9),
                    previousActivePath = c.path,
                    gapItemCountBeforeSection = 2,
                ))
            }
        }
    }

    @Test
    fun automaticGapsSkipTheFirstAnchorAndLeaveOrdinaryItemsUnchanged() {
        val expected = mapOf(1 to null, 2 to a, 7 to a, 10 to b, 17 to b, 20 to c, 24 to c)
        for ((index, section) in expected) {
            assertEquals(section, resolveActiveSection(
                anchors, VisibleGridItem(index), true, true,
                SectionSyncOptions(nonSelectableItemSelection = NonSelectableItemSelection.NextSection),
                previousActivePath = a.path,
                gapItemCountBeforeSection = 2,
            ))
        }
        // With the default count of zero, index 9 remains ordinary A content.
        assertEquals(a, resolveActiveSection(anchors, VisibleGridItem(9), true, true,
            previousActivePath = b.path))
    }

    @Test
    fun automaticGapsOverrideShortAndEndPolicies() {
        for (canScrollBackward in listOf(false, true)) {
            assertEquals(b, resolveActiveSection(
                anchors, VisibleGridItem(9), false, canScrollBackward,
                SectionSyncOptions(
                    shortContentSelection = BoundarySelection.LastAnchor,
                    endOfContentSelection = BoundarySelection.LastAnchor,
                    nonSelectableItemSelection = NonSelectableItemSelection.NextSection,
                ),
                gapItemCountBeforeSection = 2,
            ))
        }
    }

    @Test
    fun automaticGapsAreAbsentForEmptyOrSingleAnchorLists() {
        assertNull(resolveActiveSection<String>(emptyList(), VisibleGridItem(0), true, true,
            gapItemCountBeforeSection = Int.MAX_VALUE))
        assertNull(resolveActiveSection(listOf(a), VisibleGridItem(1), true, true,
            previousActivePath = a.path, gapItemCountBeforeSection = Int.MAX_VALUE))
        assertEquals(a, resolveActiveSection(listOf(a), VisibleGridItem(2), true, true,
            gapItemCountBeforeSection = Int.MAX_VALUE))
    }

    @Test
    fun invalidAutomaticGapCountsAreRejected() {
        for (suppliedAnchors in listOf(emptyList(), listOf(a), anchors)) {
            assertThrows(IllegalArgumentException::class.java) {
                resolveActiveSection(suppliedAnchors, VisibleGridItem(0), true, true,
                    gapItemCountBeforeSection = -1)
            }
        }
        for (count in listOf(8, 9, Int.MAX_VALUE)) {
            assertThrows(IllegalArgumentException::class.java) {
                resolveActiveSection(anchors, VisibleGridItem(9), true, true,
                    gapItemCountBeforeSection = count)
            }
        }
        assertEquals(a, resolveActiveSection(anchors, VisibleGridItem(3), true, true,
            previousActivePath = a.path, gapItemCountBeforeSection = 7))
    }

    @Test
    fun largeAutomaticGapsAreCheckedWithoutExpandingEveryIndex() {
        val first = SectionAnchor(SectionPath.of("first"), 0)
        val last = SectionAnchor(SectionPath.of("last"), Int.MAX_VALUE)
        assertEquals(last, resolveActiveSection(
            listOf(first, last), VisibleGridItem(1), true, true,
            SectionSyncOptions(nonSelectableItemSelection = NonSelectableItemSelection.NextSection),
            gapItemCountBeforeSection = Int.MAX_VALUE - 1,
        ))
    }

    private fun resolve(
        index: Int,
        policy: NonSelectableItemSelection = NonSelectableItemSelection.KeepCurrent,
        previous: SectionPath<String>? = null,
    ) = resolveActiveSection(anchors, VisibleGridItem(index), true, true,
        SectionSyncOptions(nonSelectableItemSelection = policy), gaps, previous)
}
