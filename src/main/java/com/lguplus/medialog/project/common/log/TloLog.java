package com.lguplus.medialog.project.common.log;

import java.util.Random;

import com.lguplus.medialog.project.config.consts.Const;
import com.lguplus.medialog.project.config.consts.ResultCode;


public class TloLog {
	private String seqId = genSeqId();
	private String logTime;
	private String logType = "SVC";
	private String sid;
	private String resultCode = ResultCode.SUCCESS.getValue();
	private String reqTime;
	private String rspTime;
	private String clientIp;
	private String devInfo;
	private String osInfo;
	private String nwInfo = "";
	private String svcName = "";
	private String devModel = "";
	private String carrierType = "E";
	
	private String reqUri;
	private String reqParam = "";
	private String legacyCd = "";
	
	private static String genSeqId() {
		Random r = new Random();
		return String.format("%s%04d%04d", LogUtils.getCurrentTime(), r.nextInt(10000), r.nextInt(10000));
	}
//	public String getSeqId() {
//		return seqId;
//	}
//	public void setSeqId(String seqId) {
//		this.seqId = seqId;
//	}
	public String getLogTime() {
		return logTime;
	}
	public void setLogTime(String logTime) {
		this.logTime = logTime;
	}
//	public String getLogType() {
//		return logType;
//	}
//	public void setLogType(String logType) {
//		this.logType = logType;
//	}
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public String getResultCode() {
		return resultCode;
	}
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	public String getReqTime() {
		return reqTime;
	}
	public void setReqTime(String reqTime) {
		this.reqTime = reqTime;
	}
	public String getRspTime() {
		return rspTime;
	}
	public void setRspTime(String rspTime) {
		this.rspTime = rspTime;
	}
	public String getClientIp() {
		return clientIp;
	}
	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}
	public String getDevInfo() {
		return devInfo;
	}
	public void setDevInfo(String devInfo) {
		this.devInfo = devInfo;
	}
	public String getOsInfo() {
		return osInfo;
	}
	public void setOsInfo(String osInfo) {
		this.osInfo = osInfo;
	}
//	public String getNwInfo() {
//		return nwInfo;
//	}
//	public void setNwInfo(String nwInfo) {
//		this.nwInfo = nwInfo;
//	}
	public String getSvcName() {
		return svcName;
	}
	public void setSvcName(String svcName) {
		this.svcName = svcName;
	}
//	public String getDevModel() {
//		return devModel;
//	}
//	public void setDevModel(String devModel) {
//		this.devModel = devModel;
//	}
//	public String getCarrierType() {
//		return carrierType;
//	}
//	public void setCarrierType(String carrierType) {
//		this.carrierType = carrierType;
//	}
	
	public String getReqUri() {
		return reqUri;
	}
	public void setReqUri(String reqUri) {
		this.reqUri = reqUri;
	}
	public String getReqParam() {
		return reqParam;
	}
	public void setReqParam(String reqParam) {
		this.reqParam = reqParam;
	}
	public String getLegacyCd() {
		return legacyCd;
	}
	public void setLegacyCd(String legacyCd) {
		this.legacyCd = legacyCd;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SEQ_ID=").append(seqId);
//		builder.append("|LOG_TIME=").append(logTime);
//		builder.append("|LOG_TYPE=").append(logType);
		builder.append("|SID=").append(sid);
		builder.append("|RESULT_CODE=").append(resultCode);
		builder.append("|REQ_TIME=").append(reqTime);
		builder.append("|RSP_TIME=").append(rspTime);
		builder.append("|CLIENT_IP=").append(clientIp);
//		builder.append("|DEV_INFO=").append(devInfo);
//		builder.append("|OS_INFO=").append(osInfo);
//		builder.append("|NW_INFO=").append(nwInfo);
//		builder.append("|SVC_NAME=").append(svcName);
//		builder.append("|DEV_MODEL=").append(devModel);
//		builder.append("|CARRIER_TYPE=").append(carrierType);
//		builder.append("|FUNC_ID=").append(funcId);
//		builder.append("|SUB_CODE=").append(subCode);
		builder.append("|REQ_URI=").append(reqUri);
		builder.append("|REQ_PARAM=").append(reqParam);
		builder.append("|LEGACY_CD=").append(legacyCd);

		return builder.toString();
	}

}
