/*
+--------------------------------------------------------------------------
|   Mblog [#RELEASE_VERSION#]
|   ========================================
|   Copyright (c) 2014, 2015 mtons. All Rights Reserved
|   http://www.mtons.com
|
+---------------------------------------------------------------------------
*/
package mblog.web.controller.desk.group;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import mblog.core.biz.PostBiz;
import mblog.core.data.AccountProfile;
import mblog.core.data.Group;
import mblog.core.data.Post;
import mblog.core.persist.service.GroupService;
import mblog.core.persist.service.PostService;
import mblog.web.controller.BaseController;
import mblog.web.controller.desk.Views;

/**
 * 文章浏览
 * 
 * @author langhsu
 * 
 */
@Controller
@RequestMapping("/view")
public class GroupVidewController extends BaseController {
	@Autowired
	private PostBiz postBiz;
	@Autowired
	private PostService postService;
	@Autowired
	private GroupService groupService;

	@RequestMapping("/{id}")
	public String view(@PathVariable Long id, ModelMap model) {
		Post ret = postBiz.getPost(id);
		
		Assert.notNull(ret, "该文章已被删除");
		
		if(ret.getPrivacy()==1){
			//未登陆
			AccountProfile getProfile = getSubject().getProfile();
			if(getProfile==null){
				Assert.notNull(null, "该文章是私密状态");
			}
			//不是作者
			if( getProfile.getId()!= ret.getAuthorId()){
				//不是管理员
				if( getProfile.getUsername().equals("xbadmin")|| getProfile.getUsername().equals("xinbei")){
				}else{
					Assert.notNull(null, "该文章是私密状态");
				}
			}
			
		}
		
		Group group = groupService.getById(ret.getGroup());

		postService.identityViews(id);
		model.put("ret", ret);
		return routeView(Views.ROUTE_POST_VIEW, group.getTemplate());
	}
}
