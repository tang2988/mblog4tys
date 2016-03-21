package mblog.core.persist.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.*;

/**
 * 积分明细表 父ID 变动前积分 积分变动值 变动后积分 来源 时间 关联id
 * 
 */
@Entity
@Table(name = "mto_point_detail")
public class PointDetailPO {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private Long pid;// 父ID
	private Long userId;// 用户
	private Long afterPoint;// 变动后积分
	private Long beforePoint;// 变动前总积分
	private Long addPoint;// 变动积分

	private Date updateTime;// 更新时间
	private Long opId;// 操作用户
	private Long relateId;// 关联id
	private String sourceDesc;// 来源
	private String reserve1;//
	private Long reserve2;//
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Long getPid() {
		return pid;
	}
	public void setPid(Long pid) {
		this.pid = pid;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getAfterPoint() {
		return afterPoint;
	}
	public void setAfterPoint(Long afterPoint) {
		this.afterPoint = afterPoint;
	}
	public Long getBeforePoint() {
		return beforePoint;
	}
	public void setBeforePoint(Long beforePoint) {
		this.beforePoint = beforePoint;
	}
	public Long getAddPoint() {
		return addPoint;
	}
	public void setAddPoint(Long addPoint) {
		this.addPoint = addPoint;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public Long getOpId() {
		return opId;
	}
	public void setOpId(Long opId) {
		this.opId = opId;
	}
	public Long getRelateId() {
		return relateId;
	}
	public void setRelateId(Long relateId) {
		this.relateId = relateId;
	}
	public String getSourceDesc() {
		return sourceDesc;
	}
	public void setSourceDesc(String sourceDesc) {
		this.sourceDesc = sourceDesc;
	}
	public String getReserve1() {
		return reserve1;
	}
	public void setReserve1(String reserve1) {
		this.reserve1 = reserve1;
	}
	public Long getReserve2() {
		return reserve2;
	}
	public void setReserve2(Long reserve2) {
		this.reserve2 = reserve2;
	}
	
	

}
