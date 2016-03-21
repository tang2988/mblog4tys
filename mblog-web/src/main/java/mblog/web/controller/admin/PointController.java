package mblog.web.controller.admin;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import mblog.core.data.CardTransactionRecord;
import mblog.core.data.Point;
import mblog.core.data.PointDetail;
import mblog.core.persist.service.PointService;
import mblog.core.persist.utils.QueryRules;
import mblog.web.controller.BaseController;
import mtons.modules.pojos.Paging;

/**
 * @author tys
 */
@Controller
@RequestMapping("/admin/point")
public class PointController extends BaseController {
	private static final Logger log = LoggerFactory.getLogger(PointController.class);
	@Autowired
	private PointService pointService;

	@RequestMapping("/list")
	public String list(Integer pn, String userId,String curPoint_ge,String curPoint_le, ModelMap model) {
		Paging paging = wrapPage(pn,30);
		
		List<QueryRules> qrLst = new ArrayList<>();
		if(StringUtils.isNotBlank(userId)){
			QueryRules qr = new QueryRules("userId",Long.valueOf(userId)	);	
			qrLst.add(qr);
		}
		if(StringUtils.isNotBlank(curPoint_ge)){
			QueryRules qr = new QueryRules("curPoint",curPoint_ge,"ge"	);qrLst.add(qr);
		}
		
		if(StringUtils.isNotBlank(curPoint_le)){
			QueryRules qr = new QueryRules("curPoint",curPoint_le,"le"	);qrLst.add(qr);
		}
		
		pointService.paging(paging, qrLst);
		model.put("page", paging);
		model.put("userId", userId);
		model.put("curPoint_ge", curPoint_ge);
		model.put("curPoint_le", curPoint_le);
		
		return "/admin/point/list";
	}

	/*@RequestMapping("/delete")
	public String delete(Long id) {
		pointService.delete(id);
		return "redirect:/admin/cardTransactionRecord/CardTransactionRecord";
	}*/

	@RequestMapping("/edit")
	public String edit(@RequestParam(value = "id", required = false, defaultValue = "0") long id, Model model) {
		if (id != 0) {
			Point point = pointService.findById(id);
			model.addAttribute("point", point);
		}
		return "/admin/point/edit";
	}

	@RequestMapping("/save")
	// @ResponseBody
	public String save(Long id, String addPoint,String sourceDesc , ModelMap model) {
		try {
			if(StringUtils.isBlank(addPoint)){
				throw new RuntimeException("请填写本次调整积分");
			}
			
			if(StringUtils.isBlank(sourceDesc)){
				throw new RuntimeException("请填写本次调整原因");
			}
			
			long opId = getSubject().getProfile().getId();
			
			PointDetail pd = new PointDetail();
			pd.setAddPoint(Long.valueOf(addPoint));
			pd.setSourceDesc(sourceDesc);
			pd.setPid(id);
			pd.setOpId(opId);
			pointService.updatePoint(pd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/admin/point/list";
	}
	
	
	/*@RequestMapping("/sync")
	// @ResponseBody
	public @ResponseBody Data sync(String sysSource, String mobile,String yearmonthdatestart,String yearmonthdateend,Model model) {
		try {
			if(StringUtils.isNotBlank(mobile)){
				pointService.syncDataFromSysSourceByMobile(sysSource,  yearmonthdatestart, yearmonthdateend,mobile);
			}else{
				pointService.syncDataFromSysSource(sysSource, yearmonthdatestart, yearmonthdateend);	
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Data.success("ok");
//		return "redirect:/admin/cardTransactionRecord/sync";
	}*/
	
}
