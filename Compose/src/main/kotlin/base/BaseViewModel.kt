package base

import kotlinx.coroutines.flow.MutableStateFlow

/**
 * creator: lt  2021/11/18  lt.dygzs@qq.com
 * effect : vm
 * warning:
 */
interface BaseViewModel {
    /**
     * stateFlow,与liveData不同的是,如果变化的对象equals=true则不会回调,liveData会
     */
    fun <T> stateFlow(t: T) = MutableStateFlow(t)
}