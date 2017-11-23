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

public class test2 {
	

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
        try{
        	//ImageIO.write(rgbImageRed, "png", new File("data/img/ooooo.png"));
        }catch(Exception e){
        	
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
	            int r = c.getRed();
	            int g = c.getGreen();
	            int b = c.getBlue();
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
    	        	        try {
    	        	        	int width_t = end-start;
    	        	        	BufferedImage temp = new BufferedImage(end-start, height, BufferedImage.TYPE_INT_RGB);//重点，技巧在这个参数BufferedImage.TYPE_BYTE_GRAY  
    	        	            for(int it=0;it<width_t;it++){
    	        	            	for(int jt=0;jt<height;jt++){
    	        	    	            int rgb = rgbImageRed.getRGB(start+it, jt);
    	        	    	            temp.setRGB(it, jt, rgb);	          		
    	        	            	}
    	        	            }     
    	        				ImageIO.write(rgbImageRed, "png", new File("data/img/errors/"+sb+".png"));
    	        				ImageIO.write(temp, "png", new File("data/img/errors/"+sb+"r.png"));
    	        				//System.out.println(sb);
    	        			} catch (IOException e) {
    	        				// TODO Auto-generated catch block
    	        				e.printStackTrace();
    	        			} 
    	        			return null;
    	        		}
    	        		sb.append(r);
    	        		start=end+1;        				
        			}       			
	        		end=i;
	        		int r = recognize(rgbImageRed,start,end,height,index++);
	        		if(r<0){
	        	        try {
	        	        	int width_t = end-start;
	        	        	BufferedImage temp = new BufferedImage(end-start, height, BufferedImage.TYPE_INT_RGB);//重点，技巧在这个参数BufferedImage.TYPE_BYTE_GRAY  
	        	            for(int it=0;it<width_t;it++){
	        	            	for(int jt=0;jt<height;jt++){
	        	    	            int rgb = rgbImageRed.getRGB(start+it, jt);
	        	    	            temp.setRGB(it, jt, rgb);	          		
	        	            	}
	        	            }     
	        				ImageIO.write(rgbImageRed, "png", new File("data/img/errors/"+sb+".png"));
	        				ImageIO.write(temp, "png", new File("data/img/errors/"+sb+"r.png"));
	        				//System.out.println(sb);
	        			} catch (IOException e) {
	        				// TODO Auto-generated catch block
	        				e.printStackTrace();
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
        try {
			//ImageIO.write(rgbImageRed, "png", new File("data/img/r"+index+".png"));			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
        			BufferedImage bi = ImageIO.read(new File("data/img/model/"+i+".png")); 
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
        			BufferedImage bi = ImageIO.read(new File("data/img/model/"+i+".png")); 
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
        			BufferedImage bi = ImageIO.read(new File("data/img/model/"+i+".png")); 
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
        			BufferedImage bi = ImageIO.read(new File("data/img/model/"+i+".png")); 
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
        			BufferedImage bi = ImageIO.read(new File("data/img/model/"+i+".png")); 
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
        			BufferedImage bi = ImageIO.read(new File("data/img/model/"+i+".png")); 
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
        			BufferedImage bi = ImageIO.read(new File("data/img/model/"+i+".png")); 
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
        			BufferedImage bi = ImageIO.read(new File("data/img/model/"+i+".png")); 
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
        			BufferedImage bi = ImageIO.read(new File("data/img/model/"+i+".png")); 
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
        			BufferedImage bi = ImageIO.read(new File("data/img/model/"+i+".png")); 
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
        			BufferedImage bi = ImageIO.read(new File("data/img/model/"+i+".png")); 
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
        			BufferedImage bi = ImageIO.read(new File("data/img/model/"+i+".png")); 
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
				ImageIO.write(bi, "png", new File("data/img/wrong/"+result+".png"));
            	return 0;
            }
       
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return -1;
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
