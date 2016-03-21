package mblog.core.persist.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mblog.core.data.Goods;
import mblog.core.data.PointDetail;
import mblog.core.persist.dao.PointDetailDao;
import mblog.core.persist.entity.GoodsPO;
import mblog.core.persist.entity.PointDetailPO;
import mblog.core.persist.service.PointDetailService;
import mblog.core.persist.service.UserService;
import mblog.core.persist.utils.BeanMapUtils;
import mblog.core.persist.utils.QueryRules;
import mtons.modules.exception.MtonsException;
import mtons.modules.pojos.Paging;

@Service
@Transactional
public class PointDetailServiceImpl implements PointDetailService {
	private static final Logger log = LoggerFactory.getLogger(PointDetailService.class);
    @Autowired
    private PointDetailDao pointDetailDao;

//    @Autowired
//    private ServletContext servletContext;

    @Autowired
    UserService userService;
    
    
	@Override
	@Transactional(readOnly = true)
	public void paging(Paging paging, List<QueryRules> qrLst) {
		List<PointDetailPO> list = pointDetailDao.paging(paging, qrLst);
		List<PointDetail> rets = new ArrayList<>();

		for (PointDetailPO po : list) {
			PointDetail m = new PointDetail();
            BeanUtils.copyProperties(po, m);
			rets.add(m);
		}
		paging.setResults(rets);
	}
	
    @Override
    public void save(PointDetail ctr) {
        PointDetailPO pointDetailPO = new PointDetailPO();

        BeanUtils.copyProperties(ctr, pointDetailPO);

        pointDetailDao.saveOrUpdate(pointDetailPO);

//        servletContext.setAttribute("pointDetails", findAll());
    }

    @Override
    public void delete(long id) {
        PointDetailPO pointDetailPO = pointDetailDao.get(id);
        pointDetailDao.delete(pointDetailPO);
//        servletContext.setAttribute("pointDetails", findAll());
    }

    @Override
    public PointDetail findById(long id) {
    	PointDetail pointDetail = BeanMapUtils.copy(pointDetailDao.get(id));
        return pointDetail;
    }

    @Override
    public List<PointDetail> findAll() {
//        List<pointDetailPO> linkPOs = pointDetailDao.list();

        List<PointDetailPO> linkPOs = pointDetailDao.findAll();

        List<PointDetail> rets = new ArrayList<PointDetail>();
        for (PointDetailPO po : linkPOs) {
        	PointDetail m = new PointDetail();
            BeanUtils.copyProperties(po, m);
            rets.add(m);
        }
        return rets;
    }

}
