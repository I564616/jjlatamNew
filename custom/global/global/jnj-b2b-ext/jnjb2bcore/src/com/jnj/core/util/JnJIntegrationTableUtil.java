package com.jnj.core.util;

import java.lang.reflect.InvocationTargetException;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.jnj.core.dto.JnjCanonicalDTO;
import com.jnj.core.model.JnjIntegrationCronJobModel;

import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.ItemModel;

public class JnJIntegrationTableUtil {
	
	private static final Logger LOGGER = Logger.getLogger(JnJIntegrationTableUtil.class);
	
	/**
	 * @param className
	 * @param MethodName
	 * @param returnType
	 * @param methodParam
	 * @param jnjCanonicalDTO
	 * @param resultSetMap
	 * @return
	 */
	public static <T> Object getUtilReturnValue(String className, String MethodName, String returnType,
			String methodParam, JnjCanonicalDTO jnjCanonicalDTO, Map<String, String> resultSetMap) {
		try {
			// Create object using spring framework
			Object object = Registry.getApplicationContext().getBean(className);
			Method[] allMethods = object.getClass().getDeclaredMethods();
			for (Method method : allMethods) {
				String methodName = method.getName();
				if (MethodName.equalsIgnoreCase(methodName)) {
					if(StringUtils.isEmpty(returnType)){
						method.invoke(object, jnjCanonicalDTO, resultSetMap);
					}else{
						return method.invoke(object, jnjCanonicalDTO, resultSetMap);

					}
					// invoke method
				}
			}
		} catch (IllegalAccessException ex) {
			// TODO Auto-generated catch block
			LOGGER.error(ex.getMessage());
		} catch (IllegalArgumentException ex) {
			// TODO Auto-generated catch block
			LOGGER.error(ex.getMessage());
		} catch (InvocationTargetException ex) {
			// TODO Auto-generated catch block
			LOGGER.error(ex.getMessage());
		}
		return null;
	}
	
	
	/**
	 * This method used to identify the class and method to invoke the post process with the orderNumList
	 * @param jobModel 
	 * @param returnType
	 * @param methodParam
	 * @param jnjCanonicalDTO
	 * @param resultSetMap
	 * @param orderNumList
	 * @return <T>
	 */
	public static <T> Object getUtilReturnValue(JnjIntegrationCronJobModel jobModel, String returnType,
			String methodParam, JnjCanonicalDTO jnjCanonicalDTO, List<LinkedHashMap<String, String>> resultSetMap, Set<String> orderNumList) {
		try {
			// Create object using spring framework
			String className = jobModel.getPostProcessingClassName();
			String targetMethodName = jobModel.getPostProcessingMethodName();
			Object object = Registry.getApplicationContext().getBean(className);
			Method[] allMethods = object.getClass().getDeclaredMethods();
			for (Method method : allMethods) {
				String methodName = method.getName();
				if (targetMethodName.equalsIgnoreCase(methodName)) {
					if(StringUtils.isEmpty(returnType)){
						method.invoke(object,jnjCanonicalDTO, resultSetMap, orderNumList);
					}else{
						return method.invoke(object,jnjCanonicalDTO, resultSetMap, orderNumList);
					}
					// invoke method
				}
			}
		} catch (IllegalAccessException ex) {
			LOGGER.error(ex.getMessage());
		} catch (IllegalArgumentException ex) {
			LOGGER.error(ex.getMessage());
		} catch (InvocationTargetException ex) {
			LOGGER.error(ex.getMessage());
		}
		return null;
	}
	
	/**
	 * @param className
	 * @param MethodName
	 * @param returnType
	 * @param methodParam
	 * @param jnjCanonicalDTO
	 * @param itemModel
	 * @param resultSetMap
	 * @return
	 * Changes took it from APAC
	 */
	public static <T> Object getUtilReturnValue(final String className, final String MethodName, final String returnType,
			final String methodParam, final JnjCanonicalDTO jnjCanonicalDTO, final ItemModel itemModel,
			final Map<String, String> resultSetMap)
	{
		try
		{
			// Create object using spring framework
			final Object object = Registry.getApplicationContext().getBean(className);
			final Method[] allMethods = object.getClass().getDeclaredMethods();
			for (final Method method : allMethods)
			{
				final String methodName = method.getName();
				if (MethodName.equalsIgnoreCase(methodName))
				{
					if (StringUtils.isEmpty(returnType))
					{
						method.invoke(object, jnjCanonicalDTO, itemModel, resultSetMap);
					}
					else
					{
						return method.invoke(object, jnjCanonicalDTO, itemModel, resultSetMap);

					}
					// invoke method
				}
			}
		}
		catch (final IllegalAccessException ex)
		{
			// TODO Auto-generated catch block
			LOGGER.error(ex.getMessage());
		}
		catch (final IllegalArgumentException ex)
		{
			// TODO Auto-generated catch block
			LOGGER.error(ex.getMessage());
		}
		catch (final InvocationTargetException ex)
		{
			// TODO Auto-generated catch block
			LOGGER.error(ex.getMessage());
		}
		return null;
	}
	
	
}
