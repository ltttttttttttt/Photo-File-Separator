package util

import java.awt.Component
import java.io.File
import javax.swing.JFileChooser


/**
 * creator: lt  2021/4/16  lt.dygzs@qq.com
 * effect : 选择文件地址
 * warning: 参考: https://blog.csdn.net/weixin_42294984/article/details/82707409
 */
object SelectFileUtil {

    /**
     * 选择一个文件夹
     */
    fun selectSignDir(): File? {
        val jfc = JFileChooser("C://") //新建一个选择文件夹的类型
        jfc.fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
        if (jfc.showOpenDialog(object : Component() {}) == JFileChooser.APPROVE_OPTION) { //开始选择路径
            return jfc.selectedFile
        }
        return null
    }
}