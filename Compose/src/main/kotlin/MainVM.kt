import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.ImageBitmap
import base.BaseViewModel
import java.io.*

/**
 * creator: lt  2021/11/18  lt.dygzs@qq.com
 * effect :
 * warning:
 */
class MainVM private constructor(obj: Obj?) : BaseViewModel {
    val outputDirPath = stateFlow(obj?.outputDirPath ?: dataFile.parent)//输出文件夹
    val removePrefix = stateFlow(obj?.removePrefix ?: "")//去掉前缀
    val removeSuffix = stateFlow(obj?.removeSuffix ?: "")//去掉后缀
    val addPrefix = stateFlow(obj?.addPrefix ?: "")//增加前缀
    val addSuffix = stateFlow(obj?.addSuffix ?: "")//增加后缀
    val unifiedNaming = stateFlow(obj?.unifiedNaming ?: "")//统一命名
    val isSelectWebP = stateFlow(obj?.isSelectWebP ?: true)//是否选中压缩webp
    val webPRatio = stateFlow(obj?.webPRatio ?: 75F)//webp压缩率
    val photoType = stateFlow(obj?.photoType ?: "mipmap")//图片类型,如果为空字符串就是无,其他是自定义的
    val repeatStrategy = stateFlow(obj?.repeatStrategy ?: 0)//图片重复的策略:0尾数+n 1复制到新文件夹 2忽略 3覆盖
    val isAutoCopyId =
        stateFlow(obj?.isAutoCopyId ?: true)//是否自动复制上次分离的图片图片的id todo 后续加上使用xml或code的id
    val logs = mutableStateListOf<Pair<ImageBitmap, String>>()//日志数据

    /**
     * 开始移动
     */
    fun action(path: String) {
        // TODO by lt 2021/11/20 17:04
        println(path)
    }

    /**
     * 将数据保存到本地
     */
    fun saveData() {
        if (dataFile.exists())
            dataFile.delete()
        dataFile.createNewFile()
        ObjectOutputStream(FileOutputStream(dataFile)).use {
            it.writeObject(toObj())
        }
    }

    private class Obj : Serializable {
        var outputDirPath = ""
        var removePrefix = ""
        var removeSuffix = ""
        var addPrefix = ""
        var addSuffix = ""
        var unifiedNaming = ""
        var isSelectWebP = true
        var webPRatio = 75F
        var photoType = "mipmap"
        var repeatStrategy = 0
        var isAutoCopyId = true
    }

    private fun toObj(): Obj {
        val obj = Obj()
        obj.outputDirPath = outputDirPath.value
        obj.removePrefix = removePrefix.value
        obj.removeSuffix = removeSuffix.value
        obj.addPrefix = addPrefix.value
        obj.addSuffix = addSuffix.value
        obj.unifiedNaming = unifiedNaming.value
        obj.unifiedNaming = unifiedNaming.value
        obj.isSelectWebP = isSelectWebP.value
        obj.webPRatio = webPRatio.value
        obj.photoType = photoType.value
        obj.repeatStrategy = repeatStrategy.value
        obj.isAutoCopyId = isAutoCopyId.value
        return obj
    }

    companion object {
        val dataFile = File(File("").absolutePath, "photoConfig.obj")//存放数据的文件

        /**
         * 从本地读取或重新创建MainVM
         */
        fun create(): MainVM {
            if (!dataFile.exists())
                return MainVM(null)
            ObjectInputStream(FileInputStream(dataFile)).use {
                return MainVM(it.readObject() as? Obj ?: return@use)
            }
            return MainVM(null)
        }
    }
}