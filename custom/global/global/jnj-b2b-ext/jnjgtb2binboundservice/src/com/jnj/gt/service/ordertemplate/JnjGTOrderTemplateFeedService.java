/**
 * 
 */
package com.jnj.gt.service.ordertemplate;

import java.util.List;

import com.jnj.gt.model.JnjGTIntOrderTemplateEntryModel;


/**
 * @author komal.sehgal
 * 
 */
public interface JnjGTOrderTemplateFeedService
{

	public List<JnjGTIntOrderTemplateEntryModel> getJnjGTIntOrderTemplateEntry(final String templateId);
}
