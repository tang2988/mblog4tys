package mblog.core.data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Beldon
 */
public class CardTransactionRecord implements Serializable {
	private static final long serialVersionUID = 8090260110013930023L;

	private long id;
    private String customerName;//客户姓名
    private BigDecimal feeAmt;//手续费金额
    private String agencyName;//所属代理机构名称
    private BigDecimal transacount;//交易金额
    private String terminalId;//终端编号
    private String deal_data;//交易日期
    private String deal_time;//交易时间
    private String dealTypeName;//交易类型
    private String bankcardNumber;//银行卡号
    private String idcard;//身份证
    private String moblieNoV;//手机号码
    private String serialNumber;//交易流水号
    private String onlyCode;//交易流水号
    private String sysSource;//刷卡平台
    
    private Date dealTime;//
    private Long point;//返现
    private Long point1;//个人返现
    private Long point2;//全体返现
    private Long userId;//
    private String realterminalId;
    
    private Date createTime;//
    @Override
    public String toString() {
    	return "serialNumber:"+serialNumber+",transacount:"+transacount+",moblieNoV:"+moblieNoV+",terminalId:"+terminalId;
    }
    
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public BigDecimal getFeeAmt() {
		return feeAmt;
	}
	public void setFeeAmt(BigDecimal feeAmt) {
		this.feeAmt = feeAmt;
	}
	public String getAgencyName() {
		return agencyName;
	}
	public void setAgencyName(String agencyName) {
		this.agencyName = agencyName;
	}
	public BigDecimal getTransacount() {
		return transacount;
	}
	public void setTransacount(BigDecimal transacount) {
		this.transacount = transacount;
	}
	public String getTerminalId() {
		return terminalId;
	}
	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}
	public String getDeal_data() {
		return deal_data;
	}
	public void setDeal_data(String deal_data) {
		this.deal_data = deal_data;
	}
	public String getDeal_time() {
		return deal_time;
	}
	public void setDeal_time(String deal_time) {
		this.deal_time = deal_time;
	}
	public String getDealTypeName() {
		return dealTypeName;
	}
	public void setDealTypeName(String dealTypeName) {
		this.dealTypeName = dealTypeName;
	}
	public String getBankcardNumber() {
		return bankcardNumber;
	}
	public void setBankcardNumber(String bankcardNumber) {
		this.bankcardNumber = bankcardNumber;
	}
	public String getIdcard() {
		return idcard;
	}
	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}
	public String getMoblieNoV() {
		return moblieNoV;
	}
	public void setMoblieNoV(String moblieNoV) {
		this.moblieNoV = moblieNoV;
	}
	public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	public String getOnlyCode() {
		return onlyCode;
	}
	public void setOnlyCode(String onlyCode) {
		this.onlyCode = onlyCode;
	}
	public String getSysSource() {
		return sysSource;
	}
	public void setSysSource(String sysSource) {
		this.sysSource = sysSource;
	}

	public Date getDealTime() {
		return dealTime;
	}

	public void setDealTime(Date dealTime) {
		this.dealTime = dealTime;
	}

	public Long getPoint() {
		return point;
	}

	public void setPoint(Long point) {
		this.point = point;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getRealterminalId() {
		return realterminalId;
	}

	public void setRealterminalId(String realterminalId) {
		this.realterminalId = realterminalId;
	}

	public Long getPoint1() {
		return point1;
	}

	public void setPoint1(Long point1) {
		this.point1 = point1;
	}

	public Long getPoint2() {
		return point2;
	}

	public void setPoint2(Long point2) {
		this.point2 = point2;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
    
	
}
