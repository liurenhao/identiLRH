package com.lrhii.exception;

public class IIException extends Exception {
	private String msg = "";
	private String code = "";
	public IIException(String code,String msg){
		this.msg = msg;
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "IIException [msg=" + msg + ", code=" + code + "]";
	}
	
}
