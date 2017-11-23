package cn.futures.data.importor.crawler.test;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class test {
	
	/**
	 * 得到图像的主像素值
	 * @param image
	 * @return
	 */
	public BufferedImage getMainPix(String srcImageFile,String dirImageFile){
		// 读取源图像  
        BufferedImage image = null;
		try {
			image = ImageIO.read(new File(srcImageFile));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		int width = image.getWidth();  
        int height = image.getHeight();  
        BufferedImage rgbImageRed = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);//重点，技巧在这个参数BufferedImage.TYPE_BYTE_GRAY  
        for(int i= 0 ; i < width ; i++){  
            for(int j = 0 ; j < height; j++){  
	            int rgb = image.getRGB(i, j);
	            if(rgb == -1 || i==0 || j==0 || i == width-1 || j == height-1){
	            	rgbImageRed.setRGB(i, j, -1);//-1表示白色
	            	continue;
	            }
	            //去孤点，上下左右有三个或者四个为白色的像素点去掉
	            int whiteNum = 0;
	            if(image.getRGB(i-1, j) == -1){
	            	whiteNum++;
	            }
	            if(image.getRGB(i+1, j) == -1){
	            	whiteNum++;
	            }
	            if(image.getRGB(i, j-1) == -1){
	            	whiteNum++;
	            }
	            if(image.getRGB(i, j+1) == -1){
	            	whiteNum++;
	            }
	            if(whiteNum>2){
	            	rgbImageRed.setRGB(i, j, -1);//-1表示白色
	            	continue;
	            }
	            Color c = new Color(rgb);
	            int pixel = c.getRed();
	            if(pixel<150)
	            	rgbImageRed.setRGB(i, j, Color.white.getRGB());
	            else{
		            rgbImageRed.setRGB(i, j, c.red.getRGB());
	            } 
            }  
        } 
        for(int j = 3 ; j < height-3; j++){ 
        	for(int i= 3 ; i < width-3 ; i++){  
        		int rgb = image.getRGB(i, j);
	            Color c = new Color(rgb);
	            if(rgb == -1 && image.getRGB(i, j-1) == -1
	            		&& image.getRGB(i-1, j)== Color.red.getRGB() &&  image.getRGB(i-1, j-1) == Color.red.getRGB()
	            		&& image.getRGB(i+1, j) == Color.red.getRGB() && image.getRGB(i+1, j-1) == Color.red.getRGB()){
	            	rgbImageRed.setRGB(i, j, Color.red.getRGB());
	            } 
	            /*if(rgb == -1 && image.getRGB(i, j-1) == -1&&image.getRGB(i, j-2) == -1
	            		&& image.getRGB(i-1, j)== Color.red.getRGB() &&  image.getRGB(i-1, j-1) == Color.red.getRGB()&&image.getRGB(i-1, j-2) == Color.red.getRGB()
	            		&& image.getRGB(i+1, j) == Color.red.getRGB() && image.getRGB(i+1, j-1) == Color.red.getRGB()&&image.getRGB(i+1, j-2) == Color.red.getRGB()){
	            	rgbImageRed.setRGB(i, j, Color.red.getRGB());
	            } */
            }
        }
        try {
			ImageIO.write(rgbImageRed, "png", new File(dirImageFile));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
        return rgbImageRed;
	}
	
    /**  
     * 图像切割（改）     *  
     * @param srcImageFile            源图像地址 
     * @param dirImageFile            新图像地址 
     * @param x                       目标切片起点x坐标 
     * @param y                      目标切片起点y坐标 
     * @param destWidth              目标切片宽度 
     * @param destHeight             目标切片高度 
     */  
    public void abscut(String srcImageFile,String dirImageFile, int x, int y, int destWidth,  
            int destHeight) {  
        try {  
            Image img;  
            ImageFilter cropFilter;  
            // 读取源图像  
            BufferedImage bi = ImageIO.read(new File(srcImageFile));  
            int srcWidth = bi.getWidth(); // 源图宽度  
            int srcHeight = bi.getHeight(); // 源图高度            
            if (srcWidth >= destWidth && srcHeight >= destHeight) {  
                Image image = bi.getScaledInstance(srcWidth, srcHeight,  
                        Image.SCALE_DEFAULT);  
                // 改进的想法:是否可用多线程加快切割速度  
                // 四个参数分别为图像起点坐标和宽高  
                // 即: CropImageFilter(int x,int y,int width,int height)  
                cropFilter = new CropImageFilter(x, y, destWidth, destHeight);  
                img = Toolkit.getDefaultToolkit().createImage(  
                        new FilteredImageSource(image.getSource(), cropFilter));  
//                BufferedImage subImg = bi.getSubimage(x, y, destWidth, destHeight);
                //subImg = grayImage(subImg);
//                String s = new OCR().recognizeEverything(subImg);
//        		System.out.println("图片识别结果是：" + s);
                BufferedImage tag = new BufferedImage(destWidth, destHeight,  
                        BufferedImage.TYPE_INT_RGB);  
                Graphics g = tag.getGraphics();  
                g.drawImage(img, 0, 0, null); // 绘制缩小后的图  
                g.dispose();  
                // 输出为文件  
                ImageIO.write(tag, "JPEG", new File(dirImageFile));  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
    
    public BufferedImage grayImage(BufferedImage image) throws IOException{  
        int width = image.getWidth();  
        int height = image.getHeight();  
          
        BufferedImage grayImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);//重点，技巧在这个参数BufferedImage.TYPE_BYTE_GRAY  
        for(int i= 0 ; i < width ; i++){  
            for(int j = 0 ; j < height; j++){  
            int rgb = image.getRGB(i, j);  
            grayImage.setRGB(i, j, rgb);  
            }  
        }  
        File newFile = new File("e:/res.jpg");  
        ImageIO.write(grayImage, "jpg", newFile); 
        return grayImage;
    }  
    
	public static void main(String[] args) throws IOException{
		String srcImageFile = "E:/aaa.png";
		String dirImageFile = "e:/res.jpg";
		//new test().abscut(srcImageFile, dirImageFile, 0, 0, 133, 20);
		new test().getMainPix(srcImageFile, dirImageFile);
	}
}
