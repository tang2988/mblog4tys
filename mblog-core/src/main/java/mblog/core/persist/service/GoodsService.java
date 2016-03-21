package mblog.core.persist.service;

import java.util.List;

import mblog.core.data.Goods;
import mblog.core.persist.utils.QueryRules;
import mtons.modules.pojos.Paging;

public interface GoodsService {

	public void paging(Paging paging, List<QueryRules> qrLst);

	void save(Goods ctr);

	void delete(long id);

	Goods findById(long id);

	List<Goods> findAll();
	
	public Goods updateStoreNum(long id,Integer num) ;
}
