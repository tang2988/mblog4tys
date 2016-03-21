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

import java.util.List;
import java.util.Map;

import mblog.core.data.CardTransactionRecord;

/**
 * @author langhsu
 *
 */
public abstract class CardTransactionRecordUtils {
	public static String initStartDateStr = "20150901";

	public static CardTransactionRecordUtils getInstance(String sysSource) {
		if("瑞刷".equals(sysSource)){
			return RSUtils.instance;
		}else if("瑞银信".equals(sysSource)){
			return RYXUtils.instance;
		}else{
			throw new RuntimeException("未找到POS厂商："+sysSource);
		}
	}
	
	
	public abstract List<CardTransactionRecord> getCardTransactionRecordLst( Map<String,String> paraMap)  ;
}
