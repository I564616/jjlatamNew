/**
 * 
 */
package com.jnj.b2b.cartandcheckoutaddon.forms;

import java.util.List;


/**
 * @author Accenture
 * 
 */
public class UpdateMultipleEntriesInCartForm
{
	private List<String> entryQuantityList;

	/**
	 * @return the entryQuantityList
	 */
	public List<String> getEntryQuantityList()
	{
		return entryQuantityList;
	}

	/**
	 * @param entryQuantityList
	 *           the entryQuantityList to set
	 */
	public void setEntryQuantityList(final List<String> entryQuantityList)
	{
		this.entryQuantityList = entryQuantityList;
	}


}
