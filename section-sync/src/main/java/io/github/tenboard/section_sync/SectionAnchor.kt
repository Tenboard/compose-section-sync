package io.github.tenboard.section_sync

/**
 * Maps a unique section [path] to its first displayed item.
 *
 * Anchor lists must have strictly increasing [firstItemIndex] values and unique paths.
 * Omit sections that have no displayed items.
 */
data class SectionAnchor<K : Any>(
    val path: SectionPath<K>,
    val firstItemIndex: Int,
) {
    init {
        require(firstItemIndex >= 0) { "Anchor index must be nonnegative: $firstItemIndex" }
    }
}

internal fun <K : Any> validateAnchors(anchors: List<SectionAnchor<K>>) {
    var previousIndex = -1
    val paths = HashSet<SectionPath<K>>()
    for (anchor in anchors) {
        require(anchor.firstItemIndex > previousIndex) {
            "Anchor indexes must be strictly increasing: $previousIndex, ${anchor.firstItemIndex}"
        }
        require(paths.add(anchor.path)) { "Anchor paths must be unique: ${anchor.path}" }
        previousIndex = anchor.firstItemIndex
    }
}

data class VisibleGridItem(
    val index: Int,
)
