package com.lrhii.trans.service;

import com.lrhii.exception.IIException;

/**
 * 识别接口
 * @author liuhao
 *
 */
public abstract class IdenService {
	/**
	 * 识别
	 * @return 识别字符
	 * @throws IIException
	 */
	public abstract String identify(String base64Img) throws IIException;
	
	public abstract String identifyFile(String filename) throws IIException;
}
