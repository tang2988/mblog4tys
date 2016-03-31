/*
+--------------------------------------------------------------------------
|   Mblog [#RELEASE_VERSION#]
|   ========================================
|   Copyright (c) 2014, 2015 mtons. All Rights Reserved
|   http://www.mtons.com
|
+---------------------------------------------------------------------------
*/
package mblog.web.controller.desk.user;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import mblog.base.lang.EnumPrivacy;
import mblog.core.data.AccountProfile;
import mblog.core.data.Goods;
import mblog.core.data.GoodsOther;
import mblog.core.data.MyPOS;
import mblog.core.data.Point;
import mblog.core.data.PointRule;
import mblog.core.data.User;
import mblog.core.persist.service.CardTransactionRecordService;
import mblog.core.persist.service.CommentService;
import mblog.core.persist.service.FavorService;
import mblog.core.persist.service.FeedsService;
import mblog.core.persist.service.FollowService;
import mblog.core.persist.service.GoodsOtherService;
import mblog.core.persist.service.GoodsService;
import mblog.core.persist.service.MyPOSService;
import mblog.core.persist.service.NotifyService;
import mblog.core.persist.service.PointDetailService;
import mblog.core.persist.service.PointRuleService;
import mblog.core.persist.service.PointService;
import mblog.core.persist.service.PostService;
import mblog.core.persist.service.UserService;
import mblog.core.persist.utils.CardTransactionRecordUtils;
import mblog.core.persist.utils.QueryRules;
import mblog.core.persist.utils.StringUtil;
import mblog.shiro.authc.AccountSubject;
import mblog.web.controller.BaseController;
import mblog.web.controller.admin.CardTransactionRecordController;
import mblog.web.controller.desk.Views;
import mtons.modules.lang.Const;
import mtons.modules.pojos.Data;
import mtons.modules.pojos.Paging;
import mtons.modules.pojos.UserProfile;

/**
 * 用户主页
 * @author langhsu
 *
 */
@Controller
public class HomeController extends BaseController {
	private static final Logger log = LoggerFactory.getLogger(CardTransactionRecordController.class);
	
	@Autowired
	private PostService postService;
	@Autowired
	private FeedsService feedsService;
	@Autowired
	private CommentService commentService;
	@Autowired
	private UserService userService;
	@Autowired
	private FollowService followService;
	@Autowired
	private FavorService favorService;
	@Autowired
	private NotifyService notifyService;
	@Autowired
	MyPOSService myPOSService;
	@Autowired
	PointService pointService;	
	@Autowired
	PointRuleService pointRuleService;	
	@Autowired
	private PointDetailService pointDetailService;
	@Autowired
	private GoodsService goodsService;
	@Autowired
	private GoodsOtherService goodsOtherService;
	@Autowired
	private CardTransactionRecordService cardTransactionRecordService;
	
	/**
	 * 用户主页
	 * @param pn
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/home")
	public String home(Integer pn, ModelMap model) {
		Paging paging = wrapPage(pn);
		AccountSubject subject = getSubject();

		feedsService.findUserFeeds(paging, subject.getProfile().getId(), Const.ZERO, Const.ZERO);

		model.put("page", paging);
		initUser(model);

		return getView(Views.HOME_FEEDS);
	}

	/**
	 * 我发布的文章
	 * @param pn
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/home", params = "method=posts")
	public String posts(Integer pn, ModelMap model) {
		Paging page = wrapPage(pn);
		UserProfile up = getSubject().getProfile();
		postService.pagingByAuthorId(page, up.getId(), EnumPrivacy.ALL);

		model.put("page", page);
		initUser(model);

		return getView(Views.HOME_POSTS);
	}

	
	/**
	 * 我POS
	 * @param pn
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/home", params = "method=myPos")
	public String myPos(Integer pn, String sysSource,String yearmonthdatestart,String yearmonthdateend, ModelMap model) {
		Paging page = wrapPage(pn);
		UserProfile up = getSubject().getProfile();


		List<QueryRules> qrLst = new ArrayList<>();
		if(StringUtils.isNotBlank(sysSource)){
			QueryRules qr = new QueryRules("sysSource",sysSource	);	
			qrLst.add(qr);
		}
		try {
			if(StringUtils.isNotBlank(yearmonthdatestart)){
				QueryRules qr = new QueryRules("dealTime",DateUtils.parseDate(yearmonthdatestart+" 00:00:00", new String[]{"yyyy-MM-dd HH:mm:ss"}),"ge"	);
				qrLst.add(qr);
			}
			
			if(StringUtils.isNotBlank(yearmonthdateend)){
				QueryRules qr = new QueryRules("dealTime",DateUtils.parseDate(yearmonthdateend+" 23:59:59", new String[]{"yyyy-MM-dd HH:mm:ss"})	,"le");
				qrLst.add(qr);
			}
		} catch (ParseException e) {
			log.error("日期解析报错",e);
		}

	
		if(1==1){
			QueryRules qr = new QueryRules("userId",up.getId()	);qrLst.add(qr);
		}
		cardTransactionRecordService.paging(page, qrLst);

		model.put("page", page);
		initUser(model);
		model.put("sysSource", sysSource);
		model.put("yearmonthdatestart", yearmonthdatestart);
		model.put("yearmonthdateend", yearmonthdateend);
		return getView(Views.HOME_myPos);
	}
	
	
	/**
	 * 我myPosMgr
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/home", params = "method=myPosMgr")
	public String myPosMgr(  ModelMap model) {
		UserProfile up = getSubject().getProfile();

		List<QueryRules> qrLst = new ArrayList<>();
		qrLst.add( new QueryRules("reserve1",up.getId(),QueryRules.OP_EQ));
		List<MyPOS> myPosLst = myPOSService.findByCondition(qrLst);

		model.put("myPosLst", myPosLst);
		initUser(model);
		return getView(Views.HOME_myPosMgr);
	}
	
	
	/**
	 * 我myPosMgr
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/home", params = "method=myPosMgrAdd")
	public @ResponseBody Data myPosMgrAdd( String sysSource,String mobile,String yearmonthdatestart,String yearmonthdateend,String terminalcode,String transAcount, ModelMap model) {
		
		UserProfile up = getSubject().getProfile();
		User curUser = userService.getByUsername(up.getUsername());
		if(!StringUtil.isMobilePhone(mobile) ){
			return Data.failure("手机号格式错误，请重新输入");
		}
		
		if(!StringUtil.isRYXSerialNo(terminalcode) ){
			return Data.failure("POS终端号格式错误，请重新输入");
		}
		
		List<QueryRules> qrLst = new ArrayList<>();
//		qrLst.add( new QueryRules("reserve1",curUser.getId(),QueryRules.OP_EQ));
		qrLst.add( new QueryRules("sysSource",sysSource,QueryRules.OP_EQ));
		qrLst.add( new QueryRules("moblieNoV",mobile,QueryRules.OP_EQ));
		qrLst.add( new QueryRules("reserve2",terminalcode,QueryRules.OP_EQ));
		List<MyPOS> myPosLst = myPOSService.findByCondition(qrLst);
	    if(myPosLst!=null && myPosLst.size()>0){
	    	return Data.failure("该POS已录入系统，如不在本人名下，请联系销售方");
	    }
		
		MyPOS mPos = myPOSService.checkMyPos(curUser.getId(),sysSource, yearmonthdatestart, yearmonthdatestart, mobile, terminalcode,transAcount);
		if(mPos.getId()>0){
			curUser.setMobile(mobile);
			//
			userService.updateMobile(curUser.getId(), mobile);
			
			//是我们的POS客户，同步他的POS交易记录
    		new Thread(new Runnable() {
				@Override
				public void run() {
					cardTransactionRecordService.syncDataFromSysSourceByMobile(mPos.getSysSource(), CardTransactionRecordUtils.initStartDateStr, DateFormatUtils.format(new Date(), "yyyyMMdd"), mPos.getMoblieNoV(),mPos.getReserve2(),mPos.getReserve1());					
				}
			}).start();
    		
			return Data.success("恭喜您通过了POS验证，数据正在同步中，请稍后查看交易记录");	
		}
		return Data.failure("抱歉，您未通过了POS验证，请填写正确的刷卡记录再次提交验证，或联系本站");
		
	}
	
	/**
	 * 我myPosMgr
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/mpos" )
	public @ResponseBody Data mpos( ModelMap model) {
		myPOSService.aa();
		return Data.failure("抱歉，您未通过了POS验证，请填写正确的刷卡记录再次提交验证，或联系本站");
		
	}
	
	
	
	/**
	 * 我发表的评论
	 * @param pn
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/home", params = "method=comments")
	public String comments(Integer pn, ModelMap model) {
		Paging page = wrapPage(pn);
		AccountSubject subject = getSubject();
		page = commentService.paging4Home(page, subject.getProfile().getId());

		model.put("page", page);
		initUser(model);

		return getView(Views.HOME_COMMENTS);
	}

	/**
	 * 我喜欢过的文章
	 * @param pn
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/home/favors")
	public String favors(Integer pn, ModelMap model) {
		Paging page = wrapPage(pn);
		UserProfile profile = getSubject().getProfile();
		favorService.pagingByOwnId(page, profile.getId());

		model.put("page", page);
		initUser(model);

		return getView(Views.HOME_FAVORS);
	}

	/**
	 * 我的关注
	 * @param pn
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/home/follows")
	public String follows(Integer pn, ModelMap model) {
		Paging page = wrapPage(pn);
		UserProfile profile = getSubject().getProfile();
		followService.follows(page, profile.getId());

		model.put("page", page);
		initUser(model);

		return getView(Views.HOME_FOLLOWS);
	}

	/**
	 * 我的粉丝
	 * @param pn
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/home/fans")
	public String fans(Integer pn, ModelMap model) {
		Paging page = wrapPage(pn);
		UserProfile profile = getSubject().getProfile();
		followService.fans(page, profile.getId());

		model.put("page", page);
		initUser(model);

		return getView(Views.HOME_FANS);
	}

	/**
	 * 我的通知
	 * @param pn
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/home/notifies")
	public String notifies(Integer pn, ModelMap model) {
		Paging page = wrapPage(pn);
		UserProfile profile = getSubject().getProfile();
		notifyService.findByOwnId(page, profile.getId());
		// 标记已读
		notifyService.readed4Me(profile.getId());

		model.put("page", page);
		initUser(model);

		pushBadgesCount();

		return getView(Views.HOME_NOTIFIES);
	}
	
	@RequestMapping("/home/point")
	public String point( ModelMap model) {
		List<QueryRules> qrLst = new ArrayList<>();
		
		Paging paging = wrapPage(1,30);
		goodsService.paging(paging, qrLst);
		model.put("page", paging );
		
		
		Paging paging2 = wrapPage(1,1);
		UserProfile profile = getSubject().getProfile();
		
		QueryRules qr = new QueryRules("userId",profile.getId()	);	
		qrLst.add(qr);
		pointService.paging(paging2, qrLst);
		List<Point> lst = (List<Point>)paging2.getResults()	;
		
		Point point =new Point();
		if(lst.size()>0){
			point=lst.get(0);
		}
		model.put("point", point );
		
		List<PointRule> prLst = pointRuleService.getMyPointRule(profile.getId()	);
		model.put("prLst", prLst );
		
		initUser(model);
		return getView("/home/point");
	}
	
	
	@RequestMapping("/home/pointDetail")
	public String pointDetail(Integer pn, String yearmonthdatestart,String yearmonthdateend,  ModelMap model) throws ParseException {
		Paging paging = wrapPage(pn,10);
		
		UserProfile profile = getSubject().getProfile();
		List<QueryRules> qrLst = new ArrayList<>();
		
		
		if(profile!=null){
			QueryRules qr = new QueryRules("userId",profile.getId()	);	
			qrLst.add(qr);
		}
		
		if(StringUtils.isNotBlank(yearmonthdatestart)){
			QueryRules qr = new QueryRules("updateTime",DateUtils.parseDate(yearmonthdatestart+" 00:00:00", new String[]{"yyyy-MM-dd HH:mm:ss"}),"ge"	);qrLst.add(qr);
		}
		
		if(StringUtils.isNotBlank(yearmonthdateend)){
			QueryRules qr = new QueryRules("updateTime",DateUtils.parseDate(yearmonthdateend+" 23:59:59", new String[]{"yyyy-MM-dd HH:mm:ss"})	,"le");qrLst.add(qr);
		}
	
		pointDetailService.paging(paging, qrLst);


		model.put("yearmonthdatestart", yearmonthdatestart);
		model.put("yearmonthdateend", yearmonthdateend);
		
		
//		pointService.paging(paging, qrLst);
		model.put("page", paging);
		initUser(model);
		return getView("/home/pointDetail");
	}
	
	
	@RequestMapping("/home/goodsDetail")
	public String goodsDetail(Long id, ModelMap model) {
		Goods goods = goodsService.findById(id);
		model.put("goods", goods );
		
		List<QueryRules> qrLst = new ArrayList<>();
		QueryRules qr = new QueryRules("goodsId",id);	
		qrLst.add(qr);
		Paging page = wrapPage(1,100);
		goodsOtherService.paging(page, qrLst);
		model.put("page", page );
		initUser(model);
		return getView("/home/goodsDetail");
	}

	
	
	@RequestMapping("/home/goodsOther")
	public String goodsOther(Long goodsId,Integer buyNum,String remark, ModelMap model) {
		UserProfile profile = getSubject().getProfile();
		if(buyNum<1){
			return "redirect:/home/goodsDetail?id="+goodsId;
		}
		
		GoodsOther goodsOther = new GoodsOther();
		goodsOther.setBuyNum(buyNum);
		goodsOther.setGoodsId(goodsId);
		goodsOther.setRemark(remark);
		goodsOther.setUserId(profile.getId());
		goodsOther.setOpId(profile.getId());
		goodsOther.setCreateTime(new Date());
		goodsOtherService.buyGoods(goodsOther );
		
		return "redirect:/home/goodsOtherLst";
	}

	
	@RequestMapping("/home/goodsOtherLst")
	public String goodsOtherLst(Integer pn, String yearmonthdatestart,String yearmonthdateend,  ModelMap model) throws ParseException {
		Paging paging = wrapPage(pn,100);
		
		UserProfile profile = getSubject().getProfile();
		List<QueryRules> qrLst = new ArrayList<>();
		
		
		if(profile!=null){
			QueryRules qr = new QueryRules("userId",profile.getId()	);	
			qrLst.add(qr);
		}
		
		/*if(StringUtils.isNotBlank(yearmonthdatestart)){
			QueryRules qr = new QueryRules("updateTime",DateUtils.parseDate(yearmonthdatestart+" 00:00:00", new String[]{"yyyy-MM-dd HH:mm:ss"}),"ge"	);qrLst.add(qr);
		}
		
		if(StringUtils.isNotBlank(yearmonthdateend)){
			QueryRules qr = new QueryRules("updateTime",DateUtils.parseDate(yearmonthdateend+" 23:59:59", new String[]{"yyyy-MM-dd HH:mm:ss"})	,"le");qrLst.add(qr);
		}*/
	
		goodsOtherService.paging(paging, qrLst);


		model.put("yearmonthdatestart", yearmonthdatestart);
		model.put("yearmonthdateend", yearmonthdateend);
		
		model.put("page", paging);
		initUser(model);
		return getView("/home/goodsOtherLst");
	}

	@RequestMapping("/home/missionList")
	public String missionList(Integer pn,   ModelMap model) throws ParseException {
		Paging paging = wrapPage(pn,20);
		
		UserProfile profile = getSubject().getProfile();
		List<QueryRules> qrLst = new ArrayList<>();
		
		
		if(profile!=null){
			QueryRules qr = new QueryRules("userId",profile.getId()	);	
			qrLst.add(qr);
		}
		model.put("page", paging);
		initUser(model);
		return getView("/home/missionList");
	}
	
	@RequestMapping("/home/optimizeCard")
	public String optimizeCard(Integer pn, String yearmonthdatestart,String yearmonthdateend,  ModelMap model) throws ParseException {
		Paging paging = wrapPage(pn,20);
		
		UserProfile profile = getSubject().getProfile();
		List<QueryRules> qrLst = new ArrayList<>();
		
		
		if(profile!=null){
			QueryRules qr = new QueryRules("userId",profile.getId()	);	
			qrLst.add(qr);
		}
		model.put("page", paging);
		initUser(model);
		return getView("/home/optimizeCard");
	}
	
	
	private void initUser(ModelMap model) {
		UserProfile up = getSubject().getProfile();
		User user = userService.get(up.getId());

		model.put("user", user);
	}

	private void pushBadgesCount() {
		AccountProfile profile = (AccountProfile) session.getAttribute("profile");
		if (profile != null && profile.getBadgesCount() != null) {
			profile.getBadgesCount().setNotifies(0);
			session.setAttribute("profile", profile);
		}
	}
}
