package mblog.core.persist.service;

import java.util.Date;
import java.util.List;

import mblog.core.data.PointRule;
import mblog.core.persist.utils.QueryRules;
import mtons.modules.pojos.Paging;

public interface PointRuleService {

	public void paging(Paging paging, List<QueryRules> qrLst);

	void save(PointRule ctr);

	void delete(long id);

	PointRule findById(long id);

	List<PointRule> findAll();
	
	public List<PointRule> findByCondition( List<QueryRules> qrLst);
	
	public PointRule findOneByCondition( List<QueryRules> qrLst) ;
	
	public void calBackPointFromCardTransactionRecord(Date startTime,Date endTime) ;
}
