package view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import util.compose.M
import util.compose.VerticalSpace
import util.compose.backgroundColor

/**
 * creator: lt  2021/4/14  lt.dygzs@qq.com
 * effect : 带有标题的CardView
 * warning:
 */
@Composable
fun VerticalGroupCardView(title: String, m: M = M, content: @Composable ColumnScope.() -> Unit) {
    Box(
        m.offset(0.dp, 8.dp)
            .background(
                backgroundColor,
                shape = RoundedCornerShape(8.dp)
            ).width(200.dp)
    ) {
        Column(M.padding(8.dp)) {
            Text(title, fontSize = 18.sp)
            VerticalSpace(4)
            content()
        }
    }
}
