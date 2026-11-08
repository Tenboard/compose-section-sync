package io.github.tenboard.composesectionsync.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.tenboard.composesectionsync.data.local.SampleMenuDataSource
import io.github.tenboard.composesectionsync.ui.component.AppHeader
import io.github.tenboard.composesectionsync.ui.component.MenuGridHeader
import io.github.tenboard.composesectionsync.ui.component.MenuGridItem
import io.github.tenboard.composesectionsync.ui.component.PrimaryCategoryTabRow
import io.github.tenboard.composesectionsync.ui.component.SecondaryCategoryTabRow
import io.github.tenboard.section_sync.OngoingScrollBehavior
import io.github.tenboard.section_sync.NonSelectableItemSelection
import io.github.tenboard.section_sync.SectionPath
import io.github.tenboard.section_sync.SectionSyncOptions
import io.github.tenboard.section_sync.activeKeyAtOrNull
import io.github.tenboard.section_sync.rememberSectionSyncState
import kotlinx.coroutines.launch

@Composable
fun MenuSampleScreen() {
    val coroutineScope = rememberCoroutineScope()

    val data = SampleMenuDataSource
    val primaryCategories = data.primaryCategories

    val mapping = remember(primaryCategories) { createMenuGridMapping(primaryCategories) }

    val gridState = rememberLazyGridState()
    val syncState = rememberSectionSyncState(
        anchors = mapping.anchors,
        gridState = gridState,
        gapItemCountBeforeSection = 1,
        sectionSyncOptions = SectionSyncOptions(
            ongoingScrollBehavior = OngoingScrollBehavior.InterruptAndProceed,
            nonSelectableItemSelection = NonSelectableItemSelection.KeepCurrent,
        ),
    )

    val primaryCategoryId = syncState.activeKeyAtOrNull(PRIMARY_CATEGORY_LEVEL)
    val secondaryCategoryId = syncState.activeKeyAtOrNull(SECONDARY_CATEGORY_LEVEL)

    val primaryCategoryIndex = primaryCategories
        .indexOfFirst { category ->
            category.id == primaryCategoryId
        }
        .takeIf { index -> index >= 0 }
        ?: 0

    val secondaryCategories =
        primaryCategories
            .getOrNull(primaryCategoryIndex)
            ?.subCategories
            .orEmpty()

    val secondaryCategoryIndex = secondaryCategories
        .indexOfFirst { category ->
            category.id == secondaryCategoryId
        }
        .takeIf { index -> index >= 0 }
        ?: 0

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        AppHeader()

        PrimaryCategoryTabRow(
            categories = primaryCategories,
            selectedTabIndex = primaryCategoryIndex,
            onTabSelected = { index ->
                primaryCategories.getOrNull(index)?.let { primaryCategory ->
                    primaryCategory.subCategories.firstOrNull()?.let { secondaryCategory ->
                        val targetPath = SectionPath.of(
                            primaryCategory.id,
                            secondaryCategory.id,
                        )

                        coroutineScope.launch {
                            syncState.animateScrollToSection(targetPath)
                        }
                    }
                }
            },
        )

        SecondaryCategoryTabRow(
            categories = secondaryCategories,
            selectedTabIndex = secondaryCategoryIndex,
            onTabSelected = { index ->
                val primaryCategory = primaryCategories.getOrNull(primaryCategoryIndex)
                val secondaryCategory = secondaryCategories.getOrNull(index)

                if (primaryCategory != null && secondaryCategory != null) {
                    val targetPath = SectionPath.of(
                        primaryCategory.id,
                        secondaryCategory.id,
                    )

                    coroutineScope.launch {
                        syncState.animateScrollToSection(targetPath)
                    }
                }
            },
        )

        LazyVerticalGrid(
            modifier = Modifier.weight(1f),
            state = gridState,
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            data.categories.forEachIndexed { categoryIndex, category ->
                if (categoryIndex > 0) {
                    item(
                        key = "gap:${category.id}",
                        span = { GridItemSpan(maxLineSpan) },
                    ) {
                        Spacer(Modifier.height(40.dp))
                    }
                }
                item(
                    key = "header:${category.id}",
                    span = { GridItemSpan(maxLineSpan) }
                ) {
                    MenuGridHeader(category)
                }

                items(
                    items = category.menuList,
                    key = { menu -> menu.id },
                ) { item ->
                    MenuGridItem(
                        item = item,
                    )
                }
            }
        }
    }
}

private const val PRIMARY_CATEGORY_LEVEL = 0
private const val SECONDARY_CATEGORY_LEVEL = 1

