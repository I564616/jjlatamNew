/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2013 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package com.jnj.b2b.storefront.renderer;

import de.hybris.platform.acceleratorcms.component.renderer.CMSComponentRenderer;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.enums.LinkTargets;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.JspWriter;
import jakarta.servlet.jsp.PageContext;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.taglibs.standard.tag.common.core.UrlSupport;

import com.jnj.b2b.storefront.tags.Functions;
import com.jnj.core.model.JnjLinkComponentModel;


/**
 */
public class JnjLinkComponentRenderer implements CMSComponentRenderer<JnjLinkComponentModel>
{
	private Converter<ProductModel, ProductData> productUrlConverter;
	private Converter<CategoryModel, CategoryData> categoryUrlConverter;

	protected Converter<ProductModel, ProductData> getProductUrlConverter()
	{
		return productUrlConverter;
	}

	public void setProductUrlConverter(final Converter<ProductModel, ProductData> productUrlConverter)
	{
		this.productUrlConverter = productUrlConverter;
	}

	protected Converter<CategoryModel, CategoryData> getCategoryUrlConverter()
	{
		return categoryUrlConverter;
	}

	public void setCategoryUrlConverter(final Converter<CategoryModel, CategoryData> categoryUrlConverter)
	{
		this.categoryUrlConverter = categoryUrlConverter;
	}

	protected String getUrl(final JnjLinkComponentModel component)
	{
		// Call the function getUrlForCMSLinkComponent so that this code is only in one place
		return Functions.getUrlForCMSLinkComponent(component, getProductUrlConverter(), getCategoryUrlConverter());
	}

	@Override
	public void renderComponent(final PageContext pageContext, final JnjLinkComponentModel component) throws ServletException,
			IOException
	{
		try
		{
			final String url = getUrl(component);
			String encodedUrl = null;

			/** Checking if the URL is null and media is attached to the link component **/
			if (StringUtils.isEmpty(url) && null != component.getDetails())
			{
				/** Setting URL as media URL **/
				encodedUrl = component.getDetails().getURL();
			}
			else
			{
				/** Else setting URL as normal URL **/
				encodedUrl = UrlSupport.resolveUrl(url, null, pageContext);
			}

			final JspWriter out = pageContext.getOut();
			out.write("<a href=\"");
			out.write(encodedUrl);
			out.write("\"");
			if(null != component.getDetails() && !component.getDetails().equals("Continue Shopping")){
			out.write(" title=\"");
			out.write(component.getLinkName());
			}
			out.write("\" ");
			if (component.getTarget() != null && !LinkTargets.SAMEWINDOW.equals(component.getTarget()))
			{
				out.write(" target=\"_blank\" ");
			}
			// Write additional attributes onto the link
			if (component.getStyleAttributes() != null)
			{
				out.write(component.getStyleAttributes());
			}
			else if (BooleanUtils.isTrue(component.getShiftRight()))
			{
				out.write(" class=\"activeSub\" ");
			}
			out.write(">");
			out.write(component.getLinkName());
			out.write("</a>");
		}
		catch (final JspException ignore)
		{
			// ignore
		}
	}
}
