package cn.futures.data.util;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.imageio.ImageIO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ImgRecogManager {
	private static Log logger = LogFactory.getLog(ImgRecogManager.class);

	//处理一个干扰过的电话号码
    public static String process(BufferedImage image){
		int width = image.getWidth();  
        int height = image.getHeight();  
        StringBuilder sb = new StringBuilder();
        BufferedImage rgbImageRed = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);//重点，技巧在这个参数BufferedImage.TYPE_BYTE_GRAY  
    
        //先提取红色像素，将偏红色的像素也转换成标准的红色，这样就去除了干扰因素。
        for(int i= 0 ; i < width ; i++){  
            for(int j = 0 ; j < height; j++){  
	            int rgb = image.getRGB(i, j);
	            Color c = new Color(rgb);
	            int pixelr = c.getRed();
	            int pixelg = c.getGreen();
	            int pixelb = c.getBlue();
	            //红色像素的颜色中r值会大于250并且其他两个都较小并且不能有偏色，才能够识别准确
	            if(pixelr>250 && pixelg<195 && pixelb<195 && pixelb/pixelg>0.5 && pixelb/pixelg<2){
	            	rgbImageRed.setRGB(i, j, new Color(255,0,0).getRGB());
	            }
	            else{
	            	rgbImageRed.setRGB(i, j, Color.white.getRGB());	    	             
	            }
            }
        }
        //切分图片，切成11个带识别的数字,通常情况下切分出来的单个数字宽度都是10，但是个别情况下切出来是11
        int start=0,end=0;
        int index=0;
        for(int i=0;i<width;i++){
	        int rnum = 0;
	        int mem = 0;
        	for(int j=0;j<height;j++){
	            int rgb = rgbImageRed.getRGB(i, j);
	            Color c = new Color(rgb);
//	            int r = c.getRed();
	            int g = c.getGreen();
//	            int b = c.getBlue();
	            if( g == 0){//判断是不是标准红色
	            	rnum++;
	            	mem = j;
	            }
        	}
        	if(rnum>1){//多余两个红色像素块
        		if(start==0){
        			start = i;
        		}   
        		continue;
        	}else{
	        	if(rnum==1){//单个点的情况
	        		if(start==0 && i>0 ){//可能是右联通的字符的开始，左边必须是空列，比如像7
	        			int tempc = 0;
	        			for(int j=0;j<height;j++){
	        	            int rgb = rgbImageRed.getRGB(i-1, j);
	        	            if(rgb == Color.white.getRGB()){
	        	            	tempc ++;
	        	            }
	        			}
	        			if(tempc==0){
	        				start=i;
	        				continue;
	        			}
	        		}
        			if(start!=0&&i>0&&i<width-1){//可能是字符中间联通的位置
        				int rgb1 = rgbImageRed.getRGB(i-1, mem);
        				int rgb2 = rgbImageRed.getRGB(i+1, mem);        			
        				if(rgb1!=Color.white.getRGB() && rgb2!= Color.white.getRGB() ){
        					continue;
        				}
        			}         			
        		}
        		if( start!=0 && i-start>=3 ){   //长度大于3才有识别的意义 ，就算是1，长度也要最少是3 
        			if(i-start>=18){
        				end = start+(i-start)/2;
    	        		int r = recognize(rgbImageRed,start,end,height,index++);
    	        		if(r<0){
	        	        	int width_t = end-start;
	        	        	BufferedImage temp = new BufferedImage(end-start, height, BufferedImage.TYPE_INT_RGB);//重点，技巧在这个参数BufferedImage.TYPE_BYTE_GRAY  
	        	            for(int it=0;it<width_t;it++){
	        	            	for(int jt=0;jt<height;jt++){
	        	    	            int rgb = rgbImageRed.getRGB(start+it, jt);
	        	    	            temp.setRGB(it, jt, rgb);	          		
	        	            	}
	        	            }     
    	        			return null;
    	        		}
    	        		sb.append(r);
    	        		start=end+1;        				
        			}       			
	        		end=i;
	        		int r = recognize(rgbImageRed,start,end,height,index++);
	        		if(r<0){
        	        	int width_t = end-start;
        	        	BufferedImage temp = new BufferedImage(end-start, height, BufferedImage.TYPE_INT_RGB);//重点，技巧在这个参数BufferedImage.TYPE_BYTE_GRAY  
        	            for(int it=0;it<width_t;it++){
        	            	for(int jt=0;jt<height;jt++){
        	    	            int rgb = rgbImageRed.getRGB(start+it, jt);
        	    	            temp.setRGB(it, jt, rgb);	          		
        	            	}
        	            }     
	        			return null;
	        		}
	        		sb.append(r);
	        		start=0;
        		}
        	}
        }
        
    	return sb.toString();
    }
    
    //识别切出来的单个数字
    public static int recognize(BufferedImage image,int start,int end,int height,int index){
    	int width = end-start;
    	BufferedImage rgbImageRed = new BufferedImage(end-start, height, BufferedImage.TYPE_INT_RGB);//重点，技巧在这个参数BufferedImage.TYPE_BYTE_GRAY  
        for(int i=0;i<width;i++){
        	for(int j=0;j<height;j++){
	            int rgb = image.getRGB(start+i, j);
	            rgbImageRed.setRGB(i, j, rgb);	          		
        	}
        }   
        if(width<=6){//1是最窄的，最大也只有5个像素值，有可能有一个干扰点
        	return 1;
        }
        if(width==10){//10个像素宽度是标准字符宽度
        	int[] guess = new int[10];
        	//跟每一个标准字符比较，判断哪一个数字的可能性最大
        	for(int i=0;i<10;i++){
        		guess[i] = 0;
        		if(i==1)
        			continue;
        		try{
        			//以后可以考虑提前加载到内存中，避免每次都要读
        			InputStream in=ImgRecogManager.class.getResourceAsStream("/cn/futures/img/model/"+i+".png"); 
        			BufferedImage bi = ImageIO.read(in); 
        			for(int x=0;x<10;x++){
        				for(int y=0;y<height;y++){
        					//System.out.println(x+" "+y);
        		            int rgb2 = rgbImageRed.getRGB(x, y);
        		            if(rgb2 != (new Color(255,0,0)).getRGB())
        		            	continue;
        		            int rgb1 = bi.getRGB(x,y);
        		            if( rgb1 == rgb2 )
        		            	guess[i] ++;//计算相似度
        				}
        			}        			
        		}catch(IOException e){
        			e.printStackTrace();
        		}
        	}
        	//判断最大可能
        	int max = -1;
        	for( int i=0;i<10;i++){
        		if( max<0 || guess[i]>guess[max] ){
        			max = i;
        		}
        	}
        	return max;
        }
        if(width==9){//9个字符宽度的话，标准是10个，所以要尝试移动一位来做判断
        	int[] guess = new int[10];
        	for(int i=0;i<10;i++){
        		guess[i] = 0;
        		if(i==1)
        			continue;
        		try{
        			InputStream in=ImgRecogManager.class.getResourceAsStream("/cn/futures/img/model/"+i+".png"); 
        			BufferedImage bi = ImageIO.read(in); 
        			for(int x=0;x<width;x++){
        				for(int y=0;y<height;y++){
        		            int rgb2 = rgbImageRed.getRGB(x, y);
        		            if(rgb2 != (new Color(255,0,0)).getRGB())
        		            	continue;
        		            int rgb1 = bi.getRGB(x,y);
        		            if( rgb1 == rgb2 )
        		            	guess[i] ++;
        				}
        			}        			
        		}catch(IOException e){
        			e.printStackTrace();
        		}
        	}
        	//右移一位
        	for(int i=0;i<10;i++){     
        		if(i==1)
        			continue;   		
        		try{
        			int count = 0;
        			InputStream in=ImgRecogManager.class.getResourceAsStream("/cn/futures/img/model/"+i+".png"); 
        			BufferedImage bi = ImageIO.read(in); 
        			for(int x=0;x<width;x++){
        				for(int y=0;y<height;y++){
        		            int rgb2 = rgbImageRed.getRGB(x, y);
        		            if(rgb2 != (new Color(255,0,0)).getRGB())
        		            	continue;
        		            int rgb1 = bi.getRGB(x+1,y);
        		            if( rgb1 == rgb2 )
        		            	count ++;
        				}
        			}  
        			if(count>guess[i])
        				guess[i] = count;
        		}catch(IOException e){
        			e.printStackTrace();
        		}
        	}
        	
        	int max = -1;
        	for( int i=0;i<10;i++){
        		if( max<0 || guess[i]>guess[max] ){
        			max = i;
        		}
        	}
        	return max;  	
        }
        //同9个字符一样，要多尝试一下
        if(width==8){
        	int[] guess = new int[10];
        	for(int i=0;i<10;i++){
        		guess[i] = 0;
        		if(i==1)
        			continue;
        		try{
        			InputStream in=ImgRecogManager.class.getResourceAsStream("/cn/futures/img/model/"+i+".png"); 
        			BufferedImage bi = ImageIO.read(in); 
        			for(int x=0;x<width;x++){
        				for(int y=0;y<height;y++){
        		            int rgb2 = rgbImageRed.getRGB(x, y);
        		            if(rgb2 != (new Color(255,0,0)).getRGB())
        		            	continue;
        		            int rgb1 = bi.getRGB(x,y);
        		            if( rgb1 == rgb2 )
        		            	guess[i] ++;
        				}
        			}        			
        		}catch(IOException e){
        			e.printStackTrace();
        		}
        	}
        	for(int i=0;i<10;i++){     
        		if(i==1)
        			continue;   		
        		try{
        			int count = 0;
        			InputStream in=ImgRecogManager.class.getResourceAsStream("/cn/futures/img/model/"+i+".png"); 
        			BufferedImage bi = ImageIO.read(in); 
        			for(int x=0;x<width;x++){
        				for(int y=0;y<height;y++){
        		            int rgb2 = rgbImageRed.getRGB(x, y);
        		            if(rgb2 != (new Color(255,0,0)).getRGB())
        		            	continue;
        		            int rgb1 = bi.getRGB(x+1,y);
        		            if( rgb1 == rgb2 )
        		            	count ++;
        				}
        			}  
        			if(count>guess[i])
        				guess[i] = count;
        		}catch(IOException e){
        			e.printStackTrace();
        		}
        	}
        	for(int i=0;i<10;i++){     
        		if(i==1)
        			continue;   		
        		try{
        			int count = 0;
        			InputStream in=ImgRecogManager.class.getResourceAsStream("/cn/futures/img/model/"+i+".png"); 
        			BufferedImage bi = ImageIO.read(in); 
        			for(int x=0;x<width;x++){
        				for(int y=0;y<height;y++){
        		            int rgb2 = rgbImageRed.getRGB(x, y);
        		            if(rgb2 != (new Color(255,0,0)).getRGB())
        		            	continue;
        		            int rgb1 = bi.getRGB(x+2,y);
        		            if( rgb1 == rgb2 )
        		            	count ++;
        				}
        			}  
        			if(count>guess[i])
        				guess[i] = count;
        		}catch(IOException e){
        			e.printStackTrace();
        		}
        	}
        	int max = -1;
        	for( int i=0;i<10;i++){
        		if( max<0 || guess[i]>guess[max] ){
        			max = i;
        		}
        	}
        	return max;          	
        }
        //同9个字符一样，要多尝试一下
        if(width==7){
        	int[] guess = new int[10];
        	for(int i=0;i<10;i++){
        		guess[i] = 0;
        		if(i==1)
        			continue;
        		try{
        			InputStream in=ImgRecogManager.class.getResourceAsStream("/cn/futures/img/model/"+i+".png"); 
        			BufferedImage bi = ImageIO.read(in); 
        			for(int x=0;x<width;x++){
        				for(int y=0;y<height;y++){
        		            int rgb2 = rgbImageRed.getRGB(x, y);
        		            if(rgb2 != (new Color(255,0,0)).getRGB())
        		            	continue;
        		            int rgb1 = bi.getRGB(x,y);
        		            if( rgb1 == rgb2 )
        		            	guess[i] ++;
        				}
        			}        			
        		}catch(IOException e){
        			e.printStackTrace();
        		}
        	}
        	for(int i=0;i<10;i++){     
        		if(i==1)
        			continue;   		
        		try{
        			int count = 0;
        			InputStream in=ImgRecogManager.class.getResourceAsStream("/cn/futures/img/model/"+i+".png"); 
        			BufferedImage bi = ImageIO.read(in); 
        			for(int x=0;x<width;x++){
        				for(int y=0;y<height;y++){
        		            int rgb2 = rgbImageRed.getRGB(x, y);
        		            if(rgb2 != (new Color(255,0,0)).getRGB())
        		            	continue;
        		            int rgb1 = bi.getRGB(x+1,y);
        		            if( rgb1 == rgb2 )
        		            	count ++;
        				}
        			}  
        			if(count>guess[i])
        				guess[i] = count;
        		}catch(IOException e){
        			e.printStackTrace();
        		}
        	}
        	for(int i=0;i<10;i++){     
        		if(i==1)
        			continue;   		
        		try{
        			int count = 0;
        			InputStream in=ImgRecogManager.class.getResourceAsStream("/cn/futures/img/model/"+i+".png"); 
        			BufferedImage bi = ImageIO.read(in); 
        			for(int x=0;x<width;x++){
        				for(int y=0;y<height;y++){
        		            int rgb2 = rgbImageRed.getRGB(x, y);
        		            if(rgb2 != (new Color(255,0,0)).getRGB())
        		            	continue;
        		            int rgb1 = bi.getRGB(x+2,y);
        		            if( rgb1 == rgb2 )
        		            	count ++;
        				}
        			}  
        			if(count>guess[i])
        				guess[i] = count;
        		}catch(IOException e){
        			e.printStackTrace();
        		}
        	}          	
        	for(int i=0;i<10;i++){     
        		if(i==1)
        			continue;   		
        		try{
        			int count = 0;
        			InputStream in=ImgRecogManager.class.getResourceAsStream("/cn/futures/img/model/"+i+".png"); 
        			BufferedImage bi = ImageIO.read(in); 
        			for(int x=0;x<width;x++){
        				for(int y=0;y<height;y++){
        		            int rgb2 = rgbImageRed.getRGB(x, y);
        		            if(rgb2 != (new Color(255,0,0)).getRGB())
        		            	continue;
        		            int rgb1 = bi.getRGB(x+3,y);
        		            if( rgb1 == rgb2 )
        		            	count ++;
        				}
        			}  
        			if(count>guess[i])
        				guess[i] = count;
        		}catch(IOException e){
        			e.printStackTrace();
        		}
        	}        	      	
        	int max = -1;
        	for( int i=0;i<10;i++){
        		if( max<0 || guess[i]>guess[max] ){
        			max = i;
        		}
        	}
        	return max;          	
        }
        //同9个字符一样，要多尝试一下
        if(width==11){
        	int[] guess = new int[10];
        	for(int i=0;i<10;i++){
        		guess[i] = 0;
        		if(i==1)
        			continue;
        		try{
        			InputStream in=ImgRecogManager.class.getResourceAsStream("/cn/futures/img/model/"+i+".png"); 
        			BufferedImage bi = ImageIO.read(in); 
        			for(int x=0;x<10;x++){
        				for(int y=0;y<height;y++){
        		            int rgb2 = rgbImageRed.getRGB(x, y);
        		            if(rgb2 != (new Color(255,0,0)).getRGB())
        		            	continue;
        		            int rgb1 = bi.getRGB(x,y);
        		            if( rgb1 == rgb2 )
        		            	guess[i] ++;
        				}
        			}        			
        		}catch(IOException e){
        			e.printStackTrace();
        		}
        	}
        	for(int i=0;i<10;i++){     
        		if(i==1)
        			continue;   		
        		try{
        			int count = 0;
        			InputStream in=ImgRecogManager.class.getResourceAsStream("/cn/futures/img/model/"+i+".png"); 
        			BufferedImage bi = ImageIO.read(in); 
        			for(int x=0;x<10;x++){
        				for(int y=0;y<height;y++){
        		            int rgb2 = rgbImageRed.getRGB(x+1, y);
        		            if(rgb2 != (new Color(255,0,0)).getRGB())
        		            	continue;
        		            int rgb1 = bi.getRGB(x,y);
        		            if( rgb1 == rgb2 )
        		            	count ++;
        				}
        			}  
        			if(count>guess[i])
        				guess[i] = count;
        		}catch(IOException e){
        			e.printStackTrace();
        		}
        	}
        	
        	int max = -1;
        	for( int i=0;i<10;i++){
        		if( max<0 || guess[i]>guess[max] ){
        			max = i;
        		}
        	}
        	return max;  	
        }
        //System.out.println("图片太不完整了");
        return -1;
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
    public static int abscut(String srcImageFile, int x, int y, int destWidth, int destHeight) {  
        try {  
            // 读取源图像  
            BufferedImage bi = ImageIO.read(new File(srcImageFile));  
            int srcWidth = bi.getWidth(); // 源图宽度  
            int srcHeight = bi.getHeight(); // 源图高度         
            if(srcWidth != destWidth || srcHeight != destHeight ){
            	System.out.println("图像大小不符合标准："+srcWidth+"*"+srcHeight);
            }
            String result = process(bi);
            if(result == null)
            	return -1;
           // System.out.println(srcImageFile);
            if(result.equals(srcImageFile.substring(srcImageFile.lastIndexOf("\\")+1,srcImageFile.lastIndexOf(".")))){
            	return 1;
            }else{ 
            	return 0;
            }
       
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return -1;
    }  
    
    // 生成图片函数  
    public  static void makeImg(String imgUrl,String fileURL, String imgName) {  
        try {  
  
            URL url = new URL(imgUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            //打开网络输入流
            DataInputStream dis = new DataInputStream(connection.getInputStream());
            String newImageName = fileURL+imgName;
            if (!new File(fileURL).exists())
            	createPath(fileURL);
            //建立一个新的文件
            FileOutputStream fos = new FileOutputStream(new File(newImageName));
            byte[] buffer = new byte[1024];
            int length;
            //开始填充数据
            while((length = dis.read(buffer))>0){
            	fos.write(buffer,0,length);
            }
            dis.close();
            fos.close(); 
        } catch (Exception e) {  
            e.printStackTrace(); 
            logger.error("电话号码下载异常");
        }  
    }
    public static void createPath(String name){
		File idDir = new File(name);
		if (!idDir.exists()) {
			idDir.mkdirs();
		}
    }
    
	public static void main(String[] args) throws IOException{
		String filename ="data/img/15097448314.png";
		//abscut(filename, 0, 0, 133, 20);
		String dir = "data/img/sample/";
		File d = new File(dir);
		int succ=0, wrong=0, fail=0;
		int r;
		for( File file:d.listFiles()){			
			r = abscut(file.getAbsolutePath(), 0, 0, 133, 20);
			switch(r){
			case 1:  succ++;break;
			case 0: wrong++;break;
			case -1: fail++;break;
			}
		}
		System.out.println("成功："+succ);
		System.out.println("错误："+wrong);
		System.out.println("失败："+fail);
	}
}
