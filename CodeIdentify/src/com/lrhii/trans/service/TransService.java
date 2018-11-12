package com.lrhii.trans.service;

import com.lrhii.exception.IIException;

/**
 * 训练接口
 * @author liuhao
 *
 */
public abstract class TransService {
	/**
	 * 样本入库
	 * @param base64Img
	 */
	public abstract void offer(String base64Img) throws IIException;
	
	/**
	 * 训练
	 * @throws IIException
	 */
	public abstract void trans() throws IIException;
	

}
