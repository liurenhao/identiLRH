package com.lrhii.trans.core;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * ��ֵ���㷨
 * @author liuhao
 *
 */
public class BinaryCore {
	
	//��ֵ���㷨
	private static int[] getBinaryImg(int w, int h, int[] inputs) {
        int[] gray = new int[w * h];
        int[] newpixel = new int[w * h];
        for (int index = 0; index < w * h; index++) {
            int red = (inputs[index] & 0x00FF0000) >> 16;
            int green = (inputs[index] & 0x0000FF00) >> 8;
            int blue = inputs[index] & 0x000000FF;
            //��Ҷ�ֵ
            gray[index] = (int) ((float) red * 0.3 + (float) green * 0.59 + (float) blue * 0.11);
        }
        //������Ҷ�ֵzmax����С�Ҷ�ֵzmin
        int Gmax = gray[0], Gmin = gray[0];
        for (int index = 0; index < w * h; index++) {
            if (gray[index] > Gmax) {
                Gmax = gray[index];
            }
            if (gray[index] < Gmin) {
                Gmin = gray[index];
            }
        }

        //��ȡ�Ҷ�ֱ��ͼ
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
        * �����������ѷָ���ֵ
        * */
        int T = 0;
        int newT = (Gmax + Gmin) / 2;//��ʼ��ֵ
        while (T != newT)
        //���������ǰ����ƽ���Ҷ�ֵbp��fp
        {
            for (i = 0; i < T; i++) {
                count1 += histogram[i];//�������ص���ܸ���
                sum1 += histogram[i] * i;//�������ص�ĻҶ���ֵ
            }
            bp = (count1 == 0) ? 0 : (sum1 / count1);//�������ص��ƽ���Ҷ�ֵ

            for (j = i; j < histogram.length; j++) {
                count2 += histogram[j];//ǰ�����ص���ܸ���
                sum2 += histogram[j] * j;//ǰ�����ص�ĻҶ���ֵ
            }
            fp = (count2 == 0) ? 0 : (sum2 / count2);//ǰ�����ص��ƽ���Ҷ�ֵ
            T = newT;
            newT = (bp + fp) / 2;
        }
        int finestYzt = newT; //�����ֵ

        //��ֵ��
        for (int index = 0; index < w * h; index++) {
            if (gray[index] > finestYzt)
                newpixel[index] = Color.WHITE.getRGB();
            else newpixel[index] = Color.BLACK.getRGB();
        }
        
        return newpixel;
    }
	
	/**
	 * ��ֵ��
	 * @param colorMat  ������ɫ����
	 * @return 
	 */
	private static int[] getBinaryImg(int[][] colorMat){
		int h = colorMat.length;
		int w = colorMat[0].length;
		
		//�ҶȾ���
		int gray[][] = new int[h][w];
		//�Ҷ�ֱ��ͼ
		int[] histogram = new int[256];
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				int red = (colorMat[i][j] & 0x00FF0000) >> 16;
	            int green = (colorMat[i][j] & 0x0000FF00) >> 8;
	            int blue = colorMat[i][j] & 0x000000FF;
	            int grayV = (int) ((float) red * 0.3 + (float) green * 0.59 + (float) blue * 0.11);
	            //��Ҷ�ֵ
	            gray[i][j] = grayV;
	            histogram[grayV]++;
			}
		}
		//�������ֵ
        int threshold = getThreshold(histogram);
        //��ֵ��
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
     * ��ȡ��ֵ��ͼ��
     * @param path
     * @throws Exception
     */
    public static BufferedImage binary(BufferedImage img) throws Exception{
    	int w = img.getWidth();
    	int h = img.getHeight();
//    	int[] inputs = new int[h*w];
//    	//�������
//    	for(int x=0;x<h;x++){
//    		for(int y=0;y<w;y++){
//    			inputs[w*x+y] = img.getRGB(y, x);
//    		}
//    	}
//    	//��ֵ��
//    	int[] binaryImg = getBinaryImg(w, h, inputs);
    	int[][] inputs = new int[h][w];
    	//�������
    	for(int x=0;x<h;x++){
    		for(int y=0;y<w;y++){
    			inputs[x][y] = img.getRGB(y, x);
    		}
    	}
    	//��ֵ��
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
     * ��Ҷ�ͼ�����ֵ
     * @param histogram �Ҷ�ֱ��ͼ
     * @return
     */
    public static int getThreshold(int[] histogram){
    	int minG = 0;//��С�Ҷ�ֵ
    	int maxG = 0;//���Ҷ�ֵ
    	for (int k = 0; k < histogram.length; k++) {//����С�Ҷ�ֵ
			if(histogram[k] != 0){
				minG = k;
				break;
			}
		}
    	for (int k = histogram.length - 1; k >= 0; k--) {//�����Ҷ�ֵ
    		if(histogram[k] != 0){
    			maxG = k;
				break;
			}
		}
    	/*
         * �����������ѷָ���ֵ
         * */
         int threshold = 0;//�����ֵ
         int initT = (minG + maxG) / 2;//��ʼ��ֵ
         int bp = 0,fp = 0,bCount = 0,bSum = 0,fCount = 0,fSum = 0,i = 0,j = 0;
         while (threshold != initT)
         //���������ǰ����ƽ���Ҷ�ֵbp��fp
         {
             for (i = 0; i < threshold; i++) {
            	 bCount += histogram[i];//�������ص���ܸ���
            	 bSum += histogram[i] * i;//�������ص�ĻҶ���ֵ
             }
             bp = (bCount == 0) ? 0 : (bSum / bCount);//�������ص��ƽ���Ҷ�ֵ

             for (j = i; j < histogram.length; j++) {
            	 fCount += histogram[j];//ǰ�����ص���ܸ���
            	 fSum += histogram[j] * j;//ǰ�����ص�ĻҶ���ֵ
             }
             fp = (fCount == 0) ? 0 : (fSum / fCount);//ǰ�����ص��ƽ���Ҷ�ֵ
             threshold = initT;
             initT = (bp + fp) / 2;
         }
         return threshold;
    }
    
}
