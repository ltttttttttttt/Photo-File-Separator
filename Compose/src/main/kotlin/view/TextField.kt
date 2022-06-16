package view

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import util.compose.M
import util.compose.Shapes
import util.compose.textStyles

/**
 * creator: lt  2021/11/14  lt.dygzs@qq.com
 * effect : 输入框的简便写法
 * warning:
 */
@Composable
fun MTextField(
    text: String,
    hint: String = "",
    maxLines: Int = 1,
    modifier: Modifier = Modifier,
    backgroundColor: Color = util.compose.backgroundColor,
    onValueChange: (String) -> Unit
) {
    BasicTextField(
        value = text,
        onValueChange = onValueChange,
        textStyle = textStyles.body2,
        singleLine = maxLines == 1,
        maxLines = maxLines,
        modifier = modifier,
        decorationBox = {
            Box(
                M
                    .background(backgroundColor, Shapes.medium)
                    .padding(8.dp, 4.dp, 8.dp, 4.dp),
                contentAlignment = Alignment.CenterStart,
            ) {
                if (text.isEmpty() && hint.isNotEmpty()) {
                    Text(
                        text = hint,
                        color = Color.Black,
                        fontSize = 15.sp
                    )
                }
                it()
            }
        }
    )
}

@Preview
@Composable
private fun PreviewMTextField() {
    MTextField(text = "abc", onValueChange = {})
}