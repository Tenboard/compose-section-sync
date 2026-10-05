package io.github.tenboard.composesectionsync.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.tenboard.composesectionsync.model.Category

@Composable
fun MenuGridHeader(
    item: Category
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 29.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = item.name,
            fontSize = 22.sp,
            color = MaterialTheme.colorScheme.onSurface
        )

        Text(
            text = "${item.menuList.size} items",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.secondary
        )
    }
}
