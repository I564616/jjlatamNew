/**
 * 
 */
package com.jnj.b2b.jnjglobalprofile.forms;

import java.util.List;


/**
 * @author komal.sehgal
 * 
 */
public class JnjGTProfileForm
{
	private String firstName;
	private String lastName;
	private String orgName;
	private String department;
	private String emailAddress;
	private String phone;
	private String mobile;
	private String fax;
	private String country;
	private String addressLine1;
	private String addressLine2;
	private String city;
	private String state;
	private String zip;
	private String supervisorName;
	private String supervisorEmail;
	private String supervisorPhone;
	private List<String> emailPreferences;
	private String backorderEmailType;
	private String shippedOrderEmailType;
	private String phoneNumberPrefix;
	private String mobileNumberPrefix;
	private String faxNumberPrefix;
	private String supervisorPhoneCode;
	/*5508,5509*/
	private List<String> smsPreferences;
	private String preferredMobileNumberPrefix;
	private String preferredMobileNumber;
	
	

	//AAOL-4100
		private String invoiceEmailPrefType;
		private String deliveryNoteEmailPrefType;

	public String getInvoiceEmailPrefType() {
			return invoiceEmailPrefType;
		}

		public void setInvoiceEmailPrefType(String invoiceEmailPrefType) {
			this.invoiceEmailPrefType = invoiceEmailPrefType;
		}

		public String getDeliveryNoteEmailPrefType() {
			return deliveryNoteEmailPrefType;
		}

		public void setDeliveryNoteEmailPrefType(String deliveryNoteEmailPrefType) {
			this.deliveryNoteEmailPrefType = deliveryNoteEmailPrefType;
		}

	/**
	 * @return the supervisorPhoneCode
	 */
	public String getSupervisorPhoneCode()
	{
		return supervisorPhoneCode;
	}

	/**
	 * @param supervisorPhoneCode
	 *           the supervisorPhoneCode to set
	 */
	public void setSupervisorPhoneCode(final String supervisorPhoneCode)
	{
		this.supervisorPhoneCode = supervisorPhoneCode;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName()
	{
		return firstName;
	}

	/**
	 * @param firstName
	 *           the firstName to set
	 */
	public void setFirstName(final String firstName)
	{
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName()
	{
		return lastName;
	}

	/**
	 * @param lastName
	 *           the lastName to set
	 */
	public void setLastName(final String lastName)
	{
		this.lastName = lastName;
	}

	/**
	 * @return the orgName
	 */
	public String getOrgName()
	{
		return orgName;
	}

	/**
	 * @param orgName
	 *           the orgName to set
	 */
	public void setOrgName(final String orgName)
	{
		this.orgName = orgName;
	}

	/**
	 * @return the department
	 */
	public String getDepartment()
	{
		return department;
	}

	/**
	 * @param department
	 *           the department to set
	 */
	public void setDepartment(final String department)
	{
		this.department = department;
	}

	/**
	 * @return the emailAddress
	 */
	public String getEmailAddress()
	{
		return emailAddress;
	}

	/**
	 * @param emailAddress
	 *           the emailAddress to set
	 */
	public void setEmailAddress(final String emailAddress)
	{
		this.emailAddress = emailAddress;
	}

	/**
	 * @return the phone
	 */
	public String getPhone()
	{
		return phone;
	}

	/**
	 * @param phone
	 *           the phone to set
	 */
	public void setPhone(final String phone)
	{
		this.phone = phone;
	}

	/**
	 * @return the mobile
	 */
	public String getMobile()
	{
		return mobile;
	}

	/**
	 * @param mobile
	 *           the mobile to set
	 */
	public void setMobile(final String mobile)
	{
		this.mobile = mobile;
	}

	/**
	 * @return the fax
	 */
	public String getFax()
	{
		return fax;
	}

	/**
	 * @param fax
	 *           the fax to set
	 */
	public void setFax(final String fax)
	{
		this.fax = fax;
	}

	/**
	 * @return the country
	 */
	public String getCountry()
	{
		return country;
	}

	/**
	 * @param country
	 *           the country to set
	 */
	public void setCountry(final String country)
	{
		this.country = country;
	}

	/**
	 * @return the addressLine1
	 */
	public String getAddressLine1()
	{
		return addressLine1;
	}

	/**
	 * @param addressLine1
	 *           the addressLine1 to set
	 */
	public void setAddressLine1(final String addressLine1)
	{
		this.addressLine1 = addressLine1;
	}

	/**
	 * @return the addressLine2
	 */
	public String getAddressLine2()
	{
		return addressLine2;
	}

	/**
	 * @param addressLine2
	 *           the addressLine2 to set
	 */
	public void setAddressLine2(final String addressLine2)
	{
		this.addressLine2 = addressLine2;
	}

	/**
	 * @return the city
	 */
	public String getCity()
	{
		return city;
	}

	/**
	 * @param city
	 *           the city to set
	 */
	public void setCity(final String city)
	{
		this.city = city;
	}

	/**
	 * @return the state
	 */
	public String getState()
	{
		return state;
	}

	/**
	 * @param state
	 *           the state to set
	 */
	public void setState(final String state)
	{
		this.state = state;
	}

	/**
	 * @return the zip
	 */
	public String getZip()
	{
		return zip;
	}

	/**
	 * @param zip
	 *           the zip to set
	 */
	public void setZip(final String zip)
	{
		this.zip = zip;
	}

	/**
	 * @return the supervisorName
	 */
	public String getSupervisorName()
	{
		return supervisorName;
	}

	/**
	 * @param supervisorName
	 *           the supervisorName to set
	 */
	public void setSupervisorName(final String supervisorName)
	{
		this.supervisorName = supervisorName;
	}

	/**
	 * @return the supervisorEmail
	 */
	public String getSupervisorEmail()
	{
		return supervisorEmail;
	}

	/**
	 * @param supervisorEmail
	 *           the supervisorEmail to set
	 */
	public void setSupervisorEmail(final String supervisorEmail)
	{
		this.supervisorEmail = supervisorEmail;
	}

	/**
	 * @return the supervisorPhone
	 */
	public String getSupervisorPhone()
	{
		return supervisorPhone;
	}

	/**
	 * @param supervisorPhone
	 *           the supervisorPhone to set
	 */
	public void setSupervisorPhone(final String supervisorPhone)
	{
		this.supervisorPhone = supervisorPhone;
	}

	/**
	 * @return the emailPreferences
	 */
	public List<String> getEmailPreferences()
	{
		return emailPreferences;
	}

	/**
	 * @return the backorderEmailType
	 */
	public String getBackorderEmailType() {
		return backorderEmailType;
	}

	/**
	 * @param backorderEmailType the backorderEmailType to set
	 */
	public void setBackorderEmailType(String backorderEmailType) {
		this.backorderEmailType = backorderEmailType;
	}

	/**
	 * @param emailPreferences
	 *           the emailPreferences to set
	 */
	public void setEmailPreferences(final List<String> emailPreferences)
	{
		this.emailPreferences = emailPreferences;
	}

	/**
	 * @return the phoneNumberPrefix
	 */
	public String getPhoneNumberPrefix()
	{
		return phoneNumberPrefix;
	}

	/**
	 * @param phoneNumberPrefix
	 *           the phoneNumberPrefix to set
	 */
	public void setPhoneNumberPrefix(final String phoneNumberPrefix)
	{
		this.phoneNumberPrefix = phoneNumberPrefix;
	}

	/**
	 * @return the mobileNumberPrefix
	 */
	public String getMobileNumberPrefix()
	{
		return mobileNumberPrefix;
	}

	/**
	 * @param mobileNumberPrefix
	 *           the mobileNumberPrefix to set
	 */
	public void setMobileNumberPrefix(final String mobileNumberPrefix)
	{
		this.mobileNumberPrefix = mobileNumberPrefix;
	}

	/**
	 * @return the faxNumberPrefix
	 */
	public String getFaxNumberPrefix()
	{
		return faxNumberPrefix;
	}

	/**
	 * @param faxNumberPrefix
	 *           the faxNumberPrefix to set
	 */
	public void setFaxNumberPrefix(final String faxNumberPrefix)
	{
		this.faxNumberPrefix = faxNumberPrefix;
	}

	/**
	 * @return the shippedOrderEmailType
	 */
	public String getShippedOrderEmailType() {
		return shippedOrderEmailType;
	}

	/**
	 * @param shippedOrderEmailType the shippedOrderEmailType to set
	 */
	public void setShippedOrderEmailType(String shippedOrderEmailType) {
		this.shippedOrderEmailType = shippedOrderEmailType;
	}
/*5509*/
	public List<String> getSmsPreferences() {
		return smsPreferences;
	}

	public void setSmsPreferences(List<String> smsPreferences) {
		this.smsPreferences = smsPreferences;
	}

	public String getPreferredMobileNumberPrefix() {
		return preferredMobileNumberPrefix;
	}

	public void setPreferredMobileNumberPrefix(String preferredMobileNumberPrefix) {
		this.preferredMobileNumberPrefix = preferredMobileNumberPrefix;
	}

	public String getPreferredMobileNumber() {
		return preferredMobileNumber;
	}

	public void setPreferredMobileNumber(String preferredMobileNumber) {
		this.preferredMobileNumber = preferredMobileNumber;
	}

}
