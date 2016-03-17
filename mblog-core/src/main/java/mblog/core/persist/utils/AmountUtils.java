package mblog.core.persist.utils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 金额计算工具类
 */
public class AmountUtils {

	protected final static Logger log = LoggerFactory.getLogger(AmountUtils.class);

	// 默认除法运算精度
	private static final int DEFAULT_DIV_SCALE = 16;

	private static final String YYYY_MM_DD = "yyyy-MM-dd";

	private static final SimpleDateFormat sf = new SimpleDateFormat(YYYY_MM_DD);

	public static final BigDecimal MANAGER_FEE_RATE = new BigDecimal("0.1");// 居间费比列

	/**
	 * @MethodName: add
	 * @Param: AmountUtil
	 * @Author:
	 * @Date: 2013-4-20 下午05:15:18
	 * @Return:
	 * @Descb: 日期累加
	 * @Throws:
	 */
	private static Date add(Date date, int type, int value) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(type, value);
		return calendar.getTime();
	}

	/**
	 * @MethodName: add
	 * @Param: AmountUtil
	 * @Author:
	 * @Date: 2013-4-20 下午05:15:18
	 * @Return:
	 * @Descb: 日期累加
	 * @Throws:
	 */
	private static Date addDays(Date date, int value) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, value);
		return calendar.getTime();
	}

	/**
	 * 将Object转成BigDecimal
	 * 
	 * @param obj
	 * @return
	 */
	public static BigDecimal objToBigDecimal(Object obj) {
		if (obj instanceof BigDecimal) {
			return (BigDecimal) obj;
		} else {
			return new BigDecimal(obj+"");
		}
	}
	
	

	/**
	 * 两个数值相加,保留两位小数点. 相加规则：如果数值小数点超过两位，直接截取两位小数，不做小数点进位处理,然后再相加 先转成BigDecimal
	 * ，然后右移两位小数点。 再取其整数值相加,转成BigDecimal，左移两位小数点
	 * 
	 * @param num1
	 * @param num2
	 * @return
	 */
	public static BigDecimal add(Object num1, Object num2) {
		/**
		 * 先将两个数值转成String，再new BigDecimal，以防精度丢失
		 */
		BigDecimal bd1 = objToBigDecimal(num1);
		BigDecimal bd2 = objToBigDecimal(num2);
		/**
		 * 右移小数点
		 */
		bd1 = bd1.movePointRight(2);
		bd2 = bd2.movePointRight(2);
		/**
		 * 各取整数值相加
		 */
		long resultLong = bd1.longValue() + bd2.longValue();
		/**
		 * 转成BigDecimal，左移小数点
		 */
		BigDecimal result = new BigDecimal(String.valueOf(resultLong));
		result = result.movePointLeft(2);
		return result;
	}

	/**
	 * 两个数值相减,前面一个减去后面一个： num1-num2 相减规则：如果数值小数点超过两位，直接截取两位小数，不做小数点进位处理,然后再相减
	 * 先转成BigDecimal ，然后右移两位小数点。 再取其整数值相加,转成BigDecimal，左移两位小数点
	 * 
	 * @param num1
	 * @param num2
	 * @return
	 */
	public static BigDecimal subtract(Object num1, Object num2) {
		/**
		 * 先将两个数值转成String，再new BigDecimal，以防精度丢失
		 */
		BigDecimal bd1 = objToBigDecimal(num1);
		BigDecimal bd2 = objToBigDecimal(num2);
		/**
		 * 右移小数点
		 */
		bd1 = bd1.movePointRight(2);
		bd2 = bd2.movePointRight(2);
		/**
		 * 各取整数值相加
		 */
		long resultLong = bd1.longValue() - bd2.longValue();
		/**
		 * 转成BigDecimal，左移小数点
		 */
		BigDecimal result = new BigDecimal(String.valueOf(resultLong));
		result = result.movePointLeft(2);
		return result;
	}

	/**
	 * 保留两位小数，不管小数点后的第三位是多少都舍去
	 * 
	 * @param obj类型为String
	 *            ,int,Integer,long,Long,float,Fload,double,Double,BigDecimal，
	 *            其他的直接返回0.00 如果为String，也必须是数值型，否则也只返回0.00
	 * @return
	 */
	public static BigDecimal numberFormat(Object obj) {
		/**
		 * 先将两个数值转成String，再new BigDecimal，以防精度丢失
		 */
		BigDecimal bd = objToBigDecimal(obj);
		/**
		 * 右移小数点
		 */
		bd = bd.movePointRight(2);
		/**
		 * 取整数
		 */
		long resultLong = bd.longValue();
		/**
		 * 转成BigDecimal，左移小数点
		 */
		BigDecimal result = new BigDecimal(String.valueOf(resultLong));
		result = result.movePointLeft(2);
		return result;
	}

	/**
	 * 保留两位小数，不管小数点后的第三位是多少都舍去
	 * 
	 * @param obj类型为String
	 *            ,int,Integer,long,Long,float,Fload,double,Double,BigDecimal，
	 *            其他的直接返回0.00 如果为String，也必须是数值型，否则也只返回0.00
	 * @return
	 */
	public static BigDecimal numberFormat(Object obj, int length) {
		/**
		 * 先将两个数值转成String，再new BigDecimal，以防精度丢失
		 */
		BigDecimal bd = objToBigDecimal(obj);
		/**
		 * 右移小数点
		 */
		bd = bd.movePointRight(length);
		/**
		 * 取整数
		 */
		long resultLong = bd.longValue();
		/**
		 * 转成BigDecimal，左移小数点
		 */
		BigDecimal result = new BigDecimal(String.valueOf(resultLong));
		result = result.movePointLeft(length);
		return result;
	}

	/**
	 * 除法运算，b1/b2
	 * 
	 * @param b1
	 * @param b2
	 * @return
	 */
	public static BigDecimal divide(BigDecimal b1, BigDecimal b2) {
		return b1.divide(b2, DEFAULT_DIV_SCALE, BigDecimal.ROUND_DOWN);
	}

	/**
	 * 利率计算，
	 * 
	 * @param yearRate
	 *            年利率，分百分比
	 * @param isDayThe
	 *            1：计算天利率，2：计算月利率
	 * @return
	 */
	public static BigDecimal rate(BigDecimal yearRate, int isDayThe) {
		BigDecimal baseBig = null;
		if (isDayThe == 1) {
			baseBig = new BigDecimal(360 * 100);
		} else if (isDayThe == 2) {
			baseBig = new BigDecimal(12 * 100);
		}
		return divide(yearRate, baseBig);
	}

	/**
	 * 先息后本:满标第二天还利息，到期还本金
	 * 
	 * @param borrowSum
	 *            借款金额
	 * @param yearRate
	 *            年利率
	 * @param deadline
	 *            期限
	 * @param isDayThe
	 *            1：天标，2：月标
	 * @return
	 */
	public static List<Map<String, Object>> rateCalculateSum(BigDecimal borrowSum, BigDecimal yearRate, int deadline,
			int isDayThe) {
		// 返回的结果集
		Map<String, Object> map = new HashMap<String, Object>();
		Date date = new Date();

		BigDecimal totalInterest = null;// 总利息
		BigDecimal rate = rate(yearRate, isDayThe);// 天或者月利率
		BigDecimal deadlineBig = new BigDecimal(deadline);// 期限
		Date currTime = add(date, Calendar.DAY_OF_MONTH, 1);// 利息还款时间是当前时间加一天
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();

		// 天标第一期还利息=本金*天或者月利率*期限
		// 先息后本第一期不用还本金
		totalInterest = borrowSum.multiply(rate).multiply(deadlineBig);
		totalInterest = numberFormat(totalInterest);
		currTime = add(date, Calendar.DATE, 1);//

		map = new HashMap<String, Object>();
		map.put("repayPeriod", "1/2");
		map.put("repayDate", sf.format(currTime));
		map.put("stillPrincipal", "0.00");
		map.put("stillInterest", numberFormat(totalInterest));
		map.put("principalBalance", numberFormat(borrowSum));
		map.put("interestBalance", "0.00");
		map.put("mRate", rate.multiply(new BigDecimal(100)));
		map.put("period", 1);
		map.put("totalAmount", add(borrowSum, totalInterest));
		mapList.add(map);

		if (isDayThe == 1) {
			currTime = add(date, Calendar.DATE, deadline);//
		} else {
			currTime = add(date, Calendar.MONTH, deadline);//
		}

		// 先息后本第二期不用还利息
		map = new HashMap<String, Object>();
		map.put("repayPeriod", "2/2");
		map.put("repayDate", sf.format(currTime));
		map.put("stillPrincipal", numberFormat(borrowSum));
		map.put("stillInterest", "0.00");
		map.put("principalBalance", "0.00");
		map.put("interestBalance", "0.00");
		map.put("mRate", rate.multiply(new BigDecimal(100)));
		map.put("period", 2);
		map.put("totalAmount", add(borrowSum, totalInterest));
		mapList.add(map);
		return mapList;
	}

	/**
	 * 先息后本收益总收益
	 * 
	 * @param realAmount
	 *            投标金额
	 * @param yearRate
	 *            年利率
	 * @param deadline
	 *            期限
	 * @param isDayThe
	 *            1：天标，2：月标
	 * @return
	 */
	public static Map<String, String> earnCalculateSum(BigDecimal realAmount, BigDecimal yearRate, int deadline,
			int isDayThe) {
		Map<String, String> mapEarn = new HashMap<String, String>();
		StringBuilder msg = new StringBuilder();
		BigDecimal totalInterest = null;// 总利息
		BigDecimal rate = rate(yearRate, isDayThe);// 天或者月利率
		BigDecimal deadlineBig = new BigDecimal(deadline);// 期限

		// 总利息
		totalInterest = realAmount.multiply(rate).multiply(deadlineBig);
		totalInterest = numberFormat(totalInterest);

		String m = "";
		if (isDayThe == 1) {
			m = "天";
		} else {
			m = "月";
		}

		// 居间服务费
		BigDecimal iManageFee = divide(totalInterest, new BigDecimal(10));
		iManageFee = numberFormat(iManageFee);

		// 总收益=本金+利息
		BigDecimal earnAmount = add(realAmount, totalInterest);
		msg.append("到期收益总额￥" + earnAmount + "元，年利率" + numberFormat(yearRate));
		msg.append("%，投资期限" + deadline + m + "，其中投资金额￥" + numberFormat(realAmount) + "元，到期收益利息￥"
				+ numberFormat(totalInterest));
		msg.append("元，到期扣除居间服务费￥" + numberFormat(iManageFee) + "元");

		mapEarn.put("msg", msg.toString());
		mapEarn.put("realAmount", numberFormat(realAmount).toString());
		mapEarn.put("totalInterest", numberFormat(totalInterest).toString());
		mapEarn.put("iManageFee", numberFormat(iManageFee).toString());
		return mapEarn;
	}

	/**
	 * 先息后本收益：计算每一期还款本金和利息
	 * 
	 * @param realAmount
	 *            投资金额
	 * @param yearRate
	 *            年利率
	 * @param deadline
	 *            期限
	 * @param isDayThe
	 *            1：天标，2：月标
	 * @param period
	 *            期数
	 * @return
	 */
	public static Map<String, String> earnCalculateSumRepayPeriod(BigDecimal realAmount, BigDecimal yearRate,
			int deadline, int isDayThe, int period) {
		Map<String, String> mapEarn = new HashMap<String, String>();
		BigDecimal totalInterest = null;// 总利息
		BigDecimal rate = rate(yearRate, isDayThe);// 天或者月利率
		BigDecimal deadlineBig = new BigDecimal(deadline);// 期限

		// 总利息
		totalInterest = realAmount.multiply(rate).multiply(deadlineBig);
		totalInterest = numberFormat(totalInterest);

		// 居间服务费
		BigDecimal iManageFee = divide(totalInterest, new BigDecimal(10));
		iManageFee = numberFormat(iManageFee);

		// 期数
		if (period == 1) {
			realAmount = new BigDecimal("0.00");
		} else if (period == 2) {
			totalInterest = new BigDecimal("0.00");
			iManageFee = new BigDecimal("0.00");
		}

		mapEarn.put("realAmount", numberFormat(realAmount).toString());
		mapEarn.put("totalInterest", totalInterest.toString());
		mapEarn.put("iManageFee", iManageFee.toString());
		return mapEarn;
	}

	/**
	 * 一次性还款
	 * 
	 * @param borrowSum
	 *            借款金额
	 * @param yearRate
	 *            年利率
	 * @param deadline
	 *            期限
	 * @param isDayThe
	 *            1：天标，2：月标
	 * @return
	 */
	public static List<Map<String, Object>> rateCalculateOne(BigDecimal borrowSum, BigDecimal yearRate, int deadline,
			int isDayThe) {
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		BigDecimal totalInterest = null;// 总利息
		BigDecimal rate = rate(yearRate, isDayThe);// 天或者月利率
		BigDecimal deadlineBig = new BigDecimal(deadline);// 期限
		Date date = new Date();
		Date currTime = null;

		// 总利息
		totalInterest = borrowSum.multiply(rate).multiply(deadlineBig);
		totalInterest = numberFormat(totalInterest);

		if (isDayThe == 1) {
			currTime = add(date, Calendar.DATE, deadline);
		} else if (isDayThe == 2) {
			currTime = add(date, Calendar.MONTH, deadline);
		}

		map.put("repayPeriod", "1/1");
		map.put("repayDate", sf.format(currTime));
		map.put("stillPrincipal", numberFormat(borrowSum));
		map.put("stillInterest", totalInterest);
		map.put("principalBalance", "0.00");
		map.put("interestBalance", "0.00");
		map.put("mRate", rate.multiply(new BigDecimal(100)));
		map.put("period", 1);
		map.put("totalAmount", add(borrowSum, totalInterest));
		mapList.add(map);

		return mapList;
	}

	/**
	 * 一次性还款收益
	 * 
	 * @param realAmount
	 *            投标金额
	 * @param yearRate
	 *            年利率
	 * @param deadline
	 *            期限
	 * @param isDayThe
	 *            1：天标，2：月标
	 * @return
	 */
	public static Map<String, String> earnCalculateOne(BigDecimal realAmount, BigDecimal yearRate, int deadline,
			int isDayThe) {
		Map<String, String> mapEarn = new HashMap<String, String>();
		StringBuilder msg = new StringBuilder();
		BigDecimal totalInterest = null;// 总利息
		BigDecimal rate = rate(yearRate, isDayThe);// 天或者月利率
		BigDecimal deadlineBig = new BigDecimal(deadline);// 期限

		// 总利息
		totalInterest = realAmount.multiply(rate).multiply(deadlineBig);
		totalInterest = numberFormat(totalInterest);

		String m = "";
		if (isDayThe == 1) {
			m = "天";
		} else {
			m = "个月";
		}

		// 居间服务费
		BigDecimal iManageFee = divide(totalInterest, new BigDecimal(10));
		iManageFee = numberFormat(iManageFee);

		BigDecimal earnAmount = add(realAmount, totalInterest);
		msg.append("到期收益总额￥" + earnAmount + "元，年利率" + numberFormat(yearRate));
		msg.append("%，投资期限" + deadline + m + "，其中投资金额￥" + numberFormat(realAmount) + "元，到期收益利息￥" + totalInterest);
		msg.append("元，到期扣除居间服务费￥" + iManageFee + "元");
		mapEarn.put("msg", msg.toString());
		mapEarn.put("realAmount", numberFormat(realAmount) + "");
		mapEarn.put("totalInterest", totalInterest + "");
		mapEarn.put("iManageFee", iManageFee + "");
		mapEarn.put("monthRate", rate + "");
		return mapEarn;
	}

	/**
	 * 按月付息到期还本
	 * 
	 * @param borrowSum
	 *            借款金额
	 * @param yearRate
	 *            年利率
	 * @param deadline
	 *            期限
	 * @param isDayThe
	 *            1：天标，2：月标
	 * @return
	 */
	public static List<Map<String, Object>> rateCalculateMonthTwo(BigDecimal borrowSum, BigDecimal yearRate,
			int deadline, int isDayThe) {
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = null;
		BigDecimal rate = rate(yearRate, isDayThe);// 天或者月利率
		Date date = new Date();
		Date currTime = null;

		BigDecimal monPayRate = borrowSum.multiply(rate);// 月利息
		monPayRate = numberFormat(monPayRate);
		for (int n = 1; n <= deadline; n++) {
			map = new HashMap<String, Object>();
			currTime = add(date, Calendar.MONTH, n);
			map.put("repayPeriod", n + "/" + deadline);
			map.put("repayDate", sf.format(currTime));
			map.put("mRate", rate.multiply(new BigDecimal(100)));
			map.put("period", n);
			if (n == deadline) {// 最后一期
				map.put("stillPrincipal", numberFormat(borrowSum));
				map.put("stillInterest", monPayRate);
				map.put("principalBalance", numberFormat(borrowSum));
				map.put("interestBalance", "0.00");
			} else {// 前(n-1)期
				map.put("stillPrincipal", "0.00");
				map.put("stillInterest", monPayRate);
				map.put("principalBalance", "0.00");
				map.put("interestBalance", numberFormat(monPayRate.multiply(new BigDecimal(deadline - n))));
			}
			map.put("totalInterest", numberFormat(monPayRate.multiply(new BigDecimal(deadline))));
			map.put("totalAmount", add(borrowSum, monPayRate.multiply(new BigDecimal(deadline))));
			mapList.add(map);
		}
		return mapList;
	}

	/**
	 * 按月付息到期还本收益
	 * 
	 * @param realAmount
	 *            投标金额
	 * @param yearRate
	 *            年利率
	 * @param deadline
	 *            期限
	 * @param isDayThe
	 *            1：天标，2：月标
	 * @return
	 */
	public static Map<String, String> earnCalculateMonthTwo(BigDecimal realAmount, BigDecimal yearRate, int deadline,
			int isDayThe) {
		Map<String, String> mapEarn = new HashMap<String, String>();
		StringBuilder msg = new StringBuilder();
		BigDecimal totalInterest = null;// 总利息
		BigDecimal rate = rate(yearRate, isDayThe);// 天或者月利率
		BigDecimal deadlineBig = new BigDecimal(deadline);// 期限

		// 总利息
		totalInterest = realAmount.multiply(rate);
		totalInterest = numberFormat(totalInterest);
		totalInterest = totalInterest.multiply(deadlineBig);

		// 居间服务费
		BigDecimal iManageFee = divide(totalInterest, new BigDecimal(10));
		iManageFee = numberFormat(iManageFee);

		String m = "";
		if (isDayThe == 1) {
			m = "天";
		} else {
			m = "个月";
		}

		BigDecimal earnAmount = add(realAmount, totalInterest);

		msg.append("到期收益总额：￥" + numberFormat(earnAmount) + "元,年利率：" + numberFormat(yearRate));
		msg.append("%,投资期限" + deadline + m + ",其中投资金额：￥" + numberFormat(realAmount) + "元,到期收益利息：￥" + totalInterest);
		msg.append("元,到期扣除居间服务费：￥" + iManageFee + "元");
		mapEarn.put("msg", msg.toString());
		mapEarn.put("realAmount", numberFormat(realAmount) + "");
		mapEarn.put("totalInterest", totalInterest + "");
		mapEarn.put("iManageFee", iManageFee + "");
		mapEarn.put("monthRate", rate + "");
		return mapEarn;
	}

	/**
	 * 按月付息到期还本还款 计算本期还款本金、利息、管理费
	 * 
	 * @param realAmount
	 *            投资金额
	 * @param yearRate
	 *            年利率
	 * @param deadline
	 *            期限
	 * @param isDayThe
	 *            1：天标，2：月标
	 * @param period
	 *            期数
	 * @return
	 */
	public static Map<String, String> earnCalculateMonthTwoRepayPeriod(BigDecimal realAmount, BigDecimal yearRate,
			int deadline, int isDayThe, int period) {
		Map<String, String> mapEarn = new HashMap<String, String>();
		BigDecimal rate = rate(yearRate, isDayThe);// 天或者月利率

		BigDecimal interest = realAmount.multiply(rate);// 月利息
		interest = numberFormat(interest);
		// 居间服务费
		BigDecimal iManageFee = divide(interest, new BigDecimal(10));
		iManageFee = numberFormat(iManageFee);

		if (period != deadline) {
			realAmount = new BigDecimal("0.00");
		}
		realAmount = numberFormat(realAmount);

		mapEarn.put("realAmount", realAmount + "");
		mapEarn.put("totalInterest", interest + "");
		mapEarn.put("iManageFee", iManageFee + "");
		return mapEarn;
	}

	/**
	 * 
	 * 描述：计算最大可赎回金额
	 * 
	 * @author lizg
	 * @date 2014-9-4下午5:41:24
	 * @param realAmt
	 * @param annualRate
	 * @param realRate
	 * @param month
	 * @param day
	 * @return
	 */
	public static BigDecimal getMaxRedeemAmt(BigDecimal realAmt, BigDecimal annualRate, BigDecimal realRate, int month,
			int day) {
		BigDecimal result = null;
		if (realAmt == null || annualRate == null || annualRate == null) {
			return new BigDecimal("0.00");
		}
		BigDecimal monthRate = rate(subtract(annualRate, realRate), 2);// 月利率（多付出的利率）
		BigDecimal dayRate = rate(realRate, 1);// 天利率
		BigDecimal monthAmt = realAmt.multiply(monthRate).multiply(new BigDecimal(month));// 多付出的利息
		BigDecimal dayAmt = realAmt.multiply(dayRate).multiply(new BigDecimal(day));// 剩下天的利息
		monthAmt = numberFormat(monthAmt);
		dayAmt = numberFormat(dayAmt);
		/**
		 * 结果=本金-多付的利息+天利息
		 */
		result = subtract(realAmt, monthAmt);
		result = add(result, dayAmt);
		return result;
	}

	/**
	 * 
	 * 描述：赎回本金
	 * 
	 * @author lizg
	 * @date 2014-9-4下午5:41:24
	 * @param realAmt
	 * @param annualRate
	 * @param realRate
	 * @param month
	 * @param day
	 * @return
	 */
	public static BigDecimal getPrincipalAmt(BigDecimal redeemAmt, BigDecimal annualRate, BigDecimal realRate,
			int month, int day) {
		BigDecimal result = null;
		if (redeemAmt == null || annualRate == null || annualRate == null) {
			return new BigDecimal("0.00");
		}
		BigDecimal monthRate = rate(subtract(annualRate, realRate), 2);// 月利率（多付出的利率）
		BigDecimal dayRate = rate(realRate, 1);// 天利率
		BigDecimal monthAmt = monthRate.multiply(new BigDecimal(month));
		BigDecimal dayAmt = dayRate.multiply(new BigDecimal(day));
		BigDecimal baseBig = new BigDecimal(1).subtract(monthAmt).add(dayAmt);
		if (baseBig.doubleValue() == 0) {
			return new BigDecimal("0.00");
		}
		/**
		 * 结果=本金/baseBig
		 */
		result = divide(redeemAmt, baseBig);
		result = numberFormat(result);
		return result;
	}

	/**
	 * 
	 * 描述：债权赎回 计算实际到账金额
	 * 
	 * @author lizg
	 * @date 2014-10-15上午10:59:42
	 * @param redeemAmt
	 * @param annualRate
	 * @param realRate
	 * @param month
	 * @param day
	 * @return
	 */
	public static BigDecimal getRedeemRealAmt(BigDecimal redeemAmt, BigDecimal annualRate, BigDecimal realRate,
			int month, int day) {
		BigDecimal result = null;
		if (redeemAmt == null || annualRate == null || annualRate == null) {
			return new BigDecimal("0.00");
		}
		BigDecimal monthRate = rate(subtract(annualRate, realRate), 2);// 月利率（多付出的利率）
		BigDecimal dayRate = rate(realRate, 1);// 天利率
		BigDecimal monthAmt = redeemAmt.multiply(monthRate).multiply(new BigDecimal(month))
				.multiply(new BigDecimal("0.9"));// 实际上多付给投资人的利息
		BigDecimal dayAmt = redeemAmt.multiply(dayRate).multiply(new BigDecimal(day)).multiply(new BigDecimal("0.9"));// 投资人多出的几天所得利息
		result = redeemAmt.subtract(monthAmt).add(dayAmt);
		result = numberFormat(result);
		return result;
	}

	/**
	 * 
	 * @param rule_annualRate
	 *            :年利率规则
	 * @param deadline_day
	 *            ：期限对应天数
	 * 
	 */
	public static List<String> obtainIncrRateList(String rule_annualRate, int dayNum) {

		List<String> rule_list = new ArrayList<String>();
		int start = 0;
		int end = 0;
		try {
			String[] rule_array = rule_annualRate.split("\\|");
			for (String str : rule_array) {
				start = Integer.parseInt((str.split(",")[0].split("-")[0]));
				end = Integer.parseInt((str.split(",")[0].split("-")[1]));
				if (dayNum > start)
					rule_list.add(start + "|" + (end - start) + "|" + str.split(",")[1]);
			}
			Collections.sort(rule_list);
			String str = rule_list.get(rule_list.size() - 1);
			rule_list.remove(str);
			String[] array = str.split("\\|");
			str = array[0] + "|" + (dayNum - Integer.parseInt(array[0])) + "|" + array[2];
			rule_list.add(str);
		} catch (Exception e) {
			log.error("获取利率增量值异常：请求参数------>[rule_annualRate:]" + rule_annualRate + "[deadline_day:]" + dayNum, e);
		}
		return rule_list;
	}

}
