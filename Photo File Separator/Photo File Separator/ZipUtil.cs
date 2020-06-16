using System;
using System.Collections;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;

namespace Photo_File_Separator
{
    class ZipUtil
    {
        private static string RunCmd(string command)
        {
            //实例一个Process类，启动一个独立进程
            Process p = new Process();

            p.StartInfo.FileName = "cmd.exe";
            p.StartInfo.Arguments = "/c " + command;    //设定程式执行参数
            p.StartInfo.UseShellExecute = false;        //关闭Shell的使用
            p.StartInfo.RedirectStandardInput = true;   //重定向标准输入
            p.StartInfo.RedirectStandardOutput = true;  //重定向标准输出
            p.StartInfo.RedirectStandardError = true;   //重定向错误输出
            p.StartInfo.CreateNoWindow = true;          //设置不显示窗口

            p.Start();   //启动
            string retstr = p.StandardOutput.ReadToEnd();
            p.WaitForExit();
            p.Close();
            return retstr;    //从输出流取得命令执行结果
        }
        /// <summary>
        /// 获得RAR信息
        /// </summary>
        /// <param name="rar_path">包文件路径</param>
        /// <returns></returns>
        public static Hashtable CheckRar(string rar_path)
        {
            string strcmd1 = string.Format("rar lb {0} ", rar_path);
            string outcmd_string1 = RunCmd(strcmd1).Replace("\r\n", "|");
            outcmd_string1 = outcmd_string1.Remove(outcmd_string1.LastIndexOf('|'));

            string strcmd2 = string.Format("rar l {0} ", rar_path);
            string outcmd_string2 = RunCmd(strcmd2);

            string[] strfilenames = outcmd_string1.Split('|');
            int filecount = strfilenames.Length;
            string[] strfilesizes = new string[filecount];

            for (int i = 0; i < filecount; i++)
            {
                string filesize = outcmd_string2.Substring(outcmd_string2.IndexOf(strfilenames[i]) + strfilenames[i].Length).Trim();
                filesize = filesize.Substring(0, filesize.IndexOf(" "));
                strfilesizes[i] = filesize;
            }

            Hashtable ht_rar = new Hashtable();
            for (int i = 0; i < filecount; i++)
            {
                ht_rar.Add(strfilenames[i], strfilesizes[i]);
            }

            return ht_rar;
        }

    }
}
