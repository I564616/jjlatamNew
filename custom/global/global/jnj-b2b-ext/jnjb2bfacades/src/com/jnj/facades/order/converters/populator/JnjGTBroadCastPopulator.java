/**
 * 
 */
package com.jnj.facades.order.converters.populator;

import de.hybris.platform.converters.Populator;

import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.util.JnJCmsUtil;
import com.jnj.facades.data.JnjGTBroadCastData;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.model.BroadcastMessageModel;


/**
 * This class is populator for BroadCast Model.
 * 
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjGTBroadCastPopulator implements Populator<BroadcastMessageModel, JnjGTBroadCastData>
{
	@Autowired
	private JnJCmsUtil jnJCmsUtil;

	@Override
	public void populate(final BroadcastMessageModel source, final JnjGTBroadCastData target)
	{
		if (source != null && target != null)
		{
			target.setId(source.getUid());
			if (null != source.getMessageLevel())
			{
				target.setType(source.getMessageLevel().getCode());
			}
			if (null != target.getContentType() && target.getContentType().equals(Jnjb2bCoreConstants.HomePage.CONTENT_TYPE_HTML))
			{
				target.setContent(source.getContent());
			}
			else if (null != target.getContentType() && null != source.getContent()
					&& target.getContentType().equals(Jnjb2bCoreConstants.HomePage.CONTENT_TYPE_TEXT))
			{
				target.setContent(source.getContent());
			}
		}
	}
}
