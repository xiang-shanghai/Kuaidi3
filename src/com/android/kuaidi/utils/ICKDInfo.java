package com.android.kuaidi.utils;

import java.util.List;

public class ICKDInfo {
	private int status; //��ѯ���״̬��0|1|2|3|4��0��ʾ��ѯʧ�ܣ�1������2�����У�3��ǩ�գ�4�˻�,5��������
	private int errorCode; //������룬0�޴���1���Ų����ڣ�2��֤�����3���Ӳ�ѯ������ʧ�ܣ�4�����ڲ�����5����ִ�д���6��ݵ��Ÿ�ʽ����7��ݹ�˾����10δ֪����20API����21API�����ã�22API��ѯ���ľ�
	private String message; //������Ϣ
	private String mailNo; //��ݵ���
	private String expTextName; //��ݹ�˾������
	private List<Data> dataList;
	private String tel;
	
	public class Data { //����
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
			result = "��ѯʧ��";
			break;
		case 1:
			result = "����";
			break;
		case 2:
			result = "������";
			break;
		case 3:
			result = "��ǩ��";
			break;
		case 4:
			result = "�˻�";
			break;
		case 5:
			result = "��������";
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
			result = "�޴���";
			break;
		case 1:
			result = "���Ų�����";
			break;
		case 2:
			result = "��֤�����";
			break;
		case 3:
			result = "���Ӳ�ѯ������ʧ��";
			break;
		case 4:
			result = "�����ڲ�����";
			break;
		case 5:
			result = "����ִ�д���";
			break;
		case 6:
			result = "��ݵ��Ÿ�ʽ����";
			break;
		case 7:
			result = "��ݹ�˾����";
			break;
		case 10:
			result = "δ֪����";
			break;
		case 20:
			result = "API����";
			break;
		case 21:
			result = "API������";
			break;
		case 22:
			result = "API��ѯ���ľ�";
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
