/**
 * 
 */
package com.jnj.core.services.creditcard;

import com.jnj.core.model.JnjGTCreditCardModel;


/**
 * @author himanshi.batra
 * 
 */
public interface JnjGTCreditCardService
{

	/**
	 * Save creditcard.
	 * 
	 * @param JnjGTCreditCardModel
	 *           the jnj na CreditCard model
	 * @return true, if successful
	 */
	public boolean saveCreditCard(final JnjGTCreditCardModel JnjGTCreditCardModel);


	/**
	 * Gets the creditcard model by example.
	 * 
	 * @param creditcard
	 *           the creditcard
	 * @return the creditcard model by example
	 */

	public JnjGTCreditCardModel getJnjGTCreditCardModel(JnjGTCreditCardModel creditcard);
}
