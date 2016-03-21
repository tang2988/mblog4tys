package mblog.core.data;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

/**
 * 积分表 用户 当前积分 获得总积分 更新时间 消费总积分
 */
public class Point  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2684718048305082026L;
	private long id;
	private Long userId;// 用户
	private Long curPoint;// 当前积分
	private Long hadPoint;// 获得总积分
	private Long usedPoint;// 消费总积分

	private Date updateTime;// 更新时间

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
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getCurPoint() {
		return curPoint;
	}
	public void setCurPoint(Long curPoint) {
		this.curPoint = curPoint;
	}
	public Long getHadPoint() {
		return hadPoint;
	}
	public void setHadPoint(Long hadPoint) {
		this.hadPoint = hadPoint;
	}
	public Long getUsedPoint() {
		return usedPoint;
	}
	public void setUsedPoint(Long usedPoint) {
		this.usedPoint = usedPoint;
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
