package com.lrhii.trans.core;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * 降噪算法
 * @author liuhao
 *
 */
public class NoiseReduce {
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
        for(int y = 0; y < h; y++){
            for(int x = 0; x < w; x++){                   
                boolean flag = false ;
                int count = 0;//统计邻域白色点的数量
                //外边界全部置为白色
                if(x==0 || x==w-1 || y==0 || y==h-1){
                	binaryBufferedImage.setRGB(x,y,-1);//设置背景色为白色   
                }else{
                	if(ImageHandle.isBlack(binaryBufferedImage.getRGB(x, y))){
                        if(ImageHandle.isWhite(binaryBufferedImage.getRGB(x-1, y))){
                        	count++;
                        }
                        if(ImageHandle.isWhite(binaryBufferedImage.getRGB(x+1, y))){
                        	count++;
                        }
                        if(ImageHandle.isWhite(binaryBufferedImage.getRGB(x, y+1))){
                        	count++;
                        } 
                        if(ImageHandle.isWhite(binaryBufferedImage.getRGB(x, y-1))){
                        	count++;
                        }
                        //斜上下为空时，去掉此点
                        if(ImageHandle.isWhite(binaryBufferedImage.getRGB(x-1, y+1))){
                        	count++;
                        }
                        if(ImageHandle.isWhite(binaryBufferedImage.getRGB(x+1, y-1))){
                        	count++;
                        }
                        if(ImageHandle.isWhite(binaryBufferedImage.getRGB(x+1, y+1))){
                        	count++;
                        } 
                        if(ImageHandle.isWhite(binaryBufferedImage.getRGB(x-1, y-1))){
                        	count++;
                        }
                        if(count > 6) flag =true;//设置阈值为6
                        if(flag){
                            binaryBufferedImage.setRGB(x,y,-1);//设置背景色为白色                 
                        }
                    }
                }
            }
        }
    }

}
