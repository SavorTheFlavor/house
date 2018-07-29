package com.me.house.common.result;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import com.google.common.base.Joiner;
import com.google.common.collect.Maps;

public class ResultMsg {
	public static final String errorMsgKey = "errorMsg";
	
	public static final String successMsgKey = "successMsg";
	
	private String errorMsg;
	
	private String successMsg;
	
	public boolean isSuccess(){
		return errorMsg == null;
	}
	

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getSuccessMsg() {
		return successMsg;
	}

	public void setSuccessMsg(String successMsg) {
		this.successMsg = successMsg;
	}
	
	
	public static ResultMsg errorMsg(String msg){
		ResultMsg resultMsg = new ResultMsg();
		resultMsg.setErrorMsg(msg);
		return resultMsg;
	}
	
	public static ResultMsg successMsg(String msg){
		ResultMsg resultMsg = new ResultMsg();
		resultMsg.setSuccessMsg(msg);
		return resultMsg;
	}
	
	public Map<String, String> asMap(){
		Map<String, String> map = Maps.newHashMap();
		map.put(successMsgKey, successMsg);
		map.put(errorMsgKey, errorMsg);
		return map;
	}

	public String asUrlParams(){
		Map<String, String> map = asMap();
		Map<String, String> newMap = Maps.newHashMap();
		map.forEach((k, v) -> {
			try {
				newMap.put(k, URLEncoder.encode(v, "utf-8"));
			}catch (Exception e){

			}
		});
		return Joiner.on("&").useForNull("").withKeyValueSeparator("=").join(newMap);
	}

	public static void main(String[] args) {
		ResultMsg resultMsg = ResultMsg.successMsg("http://www.w3school.com.cn/tags/att_form_enctype.asp?asa=1231&ased=123&qq=1231");
		System.out.println(resultMsg.asUrlParams());
	}

}
