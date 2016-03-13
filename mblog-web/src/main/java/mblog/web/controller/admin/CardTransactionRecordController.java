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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import mblog.core.persist.service.CardTransactionRecordService;
import mblog.core.persist.utils.QueryRules;
import mblog.web.controller.BaseController;
import mtons.modules.pojos.Data;
import mtons.modules.pojos.Paging;

/**
 * @author tys
 */
@Controller
@RequestMapping("/admin/cardTransactionRecord")
public class CardTransactionRecordController extends BaseController {
	private static final Logger log = LoggerFactory.getLogger(CardTransactionRecordController.class);
	@Autowired
	private CardTransactionRecordService cardTransactionRecordService;

	@RequestMapping("/list")
	public String list(Integer pn, String sysSource,String yearmonthdatestart,String yearmonthdateend,String mobile, ModelMap model) {
		Paging paging = wrapPage(pn,30);
		
		List<QueryRules> qrLst = new ArrayList<>();
		if(StringUtils.isNotBlank(sysSource)){
			QueryRules qr = new QueryRules("sysSource",sysSource	);	
			qrLst.add(qr);
		}
		
		if(StringUtils.isNotBlank(yearmonthdatestart)){
			QueryRules qr = new QueryRules("deal_data",yearmonthdatestart+" 00:00:00","ge"	);qrLst.add(qr);
		}
		
		if(StringUtils.isNotBlank(yearmonthdateend)){
			QueryRules qr = new QueryRules("deal_data",yearmonthdateend+" 23:59:59"	,"le");qrLst.add(qr);
		}
	
		
		if(StringUtils.isNotBlank(mobile)){
			QueryRules qr = new QueryRules("moblieNoV",mobile	);qrLst.add(qr);
		}
		cardTransactionRecordService.paging(paging, qrLst);
		model.put("page", paging);
		model.put("sysSource", sysSource);
		model.put("yearmonthdatestart", yearmonthdatestart);
		model.put("yearmonthdateend", yearmonthdateend);
		model.put("mobile", mobile);
		
		return "/admin/cardTransactionRecord/list";
	}

	/*@RequestMapping("/delete")
	public String delete(Long id) {
		cardTransactionRecordService.delete(id);
		return "redirect:/admin/cardTransactionRecord/CardTransactionRecord";
	}

	@RequestMapping("/edit")
	public String edit(@RequestParam(value = "id", required = false, defaultValue = "0") long id, Model model) {
		if (id != 0) {
			CardTransactionRecord cardTransactionRecord = cardTransactionRecordService.findById(id);
			model.addAttribute("cardTransactionRecord", cardTransactionRecord);
		}
		return "/admin/cardTransactionRecord/edit";
	}*/

/*	@RequestMapping("/save")
	// @ResponseBody
	public String save(@ModelAttribute(value = "cardTransactionRecord") CardTransactionRecord cardTransactionRecord) {
		// if (cardTransactionRecord == null ||
		// cardTransactionRecord.getSiteName() == null) {
		// return Data.failure(-1, "请输入友情链接站点名称");
		// }
		try {
			cardTransactionRecordService.save(cardTransactionRecord);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/admin/cardTransactionRecord/list";
	}*/
	
	
	@RequestMapping("/sync")
	public @ResponseBody Data sync(String sysSource, String mobile,String yearmonthdatestart,String yearmonthdateend,Model model) {
		try {
			cardTransactionRecordService.syncDataFromSysSource(sysSource, yearmonthdatestart, yearmonthdateend);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Data.success("ok");
	}
	
}
