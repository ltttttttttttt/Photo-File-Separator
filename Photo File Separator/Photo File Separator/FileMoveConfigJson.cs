using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Photo_File_Separator
{
    [Serializable]
    public class FileMoveConfigJson
    {
        public LinkedList<String> copyToDirs;//复制到的文件夹
        public String removeLast;//去掉的后缀
        public String removeFirst;//去掉的前缀
        public String oneName;//统一设置名称
        public String addLast;//增加的后缀
        public String addFirst;//增加的前缀
        public String imgDirType;//图片上层文件夹的类型,比如mipmap
        public int repeatType = 0;//重复策略:0尾数增加,1复制到新文件夹中,2忽略,3覆盖
        public bool isToWebP = false;//是否压缩至webp格式
        public int webpValue = 75;//webp的压缩率
        public bool isAutoCopyImgId = false;//是否自动复制图片的id,来让安卓更易用
    }
}
