package com.lrhii.trans;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

public class Binary {
    public static int[] getBinaryImg(int w, int h, int[] inputs) {
        int[] gray = new int[w * h];
        int[] newpixel = new int[w * h];
        for (int index = 0; index < w * h; index++) {
            int red = (inputs[index] & 0x00FF0000) >> 16;
            int green = (inputs[index] & 0x0000FF00) >> 8;
            int blue = inputs[index] & 0x000000FF;
            gray[index] = (int) ((float) red * 0.3 + (float) green * 0.59 + (float) blue * 0.11);
        }
        //求出最大灰度值zmax和最小灰度值zmin
        int Gmax = gray[0], Gmin = gray[0];
        for (int index = 0; index < w * h; index++) {
            if (gray[index] > Gmax) {
                Gmax = gray[index];
            }
            if (gray[index] < Gmin) {
                Gmin = gray[index];
            }
        }

        //获取灰度直方图
        int i, j, t, count1 = 0, count2 = 0, sum1 = 0, sum2 = 0;
        int bp, fp;
        int[] histogram = new int[256];
        for (t = Gmin; t <= Gmax; t++) {
            for (int index = 0; index < w * h; index++) {
                if (gray[index] == t)
                    histogram[t]++;
            }
        }

        /*
        * 迭代法求出最佳分割阈值
        * */
        int T = 0;
        int newT = (Gmax + Gmin) / 2;//初始阈值
        while (T != newT)
        //求出背景和前景的平均灰度值bp和fp
        {
            for (i = 0; i < T; i++) {
                count1 += histogram[i];//背景像素点的总个数
                sum1 += histogram[i] * i;//背景像素点的灰度总值
            }
            bp = (count1 == 0) ? 0 : (sum1 / count1);//背景像素点的平均灰度值

            for (j = i; j < histogram.length; j++) {
                count2 += histogram[j];//前景像素点的总个数
                sum2 += histogram[j] * j;//前景像素点的灰度总值
            }
            fp = (count2 == 0) ? 0 : (sum2 / count2);//前景像素点的平均灰度值
            T = newT;
            newT = (bp + fp) / 2;
        }
        System.out.println("最佳阈值："+newT);
        int finestYzt = newT; //最佳阈值

        //二值化
        for (int index = 0; index < w * h; index++) {
            if (gray[index] > finestYzt)
                newpixel[index] = Color.WHITE.getRGB();
            else newpixel[index] = Color.BLACK.getRGB();
        }
        
        return newpixel;
    }
    
    public static void main(String[] args) throws Exception {
//    	BufferedImage img = ImageIO.read(new File("d://check.png")); 
//    	int w = img.getWidth();
//    	int h = img.getHeight();
//    	int[] inputs = new int[w*h];
//    	for(int x=0;x<h;x++){
//    		for(int y=0;y<w;y++){
//    			inputs[w*x+y] = img.getRGB(y, x);
//    		}
//    	}
//    	int[] binaryImg = Binary.getBinaryImg(w, h, inputs);
//    	BufferedImage bufferedImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_BGR);
//    	bufferedImage.setRGB(0, 0, w, h, binaryImg, 0, w);
//    	ImageIO.write(bufferedImage, "PNG", new File("d://checkwb.png"));
//    	System.out.println("图像输出成功！");
//    	System.out.println(Color.WHITE.getRed()+"-"+Color.WHITE.getGreen()+"-"+Color.WHITE.getBlue());
//    	System.out.println(Color.BLACK.getRed()+"-"+Color.BLACK.getGreen()+"-"+Color.BLACK.getBlue());
//    	for(int x=0;x<5;x++){
//    		for(int y=0;y<6;y++){
//    			System.out.println(6*x+(y));
//    		}
//    	}
    	binary("E://source");
//    	binaryOne("E://source//mkny.png");
	}
    
    public static void binary(String source) throws Exception{
    	File dir = new File(source);  
        File[] files = dir.listFiles();  
        for (File file : files) {  
        	BufferedImage img = ImageIO.read(new File(source+"//"+file.getName())); 
        	int w = img.getWidth();
        	int h = img.getHeight();
        	int[] inputs = new int[w*h];
        	for(int x=0;x<h;x++){
        		for(int y=0;y<w;y++){
        			inputs[w*x+y] = img.getRGB(y, x);
        		}
        	}
        	int[] binaryImg = Binary.getBinaryImg(w, h, inputs);
        	BufferedImage bufferedImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_BGR);
        	bufferedImage.setRGB(0, 0, w, h, binaryImg, 0, w);
        	//去躁
        	clear8Area(bufferedImage);
        	ImageIO.write(bufferedImage, "PNG", new File("E://source-binary//"+file.getName()));
        	System.out.println("图像输出成功！");
        }  
    }
    
    public static void binaryOne(String path) throws Exception{
    	BufferedImage img = ImageIO.read(new File(path)); 
    	int w = img.getWidth();
    	int h = img.getHeight();
    	int[] inputs = new int[w*h];
    	for(int x=0;x<h;x++){
    		for(int y=0;y<w;y++){
    			inputs[w*x+y] = img.getRGB(y, x);
    		}
    	}
    	int[] binaryImg = Binary.getBinaryImg(w, h, inputs);
    	BufferedImage bufferedImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_BGR);
    	bufferedImage.setRGB(0, 0, w, h, binaryImg, 0, w);
    	//去躁
    	clear8Area(bufferedImage);
    	ImageIO.write(bufferedImage, "PNG", new File("E://source-one//"+path.substring(path.lastIndexOf("//"))));
    	System.out.println("图像输出成功！");
    }
    
    /**
     * 去除噪音点 -- 8邻域降噪
     * 对于一个灰度图（前景色为黑色）   黑色点统称有效像素点
     * 当一个有效像素点的相邻域所有点都是白色  或者所有点为白色的总和大于某个阈值
     * 则可判定其为噪点
     * @param binaryBufferedImage
     */
    public static void clear8Area(BufferedImage binaryBufferedImage){
    	int w = binaryBufferedImage.getWidth();
    	int h = binaryBufferedImage.getHeight();
       
    	//外边界全部置为白色
    	
        for(int y = 0; y < h; y++){
            for(int x = 0; x < w; x++){                   
                boolean flag = false ;
                int count = 0;//统计邻域白色点的数量
                if(x==0 || x==w-1 || y==0 || y==h-1){
                	binaryBufferedImage.setRGB(x,y,-1);//设置背景色为白色   
                }else{
                	if(isBlack(binaryBufferedImage.getRGB(x, y))){
                        if(isWhite(binaryBufferedImage.getRGB(x-1, y))){
                        	count++;
                        }
                        if(isWhite(binaryBufferedImage.getRGB(x+1, y))){
                        	count++;
                        }
                        if(isWhite(binaryBufferedImage.getRGB(x, y+1))){
                        	count++;
                        } 
                        if(isWhite(binaryBufferedImage.getRGB(x, y-1))){
                        	count++;
                        }
                        //斜上下为空时，去掉此点
                        if(isWhite(binaryBufferedImage.getRGB(x-1, y+1))){
                        	count++;
                        }
                        if(isWhite(binaryBufferedImage.getRGB(x+1, y-1))){
                        	count++;
                        }
                        if(isWhite(binaryBufferedImage.getRGB(x+1, y+1))){
                        	count++;
                        } 
                        if(isWhite(binaryBufferedImage.getRGB(x-1, y-1))){
                        	count++;
                        }
                        if(count>6) flag =true;//设置阈值为5
                        if(flag){
                            binaryBufferedImage.setRGB(x,y,-1);//设置背景色为白色                 
                        }
                    }
                }
            }
        }
        
    }
    
    
    
    public static boolean isBlack(int colorInt)  
    {  
        Color color = new Color(colorInt);  
        if (color.getRed() + color.getGreen() + color.getBlue() <= 0)  
        {  
            return true;  
        }  
        return false;  
    }  

    public static boolean isWhite(int colorInt)  
    {  
        Color color = new Color(colorInt);  
        if (color.getRed() + color.getGreen() + color.getBlue() > 0)  
        {  
            return true;  
        }  
        return false;  
    }  
    
    
}