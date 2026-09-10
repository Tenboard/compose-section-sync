package io.github.tenboard.section_sync

/**
 * Resolves the active section from the first visible item and the configured boundary policies.
 * A non-scrollable list uses [SectionSyncOptions.shortContentSelection] before the end policy.
 * [BoundarySelection.FollowViewport] selects the last anchor at or before the visible index,
 * or returns `null` when there is no such anchor. An empty anchor list also returns `null`.
 *
 * @throws IllegalArgumentException if anchor indexes are not strictly increasing or paths repeat.
 */
fun <K : Any> resolveActiveSection(
    anchors: List<SectionAnchor<K>>,
    visibleGridItem: VisibleGridItem,
    canScrollForward: Boolean,
    canScrollBackward: Boolean,
    options: SectionSyncOptions = SectionSyncOptions(),
): SectionAnchor<K>? {
    validateAnchors(anchors)
    return resolveActiveSectionValidated(
        anchors, visibleGridItem, canScrollForward, canScrollBackward, options,
    )
}

internal fun <K : Any> resolveActiveSectionValidated(
    anchors: List<SectionAnchor<K>>,
    visibleGridItem: VisibleGridItem,
    canScrollForward: Boolean,
    canScrollBackward: Boolean,
    options: SectionSyncOptions,
): SectionAnchor<K>? {
    if (anchors.isEmpty()) return null

    val selection = when {
        !canScrollForward && !canScrollBackward -> options.shortContentSelection
        !canScrollForward -> options.endOfContentSelection
        else -> BoundarySelection.FollowViewport
    }
    return when (selection) {
        BoundarySelection.FirstAnchor -> anchors.first()
        BoundarySelection.LastAnchor -> anchors.last()
        BoundarySelection.FollowViewport -> anchors.lastOrNull { anchor ->
            anchor.firstItemIndex <= visibleGridItem.index
        }
    }
}
