package view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import util.compose.HorizontalSpace
import util.compose.M

/**
 * creator: lt  2021/4/14  lt.dygzs@qq.com
 * effect : 带文字的CheckBox
 * warning:
 */
@Composable
fun CheckBoxView(isCheck: Boolean, text: String, onCheckedChange: (Boolean) -> Unit) {
    Row(
        M.clickable { onCheckedChange(!isCheck) }.height(30.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(isCheck, onCheckedChange)
        HorizontalSpace(4)
        Text(text)
    }
}