package com.lrhii.trans.core;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * 二值化算法
 * @author liuhao
 *
 */
public class BinaryCore {
	
	//二值化算法
	private static int[] getBinaryImg(int w, int h, int[] inputs) {
        int[] gray = new int[w * h];
        int[] newpixel = new int[w * h];
        for (int index = 0; index < w * h; index++) {
            int red = (inputs[index] & 0x00FF0000) >> 16;
            int green = (inputs[index] & 0x0000FF00) >> 8;
            int blue = inputs[index] & 0x000000FF;
            //求灰度值
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
        int finestYzt = newT; //最佳阈值

        //二值化
        for (int index = 0; index < w * h; index++) {
            if (gray[index] > finestYzt)
                newpixel[index] = Color.WHITE.getRGB();
            else newpixel[index] = Color.BLACK.getRGB();
        }
        
        return newpixel;
    }
	
	/**
	 * 二值化
	 * @param colorMat  像素颜色矩阵
	 * @return 
	 */
	private static int[] getBinaryImg(int[][] colorMat){
		int h = colorMat.length;
		int w = colorMat[0].length;
		
		//灰度矩阵
		int gray[][] = new int[h][w];
		//灰度直方图
		int[] histogram = new int[256];
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				int red = (colorMat[i][j] & 0x00FF0000) >> 16;
	            int green = (colorMat[i][j] & 0x0000FF00) >> 8;
	            int blue = colorMat[i][j] & 0x000000FF;
	            int grayV = (int) ((float) red * 0.3 + (float) green * 0.59 + (float) blue * 0.11);
	            //求灰度值
	            gray[i][j] = grayV;
	            histogram[grayV]++;
			}
		}
		//求最佳阈值
        int threshold = getThreshold(histogram);
        //二值化
        int[] binaryPixs = new int[h*w];
        for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				if(gray[i][j] > threshold){
					binaryPixs[i*w+j] = Color.WHITE.getRGB();
				}
			}
		}
		return binaryPixs;
	}
	
    /**
     * 获取二值化图像
     * @param path
     * @throws Exception
     */
    public static BufferedImage binary(BufferedImage img) throws Exception{
    	int w = img.getWidth();
    	int h = img.getHeight();
//    	int[] inputs = new int[h*w];
//    	//构造矩阵
//    	for(int x=0;x<h;x++){
//    		for(int y=0;y<w;y++){
//    			inputs[w*x+y] = img.getRGB(y, x);
//    		}
//    	}
//    	//二值化
//    	int[] binaryImg = getBinaryImg(w, h, inputs);
    	int[][] inputs = new int[h][w];
    	//构造矩阵
    	for(int x=0;x<h;x++){
    		for(int y=0;y<w;y++){
    			inputs[x][y] = img.getRGB(y, x);
    		}
    	}
    	//二值化
    	int[] binaryImg = getBinaryImg(inputs);
    	BufferedImage bufferedImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_BGR);
    	bufferedImage.setRGB(0, 0, w, h, binaryImg, 0, w);
    	return bufferedImage;
    }
    
    public static void main(String[] args) {
    	int[][] colorMat = new int[2][3];
    	System.out.println(colorMat.length);
    	System.out.println(colorMat[0].length);
    	
//    	int[] histogram = new int[256];
//    	histogram[10] = 2;
//    	histogram[50] = 1;
//    	histogram[60] = 2;
//    	histogram[62] = 1;
//    	histogram[63] = 1;
//    	histogram[100] = 1;
//    	histogram[200] = 1;
//    	histogram[250] = 1;
//    	int threshold = getThreshold(histogram);
//    	System.out.println(threshold);
	}
    
    /**
     * 求灰度图最佳阈值
     * @param histogram 灰度直方图
     * @return
     */
    public static int getThreshold(int[] histogram){
    	int minG = 0;//最小灰度值
    	int maxG = 0;//最大灰度值
    	for (int k = 0; k < histogram.length; k++) {//求最小灰度值
			if(histogram[k] != 0){
				minG = k;
				break;
			}
		}
    	for (int k = histogram.length - 1; k >= 0; k--) {//求最大灰度值
    		if(histogram[k] != 0){
    			maxG = k;
				break;
			}
		}
    	/*
         * 迭代法求出最佳分割阈值
         * */
         int threshold = 0;//最佳阈值
         int initT = (minG + maxG) / 2;//初始阈值
         int bp = 0,fp = 0,bCount = 0,bSum = 0,fCount = 0,fSum = 0,i = 0,j = 0;
         while (threshold != initT)
         //求出背景和前景的平均灰度值bp和fp
         {
             for (i = 0; i < threshold; i++) {
            	 bCount += histogram[i];//背景像素点的总个数
            	 bSum += histogram[i] * i;//背景像素点的灰度总值
             }
             bp = (bCount == 0) ? 0 : (bSum / bCount);//背景像素点的平均灰度值

             for (j = i; j < histogram.length; j++) {
            	 fCount += histogram[j];//前景像素点的总个数
            	 fSum += histogram[j] * j;//前景像素点的灰度总值
             }
             fp = (fCount == 0) ? 0 : (fSum / fCount);//前景像素点的平均灰度值
             threshold = initT;
             initT = (bp + fp) / 2;
         }
         return threshold;
    }
    
}
