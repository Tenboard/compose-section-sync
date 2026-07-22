package io.github.tenboard.composesectionsync

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import io.github.tenboard.composesectionsync.ui.MenuSampleScreen
import io.github.tenboard.composesectionsync.ui.component.SafeArea

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                SafeArea {
                    MenuSampleScreen()
                }
            }
        }
    }
}
