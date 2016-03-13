package mblog.core.data;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author Beldon
 */
public class MyPOS implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -2891229977878793950L;
    private long id;
    private String customerName;//客户姓名
    private String agencyName;//所属代理机构名称
    private String terminalId;//终端编号
    private String deal_time;//交易时间
    private String idcard;//身份证
    private String moblieNoV;//手机号码
    private String sysSource;//刷卡平台
    
    private Long reserve1;//userid
    private String reserve2;//IPOS
    private String reserve3;//
    private String reserve4;//
    
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
	public String getAgencyName() {
		return agencyName;
	}
	public void setAgencyName(String agencyName) {
		this.agencyName = agencyName;
	}
	public String getTerminalId() {
		return terminalId;
	}
	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}
	public String getDeal_time() {
		return deal_time;
	}
	public void setDeal_time(String deal_time) {
		this.deal_time = deal_time;
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
	public String getSysSource() {
		return sysSource;
	}
	public void setSysSource(String sysSource) {
		this.sysSource = sysSource;
	}
	public Long getReserve1() {
		return reserve1;
	}
	public void setReserve1(Long reserve1) {
		this.reserve1 = reserve1;
	}
	public String getReserve2() {
		return reserve2;
	}
	public void setReserve2(String reserve2) {
		this.reserve2 = reserve2;
	}
	public String getReserve3() {
		return reserve3;
	}
	public void setReserve3(String reserve3) {
		this.reserve3 = reserve3;
	}
	public String getReserve4() {
		return reserve4;
	}
	public void setReserve4(String reserve4) {
		this.reserve4 = reserve4;
	}
    
	
	
}
