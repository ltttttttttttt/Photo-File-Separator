package util.compose

import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * creator: lt  2021/4/14  lt.dygzs@qq.com
 * effect : 统一样式设置
 * warning:
 */
@Composable
fun MyTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        PlayThemeDark
    } else {
        PlayThemeLight
    }
    MaterialTheme(
        colors = colors,
        typography = typography,
        content = content
    )
}

val blue = Color(0xFF2772AA)
val backgroundColor = Color(0XFFE0E0E0)
val blueDark = Color(0xFF0B182E)

val Purple300 = Color(0xFFCD52FC)
val Purple700 = Color(0xFF8100EF)

private val PlayThemeLight = lightColors(
    primary = blue,//控件背景色,比如按钮背景颜色,复选框
    onPrimary = Color.White,//次要字颜色,比如按钮颜色
    primaryVariant = blue,//主要变体?
    secondary = blue//次要控件色
)

private val PlayThemeDark = darkColors(
    primary = blueDark,
    onPrimary = Color.White,
    secondary = blueDark,
    surface = blueDark
)