package mblog.core.persist.dao.impl;

import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import mblog.core.persist.dao.PointRuleDao;
import mblog.core.persist.entity.PointRulePO;
import mblog.core.persist.entity.FriendLinkPO;
import mblog.core.persist.entity.GoodsOtherPO;
import mblog.core.persist.entity.PointRulePO;
import mblog.core.persist.utils.QueryRules;
import mtons.modules.annotation.Repository;
import mtons.modules.persist.impl.BaseRepositoryImpl;
import mtons.modules.pojos.Paging;

/**
 * @author Beldon
 */
@Repository(entity = PointRulePO.class)
public class PointRuleDaoImpl extends BaseRepositoryImpl<PointRulePO> implements PointRuleDao {
	private static final long serialVersionUID = 754755214307906383L;

	@Override
	@SuppressWarnings("unchecked")
    public List<PointRulePO> findAll(){
        Query query = createQuery("from PointRulePO am order by am.id");
        return query.list();
    }
	
	
	@Override
	public List<PointRulePO> paging(Paging paging, List<QueryRules> qrLst)   {
		PagingQuery<PointRulePO> q = pagingQuery(paging);
		
		for(QueryRules qr: qrLst){
			if("eq".equals(qr.getOp())){
				q.add(Restrictions.eq(qr.getName(),qr.getVal()));
			}else if("like".equals(qr.getOp())){
				q.add(Restrictions.like(qr.getName(),qr.getVal()));
			}else if("lt".equals(qr.getOp())){
				q.add(Restrictions.lt(qr.getName(),qr.getVal()));
			}else if("gt".equals(qr.getOp())){
				q.add(Restrictions.gt(qr.getName(),qr.getVal()));
			}else if("le".equals(qr.getOp())){
				q.add(Restrictions.le(qr.getName(),qr.getVal()));
			}else if("ge".equals(qr.getOp())){
				q.add(Restrictions.ge(qr.getName(),qr.getVal()));
				
			}else{
				throw new RuntimeException("查询刷卡交易记录异常："+qr);
			}
		}
		
		q.desc("id");
		return q.list();
	}
	
	
	
	@Override
	public List<PointRulePO> findByCondition( List<QueryRules> qrLst)   {
		Criteria q = createCriteria();
		for(QueryRules qr: qrLst){
			if("eq".equals(qr.getOp())){
				q.add(Restrictions.eq(qr.getName(),qr.getVal()));
			}else if("like".equals(qr.getOp())){
				q.add(Restrictions.like(qr.getName(),qr.getVal()));
			}else if("lt".equals(qr.getOp())){
				q.add(Restrictions.lt(qr.getName(),qr.getVal()));
			}else if("gt".equals(qr.getOp())){
				q.add(Restrictions.gt(qr.getName(),qr.getVal()));
			}else if("le".equals(qr.getOp())){
				q.add(Restrictions.le(qr.getName(),qr.getVal()));
			}else if("ge".equals(qr.getOp())){
				q.add(Restrictions.ge(qr.getName(),qr.getVal()));
				
			}else{
				throw new RuntimeException("查询刷卡交易记录异常："+qr);
			}
		}
//		q.addOrder(Order.desc("id"));
		return q.list();
	}
	
	@Override
	public PointRulePO findOneByCondition( List<QueryRules> qrLst)   {
		List<PointRulePO> lst = findByCondition(qrLst);
		return (PointRulePO)(lst!=null&& lst.size()>0? lst.get(0):null);
	}
}
