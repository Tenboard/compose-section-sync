package io.github.tenboard.section_sync


data class SectionAnchor<K : Any>(
    val path: SectionPath<K>,
    val firstItemIndex: Int,
)

data class VisibleGridItem(
    val index: Int,
)