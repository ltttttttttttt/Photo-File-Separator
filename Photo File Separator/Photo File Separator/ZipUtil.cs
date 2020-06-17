using System;
using System.Text;
using System.Collections;
using System.IO;
using System.Diagnostics;
using System.Runtime.Serialization.Formatters.Binary;
using System.Data;
using ICSharpCode.SharpZipLib.BZip2;
using ICSharpCode.SharpZipLib.Zip;
using ICSharpCode.SharpZipLib.Zip.Compression;
using ICSharpCode.SharpZipLib.Zip.Compression.Streams;
using ICSharpCode.SharpZipLib.GZip;
using ICSharpCode.SharpZipLib.Checksums;
using 落地页测试代码;

namespace Photo_File_Separator
{
    class ZipUtil
    {
        public static void ZipFile(string FileToZip, string ZipedFile, int CompressionLevel, int BlockSize)
        {
            //如果文件没有找到，则报错   
            if (!System.IO.File.Exists(FileToZip))
            {
                throw new System.IO.FileNotFoundException("The specified file " + FileToZip + " could not be found. Zipping aborderd");
            }
            System.IO.FileStream StreamToZip = new System.IO.FileStream(FileToZip, System.IO.FileMode.Open, System.IO.FileAccess.Read);
            System.IO.FileStream ZipFile = System.IO.File.Create(ZipedFile);
            ZipOutputStream ZipStream = new ZipOutputStream(ZipFile);
            ZipEntry ZipEntry = new ZipEntry("ZippedFile");
            ZipStream.PutNextEntry(ZipEntry);
            ZipStream.SetLevel(CompressionLevel);
            byte[] buffer = new byte[BlockSize];
            System.Int32 size = StreamToZip.Read(buffer, 0, buffer.Length);
            ZipStream.Write(buffer, 0, size);
            try
            {
                while (size < StreamToZip.Length)
                {
                    int sizeRead = StreamToZip.Read(buffer, 0, buffer.Length);
                    ZipStream.Write(buffer, 0, sizeRead);
                    size += sizeRead;
                }
            }
            catch (System.Exception ex)
            {
                throw ex;
            }
            ZipStream.Finish();
            ZipStream.Close();
            StreamToZip.Close();
        }
        public static void ZipFileMain(String inPath, String outPath)
        {
            string[] filenames = Directory.GetFiles(inPath);
            Crc32 Crc = new Crc32();
            ZipOutputStream s = new ZipOutputStream(File.Create(outPath));
            s.SetLevel(6); // 0 - store only to 9 - means best compression   
            foreach (string file in filenames)
            {
                //打开压缩文件   
                FileStream fs = File.OpenRead(file);
                byte[] buffer = new byte[fs.Length];
                fs.Read(buffer, 0, buffer.Length);
                ZipEntry entry = new ZipEntry(file);
                entry.DateTime = DateTime.Now;
                // set Size and the crc, because the information   
                // about the size and crc should be stored in the header   
                // if it is not set it is automatically written in the footer.   
                // (in this case size == crc == -1 in the header)   
                // Some ZIP programs have problems with zip files that don"t store   
                // the size and crc in the header.   
                entry.Size = fs.Length;
                fs.Close();
                Crc.Reset();
                Crc.Update(buffer);

                entry.Crc = Crc.Value;
                s.PutNextEntry(entry);
                s.Write(buffer, 0, buffer.Length);
            }
            s.Finish();
            s.Close();
        }

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