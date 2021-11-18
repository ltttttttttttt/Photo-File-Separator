package view

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * creator: lt  2021/4/13  lt.dygzs@qq.com
 * effect : 设置的输入view
 * warning:
 */
@Composable
fun SettingInput(
    leftText: String,
    onTextChangeListener: (String) -> Unit
) {
    var etText by remember { mutableStateOf("") }
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(leftText)
        Spacer(Modifier.width(8.dp))
        MTextField(
            etText,
            modifier = Modifier.width(150.dp),
        ) {
            etText = it
            onTextChangeListener(it)
        }
    }
}