package io.github.tenboard.composesectionsync.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.tenboard.composesectionsync.data.local.SampleMenuDataSource
import io.github.tenboard.composesectionsync.ui.component.MenuGridItem
import io.github.tenboard.composesectionsync.ui.component.PrimaryCategoryTabRow
import io.github.tenboard.composesectionsync.ui.component.SecondaryCategoryTabRow
import io.github.tenboard.section_sync.SectionAnchor
import io.github.tenboard.section_sync.SectionPath
import io.github.tenboard.section_sync.rememberSectionSyncState
import kotlinx.coroutines.launch

@Composable
fun MenuSampleScreen() {
    val coroutineScope = rememberCoroutineScope()

    val data = SampleMenuDataSource
    val primaryCategories = data.primaryCategories

    val anchors = remember { getAnchors() }

    val gridState = rememberLazyGridState()
    val syncState = rememberSectionSyncState(
        anchors = anchors,
        gridState = gridState,
    )

    val primaryCategoryIndex = primaryCategories
        .indexOfFirst { category ->
            category.id == syncState.activePath?.segments?.getOrNull(0)
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
            category.id == syncState.activePath?.segments?.getOrNull(1)
        }
        .takeIf { index -> index >= 0 }
        ?: 0

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
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
            contentPadding = PaddingValues(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            data.categories.forEach { category ->
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

private fun getAnchors(): List<SectionAnchor<String>> {
    val anchors = mutableListOf<SectionAnchor<String>>()

    var firstItemIndex = 0
    SampleMenuDataSource.primaryCategories.forEach { primaryCategory ->
        primaryCategory.subCategories.forEach { category ->
            anchors.add(
                SectionAnchor(
                    path = SectionPath.of(
                        primaryCategory.id, category.id,
                    ),
                    firstItemIndex = firstItemIndex,
                )
            )
            firstItemIndex += category.menuList.size
        }
    }

    return anchors
}
