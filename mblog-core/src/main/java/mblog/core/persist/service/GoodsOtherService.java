package mblog.core.persist.service;

import java.util.List;

import mblog.core.data.GoodsOther;
import mblog.core.persist.utils.QueryRules;
import mtons.modules.pojos.Paging;

public interface GoodsOtherService {

	public void paging(Paging paging, List<QueryRules> qrLst);

	void save(GoodsOther ctr);

	void delete(long id);

	GoodsOther findById(long id);

	List<GoodsOther> findAll();
	
	public GoodsOther buyGoods(GoodsOther goodsOther) ;
}
