package mblog.core.persist.dao;

import java.util.List;

import mblog.core.persist.entity.GoodsPO;
import mblog.core.persist.utils.QueryRules;
import mtons.modules.persist.BaseRepository;
import mtons.modules.pojos.Paging;

/**
 * @author Beldon
 */
public interface GoodsDao extends BaseRepository<GoodsPO> {

    List<GoodsPO> findAll();
    
    public List<GoodsPO> paging(Paging paging, List<QueryRules> qrLst)  ;
    
    
	public List<GoodsPO> findByCondition( List<QueryRules> qrLst)   ;

}
