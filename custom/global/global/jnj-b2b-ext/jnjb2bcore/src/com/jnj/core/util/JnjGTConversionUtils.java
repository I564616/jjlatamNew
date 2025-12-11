package com.jnj.core.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
/**
 * @author  
 *
 */
public class JnjGTConversionUtils extends  StringUtils{

	
	/**
	 * @param in
	 * @return boolean
	 */
	public static boolean emptyInput(String in) {
       if (in == null || in.length() == 0 || in.trim().length() == 0)
           return true;
       return false;
   }
	
	/*
    ** No Nulls
    */
    /**
    * @param inStr
    * @return String
    */
   public static String noNull(String inStr) {
        return (inStr == null)?"":inStr.trim();
    }

    /**
    * @param inStr
    * @return String
    */
   public static String nullBlankToZero(String inStr) {
        return (inStr == null || inStr.length() ==0)?"0":inStr.trim();
    }

    /**
    * @param inStr
    * @return String
    */
   public static String blankToNull(String inStr) {
        return (inStr != null && inStr.length() == 0)? null : inStr;
    }

    /**
    * @param inStr
    * @return boolean
    */
   public static boolean nullOrBlank(String inStr) {
        if(null == inStr || inStr.trim().length() == 0)
            return true;
        else
            return false;
    }
    
    /**************************************************************************
     * removeMultipleSpace
     * @param value -
    * @return String
     **************************************************************************/
     public static String removeMultipleSpace(String value) {
         if (value == null) return "";
         StringBuffer strval = new StringBuffer();
         for (int i = 0; i < value.length(); i++)
         {
             char ch = value.charAt(i);
             if(i > 0 && value.charAt(i) == ' ' && value.charAt(i-1) == ' ')
                 continue;
             strval.append(value.charAt(i));
         }
         return strval.toString();
     }
     
     
     /**
    * @param val
    * @return long
    */
   public static long convertStringToLong(String val) {
  		long longValue = 0;
  		if(null!=val){
  			longValue = Long.parseLong(val);
  		} 
  		return longValue;
  	}
  	
     /**
    * @param val
    * @return Integer
    */
   public static Integer convertStringToInt(String val) {
  		Integer intVal = 0;
  		if(null!=val){
  			intVal = Integer.parseInt(val);  
  		} 
  		return intVal;
  	}
  	
     /**
    * @param val
    * @return Double
    */
   public static Double convertStringToDouble(String val) {
  		Double dVal = 0.0;
  		if(null!=val){
  			dVal = Double.parseDouble(val); 
  		} 
  		return dVal;
  	}
  	
     /**
    * @param date
    * @return Date
    */
   public static Date convertStringToDateFormat(final String date) {
		Date parsedDate = null;
		if (date != null) {
			final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			try {
				parsedDate = formatter.parse(date);
			} catch (final ParseException exception) {
				exception.printStackTrace();
			}
		}
		return parsedDate;
	}
	
     /**
    * @param val
    * @return float
    */
   public static float convertStringToFloat(String val) {
		float longValue = 0;
		if(null!=val){
			longValue = Float.parseFloat(val);
		} 
		return longValue;
	}
}
