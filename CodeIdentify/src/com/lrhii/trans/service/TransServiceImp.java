package com.lrhii.trans.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.lrhii.exception.IIException;
import com.lrhii.trans.Const;
import com.lrhii.trans.utils.Base64Util;
import com.lrhii.trans.utils.ImgUtil;

public class TransServiceImp extends TransService {
	
	private String lastDate = "";
	private int counterLast = 0;

	@Override
	public void offer(String base64Img) throws IIException {
		byte[] imgByte = null;
		try {
			imgByte = Base64Util.decodeBase64(base64Img);
		} catch (Exception e) {
			throw new IIException("0001", "Base64½âÎö±¨´í£¡");
		}
		Date date = new Date();
		SimpleDateFormat d = new SimpleDateFormat("yyyyMMddHHmmss");
		String currentDate = d.format(date);
		if(lastDate.equals(currentDate)){
			counterLast++;
		}else{
			counterLast = 0;
		}
		ImgUtil.byte2image(imgByte, Const.OFFER_PATH + "//" + lastDate + counterLast);
	}

	@Override
	public void trans() throws IIException {
		// TODO Auto-generated method stub
		
	}
	
}
