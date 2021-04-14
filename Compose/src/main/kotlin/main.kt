import androidx.compose.desktop.Window
import androidx.compose.foundation.gestures.DraggableState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import util.compose.HorizontalSpace
import util.compose.MyTheme
import util.compose.VerticalSpace
import util.compose.rememberMutableStateOf
import view.CheckBoxView
import view.RadioButtonView
import view.SettingInput
import view.VerticalGroupCardView

fun main() = Window(
    title = "UI图分离器",
) {
    MyTheme {
        Column(Modifier.padding(8.dp)) {
            TopInput()
            Column {
                Row {
                    SettingsPreSuffix()
                    HorizontalSpace(8)
                    Column {
                        SettingsWebP(true, 75)
                        VerticalSpace(8)
                        SettingsPhoto(0, listOf("mipmap", "drawable", "无"))
                    }
                }
                Row {
                    SettingsRepeat(0, listOf("尾数+n", "复制到新文件夹中", "忽略", "覆盖"))
                    HorizontalSpace(8)
                    SettingsOther(listOf(true to "自动复制id"))
                }
            }
        }
    }
}

//其他设置
@Composable
fun SettingsOther(data: List<Pair<Boolean, String>>) {
    VerticalGroupCardView("其他") {
        data.forEach {
            CheckBoxView(it.first, it.second) {
                // TODO by lt 2021/4/14 18:58 不知道这个list的状态怎么存和变更
            }
        }
    }
}

//重复策略设置
@Composable
fun SettingsRepeat(selectIndex: Int, data: List<String>) {
    var selectIndex by rememberMutableStateOf(selectIndex)
    VerticalGroupCardView("重复策略") {
        data.forEachIndexed { index, s ->
            RadioButtonView(index == selectIndex, s) {
                selectIndex = index
            }
        }
    }
}

//图片设置
@Composable
fun SettingsPhoto(selectIndex: Int, data: List<String>) {
    var selectIndex by rememberMutableStateOf(selectIndex)
    var etText by remember { mutableStateOf("") }
    VerticalGroupCardView("图片类型") {
        data.forEachIndexed { index, s ->
            RadioButtonView(index == selectIndex, s) {
                selectIndex = index
            }
        }
        VerticalSpace(4)
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(selectIndex == data.size, { selectIndex = data.size })
            HorizontalSpace(4)
            TextField(
                value = etText,
                onValueChange = {
                    etText = it
                },
                shape = RoundedCornerShape(0.dp),
                maxLines = 1,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                modifier = Modifier.height(60.dp).width(150.dp).padding(2.dp),
                textStyle = MaterialTheme.typography.body1,
            )
        }
    }
}

//webp设置
@Composable
fun SettingsWebP(isCheck: Boolean, number: Int) {
    var isCheck by rememberMutableStateOf(isCheck)
    var number by remember { mutableStateOf(number) }
    VerticalGroupCardView("WebP") {
        CheckBoxView(isCheck, "压缩为WebP") { isCheck = it }
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

//前后缀设置
@Composable
fun SettingsPreSuffix() {
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