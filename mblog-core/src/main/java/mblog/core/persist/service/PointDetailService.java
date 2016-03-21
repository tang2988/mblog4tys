package mblog.core.persist.service;

import java.util.List;

import mblog.core.data.PointDetail;
import mblog.core.persist.utils.QueryRules;
import mtons.modules.pojos.Paging;

public interface PointDetailService {

	public void paging(Paging paging, List<QueryRules> qrLst);

	void save(PointDetail ctr);

	void delete(long id);

	PointDetail findById(long id);

	List<PointDetail> findAll();
	

}
