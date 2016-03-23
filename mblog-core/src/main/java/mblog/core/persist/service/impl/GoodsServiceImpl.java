package mblog.core.persist.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mblog.core.data.AccountProfile;
import mblog.core.data.Goods;
import mblog.core.persist.dao.GoodsDao;
import mblog.core.persist.entity.GoodsPO;
import mblog.core.persist.entity.PointRulePO;
import mblog.core.persist.entity.UserPO;
import mblog.core.persist.service.GoodsService;
import mblog.core.persist.service.UserService;
import mblog.core.persist.utils.BeanMapUtils;
import mblog.core.persist.utils.QueryRules;
import mtons.modules.exception.MtonsException;
import mtons.modules.pojos.Paging;

@Service
@Transactional
public class GoodsServiceImpl implements GoodsService {
	private static final Logger log = LoggerFactory.getLogger(GoodsService.class);
    @Autowired
    private GoodsDao goodsDao;

//    @Autowired
//    private ServletContext servletContext;

    @Autowired
    UserService userService;
    
    
	@Override
	@Transactional(readOnly = true)
	public void paging(Paging paging, List<QueryRules> qrLst) {
		List<GoodsPO> list = goodsDao.paging(paging, qrLst);
		List<Goods> rets = new ArrayList<>();

		for (GoodsPO po : list) {
			Goods m = new Goods();
            BeanUtils.copyProperties(po, m);
			rets.add(m);
		}
		paging.setResults(rets);
	}
	
    @Override
    public void save(Goods ctr) {
        GoodsPO goodsPO = goodsDao.get(ctr.getId());
        if (goodsPO == null) {
        	goodsPO = new GoodsPO();
			BeanUtils.copyProperties(ctr, goodsPO);
			goodsDao.save(goodsPO);
		} else {
			BeanUtils.copyProperties(ctr, goodsPO);
			goodsDao.update(goodsPO);
		}
//        servletContext.setAttribute("goodss", findAll());
    }

    @Override
    public void delete(long id) {
        GoodsPO goodsPO = goodsDao.get(id);
        goodsDao.delete(goodsPO);
//        servletContext.setAttribute("goodss", findAll());
    }

    @Override
    public Goods findById(long id) {
    	Goods goods = BeanMapUtils.copy(goodsDao.get(id));
        return goods;
    }

    @Override
    public List<Goods> findAll() {
//        List<goodsPO> linkPOs = goodsDao.list();

        List<GoodsPO> linkPOs = goodsDao.findAll();

        List<Goods> rets = new ArrayList<Goods>();
        for (GoodsPO po : linkPOs) {
        	Goods m = new Goods();
            BeanUtils.copyProperties(po, m);
            rets.add(m);
        }
        return rets;
    }
    
    @Override
	@Transactional
	public synchronized Goods updateStoreNum(long id,Integer num) {
    	GoodsPO po = goodsDao.get(id);
		if (po != null) {
			Integer old = po.getStoreNum();
			if( num<0&& old<(num)){
				throw new MtonsException("不好意思，该商品已售完");
			}
			po.setStoreNum(old-num);;
		}
		Goods m = new Goods();
        BeanUtils.copyProperties(po, m);
		return m;
	}
}
