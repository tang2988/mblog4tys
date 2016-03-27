package mblog.core.task;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import mblog.core.persist.service.CardTransactionRecordService;
import mblog.core.persist.service.PointRuleService;

@Component
public class MyTaskManager {
	private static final Logger log = LoggerFactory.getLogger(MyTaskManager.class);
	@Autowired
	CardTransactionRecordService cardTransactionRecordService;
	
	@Autowired
	PointRuleService pointRuleService;
	/* 	@Scheduled(fixedDelay = 5000)  
	    void doSomethingWithDelay(){  
	        System.out.println("I'm doing with delay now!");  
	    }
	      
	    @Scheduled(fixedRate = 5000)  
	    void doSomethingWithRate(){  
	        System.out.println("I'm doing with rate now!");  
	    }*/
	      
	    @Scheduled(cron = "0 0 6,8 * * *")  
	    void syncCardTransactionRecord(){  
	    	log.info("开始同步前一天的交易数据....");
	    	
	    	Calendar yesterday = Calendar.getInstance();
	    	yesterday.add(Calendar.DATE, -1);
	        cardTransactionRecordService.syncDataFromSysSource("瑞银信",DateFormatUtils.format(yesterday, "yyyyMMdd"), DateFormatUtils.format(yesterday, "yyyyMMdd"));
	        
	        cardTransactionRecordService.syncDataFromSysSource("瑞刷",DateFormatUtils.format(yesterday, "yyyyMMdd"), DateFormatUtils.format(yesterday, "yyyyMMdd"));
	        
	        log.info("结束同步前一天的交易数据....");
	    }
	    
	    
	    @Scheduled(cron = "0 0 9,11 * * *")  
	    void calBackPointFromCardTransactionRecord(){  
	    	log.info("开始发放前一天的交易返现....");
	    	
	    	Calendar yesterday = Calendar.getInstance();
	    	yesterday.add(Calendar.DATE, -1);
	    	
			try {
				Date startTime = DateUtils.parseDate(DateFormatUtils.format(yesterday, "yyyy-MM-dd")+"00:00:00", new String[]{"yyyy-MM-dd HH:mm:ss"});
				Date endTime=DateUtils.parseDate(DateFormatUtils.format(yesterday, "yyyy-MM-dd")+"23:59:59", new String[]{"yyyy-MM-dd HH:mm:ss"});
				pointRuleService.calBackPointFromCardTransactionRecord(startTime, endTime);
		    	
			} catch (ParseException e) {
				 log.info("发放前一天的交易返现异常",e);
			}
			
	        log.info("完成发放前一天的交易返现....");
	    }
	
}
