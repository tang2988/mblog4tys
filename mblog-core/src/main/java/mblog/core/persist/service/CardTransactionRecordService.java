package mblog.core.persist.service;

import java.util.List;

import mblog.core.data.CardTransactionRecord;
import mblog.core.persist.utils.QueryRules;
import mtons.modules.pojos.Paging;

public interface CardTransactionRecordService {

	public void paging(Paging paging, List<QueryRules> qrLst) ;
	
    void save(CardTransactionRecord ctr);

    void delete(long id);

    CardTransactionRecord findById(long id);

    List<CardTransactionRecord> findAll();
    
    
    
    public void syncDataFromRYXByMobile( String yearmonthdatestart,String yearmonthdateend,String moblieNo);
    
    
    public void syncDataFromRYX( String yearmonthdatestart,String yearmonthdateend ) ;
    
    
    public Boolean checkVip( String sysSource, String yearmonthdatestart,String yearmonthdateend,String moblieNo,String terminalcode,String transAcount);
}
