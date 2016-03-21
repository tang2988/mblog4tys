package mblog.core.persist.service;

import java.util.List;

import mblog.core.data.Point;
import mblog.core.data.PointDetail;
import mblog.core.persist.utils.QueryRules;
import mtons.modules.pojos.Paging;

public interface PointService {

	public void paging(Paging paging, List<QueryRules> qrLst);

	void save(Point ctr);

	void delete(long id);

	Point findById(long id);

	List<Point> findAll();
	
	public Point updatePoint(PointDetail pd) ;
}
