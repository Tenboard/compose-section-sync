# Compose Section Sync

A Jetpack Compose library that keeps section tabs and `LazyVerticalGrid`
scrolling in sync in both directions.

> Beta: the public API may change before a stable release.

| Tab → Section | Scroll → Tabs |
| :---: | :---: |
| <img src="https://github.com/user-attachments/assets/174bdf9f-b913-458e-8a0b-fc8c98849794" width="240" alt="Tab to section"> | <img src="https://github.com/user-attachments/assets/a936bf3b-b6bf-480f-9225-1196b760bfa2" width="240" alt="Scroll to tabs"> |

## Installation

Configure `google()` and `mavenCentral()` in your project's repositories, then add:

```kotlin
implementation("io.github.tenboard:compose-section-sync:0.1.0-beta01")
```

## Compatibility

- Android: API 21 or higher.
- Kotlin / Compose Compiler build baseline: 2.0.21.

For Kotlin 2.0+ projects, use the
[Compose Compiler Gradle plugin](https://developer.android.com/develop/ui/compose/setup-compose-dependencies-and-compiler)
with the same version as your project's Kotlin plugin.

## Usage

Map each section to its first grid item and share the same `LazyGridState`
between the library and the grid. This example has 40 items in two sections,
starting at indices 0 and 20.

```kotlin
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.github.tenboard.section_sync.SectionAnchor
import io.github.tenboard.section_sync.SectionPath
import io.github.tenboard.section_sync.activeKeyAtOrNull
import io.github.tenboard.section_sync.rememberSectionSyncState
import kotlinx.coroutines.launch

@Composable
fun SectionGrid() {
    val anchors = remember {
        listOf(
            SectionAnchor(SectionPath.of("Food"), firstItemIndex = 0),
            SectionAnchor(SectionPath.of("Drinks"), firstItemIndex = 20),
        )
    }
    val gridState = rememberLazyGridState()
    val scope = rememberCoroutineScope()
    val syncState = rememberSectionSyncState(anchors, gridState)
    val activeKey = syncState.activeKeyAtOrNull(level = 0)

    Column(Modifier.fillMaxSize()) {
        Row {
            anchors.forEach { anchor ->
                val label = anchor.path.keyAtOrNull(0).orEmpty()
                val selected = activeKey == label
                BasicText(
                    text = label,
                    style = TextStyle(
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                    ),
                    modifier = Modifier.selectable(
                        selected = selected,
                        role = Role.Tab,
                        onClick = {
                            scope.launch {
                                syncState.animateScrollToSection(anchor.path)
                            }
                        },
                    ).padding(12.dp),
                )
            }
        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            state = gridState,
            modifier = Modifier.weight(1f),
        ) {
            items(40) { index ->
                BasicText("Item $index", Modifier.padding(16.dp))
            }
        }
    }
}
```

For nested categories, use `SectionPath.of(parentKey, childKey)` and read the
active keys with `activeKeyAtOrNull(0)` and `activeKeyAtOrNull(1)`.
Use `scrollToSection(path)` instead of `animateScrollToSection(path)` for an
immediate jump. Call either from a Compose coroutine scope, as above.

## Usage rules and limitations

- Each anchor must point to an existing grid item. Count headers and other
  rendered items too; an index is an item position, not a row number.
- Anchor indices must be non-negative and strictly increasing, and paths must
  be unique. Invalid mappings throw `IllegalArgumentException`.
- Omit sections with no rendered items. Active-key lookups may return `null`
  when there is no active section or the requested path level does not exist.
- Keep paths and keys immutable. Replace the anchor list when the item mapping
  changes; this cancels any current section scroll request. The same rule applies
  to `nonSelectableItemIndices` and `gapItemCountBeforeSection`. Both collections
  are copied; in-place mutations are not observed.
- Only `LazyVerticalGrid` is supported. `LazyColumn`, `LazyHorizontalGrid`,
  reverse layout, unloaded Paging sections, and Compose Multiplatform are not
  supported.

## Options

Pass `sectionSyncOptions = SectionSyncOptions(...)` to `rememberSectionSyncState`
to customize these defaults:

| Option | Default | Default behavior |
| --- | --- | --- |
| `ongoingScrollBehavior` | `InterruptAndProceed` | Stop the current scroll and perform the new request. |
| `shortContentSelection` | `FirstAnchor` | Select the first section when the grid cannot scroll in either direction. |
| `endOfContentSelection` | `LastAnchor` | Select the last section at the end of scrollable content. |
| `selectionAfterScroll` | `KeepRequestedUntilPositionChanges` | Keep the requested section selected after a successful scroll until the viewport position changes. |
| `nonSelectableItemSelection` | `KeepCurrent` | Keep the last confirmed section while the first visible item is explicitly excluded. |

See [SectionSyncOptions](section-sync/src/main/java/io/github/tenboard/section_sync/SectionSyncOptions.kt)
for alternative values and detailed behavior.

### Category gaps

Use `gapItemCountBeforeSection` to automatically treat a fixed number of items
immediately before every anchor except the first as gaps. Its default is `0`.
You can also pass `nonSelectableItemIndices` for individually excluded items,
or combine both inputs; overlapping indices are treated as one exclusion.

These inputs register existing grid items. They do not insert spacers, add
anchors, or shift anchor indices. Count every rendered header, spacer, and
content item; indices are not row numbers. Use full-span spacers so that
selectable content does not share their row. Padding and `Arrangement.spacedBy`
have no item indices and are not covered.

For example, render 20 Food items, one full-span spacer, then 20 Drinks items:

```kotlin
val anchors = remember {
    listOf(
        SectionAnchor(SectionPath.of("Food"), firstItemIndex = 0),
        SectionAnchor(SectionPath.of("Drinks"), firstItemIndex = 21),
    )
}
val syncState = rememberSectionSyncState(
    anchors = anchors,
    gridState = gridState,
    gapItemCountBeforeSection = 1, // Automatically excludes index 20.
    sectionSyncOptions = SectionSyncOptions(
        nonSelectableItemSelection = NonSelectableItemSelection.KeepCurrent,
    ),
)
```

For manual registration instead, omit `gapItemCountBeforeSection` and pass
`nonSelectableItemIndices = setOf(20)`. For a combined example, anchors at
0, 10, and 20 with `gapItemCountBeforeSection = 1` automatically exclude
9 and 19. Passing `nonSelectableItemIndices = setOf(5, 9)` makes the effective
excluded indices `{5, 9, 19}`. Both sources use the same selection policy.

Automatic gaps are generated only before the second and later anchors.
Use manual indices for leading/trailing gaps or additional irregular gaps.
The count must be non-negative and smaller than every pair of consecutive
anchor indices' difference, so an automatic gap cannot include the preceding
anchor. Invalid counts throw `IllegalArgumentException`, even for a negative
count with no anchors. The library assumes that each calculated index is an
actual gap item; it cannot distinguish ordinary content placed there.

The spacer is a separate grid item, for example:

```kotlin
item(span = { GridItemSpan(maxLineSpan) }) {
    Spacer(Modifier.height(40.dp))
}
```

Import `SectionSyncOptions` and `NonSelectableItemSelection` from
`io.github.tenboard.section_sync`, `GridItemSpan` from
`androidx.compose.foundation.lazy.grid`, and `Spacer`/`height` from
`androidx.compose.foundation.layout` in addition to the imports in Usage.

| Gap policy | Behavior between Food and Drinks |
| --- | --- |
| `PreviousSection` | Select Food, regardless of scroll direction. |
| `NextSection` | Select Drinks, regardless of scroll direction. |
| `KeepCurrent` | Retain Food when entering from Food, or Drinks when entering from Drinks. |

The policy uses the **first visible item**, not whether any spacer is visible.
`KeepCurrent` returns `null` if no section has been confirmed or its path was
removed. If `PreviousSection`/`NextSection` has no neighboring anchor, it also
falls back to the last confirmed section, or `null`. Empty anchors or a grid
with no visible items clear selection. Excluded indices must be non-negative,
point to existing grid items, and must not overlap anchor indices; negative or
overlapping indices throw `IllegalArgumentException`.

Selection precedence is:

1. A running section request controls the provisional selection. After a
   successful request, `KeepRequestedUntilPositionChanges` retains its target
   until the first visible index or offset changes.
2. When re-evaluating the viewport, a manually or automatically excluded first
   item uses the gap policy, even for short content or at the end of content.
3. Otherwise, short-content selection precedes end-of-content selection,
   followed by the normal viewport rule.

Cancelled and discarded requests never become the confirmed selection. They
restore the last confirmed path and then apply the current viewport rules, so
`KeepCurrent` cannot retain an unfinished request's target. Mapping changes
cancel the current request; selection-policy changes clear position retention
and re-evaluate after the current request ends, including changes made while
the request was running. Cancellation does not cancel the caller's parent Job.

The default empty set and gap count of zero preserve existing viewport behavior.
Adding the public parameters and the `SectionSyncOptions` property changes JVM signatures,
including the options constructor and `copy`; rebuild dependent code when
upgrading. Ordinary Kotlin calls using the existing arguments remain valid on
recompilation. These additions require a new library release before they can
be used through the Maven dependency above.

## Sample

See [MenuSampleScreen](app/src/main/java/io/github/tenboard/composesectionsync/ui/MenuSampleScreen.kt)
for a complete example with nested categories, section headers, and full-span
gaps registered automatically with `gapItemCountBeforeSection = 1` that
preserve the selected category.

## License

[MIT License](LICENSE).
