package mblog.core.persist.dao;

import java.util.List;

import mblog.core.persist.entity.PointDetailPO;
import mblog.core.persist.utils.QueryRules;
import mtons.modules.persist.BaseRepository;
import mtons.modules.pojos.Paging;

/**
 * @author Beldon
 */
public interface PointDetailDao extends BaseRepository<PointDetailPO> {

    List<PointDetailPO> findAll();
    
    public List<PointDetailPO> paging(Paging paging, List<QueryRules> qrLst)  ;
    
    
	public List<PointDetailPO> findByCondition( List<QueryRules> qrLst)   ;

}
