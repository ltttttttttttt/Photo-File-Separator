using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using 落地页测试代码;

namespace Photo_File_Separator
{
    class FileManager
    {
        //开始全部的移动
        public static void actionMove(Array array, FileMoveConfig config)
        {
            if (array == null || array.Length == 0)
                return;
            foreach (String s in array)
            {
                move(s, config);
            }
        }

        //开始单个移动
        public static void move(String file, FileMoveConfig config)
        {
            if (FileHelper.IsExistFile(file))
            {
                moveFile(file, config);
            }
            else if (FileHelper.IsExistDirectory(file))
            {
                moveDir(file, config);
            }
        }

        //开始移动文件
        public static void moveFile(String file, FileMoveConfig config)
        {
            //检查父文件夹存不存在
            FileHelper.checkDir(config.copyToDir);
            FileHelper.checkDir(config.copyToDir + "\\xhdpi");
            FileHelper.checkDir(config.copyToDir + "\\xxhdpi");
            FileInfo fileinfo = new FileInfo(file);
            String name = fileinfo.Name;
            if (config.oneName.Length == 0)
            {
                //去掉前缀
                if (name.IndexOf(config.removeFirst) == 0)
                {
                    name = name.Substring(config.removeFirst.Length, name.Length - config.removeFirst.Length);
                }
                //去掉后缀
                int lastPosition = Path.GetFileName(file).ToString().LastIndexOf(config.removeLast);
                if (config.removeLast.Length != 0 && lastPosition > 0)
                {
                    String nameCopy = name;
                    name = nameCopy.Substring(0, lastPosition) + nameCopy.Substring(lastPosition + config.removeLast.Length, nameCopy.Length - lastPosition - config.removeLast.Length);
                }
            }
            else
            {
                name = config.oneName + Path.GetExtension(file);
            }
            if (file.IndexOf("@2x") >= 0)
            {
                name = name.Replace("@2x", "");
                copy(file, config.copyToDir, "\\xhdpi\\" + name);
            }
            else if (file.IndexOf("@3x") >= 0)
            {
                name = name.Replace("@3x", "");
                copy(file, config.copyToDir, "\\xxhdpi\\" + name);
            }
            else if (file.IndexOf("xhdpi") >= 0)
            {
                name = name.Replace("xhdpi", "");
                copy(file, config.copyToDir, "\\xhdpi\\" + name);
            }
            else if (file.IndexOf("xxhdpi") >= 0)
            {
                name = name.Replace("xxhdpi", "");
                copy(file, config.copyToDir, "\\xxhdpi\\" + name);
            }
            else if (file.IndexOf("hdpi") >= 0)
            {
                FileHelper.checkDir(config.copyToDir + "\\hdpi");
                name = name.Replace("hdpi", "");
                copy(file, config.copyToDir, "\\hdpi\\" + name);
            }
            else if (file.IndexOf("xxxhdpi") >= 0)
            {
                FileHelper.checkDir(config.copyToDir + "\\xxxhdpi");
                name = name.Replace("xxxhdpi", "");
                copy(file, config.copyToDir, "\\xxxhdpi\\" + name);
            }
        }

        //检查,如果有相同的则移动到新的文件夹
        public static void copy(String from, String toDir, String toName)
        {
            if (FileHelper.IsExistFile(toDir + toName))
            {
                toDir += "\\重复的文件_请检查后自行操作_lt";
                while (true)
                {
                    if (FileHelper.IsExistFile(toDir + toName))
                    {
                        toDir += "_lt";
                    }
                    else
                    {
                        break;
                    }
                }
                FileHelper.checkDir(new FileInfo(toDir + toName).Directory.ToString());
            }
            FileHelper.copy(from, toDir + toName);
        }

        //开始移动文件夹
        public static void moveDir(String dir, FileMoveConfig config)
        {
            string[] srr = FileHelper.GetFileNames(dir, "*", true);
            //循环
            foreach (string s in srr)
            {
                move(s, config);
            }
        }
    }
}
