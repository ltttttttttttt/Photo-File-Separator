import androidx.compose.desktop.Window
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import view.settingInput

//仿微信: http://www.qishunwang.net/news_show_67329.aspx

fun main() = Window(
    title = "UI图分离器",
) {
    MaterialTheme() {
        Column(Modifier.padding(8.dp)) {
            topInput()
            Row {

                settings()
            }
        }
    }
}

@Composable
fun settings() {
    Column {
        settingInput("去掉前缀") {

        }
        Spacer(Modifier.height(4.dp))
        settingInput("去掉后缀") {

        }
        Spacer(Modifier.height(4.dp))
        settingInput("增加前缀") {

        }
        Spacer(Modifier.height(4.dp))
        settingInput("增加后缀") {

        }
        Spacer(Modifier.height(4.dp))
        settingInput("统一命名") {

        }
    }
}

//顶部地址栏
@Composable
fun topInput() {
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