package view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import util.compose.HorizontalSpace

/**
 * creator: lt  2021/4/14  lt.dygzs@qq.com
 * effect : 带文字的RadioButton
 * warning:
 */
@Composable
fun RadioButtonView(
    isSelect: Boolean,
    text: String,
    onClick: () -> Unit,
) {
    Row(Modifier.clickable(onClick = onClick).height(30.dp), verticalAlignment = Alignment.CenterVertically) {
        RadioButton(isSelect, onClick)
        HorizontalSpace(4)
        Text(text)
    }
}