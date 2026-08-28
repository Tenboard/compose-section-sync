package io.github.tenboard.section_sync



data class SectionPath<K : Any>(
    val segments: List<K>,
) {
    companion object {
        fun <K : Any> of(vararg segments: K): SectionPath<K> {
            return SectionPath(
                segments = segments.toList(),
            )
        }
    }
}