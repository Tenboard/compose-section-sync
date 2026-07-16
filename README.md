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
- Supporting grids with multiple columns and dynamic section data

## Initial Scope

The first alpha version will support:

- `LazyVerticalGrid`
- Key-based sections
- Grid-to-section synchronization
- Section-to-grid scrolling
- Immediate and animated scrolling
- End-of-list section resolution
- Programmatic scroll conflict handling

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
