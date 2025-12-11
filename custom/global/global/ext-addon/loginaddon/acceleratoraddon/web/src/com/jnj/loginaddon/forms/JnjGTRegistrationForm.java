/**
 * 
 */
package com.jnj.loginaddon.forms;

import java.util.List;

import com.jnj.facades.data.SecretQuestionData;



/**
 * Form object for new user registration
 * 
 * @author ujjwal.negi
 * 
 */
public class JnjGTRegistrationForm
{
	private Boolean consumerProductsSector;
	private Boolean mddSector;
	private Boolean pharmaSector;
	private String typeOfProfile;
	private String accountNumbers;
	private Boolean unknownAccount;
	private String wWID;
	private String division;
	private String accountName;
	private String gLN;
	private String typeOfBusiness;
	private String subsidiaryOf;
	private String billToCountry;
	private String billToLine1;
	private String billToLine2;
	private String billToCity;
	private String billToState;
	private String billToZipCode;
	private String shipToCountry;
	private String shipToLine1;
	private String shipToLine2;
	private String shipToCity;
	private String shipToState;
	private String shipToZipCode;
	private Boolean salesAndUseTaxFlag;
	private String initialOpeningOrderAmount;
	private String estimatedAmountPerYear;
	private List<String> medicalProductsPurchase;
	private String firstName;
	private String lastName;
	private String orgName;
	private String department;
	private String emailAddress;
	private String phone;
	private String mobile;
	private String fax;
	private String phoneNumberPrefix;
	private String mobileNumberPrefix;
	private String faxPrefix;
	private String country;
	private String addressLine1;
	private String addressLine2;
	private String city;
	private String state;
	private String zip;
	private String permissionLevel;
	private String supervisorName;
	private String supervisorEmail;
	private String supervisorPhonePrefix;
	
	private String supervisorPhone;
	private List<String> emailPreferences;
	/*5506*/
	private List<String> smsPreferences;
	private String preferredMobileNumberPrefix;
	private String preferredMobileNumber;
	private String backorderEmailType;
	//AAOL-3716
	private String shippedOrderEmailType;
	
	private String password;
	private List<SecretQuestionData> secretQuestionsAnswers;
	private String glnOrAccountNumber;
	
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

	public String getShippedOrderEmailType() {
		return shippedOrderEmailType;
	}

	public void setShippedOrderEmailType(String shippedOrderEmailType) {
		this.shippedOrderEmailType = shippedOrderEmailType;
	}




	/**
	 * @return the glnOrAccountNumber
	 */
	public String getGlnOrAccountNumber()
	{
		return glnOrAccountNumber;
	}

	/**
	 * @param glnOrAccountNumber
	 *           the glnOrAccountNumber to set
	 */
	public void setGlnOrAccountNumber(final String glnOrAccountNumber)
	{
		this.glnOrAccountNumber = glnOrAccountNumber;
	}

	/**
	 * @return the consumerProductsSector
	 */
	public Boolean getConsumerProductsSector()
	{
		return consumerProductsSector;
	}

	/**
	 * @param consumerProductsSector
	 *           the consumerProductsSector to set
	 */
	public void setConsumerProductsSector(final Boolean consumerProductsSector)
	{
		this.consumerProductsSector = consumerProductsSector;
	}

	/**
	 * @return the mddSector
	 */
	public Boolean getMddSector()
	{
		return mddSector;
	}

	/**
	 * @param mddSector
	 *           the mddSector to set
	 */
	public void setMddSector(final Boolean mddSector)
	{
		this.mddSector = mddSector;
	}

	/**
	 * @return the typeOfProfile
	 */
	public String getTypeOfProfile()
	{
		return typeOfProfile;
	}

	/**
	 * @param typeOfProfile
	 *           the typeOfProfile to set
	 */
	public void setTypeOfProfile(final String typeOfProfile)
	{
		this.typeOfProfile = typeOfProfile;
	}

	/**
	 * @return the accountNumbers
	 */
	public String getAccountNumbers()
	{
		return accountNumbers;
	}

	/**
	 * @param accountNumbers
	 *           the accountNumbers to set
	 */
	public void setAccountNumbers(final String accountNumbers)
	{
		this.accountNumbers = accountNumbers;
	}

	/**
	 * @return the unknownAccount
	 */
	public Boolean getUnknownAccount()
	{
		return unknownAccount;
	}

	/**
	 * @param unknownAccount
	 *           the unknownAccount to set
	 */
	public void setUnknownAccount(final Boolean unknownAccount)
	{
		this.unknownAccount = unknownAccount;
	}

	/**
	 * @return the wwID
	 */
	public String getWWID()
	{
		return wWID;
	}

	/**
	 * @param wwID
	 *           the wwID to set
	 */
	public void setWWID(final String wwID)
	{
		this.wWID = wwID;
	}

	/**
	 * @return the division
	 */
	public String getDivision()
	{
		return division;
	}

	/**
	 * @param division
	 *           the division to set
	 */
	public void setDivision(final String division)
	{
		this.division = division;
	}

	/**
	 * @return the accountName
	 */
	public String getAccountName()
	{
		return accountName;
	}

	/**
	 * @param accountName
	 *           the accountName to set
	 */
	public void setAccountName(final String accountName)
	{
		this.accountName = accountName;
	}

	/**
	 * @return the gLN
	 */
	public String getgLN()
	{
		return gLN;
	}

	/**
	 * @param gLN
	 *           the gLN to set
	 */
	public void setgLN(final String gLN)
	{
		this.gLN = gLN;
	}

	/**
	 * @return the typeOfBusiness
	 */
	public String getTypeOfBusiness()
	{
		return typeOfBusiness;
	}

	/**
	 * @param typeOfBusiness
	 *           the typeOfBusiness to set
	 */
	public void setTypeOfBusiness(final String typeOfBusiness)
	{
		this.typeOfBusiness = typeOfBusiness;
	}

	/**
	 * @return the subsidiaryOf
	 */
	public String getSubsidiaryOf()
	{
		return subsidiaryOf;
	}

	/**
	 * @param subsidiaryOf
	 *           the subsidiaryOf to set
	 */
	public void setSubsidiaryOf(final String subsidiaryOf)
	{
		this.subsidiaryOf = subsidiaryOf;
	}

	/**
	 * @return the billToLine1
	 */
	public String getBillToLine1()
	{
		return billToLine1;
	}

	/**
	 * @param billToLine1
	 *           the billToLine1 to set
	 */
	public void setBillToLine1(final String billToLine1)
	{
		this.billToLine1 = billToLine1;
	}

	/**
	 * @return the billToLine2
	 */
	public String getBillToLine2()
	{
		return billToLine2;
	}

	/**
	 * @param billToLine2
	 *           the billToLine2 to set
	 */
	public void setBillToLine2(final String billToLine2)
	{
		this.billToLine2 = billToLine2;
	}

	/**
	 * @return the billToCity
	 */
	public String getBillToCity()
	{
		return billToCity;
	}

	/**
	 * @param billToCity
	 *           the billToCity to set
	 */
	public void setBillToCity(final String billToCity)
	{
		this.billToCity = billToCity;
	}

	/**
	 * @return the billToState
	 */
	public String getBillToState()
	{
		return billToState;
	}

	/**
	 * @param billToState
	 *           the billToState to set
	 */
	public void setBillToState(final String billToState)
	{
		this.billToState = billToState;
	}

	/**
	 * @return the billToZipCode
	 */
	public String getBillToZipCode()
	{
		return billToZipCode;
	}

	/**
	 * @param billToZipCode
	 *           the billToZipCode to set
	 */
	public void setBillToZipCode(final String billToZipCode)
	{
		this.billToZipCode = billToZipCode;
	}

	/**
	 * @return the shipToLine1
	 */
	public String getShipToLine1()
	{
		return shipToLine1;
	}

	/**
	 * @param shipToLine1
	 *           the shipToLine1 to set
	 */
	public void setShipToLine1(final String shipToLine1)
	{
		this.shipToLine1 = shipToLine1;
	}

	/**
	 * @return the shipToLine2
	 */
	public String getShipToLine2()
	{
		return shipToLine2;
	}

	/**
	 * @param shipToLine2
	 *           the shipToLine2 to set
	 */
	public void setShipToLine2(final String shipToLine2)
	{
		this.shipToLine2 = shipToLine2;
	}

	/**
	 * @return the shipToCity
	 */
	public String getShipToCity()
	{
		return shipToCity;
	}

	/**
	 * @param shipToCity
	 *           the shipToCity to set
	 */
	public void setShipToCity(final String shipToCity)
	{
		this.shipToCity = shipToCity;
	}

	/**
	 * @return the shipToState
	 */
	public String getShipToState()
	{
		return shipToState;
	}

	/**
	 * @param shipToState
	 *           the shipToState to set
	 */
	public void setShipToState(final String shipToState)
	{
		this.shipToState = shipToState;
	}

	/**
	 * @return the shipToZipCode
	 */
	public String getShipToZipCode()
	{
		return shipToZipCode;
	}

	/**
	 * @param shipToZipCode
	 *           the shipToZipCode to set
	 */
	public void setShipToZipCode(final String shipToZipCode)
	{
		this.shipToZipCode = shipToZipCode;
	}

	/**
	 * @return the salesAndUseTaxFlag
	 */
	public Boolean getSalesAndUseTaxFlag()
	{
		return salesAndUseTaxFlag;
	}

	/**
	 * @param salesAndUseTaxFlag
	 *           the salesAndUseTaxFlag to set
	 */
	public void setSalesAndUseTaxFlag(final Boolean salesAndUseTaxFlag)
	{
		this.salesAndUseTaxFlag = salesAndUseTaxFlag;
	}

	/**
	 * @return the initialOpeningOrderAmount
	 */
	public String getInitialOpeningOrderAmount()
	{
		return initialOpeningOrderAmount;
	}

	/**
	 * @param initialOpeningOrderAmount
	 *           the initialOpeningOrderAmount to set
	 */
	public void setInitialOpeningOrderAmount(final String initialOpeningOrderAmount)
	{
		this.initialOpeningOrderAmount = initialOpeningOrderAmount;
	}

	/**
	 * @return the estimatedAmountPerYear
	 */
	public String getEstimatedAmountPerYear()
	{
		return estimatedAmountPerYear;
	}

	/**
	 * @param estimatedAmountPerYear
	 *           the estimatedAmountPerYear to set
	 */
	public void setEstimatedAmountPerYear(final String estimatedAmountPerYear)
	{
		this.estimatedAmountPerYear = estimatedAmountPerYear;
	}

	/**
	 * @return the medicalProductsPurchase
	 */
	public List<String> getMedicalProductsPurchase()
	{
		return medicalProductsPurchase;
	}

	/**
	 * @param medicalProductsPurchase
	 *           the medicalProductsPurchase to set
	 */
	public void setMedicalProductsPurchase(final List<String> medicalProductsPurchase)
	{
		this.medicalProductsPurchase = medicalProductsPurchase;
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
	 * @return the supervisorFirstName
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
	 * @return the password
	 */
	public String getPassword()
	{
		return password;
	}

	/**
	 * @param password
	 *           the password to set
	 */
	public void setPassword(final String password)
	{
		this.password = password;
	}

	/**
	 * @return the secretQuestionsAnswers
	 */
	public List<SecretQuestionData> getSecretQuestionsAnswers()
	{
		return secretQuestionsAnswers;
	}

	/**
	 * @param secretQuestionsAnswers
	 *           the secretQuestionsAnswers to set
	 */
	public void setSecretQuestionsAnswers(final List<SecretQuestionData> secretQuestionsAnswers)
	{
		this.secretQuestionsAnswers = secretQuestionsAnswers;
	}

	/**
	 * @return the permissionLevel
	 */
	public String getPermissionLevel()
	{
		return permissionLevel;
	}

	/**
	 * @param permissionLevel
	 *           the permissionLevel to set
	 */
	public void setPermissionLevel(final String permissionLevel)
	{
		this.permissionLevel = permissionLevel;
	}

	/**
	 * @return the billToCountry
	 */
	public String getBillToCountry()
	{
		return billToCountry;
	}

	/**
	 * @param billToCountry
	 *           the billToCountry to set
	 */
	public void setBillToCountry(final String billToCountry)
	{
		this.billToCountry = billToCountry;
	}

	/**
	 * @return the shipToCountry
	 */
	public String getShipToCountry()
	{
		return shipToCountry;
	}

	/**
	 * @param shipToCountry
	 *           the shipToCountry to set
	 */
	public void setShipToCountry(final String shipToCountry)
	{
		this.shipToCountry = shipToCountry;
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

	public Boolean getPharmaSector() {
		return pharmaSector;
	}

	public void setPharmaSector(Boolean pharmaSector) {
		this.pharmaSector = pharmaSector;
	}


	public String getPhoneNumberPrefix() {
		return phoneNumberPrefix;
	}

	public void setPhoneNumberPrefix(String phoneNumberPrefix) {
		this.phoneNumberPrefix = phoneNumberPrefix;
	}

	public String getSupervisorPhonePrefix() {
		return supervisorPhonePrefix;
	}

	public void setSupervisorPhonePrefix(String supervisorPhonePrefix) {
		this.supervisorPhonePrefix = supervisorPhonePrefix;
	}
	
	public String getMobileNumberPrefix() {
		return mobileNumberPrefix;
	}

	public void setMobileNumberPrefix(String mobileNumberPrefix) {
		this.mobileNumberPrefix = mobileNumberPrefix;
	}

	public String getFaxPrefix() {
		return faxPrefix;
	}

	public void setFaxPrefix(String faxPrefix) {
		this.faxPrefix = faxPrefix;
	}

	/*5506*/
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
	
/*5506*/
}
