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
        System.out.println("�����ֵ��"+newT);
        int finestYzt = newT; //�����ֵ

        //��ֵ��
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
//    	System.out.println("ͼ������ɹ���");
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
        	//ȥ��
        	clear8Area(bufferedImage);
        	ImageIO.write(bufferedImage, "PNG", new File("E://source-binary//"+file.getName()));
        	System.out.println("ͼ������ɹ���");
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
    	//ȥ��
    	clear8Area(bufferedImage);
    	ImageIO.write(bufferedImage, "PNG", new File("E://source-one//"+path.substring(path.lastIndexOf("//"))));
    	System.out.println("ͼ������ɹ���");
    }
    
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
       
    	//��߽�ȫ����Ϊ��ɫ
    	
        for(int y = 0; y < h; y++){
            for(int x = 0; x < w; x++){                   
                boolean flag = false ;
                int count = 0;//ͳ�������ɫ�������
                if(x==0 || x==w-1 || y==0 || y==h-1){
                	binaryBufferedImage.setRGB(x,y,-1);//���ñ���ɫΪ��ɫ   
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
                        //б����Ϊ��ʱ��ȥ���˵�
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
                        if(count>6) flag =true;//������ֵΪ5
                        if(flag){
                            binaryBufferedImage.setRGB(x,y,-1);//���ñ���ɫΪ��ɫ                 
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