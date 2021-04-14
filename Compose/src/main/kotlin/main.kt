import androidx.compose.desktop.Window
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.DraggableState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import util.compose.*
import view.SettingInput

fun main() = Window(
    title = "UI图分离器",
) {
    MyTheme {
        Column(Modifier.padding(8.dp)) {
            TopInput()
            Column {
                Row {
                    Settings1()
                    HorizontalSpace(8)
                    SettingsWebP(true, 75)
                    HorizontalSpace(8)
                    SettingsPhoto()
                }
            }
        }
    }
}

//图片设置
@Composable
fun SettingsPhoto() {
}

//webp设置
@Composable
fun SettingsWebP(isCheck: Boolean, number: Int) {
    var isCheck by rememberMutableStateOf(isCheck)
    var number by remember { mutableStateOf(number) }
    Box(
        Modifier.offset(0.dp, 8.dp)
            .background(
                backgroundColor,
                shape = RoundedCornerShape(8.dp)
            ).width(200.dp)
    ) {
        Column(M.padding(8.dp)) {
            Text("WebP", fontSize = 18.sp)
            VerticalSpace(4)
            Row {
                Checkbox(isCheck, {
                    isCheck = it
                })
                HorizontalSpace(4)
                Text("压缩为WebP")
            }
            VerticalSpace(2)
            Text("压缩率$number(推荐75)")
            VerticalSpace(2)
            // TODO by lt 2021/4/14 14:32 不知道compose是否有现成的可拖动进度条
            LinearProgressIndicator(
                number.toFloat() / 100f,
                //滑动监听
                Modifier.draggable(state = DraggableState(onDelta = {
                    number += it.toInt() / 2
                    if (number < 0)
                        number = 0
                    else if (number > 100)
                        number = 100
                }), orientation = Orientation.Horizontal)//只监听横向滑动?
            )
        }
    }
}

//前后缀设置
@Composable
fun Settings1() {
    Column {
        SettingInput("去掉前缀") {

        }
        VerticalSpace(4)
        SettingInput("去掉后缀") {

        }
        VerticalSpace(4)
        SettingInput("增加前缀") {

        }
        VerticalSpace(4)
        SettingInput("增加后缀") {

        }
        VerticalSpace(4)
        SettingInput("统一命名") {

        }
    }
}

//顶部地址栏
@Composable
fun TopInput() {
    var etText by remember { mutableStateOf("") }
    Row {
        TextField(
            value = etText,
            onValueChange = {
                etText = it
            },
            shape = RoundedCornerShape(0.dp),
            maxLines = 1,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            modifier = Modifier.height(55.dp).weight(1f)
        )
        Spacer(Modifier.width(16.dp))
        Button(
            {

            },
            modifier = Modifier.height(55.dp)
        ) {
            Text("选择输出文件夹")
        }
    }
}