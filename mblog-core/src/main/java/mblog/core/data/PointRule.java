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
 * 积分规则表
 * 
 * 名称 描述 变动类型：10注册12认证 20POS 30网赚 40消费 变动方向：0增加1减少 系数 状态 开始时间 结束时间
 */
public class PointRule  implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5837942483522905869L;
	private long id;
	private Long opId;// 操作用户
	private String serialNo;// 规则编号
	private Integer type;// 变动类型：10注册12认证 20POS 30网赚 40消费
	private Integer direction;// 变动方向：0增加1减少
	private Integer ratio;// 系数
	private Integer status;// 状态
	private Date startTime;// 开始时间
	private Date endTime;// 结束时间
	private Date updateTime;// 更新时间

	@Version
	private Long version;//
	
	private Long reserve1;//
	private Long reserve2;//
	private String reserve3;//备注
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
	public String getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getDirection() {
		return direction;
	}
	public void setDirection(Integer direction) {
		this.direction = direction;
	}
	public Integer getRatio() {
		return ratio;
	}
	public void setRatio(Integer ratio) {
		this.ratio = ratio;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
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
