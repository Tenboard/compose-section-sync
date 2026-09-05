package io.github.tenboard.section_sync

import androidx.compose.runtime.Immutable

/**
 * Configures how section synchronization requests are handled.
 *
 * @property ongoingScrollBehavior Determines how a new section scroll request is handled when the
 * grid is already scrolling. Defaults to [OngoingScrollBehavior.InterruptAndProceed].
 */
@Immutable
data class SectionSyncOptions(
    val ongoingScrollBehavior: OngoingScrollBehavior = OngoingScrollBehavior.InterruptAndProceed,
)

/**
 * Defines how a section scroll request is handled while the grid is already scrolling.
 */
enum class OngoingScrollBehavior {
    /**
     * Interrupts the ongoing scroll and proceeds with the requested section scroll.
     */
    InterruptAndProceed,

    /**
     * Interrupts the ongoing scroll and discards the new request, leaving the grid at its current
     * position.
     */
    InterruptAndDiscardRequest,
}
