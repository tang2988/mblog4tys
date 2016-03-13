package mblog.core.persist.service;

import java.util.List;

import mblog.core.data.MyPOS;
import mblog.core.persist.utils.QueryRules;
import mtons.modules.pojos.Paging;

public interface MyPOSService {

	public void paging(Paging paging, List<QueryRules> qrLst);

	void save(MyPOS ctr);

	void delete(long id);

	MyPOS findById(long id);

	List<MyPOS> findAll();

	public List<MyPOS> findByCondition(List<QueryRules> qrLst) ;
	
	public MyPOS checkMyPos(Long userId,String sysSource, String yearmonthdatestart, String yearmonthdateend, String moblieNo,
			String terminalcode, String transAcount);
}
