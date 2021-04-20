import androidx.compose.desktop.Window
import androidx.compose.foundation.gestures.DraggableState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import util.SelectFileUtil
import util.compose.*
import view.*

fun main() = Window(
    title = "UI图分离器",
    size = IntSize(800, 800),
) {
    val (etTopText, setEtTopText) = remember { mutableStateOf("") }
    MyTheme {
        Column(Modifier.padding(8.dp)) {
            TopInput(etTopText, setEtTopText)
            Row(M.height(500.dp)) {
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
                HorizontalSpace(8)
                LogList(listOf(ImageBitmap(30, 30) to "日志1", ImageBitmap(30, 30) to "日志2"))
            }
            VerticalSpace(8)
            DataSourceButton {
                // TODO by lt 2021/4/15 11:11 action
            }
        }
    }
}

//最底部接收数据的按钮
@Composable
fun DataSourceButton(onClick: () -> Unit) {
    Button(onClick, M.fillMaxHeight().fillMaxWidth()) {
        Text("拖动文件到这里或点击选择文件夹")
    }
}

//日志列表
@Composable
fun LogList(data: List<Pair<ImageBitmap, String>>) {
    Column {
        VerticalGroupCardView("日志", M.weight(1f).fillMaxWidth()) {
            LazyColumn {
                items(data) {
                    LogItemView(it)
                }
            }
        }
        VerticalSpace(8)
    }
}

//其他设置
@Composable
fun SettingsOther(data: List<Pair<Boolean, String>>) {
    val data = remember { mutableStateListOf(*data.toTypedArray()) }
    VerticalGroupCardView("其他") {
        data.forEachIndexed { index, pair ->
            CheckBoxView(pair.first, pair.second) {
                data[index] = Pair(it, pair.second)
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
    var etText by remember { mutableStateOf("自定义") }
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
fun TopInput(etText: String, onText: (String) -> Unit) {
    Row {
        TextField(
            value = etText,
            onValueChange = onText,
            label = { Text("输出文件夹路径") },
            shape = RoundedCornerShape(0.dp),
            maxLines = 1,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            modifier = Modifier.height(55.dp).weight(1f)
        )
        Spacer(Modifier.width(16.dp))
        Button(
            {
                SelectFileUtil.selectSignDir()?.absolutePath?.let(onText)
            },
            modifier = Modifier.height(55.dp)
        ) {
            Text("选择输出文件夹")
        }
    }
}