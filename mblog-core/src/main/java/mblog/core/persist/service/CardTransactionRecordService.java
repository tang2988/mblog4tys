package mblog.core.persist.service;

import java.util.List;

import mblog.core.data.CardTransactionRecord;
import mblog.core.persist.utils.QueryRules;
import mtons.modules.pojos.Paging;

public interface CardTransactionRecordService {

	public void paging(Paging paging, List<QueryRules> qrLst);

	void save(CardTransactionRecord ctr);

    public void update(CardTransactionRecord ctr) ;
    
	void delete(long id);

	CardTransactionRecord findById(long id);

	List<CardTransactionRecord> findAll();
	
	public List<CardTransactionRecord> getDataFromSysSource (String sysSource, String yearmonthdatestart,String yearmonthdateend,String moblieNo,String terminalcode ) ;

	public void syncDataFromSysSourceByMobile(String sysSource, String yearmonthdatestart, String yearmonthdateend,
			String moblieNo,String terminalcode,Long userId);

	public void syncDataFromSysSource(String sysSource, String yearmonthdatestart, String yearmonthdateend);

	
    public Integer countRecord(CardTransactionRecord ctr);
}
