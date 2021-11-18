import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import util.SelectFileUtil
import util.compose.*
import util.toMutableState
import view.*

//新window api: https://github.com/JetBrains/compose-jb/tree/master/tutorials/Window_API_new
fun main() =
    application {
        val vm = remember { MainVM() }
        Window(
            title = "UI图分离器",
            onCloseRequest = ::exitApplication,
        ) {
            Size(800f, 600f)
            MyTheme {
                View(vm)
            }
        }
    }

@Composable
private fun View(vm: MainVM) {
    var outputDirPath by vm.outputDirPath.toMutableState()
    Column(Modifier.padding(8.dp)) {
        TopInput(outputDirPath) { outputDirPath = it }
        Row(M.height(340.dp)) {
            Column {
                SettingsPreSuffix(vm)
                VerticalSpace(8)
                SettingsRepeat(0, listOf("尾数+n", "复制到新文件夹中", "忽略", "覆盖"))
            }
            HorizontalSpace(8)
            Column {
                SettingsWebP(true, 75f)
                VerticalSpace(8)
                SettingsPhoto(0, listOf("mipmap", "drawable", "无"))
                VerticalSpace(8)
                SettingsOther(listOf(true to "自动复制id"))
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
            MTextField(
                etText,
                hint = "自定义",
                modifier = Modifier.width(150.dp),
                backgroundColor = ColorC4,
            ) {
                etText = it
            }
        }
    }
}

//webp设置
@Composable
fun SettingsWebP(isCheck: Boolean, number: Float) {
    var isCheck by rememberMutableStateOf(isCheck)
    var number by remember { mutableStateOf(number) }
    VerticalGroupCardView("WebP") {
        CheckBoxView(isCheck, "压缩为WebP") { isCheck = it }
        VerticalSpace(2)
        Text("压缩率${number.toInt()}(推荐75)")
        VerticalSpace(2)
        Slider(
            value = number,
            colors = SliderDefaults.colors(
                thumbColor = Color.White, // 圆圈的颜色
//                activeTrackColor = Color(0xFF0079D3)
            ),
            onValueChange = {
                number = it
            },
            modifier = M.height(20.dp),
            valueRange = 0f..100f,
        )
    }
}

//前后缀设置
@Composable
fun SettingsPreSuffix(vm: MainVM) {
    var removePrefix by vm.removePrefix.toMutableState()
    var removeSuffix by vm.removeSuffix.toMutableState()
    var addPrefix by vm.addPrefix.toMutableState()
    var addSuffix by vm.addSuffix.toMutableState()
    var unifiedNaming by vm.unifiedNaming.toMutableState()
    Column {
        VerticalSpace(4)
        SettingInput("去掉前缀", removePrefix) { removePrefix = it }
        VerticalSpace(4)
        SettingInput("去掉后缀", removeSuffix) { removeSuffix = it }
        VerticalSpace(4)
        SettingInput("增加前缀", addPrefix) { addPrefix = it }
        VerticalSpace(4)
        SettingInput("增加后缀", addSuffix) { addSuffix = it }
        VerticalSpace(4)
        SettingInput("统一命名", unifiedNaming) { unifiedNaming = it }
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