package com.base.basemodule.utils.formValidation.common;
/**
 * 公用的正则表达式
 * @author Administrator
 *
 */
public class Regexs {
	//匹配只能输入数字
	public static final String NUMBER = "^[0-9]+$";
	//匹配只能输入字母 不区分大小写
	public static final String LETTER_NO_CASE = "^[a-zA-Z]+$";
	//匹配只能输入字母只能输入大写
	public static final String LETTER_UPPER_CASE = "^[A-Z]+$";
	//匹配只能输入字母只能输入小写
	public static final String LETTER_LOWER_CASE = "^[a-z]+$";
	//匹配只能输入邮箱
	public static final String MAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,4}$";
	//匹配只能输入手机号
	public static final String PHONE = "^1\\d{10}$";
	//匹配只能输入密码
	public static final String PASSWORD = "^\\w{6,20}$";
	//匹配只能输入中文
	public static final String CHINESE = "^[\\u4e00-\\u9fa5]+$";
	//匹配只能输入身份证
	public static final String IDCARD = "^(\\d{6})(19|20)(\\d{2})(1[0-2]|0[1-9])(0[1-9]|[1-2][0-9]|3[0-1])(\\d{3})(\\d|X|x)?$";
}
