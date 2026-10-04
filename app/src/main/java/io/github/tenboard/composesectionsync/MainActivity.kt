package io.github.tenboard.composesectionsync

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import io.github.tenboard.composesectionsync.ui.MenuSampleScreen
import io.github.tenboard.composesectionsync.ui.component.SafeArea
import io.github.tenboard.composesectionsync.ui.theme.ComposeSectionSyncTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ComposeSectionSyncTheme {
                SafeArea {
                    MenuSampleScreen()
                }
            }
        }
    }
}
