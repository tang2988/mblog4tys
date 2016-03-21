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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import mblog.base.lang.EnumPrivacy;
import mblog.core.data.AccountProfile;
import mblog.core.data.User;
import mblog.core.persist.service.CardTransactionRecordService;
import mblog.core.persist.service.CommentService;
import mblog.core.persist.service.FavorService;
import mblog.core.persist.service.FeedsService;
import mblog.core.persist.service.FollowService;
import mblog.core.persist.service.MyPOSService;
import mblog.core.persist.service.NotifyService;
import mblog.core.persist.service.PostService;
import mblog.core.persist.service.UserService;
import mblog.core.persist.utils.AmountUtils;
import mblog.core.persist.utils.CardTransactionRecordUtils;
import mblog.core.persist.utils.QueryRules;
import mblog.core.persist.utils.StringUtil;
import mblog.shiro.authc.AccountSubject;
import mblog.web.controller.BaseController;
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

		User curUser = userService.getByUsername(up.getUsername());//TYS 后期再UserProfile加入mobile字段
		String mobile = curUser.getMobile();
		if(StringUtils.isBlank(mobile)){
			mobile = "13812344321";
		}

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
		cardTransactionRecordService.paging(page, qrLst);

		model.put("page", page);
		model.put("user", curUser);

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

		User curUser = userService.getByUsername(up.getUsername());//TYS 后期再UserProfile加入mobile字段
		model.put("user", curUser);
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
		
		
		
		Boolean isVip = myPOSService.checkVip(sysSource, yearmonthdatestart, yearmonthdatestart, mobile, terminalcode,transAcount);
		if(isVip){
			curUser.setMobile(mobile);
			//
			userService.updateMobile(curUser.getId(), mobile);
			
			//是我们的POS客户，同步他的POS交易记录
    		new Thread(new Runnable() {
				@Override
				public void run() {
					cardTransactionRecordService.syncDataFromSysSourceByMobile(sysSource, CardTransactionRecordUtils.initStartDateStr, DateFormatUtils.format(new Date(), "yyyyMMdd"), mobile);					
				}
			}).start();
    		
			return Data.success("恭喜您通过了VIP验证，数据正在同步中，请稍后查看交易记录");	
		}
		return Data.failure("抱歉，您未通过了VIP验证，请填写正确的刷卡记录再次提交验证");
		
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
