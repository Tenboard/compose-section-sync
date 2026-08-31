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
