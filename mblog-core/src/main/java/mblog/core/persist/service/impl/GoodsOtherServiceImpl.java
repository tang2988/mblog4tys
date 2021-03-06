package mblog.core.persist.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mblog.core.data.Goods;
import mblog.core.data.GoodsOther;
import mblog.core.data.PointDetail;
import mblog.core.persist.dao.GoodsOtherDao;
import mblog.core.persist.entity.GoodsOtherPO;
import mblog.core.persist.service.GoodsOtherService;
import mblog.core.persist.service.GoodsService;
import mblog.core.persist.service.PointService;
import mblog.core.persist.service.UserService;
import mblog.core.persist.utils.BeanMapUtils;
import mblog.core.persist.utils.QueryRules;
import mtons.modules.pojos.Paging;

@Service
@Transactional
public class GoodsOtherServiceImpl implements GoodsOtherService {
	private static final Logger log = LoggerFactory.getLogger(GoodsOtherService.class);
    @Autowired
    private GoodsOtherDao goodsOtherDao;

//    @Autowired
//    private ServletContext servletContext;

    @Autowired
    UserService userService;
    @Autowired
    GoodsService goodsService;
    @Autowired
    PointService pointService;
    
    
	@Override
	@Transactional(readOnly = true)
	public void paging(Paging paging, List<QueryRules> qrLst) {
		List<GoodsOtherPO> list = goodsOtherDao.paging(paging, qrLst);
		List<GoodsOther> rets = new ArrayList<>();

		for (GoodsOtherPO po : list) {
			GoodsOther m = new GoodsOther();
            BeanUtils.copyProperties(po, m);
			rets.add(m);
		}
		paging.setResults(rets);
	}
	
    @Override
    public void save(GoodsOther ctr) {
        GoodsOtherPO goodsOtherPO = new GoodsOtherPO();

        BeanUtils.copyProperties(ctr, goodsOtherPO);

        goodsOtherDao.save( goodsOtherPO);
        BeanUtils.copyProperties(goodsOtherPO, ctr);
//        servletContext.setAttribute("goodsOthers", findAll());
    }

    
    @Override
    public void update(GoodsOther ctr) {
        GoodsOtherPO goodsOtherPO = goodsOtherDao.get(ctr.getId());
        goodsOtherPO.setStatus(ctr.getStatus());
        goodsOtherPO.setReserve3(ctr.getReserve3());
        goodsOtherPO.setUpdateTime(ctr.getUpdateTime());
        goodsOtherPO.setOpId(ctr.getOpId());
        
        goodsOtherDao.update(goodsOtherPO);

//        servletContext.setAttribute("goodsOthers", findAll());
    }
    
    @Override
    public void delete(long id) {
        GoodsOtherPO goodsOtherPO = goodsOtherDao.get(id);
        goodsOtherDao.delete(goodsOtherPO);
//        servletContext.setAttribute("goodsOthers", findAll());
    }

    @Override
    public GoodsOther findById(long id) {
    	GoodsOther goodsOther = BeanMapUtils.copy(goodsOtherDao.get(id));
        return goodsOther;
    }
    @Override
    public List<GoodsOther> findByCondition( List<QueryRules> qrLst)   {
    	
    	List<GoodsOtherPO> linkPOs = goodsOtherDao.findByCondition(qrLst);

        List<GoodsOther> rets = new ArrayList<GoodsOther>();
        for (GoodsOtherPO po : linkPOs) {
        	GoodsOther m = new GoodsOther();
            BeanUtils.copyProperties(po, m);
            rets.add(m);
        }
        return rets;
    }
    
	@Override
	public GoodsOther findOneByCondition( List<QueryRules> qrLst)   {
		GoodsOtherPO po = goodsOtherDao.findOneByCondition(qrLst);
		if(po==null){
			return null;
		}
		GoodsOther m = new GoodsOther();
        BeanUtils.copyProperties(po, m);
        return (m);
	}

    @Override
    public List<GoodsOther> findAll() {
//        List<goodsOtherPO> linkPOs = goodsOtherDao.list();

        List<GoodsOtherPO> linkPOs = goodsOtherDao.findAll();

        List<GoodsOther> rets = new ArrayList<GoodsOther>();
        for (GoodsOtherPO po : linkPOs) {
        	GoodsOther m = new GoodsOther();
            BeanUtils.copyProperties(po, m);
            rets.add(m);
        }
        return rets;
    }
	@Override
	@Transactional
	public synchronized GoodsOther buyGoods(GoodsOther goodsOther) {
		Goods goods = goodsService.updateStoreNum(goodsOther.getGoodsId(), goodsOther.getBuyNum());
		
		goodsOther.setCost(goodsOther.getBuyNum()*goods.getPrice());
		goodsOther.setUpdateTime(new Date());
		goodsOther.setStatus(GoodsOther.STATUS_BUYED);
		save(goodsOther);
		
		
		PointDetail pd = new PointDetail();
		pd.setAddPoint(-1* goodsOther.getCost());
		pd.setOpId(goodsOther.getUserId());
		pd.setUserId(goodsOther.getUserId());
		pd.setSourceDesc("支出："+goods.getName());
		pd.setRelateId(goodsOther.getId());
		pointService.updatePoint(pd);
		
		
		goodsOther.setStatus(GoodsOther.STATUS_PAYED);
		update(goodsOther);
		return goodsOther;
	}
    
}
