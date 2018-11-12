package com.lrhii.trans.thread;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.lrhii.exception.IIException;
import com.lrhii.svm.SVMDemo;
import com.lrhii.trans.core.BinaryCore;
import com.lrhii.trans.core.NoiseReduce;

public class ImageHandle {
	public static String[] handle(BufferedImage targetImage,String name) throws IIException{
		//��ֵ��
		BufferedImage binaryImage = null;
		try {
			binaryImage = BinaryCore.binary(targetImage);
			//�������������
			ImageIO.write(binaryImage, "png", new File("H://identityLib//222.png"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new IIException("0010", "��ֵ������ʧ�ܣ�");
		}
		//���� -- 8������
		NoiseReduce.clear8Area(binaryImage);
		try { 
			ImageIO.write(binaryImage, "png", new File("H://identityLib//333.png"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		//ͼƬ�ָ�
		List<BufferedImage> subImages = new ArrayList<BufferedImage>();
		try {
			subImages = splitImage(binaryImage);
			if(subImages.size()!=4){//Ŀǰ�̶�4λ��ĸ
				throw new IIException("0020", "ͼ��ָ�ʧ�ܣ�");
			}
			//�������������
			String path = "E://translib//";
			for (int i = 0; i < subImages.size(); i++) {
				//�жϴ�д��ĸ
				if((int)name.charAt(i)>64 && (int)name.charAt(i)<91){
					File file = new File(path+"upper//"+name.charAt(i));
					String[] list = file.list();
					int a = 0;
					if(list.length>0){
						String nameF = list[list.length-1];
						String substring = nameF.substring(0, nameF.indexOf("."));
						a = Integer.parseInt(substring) + 1;
					}
					ImageIO.write(subImages.get(i), "png", new File(path+"upper//"+name.charAt(i)+"//"+a+".png"));
				}
				//�ж�Сд��ĸ
				else if((int)name.charAt(i)>96 && (int)name.charAt(i)<123){
					File file = new File(path+"lower//"+name.charAt(i));
					String[] list = file.list();
					int a = 0;
					if(list.length>0){
						String nameF = list[list.length-1];
						String substring = nameF.substring(0, nameF.indexOf("."));
						a = Integer.parseInt(substring) + 1;
					}
					ImageIO.write(subImages.get(i), "png", new File(path+"lower//"+name.charAt(i)+"//"+a+".png"));
				}
				//�ж�����
				else if((int)name.charAt(i)>47 && (int)name.charAt(i)<58){
					File file = new File(path+"number//"+name.charAt(i));
					String[] list = file.list();
					int a = 0;
					if(list.length>0){
						String nameF = list[list.length-1];
						String substring = nameF.substring(0, nameF.indexOf("."));
						a = Integer.parseInt(substring) + 1;
					}
					ImageIO.write(subImages.get(i), "png", new File(path+"number//"+name.charAt(i)+"//"+a+".png"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new IIException("0020", "ͼ��ָ�ʧ�ܣ�");
		}
		//ʶ��
		String[] result = classify(subImages);
		return result;
	}
	
    /**
     * �ָ�ͼƬ
     * @param img
     * @return
     * @throws Exception
     */
    private static List<BufferedImage> splitImage(BufferedImage img)  
            throws Exception {  
        List<BufferedImage> subImgs = new ArrayList<BufferedImage>();  
        int width = img.getWidth();  
        int height = img.getHeight();  
        List<Integer> weightlist = new ArrayList<Integer>();  
        for (int x = 0; x < width; ++x) {  
            int count = 0;  
            for (int y = 0; y < height; ++y) {  
                if (isBlack(img.getRGB(x, y))) {  
                    count++;  
                }  
            }  
            weightlist.add(count);  
        }
        for (int i = 0; i < weightlist.size();i++) {  
            int length = 0;  
            while(true) {
            	if(weightlist.get(i) > 1){
            		i++;
                    length++;  
            	}else{//���С�ڵ���1 ���ж�  ������߶���ֵ��������һ�ߵ�ֵ����1����Ϊ��Ч
            		if(i==0 && i<weightlist.size()-1){
            			if(weightlist.get(i+1) > 1){
            				i++;
                            length++;
            			}else{
            				break;
            			}
            		}else if(i==weightlist.size()-1 && i>0){
            			if(weightlist.get(i-1) > 1){
            				i++;
                            length++;
            			}else{
            				break;
            			}
            		}else{
            			if((weightlist.get(i+1) > 0 && weightlist.get(i-1) >1)
            					|| (weightlist.get(i-1) > 0 && weightlist.get(i+1) >1)){
            				i++;
                            length++;
            			}else{
            				break;
            			}
            		}
            	}
                if(i>=weightlist.size()){
                	break;
                }
            }  
//            if (length > 12) {  
//                subImgs.add(removeBlank(img.getSubimage(i - length - 1, 0,  
//                        length / 2, height)));
//                subImgs.add(removeBlank(img.getSubimage(i - length / 2 - 1, 0,  
//                        length / 2, height)));  
//            } else if (length > 3) {  
//                subImgs.add(removeBlank(img.getSubimage(i - length - 1, 0,  
//                        length, height)));
//            }  
            if (length > 2) {
              subImgs.add(removeBlank(img.getSubimage(i - length - 1, 0,  
                      length, height)));
          }  
        }
        return subImgs;
    }
	
    /**
     * ���¼���
     * @param img
     * @return
     * @throws Exception
     */
    private static BufferedImage removeBlank(BufferedImage img) throws Exception {  
        int width = img.getWidth();  
        int height = img.getHeight();  
        int start = 0;  
        int end = 0;  
        Label1: for (int y = 0; y < height; ++y) {  
            int count = 0;  
            for (int x = 0; x < width; ++x) {  
                if (isBlack(img.getRGB(x, y))) {  
                    count++;  
                }  
                if (count >= 1) {  
                    start = y;  
                    break Label1;  
                }  
            }  
        }  
        Label2: for (int y = height - 1; y >= 0; --y) {  
            int count = 0;  
            for (int x = 0; x < width; ++x) {  
                if (isBlack(img.getRGB(x, y))) {  
                    count++;  
                }  
                if (count >= 1) {  
                    end = y;  
                    break Label2;  
                }  
            }  
        }  
        return img.getSubimage(0, start, width, end - start + 1);  
    }
    
    /**
     * �ж��Ƿ��ɫ���ص�
     * @param colorInt
     * @return
     */
    public static boolean isBlack(int colorInt)  
    {  
        Color color = new Color(colorInt);  
        if (color.getRed() + color.getGreen() + color.getBlue() <= 0)  
        {  
            return true;  
        }  
        return false;  
    }  

    /**
     * �ж��Ƿ��ɫ���ص�
     * @param colorInt
     * @return
     */
    public static boolean isWhite(int colorInt)  
    {  
        Color color = new Color(colorInt);  
        if (color.getRed() + color.getGreen() + color.getBlue() > 0)  
        {  
            return true;  
        }  
        return false;  
    }
    
    /**
     * ͼ���һ    16*16px
     */
    public static BufferedImage resizeImage(BufferedImage image){
    	BufferedImage bufferedImage = new BufferedImage(16, 16, BufferedImage.TYPE_3BYTE_BGR);
    	bufferedImage.getGraphics().drawImage(image, 0, 0, 16, 16, null);
    	return bufferedImage;
    }
    
    /**
     * ������ȡ  
     * 16*16��ģ��    �ָ��16��4*4��С����  ������Ч���صĸ���
     */
    public static int[] extractFeature(BufferedImage image){
    	//�и��16�� 4*4px ��С���� 
    	int modelSize = 4;
    	int index = 0;
    	BufferedImage[] images = new BufferedImage[modelSize*modelSize];
    	for(int i=0;i<image.getHeight();i+=modelSize){
    		for(int j=0;j<image.getWidth();j+=modelSize){
    			images[index] = image.getSubimage(j, i, modelSize, modelSize);
    			index++;
    		}
    	}
    	int[] blackNums = new int[modelSize*modelSize];
    	for(int i=0;i<images.length;i++){
    		int w = images[i].getWidth();
    		int h = images[i].getHeight();
    		int blackNum = 0;
    		for(int y=0;y<h;y++){
    			for(int x=0;x<w;x++){
    				if(isBlack(images[i].getRGB(x, y))){//��¼��Ч���ص�ĸ���
    					blackNum++;
    				}
        		}
    		}
    		blackNums[i] = blackNum;
    	}
    	return blackNums;
    }
    
    public static String[] classify(List<BufferedImage> subImages){
    	String[] result = new String[subImages.size()];
    	for (int i = 0; i < subImages.size(); i++) {
    		BufferedImage bufferedImage = subImages.get(i);
    		//ͼ���һ
    		BufferedImage resizeImage = resizeImage(bufferedImage);
    		//������ȡ
        	int[] blackNums = extractFeature(resizeImage);
        	//����ʶ��
        	result[i] = SVMDemo.arrayToString(blackNums);
		}
    	return result;
    }
}
