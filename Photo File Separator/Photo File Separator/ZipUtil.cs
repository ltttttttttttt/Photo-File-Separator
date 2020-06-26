using System;
using System.Text;
using System.Collections;
using System.IO;
using System.Diagnostics;
using System.Runtime.Serialization.Formatters.Binary;
using System.Data;
using 落地页测试代码;
using ICSharpCode.SharpZipLib.Zip;

namespace Photo_File_Separator
{
    class ZipUtil
    {

        public static void UnZip(String inPath, String outDir)
        {
            ZipInputStream s = new ZipInputStream(File.OpenRead(inPath));
            ZipEntry theEntry;
            while ((theEntry = s.GetNextEntry()) != null)
            {
                string directoryName = Path.GetDirectoryName(outDir);
                string fileName = Path.GetFileName(theEntry.Name);
                //生成解压目录   
                Directory.CreateDirectory(directoryName);
                if (fileName != String.Empty)
                {
                    FileHelper.checkDir(new FileInfo(outDir + theEntry.Name).Directory.ToString());
                    //解压文件到指定的目录   
                    FileStream streamWriter = File.Create(outDir + theEntry.Name);
                    int size = 2048;
                    byte[] data = new byte[2048];
                    while (true)
                    {
                        size = s.Read(data, 0, data.Length);
                        if (size > 0)
                        {
                            streamWriter.Write(data, 0, size);
                        }
                        else
                        {
                            break;
                        }
                    }
                    streamWriter.Close();
                }
            }
            s.Close();
        }
    }
}