using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using 落地页测试代码;

namespace Photo_File_Separator {
    class FileMoveConfig {
        public String copyToDir = "";//复制到的文件夹
        public String removeLast = "";//去掉的后缀
        public String removeFirst = "";//去掉的前缀
        public String oneName = "";//统一设置名称
        public String addLast = "";//增加的后缀
        public String addFirst = "";//增加的前缀
        public String imgDirType = "";//图片上层文件夹的类型,比如mipmap
        public int repeatType = 0;//重复策略:0尾数增加,1复制到新文件夹中,2忽略,3覆盖

        public void saveConfig()
        {
            FileMoveConfigJson j = new FileMoveConfigJson();
            j.removeLast = removeLast;
            j.removeFirst = removeFirst;
            j.oneName = oneName;
            j.addLast = addLast;
            j.addFirst = addFirst;
            j.imgDirType = imgDirType;
            j.repeatType = repeatType;
            FileMoveConfigJson nativeJ = getJsonConfig();
            if (nativeJ != null)
            {
                LinkedList<String> list = nativeJ.copyToDirs;
                if (list != null)
                {
                    list.Remove(copyToDir);
                    list.AddFirst(copyToDir);
                }
                else
                {
                    list = new LinkedList<string>();
                    list.AddFirst(copyToDir);
                }
                j.copyToDirs = list;
            }
            else
            {
                LinkedList<string> list = new LinkedList<string>();
                list.AddFirst(copyToDir);
                j.copyToDirs = list;
            }
            StreamWriter sw = new StreamWriter("PhotoFileSeparator.json");
            sw.Write(JsonConvert.SerializeObject(j));
            sw.Close();
            //File.WriteAllText(System.AppDomain.CurrentDomain.BaseDirectory + "PhotoFileSeparator.json", JsonConvert.SerializeObject(j));
        }

        public static FileMoveConfigJson getJsonConfig()
        {
            FileHelper.ExistsFile("PhotoFileSeparator.json");
            String json = File.ReadAllText("PhotoFileSeparator.json");
            FileMoveConfigJson nativeJ = null;
            if (json != null)
                nativeJ = JsonConvert.DeserializeObject<FileMoveConfigJson>(json);
            return nativeJ;
        }
    }
}
