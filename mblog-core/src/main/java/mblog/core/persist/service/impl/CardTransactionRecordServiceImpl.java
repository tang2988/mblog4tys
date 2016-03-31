package mblog.core.persist.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mblog.core.data.CardTransactionRecord;
import mblog.core.data.MyPOS;
import mblog.core.persist.dao.CardTransactionRecordDao;
import mblog.core.persist.entity.CardTransactionRecordPO;
import mblog.core.persist.service.CardTransactionRecordService;
import mblog.core.persist.service.MyPOSService;
import mblog.core.persist.service.UserService;
import mblog.core.persist.utils.BeanMapUtils;
import mblog.core.persist.utils.CardTransactionRecordUtils;
import mblog.core.persist.utils.QueryRules;
import mtons.modules.pojos.Paging;

/**
 * @author Beldon 2015/10/25
 */
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
    @Autowired
    MyPOSService myPOSService;
    
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
    
    
    @Override
    public void update(CardTransactionRecord ctr) {
        CardTransactionRecordPO cardTransactionRecordPO = cardTransactionRecordDao.get(ctr.getId());
        BeanUtils.copyProperties(ctr, cardTransactionRecordPO);
        cardTransactionRecordDao.update(cardTransactionRecordPO);

    }
    
    
    
    public synchronized void saveUnique(CardTransactionRecord ctr) {
		if(countRecord(ctr)<1){
			  CardTransactionRecordPO cardTransactionRecordPO = new CardTransactionRecordPO();
		        BeanUtils.copyProperties(ctr, cardTransactionRecordPO);
			cardTransactionRecordDao.save(cardTransactionRecordPO);
		}
    }

    public Integer countRecord(CardTransactionRecord ctr){
    	List<QueryRules> qrLst  =new ArrayList<>();
        QueryRules moblieNoVQR = new QueryRules("moblieNoV", ctr.getMoblieNoV(), QueryRules.OP_EQ	);
        qrLst.add(moblieNoVQR);
        QueryRules onlyCodeQR = new QueryRules("onlyCode", ctr.getOnlyCode(),  QueryRules.OP_EQ	);
        qrLst.add(onlyCodeQR);
        QueryRules sysSourceQR = new QueryRules("sysSource", ctr.getSysSource(),  QueryRules.OP_EQ	);
        qrLst.add(sysSourceQR);		
        QueryRules terminalIdQR = new QueryRules("terminalId", ctr.getTerminalId(),  QueryRules.OP_EQ	);
        qrLst.add(terminalIdQR);
        
        List<CardTransactionRecordPO> lst = cardTransactionRecordDao.findByCondition(qrLst  );
		return lst==null ?0:lst.size();
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
    
    
    /**
     * 获取LIST
     */
    public List<CardTransactionRecord> getDataFromSysSource (String sysSource, String yearmonthdatestart,String yearmonthdateend,String moblieNo,String terminalcode ) {
    	
		HashMap<String,String> param = new HashMap<String,String>();
		param.put("yearmonthdatestart", yearmonthdatestart);
		param.put("yearmonthdateend", yearmonthdateend);
		param.put("moblieNo", moblieNo);
		param.put("terminalcode", terminalcode);
		
		log.info("开始获取{}系统数据,param={}",sysSource,param);
		return CardTransactionRecordUtils.getInstance(sysSource)
				.getCardTransactionRecordLst(param);
		
		
	}
    
    
    /****
     * 根据手机号码同步
     */
    @Transactional
    public synchronized void syncDataFromSysSourceByMobile(String sysSource, String yearmonthdatestart,String yearmonthdateend,String moblieNo,String terminalcode,Long userId) {
    	List<CardTransactionRecord> getCardTransactionRecordLst =  getDataFromSysSource(sysSource,yearmonthdatestart, yearmonthdateend, moblieNo, terminalcode);
    	for(CardTransactionRecord ctr: getCardTransactionRecordLst){
    		CardTransactionRecordUtils.postDate(ctr, userId, terminalcode);
    		saveUnique(ctr);
		}
	}
    
    
    public void syncDataFromSysSource(String sysSource, String yearmonthdatestart,String yearmonthdateend ) {
    	int pageNo = 1;
    	while (true) {
    		Paging paging = new Paging(pageNo++, 500);
    		
    		List<QueryRules> qrLst  =new ArrayList<>();
    		myPOSService.paging(paging, qrLst);
    		List<MyPOS> myPosLst = (List<MyPOS>) paging.getResults();
    		if(myPosLst==null || myPosLst.size()<1){
    			break;
    		}
    		for(MyPOS m: myPosLst){
				syncDataFromSysSourceByMobile( m.getSysSource(),yearmonthdatestart, yearmonthdateend, m.getMoblieNoV(),m.getReserve2(),m.getReserve1());
    		}
		}
	}
    
    
    
}
