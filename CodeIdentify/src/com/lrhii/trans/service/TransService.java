package com.lrhii.trans.service;

import com.lrhii.exception.IIException;

/**
 * ѵ���ӿ�
 * @author liuhao
 *
 */
public abstract class TransService {
	/**
	 * �������
	 * @param base64Img
	 */
	public abstract void offer(String base64Img) throws IIException;
	
	/**
	 * ѵ��
	 * @throws IIException
	 */
	public abstract void trans() throws IIException;
	

}
