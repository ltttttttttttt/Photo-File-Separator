import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.ImageBitmap
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
    val isSelectWebP = stateFlow(true)//是否选中压缩webp
    val webPRatio = stateFlow(75F)//webp压缩率
    val photoType = stateFlow("mipmap")//图片类型,如果为空字符串就是无,其他是自定义的
    val repeatStrategy = stateFlow(0)//图片重复的策略:0尾数+n 1复制到新文件夹 2忽略 3覆盖
    val isAutoCopyId = stateFlow(true)//是否自动复制上次分离的图片图片的id todo 后续加上使用xml或code的id
    val logs = mutableStateListOf<Pair<ImageBitmap, String>>()//日志数据
}