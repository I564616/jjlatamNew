package com.jnj.gt.data.product;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.jnj.core.model.JnJProductModel;


/**
 * The bean class is used for populating data against a Base product for the Evaluation logic to be performed after
 * deducing Exclusion. It is responsible to store <code>Collection</code> of <code>JnJProductModel</code> with respect
 * the base code value, as well as the active products. along with other info.
 * 
 * @author akash
 * 
 */
public class JnjGTProductExclusionData
{
	/**
	 * Product Base Code Value.
	 */
	private String productBaseCode;

	/**
	 * Map to hold Mod Based products, i.e. having different product SKU codes.
	 */
	private Map<String, JnJProductModel> modBasedProducts;

	/**
	 * Counter for total number of products having publish Indicator as 1.
	 */
	private Integer publishIndicatorCount;

	/**
	 * Counter for total number of active products.
	 */
	private Integer activeProductsCount;

	/**
	 * Map to hold active products having status code 31 only.
	 */
	private Map<String, JnJProductModel> activeModBasedProducts;

	/**
	 * Map to
	 */
	private Map<String, Date> productOnlineDates;

	/**
	 * Constructor to initialize the collections.
	 */
	public JnjGTProductExclusionData()
	{
		this.activeModBasedProducts = new HashMap<String, JnJProductModel>();
		this.modBasedProducts = new HashMap<String, JnJProductModel>();
	}

	@Override
	public boolean equals(final Object obj)
	{
		return (obj instanceof JnjGTProductExclusionData)
				&& (this.productBaseCode == ((JnjGTProductExclusionData) obj).productBaseCode);
	}

	@Override
	public int hashCode()
	{
		return this.productBaseCode.hashCode();
	}

	public String getProductBaseCode()
	{
		return productBaseCode;
	}

	public void setProductBaseCode(final String productBaseCode)
	{
		this.productBaseCode = productBaseCode;
	}

	public Map<String, JnJProductModel> getModBasedProducts()
	{
		return modBasedProducts;
	}

	public void setModBasedProducts(final Map<String, JnJProductModel> modBasedProducts)
	{
		this.modBasedProducts = modBasedProducts;
	}

	public Integer getPublishIndicatorCount()
	{
		return publishIndicatorCount;
	}

	public void setPublishIndicatorCount(final Integer publishIndicatorCount)
	{
		this.publishIndicatorCount = publishIndicatorCount;
	}

	public Integer getActiveProductsCount()
	{
		return activeProductsCount;
	}

	public void setActiveProductsCount(final Integer activeProductsCount)
	{
		this.activeProductsCount = activeProductsCount;
	}

	public Map<String, JnJProductModel> getActiveModBasedProducts()
	{
		return activeModBasedProducts;
	}

	public void setActiveModBasedProducts(final Map<String, JnJProductModel> activeModBasedProducts)
	{
		this.activeModBasedProducts = activeModBasedProducts;
	}

	public Map<String, Date> getProductOnlineDates()
	{
		return productOnlineDates;
	}

	public void setProductOnlineDates(final Map<String, Date> productOnlineDates)
	{
		this.productOnlineDates = productOnlineDates;
	}



}
