/**
 * 
 */
package com.jnj.facades.company;

import de.hybris.platform.b2bcommercefacades.company.B2BUserFacade;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;

import java.io.UnsupportedEncodingException;

import com.jnj.core.dto.JnjGTPageableData;


/**
 * @author komal.sehgal
 * 
 */
public interface JnjGTB2BCommerceUserFacade extends B2BUserFacade
{

	/**
	 * @param pageableData
	 * @return
	 */
	public SearchPageData<CustomerData> searchCustomers(JnjGTPageableData pageableData);


	/**
	 * This method is used for sending Approval Email
	 * 
	 * @param string
	 * @throws DuplicateUidException
	 * @throws UnsupportedEncodingException
	 */
	public void sentApprovedProfileEmail(final String siteLogoUrl, final String userId, final String siteUrl)
			throws DuplicateUidException, UnsupportedEncodingException;

	/**
	 * This method will compare the Password Token with the Stored Token for the User For Passwrd Expiry Mail
	 * 
	 * @param passwordExpireToken
	 * @param email
	 * @return
	 * @throws DuplicateUidException
	 */
	public boolean verifyPasswordToken(String passwordExpireToken, String email) throws DuplicateUidException;

	/**
	 * This method will sent an email on User Rejection
	 * 
	 * @param siteLogoUrl
	 * @param userId
	 * @param siteUrl
	 */
	public void sentRejectionEmail(String siteLogoUrl, String userId, String siteUrl);

	/**
	 * @param siteLogoUrl
	 * @param userId
	 * @param siteUrl
	 * @throws DuplicateUidException
	 * @throws UnsupportedEncodingException
	 */
	public void sentCreateProfileEmail(final String siteLogoUrl, final String userId, final String siteUrl)
			throws DuplicateUidException, UnsupportedEncodingException;

	/**
	 * @param siteLogoUrl
	 * @param userId
	 * @param siteUrl
	 * @throws DuplicateUidException
	 * @throws UnsupportedEncodingException
	 */
	public void sentTemporaryPasswordEmail(final String siteLogoUrl, final String userId, final String siteUrl)
			throws DuplicateUidException, UnsupportedEncodingException;
}
