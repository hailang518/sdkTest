package com.easypay.sdk.vo;


import java.io.Serializable;
import java.math.BigDecimal;


/**
 * 人民币收汇通知
 */
public class CollectNotifyReqVO implements Serializable {
	private static final long serialVersionUID = 3958342232432825500L;
	
	private String acceptId = "";              // 入账编号
	private String notifyStatus = "";          // 通知状态(1-预入账通知  2-入账通知)
	private String payerName = "";             // 付款人户名
	private String payerAccount = "";          // 付款人账号
	private String payerBankId = "";           // 付款人开户行
	private String payeeName = "";             // 收款人户名
	private String payeeAccount = "";          // 收款人账号
	private String payeeBankId = "";           // 收款人开户行
	private String remitCyCode = "";           // 汇款币种
	private BigDecimal remitAmount = null;     // 汇款金额
	private String businessType = "";          // 业务类型
	private String remitAddress = "";          // 汇款人地址
	private String remitRemark = "";           // 汇款附言
	private String remitDetail = "";           // 汇款详情
	private String transTime = "";             // 交易时间
	private String businessNo = "";            // 业务受理编号
	private String rmk1 = "";                  // 备注1
	private String rmk2 = "";                  // 备注2
	
	private String rmk3 = "";                  // 备注3

	public String getAcceptId() {
		return acceptId;
	}

	public void setAcceptId(String acceptId) {
		this.acceptId = acceptId;
	}

	public String getNotifyStatus() {
		return notifyStatus;
	}

	public void setNotifyStatus(String notifyStatus) {
		this.notifyStatus = notifyStatus;
	}

	public String getPayerName() {
		return payerName;
	}

	public void setPayerName(String payerName) {
		this.payerName = payerName;
	}

	public String getPayerAccount() {
		return payerAccount;
	}

	public void setPayerAccount(String payerAccount) {
		this.payerAccount = payerAccount;
	}

	public String getPayerBankId() {
		return payerBankId;
	}

	public void setPayerBankId(String payerBankId) {
		this.payerBankId = payerBankId;
	}

	public String getPayeeName() {
		return payeeName;
	}

	public void setPayeeName(String payeeName) {
		this.payeeName = payeeName;
	}

	public String getPayeeAccount() {
		return payeeAccount;
	}

	public void setPayeeAccount(String payeeAccount) {
		this.payeeAccount = payeeAccount;
	}

	public String getPayeeBankId() {
		return payeeBankId;
	}

	public void setPayeeBankId(String payeeBankId) {
		this.payeeBankId = payeeBankId;
	}

	public String getRemitCyCode() {
		return remitCyCode;
	}

	public void setRemitCyCode(String remitCyCode) {
		this.remitCyCode = remitCyCode;
	}

	public BigDecimal getRemitAmount() {
		return remitAmount;
	}

	public void setRemitAmount(BigDecimal remitAmount) {
		this.remitAmount = remitAmount;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getRemitAddress() {
		return remitAddress;
	}

	public void setRemitAddress(String remitAddress) {
		this.remitAddress = remitAddress;
	}

	public String getRemitRemark() {
		return remitRemark;
	}

	public void setRemitRemark(String remitRemark) {
		this.remitRemark = remitRemark;
	}

	public String getRemitDetail() {
		return remitDetail;
	}

	public void setRemitDetail(String remitDetail) {
		this.remitDetail = remitDetail;
	}

	public String getTransTime() {
		return transTime;
	}

	public void setTransTime(String transTime) {
		this.transTime = transTime;
	}

	public String getBusinessNo() {
		return businessNo;
	}

	public void setBusinessNo(String businessNo) {
		this.businessNo = businessNo;
	}

	public String getRmk1() {
		return rmk1;
	}

	public void setRmk1(String rmk1) {
		this.rmk1 = rmk1;
	}

	public String getRmk2() {
		return rmk2;
	}

	public void setRmk2(String rmk2) {
		this.rmk2 = rmk2;
	}

	public String getRmk3() {
		return rmk3;
	}

	public void setRmk3(String rmk3) {
		this.rmk3 = rmk3;
	}

	@Override
	public String toString() {
		return "CollectNotifyReqVO [acceptId=" + acceptId + ", notifyStatus=" + notifyStatus + ", payerName=" + payerName + ", payerAccount=" + payerAccount + ", payerBankId=" + payerBankId
				+ ", payeeName=" + payeeName + ", payeeAccount=" + payeeAccount + ", payeeBankId=" + payeeBankId + ", remitCyCode=" + remitCyCode + ", remitAmount=" + remitAmount + ", businessType="
				+ businessType + ", remitAddress=" + remitAddress + ", remitRemark=" + remitRemark + ", remitDetail=" + remitDetail + ", transTime=" + transTime + ", businessNo=" + businessNo
				+ ", rmk1=" + rmk1 + ", rmk2=" + rmk2 + ", rmk3=" + rmk3 + "]";
	}

}
