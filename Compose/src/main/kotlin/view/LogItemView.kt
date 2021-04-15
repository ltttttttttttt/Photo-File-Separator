package view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import util.compose.HorizontalSpace

/**
 * creator: lt  2021/4/15  lt.dygzs@qq.com
 * effect : 日志内部的view(一个图片+一个文字)
 * warning:
 */
@Composable
fun LogItemView(pair: Pair<ImageBitmap, String>) {
    Row {
        Image(pair.first, pair.second)
        HorizontalSpace(4)
        Text(pair.second)
    }
}