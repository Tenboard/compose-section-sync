package io.github.tenboard.section_sync



data class SectionPath<K : Any>(
    val segments: List<K>,
    val tabInfo: List<Int>,
) {
    companion object {
        fun <K : Any> of(vararg segments: K, tabInfo: List<Int>): SectionPath<K> {
            return SectionPath(
                segments = segments.toList(),
                tabInfo = tabInfo
            )
        }
    }
}