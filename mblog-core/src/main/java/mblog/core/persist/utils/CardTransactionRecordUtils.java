/*
+--------------------------------------------------------------------------
|   Mblog [#RELEASE_VERSION#]
|   ========================================
|   Copyright (c) 2014, 2015 mtons. All Rights Reserved
|   http://www.mtons.com
|
+---------------------------------------------------------------------------
*/
package mblog.core.persist.utils;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.time.DateUtils;

import mblog.core.data.CardTransactionRecord;

/**
 * @author langhsu
 *
 */
public abstract class CardTransactionRecordUtils {
//	public static String initStartDateStr = "20160302";//
	public static String initStartDateStr = "20151029";

	public static CardTransactionRecordUtils getInstance(String sysSource) {
		if("瑞刷".equals(sysSource)){
			return RSUtils.getInstance();
		}else if("瑞银信".equals(sysSource)){
			return RYXUtils.getInstance();
		}else{
			throw new RuntimeException("未找到POS厂商："+sysSource);
		}
	}
	
	
	public abstract List<CardTransactionRecord> getCardTransactionRecordLst( Map<String,String> paraMap)  ;
	
	
	static void parseDealTime(CardTransactionRecord ctr){
	    String deal_data=ctr.getDeal_data();//交易日期
	    String deal_time=ctr.getDeal_time();//交易时间
	    
	    Date dealtime;
		try {
			dealtime = DateUtils.parseDate(deal_data+" "+deal_time, new String[]{"yyyyMMdd HH:mm:ss"});
		} catch (ParseException e) {
			throw new RuntimeException("解析时间错误："+(deal_data+" "+deal_time));
		}
	    ctr.setDealTime(dealtime);
	}
	
	/**
	 * 整理数据
	 * @param ctr
	 * @param userId
	 * @param realterminalId
	 */
	public static void postDate(CardTransactionRecord ctr,Long userId,String realterminalId){
		parseDealTime(ctr);
		ctr.setUserId(userId);
		ctr.setRealterminalId(realterminalId);
		ctr.setPoint(0L);
		ctr.setPoint1(0L);
		ctr.setPoint2(0L);
		ctr.setCreateTime(new Date());
	}
}
