package io.github.tenboard.section_sync

/**
 * Resolves the active section from the first visible item and the configured boundary policies.
 * A non-scrollable list uses [SectionSyncOptions.shortContentSelection] before the end policy.
 * [BoundarySelection.FollowViewport] selects the last anchor at or before the visible index,
 * or returns `null` when there is no such anchor. An empty anchor list also returns `null`.
 *
 * [gapItemCountBeforeSection] automatically excludes that many items immediately before each
 * anchor except the first. These ranges are combined with [nonSelectableItemIndices]; neither
 * grid items nor anchor indices are changed. The caller must render the corresponding gap items.
 * If the first visible index is excluded by either input, the excluded-item policy takes
 * precedence over both boundary policies. [previousActivePath] is an explicit input, not stored
 * state: only a path still present in [anchors] can be retained. A missing preceding/following
 * anchor falls back to this path, or `null` if there is no valid previous path.
 *
 * @throws IllegalArgumentException if anchor indexes are not strictly increasing, paths repeat,
 * an excluded index is negative or is also an anchor index, or [gapItemCountBeforeSection] is
 * negative or causes an automatic gap to include the preceding anchor.
 */
fun <K : Any> resolveActiveSection(
    anchors: List<SectionAnchor<K>>,
    visibleGridItem: VisibleGridItem,
    canScrollForward: Boolean,
    canScrollBackward: Boolean,
    options: SectionSyncOptions = SectionSyncOptions(),
    nonSelectableItemIndices: Set<Int> = emptySet(),
    previousActivePath: SectionPath<K>? = null,
    gapItemCountBeforeSection: Int = 0,
): SectionAnchor<K>? {
    validateAnchors(anchors)
    validateNonSelectableItemIndices(anchors, nonSelectableItemIndices, gapItemCountBeforeSection)
    return resolveActiveSectionValidated(
        anchors, visibleGridItem, canScrollForward, canScrollBackward, options,
        nonSelectableItemIndices, previousActivePath, gapItemCountBeforeSection,
    )
}

internal fun <K : Any> resolveActiveSectionValidated(
    anchors: List<SectionAnchor<K>>,
    visibleGridItem: VisibleGridItem,
    canScrollForward: Boolean,
    canScrollBackward: Boolean,
    options: SectionSyncOptions,
    nonSelectableItemIndices: Set<Int>,
    previousActivePath: SectionPath<K>?,
    gapItemCountBeforeSection: Int,
): SectionAnchor<K>? {
    if (anchors.isEmpty()) return null

    val isAutomaticGap = gapItemCountBeforeSection > 0 && (1 until anchors.size).any { index ->
        val nextSectionStartIndex = anchors[index].firstItemIndex
        visibleGridItem.index in (nextSectionStartIndex - gapItemCountBeforeSection) until nextSectionStartIndex
    }
    if (visibleGridItem.index in nonSelectableItemIndices || isAutomaticGap) {
        val previous = anchors.firstOrNull { it.path == previousActivePath }
        return when (options.nonSelectableItemSelection) {
            NonSelectableItemSelection.PreviousSection ->
                anchors.lastOrNull { it.firstItemIndex < visibleGridItem.index } ?: previous
            NonSelectableItemSelection.NextSection ->
                anchors.firstOrNull { it.firstItemIndex > visibleGridItem.index } ?: previous
            NonSelectableItemSelection.KeepCurrent -> previous
        }
    }

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

internal fun <K : Any> validateNonSelectableItemIndices(
    anchors: List<SectionAnchor<K>>,
    nonSelectableItemIndices: Set<Int>,
    gapItemCountBeforeSection: Int = 0,
) {
    require(gapItemCountBeforeSection >= 0) { "Gap item count must be nonnegative" }
    require(nonSelectableItemIndices.all { it >= 0 }) { "Excluded item indices must be nonnegative" }
    require(anchors.none { it.firstItemIndex in nonSelectableItemIndices }) {
        "An anchor cannot point to an excluded item"
    }
    for (index in 1 until anchors.size) {
        require(gapItemCountBeforeSection <
            anchors[index].firstItemIndex - anchors[index - 1].firstItemIndex) {
            "An automatic gap cannot include the preceding anchor"
        }
    }
}
