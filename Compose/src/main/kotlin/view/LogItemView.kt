package view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.lt.load_the_image.rememberImagePainter
import util.compose.HorizontalSpace
import util.compose.M

/**
 * creator: lt  2021/4/15  lt.dygzs@qq.com
 * effect : 日志内部的view(一个图片+一个文字)
 * warning:
 */
@Composable
fun LogItemView(path: String) {
    Row(M.padding(vertical = 3.dp)) {
        Image(rememberImagePainter(path), path, M.size(30.dp))
        HorizontalSpace(4)
        Text(path)
    }
}