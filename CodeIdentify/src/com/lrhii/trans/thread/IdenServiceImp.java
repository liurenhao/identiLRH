package com.lrhii.trans.thread;

import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;

import com.lrhii.exception.IIException;
import com.lrhii.svm.SVMDemo;
import com.lrhii.trans.service.IdenService;
import com.lrhii.trans.utils.Base64Util;
import com.lrhii.trans.utils.ImgUtil;

public class IdenServiceImp{
	
	private String warehousePath = "H://identityLib";

	private static int counterLast = 0;
	private static String lastDate = "";
	
	public String identify(String base64Img,String name) throws IIException {
		byte[] imgBytes = null;
		BufferedImage btyeToBI = null;
		//解码
		try {
			imgBytes = Base64Util.decodeBase64(base64Img);
			final byte[] cloneimgBytes = imgBytes.clone();
			//存进备用样本库
			new Thread(new Runnable() {
				public void run() {
					try {
						Date date = new Date();
						SimpleDateFormat d = new SimpleDateFormat("yyyyMMddHHmmss");
						String currentDate = d.format(date);
						synchronized (IdenServiceImp.class) {
							if(lastDate.equals(currentDate)){
								counterLast++;
							}else{
								counterLast = 0;
								lastDate = currentDate;        
							}
						}
						ImgUtil.byte2image(cloneimgBytes, warehousePath+"//"+lastDate + counterLast+".png");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();
		} catch (Exception e) {
			e.printStackTrace();
			throw new IIException("0001", "Base64解析报错！");
		}
		//转图像流
		try {
			btyeToBI = ImgUtil.btyeToBI(imgBytes);
		} catch (Exception e) {
			e.printStackTrace();
			throw new IIException("0004", "byte[]转图像流出错！");
		}
		//图像处理
		String[] targetSample = ImageHandle.handle(btyeToBI,name);
		//模式识别
		String result = "";
		for (int i = 0; i < targetSample.length; i++) {
			result += SVMDemo.predict("51 "+ targetSample[i]);
		}
		return result;
	}

	public static void main(String[] args) throws Exception {
		IdenServiceImp idenServiceImp = new IdenServiceImp();
		String code = "";
//		code = idenServiceImp.identify("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");
		code = idenServiceImp.identifyFile("E://identityLib_err//FmQN.png");
		System.out.println(code);
	}

	public String identifyFile(String filename) throws IIException {
//		byte[] imgBytes = null;
		BufferedImage btyeToBI = null;
//		//解码
//		try {
//			imgBytes = new File(filename).get
//			//存进备用样本库
//			ImgUtil.byte2image(imgBytes, "H://identityLib//111.png");
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new IIException("0001", "Base64解析报错！");
//		}
		//转图像流
		try {
			btyeToBI = ImageIO.read(new File(filename));
		} catch (Exception e) {
			e.printStackTrace();
			throw new IIException("0004", "byte[]转图像流出错！");
		}
		int lastIndexOf = filename.lastIndexOf("/");
		String name = filename.substring(lastIndexOf+1);
		System.out.println(name);
		//图像处理
		String[] targetSample = ImageHandle.handle(btyeToBI,name);
		//模式识别
		String result = "";
		for (int i = 0; i < targetSample.length; i++) {
			result += SVMDemo.predict("51 "+ targetSample[i]);
		}
		return result;
	}

	/**
	 * @return the warehousePath
	 */
	public String getWarehousePath() {
		return warehousePath;
	}

	/**
	 * @param warehousePath the warehousePath to set
	 */
	public void setWarehousePath(String warehousePath) {
		this.warehousePath = warehousePath;
	}
}
