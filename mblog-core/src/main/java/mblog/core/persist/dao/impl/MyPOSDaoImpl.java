package mblog.core.persist.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;

import mblog.core.persist.dao.MyPOSDao;
import mblog.core.persist.entity.MyPOSPO;
import mblog.core.persist.utils.QueryRules;
import mtons.modules.annotation.Repository;
import mtons.modules.persist.impl.BaseRepositoryImpl;
import mtons.modules.pojos.Paging;

/**
 * @author Beldon
 */
@Repository(entity = MyPOSPO.class)
public class MyPOSDaoImpl extends BaseRepositoryImpl<MyPOSPO> implements MyPOSDao {
	private static final long serialVersionUID = 754755214307906383L;

	@Override
	@SuppressWarnings("unchecked")
    public List<MyPOSPO> findAll(){
        Query query = createQuery("from MyPOSPO am order by am.id");
        return query.list();
    }
	
	
	@Override
	public List<MyPOSPO> paging(Paging paging, List<QueryRules> qrLst)   {
		PagingQuery<MyPOSPO> q = pagingQuery(paging);
		
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
	public List<MyPOSPO> findByCondition( List<QueryRules> qrLst)   {
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
