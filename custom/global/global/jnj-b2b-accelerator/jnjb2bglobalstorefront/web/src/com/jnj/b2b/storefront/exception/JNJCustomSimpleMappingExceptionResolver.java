package com.jnj.b2b.storefront.exception;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import com.jnj.core.util.JnJCommonUtil;
import com.jnj.facades.data.JNJExceptionData;

import de.hybris.platform.commercefacades.customer.CustomerFacade;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.servicelayer.internal.service.ServicelayerUtils;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.task.TaskModel;
import de.hybris.platform.task.TaskService;
import de.hybris.platform.util.Config;


public class JNJCustomSimpleMappingExceptionResolver extends SimpleMappingExceptionResolver
{
	private static final Logger LOGGER = Logger.getLogger(JNJCustomSimpleMappingExceptionResolver.class);

	@Autowired(required = true)
	private CustomerFacade customerFacade;
	
	@Autowired
	protected JnJCommonUtil jnjCommonUtil;
	
	public JnJCommonUtil getJnjCommonUtil() {
		return jnjCommonUtil;
	}

	public void setJnjCommonUtil(JnJCommonUtil jnjCommonUtil) {
		this.jnjCommonUtil = jnjCommonUtil;
	}

	/**
	 * 
	 */
	public JNJCustomSimpleMappingExceptionResolver()
	{
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver#resolveException(javax.servlet.http.
	 * HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, java.lang.Exception)
	 */
	@Override
	public ModelAndView resolveException(final HttpServletRequest request, final HttpServletResponse response,
			final Object handler, final Exception ex)
	{
		final String stackTrace = getStackTrace(ex);
		LOGGER.error("******************** UNCAUGHT EXCEPTION DETAILS START HERE************************");
		LOGGER.error(getStackTrace(ex));
		LOGGER.error("******************** UNCAUGHT EXCEPTION DETAILS ENDS HERE************************");


		final boolean flag = Boolean.parseBoolean(Config.getParameter("jnj.support.mail.flag"));

		final JNJExceptionData jnjExceptionData = new JNJExceptionData();

		if (flag)
		{
			final ModelService modelService = (ModelService) ServicelayerUtils.getApplicationContext().getBean("modelService");
			final TaskService taskService = (TaskService) ServicelayerUtils.getApplicationContext().getBean("taskService");
			final TaskModel task = modelService.create(TaskModel.class);
			final DateFormat dateFormat = new SimpleDateFormat(jnjCommonUtil.getTimeStampFormat());
			final Calendar cal = Calendar.getInstance();
			final CustomerData customerData = customerFacade.getCurrentCustomer();
			if (customerData != null)
			{
				jnjExceptionData.setCustomerData(customerData);
			}
			jnjExceptionData.setExceptionTime(dateFormat.format(cal.getTime()));
			task.setContext(jnjExceptionData);
			jnjExceptionData.setExceptionString(stackTrace.toString());
			// the action bean name
			task.setRunnerBean("jnjExceptionMailerTask");
			// the execution time - here asap
			task.setExecutionDate(new Date());
			modelService.save(task);
			taskService.scheduleTask(task);
		}
		return super.resolveException(request, response, handler, ex);
	}

	/**
	 * @param t
	 *           , Throwable
	 * @return String of the exception
	 */
	public String getStackTrace(final Throwable throwable)
	{
		final StringWriter stringWritter = new StringWriter();
		final PrintWriter printWritter = new PrintWriter(stringWritter, true);
		throwable.printStackTrace(printWritter);
		printWritter.flush();
		stringWritter.flush();
		return stringWritter.toString();
	}
}
