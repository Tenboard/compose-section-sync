package io.github.tenboard.section_sync

data class SectionPath<K : Any>(
    val segments: List<K>,
) {

    /**
     * Returns the key at the zero-based [level],
     * or `null` when the level is out of bounds.
     */
    fun keyAtOrNull(level: Int): K? {
        return segments.getOrNull(level)
    }

    companion object {
        fun <K : Any> of(vararg segments: K): SectionPath<K> {
            return SectionPath(
                segments = segments.toList(),
            )
        }
    }
}