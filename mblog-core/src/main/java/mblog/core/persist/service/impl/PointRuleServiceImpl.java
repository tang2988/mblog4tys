package mblog.core.persist.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mblog.base.lang.Consts;
import mblog.core.data.CardTransactionRecord;
import mblog.core.data.PointDetail;
import mblog.core.data.PointRule;
import mblog.core.persist.dao.PointRuleDao;
import mblog.core.persist.entity.PointRulePO;
import mblog.core.persist.service.CardTransactionRecordService;
import mblog.core.persist.service.PointRuleService;
import mblog.core.persist.service.PointService;
import mblog.core.persist.service.UserService;
import mblog.core.persist.utils.BeanMapUtils;
import mblog.core.persist.utils.QueryRules;
import mtons.modules.pojos.Paging;

@Service
@Transactional
public class PointRuleServiceImpl implements PointRuleService {
	private static final Logger log = LoggerFactory.getLogger(PointRuleService.class);
	@Autowired
	private PointRuleDao pointRuleDao;

	@Autowired
	private PointService pointService;
	
	@Autowired
	private CardTransactionRecordService cardTransactionRecordService;
	
	// @Autowired
	// private ServletContext servletContext;

	@Autowired
	UserService userService;

	@Override
	@Transactional(readOnly = true)
	public void paging(Paging paging, List<QueryRules> qrLst) {
		List<PointRulePO> list = pointRuleDao.paging(paging, qrLst);
		List<PointRule> rets = new ArrayList<>();

		for (PointRulePO po : list) {
			PointRule m = new PointRule();
			BeanUtils.copyProperties(po, m);
			rets.add(m);
		}
		paging.setResults(rets);
	}

	@Override
	public void save(PointRule ctr) {
		PointRulePO pointRulePO = pointRuleDao.get(ctr.getId());
		if (pointRulePO == null) {
			pointRulePO = new PointRulePO();
			BeanUtils.copyProperties(ctr, pointRulePO);
			pointRuleDao.save(pointRulePO);
		} else {
			BeanUtils.copyProperties(ctr, pointRulePO);
			pointRuleDao.update(pointRulePO);
		}
		// servletContext.setAttribute("pointRules", findAll());
	}

	@Override
	public void delete(long id) {
		PointRulePO pointRulePO = pointRuleDao.get(id);
		pointRuleDao.delete(pointRulePO);
		// servletContext.setAttribute("pointRules", findAll());
	}

	@Override
	public PointRule findById(long id) {
		PointRule pointRule = BeanMapUtils.copy(pointRuleDao.get(id));
		return pointRule;
	}

	@Override
	public List<PointRule> findAll() {
		// List<pointRulePO> linkPOs = pointRuleDao.list();

		List<PointRulePO> linkPOs = pointRuleDao.findAll();

		List<PointRule> rets = new ArrayList<PointRule>();
		for (PointRulePO po : linkPOs) {
			PointRule m = new PointRule();
			BeanUtils.copyProperties(po, m);
			rets.add(m);
		}
		return rets;
	}
	
	@Override
	public List<PointRule> findByCondition( List<QueryRules> qrLst)   {
		List<PointRulePO> linkPOs = pointRuleDao.findByCondition(qrLst);

		List<PointRule> rets = new ArrayList<PointRule>();
		for (PointRulePO po : linkPOs) {
			PointRule m = new PointRule();
			BeanUtils.copyProperties(po, m);
			rets.add(m);
		}
		return rets;
	}
	
	@Override
	public PointRule findOneByCondition( List<QueryRules> qrLst)   {
		PointRule pointRule = BeanMapUtils.copy(pointRuleDao.findOneByCondition(qrLst));
		return pointRule;
	}

	
	
	public void calBackPointFromCardTransactionRecord(Date startTime,Date endTime) {
		int pn = 1;
		Paging paging = new Paging(pn ,100);
		List<QueryRules> qrLst = new ArrayList<>();
		qrLst.add(new QueryRules("point", 0L, QueryRules.OP_EQ));
		qrLst.add(new QueryRules("dealTime", startTime, QueryRules.OP_GE));
		qrLst.add(new QueryRules("dealTime", endTime, QueryRules.OP_LE));
		
		while(true){
			cardTransactionRecordService.paging(paging , qrLst );
			List<CardTransactionRecord> lst = (List<CardTransactionRecord>) paging.getResults();
			if(lst.size()<1){
				break;
			}
			for(CardTransactionRecord ctr: lst ){
				calBackPointFromOneCardTransactionRecord(ctr);
			}
		}
	}
	
	private void calBackPointFromOneCardTransactionRecord(CardTransactionRecord ctr) {
		
		List<QueryRules> qrLst = new ArrayList<>();
		qrLst.add(new QueryRules("status", PointRule.STATUS_ON, QueryRules.OP_EQ));
		qrLst.add(new QueryRules("startTime", new Date(), QueryRules.OP_GE));
		qrLst.add(new QueryRules("endTime", new Date(), QueryRules.OP_LE));
		qrLst.add(new QueryRules("reserve1", ctr.getUserId(), QueryRules.OP_EQ));//针对个人的
		
		//针对个人的返现
		List<PointRule> prLst = findByCondition(qrLst);
		List<PointDetail> pdLst =calBackPointFromPointRule(prLst, ctr);
		for(PointDetail pd: pdLst){
			pointService.updatePoint(pd);
		}
		
		//计算全员返现
		pdLst =calBackPointFromPointRule(getAllUserPointRuleLst(), ctr);
		for(PointDetail pd: pdLst){
			pointService.updatePoint(pd);
		}
		
	}
	
	/**
	 * 根据积分规则计算积分
	 * @param prLst
	 * @param ctr
	 * @return
	 */
	private List<PointDetail> calBackPointFromPointRule(List<PointRule> prLst,CardTransactionRecord ctr) {
		List<PointDetail> pdLst = new ArrayList<>();
		
		for(PointRule pr: prLst ){
			
			String keyWord = ctr.getSysSource()+ctr.getDealTypeName();
			//是否匹配当前规则
			if(keyWord.equals(pr.getReserve4())){
				Integer getRatio = pr.getRatio();
				BigDecimal getFeeAmt = ctr.getFeeAmt();
				BigDecimal backPoint = getFeeAmt.multiply(BigDecimal.valueOf(getRatio));//.divide(BigDecimal.valueOf(100));
				if(backPoint.longValue()<=0){
					//若返现小于0，赠送1积分
					backPoint = BigDecimal.ONE;
				}
				
				//包装积分明细
				PointDetail pd = new PointDetail();
				pd.setOpId(Consts.SYSTEM_OP_USERID);
				pd.setAddPoint(backPoint.longValue());
				pd.setUserId(ctr.getUserId());
				pdLst.add(pd);
			}
		}
		
		return pdLst;
	}
	
	
	List<PointRule> allUserPointRuleLst = null;
	/**
	 * 全员积分规则
	 * @return
	 */
	private List<PointRule> getAllUserPointRuleLst() {
		if(allUserPointRuleLst==null){
			List<QueryRules> qrLst = new ArrayList<>();
			qrLst.add(new QueryRules("status", PointRule.STATUS_ON, QueryRules.OP_EQ));
			qrLst.add(new QueryRules("startTime", new Date(), QueryRules.OP_GE));
			qrLst.add(new QueryRules("endTime", new Date(), QueryRules.OP_LE));
			qrLst.add(new QueryRules("reserve2", 0L, QueryRules.OP_LE));//全体用户
			
			allUserPointRuleLst = findByCondition(qrLst);
		}
		return allUserPointRuleLst;
	}
	
}
