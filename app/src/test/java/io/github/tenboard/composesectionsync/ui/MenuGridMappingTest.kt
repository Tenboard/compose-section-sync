package io.github.tenboard.composesectionsync.ui

import io.github.tenboard.composesectionsync.data.local.SampleMenuDataSource
import io.github.tenboard.section_sync.SectionPath
import io.github.tenboard.section_sync.VisibleGridItem
import io.github.tenboard.section_sync.resolveActiveSection
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MenuGridMappingTest {
    @Test
    fun mappingCountsEveryHeaderMenuAndFullSpanGapInRenderOrder() {
        val primaryCategories = SampleMenuDataSource.primaryCategories
        val mapping = createMenuGridMapping(primaryCategories)
        val expectedPaths = primaryCategories.flatMap { parent ->
            parent.subCategories.map { SectionPath.of(parent.id, it.id) }
        }
        assertEquals(expectedPaths, mapping.anchors.map { it.path })

        var renderedIndex = 0
        val expectedGaps = mutableSetOf<Int>()
        SampleMenuDataSource.categories.forEachIndexed { index, category ->
            if (index > 0) expectedGaps.add(renderedIndex++)
            assertEquals(renderedIndex, mapping.anchors[index].firstItemIndex)
            renderedIndex += 1 + category.menuList.size
        }
        assertEquals(mapping.anchors.size - 1, expectedGaps.size)
        assertTrue(mapping.anchors.none { it.firstItemIndex in expectedGaps })
        expectedGaps.forEach { gap ->
            val previous = mapping.anchors.last { it.firstItemIndex < gap }
            val next = mapping.anchors.first { it.firstItemIndex > gap }
            for (confirmed in listOf(previous, next)) {
                assertEquals(confirmed, resolveActiveSection(
                    anchors = mapping.anchors,
                    visibleGridItem = VisibleGridItem(gap),
                    canScrollForward = true,
                    canScrollBackward = true,
                    previousActivePath = confirmed.path,
                    gapItemCountBeforeSection = 1,
                ))
            }
        }
    }

    @Test
    fun noCategoriesProduceNoAnchorsOrGaps() {
        val mapping = createMenuGridMapping(emptyList())
        assertTrue(mapping.anchors.isEmpty())
    }
}
