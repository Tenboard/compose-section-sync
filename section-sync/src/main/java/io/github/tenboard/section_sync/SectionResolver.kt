package io.github.tenboard.section_sync


fun <K : Any> resolveActiveSection(
    anchors: List<SectionAnchor<K>>,
    visibleGridItem: VisibleGridItem,
    canScrollForward: Boolean,
    canScrollBackward: Boolean,
): SectionAnchor<K>? {
    if (anchors.isEmpty()) return null

    return when {
        !canScrollForward && !canScrollBackward -> anchors.first()
        !canScrollForward -> anchors.last()
        else -> anchors.lastOrNull { anchor ->
            anchor.firstItemIndex <= visibleGridItem.index
        }
    }
}