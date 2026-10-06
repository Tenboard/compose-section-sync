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
  changes; this cancels any current section scroll request.
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

See [SectionSyncOptions](section-sync/src/main/java/io/github/tenboard/section_sync/SectionSyncOptions.kt)
for alternative values and detailed behavior.

## Sample

See [MenuSampleScreen](app/src/main/java/io/github/tenboard/composesectionsync/ui/MenuSampleScreen.kt)
for a complete example with nested categories and section headers.

## License

[MIT License](LICENSE).
