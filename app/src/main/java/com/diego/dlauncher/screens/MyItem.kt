package com.diego.dlauncher.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.diego.dlauncher.model.AppInfo

@Composable
fun MyItem(appInfo: AppInfo, onPress: (String) -> Unit) {
    fun handlePress() {
        onPress(appInfo.name.toString())
    }

    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().clickable { handlePress() } ) {
        Image(
            bitmap = appInfo.icon!!,
            contentDescription = "Content description for visually impaired",
            modifier = Modifier
                .size(50.dp)
                .padding(horizontal = 5.dp)
        )
        Text(text = appInfo.label.toString())
    }
}