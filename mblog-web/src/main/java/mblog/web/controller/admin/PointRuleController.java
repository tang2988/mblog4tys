package mblog.web.controller.admin;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import mblog.core.data.PointRule;
import mblog.core.persist.service.PointRuleService;
import mblog.core.persist.utils.QueryRules;
import mblog.web.controller.BaseController;
import mtons.modules.pojos.Data;
import mtons.modules.pojos.Paging;

/**
 * @author tys
 */
@Controller
@RequestMapping("/admin/pointrule")
public class PointRuleController extends BaseController {
	private static final Logger log = LoggerFactory.getLogger(PointRuleController.class);
	@Autowired
	private PointRuleService pointRuleService;

	@RequestMapping("/list")
	public String list(Integer pn, String serialNo,String type, 
			String status,String startTime,String endTime, ModelMap model) throws ParseException {
		Paging paging = wrapPage(pn,30);
		
		List<QueryRules> qrLst = new ArrayList<>();
		if(StringUtils.isNotBlank(serialNo)){
			QueryRules qr = new QueryRules("sysSource",serialNo,"like"	);	
			qrLst.add(qr);
		}
		if(StringUtils.isNotBlank(type)){
			QueryRules qr = new QueryRules("type",Integer.valueOf(type)	);qrLst.add(qr);
		}
		if(StringUtils.isNotBlank(status)){
			QueryRules qr = new QueryRules("status",status	);qrLst.add(qr);
		}
		
		if(StringUtils.isNotBlank(startTime)){
			QueryRules qr = new QueryRules("startTime",DateUtils.parseDate(startTime+" 00:00:00", new String[]{"yyyy-MM-dd HH:mm:ss"}),"ge"	);qrLst.add(qr);
		}
		
		if(StringUtils.isNotBlank(endTime)){
			QueryRules qr = new QueryRules("endTime",DateUtils.parseDate(endTime+" 23:59:59", new String[]{"yyyy-MM-dd HH:mm:ss"}),"le");qrLst.add(qr);
		}
	
		pointRuleService.paging(paging, qrLst);
		model.put("page", paging);
		model.put("serialNo", serialNo);
		model.put("type", type);
		model.put("status", status);
		model.put("startTime", startTime);
		model.put("endTime", endTime);
		
		return "/admin/pointrule/list";
	}

	/*@RequestMapping("/delete")
	public String delete(Long id) {
		cardTransactionRecordService.delete(id);
		return "redirect:/admin/cardTransactionRecord/CardTransactionRecord";
	}
*/
	@RequestMapping("/edit")
	public String edit(@RequestParam(value = "id", required = false, defaultValue = "0") long id, Model model) {
		if (id != 0) {
			PointRule pointRule = pointRuleService.findById(id);
			if(pointRule.getReserve2()==0){
				pointRule=null;//全体用户积分规则是系统默认选项，不支持修改
			}
			model.addAttribute("pointRule", pointRule);
		}
		return "/admin/pointrule/edit";
	}

	@RequestMapping("/save")
	// @ResponseBody
	public String save(Long id, String serialNo,Integer type,Integer direction,Integer ratio, 
			Integer status,String startTime,String endTime,String reserve3,Long reserve1,Long reserve2,ModelMap model) {
		try {
			long opId = getSubject().getProfile().getId();
			PointRule pointRule = new PointRule();
			pointRule.setId(id);
			pointRule.setSerialNo(type+""+StringUtils.leftPad(serialNo, 4,"0"));
			pointRule.setType(type);
			pointRule.setDirection(direction);
			pointRule.setRatio(ratio);
			pointRule.setStatus(status);
			pointRule.setStartTime(DateUtils.parseDate(startTime+" 00:00:00", new String[]{"yyyy-MM-dd HH:mm:ss"}));
			pointRule.setEndTime(DateUtils.parseDate(endTime+" 23:59:59", new String[]{"yyyy-MM-dd HH:mm:ss"}));
			pointRule.setReserve3(reserve3);
			pointRule.setOpId(opId);
			pointRule.setReserve1(reserve1);
			//全体用户积分规则是系统默认选项，不支持修改
			reserve2=1L;
			pointRule.setReserve2(reserve2);
			pointRule.setUpdateTime(new Date());
			pointRuleService.save(pointRule);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/admin/pointrule/list";
	}
	
	
	@SuppressWarnings("deprecation")
	@RequestMapping("/sync")
	public @ResponseBody Data sync(String startTime,String endTime,Model model) {
		try {
			pointRuleService.calCardTransactionRecordPoint(DateUtils.parseDate(startTime+" 00:00:00", new String[]{"yyyy-MM-dd HH:mm:ss"}), DateUtils.parseDate(endTime+" 23:59:59", new String[]{"yyyy-MM-dd HH:mm:ss"}));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Data.success("ok");
	}
	
}
