package com.base.basemodule.utils.formValidation.validation;

import android.util.Log;

import com.base.basemodule.utils.formValidation.annotation.IsDate;
import com.base.basemodule.utils.formValidation.annotation.IsDateTime;
import com.base.basemodule.utils.formValidation.annotation.IsPass;
import com.base.basemodule.utils.formValidation.annotation.NoEmpty;
import com.base.basemodule.utils.formValidation.annotation.NumberEnumCheck;
import com.base.basemodule.utils.formValidation.annotation.NumberSize;
import com.base.basemodule.utils.formValidation.annotation.RegexCheck;
import com.base.basemodule.utils.formValidation.annotation.TextEnumCheck;
import com.base.basemodule.utils.formValidation.annotation.TextSize;
import com.base.basemodule.utils.formValidation.common.ValidMsg;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


//https://github.com/fanhua1994/DzValidation

/**
 * 表单验证
 * 1.验证字符串是否为空
 * 2.验证数字范围
 * 3.验证码字符串长度
 * 4.验证手机号
 * 5.验证密码
 * 6.验证邮箱
 * 7.包含某几个值
 *
 * @author Fanhua
 * <p>
 * public class User222 {
 * @NumberSize(message="年龄不能小于{minvalue}不能大于{maxvalue}",minvalue=10,maxvalue=100) private Integer age;
 * @TextCheck(message="请选择正确的性别",value= {"男","女"})
 * private String sex;
 * @RegexCheck(value=Regexs.CHINESE,message="请输入中文名称")
 * @TextSize(message="姓名请保证在{minlength}到{maxlength}之间",minlength=2,maxlength=4) private String name;
 * @NoEmpty(message="地址不能为空哦") private String address;
 * @RegexCheck(value=Regexs.MAIL,message="请输入正确的邮箱") private String mail;
 * @RegexCheck(value=Regexs.IDCARD,message="请输入正确的身份证") private String idcard;
 * <p>
 * //@IsPass
 * @NoEmpty(message="出生日期不能为空")
 * @IsDateTime(message="请输入正确的出生日期") private String birthday;
 * <p>
 * //此处忽略getter setter
 * }
 */
public class Validation {
    private static ValidMsg v = new ValidMsg();

    /**
     * 自动校验全部
     *
     * @param obj
     * @return
     */
    public static ValidMsg AutoVerifiy(Object obj) {
        return AutoVerifiy(obj, null, false);
    }

    /**
     * 只校验白名单内
     *
     * @param obj
     * @param whiteList
     * @return
     */
    public static ValidMsg AutoVerifiyWhiteList(Object obj, List<String> whiteList) {
        return AutoVerifiy(obj, whiteList, true);
    }

    /**
     * 除了黑名单的其他全部校验
     *
     * @param obj
     * @param blackList
     * @return
     */
    public static ValidMsg AutoVerifiyBlackList(Object obj, List<String> blackList) {
        return AutoVerifiy(obj, blackList, false);
    }

    /**
     * 自动验证部分字段
     * 前提是所有字段必须加上注解
     *
     * @param obj       实体名称
     * @param fields    需要筛选的实体字段
     * @param operation true包含该list字段 false为不包含list字段
     * @return
     */
    private static ValidMsg AutoVerifiy(Object obj, List<String> filterList, boolean operation) {
        try {
            Field[] field = obj.getClass().getDeclaredFields();
            v.setPass(true);

            TextEnumCheck str_check = null;
            NumberEnumCheck int_check = null;
            NoEmpty no_empty = null;
            TextSize str_size = null;
            NumberSize int_size = null;
            RegexCheck regex = null;
            IsDate isDate = null;
            IsDateTime isDatetime = null;
            IsPass isPass = null;

            String name = null, val = null, fieldName = null;
            int valInt = 0;
            Method m = null;

            for (int i = 0; i < field.length; i++) {
                fieldName = field[i].getName();

                Log.e("lzs", "fieldName:" + fieldName);
                if (filterList != null) {
                    if (operation) {
                        if (!filterList.contains(fieldName)) {
                            //System.out.println(fieldName+"该字段已被系统忽略校验");
                            continue;
                        }
                    } else {
                        if (filterList.contains(fieldName)) {
                            //System.out.println(fieldName+"该字段已被系统忽略校验");
                            continue;
                        }
                    }
                }

                str_check = field[i].getAnnotation(TextEnumCheck.class);
                int_check = field[i].getAnnotation(NumberEnumCheck.class);
                no_empty = field[i].getAnnotation(NoEmpty.class);
                str_size = field[i].getAnnotation(TextSize.class);
                int_size = field[i].getAnnotation(NumberSize.class);
                regex = field[i].getAnnotation(RegexCheck.class);
                isDate = field[i].getAnnotation(IsDate.class);
                isDatetime = field[i].getAnnotation(IsDateTime.class);
                isPass = field[i].getAnnotation(IsPass.class);
                if (isPass != null) {
                    continue;
                }
                if (str_check == null && int_check == null && no_empty == null && str_size == null && int_size == null && regex == null && isDate == null && isDatetime == null) {
                    continue;
                }
                name = field[i].getName();
                name = name.substring(0, 1).toUpperCase() + name.substring(1); // 将属性的首字符大写，方便构造get，set方法

                m = obj.getClass().getDeclaredMethod("get" + name);
                m.setAccessible(true);

                if (str_check != null) {
                    val = (String) m.invoke(obj);
                    v = StringCheck(val, str_check.message(), str_check.value());
                    if (!v.isPass()) {
                        break;
                    }
                }

                if (int_check != null) {
                    valInt = (Integer) m.invoke(obj);
                    v = IntCheck(valInt, int_check.message(), int_check.value());
                    if (!v.isPass()) {
                        break;
                    }
                }

                if (no_empty != null) {
                    val = (String) m.invoke(obj);
                    v = StringEmpty(val, no_empty.message());
                    if (!v.isPass()) {
                        break;
                    }
                }

                if (str_size != null) {
                    val = (String) m.invoke(obj);
                    v = StringSize(val, str_size.message(), str_size.minlength(), str_size.maxlength());
                    if (!v.isPass()) {
                        break;
                    }
                }

                if (int_size != null) {
                    valInt = (Integer) m.invoke(obj);
                    v = IntSize(valInt, int_size.message(), int_size.minvalue(), int_size.maxvalue());
                    if (!v.isPass()) {
                        break;
                    }
                }

                if (regex != null) {
                    val = (String) m.invoke(obj);
                    v = StringRegex(val, regex.message(), regex.value());
                    if (!v.isPass()) {
                        break;
                    }
                }

                if (isDate != null) {
                    val = (String) m.invoke(obj);
                    v = isDate(val, isDate.message());
                    if (!v.isPass()) {
                        break;
                    }
                }

                if (isDatetime != null) {
                    val = (String) m.invoke(obj);
                    v = isDateTime(val, isDatetime.message());
                    if (!v.isPass()) {
                        break;
                    }
                }
            }

            return v;
        } catch (Exception e) {
            Log.e("lzs", "getSimpleName:" + e.getClass().getSimpleName());
            v.setPass(false);
            v.setMsg("出现未知错误，可能是使用了错误的注解，错误信息：" + e.getMessage());
        }//初始化这个对象
        return v;
    }

    //验证字符串是否为空
    public static ValidMsg StringEmpty(String str, String message) {
        if (str == null || str.isEmpty()) {
            v.setMsg(message);
            v.setPass(false);
        } else {
            v.setMsg("");
            v.setPass(true);
        }
        return v;
    }

    //验证字符串是否为空并且验证长度
    public static ValidMsg StringSize(String str, String message, int minlength, int maxlength) {
        message = message.replace("{minlength}", String.valueOf(minlength));
        message = message.replace("{maxlength}", String.valueOf(maxlength));

        if (str == null || str.isEmpty()) {
            v.setMsg(message);
            v.setPass(false);
            return v;
        }

        if (str.length() < minlength || str.length() > maxlength) {
            v.setMsg(message);
            v.setPass(false);
            return v;
        }

        v.setPass(true);
        return v;
    }

    public static ValidMsg StringCheck(String str, String message, Object... obj) {
        if (obj.length == 0) {
            v.setPass(true);
            return v;
        } else {
            String check_item = null;
            boolean isOk = false;
            for (int i = 0; i < obj.length; i++) {
                check_item = obj[i].toString();
                if (str.equals(check_item)) {
                    isOk = true;
                    break;
                }
            }

            if (isOk) {
                v.setMsg("");
                v.setPass(true);
            } else {
                v.setPass(false);
                v.setMsg(message);
            }

            return v;
        }
    }

    public static ValidMsg IntCheck(int str, String message, int[] vals) {
        boolean isok = false;
        for (int v : vals) {
            if (v == str) {
                isok = true;
                break;
            }
        }
        if (!isok)
            v.setMsg(message);

        v.setPass(isok);
        return v;
    }

    public static ValidMsg StringCheck(String str, String message, String[] obj) {
        if (obj.length == 0) {
            v.setPass(true);
            return v;
        } else {
            String check_item = null;
            boolean isOk = false;
            for (int i = 0; i < obj.length; i++) {
                check_item = obj[i];
                if (str.equals(check_item)) {
                    isOk = true;
                    break;
                }
            }

            if (isOk) {
                v.setPass(true);
            } else {
                v.setPass(false);
                v.setMsg(message);
            }

            return v;
        }
    }

    //正则表达式验证
    public static ValidMsg StringRegex(String value, String message, String regex) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(value);
        if (m.matches()) {
            v.setPass(true);
            v.setMsg("");
        } else {
            v.setPass(false);
            v.setMsg(message);
        }

        return v;
    }

    //验证数字范围
    public static ValidMsg IntSize(int value, String message, int minvalue, int maxvalue) {
        message = message.replace("{minvalue}", String.valueOf(minvalue));
        message = message.replace("{maxvalue}", String.valueOf(maxvalue));

        if (value < minvalue || value > maxvalue) {
            v.setMsg(message);
            v.setPass(false);
        } else {
            v.setPass(true);
        }
        return v;
    }

    private static boolean checkDate(String value, String pattern) {
        boolean convertSuccess = true;
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        try {
            format.setLenient(false);
            format.parse(value);
        } catch (ParseException e) {
            convertSuccess = false;
        }

        return convertSuccess;
    }


    //验证日期 		yyyy-MM-dd
    public static ValidMsg isDate(String date_str, String message) {
        if (date_str == null || date_str.indexOf(" ") > 0 || date_str.indexOf(":") > 0 || date_str.indexOf("-") == -1) {
            v.setPass(false);
            v.setMsg(message);
            return v;
        }

        v.setPass(checkDate(date_str, "yyyy-MM-dd"));
        v.setMsg(message);
        return v;
    }

    //验证日期+时间
    public static ValidMsg isDateTime(String datetime_str, String message) {
        if (datetime_str == null || datetime_str.indexOf(" ") == -1 || datetime_str.indexOf(":") == -1 || datetime_str.indexOf("-") == -1) {
            v.setPass(false);
            v.setMsg(message);
            return v;
        }

        v.setPass(checkDate(datetime_str, "yyyy-MM-dd HH:mm"));
        v.setMsg(message);
        return v;
    }
}
