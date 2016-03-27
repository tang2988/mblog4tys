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

import mblog.core.data.GoodsOther;
import mblog.core.persist.service.GoodsOtherService;
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
	private GoodsOtherService goodsOtherService;

	@RequestMapping("/list")
	public String list(Integer pn,  
			Integer status,String createTime_le,String createTime_ge, ModelMap model) throws ParseException {
		Paging paging = wrapPage(pn,30);
		
		List<QueryRules> qrLst = new ArrayList<>();
		if(status!=null){
			QueryRules qr = new QueryRules("status",status,QueryRules.OP_EQ	);qrLst.add(qr);
		}
		
		if(StringUtils.isNotBlank(createTime_le)){
			QueryRules qr = new QueryRules("createTime",DateUtils.parseDate(createTime_le, new String[]{"yyyy-MM-dd HH:mm:ss"}),QueryRules.OP_LE	);qrLst.add(qr);
		}
		
		if(StringUtils.isNotBlank(createTime_ge)){
			QueryRules qr = new QueryRules("createTime",DateUtils.parseDate(createTime_ge, new String[]{"yyyy-MM-dd HH:mm:ss"}),QueryRules.OP_GE);qrLst.add(qr);
		}
		
	
		goodsOtherService.paging(paging, qrLst);
		model.put("page", paging);
		model.put("status", status);
		model.put("createTime_le", createTime_le);
		model.put("createTime_ge", createTime_ge);
		
		return "/admin/goodsother/list";
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
			List<QueryRules> qrLst = new ArrayList<>();
			qrLst.add( new QueryRules("id", id, QueryRules.OP_EQ));
			qrLst.add( new QueryRules("status", GoodsOther.STATUS_FINISHED, QueryRules.OP_NE));
			qrLst.add( new QueryRules("status", GoodsOther.STATUS_RETURNOK, QueryRules.OP_NE));
			
			GoodsOther goodsOther = goodsOtherService.findOneByCondition(qrLst );
			model.addAttribute("goodsOther", goodsOther);
			return "/admin/goodsother/edit";
		}
		return "/admin/goodsother/save";
	}

	@RequestMapping("/save")
	// @ResponseBody
	public String save(long id,Long goodsId,Long userId,Integer status,Integer buyNum,Long cost,String remark,
			ModelMap model) {
		try {
			long opId = getSubject().getProfile().getId();
			GoodsOther goodsOther = new GoodsOther();
			goodsOther.setId(id);
			goodsOther.setUserId(userId);
			goodsOther.setGoodsId(goodsId);
			goodsOther.setStatus(status);
			goodsOther.setUpdateTime(new Date());
			goodsOther.setBuyNum(buyNum);
			goodsOther.setCost(cost);
			goodsOther.setRemark(remark);
			goodsOther.setOpId(opId);
			goodsOtherService.save(goodsOther); 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/admin/goodsother/list";
	}

	
	@RequestMapping("/editOrder")
	// @ResponseBody
	public String editOrder(long id, Integer status, String reserve3,
			ModelMap model) {
		try {
			long opId = getSubject().getProfile().getId();
			GoodsOther goodsOther = new GoodsOther();
			goodsOther.setId(id);
			goodsOther.setStatus(status);
			goodsOther.setUpdateTime(new Date());
			goodsOther.setReserve3(reserve3);
			goodsOther.setOpId(opId);
			goodsOtherService.update(goodsOther); 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/admin/goodsother/list";
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
