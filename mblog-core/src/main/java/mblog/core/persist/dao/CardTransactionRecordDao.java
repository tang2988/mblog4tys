package mblog.core.persist.dao;

import java.util.List;
import java.util.Map;

import mblog.core.persist.entity.CardTransactionRecordPO;
import mblog.core.persist.entity.FriendLinkPO;
import mblog.core.persist.utils.QueryRules;
import mtons.modules.persist.BaseRepository;
import mtons.modules.pojos.Paging;

/**
 * @author Beldon
 */
public interface CardTransactionRecordDao extends BaseRepository<CardTransactionRecordPO> {

    List<CardTransactionRecordPO> findAll();
    
    public List<CardTransactionRecordPO> paging(Paging paging, List<QueryRules> qrLst)  ;
    
    
	public List<CardTransactionRecordPO> findByCondition( List<QueryRules> qrLst)   ;

}
