import androidx.compose.desktop.Window
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

fun main() = Window(
    title = "UI图分离器",
) {
    var text by remember { mutableStateOf("Hello, World!") }

    MaterialTheme {
        topInput()
    }
}

@Composable
fun topInput() {
    var etText by remember { mutableStateOf("") }
    Row {
        TextField(
            value = etText,
            onValueChange = {
                etText = it
            },
            colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
            maxLines = 1,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            modifier = Modifier.height(45.dp).weight(1f).padding(0.dp)
        )
        Button(
            {

            },
            modifier = Modifier.height(45.dp)
        ) {
            Text("选择输出文件夹")
        }
    }
}