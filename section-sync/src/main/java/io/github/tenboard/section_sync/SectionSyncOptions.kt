package io.github.tenboard.section_sync

import androidx.compose.runtime.Immutable

/**
 * Configures how section synchronization requests are handled.
 *
 * @property ongoingScrollBehavior Determines how a new section scroll request is handled when the
 * grid is already scrolling. Defaults to [OngoingScrollBehavior.InterruptAndProceed].
 * @property shortContentSelection Selects a section when the grid cannot scroll in either direction.
 * @property endOfContentSelection Selects a section at the end of otherwise scrollable content.
 * @property selectionAfterScroll Determines selection after a section scroll completes successfully.
 * Changes to these three selection policies clear any retained selection and re-evaluate the viewport.
 */
@Immutable
data class SectionSyncOptions(
    val ongoingScrollBehavior: OngoingScrollBehavior = OngoingScrollBehavior.InterruptAndProceed,
    val shortContentSelection: BoundarySelection = BoundarySelection.FirstAnchor,
    val endOfContentSelection: BoundarySelection = BoundarySelection.LastAnchor,
    val selectionAfterScroll: SelectionAfterScroll = SelectionAfterScroll.KeepRequestedUntilPositionChanges,
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

/** Selects a section at a content boundary. Short-content policy takes precedence over end policy. */
enum class BoundarySelection {
    /** Selects the first anchor in the supplied list. */
    FirstAnchor,
    /** Selects the last anchor in the supplied list. */
    LastAnchor,
    /** Selects the last anchor at or before the first visible item, or null if there is none. */
    FollowViewport,
}

/** Defines selection after a successful section scroll; cancelled and discarded requests are not retained. */
enum class SelectionAfterScroll {
    /** Re-evaluates the viewport using the configured boundary policies when the request ends. */
    FollowViewport,
    /**
     * Retains the requested path until the first visible item index or its scroll offset changes.
     * Anchor or selection-policy changes also clear it. A new request replaces the retained selection.
     */
    KeepRequestedUntilPositionChanges,
}
