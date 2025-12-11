/**
 * 
 */
package com.jnj.b2b.cartandcheckoutaddon.forms;

/**
 * @author dheeraj.e.sharma
 * 
 */
public class CreditCardDetailsForm
{

	private String cardTypeCode;
	private String cardNumber;
	private String expiryMonth;
	private String expiryYear;
	private String issueNumber;
	private String verificationNumber;
	private boolean rememberCard;

	/**
	 * @return the cardTypeCode
	 */
	public String getCardTypeCode()
	{
		return cardTypeCode;
	}

	/**
	 * @param cardTypeCode
	 *           the cardTypeCode to set
	 */
	public void setCardTypeCode(final String cardTypeCode)
	{
		this.cardTypeCode = cardTypeCode;
	}

	/**
	 * @return the cardNumber
	 */
	public String getCardNumber()
	{
		return cardNumber;
	}

	/**
	 * @param cardNumber
	 *           the cardNumber to set
	 */
	public void setCardNumber(final String cardNumber)
	{
		this.cardNumber = cardNumber;
	}

	/**
	 * @return the expiryMonth
	 */
	public String getExpiryMonth()
	{
		return expiryMonth;
	}

	/**
	 * @param expiryMonth
	 *           the expiryMonth to set
	 */
	public void setExpiryMonth(final String expiryMonth)
	{
		this.expiryMonth = expiryMonth;
	}

	/**
	 * @return the expiryYear
	 */
	public String getExpiryYear()
	{
		return expiryYear;
	}

	/**
	 * @param expiryYear
	 *           the expiryYear to set
	 */
	public void setExpiryYear(final String expiryYear)
	{
		this.expiryYear = expiryYear;
	}

	/**
	 * @return the issueNumber
	 */
	public String getIssueNumber()
	{
		return issueNumber;
	}

	/**
	 * @param issueNumber
	 *           the issueNumber to set
	 */
	public void setIssueNumber(final String issueNumber)
	{
		this.issueNumber = issueNumber;
	}

	/**
	 * @return the verificationNumber
	 */
	public String getVerificationNumber()
	{
		return verificationNumber;
	}

	/**
	 * @param verificationNumber
	 *           the verificationNumber to set
	 */
	public void setVerificationNumber(final String verificationNumber)
	{
		this.verificationNumber = verificationNumber;
	}

	/**
	 * @return the rememberCard
	 */
	public boolean isRememberCard()
	{
		return rememberCard;
	}

	/**
	 * @param rememberCard
	 *           the rememberCard to set
	 */
	public void setRememberCard(final boolean rememberCard)
	{
		this.rememberCard = rememberCard;
	}


}
