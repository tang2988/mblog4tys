package mblog.core.persist.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mblog.core.data.PointRule;
import mblog.core.persist.dao.PointRuleDao;
import mblog.core.persist.entity.PointRulePO;
import mblog.core.persist.service.PointRuleService;
import mblog.core.persist.service.UserService;
import mblog.core.persist.utils.BeanMapUtils;
import mblog.core.persist.utils.QueryRules;
import mtons.modules.pojos.Paging;

@Service
@Transactional
public class PointRuleServiceImpl implements PointRuleService {
	private static final Logger log = LoggerFactory.getLogger(PointRuleService.class);
	@Autowired
	private PointRuleDao pointRuleDao;

	// @Autowired
	// private ServletContext servletContext;

	@Autowired
	UserService userService;

	@Override
	@Transactional(readOnly = true)
	public void paging(Paging paging, List<QueryRules> qrLst) {
		List<PointRulePO> list = pointRuleDao.paging(paging, qrLst);
		List<PointRule> rets = new ArrayList<>();

		for (PointRulePO po : list) {
			PointRule m = new PointRule();
			BeanUtils.copyProperties(po, m);
			rets.add(m);
		}
		paging.setResults(rets);
	}

	@Override
	public void save(PointRule ctr) {
		PointRulePO pointRulePO = pointRuleDao.get(ctr.getId());
		if (pointRulePO == null) {
			pointRulePO = new PointRulePO();
			BeanUtils.copyProperties(ctr, pointRulePO);
			pointRuleDao.save(pointRulePO);
		} else {
			BeanUtils.copyProperties(ctr, pointRulePO);
			pointRuleDao.update(pointRulePO);
		}
		// servletContext.setAttribute("pointRules", findAll());
	}

	@Override
	public void delete(long id) {
		PointRulePO pointRulePO = pointRuleDao.get(id);
		pointRuleDao.delete(pointRulePO);
		// servletContext.setAttribute("pointRules", findAll());
	}

	@Override
	public PointRule findById(long id) {
		PointRule pointRule = BeanMapUtils.copy(pointRuleDao.get(id));
		return pointRule;
	}

	@Override
	public List<PointRule> findAll() {
		// List<pointRulePO> linkPOs = pointRuleDao.list();

		List<PointRulePO> linkPOs = pointRuleDao.findAll();

		List<PointRule> rets = new ArrayList<PointRule>();
		for (PointRulePO po : linkPOs) {
			PointRule m = new PointRule();
			BeanUtils.copyProperties(po, m);
			rets.add(m);
		}
		return rets;
	}

}
