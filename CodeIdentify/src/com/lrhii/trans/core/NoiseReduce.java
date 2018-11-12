package com.lrhii.trans.core;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * �����㷨
 * @author liuhao
 *
 */
public class NoiseReduce {
	/**
     * ȥ�������� -- 8������
     * ����һ���Ҷ�ͼ��ǰ��ɫΪ��ɫ��   ��ɫ��ͳ����Ч���ص�
     * ��һ����Ч���ص�����������е㶼�ǰ�ɫ  �������е�Ϊ��ɫ���ܺʹ���ĳ����ֵ
     * ����ж���Ϊ���
     * @param binaryBufferedImage
     */
    public static void clear8Area(BufferedImage binaryBufferedImage){
    	int w = binaryBufferedImage.getWidth();
    	int h = binaryBufferedImage.getHeight();
        for(int y = 0; y < h; y++){
            for(int x = 0; x < w; x++){                   
                boolean flag = false ;
                int count = 0;//ͳ�������ɫ�������
                //��߽�ȫ����Ϊ��ɫ
                if(x==0 || x==w-1 || y==0 || y==h-1){
                	binaryBufferedImage.setRGB(x,y,-1);//���ñ���ɫΪ��ɫ   
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
                        //б����Ϊ��ʱ��ȥ���˵�
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
                        if(count > 6) flag =true;//������ֵΪ6
                        if(flag){
                            binaryBufferedImage.setRGB(x,y,-1);//���ñ���ɫΪ��ɫ                 
                        }
                    }
                }
            }
        }
    }

}
