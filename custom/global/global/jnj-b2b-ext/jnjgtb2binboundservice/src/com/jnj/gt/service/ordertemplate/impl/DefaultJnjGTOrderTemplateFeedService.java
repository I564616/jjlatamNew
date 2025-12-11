/**
 * 
 */
package com.jnj.gt.service.ordertemplate.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.gt.dao.ordertemplate.JnjGTOrderTemplateFeedDao;
import com.jnj.gt.model.JnjGTIntOrderTemplateEntryModel;
import com.jnj.gt.service.ordertemplate.JnjGTOrderTemplateFeedService;


/**
 * @author komal.sehgal
 * 
 */
public class DefaultJnjGTOrderTemplateFeedService implements JnjGTOrderTemplateFeedService
{

	@Autowired
	private JnjGTOrderTemplateFeedDao jnjGTOrderTemplateFeedDao;

	@Override
	public List<JnjGTIntOrderTemplateEntryModel> getJnjGTIntOrderTemplateEntry(final String templateId)
	{
		return jnjGTOrderTemplateFeedDao.getJnjGTIntOrderTemplateEntry(templateId);
	}
}
