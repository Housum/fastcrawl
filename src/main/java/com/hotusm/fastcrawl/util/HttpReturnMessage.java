package com.hotusm.fastcrawl.util;

public class HttpReturnMessage {
	// 是否成功，比较的是statusCode是否为200
	private boolean success;
	// 状态码
	private int statusCode;
	// 返回的结果
	private String result;
	//路径
	private String url;

	public HttpReturnMessage(int code) {
		this.setStatusCode(code);
	}

	public HttpReturnMessage(String result) {
		this.setResult(result);
	}

	public HttpReturnMessage(int code, String result) {
		this.setStatusCode(code);
		this.setResult(result);
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
		// 200区间的都是成功
		if (this.statusCode >= 200 && this.statusCode < 300) {
			this.success = true;
		} else {
			this.success = false;
		}
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return "{\"Status\": " + this.statusCode + ", \"Result\": " + this.result + "}";
	}

}
