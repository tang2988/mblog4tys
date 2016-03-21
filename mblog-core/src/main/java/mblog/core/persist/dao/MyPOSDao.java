package mblog.core.persist.dao;

import java.util.List;

import mblog.core.persist.entity.MyPOSPO;
import mblog.core.persist.utils.QueryRules;
import mtons.modules.persist.BaseRepository;
import mtons.modules.pojos.Paging;

/**
 * @author Beldon
 */
public interface MyPOSDao extends BaseRepository<MyPOSPO> {

    List<MyPOSPO> findAll();
    
    public List<MyPOSPO> paging(Paging paging, List<QueryRules> qrLst)  ;
    
    
	public List<MyPOSPO> findByCondition( List<QueryRules> qrLst)   ;

}
