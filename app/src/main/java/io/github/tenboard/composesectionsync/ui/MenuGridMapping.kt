package io.github.tenboard.composesectionsync.ui

import io.github.tenboard.composesectionsync.model.PrimaryCategory
import io.github.tenboard.section_sync.SectionAnchor
import io.github.tenboard.section_sync.SectionPath

internal data class MenuGridMapping(
    val anchors: List<SectionAnchor<String>>,
)

internal fun createMenuGridMapping(primaryCategories: List<PrimaryCategory>): MenuGridMapping {
    val anchors = mutableListOf<SectionAnchor<String>>()
    var firstItemIndex = 0
    primaryCategories.forEach { primaryCategory ->
        primaryCategory.subCategories.forEach { category ->
            if (anchors.isNotEmpty()) {
                firstItemIndex++
            }
            anchors.add(
                SectionAnchor(
                    path = SectionPath.of(primaryCategory.id, category.id),
                    firstItemIndex = firstItemIndex,
                ),
            )
            // Each header and each full-span gap still counts as one grid item.
            firstItemIndex += 1 + category.menuList.size
        }
    }
    return MenuGridMapping(anchors)
}
