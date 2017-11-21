package utils;

/**
 * Created by gao on 2016/8/28.
 */

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import javax.imageio.ImageIO;


public class ValiCode {
    BufferedImage img = null;
    Graphics2D g2 = null;

    private void drawImage() throws Exception {
        int base = 30;
        int width = base * 4;
        int height = base;
        // 1.创建图片缓冲区对象
        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        // 2.得到图片的绘制环境(得到画笔)
        g2 = (Graphics2D) img.getGraphics();
        // 3.开始画图
        // 设置背景颜色
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, width, height);
        // 设置边框
        g2.setColor(Color.BLACK);
        g2.drawRect(0, 0, width - 1, height - 1);

        // 画文字
        // abcde
        String codes = "23456789abcdefghjkmnopqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ";
        // {"宋体", "华文楷体", "黑体", "华文新魏", "华文隶书", "微软雅黑", "楷体_GB2312"}
        String[] fontNames = {"宋体", "华文楷体", "黑体", "微软雅黑", "楷体_GB2312"};
        for (int i = 0; i < 4; i++) {
            String code = codes.charAt(getRandom(0, codes.length())) + "";
            // 获取随机颜色
            Color color = new Color(getRandom(0, 150), getRandom(0, 150),
                    getRandom(0, 150));
            g2.setColor(color);
            String fontName = fontNames[getRandom(0, fontNames.length)];
            g2.setFont(new Font(fontName, Font.BOLD, 20));

            g2.drawString(code, 5 + base * i, base - 7);
        }

        // 画干扰线
        for (int i = 0; i < 5; i++) {
            // 获取随机颜色
            Color color = new Color(getRandom(0, 150), getRandom(0, 150),
                    getRandom(0, 150));
            g2.setColor(color);
            g2.drawLine(getRandom(0, 120), getRandom(0, 30), getRandom(0, 120),
                    getRandom(0, 30));
        }
    }

    /**
     * 获取图片到指定的输出流中
     *
     * @param out
     */
    public void getImage(OutputStream out) {
        try {
            drawImage();
            // 4.保存图片
            ImageIO.write(img, "jpeg", out);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            dispose();
        }

    }

    private void dispose() {
        // 5.释放资源
        g2.dispose();
    }

    private int getRandom(int start, int end) {
        Random random = new Random();
        return random.nextInt(end - start) + start;
    }
}
