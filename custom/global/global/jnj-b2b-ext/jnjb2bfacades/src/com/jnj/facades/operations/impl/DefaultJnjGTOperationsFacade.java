/**
 * 
 */
package com.jnj.facades.operations.impl;

import de.hybris.platform.converters.Populator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.facades.data.JnjGTBroadCastData;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.facades.operations.JnjGTOperationsFacade;
import com.jnj.core.model.BroadcastMessageModel;
import com.jnj.core.services.operations.JnjGTOperationsService;


/**
 * This class acts for the Middle Level operations
 * 
 * @author Accenture
 * @version 1.0
 */
public class DefaultJnjGTOperationsFacade implements JnjGTOperationsFacade
{

	@Autowired
	private JnjGTOperationsService jnjGTOperationService;

	@Autowired
	private Populator<BroadcastMessageModel, JnjGTBroadCastData> jnjgtBroadCastDataPopulator;


	@Override
	public List<JnjGTBroadCastData> getBroadCastMessages()
	{
		final List<JnjGTBroadCastData> broadCastDataList = new ArrayList<JnjGTBroadCastData>();
		final List<BroadcastMessageModel> broadCastMessages = jnjGTOperationService.getBroadCastMessages();
		for (final BroadcastMessageModel broadCast : broadCastMessages)
		{
			final JnjGTBroadCastData broadCastData = new JnjGTBroadCastData();
			broadCastData.setContentType(Jnjb2bCoreConstants.HomePage.CONTENT_TYPE_TEXT);
			jnjgtBroadCastDataPopulator.populate(broadCast, broadCastData);
			broadCastDataList.add(broadCastData);
		}
		return broadCastDataList;
	}

	public JnjGTBroadCastData getBroadCastDataForId(final String broadCastId)
	{
		final BroadcastMessageModel broadCastModel = jnjGTOperationService.getBroadCastDataForId(broadCastId);
		final JnjGTBroadCastData broadCastData = new JnjGTBroadCastData();
		broadCastData.setContentType(Jnjb2bCoreConstants.HomePage.CONTENT_TYPE_HTML);
		jnjgtBroadCastDataPopulator.populate(broadCastModel, broadCastData);
		return broadCastData;

	}


}
