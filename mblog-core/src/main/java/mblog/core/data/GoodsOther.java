package mblog.core.data;

import java.io.Serializable;
import java.util.Date;

/**
 * 商品订单表 商品ID 订单状态：0下单成功1支付成功2发货成功3完成4退货申请5退款完成 时间 备注
 * 
 */
public class GoodsOther implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6455917447470579379L;
	private long id;
	private Long userId;//
	private Long goodsId;// 商品ID
	private Integer status;// 订单状态：0下单成功1支付成功2发货成功3完成4退货申请5退款完成
	private Date createTime;// 
	private Long opId;// 操作用户
	private Date updateTime;// 更新时间
	private Integer buyNum;//
	private Long cost;//

	private Long version;//

	private Long reserve1;//
	private Long reserve2;//
	private String reserve3;//订单处理备注
	private String remark;//下单备注
	
	public static Integer STATUS_BUYED=0;
	public static Integer STATUS_PAYED=1;
	public static Integer STATUS_DELIVERED=2;
	public static Integer STATUS_FINISHED=3;
	public static Integer STATUS_RETURNED=4;
	public static Integer STATUS_RETURNOK=5;
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
	public Long getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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
	public Integer getBuyNum() {
		return buyNum;
	}
	public void setBuyNum(Integer buyNum) {
		this.buyNum = buyNum;
	}
	public Long getCost() {
		return cost;
	}
	public void setCost(Long cost) {
		this.cost = cost;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Long getOpId() {
		return opId;
	}
	public void setOpId(Long opId) {
		this.opId = opId;
	}
	
	
	
}
