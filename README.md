# Compose Section Sync

A Jetpack Compose library for bidirectional synchronization between
section-based tabs and `LazyVerticalGrid` scrolling.

> 🚧 This project is in early development.
> No stable artifact has been published yet.

## Motivation

Jetpack Compose provides low-level scrolling and layout information through
`LazyGridState`, but coordinating a section-based tab UI with a
`LazyVerticalGrid` requires additional state and synchronization logic.

This project aims to provide reusable state and APIs for:

- Updating the active section while the user scrolls the grid
- Scrolling the grid when the user selects a section
- Handling conflicts between user scrolling and programmatic scrolling
- Supporting grids with multiple columns and fully loaded section data

## Initial Scope

The first alpha version will support:

- `LazyVerticalGrid`
- Key-based sections
- Grid-to-section synchronization
- Section-to-grid scrolling
- Immediate and animated scrolling
- End-of-list section resolution
- Programmatic scroll conflict handling

## Usage

Define each section with stable keys in parent-to-child order and connect the
path to the first item index of that section.
Anchor indices must be non-negative and strictly increasing, and paths must be
unique. Invalid mappings throw `IllegalArgumentException`. Omit sections with no
rendered items; an empty anchor list is allowed and clears the active path.

```kotlin
val anchors = listOf(
    SectionAnchor(
        path = SectionPath.of("food", "korean"),
        firstItemIndex = 0,
    ),
    SectionAnchor(
        path = SectionPath.of("food", "western"),
        firstItemIndex = 12,
    ),
    SectionAnchor(
        path = SectionPath.of("drink", "coffee"),
        firstItemIndex = 24,
    ),
)

val gridState = rememberLazyGridState()
val coroutineScope = rememberCoroutineScope()
val sectionSyncState = rememberSectionSyncState(
    anchors = anchors,
    gridState = gridState,
)
```

When generating anchors, skip empty sections. For example, if the grid renders
only the following items in section order (without separate header items):

```kotlin
val sections = listOf(
    SectionPath.of("food", "korean") to listOf("Bibimbap", "Bulgogi"),
    SectionPath.of("food", "western") to emptyList<String>(),
    SectionPath.of("drink", "coffee") to listOf("Americano"),
)
val anchors = buildList {
    var firstItemIndex = 0
    for ((path, items) in sections) {
        if (items.isNotEmpty()) {
            add(SectionAnchor(path, firstItemIndex))
        }
        firstItemIndex += items.size
    }
}
// Only korean (index 0) and coffee (index 2) have anchors.
```

Count every rendered grid item, including headers if present, when calculating
indices. Anchor paths and keys must remain immutable. Replacing the mapping
cancels the current section scroll request without automatically restarting it.

Read the active key at any category level.

```kotlin
private const val PRIMARY_CATEGORY_LEVEL = 0
private const val SECONDARY_CATEGORY_LEVEL = 1

val primaryCategoryKey =
    sectionSyncState.activeKeyAtOrNull(PRIMARY_CATEGORY_LEVEL)

val secondaryCategoryKey =
    sectionSyncState.activeKeyAtOrNull(SECONDARY_CATEGORY_LEVEL)
```

Scroll to an exact section path.

```kotlin
coroutineScope.launch {
    sectionSyncState.animateScrollToSection(
        SectionPath.of("drink", "coffee"),
    )
}
```

## Selection policies

Pass `SectionSyncOptions` to `rememberSectionSyncState` to configure selection.

| Option | Default | Behavior |
| --- | --- | --- |
| `shortContentSelection` | `FirstAnchor` | Selection when neither scroll direction is available. |
| `endOfContentSelection` | `LastAnchor` | Selection at the end of otherwise scrollable content. |
| `selectionAfterScroll` | `KeepRequestedUntilPositionChanges` | Selection after a successful section scroll. |

Both boundary options accept `FirstAnchor`, `LastAnchor`, or `FollowViewport`.
`FollowViewport` selects the last anchor at or before the first visible item;
it returns no selection if that item precedes the first anchor. Short-content
policy takes precedence over end-of-content policy.

After a successful request, `KeepRequestedUntilPositionChanges` retains the
requested path until the first visible item index or its scroll offset changes.
This preserves tab selection when the requested item shares a row or cannot be
aligned at the top. A new request replaces the retained selection. Cancelled or
discarded requests are not retained. `FollowViewport` instead re-evaluates the
current viewport and boundary policies when the request ends, even if the
scroll position did not change.

Changing anchors or any selection policy clears retained selection and
re-evaluates the viewport. During a section scroll, viewport selection resumes
when the request ends; the latest selection policies then apply. No anchors or
no visible items means no active path.

The existing `ongoingScrollBehavior` is captured when a request starts.
`InterruptAndProceed` stops the current scroll and proceeds with the request;
subsequent user input can still cancel the movement. `InterruptAndDiscardRequest`
stops the current scroll and discards the new request.

## Non-goals

The first alpha version will not support:

- `LazyHorizontalGrid`
- `LazyColumn`
- Paging-based unloaded sections
- Compose Multiplatform
- Reverse layout

## Project Structure

- `section-sync`: Android library module
- `app`: Sample application

## Status

The public API is under design and may change without notice.
