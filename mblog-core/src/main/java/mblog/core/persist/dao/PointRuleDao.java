package mblog.core.persist.dao;

import java.util.List;

import mblog.core.persist.entity.PointRulePO;
import mblog.core.persist.utils.QueryRules;
import mtons.modules.persist.BaseRepository;
import mtons.modules.pojos.Paging;

/**
 * @author Beldon
 */
public interface PointRuleDao extends BaseRepository<PointRulePO> {

    List<PointRulePO> findAll();
    
    public List<PointRulePO> paging(Paging paging, List<QueryRules> qrLst)  ;
    
    
	public List<PointRulePO> findByCondition( List<QueryRules> qrLst)   ;

}
