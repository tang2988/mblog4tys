/*
+--------------------------------------------------------------------------
|   Mblog [#RELEASE_VERSION#]
|   ========================================
|   Copyright (c) 2014, 2015 mtons. All Rights Reserved
|   http://www.mtons.com
|
+---------------------------------------------------------------------------
*/
package mblog.core.persist.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

import mblog.base.context.AppContext;
import mblog.core.data.CardTransactionRecord;
import mblog.core.persist.service.CardTransactionRecordService;

/**
 * @author langhsu
 *
 */
public class RSUtils extends CardTransactionRecordUtils{
	
	private static final Logger log = LoggerFactory.getLogger(CardTransactionRecordService.class);
	
	static String RYX_SYS_URL =AppContext.getAppConfigByName("RS_SYS_URL");
	static String RYX_SYS_UNAME = AppContext.getAppConfigByName("RS_SYS_UNAME");
	static String RYX_SYS_UPSW = AppContext.getAppConfigByName("RS_SYS_UPSW");
	static String BROWER_USER_AGENT =AppContext.getAppConfigByName("BROWER_USER_AGENT");
	
	static String loginRYXSystemJsession = "";
	
	public static RSUtils instance ;
	
	public static RSUtils getInstance (){
		instance = null;
			log.info("瑞刷账户信息：RYX_SYS_URL={};RYX_SYS_UNAME={};RYX_SYS_UPSW={}",RYX_SYS_URL,RYX_SYS_UNAME,RYX_SYS_UPSW);
			instance = new RSUtils();
		return instance;
	}
	
	/**
	 * 获取DOCUMENT对象
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public List<CardTransactionRecord> getCardTransactionRecordLst( Map<String,String> paraMap)   {
		List<CardTransactionRecord> ctrLst = new ArrayList<>();
	
		
		List<String> daysLst = parseDateInternal(paraMap);
		//一天天查询
		for(String d : daysLst){
			paraMap.put("yearmonthdatestart",  d);
			paraMap.put("yearmonthdateend",  d);
			ctrLst.addAll(getCardTransactionRecordLstReal(paraMap));
		}
		replaceSysSource(ctrLst);
		
		return ctrLst;
	}
	
	
	public List<CardTransactionRecord> getCardTransactionRecordLstReal( Map<String,String> paraMap)   {
		
		try {
			loginRYXSystem();
			
			paraMap.put("rows", "100");
			paraMap.put("page", "1");
			paraMap.put("customerName", "");//用户姓名
			
			paraMap.put("queryFlag", "1");//所属机构类型
			paraMap.put("merchantCode", "");
//			paraMap.put("terminalcode", "");//刷卡机编号
			paraMap.put("dealType", "-1");//交易类型
			paraMap.put("agencySelect", "");//代理ID
			paraMap.put("serialNumber", "");//流水编号
//			paraMap.put("yearmonthdatestart", "20160301");
//			paraMap.put("yearmonthdateend", "20160301");
//			paraMap.put("moblieNo", "");//手机
			
			String url = RYX_SYS_URL+ "deal/deal.do?method=getDealdetail";
			

			String paraUrl = "";
			for(Map.Entry<String, String> e: paraMap.entrySet() ){
				paraUrl += "&" +e.getKey()+"="+e.getValue();
			}
			if(StringUtils.isNotBlank(paraUrl)){
				url +=paraUrl;
			}
			log.info("瑞刷查询：{}",url);
			Document resp = Jsoup.connect(url). userAgent(BROWER_USER_AGENT ).cookie("JSESSIONID",loginRYXSystemJsession).ignoreContentType(true) .post() ;

			String jsonStr = resp.toString().replaceAll("<html>", "").replaceAll("</html>", "")
					.replaceAll("<head>", "").replaceAll("</head>", "")
					.replaceAll("<body>", "").replaceAll("</body>", "");
			
			log.info("瑞刷查询返回：{}" , jsonStr);
			JSONObject  holdJson = JSONObject.parseObject(jsonStr);
			
			List<CardTransactionRecord> ctrLst = JSONObject.parseArray(holdJson.getString("rows"), CardTransactionRecord.class);
			
			return ctrLst;
		} catch (Exception e) {
			log.error("登陆瑞刷系统查询交易记录失败", e);
		}
		return new ArrayList<>();
	}

	
	private synchronized String loginRYXSystem(){
		/*$.post("/qtfr/users/users.do?method=checkLogin&loginname=111118879&password=200820e3227815ed1756a6b531e7e0d2", function(data) {
			console.info(data);
		} );*/
		
		
		try {

			if(!isNeedLogin()){
				return "已经登陆了";
			}
			
			//先请求主页，获取当前COOKIE
			String url =RYX_SYS_URL+ "main.jsp";
			Response resp = Jsoup.connect(url ). userAgent(BROWER_USER_AGENT ).execute();
			Map<String, String> cookieMap = resp.cookies();
			
			//LOGIN
			url =RYX_SYS_URL+"users/users.do?method=checkLogin&loginname="+RYX_SYS_UNAME+"&password="+RYX_SYS_UPSW;
			Document re = Jsoup.connect(url). userAgent(BROWER_USER_AGENT ).cookies(cookieMap).ignoreContentType(true) .post() ;
			log.info(re.toString());
			
			String loginSucc ="\"success\":\"true\"";
			if(re.toString().contains(loginSucc)){
				log.info("登陆成功");
				return loginRYXSystemJsession= cookieMap.get("JSESSIONID");
			}
		} catch (IOException e) {
			log.error("登陆瑞刷系统失败", e);
		}
		return "";
	}
	
	private Boolean isNeedLogin(){
		try {
			String url =RYX_SYS_URL+ "main.jsp";
			
			Response resp = Jsoup.connect(url ).cookie("JSESSIONID",loginRYXSystemJsession). userAgent(BROWER_USER_AGENT ).execute();
			String respUrl = resp.url().toString();
			if(respUrl.contains("login.jsp")){
				return true;
			}
		} catch (IOException e) {
			log.error("登陆瑞刷系统失败", e);
			return true;
		}
		
		return false;
	}


	/**
	 * 根据前后时间获取区间所有日期
	 * @param yearmonthdatestart
	 * @param yearmonthdateend
	 * @return
	 */
	public List<String> parseDateInternal( Map<String,String> paraMap) {
		String yearmonthdatestart = paraMap.get("yearmonthdatestart");
		String yearmonthdateend = paraMap.get("yearmonthdateend");
		
		Integer initStartDate = 20160301;
		Integer initEndDate = Integer.valueOf(DateFormatUtils.format(new Date(), "yyyyMMdd"));
		
		List<String> daysLst = new ArrayList<>(); 
		try {
			Integer yearmonthdatestartI = Integer.valueOf(yearmonthdatestart);
			Integer yearmonthdateendI = Integer.valueOf(yearmonthdateend);
			
			if(initStartDate-yearmonthdatestartI>0){
				yearmonthdatestart = initStartDate+"";
			}
			
			if(initEndDate-yearmonthdateendI<0){
				yearmonthdateend = initEndDate+"";
			}
			
			
			Date yearmonthdatestartD = DateUtils.parseDate(yearmonthdatestart, new String[]{"yyyyMMdd"});
			Date yearmonthdateendD = DateUtils.parseDate(yearmonthdateend, new String[]{"yyyyMMdd"});
			
			Date tmpDate= new Date(yearmonthdatestartD.getTime());
			while(true){
				String s = DateFormatUtils.format(tmpDate, "yyyyMMdd");
//				System.out.println(s);
				daysLst.add(s);
				tmpDate=DateUtils.addDays(tmpDate, 1);
				if(tmpDate.compareTo(yearmonthdateendD)>0){
					break;
				}
			}
		} catch (Exception e) {
			log.error("获取日期失败{}",paraMap, e);
		}
		return daysLst;
	}
	
	
	@Test
	public void aa()  {
		
		/*HashMap param = new HashMap<>();
		param.put("yearmonthdatestart", "20160301");
		param.put("yearmonthdateend", "20160304");
		param.put("moblieNo", "");//手机
		List<CardTransactionRecord> lst=getCardTransactionRecordLst( param);
		
		System.out.println(lst.size());
		for(CardTransactionRecord ctr :lst){
			System.out.println(ctr);
		}*/
	}
	
	
	private void replaceSysSource(List<CardTransactionRecord> lst) {
		for(CardTransactionRecord ctr: lst){
			ctr.setSysSource("瑞刷");
		}
	}

	
	

}
