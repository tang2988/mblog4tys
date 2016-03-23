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
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import mblog.core.data.Goods;
import mblog.core.persist.service.GoodsService;
import mblog.core.persist.utils.QueryRules;
import mblog.web.controller.BaseController;
import mtons.modules.pojos.Paging;

/**
 * @author tys
 */
@Controller
@RequestMapping("/admin/goodsother")
public class GoodsOtherController extends BaseController {
	private static final Logger log = LoggerFactory.getLogger(GoodsOtherController.class);
	@Autowired
	private GoodsService goodsService;

	@RequestMapping("/list")
	public String list(Integer pn,  
			String name,String startTime,String endTime, ModelMap model) throws ParseException {
		Paging paging = wrapPage(pn,30);
		
		List<QueryRules> qrLst = new ArrayList<>();
		if(StringUtils.isNotBlank(name)){
			QueryRules qr = new QueryRules("name",name,"like"	);qrLst.add(qr);
		}
		
		if(StringUtils.isNotBlank(startTime)){
			QueryRules qr = new QueryRules("startTime",DateUtils.parseDate(startTime+" 00:00:00", new String[]{"yyyy-MM-dd hh:mm:ss"}),"ge"	);qrLst.add(qr);
		}
		
		if(StringUtils.isNotBlank(endTime)){
			QueryRules qr = new QueryRules("endTime",DateUtils.parseDate(endTime+" 23:59:59", new String[]{"yyyy-MM-dd hh:mm:ss"}),"le");qrLst.add(qr);
		}
	
		goodsService.paging(paging, qrLst);
		model.put("page", paging);
		model.put("name", name);
		model.put("startTime", startTime);
		model.put("endTime", endTime);
		
		return "/admin/goods/list";
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
			Goods goods = goodsService.findById(id);
			model.addAttribute("goods", goods);
		}
		return "/admin/goods/edit";
	}

	@RequestMapping("/save")
	// @ResponseBody
	public String save(long id,String name,Long price,Integer storeNum,String mainPic,Integer status,Boolean isVip,String startTime,String endTime,Long reserve1,
			String content ,ModelMap model) {
		try {
			long opId = getSubject().getProfile().getId();
			Goods goods = new Goods();
			goods.setId(id);
			goods.setName(name);
			goods.setPrice(price);
			goods.setStoreNum(storeNum);
			goods.setMainPic(mainPic);
			goods.setStatus(status);
			goods.setIsVip(isVip);
			goods.setStartTime(DateUtils.parseDate(startTime+" 00:00:00", new String[]{"yyyy-MM-dd hh:mm:ss"}));
			goods.setEndTime(DateUtils.parseDate(endTime+" 23:59:59", new String[]{"yyyy-MM-dd hh:mm:ss"}));
			goods.setReserve1(reserve1);
			goods.setDescHtm(content);
			
			goods.setOpId(opId);
			goods.setUpdateTime(new Date());
			goodsService.save(goods); 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/admin/goods/list";
	}

	
	/*@RequestMapping("/sync")
	// @ResponseBody
	public @ResponseBody Data sync(String sysSource, String mobile,String yearmonthdatestart,String yearmonthdateend,Model model) {
		try {
			if(StringUtils.isNotBlank(mobile)){
				cardTransactionRecordService.syncDataFromSysSourceByMobile(sysSource,  yearmonthdatestart, yearmonthdateend,mobile);
			}else{
				cardTransactionRecordService.syncDataFromSysSource(sysSource, yearmonthdatestart, yearmonthdateend);	
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Data.success("ok");
//		return "redirect:/admin/cardTransactionRecord/sync";
	}*/
	
}