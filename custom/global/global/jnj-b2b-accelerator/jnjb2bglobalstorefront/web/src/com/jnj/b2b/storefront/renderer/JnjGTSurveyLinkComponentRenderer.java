package com.jnj.b2b.storefront.renderer;

import java.io.IOException;
import com.jnj.core.model.JnjGTSurveyLinkComponentModel;
import jakarta.servlet.ServletException;
import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.JspWriter;
import jakarta.servlet.jsp.PageContext;
import de.hybris.platform.cms2.enums.LinkTargets;

import de.hybris.platform.acceleratorcms.component.renderer.CMSComponentRenderer;


/**
 * This class renders the JnjSurveyParagraphComponent
 * 
 * @author sanchit.a.kumar
 * 
 */
public class JnjGTSurveyLinkComponentRenderer implements CMSComponentRenderer<JnjGTSurveyLinkComponentModel>
{
	@Override
	public void renderComponent(final PageContext pageContext, final JnjGTSurveyLinkComponentModel component)
			throws ServletException, IOException
	{
		final JspWriter out = pageContext.getOut();
		out.write("<a id=\"surveyOkLink\" href=\"");
		out.write(component.getUrl());
		out.write("\" title=\"");
		out.write(component.getLinkName());
		if (component.getTarget() != null && !LinkTargets.SAMEWINDOW.equals(component.getTarget()))
		{
			out.write("\" target=\"_blank");
		}
		out.write("\">");
		out.write(component.getLinkName());
		out.write("</a>");
	}
}
