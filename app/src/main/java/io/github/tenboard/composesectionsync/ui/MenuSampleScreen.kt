package io.github.tenboard.composesectionsync.ui

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.tenboard.composesectionsync.data.local.SampleMenuDataSource
import io.github.tenboard.composesectionsync.ui.component.MenuGridItem
import io.github.tenboard.composesectionsync.ui.component.PrimaryCategoryTabRow
import io.github.tenboard.composesectionsync.ui.component.SecondaryCategoryTabRow
import io.github.tenboard.section_sync.SectionAnchor
import io.github.tenboard.section_sync.SectionPath
import io.github.tenboard.section_sync.rememberSectionSyncState

@Composable
fun MenuSampleScreen() {
    val data = SampleMenuDataSource
    val primaryCategories = data.primaryCategories

    val anchors = getAnchors()

    val gridState = rememberLazyGridState()
    val syncState = rememberSectionSyncState(
        anchors = anchors,
        gridState = gridState,
    )

    val primaryCategoryIndex = syncState.selectedTabIndexAt(0)
    val secondaryCategoryIndex = syncState.selectedTabIndexAt(1)

    val secondaryCategories =
        primaryCategories
            .getOrNull(primaryCategoryIndex)
            ?.subCategories
            .orEmpty()

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        PrimaryCategoryTabRow(
            categories = primaryCategories,
            selectedTabIndex = primaryCategoryIndex,
            onTabSelected = { index ->
            },
        )

        SecondaryCategoryTabRow(
            categories = secondaryCategories,
            selectedTabIndex = secondaryCategoryIndex,
            onTabSelected = { index ->
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
    SampleMenuDataSource.primaryCategories.forEachIndexed { pIndex, primaryCategory ->
        primaryCategory.subCategories.forEachIndexed { cIndex, category ->
            anchors.add(
                SectionAnchor(
                    path = SectionPath.of(
                        primaryCategory.id, category.id,
                        tabInfo = listOf(pIndex, cIndex)
                    ),
                    firstItemIndex = firstItemIndex
                )
            )
            firstItemIndex += category.menuList.size
        }
    }

    Log.d("asdf", "anchors=$anchors")

    return anchors
}