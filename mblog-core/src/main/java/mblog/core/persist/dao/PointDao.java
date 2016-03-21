package mblog.core.persist.dao;

import java.util.List;

import mblog.core.persist.entity.PointPO;
import mblog.core.persist.utils.QueryRules;
import mtons.modules.persist.BaseRepository;
import mtons.modules.pojos.Paging;

/**
 * @author Beldon
 */
public interface PointDao extends BaseRepository<PointPO> {

    List<PointPO> findAll();
    
    public List<PointPO> paging(Paging paging, List<QueryRules> qrLst)  ;
    
    
	public List<PointPO> findByCondition( List<QueryRules> qrLst)   ;

}
