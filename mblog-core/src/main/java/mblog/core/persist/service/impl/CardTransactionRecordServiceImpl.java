package mblog.core.persist.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mblog.core.data.CardTransactionRecord;
import mblog.core.data.User;
import mblog.core.persist.dao.CardTransactionRecordDao;
import mblog.core.persist.entity.CardTransactionRecordPO;
import mblog.core.persist.service.CardTransactionRecordService;
import mblog.core.persist.service.UserService;
import mblog.core.persist.utils.AmountUtils;
import mblog.core.persist.utils.BeanMapUtils;
import mblog.core.persist.utils.CardTransactionRecordUtils;
import mblog.core.persist.utils.QueryRules;
import mtons.modules.pojos.Paging;

/**
 * @author Beldon 2015/10/25
 */
@SuppressWarnings({ "unchecked", "unchecked" })
@Service
@Transactional
public class CardTransactionRecordServiceImpl implements CardTransactionRecordService {
	private static final Logger log = LoggerFactory.getLogger(CardTransactionRecordService.class);
    @Autowired
    private CardTransactionRecordDao cardTransactionRecordDao;

//    @Autowired
//    private ServletContext servletContext;

    @Autowired
    UserService userService;
    
    
	@Override
	@Transactional(readOnly = true)
	public void paging(Paging paging, List<QueryRules> qrLst) {
		List<CardTransactionRecordPO> list = cardTransactionRecordDao.paging(paging, qrLst);
		List<CardTransactionRecord> rets = new ArrayList<>();

		for (CardTransactionRecordPO po : list) {
			CardTransactionRecord m = new CardTransactionRecord();
            BeanUtils.copyProperties(po, m);
			rets.add(m);
		}
		paging.setResults(rets);
	}
	
    @Override
    public void save(CardTransactionRecord ctr) {
        CardTransactionRecordPO cardTransactionRecordPO = new CardTransactionRecordPO();

        BeanUtils.copyProperties(ctr, cardTransactionRecordPO);

        cardTransactionRecordDao.saveOrUpdate(cardTransactionRecordPO);

//        servletContext.setAttribute("cardTransactionRecords", findAll());
    }
    
    
    public void saveUnique(CardTransactionRecord ctr) {
        List<QueryRules> qrLst  =new ArrayList<>();
        QueryRules moblieNoVQR = new QueryRules("moblieNoV", ctr.getMoblieNoV(), "eq"	);
        qrLst.add(moblieNoVQR);
        QueryRules onlyCodeQR = new QueryRules("onlyCode", ctr.getOnlyCode(), "eq"	);
        qrLst.add(onlyCodeQR);
        QueryRules sysSourceQR = new QueryRules("sysSource", ctr.getSysSource(), "eq"	);
        qrLst.add(sysSourceQR);		
        
		if(cardTransactionRecordDao.findByCondition(qrLst  ).size()<1){
			  CardTransactionRecordPO cardTransactionRecordPO = new CardTransactionRecordPO();
		        BeanUtils.copyProperties(ctr, cardTransactionRecordPO);
			cardTransactionRecordDao.save(cardTransactionRecordPO);	
		}
    }

    @Override
    public void delete(long id) {
        CardTransactionRecordPO cardTransactionRecordPO = cardTransactionRecordDao.get(id);
        cardTransactionRecordDao.delete(cardTransactionRecordPO);
//        servletContext.setAttribute("cardTransactionRecords", findAll());
    }

    @Override
    public CardTransactionRecord findById(long id) {
    	CardTransactionRecord cardTransactionRecord = BeanMapUtils.copy(cardTransactionRecordDao.get(id));
        return cardTransactionRecord;
    }

    @Override
    public List<CardTransactionRecord> findAll() {
//        List<cardTransactionRecordPO> linkPOs = cardTransactionRecordDao.list();

        List<CardTransactionRecordPO> linkPOs = cardTransactionRecordDao.findAll();

        List<CardTransactionRecord> rets = new ArrayList<CardTransactionRecord>();
        for (CardTransactionRecordPO po : linkPOs) {
        	CardTransactionRecord m = new CardTransactionRecord();
            BeanUtils.copyProperties(po, m);
            rets.add(m);
        }
        return rets;
    }
    
    private List<CardTransactionRecord> getDataFromRYX ( String yearmonthdatestart,String yearmonthdateend,String moblieNo,String terminalcode) {
    	
		HashMap<String,String> param = new HashMap<String,String>();
		param.put("yearmonthdatestart", yearmonthdatestart);
		param.put("yearmonthdateend", yearmonthdateend);
		param.put("moblieNo", moblieNo);
		param.put("terminalcode", terminalcode);
		
		log.info("开始获取RYX系统数据,param={}"+param);
		return CardTransactionRecordUtils.instance
				.getCardTransactionRecordLst(param);
		
		
	}
    
    @Transactional
    public synchronized void syncDataFromRYXByMobile( String yearmonthdatestart,String yearmonthdateend,String moblieNo) {
    	List<CardTransactionRecord> getCardTransactionRecordLst =  getDataFromRYX(yearmonthdatestart, yearmonthdateend, moblieNo, "");
    	for(CardTransactionRecord ctr: getCardTransactionRecordLst){
    		saveUnique(ctr);
		}
	}
    
    
    public void syncDataFromRYX( String yearmonthdatestart,String yearmonthdateend ) {
    	int pageNo = 1;
    	while (true) {
    		Paging paging = new Paging(pageNo++, 500);
    		userService.paging(paging, "");
    		List<User> userLst = (List<User>) paging.getResults();
    		if(userLst==null || userLst.size()<1){
    			break;
    		}
    		for(User user: userLst){
    			String moblieNo =user.getMobile();
    			if(StringUtils.isNotBlank(moblieNo)){
    				syncDataFromRYXByMobile( yearmonthdatestart, yearmonthdateend, moblieNo);
    			}
    		}
		}
	}
    
    
    public Boolean checkVipFromRYX( String yearmonthdatestart,String yearmonthdateend,String moblieNo,String terminalcode,String transAcount){
    	List<CardTransactionRecord> getCardTransactionRecordLst =  getDataFromRYX(yearmonthdatestart, yearmonthdateend, moblieNo, terminalcode);
    	
    	Boolean isVip = false;
    	for(CardTransactionRecord ctr: getCardTransactionRecordLst){
    		
    		if(AmountUtils. numberFormat(ctr.getTransacount()).equals(AmountUtils. numberFormat(transAcount))){
    			isVip = true;break;
    		}
    	}
    	
    	
    	if(isVip){
    		return true;
    	}
    	return false;
    }
    
    /**
     * VIP校验 (某天某刷卡器某账户刷了某笔钱)
     * @param sysSource
     * @param yearmonthdatestart
     * @param yearmonthdateend
     * @param moblieNo
     * @param terminalcode
     * @param transAcount
     * @return
     */
    public Boolean checkVip(String sysSource, String yearmonthdatestart,String yearmonthdateend,String moblieNo,String terminalcode,String transAcount){
    	if("瑞银信".equals(sysSource)){
    		return checkVipFromRYX(yearmonthdatestart, yearmonthdateend, moblieNo, terminalcode,transAcount);
//    	}else if("瑞银信".equals(sysSource)){
    		
    	}else{
    		
    		return false;
    	}
    }
}
