package com.lrhii.trans.service;

import com.lrhii.exception.IIException;

/**
 * ʶ��ӿ�
 * @author liuhao
 *
 */
public abstract class IdenService {
	/**
	 * ʶ��
	 * @return ʶ���ַ�
	 * @throws IIException
	 */
	public abstract String identify(String base64Img) throws IIException;
	
	public abstract String identifyFile(String filename) throws IIException;
}
