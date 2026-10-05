package io.github.tenboard.composesectionsync.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecondaryScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.github.tenboard.composesectionsync.model.PrimaryCategory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrimaryCategoryTabRow(
    categories: List<PrimaryCategory>,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (categories.isEmpty()) return

    val selectedIndex = selectedTabIndex.coerceIn(categories.indices)
    val selectedColor = MaterialTheme.colorScheme.primary
    val unselectedColor = MaterialTheme.colorScheme.onSurfaceVariant

    SecondaryScrollableTabRow(
        selectedTabIndex = selectedIndex,
        modifier = modifier.fillMaxWidth(),
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = selectedColor,
        edgePadding = 12.dp,
        indicator = { tabPositions ->
            val selectedTabPosition = tabPositions.getOrNull(selectedIndex)
                ?: return@SecondaryScrollableTabRow

            Box(
                modifier = Modifier
                    .tabIndicatorOffset(selectedTabPosition)
                    .padding(horizontal = 16.dp)
                    .height(3.dp)
                    .clip(RoundedCornerShape(topStart = 3.dp, topEnd = 3.dp))
                    .background(selectedColor),
            )
        },
        divider = {
            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.45f),
            )
        },
    ) {
        categories.forEachIndexed { index, category ->
            val isSelected = index == selectedIndex

            Tab(
                selected = isSelected,
                onClick = { onTabSelected(index) },
                modifier = Modifier.height(52.dp),
                selectedContentColor = selectedColor,
                unselectedContentColor = unselectedColor,
                text = {
                    Text(
                        text = category.name,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        maxLines = 1,
                    )
                },
            )
        }
    }
}
