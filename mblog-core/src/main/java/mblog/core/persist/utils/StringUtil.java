package mblog.core.persist.utils;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StringUtil extends StringUtils {

	public static final  Logger log = LoggerFactory.getLogger(StringUtil.class);


	// 将字符串转移为ASCII码
	public static String getCnASCII(String cnStr) {
		StringBuffer strBuf = new StringBuffer();
		byte[] bGBK = cnStr.getBytes();
		for (int i = 0; i < bGBK.length; i++) {
			strBuf.append(Integer.toHexString(bGBK[i] & 0xff));
		}
		return strBuf.toString();
	}

	/**
	 * 银行卡号码、身份证号码、手机号码、邮箱地址等信息处理
	 * 
	 * @param cardNo
	 *            :目标串
	 * @param prefix
	 *            ：前面保留的位数
	 * @param suffix
	 *            ：后面保留的位数
	 * @return 返回加入*后的串
	 */
	public static String getSecurityNum(String cardNo, int prefix, int suffix) {
		StringBuffer cardNoBuffer = new StringBuffer();
		int len = prefix + suffix;
		if (StringUtils.isNotBlank(cardNo) && cardNo.length() > len) {
			cardNoBuffer.append(cardNo.substring(0, prefix));
			for (int i = 0; i < cardNo.length() - len; i++) {
				cardNoBuffer.append("*");
			}
			cardNoBuffer.append(cardNo.substring(cardNo.length() - suffix, cardNo.length()));
		}
		return cardNoBuffer.toString();
	}

	/**
	 * 验证用户名
	 * 
	 * @param phone
	 * @return
	 */
	public static boolean isUsername(String username) {
		try {
			Pattern p = Pattern.compile("^\\w{6,16}$");
			Matcher m = p.matcher(username);
			return m.matches();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 验证电话号码和手机号
	 * 
	 * @param phone
	 * @return
	 */
	public static boolean isTelPhone(String phone) {
		Pattern p = Pattern
				.compile("^([1][3][0-9]{9})|([1][4][0-9]{9})|([1][5][0-9]{9})|([1][7][0-9]{9})|([1][8][0-9]{9})|(0[1][3][0-9]{9})|([0-9]{3,4}\\-[0-9]{7,8})|([0-9]{7,8})|(\\([0-9]{3,4}\\)[0-9]{3,8})|(0{0,1}13[0-9]{9})$");
		Matcher m = p.matcher(phone);
		return m.matches();
	}

	/**
	 * 验证手机号
	 * 
	 * @param phone
	 * @return
	 */
	public static boolean isMobilePhone(String phone) {
		Pattern p = Pattern.compile("^([1][3][0-9]{9})|([1][4][0-9]{9})|([1][5][0-9]{9})|([1][7][0-9]{9})|([1][8][0-9]{9})$");
		Matcher m = p.matcher(phone);
		return m.matches();
	}
	
	

	/**
	 * isRYXSerialNo
	 * 
	 * @param phone
	 * @return
	 */
	public static boolean isRYXSerialNo(String phone) {
		Pattern p = Pattern.compile("^([0-9]{15})$");
		Matcher m = p.matcher(phone);
		return m.matches();
	}


	/**
	 * 验证身份证号码
	 * 
	 * @param idCardNo
	 * @return
	 */
	public static boolean isIdCardNo(String idCardNo) {
		Pattern p = Pattern.compile("^[1-9]\\d{13,16}[a-zA-Z0-9]{1}$");
		Matcher m = p.matcher(idCardNo);
		return m.matches();
	}

	/**
	 * 验证邮箱
	 * 
	 * @param idCardNo
	 * @return
	 */
	public static boolean isEmail(String email) {
		Pattern p = Pattern.compile("^([a-zA-Z0-9]+[_|\\_|\\.|-]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\\_|\\.]?)*[a-zA-Z0-9]+\\.[a-zA-Z]{2,3}$");
		Matcher m = p.matcher(email);
		return m.matches();
	}

	/**
	 * 判断是否是由同一字符构成的，true代表由同一字符构成的 false反之
	 */
	public static boolean isSameSymbol(String s) {
		boolean flag = false;
		// 当s为空字符串或者null,认为不是由同一字符构成的
		if (StringUtils.isEmpty(s)) {
			return flag;
		}
		// 当只有一个字符的时候，认为由同一字符构成
		if (1 == s.length()) {
			flag = true;
			return flag;
		}
		char chacter = s.charAt(0);
		for (int i = 1; i <= s.length() - 1; i++) {
			if (chacter != s.charAt(i)) {
				flag = false;
				return flag;
			}
		}
		flag = true;
		return flag;
	}

	/**
	 * 判断是否有顺序 true代表有顺序 false反之
	 */
	public static boolean isOrdered(String s) {
		boolean flag = false;
		// 当s为空字符串或者null,认为不是由同一字符构成的
		if (StringUtils.isEmpty(s)) {
			return flag;
		}
		// 当只有一个字符的时候，认为由同一字符构成
		if (1 == s.length()) {
			flag = true;
			return flag;
		}
		List<Integer> temp1 = new ArrayList<Integer>();
		List<Integer> temp2 = new ArrayList<Integer>();
		for (int i = 0; i < s.length(); i++) {
			temp1.add(Integer.valueOf(s.substring(i, i + 1)));
		}
		for (int i = 0; i < s.length(); i++) {
			temp2.add(Integer.valueOf(s.substring(i, i + 1)));
		}
		Collections.sort(temp1);
		StringBuffer orderedAsc = new StringBuffer();
		for (Integer i : temp1) {
			orderedAsc.append(i);
		}
		Collections.sort(temp2);
		Collections.reverse(temp2);
		StringBuffer orderedDec = new StringBuffer();
		for (Integer i : temp2) {
			orderedDec.append(i);
		}
		if (s.equals(orderedDec.substring(0)) || s.equals(orderedAsc.substring(0))) {
			flag = true;
			return flag;
		}
		return flag;
	}

	/**
	 * html字符转义
	 * 
	 * @param idCardNo
	 * @return
	 */
	public static String htmlLabelEscape(String htmlStr) {
		if (StringUtils.isEmpty(htmlStr)) {
			return null;
		}
		htmlStr = htmlStr.replace(">", "&gt;");
		htmlStr = htmlStr.replace("<", "&lt;");
		htmlStr = htmlStr.replace("&&", "&amp;");
		htmlStr = htmlStr.replace("\"", "&quot;");
		htmlStr = htmlStr.replace("\'", "&acute;");
		return htmlStr;
	}

	/**
	 * 描述：给用户账号简单加密 前四位+***
	 * 
	 * @author jerry.deng
	 * @date 2014-9-25下午5:54:30
	 * @param username
	 * @return
	 */
	public static String enUsername(String username) {
		return username.substring(0,3) + "***";
	}
	
	/***
	 * 登录用户名规则：[a-zA-z][0-9][_] 必须包含一个字母以上,长度6-16
	 * 描述：TYS desc
	 * @author 
	 * @date 2014-12-8下午3:41:15
	 * @param cs
	 * @return 符合规则==''
	 */
	public static String checkUserNameFmt(String cs) {
		if (StringUtils.isBlank(cs) || (!isUsername(cs))) {
			//用户名 为空，空串，包含空白，长度
			return "用户名由6-16个字母、数字、下划线组成，至少包含一个字母以上";
		}
		
		if (cs.contains("tianchao") || cs.contains("heshidai")) {
			return "该账号已被注册，请更换用户名";
		}
		
		if (StringUtil.isSameSymbol(cs)) {
			return "账号不能全部为同一字符";
		}
		
		final int sz = cs.length();
		int letterCnt = 0;
		for (int i = 0; i < sz; i++) {
			if ("_".equals(String.valueOf(cs.charAt(i)))) {//多个下划线
			}else{
				if ((CharUtils.isAsciiAlphanumeric(cs.charAt(i)) == false)) {//非单词
					return "用户名由6-16个字母、数字、下划线组成，至少包含一个字母以上";
				}
				if(CharUtils.isAsciiAlpha(cs.charAt(i)))letterCnt++;
			}
		}
		if(letterCnt<1)return "用户名由6-16个字母、数字、下划线组成，至少包含一个字母以上";
		return "" ;
	}
	
	
	/***
	 * 登录密码判断
	 * 描述：TYS desc
	 * @author jerry.deng 
	 * @date 2014-12-10上午11:31:14
	 * @param psw
	 * @param psw2
	 * @return
	 */
	public static String checkUserPswFmt(String psw,String psw2) {
		String msg = checkPswFmt(psw);
		if(StringUtils.isNotBlank(msg))return msg;
		
		if (StringUtils.isBlank(psw2))return "请再次输入密码";
		
		//判等
		psw=psw.trim();
		psw2=psw2.trim();
		if (!psw.equals(psw2))return "两次密码输入不一致";

		return "" ;
	}
	
	/**
	 * 特殊字符过滤替换(逗号可以输入)
	 * 
	 * @param idCardNo
	 * @return
	 */
	public static String specialCharReplace(String htmlStr) {
		if (StringUtils.isEmpty(htmlStr)) {
			return null;
		}
		String regEx = "[`~!@#$%^&*()+=|{}':;'\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。、？]";
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(htmlStr);
		StringBuffer sb = new StringBuffer();
		boolean result1 = matcher.find();
		while (result1) {
			matcher.appendReplacement(sb, "");
			result1 = matcher.find();
		}
		matcher.appendTail(sb);
		return sb.toString();
	}
	
	public static String checkPswFmt(String psw){
		//用户名 为空，空串
		if (StringUtils.isBlank(psw))return "请输入密码";
		
		//长度
		psw=psw.trim();
		if(psw.length()<6 || psw.length()>20)return "密码由6-20个字符组成，区分大小写";

		return "" ;
	}
	
	/**
	 * 
	 * 描述：判断字符串是否为指定位数的数字串
	 * @author Patrick.Lian
	 * @date 2015-4-15上午11:38:21
	 * @param str 待验证字符串
	 * @param capacity 位数
	 * @return
	 */
	public static boolean isNumber(String str, int capacity){
		if(StringUtils.isEmpty(str)){
			return false;
		}
		String regEx = "[0-9]{" + capacity + "}";
		Pattern pattern = Pattern.compile(regEx);
		Matcher m = pattern.matcher(str);
		return m.matches();
	}
	
	
	/***
	 * 
	 * 描述：超一定的长度 缩短字符串
	 * @author GARY.TANG
	 * @date 2015-7-10下午6:11:17
	 * @param str 字符串
	 * @param len 长度限制
	 * @param fillStr 填充字符串
	 * @return
	 */
	public static String truncate(String str,Integer len,String fillStr) {
		if (StringUtil.isBlank(str) ) {
			return "";
		}
		if(str.length() > len ){
			str = str.substring(0, len) + fillStr;
		}
		
		return (str);
	}
	
    
    
    /**
     * 
     * 描述：鹏元转码
     * @author xiaohuan.zhang
     * @date 2015-12-29上午9:51:57
     * @param name
     * @return
     */
    public static String replaceName(String name){
    	StringBuffer sb = new StringBuffer();
		String ff = "．.１２３４５６７８９０ＡＢＣＤＥＦＧＨＩＪＫＬＭＮＯＰＱＲＳＴＵＶＷＸＹＺａｂｃｄｅｆｇｈｉｊｋｌｍｎｏｐｑｒｓｔｕｖｗｘｙｚ";
		String dh = "··1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		for (int i = 0; i < name.length(); i++) {
			int j = ff.indexOf(name.charAt(i));
			if (j >= 0) {
				sb.append(dh.charAt(j));
			} else {
				sb.append(name.charAt(i));
			}
		}
		return sb.toString();
    }
    
    /**
     * 
     * 描述：实名认证检测用户名是否合法(允许中文英文圆角以及.)
     * @author xiaohuan.zhang
     * @date 2015-12-29下午1:34:07
     * @param str
     * @return
     */
	public static boolean isRealName(String str) {
		 return str == null  ? false : str.matches("[a-zA-ZＡ-Ｚａ-ｚ\\.\\．\\·\u4e00-\u9fa5]*");
		 											
	}
    
}
