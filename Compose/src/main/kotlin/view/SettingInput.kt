package view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
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
        TextField(
            value = etText,
            onValueChange = {
                etText = it
                onTextChangeListener(it)
            },
//            colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
            shape = RoundedCornerShape(0.dp),
            maxLines = 1,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            modifier = Modifier.height(60.dp).width(150.dp).padding(2.dp),
            textStyle = MaterialTheme.typography.body1,
        )
    }
}