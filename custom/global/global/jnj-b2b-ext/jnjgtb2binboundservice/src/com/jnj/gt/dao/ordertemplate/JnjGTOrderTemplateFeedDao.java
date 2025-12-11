/**
 * 
 */
package com.jnj.gt.dao.ordertemplate;

import java.util.List;

import com.jnj.gt.model.JnjGTIntOrderTemplateEntryModel;

/**
 * @author komal.sehgal
 * 
 */
public interface JnjGTOrderTemplateFeedDao
{

	public List<JnjGTIntOrderTemplateEntryModel> getJnjGTIntOrderTemplateEntry(final String templateId);

}
