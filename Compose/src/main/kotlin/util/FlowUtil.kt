package util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * creator: lt  2021/11/18  lt.dygzs@qq.com
 * effect :
 * warning:
 */

/**
 * 快捷注册stateFlow或sharedFlow
 */
//operator fun <T> SharedFlow<T>.invoke(
//    coroutineContext: CoroutineContext = EmptyCoroutineContext,
//    observer: suspend (value: T) -> Unit
//) {
//    mainScope.launch(coroutineContext) {
//        collect(observer)
//    }
//}

/**
 * 将stateFlow放在compose中使用
 */
@Composable
fun <T> MutableStateFlow<T>.toMutableState(): MutableState<T> {
    val state = collectAsState()
    return object : MutableState<T> {
        override var value: T
            get() = state.value
            set(value) {
                this@toMutableState.value = value
            }

        override fun component1(): T = value

        override fun component2(): (T) -> Unit = { value = it }
    }
}