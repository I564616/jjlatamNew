/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.dto;

/**
 * TODO:<class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */

public class JnjUomDTO
{
	private String denominatorUom;
	private String alternativeUom;
	private String numeratorUom;
	private Double height;
	private Double width;
	private Double length;
	private String unitDimension;
	private int salesUnitsCount;



	/**
	 * @return the height
	 */
	public Double getHeight()
	{
		return height;
	}

	/**
	 * @param height
	 *           the height to set
	 */
	public void setHeight(final Double height)
	{
		this.height = height;
	}

	/**
	 * @return the width
	 */
	public Double getWidth()
	{
		return width;
	}

	/**
	 * @param width
	 *           the width to set
	 */
	public void setWidth(final Double width)
	{
		this.width = width;
	}

	/**
	 * @return the length
	 */
	public Double getLength()
	{
		return length;
	}

	/**
	 * @param length
	 *           the length to set
	 */
	public void setLength(final Double length)
	{
		this.length = length;
	}

	/**
	 * @return the unitDimension
	 */
	public String getUnitDimension()
	{
		return unitDimension;
	}

	/**
	 * @param unitDimension
	 *           the unitDimension to set
	 */
	public void setUnitDimension(final String unitDimension)
	{
		this.unitDimension = unitDimension;
	}


	/**
	 * @return the denominatorUom
	 */
	public String getDenominatorUom()
	{
		return denominatorUom;
	}

	/**
	 * @param denominatorUom
	 *           the denominatorUom to set
	 */
	public void setDenominatorUom(final String denominatorUom)
	{
		this.denominatorUom = denominatorUom;
	}

	/**
	 * @return the alternativeUom
	 */
	public String getAlternativeUom()
	{
		return alternativeUom;
	}

	/**
	 * @param alternativeUom
	 *           the alternativeUom to set
	 */
	public void setAlternativeUom(final String alternativeUom)
	{
		this.alternativeUom = alternativeUom;
	}

	/**
	 * @return the numeratorUom
	 */
	public String getNumeratorUom()
	{
		return numeratorUom;
	}

	/**
	 * @param numeratorUom
	 *           the numeratorUom to set
	 */
	public void setNumeratorUom(final String numeratorUom)
	{
		this.numeratorUom = numeratorUom;
	}

	public int getSalesUnitsCount()
	{
		return salesUnitsCount;
	}

	public void setSalesUnitsCount(final int salesUnitsCount)
	{
		this.salesUnitsCount = salesUnitsCount;
	}


}
