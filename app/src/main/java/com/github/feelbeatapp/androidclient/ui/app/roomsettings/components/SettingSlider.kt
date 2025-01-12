package com.github.feelbeatapp.androidclient.ui.app.roomsettings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.feelbeatapp.androidclient.ui.theme.FeelBeatTheme

@Composable
fun SettingSlider(
    label: String,
    value: Int,
    onValueChange: (Int) -> Unit,
    valueRange: IntRange,
    interval: Int,
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium)
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Slider(
                value = value.toFloat(),
                onValueChange = { onValueChange(it.toInt()) },
                valueRange = valueRange.first.toFloat()..valueRange.last.toFloat(),
                steps = (valueRange.last - valueRange.first) / interval - 1,
                enabled = enabled,
                modifier = Modifier.weight(1f).height(30.dp),
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = value.toString(), style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
@Preview(showBackground = true)
fun SettingSliderPreview() {
    FeelBeatTheme {
        SettingSlider(
            label = "Label preview",
            value = 2,
            onValueChange = {},
            valueRange = 1..5,
            interval = 1,
        )
    }
}
