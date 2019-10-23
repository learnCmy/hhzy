
package com.hhzy.crm.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>File：StringＨandleUtils.java</p>
 * <p>Title: 字符串处理工具类</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2015年9月19日 上午11:40:26</p>
 * <p>Company: cargogm.com</p>
 * @author 施建波
 * @version 1.0
 */
public abstract class StringHandleUtils
{
    /**
     * 替换内容
     * @param content           需要被替换的字符串
     * @param replaceKey        占位符
     * @param replaceValue      替换内容
     * @return
     * @author 施建波  2015年9月19日 上午11:48:20
     */
    public static String replaceContent(String content, String replaceKey, String replaceValue){
        return content.replaceAll(replaceKey, replaceValue);
    }
    

     
     /**
      * 连接字符串
      * @param prefix    字符串前缀
      * @param value     需要拼凑的字符串
      * @return
      * @author 施建波     2015年3月26日 下午1:58:39
      */
     public static String connectString(Object prefix, Object value, String joinStr){
         if(StringUtils.isBlank(joinStr)){
             joinStr = "_";
         }
         StringBuilder sb = new StringBuilder();
         sb.append(prefix);
         if(null != value){
             sb.append(joinStr).append(value);
         }
         return sb.toString();
     }
     
     public static String connectString(Object prefix, Object value){
         return connectString(prefix, value, null);
     }
     
     /**
      * 连接字符串
      * @param joinStr	连接符
      * @param key		字符串多个
      * @return
      * @author 施建波  2018年2月27日 下午4:59:03
      */
     public static String connectMulString(String joinStr, Object ... key){
    	 if(null == joinStr){
             joinStr = "_";
         }
    	 StringBuilder sb = new StringBuilder();
    	 int count = 0;
    	 for(Object item:key){
    		 if(null != item && StringUtils.isNotBlank(String.valueOf(item))){
	    		 if(count > 0)sb.append(joinStr);
	    		 sb.append(item);
	    		 count++;
    		 }
    	 }
    	 return sb.toString();
     }
     
     /**
      * 左填充
      * @param obj	需填充的字符串
      * @param num	位数
      * @return
      * @author 施建波  2015年10月9日 下午3:26:37
      */
     public static String leftFill(Object obj, Integer num){
    	 return leftFill(obj, num, null);
     }
     
     /**
      * 左填充
      * @param obj		需填充的字符串
      * @param num		位数
      * @param fillStr	填充的字符
      * @return
      * @author 施建波  2015年10月9日 下午3:29:16
      */
     public static String leftFill(Object obj, Integer num, String fillStr){
    	 String leftStr = "";
    	 if(StringUtils.isBlank(fillStr)){
    		 fillStr = "0";
    	 }
    	 if(null != obj){
    		 leftStr = obj.toString();
    		 leftStr = StringUtils.leftPad(leftStr, num, fillStr);
    	 }
    	 return leftStr;
     }

     
     public static String underline2Camel(String line){
    	 return underline2Camel(line, Boolean.TRUE);
     }
     
     /**
      * 下划线转驼峰法
      * @param line	字符串
      * @param smallCamel 大小驼峰,是否为小驼峰
      * @return
      * @author 施建波  2017年12月6日 上午9:10:26
      */
     public static String underline2Camel(String line,boolean smallCamel){
         if(line==null||"".equals(line)){
             return "";
         }
         StringBuffer sb=new StringBuffer();
         Pattern pattern=Pattern.compile("([A-Za-z\\d]+)(_)?");
         Matcher matcher=pattern.matcher(line);
         while(matcher.find()){
             String word=matcher.group();
             sb.append(smallCamel&&matcher.start()==0?Character.toLowerCase(word.charAt(0)):Character.toUpperCase(word.charAt(0)));
             int index=word.lastIndexOf('_');
             if(index>0){
                 sb.append(word.substring(1, index).toLowerCase());
             }else{
                 sb.append(word.substring(1).toLowerCase());
             }
         }
         return sb.toString();
     }

     /**
      * 驼峰法转下划线
      * @param line	源字符串
      * @return
      * @author 施建波  2017年12月6日 上午9:10:55
      */
     public static String camel2Underline(String line){
         if(line==null||"".equals(line)){
             return "";
         }
         line=String.valueOf(line.charAt(0)).toUpperCase().concat(line.substring(1));
         StringBuffer sb=new StringBuffer();
         Pattern pattern=Pattern.compile("[A-Z]([a-z\\d]+)?");
         Matcher matcher=pattern.matcher(line);
         while(matcher.find()){
             String word=matcher.group();
             sb.append(word.toLowerCase());
             sb.append(matcher.end()==line.length()?"":"_");
         }
         return sb.toString();
     }
     
     /**
      * 驼峰法转下划线
      * @param line	字符串
      * @return
      * @author 施建波  2017年12月6日 上午10:55:18
      */
     public static String camel2UnderMultipleline(String line){
    	 if(StringUtils.isNotBlank(line)){
	    	 Pattern pattern=Pattern.compile("[A-Z]");
	         Matcher matcher=pattern.matcher(line);
	         StringBuffer sb = new StringBuffer();
	         while(matcher.find()){
	             String word=matcher.group();
	             word = "_" + word.toLowerCase();
	             matcher.appendReplacement(sb, word);
	         }
	         matcher.appendTail(sb); 
	         line = sb.toString();
    	 }
    	 return line;
     }
}
