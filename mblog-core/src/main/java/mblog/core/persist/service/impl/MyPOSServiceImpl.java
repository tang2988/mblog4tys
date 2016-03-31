package mblog.core.persist.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import mblog.core.data.CardTransactionRecord;
import mblog.core.data.MyPOS;
import mblog.core.persist.dao.MyPOSDao;
import mblog.core.persist.entity.MyPOSPO;
import mblog.core.persist.service.CardTransactionRecordService;
import mblog.core.persist.service.MyPOSService;
import mblog.core.persist.utils.AmountUtils;
import mblog.core.persist.utils.BeanMapUtils;
import mblog.core.persist.utils.QueryRules;
import mtons.modules.pojos.Paging;

/**
 * @author Beldon 2015/10/25
 */
@Service
@Transactional
public class MyPOSServiceImpl implements MyPOSService {
	private static final Logger log = LoggerFactory.getLogger(MyPOSService.class);
	
	@Autowired
	private MyPOSDao myPOSDao;
	
	@Autowired
	CardTransactionRecordService cardTransactionRecordService;

	@Override
	@Transactional(readOnly = true)
	public void paging(Paging paging, List<QueryRules> qrLst) {
		List<MyPOSPO> list = myPOSDao.paging(paging, qrLst);
		List<MyPOS> rets = new ArrayList<>();

		for (MyPOSPO po : list) {
			MyPOS m = new MyPOS();
			BeanUtils.copyProperties(po, m);
			rets.add(m);
		}
		paging.setResults(rets);
	}

	@Override
	public void save(MyPOS ctr) {
		MyPOSPO po = new MyPOSPO();

		BeanUtils.copyProperties(ctr, po);

		myPOSDao.saveOrUpdate(po);
		ctr.setId(po.getId());
		// servletContext.setAttribute("cardTransactionRecords", findAll());
	}

	@Override
	public void delete(long id) {
		MyPOSPO cardTransactionRecordPO = myPOSDao.get(id);
		myPOSDao.delete(cardTransactionRecordPO);
		// servletContext.setAttribute("cardTransactionRecords", findAll());
	}

	@Override
	public MyPOS findById(long id) {
		MyPOS cardTransactionRecord = BeanMapUtils.copy(myPOSDao.get(id));
		return cardTransactionRecord;
	}

	@Override
	public List<MyPOS> findAll() {
		// List<cardTransactionRecordPO> linkPOs = myPOSDao.list();

		List<MyPOSPO> linkPOs = myPOSDao.findAll();

		List<MyPOS> rets = new ArrayList<MyPOS>();
		for (MyPOSPO po : linkPOs) {
			MyPOS m = new MyPOS();
			BeanUtils.copyProperties(po, m);
			rets.add(m);
		}
		return rets;
	}

	
	@Override
	public List<MyPOS> findByCondition(List<QueryRules> qrLst) {
		List<MyPOSPO> linkPOs = myPOSDao.findByCondition(qrLst);

		List<MyPOS> rets = new ArrayList<MyPOS>();
		for (MyPOSPO po : linkPOs) {
			MyPOS m = new MyPOS();
			BeanUtils.copyProperties(po, m);
			rets.add(m);
		}
		return rets;
	}
	
	/**
	 * VIP校验 (某天某刷卡器某账户刷了某笔钱)
	 * 
	 * @param sysSource
	 * @param yearmonthdatestart
	 * @param yearmonthdateend
	 * @param moblieNo
	 * @param terminalcode
	 * @param transAcount
	 * @return
	 */
	public synchronized MyPOS checkMyPos(Long userId,String sysSource, String yearmonthdatestart, String yearmonthdateend, String moblieNo,
			String terminalcode, String transAcount) {
		
		MyPOS myPOS = new MyPOS();
		if ("瑞银信".equals(sysSource) ||"瑞刷".equals(sysSource)) {

			List<CardTransactionRecord> getCardTransactionRecordLst = cardTransactionRecordService.getDataFromSysSource(sysSource,
					yearmonthdatestart, yearmonthdateend, moblieNo, terminalcode);

			for (CardTransactionRecord ctr : getCardTransactionRecordLst) {
				if (AmountUtils.numberFormat(ctr.getTransacount()).equals(AmountUtils.numberFormat(transAcount))) {
					if("GZ黄淑玲".equals(ctr.getAgencyName())){
						return myPOS;
					}
					myPOS.setReserve1(userId);
					myPOS.setAgencyName(ctr.getAgencyName());
					myPOS.setCustomerName(ctr.getCustomerName());
//					myPOS.setDeal_time(ctr.getDeal_data()+ctr.getDeal_time());
					myPOS.setIdcard(ctr.getIdcard());
					myPOS.setMoblieNoV(ctr.getMoblieNoV());
					myPOS.setSysSource(sysSource);
					myPOS.setTerminalId(ctr.getTerminalId());
					myPOS.setReserve2(terminalcode);
					save(myPOS);
					return myPOS;
				}
			}
			return myPOS;
		} else {

			return myPOS;
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public void aa(){
		try {
			a1();			
		} catch (Exception e) {
		}

		try {
			a2();	
		} catch (Exception e) {
		}
		
		
	}
	
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void a1(){
		
		for(int k=5;k>0;k--){
			MyPOS myPOS = new MyPOS();
			myPOS.setAgencyName(RandomStringUtils.randomAlphanumeric(5));
			if(k==3){
				throw new RuntimeException("tys make err");
			}
			save(myPOS);
		}
	}
	
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public void a2(){
		
		for(int k=5;k>0;k--){
			MyPOS myPOS = new MyPOS();
			myPOS.setCustomerName(RandomStringUtils.randomAlphanumeric(5));
			save(myPOS);
			
			if(k==3){
				throw new RuntimeException("tys make err");
			}
		}
	}
	
	
	
	
	
	
	
	
	
	
	
}
