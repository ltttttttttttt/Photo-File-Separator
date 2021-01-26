using Microsoft.Win32;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Diagnostics;
using System.Drawing;
using System.Drawing.Drawing2D;
using System.IO;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using 落地页测试代码;

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
            else if (rbEmpty.Checked)
                config.imgDirType = "";
            else if (rbDiy.Checked)
                config.imgDirType = etDiy.Text;
            if (radioButton2.Checked)
                config.repeatType = 0;
            else if (radioButton1.Checked)
                config.repeatType = 1;
            else if (radioButton3.Checked)
                config.repeatType = 2;
            else if (radioButton4.Checked)
                config.repeatType = 3;
            config.isToWebP = cbWebp.Checked;
            config.webpValue = trackBar1.Value;
            //保存config到本地
            config.saveConfig();
            config.form1 = this;
            return config;
        }

        //从本地获取配置
        private void getConfigFromNative()
        {
            //初始化默认路径
            String defaultDir = System.AppDomain.CurrentDomain.BaseDirectory;
            textBox1.Text = defaultDir.Substring(0, defaultDir.Length - 1);
            //尝试从本地获取配置,获取不到就使用默认配置
            FileMoveConfigJson j = FileMoveConfig.getJsonConfig();
            if (j == null) return;
            textBox2.Text = j.removeLast;
            textBox3.Text = j.removeFirst;
            textBox4.Text = j.oneName;
            textBox6.Text = j.addLast;
            textBox5.Text = j.addFirst;
            String imgDirType = j.imgDirType;
            if ("mipmap" == imgDirType)
                rbMipmap.Checked = true;
            else if ("drawable" == imgDirType)
                rbDrawable.Checked = true;
            else if ("" == imgDirType)
                rbEmpty.Checked = true;
            else
            {
                rbDiy.Checked = true;
                etDiy.Text = imgDirType;
            }
            int repeatType = j.repeatType;
            switch (repeatType)
            {
                case 0:
                    radioButton2.Checked = true;
                    break;
                case 1:
                    radioButton1.Checked = true;
                    break;
                case 2:
                    radioButton3.Checked = true;
                    break;
                case 3:
                    radioButton4.Checked = true;
                    break;
            }
            if (j.copyToDirs.First != null)
                textBox1.Text = j.copyToDirs.First.Value;
            cbWebp.Checked = j.isToWebP;
            tvWebp.Enabled = j.isToWebP;
            trackBar1.Enabled = j.isToWebP;
            trackBar1.Value = j.webpValue;
            tvWebp.Text = "压缩率" + j.webpValue + "(推荐75)";
            textBox1.DataSource = j.copyToDirs.ToList();
        }

        //emmm 彩蛋
        private void button3_Click(object sender, EventArgs e)
        {
            MessageBox.Show("竟然被你发现了这个彩蛋!\n如果发现bug的话可以发到 lt.dygzs@qq.com 这个邮箱哦!\n或者到文章内评论 https://blog.csdn.net/qq_33505109/article/details/99563822");
            OpenUrlUtil.OpenBrowserUrl("https://blog.csdn.net/qq_33505109/article/details/99563822");
        }

        private void radioButton4_CheckedChanged(object sender, EventArgs e)
        {
            RadioButton rb = (RadioButton)sender;
            if (rb.Checked)
            {
                MessageBox.Show("警告:该配置比较危险,容易丢失文件,请谨慎使用");
                rb.BackColor = getRedColor();
            }
            else
            {
                rb.BackColor = Color.FromKnownColor(System.Drawing.KnownColor.Control);
            }
        }

        private void Form1_FormClosed(object sender, FormClosedEventArgs e)
        {
            getConfig();
        }

        private void trackBar1_Scroll(object sender, EventArgs e)
        {
            TrackBar tb = (TrackBar)sender;
            tvWebp.Text = "压缩率" + tb.Value + "(推荐75)";
            if (tb.Value >= 95)
            {
                tb.BackColor = getRedColor();
            }
            else
            {
                tb.BackColor = Color.FromKnownColor(System.Drawing.KnownColor.Control);
            }
        }

        private void cbWebp_CheckedChanged(object sender, EventArgs e)
        {
            if (((CheckBox)sender).Checked)
            {
                tvWebp.Enabled = true;
                trackBar1.Enabled = true;
            }
            else
            {
                tvWebp.Enabled = false;
                trackBar1.Enabled = false;
            }
        }

        //打印日志
        public void printLog(String from, String toName, FileMoveConfig config)
        {
            if (rvLog.SmallImageList == null)
            {
                rvLog.SmallImageList = new ImageList();
            }
            rvLog.SmallImageList.Images.Add(ImageUtil.getImageFromFile(from));
            rvLog.Items.Add(toName, rvLog.SmallImageList.Images.Count - 1);
            rvLog.EnsureVisible(rvLog.Items.Count - 1);
        }

        private void textBox3_TextChanged(object sender, EventArgs e)
        {
            TextBox tb = (TextBox)sender;
            if (tb.Text == null || tb.Text.Length == 0)
            {
                tb.BackColor = Color.FromKnownColor(System.Drawing.KnownColor.Window);
            }
            else
            {
                tb.BackColor = getRedColor();
            }
        }

        //获取警告红
        private Color getRedColor()
        {
            return Color.FromArgb(0xef, 0x7a, 0x82);
        }
    }
}
