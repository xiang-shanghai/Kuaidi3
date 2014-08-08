package com.android.kuaidi.utils;

import java.util.List;

public class ICKDInfo {
	private int status; //查询结果状态，0|1|2|3|4，0表示查询失败，1正常，2派送中，3已签收，4退回,5其他问题
	private int errorCode; //错误代码，0无错误，1单号不存在，2验证码错误，3链接查询服务器失败，4程序内部错误，5程序执行错误，6快递单号格式错误，7快递公司错误，10未知错误，20API错误，21API被禁用，22API查询量耗尽
	private String message; //错误消息
	private String mailNo; //快递单号
	private String expTextName; //快递公司中文名
	private List<Data> dataList;
	private String tel;
	
	public class Data { //进度
		private String time; 
		private String context;
		public String getTime() {
			return time;
		}
		public void setTime(String time) {
			this.time = time;
		}
		public String getContext() {
			return context;
		}
		public void setContext(String context) {
			this.context = context;
		}
				
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMailNo() {
		return mailNo;
	}

	public void setMailNo(String mailNo) {
		this.mailNo = mailNo;
	}

	public String getExpTextName() {
		return expTextName;
	}

	public void setExpTextName(String expTextName) {
		this.expTextName = expTextName;
	}

	public List<Data> getData() {
		return dataList;
	}

	public void setData(List<Data> dataList) {
		this.dataList = dataList;
	}
	
	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}
	
	
	public String getStatusString(){
		String result = "";
		switch (getStatus()) {
		case 0:
			result = "查询失败";
			break;
		case 1:
			result = "正常";
			break;
		case 2:
			result = "派送中";
			break;
		case 3:
			result = "已签收";
			break;
		case 4:
			result = "退回";
			break;
		case 5:
			result = "其他问题";
			break;

		default:
			break;
		}
		
		return result;
	}
	
	public String getErrorCodeString(){
		String result = "";
		switch (getErrorCode()) {
		case 0:
			result = "无错误";
			break;
		case 1:
			result = "单号不存在";
			break;
		case 2:
			result = "验证码错误";
			break;
		case 3:
			result = "链接查询服务器失败";
			break;
		case 4:
			result = "程序内部错误";
			break;
		case 5:
			result = "程序执行错误";
			break;
		case 6:
			result = "快递单号格式错误";
			break;
		case 7:
			result = "快递公司错误";
			break;
		case 10:
			result = "未知错误";
			break;
		case 20:
			result = "API错误";
			break;
		case 21:
			result = "API被禁用";
			break;
		case 22:
			result = "API查询量耗尽";
			break;

		default:
			break;
		}
		
		return result;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}
	
	
}
