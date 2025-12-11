package com.jnj.facades.process.email.context;

import org.apache.log4j.Logger;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.model.JnjLaSAPFailedOrdersReportEmailProcessModel;

import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.user.CustomerModel;

public class JnjLaSAPFailedOrdersReportEmailContext
		extends AbstractEmailContext<JnjLaSAPFailedOrdersReportEmailProcessModel> {

	private static final Logger LOGGER = Logger.getLogger(JnjLaSAPFailedOrdersReportEmailContext.class);

	public static final String TO_DISPLAY_NAME = "toDisplayName";
	private static final String FROM_EMAIL = "fromEmail";
	private static final String BASE_SITE = "site";
	private static final String LANGUAGE = "language";
	private static final String TRUE = "true";

	/**
	 * This method is to initialize the values required for sap failed orders report
	 * data.
	 */
	@Override
	public void init(final JnjLaSAPFailedOrdersReportEmailProcessModel jnjLaSAPFailedOrdersReportEmailProcessModel,
			final EmailPageModel emailPageModel) {
		super.init(jnjLaSAPFailedOrdersReportEmailProcessModel, emailPageModel);
		try {

			put(EMAIL, jnjLaSAPFailedOrdersReportEmailProcessModel.getSapFailedOrdersReportData()
					.get(Jnjlab2bcoreConstants.SapFailedOrderReport.TO_EMAIL));
			put(FROM_EMAIL, jnjLaSAPFailedOrdersReportEmailProcessModel.getSapFailedOrdersReportData()
					.get(Jnjlab2bcoreConstants.SapFailedOrderReport.FROM_EMAIL_ADDRESS));
			put(FROM_DISPLAY_NAME, jnjLaSAPFailedOrdersReportEmailProcessModel.getSapFailedOrdersReportData()
					.get(Jnjlab2bcoreConstants.SapFailedOrderReport.FROM_EMAIL_NAME));
			put(Jnjb2bCoreConstants.EMAIL_ATTACHMENT_PATH,
					jnjLaSAPFailedOrdersReportEmailProcessModel.getSapFailedOrdersReportData()
							.get(Jnjlab2bcoreConstants.SapFailedOrderReport.ATTACHMENT_FILE_PATH));
			put(Jnjb2bCoreConstants.EXPORT_EMAIL_ATTACHMENT_FILE_NAME,
					jnjLaSAPFailedOrdersReportEmailProcessModel.getSapFailedOrdersReportData()
							.get(Jnjlab2bcoreConstants.SapFailedOrderReport.ATTACHMENT_FILE_NAME));
			put(Jnjb2bCoreConstants.DELETE_FILE, Boolean.TRUE);
			put(Jnjb2bCoreConstants.USE_DIRECT_PATH, TRUE);
			put(BASE_SITE, jnjLaSAPFailedOrdersReportEmailProcessModel.getSite());
			put(LANGUAGE, jnjLaSAPFailedOrdersReportEmailProcessModel.getLanguage());

		} catch (final Exception exception) {
			LOGGER.error("Error while sending sap failed orders email report." + exception);
		}

	}

	@Override
	protected BaseSiteModel getSite(
			JnjLaSAPFailedOrdersReportEmailProcessModel jnjLaSAPFailedOrdersReportEmailProcessModel) {
		return jnjLaSAPFailedOrdersReportEmailProcessModel.getSite();
	}

	@Override
	protected CustomerModel getCustomer(
			JnjLaSAPFailedOrdersReportEmailProcessModel jnjLaSAPFailedOrdersReportEmailProcessModel) {
		return null;
	}

	@Override
	protected LanguageModel getEmailLanguage(
			JnjLaSAPFailedOrdersReportEmailProcessModel jnjLaSAPFailedOrdersReportEmailProcessModel) {
		return jnjLaSAPFailedOrdersReportEmailProcessModel.getLanguage();
	}

}