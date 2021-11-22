package util.swing

import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.window.FrameWindowScope
import java.awt.datatransfer.DataFlavor
import java.awt.dnd.*
import java.awt.event.WindowEvent
import java.awt.event.WindowListener
import java.io.File

/**
 * creator: lt  2021/11/22  lt.dygzs@qq.com
 * effect :
 * warning:
 */

inline fun ComposeWindow.addWindowClosingListener(crossinline closing: () -> Unit) {
    addWindowListener(object : WindowListener {
        override fun windowOpened(e: WindowEvent?) {
        }

        override fun windowClosing(e: WindowEvent?) {
            closing()
        }

        override fun windowClosed(e: WindowEvent?) {
        }

        override fun windowIconified(e: WindowEvent?) {
        }

        override fun windowDeiconified(e: WindowEvent?) {
        }

        override fun windowActivated(e: WindowEvent?) {
        }

        override fun windowDeactivated(e: WindowEvent?) {
        }

    })
}

/**
 * 使用swing监听在界面上拖动的事件,参考: https://blog.csdn.net/Primary_wind/article/details/7757688
 */
fun FrameWindowScope.dropTarget(onFilePaths: (List<String>) -> Unit) =
    DropTarget(window, object : DropTargetListener {
        override fun dragEnter(dtde: DropTargetDragEvent?) {}

        override fun dragOver(dtde: DropTargetDragEvent?) {}

        override fun dropActionChanged(dtde: DropTargetDragEvent?) {}

        override fun dragExit(dte: DropTargetEvent?) {}

        fun isDropAcceptable(event: DropTargetDropEvent): Boolean {
            // usually, you check the available data flavors here
            // in this program, we accept all flavors
            return event.dropAction and DnDConstants.ACTION_COPY_OR_MOVE !== 0
        }

        //监听拖动进来的文件
        override fun drop(event: DropTargetDropEvent?) {
            // TODO by lt  还需要处理一下拖动的范围
            event ?: return
            if (!isDropAcceptable(event)) {
                event.rejectDrop()
                return
            }
            event.acceptDrop(DnDConstants.ACTION_COPY)
            val trans = event.transferable
            onFilePaths(
                try {
                    (trans.transferDataFlavors
                        .filter { it.equals(DataFlavor.javaFileListFlavor) }
                        .map(trans::getTransferData)
                            as? List<List<File?>?>
                            )?.filterNotNull()
                        ?.flatten()
                        ?.filterNotNull()
                        ?.map(File::getAbsolutePath)
                } catch (e: Exception) {
                    e.printStackTrace()
                    null
                } ?: return
            )
        }
    })