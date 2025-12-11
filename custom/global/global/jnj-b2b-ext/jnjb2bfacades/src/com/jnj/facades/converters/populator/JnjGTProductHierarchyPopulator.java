/**
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */

package com.jnj.facades.converters.populator;

import java.util.Collection;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.util.Assert;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commerceservices.url.UrlResolver;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

/**
 *
 * @author nsinha7
 *
 */
public class JnjGTProductHierarchyPopulator implements Populator<CategoryModel,CategoryData>{

    private Converter<MediaModel, ImageData> imageConverter;
    protected UrlResolver<CategoryModel> hierarchyModelUrlResolver;

    @Override
    public void populate(final CategoryModel source, final CategoryData target)
    {
        populateHierarchyData(source, target);
        final Collection<MediaModel> images = source.getLogo();
        if (CollectionUtils.isNotEmpty(images))
        {
            target.setImage(getImageConverter().convert(images.iterator().next()));
        }
    }

    private void populateHierarchyData(final CategoryModel source, final CategoryData target)
    {
        Assert.notNull(source, "Parameter source cannot be null.");
        Assert.notNull(target, "Parameter target cannot be null.");

        target.setCode(source.getCode());
        target.setName(source.getName());
        target.setUrl(getHierarchyModelUrlResolver().resolve(source));
        if(source.getCustomSequence()!=null) {
            target.setSequence(source.getCustomSequence());
        }
        if(source.getCategories().size()>0) {
            target.setIsLeafCategory(false);
        }else {
            target.setIsLeafCategory(true);
        }
    }

    public Converter<MediaModel, ImageData> getImageConverter()
    {
        return imageConverter;
    }

    public void setImageConverter(final Converter<MediaModel, ImageData> imageConverter)
    {
        this.imageConverter = imageConverter;
    }

    public UrlResolver<CategoryModel> getHierarchyModelUrlResolver() {
        return hierarchyModelUrlResolver;
    }

    public void setHierarchyModelUrlResolver(UrlResolver<CategoryModel> hierarchyModelUrlResolver) {
        this.hierarchyModelUrlResolver = hierarchyModelUrlResolver;
    }

}