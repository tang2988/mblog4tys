package mblog.core.persist.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.Type;

/**
 * 商品表 品名 价格 库存 描述 图片 状态 vip专享
 * 
 */
@Entity
@Table(name = "mto_goods")
public class GoodsPO {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private Long opId;// 操作用户
	private String name;// 品名
	private Long price;// 价格
	private Integer storeNum;// 库存
	
	@Type(type="text")
	private String descHtm;// 描述
	private String mainPic;// 图片
	private Integer status;// 状态
	private Integer isVip;// vip专享

	private Date startTime;// 开始时间
	private Date endTime;// 结束时间

	private Date updateTime;// 更新时间

	@Version
	private Long version;//

	private Long reserve1;//
	private Long reserve2;//
	private String reserve3;//
	private String reserve4;//
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Long getOpId() {
		return opId;
	}
	public void setOpId(Long opId) {
		this.opId = opId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getPrice() {
		return price;
	}
	public void setPrice(Long price) {
		this.price = price;
	}
	public Integer getStoreNum() {
		return storeNum;
	}
	public void setStoreNum(Integer storeNum) {
		this.storeNum = storeNum;
	}
	public String getDescHtm() {
		return descHtm;
	}
	public void setDescHtm(String descHtm) {
		this.descHtm = descHtm;
	}
	public String getMainPic() {
		return mainPic;
	}
	public void setMainPic(String mainPic) {
		this.mainPic = mainPic;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getIsVip() {
		return isVip;
	}
	public void setIsVip(Integer isVip) {
		this.isVip = isVip;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public Long getVersion() {
		return version;
	}
	public void setVersion(Long version) {
		this.version = version;
	}
	public Long getReserve1() {
		return reserve1;
	}
	public void setReserve1(Long reserve1) {
		this.reserve1 = reserve1;
	}
	public Long getReserve2() {
		return reserve2;
	}
	public void setReserve2(Long reserve2) {
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
