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

        //开始移动文件,单个文件的复制操作,主要操作函数
        public static void moveFile(String file, FileMoveConfig config)
        {
            //检查父文件夹存不存在
            FileHelper.checkDir(config.copyToDir);
            FileInfo fileinfo = new FileInfo(file);
            //文件名字和后缀
            String name = fileinfo.Name;
            if (config.oneName.Length == 0)
            {
                //不是统一设置命名
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
                //增加前缀
                name = config.addFirst + name;
                //增加后缀
                if (config.addLast.Length > 0)
                {
                    int index = name.LastIndexOf('.');
                    name = name.Substring(0, index) + config.addLast + name.Substring(index);
                }
            }
            else
            {
                //如果是统一设置命名
                name = config.oneName + Path.GetExtension(file);
            }
            if (file.IndexOf("@2x") >= 0)
            {
                name = name.Replace("@2x", "");
                copy(file, config.copyToDir, "\\xhdpi\\" + name, config);
            }
            else if (file.IndexOf("@3x") >= 0)
            {
                name = name.Replace("@3x", "");
                copy(file, config.copyToDir, "\\xxhdpi\\" + name, config);
            }
            else if (file.IndexOf("xxxhdpi") >= 0)
            {
                name = name.Replace("xxxhdpi", "");
                copy(file, config.copyToDir, "\\xxxhdpi\\" + name, config);
            }
            else if (file.IndexOf("xxhdpi") >= 0)
            {
                name = name.Replace("xxhdpi", "");
                copy(file, config.copyToDir, "\\xxhdpi\\" + name, config);
            }
            else if (file.IndexOf("xhdpi") >= 0)
            {
                name = name.Replace("xhdpi", "");
                copy(file, config.copyToDir, "\\xhdpi\\" + name, config);
            }
            else if (file.IndexOf("hdpi") >= 0)
            {
                name = name.Replace("hdpi", "");
                copy(file, config.copyToDir, "\\hdpi\\" + name, config);
            }
            else if (fileinfo.DirectoryName.IndexOf("xxxhdpi") >= 0)
            {
                name = name.Replace("xxxhdpi", "");
                copy(file, config.copyToDir, "\\xxxhdpi\\" + name, config);
            }
            else if (fileinfo.DirectoryName.IndexOf("xxhdpi") >= 0)
            {
                name = name.Replace("xxhdpi", "");
                copy(file, config.copyToDir, "\\xxhdpi\\" + name, config);
            }
            else if (fileinfo.DirectoryName.IndexOf("xhdpi") >= 0)
            {
                name = name.Replace("xhdpi", "");
                copy(file, config.copyToDir, "\\xhdpi\\" + name, config);
            }
            else if (fileinfo.DirectoryName.IndexOf("hdpi") >= 0)
            {
                name = name.Replace("hdpi", "");
                copy(file, config.copyToDir, "\\hdpi\\" + name, config);
            }
            else
            {
                copy(file, config.copyToDir, "\\其他\\" + name, config);
            }

        }

        //检查,如果有相同的则移动到新的文件夹
        //from  之前的全路径C\\a.jpg
        //toDir  要移动到的父目录C\\
        //toName  要移动到的详细目录/文件名xxhdpi\\a.jpg
        public static void copy(String from, String toDir, String toName, FileMoveConfig config)
        {
            //增加图片上层文件夹的类型
            toName = "\\" + config.imgDirType + "-" + toName.Substring(1);
            if (FileHelper.IsExistFile(toDir + toName))
            {
                //发现了重复,重复策略:0尾数增加,1复制到新文件夹中,2忽略,3覆盖
                switch (config.repeatType)
                {
                    case 0:
                        //获取最后的数字
                        int index = toName.LastIndexOf('.');
                        String first = toName.Substring(0, index);
                        String last = toName.Substring(index);
                        String lastNumber = "";
                        index = first.Length - 1;
                        while (index >= 0)
                        {
                            char c = first[index];
                            index--;
                            if (c >= '0' && c <= '9')
                                lastNumber = c + lastNumber;
                            else
                                break;
                        }
                        String number = System.Text.RegularExpressions.Regex.Replace(lastNumber, @"[^0-9]", "");
                        bool isNotHaveNumber = false;
                        if (number.Length == 0)
                        {
                            number = "0";
                            isNotHaveNumber = true;
                        }
                        int numberLength = number.Length;
                        if (isNotHaveNumber)
                            numberLength = 0;
                        number = (Int64.Parse(number) + 1).ToString();
                        toName = first.Substring(0, first.Length - numberLength) + number + last;
                        while (FileHelper.IsExistFile(toDir + toName))
                        {
                            number = Int64.Parse(number) + 1 + "";
                            toName = first.Substring(0, first.Length - numberLength) + number + last;
                        }
                        break;
                    case 1:
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
                        MessageBox.Show(toName + "    重复,复制到了另外的文件夹    " + toDir + toName);
                        break;
                    case 2:
                        MessageBox.Show(toName + "    被忽略了");
                        return;
                    case 3:
                        //如果不是统一命名才提醒
                        if (config.oneName.Length <= 0)
                            MessageBox.Show(toName + "    被覆盖了");
                        File.Delete(toDir + toName);
                        break;
                }
            }
            //检查父文件夹是否存在
            FileHelper.checkDir(new FileInfo(toDir + toName).Directory.ToString());
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
