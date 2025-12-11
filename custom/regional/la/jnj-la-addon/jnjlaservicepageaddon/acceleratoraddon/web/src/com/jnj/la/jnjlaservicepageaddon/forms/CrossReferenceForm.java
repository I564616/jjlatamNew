/*

 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.la.jnjlaservicepageaddon.forms;

/**
 * @author skant3
 *
 */
public class CrossReferenceForm
{
    private String downloadType;
    private String searchTerm;
    private String pageSize;

    /**
     * @return the pageSize
     */
    public String getPageSize()
    {
        return pageSize;
    }

    /**
     * @param pageSize
     *            the pageSize to set
     */
    public void setPageSize(final String pageSize)
    {
        this.pageSize = pageSize;
    }

    /**
     * @return the searchTerm
     */
    public String getSearchTerm()
    {
        return searchTerm;
    }

    /**
     * @param searchTerm
     *            the searchTerm to set
     */
    public void setSearchTerm(final String searchTerm)
    {
        this.searchTerm = searchTerm;
    }

    /**
     * @return the downloadType
     */
    public String getDownloadType()
    {
        return downloadType;
    }

    /**
     * @param downloadType
     *            the downloadType to set
     */
    public void setDownloadType(final String downloadType)
    {
        this.downloadType = downloadType;
    }
}
