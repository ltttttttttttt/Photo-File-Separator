package util

import java.awt.Component
import java.io.File
import javax.swing.JFileChooser


/**
 * creator: lt  2021/4/16  lt.dygzs@qq.com
 * effect : 选择文件地址
 * warning:
 */
object SelectFileUtil {
    fun selectSignFile(): File? {
        val jfc = JFileChooser() //新建一个选择文件夹的类型
        if (jfc.showOpenDialog(object : Component() {}) == JFileChooser.APPROVE_OPTION) { //开始选择路径
            return jfc.selectedFile
        }
        return null
    }
}