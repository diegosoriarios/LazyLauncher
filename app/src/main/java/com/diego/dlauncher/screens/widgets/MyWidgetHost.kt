package com.diego.dlauncher.screens.widgets

import android.appwidget.AppWidgetProviderInfo
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.google.android.glance.appwidget.host.AppWidgetHost
import com.google.android.glance.appwidget.host.rememberAppWidgetHostState

@Composable
fun MyHostWidget(provider: AppWidgetProviderInfo) {
    val previewHostState = rememberAppWidgetHostState(provider)
    if (previewHostState.isReady) {
        //previewHostState.updateAppWidget()
    }
    AppWidgetHost(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        displaySize = DpSize(200.dp, 200.dp),
        state = previewHostState
    )
}