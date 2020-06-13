using Microsoft.Win32;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Diagnostics;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;

namespace Photo_File_Separator
{
    public partial class Form1 : Form
    {
        long time = 0;
        public Form1()
        {
            InitializeComponent();
        }

        private void Form1_Load(object sender, EventArgs e)
        {
            getConfigFromNative();
        }

        private void button1_DragDrop(object sender, DragEventArgs e)
        {
            //去重
            if (((DateTime.Now.ToUniversalTime().Ticks - 621355968000000000) / 10000) - time < 1500)
                return;
            time = (DateTime.Now.ToUniversalTime().Ticks - 621355968000000000) / 10000;
            //拖动中
            Array files = (System.Array)e.Data.GetData(DataFormats.FileDrop);
            FileManager.actionMove(files, getConfig());
        }

        private void button2_Click(object sender, EventArgs e)
        {
            //选择输出位置
            if (folderBrowserDialog1.ShowDialog() == DialogResult.OK)
            {
                textBox1.Text = folderBrowserDialog1.SelectedPath.ToString();
            }
        }

        private void button1_Click(object sender, EventArgs e)
        {
            //开始执行单个文件夹
            if (folderBrowserDialog1.ShowDialog() == DialogResult.OK)
            {
                FileManager.move(folderBrowserDialog1.SelectedPath.ToString(), getConfig());
            }
        }

        //获取配置
        private FileMoveConfig getConfig()
        {
            FileMoveConfig config = new FileMoveConfig();
            config.copyToDir = textBox1.Text;
            config.removeFirst = textBox3.Text;
            config.removeLast = textBox2.Text;
            config.oneName = textBox4.Text;
            config.addFirst = textBox5.Text;
            config.addLast = textBox6.Text;
            if (rbMipmap.Checked)
                config.imgDirType = "mipmap";
            else if (rbDrawable.Checked)
                config.imgDirType = "drawable";
            if (radioButton2.Checked)
                config.repeatType = 0;
            else if (radioButton1.Checked)
                config.repeatType = 1;
            else if (radioButton3.Checked)
                config.repeatType = 2;
            else if (radioButton4.Checked)
                config.repeatType = 3;
            //TODO 保存config到本地


            return config;
        }

        //从本地获取配置
        private void getConfigFromNative()
        {
            //TODO 尝试从本地获取配置,获取不到就使用默认配置


            //初始化默认路径
            String defaultDir = System.AppDomain.CurrentDomain.BaseDirectory;
            textBox1.Text = defaultDir.Substring(0, defaultDir.Length - 1);
        }

        //emmm 彩蛋
        private void button3_Click(object sender, EventArgs e)
        {
            MessageBox.Show("竟然被你发现了这个彩蛋!\n如果发现bug的话可以发到 lt.dygzs@qq.com 这个邮箱哦!\n或者到文章内评论 https://blog.csdn.net/qq_33505109/article/details/99563822");
            OpenUrlUtil.OpenBrowserUrl("https://blog.csdn.net/qq_33505109/article/details/99563822");
        }

        private void radioButton4_CheckedChanged(object sender, EventArgs e)
        {
            if (((RadioButton)sender).Checked)
                MessageBox.Show("警告:该配置比较危险,容易丢失文件,请谨慎使用");
        }
    }
}
