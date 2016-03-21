package mblog.core.task;

import java.util.Calendar;

import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import mblog.core.persist.service.CardTransactionRecordService;

@Component
public class MyTaskManager {
	private static final Logger log = LoggerFactory.getLogger(MyTaskManager.class);
	@Autowired
	CardTransactionRecordService cardTransactionRecordService;
	/* 	@Scheduled(fixedDelay = 5000)  
	    void doSomethingWithDelay(){  
	        System.out.println("I'm doing with delay now!");  
	    }
	      
	    @Scheduled(fixedRate = 5000)  
	    void doSomethingWithRate(){  
	        System.out.println("I'm doing with rate now!");  
	    }*/
	      
	    @Scheduled(cron = "0 0 6,8 * * *")  
	    void doSomethingWith(){  
	    	log.info("开始同步前一天的交易数据....");
	    	
	    	Calendar yesterday = Calendar.getInstance();
	    	yesterday.add(Calendar.DATE, -1);
	        cardTransactionRecordService.syncDataFromSysSource("瑞银信",DateFormatUtils.format(yesterday, "yyyyMMdd"), DateFormatUtils.format(yesterday, "yyyyMMdd"));
	        
	        cardTransactionRecordService.syncDataFromSysSource("瑞刷",DateFormatUtils.format(yesterday, "yyyyMMdd"), DateFormatUtils.format(yesterday, "yyyyMMdd"));
	        
	        log.info("结束同步前一天的交易数据....");
	    }
	
}
