package io.github.tenboard.composesectionsync.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.tenboard.composesectionsync.model.PrimaryCategory

@Composable
fun PrimaryCategoryTabRow(
    categories: List<PrimaryCategory>,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (categories.isEmpty()) return

    val selectedIndex = selectedTabIndex.coerceIn(categories.indices)

    ComposeSectionSyncTabRow(
        modifier = modifier.fillMaxWidth(),
        selectedIndex = selectedIndex
    ) {
        categories.forEachIndexed { index, category ->
            val isSelected = index == selectedIndex

            ComposeSectionSyncTab(
                isSelected = isSelected,
                onClick = { onTabSelected(index) },
                modifier = Modifier.height(52.dp),
                label = category.name,
            )
        }
    }
}
