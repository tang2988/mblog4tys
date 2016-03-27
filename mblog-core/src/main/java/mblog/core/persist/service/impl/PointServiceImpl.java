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
import mblog.core.data.Point;
import mblog.core.data.PointDetail;
import mblog.core.persist.dao.PointDao;
import mblog.core.persist.dao.PointDetailDao;
import mblog.core.persist.entity.GoodsPO;
import mblog.core.persist.entity.PointDetailPO;
import mblog.core.persist.entity.PointPO;
import mblog.core.persist.service.PointDetailService;
import mblog.core.persist.service.PointService;
import mblog.core.persist.service.UserService;
import mblog.core.persist.utils.BeanMapUtils;
import mblog.core.persist.utils.QueryRules;
import mtons.modules.exception.MtonsException;
import mtons.modules.pojos.Paging;

@Service
@Transactional
public class PointServiceImpl implements PointService {
	private static final Logger log = LoggerFactory.getLogger(PointService.class);
	@Autowired
	private PointDao pointDao;

	@Autowired
	private PointDetailService pointDetailService;

	// @Autowired
	// private ServletContext servletContext;

	@Autowired
	UserService userService;

	@Override
	@Transactional(readOnly = true)
	public void paging(Paging paging, List<QueryRules> qrLst) {
		List<PointPO> list = pointDao.paging(paging, qrLst);
		List<Point> rets = new ArrayList<>();

		for (PointPO po : list) {
			Point m = new Point();
			BeanUtils.copyProperties(po, m);
			rets.add(m);
		}
		paging.setResults(rets);
	}

	@Override
	public void save(Point ctr) {
		PointPO pointPO = new PointPO();

		BeanUtils.copyProperties(ctr, pointPO);

		pointDao.saveOrUpdate(pointPO);

		// servletContext.setAttribute("points", findAll());
	}

	@Override
	public void delete(long id) {
		PointPO pointPO = pointDao.get(id);
		pointDao.delete(pointPO);
		// servletContext.setAttribute("points", findAll());
	}

	@Override
	public Point findById(long id) {
		Point point = BeanMapUtils.copy(pointDao.get(id));
		return point;
	}

	@Override
	public List<Point> findAll() {
		// List<pointPO> linkPOs = pointDao.list();

		List<PointPO> linkPOs = pointDao.findAll();

		List<Point> rets = new ArrayList<Point>();
		for (PointPO po : linkPOs) {
			Point m = new Point();
			BeanUtils.copyProperties(po, m);
			rets.add(m);
		}
		return rets;
	}

	@Override
	@Transactional
	public synchronized Point updatePoint(PointDetail pd) {
		List<QueryRules> qrLst = new ArrayList<>();
		qrLst.add(new QueryRules("userId",pd.getUserId(),QueryRules.OP_EQ));
		PointPO po =pointDao.findOneByCondition(qrLst );
		
		if (po != null) {
			Long old = po.getCurPoint();
			long num = pd.getAddPoint();// 变动

			if (num < 0) {// 消费
				if (old < (-1*num)) {
					throw new MtonsException("您的积分不足以抵扣该笔交易");
				}
				po.setUsedPoint(po.getUsedPoint() + num);
				po.setCurPoint(old + num);

			} else {
				po.setHadPoint(po.getHadPoint() + num);
				po.setCurPoint(old + num);
			}
			po.setUpdateTime(new Date());
			pd.setPid(po.getId());
			pd.setUserId(po.getUserId());
			pd.setAddPoint(num);
			pd.setBeforePoint(old);
			pd.setAfterPoint(po.getCurPoint());
			pd.setUpdateTime(new Date());
			pointDetailService.save(pd);
		}
		Point m = new Point();
		BeanUtils.copyProperties(po, m);
		return m;
	}
	
	
}
