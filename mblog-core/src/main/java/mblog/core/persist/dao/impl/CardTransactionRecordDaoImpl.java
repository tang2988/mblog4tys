package mblog.core.persist.dao.impl;

import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import mblog.core.persist.dao.CardTransactionRecordDao;
import mblog.core.persist.entity.CardTransactionRecordPO;
import mblog.core.persist.entity.FriendLinkPO;
import mblog.core.persist.utils.QueryRules;
import mtons.modules.annotation.Repository;
import mtons.modules.persist.impl.BaseRepositoryImpl;
import mtons.modules.pojos.Paging;

/**
 * @author Beldon
 */
@Repository(entity = CardTransactionRecordPO.class)
public class CardTransactionRecordDaoImpl extends BaseRepositoryImpl<CardTransactionRecordPO> implements CardTransactionRecordDao {
	private static final long serialVersionUID = 754755214307906383L;

	@Override
	@SuppressWarnings("unchecked")
    public List<CardTransactionRecordPO> findAll(){
        Query query = createQuery("from CardTransactionRecordPO am order by am.id");
        return query.list();
    }
	
	
	@Override
	public List<CardTransactionRecordPO> paging(Paging paging, List<QueryRules> qrLst)   {
		PagingQuery<CardTransactionRecordPO> q = pagingQuery(paging);
		
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
		
		q.desc("dealTime");
		return q.list();
	}
	
	
	
	@Override
	public List<CardTransactionRecordPO> findByCondition( List<QueryRules> qrLst)   {
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
}
