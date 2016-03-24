package mblog.core.persist.dao;

import java.util.List;

import mblog.core.persist.entity.GoodsOtherPO;
import mblog.core.persist.utils.QueryRules;
import mtons.modules.persist.BaseRepository;
import mtons.modules.pojos.Paging;

/**
 * @author Beldon
 */
public interface GoodsOtherDao extends BaseRepository<GoodsOtherPO> {

    List<GoodsOtherPO> findAll();
    
    public List<GoodsOtherPO> paging(Paging paging, List<QueryRules> qrLst)  ;
    
    
	public List<GoodsOtherPO> findByCondition( List<QueryRules> qrLst)   ;

	public GoodsOtherPO findOneByCondition( List<QueryRules> qrLst)   ;
}
