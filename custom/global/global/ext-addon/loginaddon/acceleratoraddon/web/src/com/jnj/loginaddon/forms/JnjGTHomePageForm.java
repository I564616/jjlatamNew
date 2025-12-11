/**
 * 
 */
package com.jnj.loginaddon.forms;

/**
 * @author komal.sehgal
 * 
 */
public class JnjGTHomePageForm
{
	protected String products;
	protected String returnCheckbox;
	protected String template;
	//for contract added this attributes
	protected boolean isNonContractProductInCart;
	protected boolean isNonContractProductInSelectedList;
	protected boolean isQuantityStatus;
	
	
	

	public boolean getisQuantityStatus() {
		return isQuantityStatus;
	}

	public void setQuantityStatus(boolean isQuantityStatus) {
		this.isQuantityStatus = isQuantityStatus;
	}

	/**
	 * @return the returnCheckbox
	 */
	public String getReturnCheckbox()
	{
		return returnCheckbox;
	}

	/**
	 * @return the template
	 */
	public String getTemplate()
	{
		return template;
	}

	/**
	 * @param template
	 *           the template to set
	 */
	public void setTemplate(final String template)
	{
		this.template = template;
	}

	/**
	 * @param returnCheckbox
	 *           the returnCheckbox to set
	 */
	public void setReturnCheckbox(final String returnCheckbox)
	{
		this.returnCheckbox = returnCheckbox;
	}

	/**
	 * @return the products
	 */
	public String getProducts()
	{
		return products;
	}

	/**
	 * @param products
	 *           the products to set
	 */
	public void setProducts(final String products)
	{
		this.products = products;
	}

	/**
	 * @return the isNonContractProductInCart
	 */
	public boolean isNonContractProductInCart()
	{
		return isNonContractProductInCart;
	}

	/**
	 * @param isNonContractProductInCart the isNonContractProductInCart to set
	 */
	public void setNonContractProductInCart(boolean isNonContractProductInCart)
	{
		this.isNonContractProductInCart = isNonContractProductInCart;
	}

	/**
	 * @return the isNonContractProductInSelectedList
	 */
	public boolean isNonContractProductInSelectedList()
	{
		return isNonContractProductInSelectedList;
	}

	/**
	 * @param isNonContractProductInSelectedList the isNonContractProductInSelectedList to set
	 */
	public void setNonContractProductInSelectedList(boolean isNonContractProductInSelectedList)
	{
		this.isNonContractProductInSelectedList = isNonContractProductInSelectedList;
	}

	
}
