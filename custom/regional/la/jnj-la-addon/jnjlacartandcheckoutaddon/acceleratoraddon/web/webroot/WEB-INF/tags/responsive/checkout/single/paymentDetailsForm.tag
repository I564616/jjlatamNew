<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="formUtil" tagdir="/WEB-INF/tags/responsive/form" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template" %>

<spring:url value="/checkout/single/summary/createUpdatePaymentDetails.json" var="createUpdatePaymentUrl"/>

<div class="title_holder">
    <div class="title">
        <div class="title-top">
            <span></span>
        </div>
    </div>
    <h2><spring:theme code="checkout.summary.paymentMethod.addEditPaymentDetails.header"/></h2>
</div>
<div class="item_container">
    <c:if test="${not empty paymentInfoData }">
		<span class="saved_card">
			<button class="form left use_saved_card_button"><spring:theme code="checkout.summary.paymentMethod.addEditPaymentDetails.useSavedCard"/></button>
			<p><spring:theme code="checkout.summary.paymentMethod.addEditPaymentDetails.useSavedCard.description"/></p>
		</span>
    </c:if>
    <form:form method="post" modelAttribute="paymentDetailsForm" action="${createUpdatePaymentUrl}" class="create_update_payment_form">
        <common:globalMessages/>
        <div class="payment_details_left_col">
            <h1><spring:theme code="checkout.summary.paymentMethod.addEditPaymentDetails.paymentCard"/></h1>

            <p><spring:theme code="checkout.summary.paymentMethod.addEditPaymentDetails.enterYourCardDetails"/></p>

            <p><spring:theme code="form.required"/></p>


            <form:hidden path="paymentId" class="create_update_payment_id" status="${not empty createUpdateStatus ? createUpdateStatus : ''}"/>
            <formUtil:formSelectBox idKey="cardType" labelKey="payment.cardType" path="cardTypeCode" mandatory="true" skipBlank="false" skipBlankMessageKey="payment.cardType.pleaseSelect" items="${cardTypes}" tabindex="1"/>
            <formUtil:formInputBox idKey="nameOnCard" labelKey="payment.nameOnCard" path="nameOnCard" inputCSS="text" mandatory="true" tabindex="2"/>
            <formUtil:formInputBox idKey="cardNumber" labelKey="payment.cardNumber" path="cardNumber" inputCSS="text" mandatory="true" tabindex="3"/>

			<fieldset id="startDate" class="cardDate">
				<legend><spring:theme code="payment.startDate"/></legend>
				<formUtil:formSelectBox idKey="StartMonth" labelKey="payment.month" path="startMonth" mandatory="true" skipBlank="false" skipBlankMessageKey="" items="${months}" tabindex="4"/>
				<formUtil:formSelectBox idKey="StartYear" labelKey="payment.year" path="startYear" mandatory="true" skipBlank="false" skipBlankMessageKey="" items="${startYears}" tabindex="5"/>
			</fieldset>

			
			<fieldset id="endDate" class="cardDate">
				<legend><spring:theme code="payment.expiryDate"/></legend>
				<formUtil:formSelectBox idKey="ExpiryMonth" labelKey="payment.month" path="expiryMonth" mandatory="true" skipBlank="false" skipBlankMessageKey="" items="${months}" tabindex="6"/>
				<formUtil:formSelectBox idKey="ExpiryYear" labelKey="payment.year" path="expiryYear" mandatory="true" skipBlank="false" skipBlankMessageKey="" items="${expiryYears}" tabindex="7"/>
			</fieldset>	
            <div id="issueNum">
                <formUtil:formInputBox idKey="issueNumber" labelKey="payment.issueNumber" path="issueNumber" inputCSS="text" mandatory="false" tabindex="8"/>
            </div>

        </div>

        <div class="payment_details_right_col">

            <h1><spring:theme code="checkout.summary.paymentMethod.addEditPaymentDetails.billingAddress"/></h1>
            <c:if test="${!edit}">
                <p><spring:theme code="checkout.summary.paymentMethod.addEditPaymentDetails.billingAddressDiffersFromDeliveryAddress"/></p>

                <div class="form_field-label checkbox-label">
                    <label for="differentAddress" class="checkbox-label"><spring:theme code="checkout.summary.paymentMethod.addEditPaymentDetails.enterDifferentBillingAddress"/></label>
                </div>
                <div class="form_field-input">
                    <form:checkbox id="differentAddress" class="checkbox-input" path="newBillingAddress" tabindex="9"/>
                </div>

            </c:if>

            <p><spring:theme code="form.required"/></p>

            <div id="newBillingAddressFields" data-edit-mode="${edit}">
                <form:hidden path="billingAddress.addressId" class="create_update_address_id" status="${not empty createUpdateStatus ? createUpdateStatus : ''}"/>
                <formUtil:formSelectBox idKey="address.title" labelKey="address.title" path="billingAddress.titleCode" mandatory="true" skipBlank="false" skipBlankMessageKey="address.title.pleaseSelect" items="${titles}" tabindex="10"/>
                <formUtil:formInputBox idKey="address.firstName" labelKey="address.firstName" path="billingAddress.firstName" inputCSS="text" mandatory="true" tabindex="11"/>
                <formUtil:formInputBox idKey="address.surname" labelKey="address.surname" path="billingAddress.lastName" inputCSS="text" mandatory="true" tabindex="12"/>
                <formUtil:formInputBox idKey="address.line1" labelKey="address.line1" path="billingAddress.line1" inputCSS="text" mandatory="true" tabindex="13"/>
                <formUtil:formInputBox idKey="address.line2" labelKey="address.line2" path="billingAddress.line2" inputCSS="text" mandatory="false" tabindex="14"/>
                <formUtil:formInputBox idKey="address.townCity" labelKey="address.townCity" path="billingAddress.townCity" inputCSS="text" mandatory="true" tabindex="15"/>
                <formUtil:formInputBox idKey="address.postcode" labelKey="address.postcode" path="billingAddress.postcode" inputCSS="text" mandatory="true" tabindex="16"/>
                <formUtil:formSelectBox idKey="address.country" labelKey="address.country" path="billingAddress.countryIso" mandatory="true" skipBlank="false" skipBlankMessageKey="address.selectCountry" items="${billingCountries}" itemValue="isocode" tabindex="17"/>
                <form:hidden path="billingAddress.shippingAddress"/>
                <form:hidden path="billingAddress.billingAddress"/>
            </div>
        </div>
        <div class="save_payment_details">

            <div class="form_field-label checkbox-label">
                <label for="SaveDetails" class="checkbox-label"><spring:theme code="checkout.summary.paymentMethod.addEditPaymentDetails.savePaymentDetailsInAccount"/></label>
            </div>
            <div class="form_field-input">
                <form:checkbox id="SaveDetails" class="checkbox-input" path="saveInAccount" tabindex="18"/>
            </div>

            <ycommerce:testId code="checkout_useThesePaymentDetails_button">
                <button type="submit" class="form left use_these_payment_details_button show_processing_message" tabindex="19" id="lastInTheForm">
                    <spring:theme code="${edit ? 'checkout.summary.paymentMethod.addEditPaymentDetails.saveAndUseThesePaymentDetails' : 'checkout.summary.paymentMethod.addEditPaymentDetails.useThesePaymentDetails'}"/>
                </button>
            </ycommerce:testId>
        </div>
    </form:form>
</div>
