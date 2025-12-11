/**
 * Copyright: Copyright © 2019
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 *
 */

package com.jnj.facades.cronjobs;

import com.jnj.core.model.JnjLaudoModel;
import com.jnj.exceptions.BusinessException;
import com.jnj.facades.laudo.JnjLaudoFacade;
import com.jnj.la.core.model.JnjLaudoDeleteCronJobModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.exceptions.ModelRemovalException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.tx.Transaction;
import org.apache.log4j.Logger;
import java.util.List;


public class JnjRegulatoryExpireDocumentCronJob extends AbstractJobPerformable<JnjLaudoDeleteCronJobModel>
{
    private static final Logger LOGGER = Logger.getLogger(JnjRegulatoryExpireDocumentCronJob.class);

    private JnjLaudoFacade jnjLaudoFacade;

	private ModelService expireDocumentModelService;

	@Override
	public boolean isAbortable()
	{
		return true;
	}
	
    @Override
	public PerformResult perform(JnjLaudoDeleteCronJobModel jnjLaudoDeleteCronJobModel){
		List<JnjLaudoModel> jnjLaudoModelList = null;
		try {
			jnjLaudoModelList = getJnjLaudoFacade().laudoDetailsExpiredDelete(jnjLaudoDeleteCronJobModel.getCountryCode());
		} catch (BusinessException e) {
			LOGGER.info(e);
		}
		if (jnjLaudoModelList!=null){
		   Transaction transaction = null;
            try{
                transaction = Transaction.current();
                transaction.begin();
                LOGGER.debug("deleting the expired data");
                getExpireDocumentModelService().removeAll(jnjLaudoModelList);
                transaction.commit();
                LOGGER.debug("deleted the expired data");
           } catch (final ModelRemovalException e){
                if (null != transaction){
                    transaction.rollback();
                }
				LOGGER.info(e);
           }
        }
		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}

	public JnjLaudoFacade getJnjLaudoFacade() {
		return jnjLaudoFacade;
	}

	public void setJnjLaudoFacade(JnjLaudoFacade jnjLaudoFacade) {
		this.jnjLaudoFacade = jnjLaudoFacade;
	}

	public ModelService getExpireDocumentModelService() {
		return expireDocumentModelService;
	}
	public void setExpireDocumentModelService(ModelService expireDocumentModelService) {
		this.expireDocumentModelService = expireDocumentModelService;
	}

}
