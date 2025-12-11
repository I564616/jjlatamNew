/**
 * 
 */
package com.jnj.b2b.cartandcheckoutaddon.forms;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author This is a Pojo Class for Proposed Line Items
 * 
 */
public class ProposedLineItemForm  implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;
	private List<JnjAddToCartCartForm> jnjAddToCartCartFormList;
	/**
	 * @return the jnjAddToCartCartFormList
	 */
	public List<JnjAddToCartCartForm> getJnjAddToCartCartFormList() {
		return jnjAddToCartCartFormList;
	}
	/**
	 * @param jnjAddToCartCartFormList the jnjAddToCartCartFormList to set
	 */
	public void setJnjAddToCartCartFormList(List<JnjAddToCartCartForm> jnjAddToCartCartFormList) {
		this.jnjAddToCartCartFormList = jnjAddToCartCartFormList;
	}
	
	


}
