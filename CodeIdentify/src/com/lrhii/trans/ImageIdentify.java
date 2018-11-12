package com.lrhii.trans;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import com.lrhii.svm.SVMDemo;


//import org.apache.commons.httpclient.HttpClient;
//import org.apache.commons.httpclient.HttpStatus;
//import org.apache.commons.httpclient.methods.GetMethod;
//import org.apache.commons.io.IOUtils;


/**
 * @author Administrator
 *
 */
public class ImageIdentify {  
	  
    private static Map<BufferedImage, String> trainMap = null;  
    private static int index = 0;  
  
    public static int isBlack(int colorInt) {  
        Color color = new Color(colorInt);  
        if (color.getRed() + color.getGreen() + color.getBlue() <= 0) {  //二值化后以0位临界值区分黑白
            return 1;  
        }  
        return 0;  
    }  
  
    public static int isWhite(int colorInt) {  
        Color color = new Color(colorInt);  
        if (color.getRed() + color.getGreen() + color.getBlue() > 0) {  
            return 1;  
        }  
        return 0;  
    }  
  
    public static BufferedImage removeBackgroud(String picFile)  
            throws Exception {  
        BufferedImage img = ImageIO.read(new File(picFile));  
        return img;  
    }  
  
    /**
     * 上下剪切
     * @param img
     * @return
     * @throws Exception
     */
    public static BufferedImage removeBlank(BufferedImage img) throws Exception {  
        int width = img.getWidth();  
        int height = img.getHeight();  
        int start = 0;  
        int end = 0;  
        Label1: for (int y = 0; y < height; ++y) {  
            int count = 0;  
            for (int x = 0; x < width; ++x) {  
                if (isBlack(img.getRGB(x, y)) == 1) {  
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
                if (isBlack(img.getRGB(x, y)) == 1) {  
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
     * 分割图片
     * @param img
     * @return
     * @throws Exception
     */
    public static List<BufferedImage> splitImage(BufferedImage img)  
            throws Exception {  
        List<BufferedImage> subImgs = new ArrayList<BufferedImage>();  
        int width = img.getWidth();  
        int height = img.getHeight();  
        List<Integer> weightlist = new ArrayList<Integer>();  
        for (int x = 0; x < width; ++x) {  
            int count = 0;  
            for (int y = 0; y < height; ++y) {  
                if (isBlack(img.getRGB(x, y)) == 1) {  
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
            	}else{//如果小于等于1 则判断  如果两边都有值，且任意一边的值大于1，则为有效
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
            if (length > 2) {
                subImgs.add(removeBlank(img.getSubimage(i - length - 1, 0,  
                        length, height)));
            }  
        }
        return subImgs;  
    }  
  
    /**
     * 载入训练图像
     * @return
     * @throws Exception
     */
    public static Map<BufferedImage, String> loadTrainData() throws Exception {  
        if (trainMap == null) {  
            Map<BufferedImage, String> map = new HashMap<BufferedImage, String>();  
            File dir = new File("E://train2");  
            File[] files = dir.listFiles();  
            for (File file : files) {  
                map.put(ImageIO.read(file), file.getName().charAt(0) + "");
            }  
            trainMap = map;  
        }  
        return trainMap;  
    }  
  
    public static String getSingleCharOcr(BufferedImage img,  
            Map<BufferedImage, String> map) {  
        String result = "";  
        int width = img.getWidth();  
        int height = img.getHeight();  
        int min = width * height;  
        for (BufferedImage bi : map.keySet()) {  
            int count = 0;  
            int widthmin = width < bi.getWidth() ? width : bi.getWidth();  
            int heightmin = height < bi.getHeight() ? height : bi.getHeight();  
            Label1: for (int x = 0; x < widthmin; ++x) {  
                for (int y = 0; y < heightmin; ++y) {  
                    if (isBlack(img.getRGB(x, y)) != isBlack(bi.getRGB(x, y))) {  
                        count++;
                        if (count >= min)  
                            break Label1;  
                    }  
                }  
            }  
            if (count < min) {  
                min = count;  
                result = map.get(bi);  
            }  
        }  
        return result;  
    }  
  
    public static String getAllOcr(String file) throws Exception {  
        BufferedImage img = removeBackgroud(file);
        List<BufferedImage> listImg = splitImage(img);  
        Map<BufferedImage, String> map = loadTrainData();  
        String result = "";
        for (BufferedImage bi : listImg) {  
//            result += getSingleCharOcr(bi, map);  
        	result += getSingleChar(bi);
        }  
        ImageIO.write(img, "PNG", new File("E://img/" + result + ".png"));  
        return result;  
    }  
  
    public static char getSingleChar(BufferedImage bi){
    	BufferedImage resizeImage = resizeImage(bi);
    	int[] blackNums = extractFeature(resizeImage);
    	return SVMDemo.predict("51 "+arrayToString(blackNums));
    }
    
//    public static void downloadImage() {  
//        HttpClient httpClient = new HttpClient();  
//        GetMethod getMethod = null;  
//        for (int i = 0; i < 30; i++) {  
//            getMethod = new GetMethod("http://www.pkland<a href='http://lib.csdn.net/base/dotnet' class='replace_word' title='.NET知识库' target='_blank' style='color:#df3434; font-weight:bold;'>.NET</a>/img.<a href='http://lib.csdn.net/base/php' class='replace_word' title='PHP知识库' target='_blank' style='color:#df3434; font-weight:bold;'>PHP</a>?key="  
//                    + (2000 + i));  
//            try {  
//                // 执行getMethod  
//                int statusCode = httpClient.executeMethod(getMethod);  
//                if (statusCode != HttpStatus.SC_OK) {  
//                    System.err.println("Method failed: "  
//                            + getMethod.getStatusLine());  
//                }  
//                // 读取内容  
//                String picName = "d://tmp//" + i + ".jpg";  
//                InputStream inputStream = getMethod.getResponseBodyAsStream();  
//                OutputStream outStream = new FileOutputStream(picName);  
//                IOUtils.copy(inputStream, outStream);  
//                outStream.close();  
//                System.out.println(i + "OK!");  
//            } catch (Exception e) {  
//                e.printStackTrace();  
//            } finally {  
//                // 释放连接  
//                getMethod.releaseConnection();  
//            }  
//        }  
//    }  
  
    /**
     * 训练图像
     * @param source
     * @throws Exception
     */
    public static String trainData(String source) throws Exception {  
        File dir = new File(source);  
        File[] files = dir.listFiles();
        int index = 0;
        StringBuffer buffer = new StringBuffer();
        for (File file : files) {  
            BufferedImage img = removeBackgroud(source+ "//" + file.getName());  
//            System.out.println(file.getName());
            List<BufferedImage> listImg = splitImage(img);  
            if (listImg.size() == 4) {  
                for (int j = 0; j < listImg.size(); ++j) {
                	BufferedImage resizeImage = resizeImage(listImg.get(j));
                	int[] blackNums = extractFeature(resizeImage);
//                	System.out.println(getLable(file.getName().charAt(j))+" "+arrayToString(blackNums));
                	buffer.append(getLable(file.getName().charAt(j))+" "+arrayToString(blackNums)+"\r\n");
//                    ImageIO.write(resizeImage, "JPG", new File("E://train2//"  
//                            + file.getName().charAt(j) + "-" + (index++)  
//                            + ".jpg"));  
                }  
            }else{
            	index++;
            }
        }
        System.out.println("无效数据个数："+index);
		return buffer.toString();
    }  
  
    /**
     * 特征提取  
     * 16*16的模型    分割成16个4*4的小方格  计算有效像素的个数
     */
    public static int[] extractFeature(BufferedImage image){
    	//切割成16个 4*4px 的小方格 
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
    				if(isBlack(images[i].getRGB(x, y)) == 1){//记录有效像素点的个数
    					blackNum++;
    				}
        		}
    		}
    		blackNums[i] = blackNum;
    	}
    	return blackNums;
    }
    
    /**
     * 输出符合SVM标准的特征字符串
     * @param array
     * @return
     */
    public static String arrayToString(int[] array){
    	String str = "";
    	for (int i = 0; i < array.length; i++) {
    		if(i==array.length-1){
    			str += i + ":" + array[i]+"";
    		}else{
    			str += i + ":" + array[i]+" ";
    		}
		}
    	return str;
    }
    
    /**
     * 图像归一    16*16px
     */
    public static BufferedImage resizeImage(BufferedImage image){
    	BufferedImage bufferedImage = new BufferedImage(16, 16, BufferedImage.TYPE_3BYTE_BGR);
    	bufferedImage.getGraphics().drawImage(image, 0, 0, 16, 16, null);
    	return bufferedImage;
    }
    
    public static int getLable(char a){
    	return (int)a;
    }
    
    /**
     * 对比识别
     * @param tagBlackNum
     * @return
     */
    public static String compare(int[] tagBlackNum){
    	List<Model> models = loadModelMap();
    	
    	return null;
    }
    
    /**
     * 载入特征库
     * @return
     */
    public static List<Model> loadModelMap(){
    	List<Model> list = new ArrayList<Model>();
        File dir = new File("E://train2");  
        File[] files = dir.listFiles();  
        for (File file : files) {  
            Model model = new ImageIdentify().new Model();
            int[] blackNums = null;
			try {
				blackNums = extractFeature(ImageIO.read(file));
			} catch (IOException e) {
				e.printStackTrace();
			}
            model.setArray(blackNums);
            model.setCh(String.valueOf(file.getName().charAt(0)));
            list.add(model);
        }
        return list;
    }
    
    /** 
     * @param args 
     * @throws Exception 
     */  
    public static void main(String[] args) throws Exception {  
//         downloadImage();  
//        for (int i = 0; i < 30; ++i) {  
//            String text = getAllOcr("d://img2//" + i + ".jpg");  
//            System.out.println(i + ".jpg = " + text);  
////        }  
//        String text = getAllOcr("E://source-binary1//40cz.png");
//        System.out.println("check.png = " + text);
    	String trainData = trainData("E://source-binary");
    	File file = new File("E://svmTmp//a.txt");
    	BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
    	bufferedWriter.write(trainData);
    	bufferedWriter.flush();
    	bufferedWriter.close();
    }
    
    class Model{
    	int[] array = null;
    	String ch = "";
		public int[] getArray() {
			return array;
		}
		public void setArray(int[] array) {
			this.array = array;
		}
		public String getCh() {
			return ch;
		}
		public void setCh(String ch) {
			this.ch = ch;
		}
    }
    
    
}  
