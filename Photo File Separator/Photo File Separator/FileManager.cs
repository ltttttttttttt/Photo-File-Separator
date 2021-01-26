using System;
using System.Collections;
using System.Collections.Generic;
using System.Drawing;
using System.IO;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;
using System.Windows.Forms;
using 落地页测试代码;

namespace Photo_File_Separator
{
    class FileManager
    {
        private static int id = 0;

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
                if (file.LastIndexOf(".rar") == file.Length - 4)
                {
                    MessageBox.Show("暂不支持rar压缩包");
                }
                else if (file.LastIndexOf(".zip") == file.Length - 4)
                {
                    String cachePath = "cache\\" + id + "\\";
                    id++;
                    FileHelper.checkDir(cachePath);
                    ZipUtil.UnZip(file, cachePath);
                    move(cachePath, config);
                    FileHelper.DeleteDirAndFile(cachePath);
                    //如果删除了当前文件夹后,cache文件夹无内容,就删除cache文件夹
                    String[] ss = FileHelper.getDirAndFiles("cache\\");
                    if (ss == null || ss.Length == 0)
                    {
                        FileHelper.DeleteDir("cache");
                    }
                }
                else if (file.LastIndexOf(".7z") == file.Length - 3)
                {
                    MessageBox.Show("暂不支持7z压缩包");
                }
                else
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
            String dir = "";
            if (file.IndexOf("@2x") >= 0)
            {
                name = name.Replace("@2x", "");
                dir = "\\xhdpi\\";
            }
            else if (file.IndexOf("@3x") >= 0)
            {
                name = name.Replace("@3x", "");
                dir = "\\xxhdpi\\";
            }
            else if (file.IndexOf("xxxhdpi") >= 0)
            {
                name = name.Replace("xxxhdpi", "");
                dir = "\\xxxhdpi\\";
            }
            else if (file.IndexOf("xxhdpi") >= 0)
            {
                name = name.Replace("xxhdpi", "");
                dir = "\\xxhdpi\\";
            }
            else if (file.IndexOf("xhdpi") >= 0)
            {
                name = name.Replace("xhdpi", "");
                dir = "\\xhdpi\\";
            }
            else if (file.IndexOf("hdpi") >= 0)
            {
                name = name.Replace("hdpi", "");
                dir = "\\hdpi\\";
            }
            else if (fileinfo.DirectoryName.IndexOf("xxxhdpi") >= 0)
            {
                dir = "\\xxxhdpi\\";
            }
            else if (fileinfo.DirectoryName.IndexOf("xxhdpi") >= 0)
            {
                dir = "\\xxhdpi\\";
            }
            else if (fileinfo.DirectoryName.IndexOf("xhdpi") >= 0)
            {
                dir = "\\xhdpi\\";
            }
            else if (fileinfo.DirectoryName.IndexOf("hdpi") >= 0)
            {
                dir = "\\hdpi\\";
            }
            else
            {
                dir = "\\其他\\";
            }
            if (config.oneName.Length == 0)
            {
                //不是统一设置命名
                //去掉前缀
                if (name.IndexOf(config.removeFirst) == 0)
                {
                    name = name.Substring(config.removeFirst.Length, name.Length - config.removeFirst.Length);
                }
                //去掉后缀
                int lastPosition = name.LastIndexOf(config.removeLast);
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
            copy(file, config.copyToDir, dir + name, config);
        }

        //检查,如果有相同的则移动到新的文件夹
        //from  之前的全路径    C\\b\\a.jpg
        //toDir  要移动到的父目录    C\\b
        //toName  要移动到的详细目录/文件名    \\xxhdpi\\a.jpg
        public static void copy(String from, String toDir, String toName, FileMoveConfig config)
        {
            //检测如果有非小英文,数字,下划线,就把其设置为下划线
            int index1 = toName.LastIndexOf(@"\");
            int index2 = toName.LastIndexOf(".");
            String trueName;
            if (index2 >= 0)
            {
                trueName = toName.Substring(index1 + 1, index2 - index1 - 1);
            }
            else
            {
                trueName = toName.Substring(index1);
            }
            //大写要转成小写
            trueName = trueName.ToLower();
            trueName = Regex.Replace(trueName, "[^a-z0-9_]", "_");
            if (trueName.Length == 0 || (trueName[0] >= '0' && trueName[0] <= '9'))
            {
                trueName = "_" + trueName;
            }
            if (index2 >= 0)
            {
                toName = toName.Substring(0, index1) + "\\" + trueName + toName.Substring(index2);
            }
            else
            {
                toName = toName.Substring(0, index1) + "\\" + trueName;
            }
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
            if (config.isToWebP)
            {
                toName = toName.Substring(0, toName.IndexOf('.') - 1) + ".webp";
                imageChangeToWebp(from, toDir + toName, config.webpValue);
            }
            else
            {
                FileHelper.copy(from, toDir + toName);
            }
        }

        //将img(jpg或png)转换为webp,三方库参考: https://www.nuget.org/packages/Imazen.WebP/    https://developer.aliyun.com/article/678410
        public static void imageChangeToWebp(String imagePath, String webpPath, int webpValue)
        {
            //读取webp图片
            //Byte[] bs = System.IO.File.ReadAllBytes("指定的WebP图片");
            // Bitmap img =new Imazen.WebP.SimpleDecoder().DecodeFromBytes(bs, bs.Length);

            //将普通图片转为webp图片
            Image pic = Image.FromFile(imagePath);
            //WebP只支持 Format32bppArgb 和 Format32bppRgb 两种像素格式,所以有时候需要改码，重绘一个图像
            Bitmap bmp = new Bitmap(pic.Width, pic.Height, System.Drawing.Imaging.PixelFormat.Format32bppArgb);
            // 将图片重绘到新画布
            Graphics g = Graphics.FromImage(bmp);
            g.DrawImage(pic, 0, 0, pic.Width, pic.Height);
            pic.Clone();
            // 转码并保存文件
            System.IO.FileStream fs = System.IO.File.Create(webpPath);
            new Imazen.WebP.SimpleEncoder().Encode(bmp, fs, webpValue);
            fs.Close();
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
