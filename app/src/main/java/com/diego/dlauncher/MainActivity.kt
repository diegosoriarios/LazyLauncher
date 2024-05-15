package com.diego.dlauncher

import android.appwidget.AppWidgetProviderInfo
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Paint.Align
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalFontFamilyResolver
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontSynthesis
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.diego.dlauncher.model.AppInfo
import com.diego.dlauncher.ui.theme.DLauncherTheme
import com.diego.dlauncher.viewModel.AppViewModel
import com.google.android.glance.appwidget.host.AppWidgetHost
import com.google.android.glance.appwidget.host.rememberAppWidgetHostState
import java.util.Calendar
import java.util.Date
import kotlin.reflect.KFunction1


class MainActivity : ComponentActivity() {
    val appViewModel = AppViewModel()

    fun openItem(className: String) {
        Log.d("LAUNCHER", className)
        val launchIntent: Intent? =
            packageManager.getLaunchIntentForPackage(className)

        if (launchIntent != null) {
            startActivity(launchIntent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appViewModel.loadApps(packageManager)
        val currentTime: Date = Calendar.getInstance().getTime()

        setContent {
            DLauncherTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyList(appViewModel, onPress = ::openItem)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TextClock(
    modifier: Modifier = Modifier,
    format: String = "kk:mm:ss",
    color: Color = Color.Unspecified,
    style: TextStyle = MaterialTheme.typography.labelLarge,
) {
    val textColor = color.takeOrElse {
        style.color.takeOrElse {
            LocalContentColor.current
        }
    }

    val resolver = LocalFontFamilyResolver.current
    val face: Typeface = remember(resolver, style) {
        resolver.resolve(
            fontFamily = style.fontFamily,
            fontWeight = FontWeight.Bold,
            fontStyle = style.fontStyle ?: FontStyle.Normal,
            fontSynthesis = style.fontSynthesis ?: FontSynthesis.All
        )
    }.value as Typeface

    AndroidView(
        modifier = modifier.fillMaxWidth().wrapContentSize(Alignment.Center),
        factory = { context ->
            android.widget.TextClock(context).apply {
                format24Hour?.let {
                    this.format24Hour = format
                }

                format12Hour?.let {
                    this.format12Hour = format
                }

                timeZone?.let { this.timeZone = it }
                textSize.let { this.textSize = 48.0f }

                setTextColor(textColor.toArgb())
                typeface = face
            }
        }
    )
}

@Composable
fun HostWidget(provider: AppWidgetProviderInfo) {
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

@Composable
fun MyList(appViewModel: AppViewModel = AppViewModel(), onPress: (String) -> Unit) {
    val appUiState by appViewModel.uiState.collectAsState()
    val apps = appUiState.apps

    fun handleOnPress(className: String) {
        onPress(className)
    }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
    ) {
        if (apps == null || apps.size == 0) {
            Text(text = "None")
        }

        TextClock()

        apps!!.forEach { app ->
            MyItem(appInfo = app, ::handleOnPress)
        }
    }
}

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

fun mock(v: String) {}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DLauncherTheme {
        MyList(AppViewModel(), ::mock)
    }
}