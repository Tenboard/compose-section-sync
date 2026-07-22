package io.github.tenboard.composesectionsync.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import io.github.tenboard.composesectionsync.model.Menu

@Composable
fun MenuGridItem(
    item: Menu,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(4f / 3f)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.LightGray),
    ) {
        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.5f),
            model = item.imageUrl,
            contentDescription = null,
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.5f),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(item.name)
            Text("${item.price}")
        }
    }
}
