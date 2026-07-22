package io.github.tenboard.composesectionsync.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.tenboard.composesectionsync.data.local.SampleMenuDataSource
import io.github.tenboard.composesectionsync.ui.component.MenuGridItem
import io.github.tenboard.composesectionsync.ui.component.PrimaryCategoryTabRow
import io.github.tenboard.composesectionsync.ui.component.SecondaryCategoryTabRow

@Composable
fun MenuSampleScreen() {
    val data = SampleMenuDataSource
    val primaryCategories = data.primaryCategories

    var selectedPrimaryTabIndex by remember { mutableIntStateOf(0) }
    var selectedSecondaryTabIndex by remember { mutableIntStateOf(0) }

    val secondaryCategories = primaryCategories
        .getOrNull(selectedPrimaryTabIndex)
        ?.subCategories
        .orEmpty()

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        PrimaryCategoryTabRow(
            categories = primaryCategories,
            selectedTabIndex = selectedPrimaryTabIndex,
            onTabSelected = { index ->
                selectedPrimaryTabIndex = index
                selectedSecondaryTabIndex = 0
            },
        )

        SecondaryCategoryTabRow(
            categories = secondaryCategories,
            selectedTabIndex = selectedSecondaryTabIndex,
            onTabSelected = { index ->
                selectedSecondaryTabIndex = index
            },
        )

        LazyVerticalGrid(
            modifier = Modifier.weight(1f),
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            secondaryCategories.forEach { category ->
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
