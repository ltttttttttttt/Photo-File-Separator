import base.BaseViewModel

/**
 * creator: lt  2021/11/18  lt.dygzs@qq.com
 * effect :
 * warning:
 */
class MainVM : BaseViewModel {
    val outputDirPath = stateFlow("")//输出文件夹
    val removePrefix = stateFlow("")//去掉前缀
    val removeSuffix = stateFlow("")//去掉后缀
    val addPrefix = stateFlow("")//增加前缀
    val addSuffix = stateFlow("")//增加后缀
    val unifiedNaming = stateFlow("")//统一命名
}