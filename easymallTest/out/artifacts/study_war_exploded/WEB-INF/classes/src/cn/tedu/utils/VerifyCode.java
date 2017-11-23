package cn.tedu.utils;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.Random;

public class VerifyCode {
	private String codes = "23456789abcdefghjkmnopqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ";
	private static String[] fontNames = { "宋体", "华文楷体", "黑体", "微软雅黑",  "楷体_GB2312" };
	private static String text = null;
	public void drawImage(OutputStream output){
		int base = 30;
		int width = 4*base;
		int height = base;


		//1.创建图片缓冲区对象
		BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		//2.获得绘制环境(拿到画笔)
		Graphics2D g2 = (Graphics2D) bi.getGraphics();

		//3.开始画图
		//>设置背景颜色
		g2.setColor(Color.WHITE);
		g2.fillRect(0, 0, width, height);

		//边框
		g2.setColor(Color.GRAY);
		g2.drawRect(0, 0, width-1, height-1);

		StringBuffer sb = new StringBuffer();//用来装载验证码的对象
		//>开始绘制验证码
		for(int i=0; i<4; i++){
			//设置字体
			g2.setFont(new Font(fontNames[getRandom(0, fontNames.length)], Font.BOLD, 22));
			//设置颜色
			g2.setColor(new Color(getRandom(0, 150), getRandom(0, 150), getRandom(0, 150)));

			String code = codes.charAt(getRandom(0, codes.length()))+"";

			//旋转文字
			int theta = getRandom(-45, 45);
			g2.rotate(theta*Math.PI/180, 7+base*i, height-8);
			g2.drawString(code, 7+base*i, height-8);
			g2.rotate(-theta*Math.PI/180, 7+base*i, height-8);

			sb.append(code);

		}

		text = sb.toString();

		//画干扰线
		for(int i=0; i<5; i++){
			//设置颜色
			g2.setColor(new Color(getRandom(0, 150), getRandom(0, 150), getRandom(0, 150)));
			g2.drawLine(getRandom(0, 120), getRandom(0, 30), getRandom(0, 120), getRandom(0, 30));

		}

		//4.将图片保存到指定的输出流中
		try {
			ImageIO.write(bi, "JPEG", output);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}finally{
			//5.释放资源
			g2.dispose();
		}
	}


	/**
	 * 获取验证码

	 * @return
	 */

	public String getCode(){
		return text;
	}
	/*
	 * 获取指定范围的随机数
	 */
	public int getRandom(int start, int end){
		Random random = new Random();
		return random.nextInt(end-start)+start;
	}
}
