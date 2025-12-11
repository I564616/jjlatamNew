/**
 *
 */
package com.jnj.core.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import jakarta.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;


/**
 * @author komal.sehgal
 *
 */
public class JnjGTCoreUtil
{
	private static final Logger LOGGER = Logger.getLogger(JnjGTCoreUtil.class);
	protected static Date MIN_START_DATE;
	protected static Date MAX_END_DATE;
	protected static final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-YYYY");

	public static boolean compareDates(final Date effectiveDate, final Date endDate)
	{
		boolean dateFlag = false;
		final Date today = new Date();
		if (today.compareTo(effectiveDate) >= 0 && endDate.compareTo(today) >= 0)
		{
			dateFlag = true;
		}
		return dateFlag;
	}

	/**
	 * This method is used to convert date range into desired date format - 'requiredFormat' from the 'currentFormat'
	 * supplied. It also adjusts the date at the days level by the integer value supplied in the parameter -
	 * 'adjustmentInDays'. If this method fails to parse any date, it will return null.
	 *
	 * @param date
	 * @param currentFormat
	 * @param requiredFormat
	 * @param adjustmentInDays
	 * @return requiredDateFormat
	 */
	public static String convertDateFormat(final String date, final String currentFormat, final String requiredFormat,
			final int adjustmentInDays)
	{
		final String METHOD_GET_DATE_RANGE = "convertDateFormat()";
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Jnjb2bCoreConstants.Logging.DATE_UTIL + Logging.HYPHEN + METHOD_GET_DATE_RANGE + Logging.HYPHEN
				+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis());
		}
		final SimpleDateFormat formatFrom = new SimpleDateFormat(currentFormat);
		final SimpleDateFormat formatTo = new SimpleDateFormat(requiredFormat);
		String reformattedStr = null;
		try
		{
			final Calendar cal = Calendar.getInstance();
			cal.setTime(formatFrom.parse(date));
			cal.add(Calendar.DATE, adjustmentInDays);
			formatTo.setCalendar(cal);
			reformattedStr = formatTo.format(cal.getTime());
		}
		catch (final ParseException parseException)
		{
			LOGGER.error(Jnjb2bCoreConstants.Logging.DATE_UTIL + Logging.HYPHEN + METHOD_GET_DATE_RANGE + Logging.HYPHEN
					+ "Parsing Excpetion Occured." + parseException);
			return null;
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Jnjb2bCoreConstants.Logging.DATE_UTIL + Logging.HYPHEN + METHOD_GET_DATE_RANGE + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis());
		}
		return reformattedStr;
	}

	public static String getIpAddress(final HttpServletRequest request)
	{

		String ipAddress = request.getHeader("X-FORWARDED-FOR");
		if (ipAddress == null)
		{
			ipAddress = request.getRemoteAddr();
		}
		return ipAddress;
	}

	public static String getFormattedPhoneNumber(final String phone)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Jnjb2bCoreConstants.Logging.DATE_UTIL + Logging.HYPHEN + "getFormattedPhoneNumber" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis());
		}
		final StringBuilder formattedPhone = new StringBuilder("");
		if (StringUtils.isNotEmpty(phone))
		{
			final String[] phoneNumbers = phone.split(Jnjb2bCoreConstants.SYMBOl_DASH);

			for (final String number : phoneNumbers)
			{
				formattedPhone.append(number);
			}
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Jnjb2bCoreConstants.Logging.DATE_UTIL + Logging.HYPHEN + "getFormattedPhoneNumber" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis());
		}
		return formattedPhone.toString();
	}

	public static Long convertStringToLong(final String value)
	{
		return Long.valueOf((long) Double.parseDouble(value));
	}

	/**
	 * Convert string to date.
	 *
	 * @param date
	 *           the date
	 * @param dateFormat
	 *           the date format
	 * @return the date
	 */
	public static Date convertStringToDate(final String date, final String dateFormat)
	{
		final String METHOD_STRING_TO_DATE = "convertStringToDate()";
		final SimpleDateFormat formatFrom = new SimpleDateFormat(dateFormat);
		final Calendar cal = Calendar.getInstance();
		try
		{
			cal.setTime(formatFrom.parse(date));
		}
		catch (final ParseException parseException)
		{
			LOGGER.error(Jnjb2bCoreConstants.Logging.DATE_UTIL + Logging.HYPHEN + METHOD_STRING_TO_DATE + Logging.HYPHEN
					+ "Parsing Excpetion Occured." + parseException);
			return null;
		}
		return cal.getTime();
	}

	/**
	 * This method fetches the days difference between the start date and end date supplied, ignoring the time of day in
	 * calculations
	 *
	 * @param startDate
	 * @param endDate
	 *
	 * @return daysDifference
	 */
	public static int getDaysDiff(final Date startDate, final Date endDate)
	{
		final Calendar calStart = Calendar.getInstance();
		final Calendar calEnd = Calendar.getInstance();

		calStart.setTime(startDate);
		calEnd.setTime(endDate);

		/** Setting the time of day to 0 for ignoring in calculations **/
		calStart.set(Calendar.HOUR_OF_DAY, 0);
		calStart.set(Calendar.MINUTE, 0);
		calStart.set(Calendar.SECOND, 0);
		calStart.set(Calendar.MILLISECOND, 0);

		/** Setting the time of day to 0 for ignoring in calculations **/
		calEnd.set(Calendar.HOUR_OF_DAY, 0);
		calEnd.set(Calendar.MINUTE, 0);
		calEnd.set(Calendar.SECOND, 0);
		calEnd.set(Calendar.MILLISECOND, 0);

		return (int) ((calEnd.getTimeInMillis() - calStart.getTimeInMillis()) / (1000 * 60 * 60 * 24));
	}

	/**
	 * Format values by comma separated. Empty/Null Values will be ignored
	 *
	 * @param String
	 *           values which need to be format
	 * @return the string
	 */
	public static String formatValuesByCommaSeparated(final String... stringVals)
	{
		StringBuilder sBuilder = null;
		String finalString = StringUtils.EMPTY;
		for (final String value : stringVals)
		{
			if (StringUtils.isNotEmpty(value))
			{
				if (null == sBuilder)
				{
					sBuilder = new StringBuilder(value);
				}
				else
				{
					sBuilder.append(Jnjb2bCoreConstants.SYMBOl_COMMA + value);
				}
			}
		}
		if (null != sBuilder)
		{
			finalString = sBuilder.toString();
		}
		return finalString;
	}
	
	public static Date getMinDate()
	{
		if (MIN_START_DATE == null)
		{
			try
			{
				MIN_START_DATE = formatter.parse("01-01-1900");
			}
			catch (final ParseException exception)
			{
				LOGGER.error("Exception occured during parsing effective date in the method getEffectiveDate()");
			}
		}

		return MIN_START_DATE;
	}

	public static Date getMaxDate()
	{
		if (MAX_END_DATE == null)
		{
			try
			{
				MAX_END_DATE = formatter.parse("30-12-9999");
			}
			catch (final ParseException exception)
			{
				LOGGER.error("Exception occured during parsing endDate in the method getEndDate()");
			}
		}
		return MAX_END_DATE;
	}
	
	public static <T> void logInfoMessage(final String functionalityName, final String methodName, final String errorMessage, Class<T> className)
	{
		final Logger LOGGER = Logger.getLogger(className);
		LOGGER.info(functionalityName + Logging.HYPHEN + methodName + Logging.HYPHEN + errorMessage + Logging.HYPHEN
				+ JnJCommonUtil.getCurrentDateTime());
	}

	public static <T> void logWarnMessage(final String functionalityName, final String methodName, final String errorMessage,
			final Exception e, Class<T> className)
	{
		final Logger LOGGER = Logger.getLogger(className);
		LOGGER.warn(
				functionalityName + Logging.HYPHEN + methodName + Logging.HYPHEN + errorMessage + Logging.HYPHEN
						+ JnJCommonUtil.getCurrentDateTime(), e);
	}

	public static <T> void logWarnMessage(final String functionalityName, final String methodName, final String errorMessage,
			final Throwable throwable, Class<T> className)
	{
		final Logger LOGGER = Logger.getLogger(className);
		LOGGER.warn(
				functionalityName + Logging.HYPHEN + methodName + Logging.HYPHEN + errorMessage + Logging.HYPHEN
						+ JnJCommonUtil.getCurrentDateTime(), throwable);
	}

	public static <T> void logWarnMessage(final String functionalityName, final String methodName, final String errorMessage, Class<T> className)
	{
		final Logger LOGGER = Logger.getLogger(className);
		LOGGER.warn(functionalityName + Logging.HYPHEN + methodName + Logging.HYPHEN + errorMessage + Logging.HYPHEN
				+ JnJCommonUtil.getCurrentDateTime());
	}


	public static <T> void logDebugMessage(final String functionalityName, final String methodName, final String errorMessage, Class<T> className)
	{
		final Logger LOGGER = Logger.getLogger(className);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(functionalityName + Logging.HYPHEN + methodName + Logging.HYPHEN + errorMessage + Logging.HYPHEN
					+ JnJCommonUtil.getCurrentDateTime());
		}
	}


	public static <T> void logErrorMessage(final String functionalityName, final String methodName, final String errorMessage,
			final Exception e, Class<T> className)
	{
		final Logger LOGGER = Logger.getLogger(className);
		LOGGER.error(
				functionalityName + Logging.HYPHEN + methodName + Logging.HYPHEN + errorMessage + Logging.HYPHEN
						+ JnJCommonUtil.getCurrentDateTime(), e);
	}


	public static <T> void logErrorMessage(final String functionalityName, final String methodName, final String errorMessage,
			final Throwable exception, Class<T> className)
	{
		final Logger LOGGER = Logger.getLogger(className);
		LOGGER.error(
				functionalityName + Logging.HYPHEN + methodName + Logging.HYPHEN + errorMessage + Logging.HYPHEN
						+ JnJCommonUtil.getCurrentDateTime(), exception);
	}
	

	public static <T> void logErrorMessage(final String functionalityName, final String methodName, final String errorMessage, Class<T> className)
	{
		final Logger LOGGER = Logger.getLogger(className);
		LOGGER.error(
				functionalityName + Logging.HYPHEN + methodName + Logging.HYPHEN + errorMessage + Logging.HYPHEN
						+ JnJCommonUtil.getCurrentDateTime());
	}

}
