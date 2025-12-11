<%@ tag trimDirectiveWhitespaces="true"%>
<%@ attribute name="className" required="true" type="java.lang.String"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="template"
	tagdir="/WEB-INF/tags/addons/loginaddon/responsive/template"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/responsive/nav"%>
<%@ taglib prefix="pagination"
	tagdir="/WEB-INF/tags/responsive/nav/pagination"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common"%>
<%@ taglib prefix="breadcrumb"
	tagdir="/WEB-INF/tags/responsive/nav/breadcrumb"%>
<%@ taglib prefix="formElement" tagdir="/WEB-INF/tags/responsive/form"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common"%>
<%@ taglib prefix="footer"
	tagdir="/WEB-INF/tags/responsive/common/footer"%>
<%@ taglib prefix="label"
	uri="/WEB-INF/tld/addons/loginaddon/message.tld"%>


<div class="Company-Information-block1">
	<div class="row">
		<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 ">
			<label for="accountName"  class="getErrorMessage" data="<spring:message code="register.stepTwo.required.account"/>"><spring:message code="register.stepTwo.accountName"/> <span class="redStar"><spring:message code="register.stepOne.required.star"/></span> </label>
		</div>
		<div class="col-lg-5 col-md-5 col-sm-5 col-xs-12 company-name-txtbox ">
				<form:input type="text"  autocomplete="off" path="accountName" name="accountName" id="accountName" maxlength = '50'
						class="comboInput ${className} form-control" />
		</div>
		 <div class="cellReg col-lg-3 col-md-3 col-sm-3 col-xs-12 "><div class="registerError"></div></div>
	</div>
	<div class="row">
		<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 margintop"><spring:message code="register.stepTwo.gln"/></div>
		<div class="col-lg-5 col-md-5 col-sm-5 col-xs-12">
			<form:input type="text"  autocomplete="off" path="gLN" name="globalLocNo" id="profile-glno globalLocNo" class="form-control margintop comboInput digits" />	
		</div>
		  <div class="cellReg col-lg-3 col-md-3 col-sm-3 col-xs-12 margintop"><div class="registerError"></div></div>
	</div>
	<div class="row">
		<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 margintop">
			<label for="bussType"  class="getErrorMessage " data="<spring:message code='register.stepTwo.required.businessType'/>"><spring:message code="register.stepTwo.businessType"/> <span class="redStar"><spring:message code="register.stepOne.required.star"/></span> </label>                          
		</div>
		<div class="col-lg-5 col-md-5 col-sm-5 col-xs-12">
			<form:select path="typeOfBusiness" id="bussType" name="bussType" class="${className} selectpicker margintop required" data-width="100%" data-msg-required="<spring:message code='register.enter.businessType'/>">
									<form:option value="" ><spring:message code='register.stepTwo.required.Select.text'/></form:option>
									<c:if test="${not empty businessType}">
									<form:options items="${businessType}"/>
									</c:if>
			</form:select>	
		</div>
		 <div class="cellReg col-lg-3 col-md-3 col-sm-3 col-xs-12 margintop"><div class="registerError"></div></div>
	</div>
	<div class="row">
			<label class="col-lg-4 col-md-4 col-sm-4 col-xs-12 margintop" for="subsidiary"><spring:message code="register.stepTwo.subsidiary"/></label>      
		<div class="col-lg-5 col-md-5 col-sm-5 col-xs-12">
				<form:input type="text"  autocomplete="off" path="subsidiaryOf" name="subsidiary" id="profile-subsidiary subsidiary" class="form-control margintop comboInput" />
		</div>
	</div>
</div>


<div class="Company-Information-block1">
	<div style="color: #0a8caa;">
		<strong><spring:message code="register.stepTwo.billToHeader"/></strong>
	</div>
	<div class="row">
			<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 margintop">
				<label for="billCountry1" class="getErrorMessage" data="<spring:message code='register.stepTwo.required.country'/>"><spring:message code="register.stepTwo.country"/> <span class="redStar"><spring:message code="register.stepOne.required.star"/></span> </label>
			</div>
		<div class="col-lg-5 col-md-5 col-sm-5 col-xs-12">
			<form:select id="billCountry1" name="billCountry1" data-width="100%" class="selectpicker margintop ${className}" data-msg-required="<spring:message code='register.stepTwo.required.country'/>" path="billToCountry">
									<form:option value=""><spring:message code="register.stepTwo.required.Selectcountry"/></form:option>
									<c:if test="${not empty countryList}">
								  	<form:options itemValue="isocode" itemLabel="name" items="${countryList}"/>
									</c:if>
			</form:select>
		</div>
		 <div class="cellReg col-lg-3 col-md-3 col-sm-3 col-xs-12 margintop"><div class="registerError"></div></div>
	</div>
	<div class="row">
		<label for="billAddress1"  class="col-lg-4 col-md-4 col-sm-4 col-xs-12 margintop getErrorMessage" data="<spring:message code='register.stepTwo.required.billAddressOne'/>"><spring:message code="register.stepTwo.billAddressOne"/> <span class="redStar"><spring:message code="register.stepOne.required.star"/></span> </label>
		<div class="col-lg-5 col-md-5 col-sm-5 col-xs-12">
				<form:input type="text"  autocomplete="off"  name="billAddress1" id="billAddress1" class="form-control margintop ${className}" data-msg-required="<spring:message code='register.stepTwo.required.billAddressOne'/>" path="billToLine1"/>
		</div>
		 <div class="cellReg col-lg-3 col-md-3 col-sm-3 col-xs-12 margintop"><div class="registerError"></div></div>
	</div>
	<div class="row">
			<label class="col-lg-4 col-md-4 col-sm-4 col-xs-12 margintop" for="billAddress2"><spring:message code="register.stepTwo.billAddressTwo"/></label>        
		<div class="col-lg-5 col-md-5 col-sm-5 col-xs-12">
			<form:input type="text" class="form-control margintop"  autocomplete="off" path="billToLine2" name="billAddress2" id="billAddress2" />	
		</div>
	</div>
	<div class="row">
		<label for="billCity"  class="col-lg-4 col-md-4 col-sm-4 col-xs-12 margintop getErrorMessage" data="<spring:message code='register.stepTwo.required.city'/>"><spring:message code="register.stepTwo.city"/><span class="redStar"><spring:message code="register.stepOne.required.star"/></span></label>
		<div class="col-lg-5 col-md-5 col-sm-5 col-xs-12">
				<form:input type="text"  autocomplete="off" path="billToCity" name="billCity" style="width: 100%" id="billCity" class="form-control margintop ${className}" data-msg-required="<spring:message code='register.stepTwo.required.city'/>"/>
		</div>
		 <div class="cellReg col-lg-3 col-md-3 col-sm-3 col-xs-12 margintop"><div class="registerError"></div></div>
	</div>
	<div class="row">
		<label for="billToState"  class="col-lg-4 col-md-4 col-sm-4 col-xs-12 margintop getErrorMessage" data="<spring:message code='register.stepTwo.required.state'/>"><spring:message code='register.stepTwo.state'/><span class="redStar"><spring:message code="register.stepOne.required.star"/> </span></label>
		<div class="col-lg-5 col-md-5 col-sm-5 col-xs-12">
				<form:input id="billToState" path="billToState" name="billState" class="form-control margintop ${className}" style="width: 100%"  data-msg-required="<spring:message code='register.stepTwo.required.state'/>"/>
							
				
		</div>
		  <div class="cellReg col-lg-3 col-md-3 col-sm-3 col-xs-12 margintop"><div class="registerError"></div></div>
	</div>
	
	<div class="hide noUsShipState">
	 <div class="cellReg">
                                <label for="billState"> <spring:message code='register.stepTwo.state'/></label>                              
                            <form:input type="text" disabled="true" autocomplete="off" path="billToState" name="billToState" id="noUsBillState" class="${className}" />
                            	
            </div> 
    </div>
	<div class="row">
		
		<label for="billToZipCode"  class="col-lg-4 col-md-4 col-sm-4 col-xs-12 margintop getErrorMessage" data="<spring:message code='register.stepTwo.required.zipcode'/>"><spring:message code='register.stepTwo.zipcode'/> <span class="redStar"><spring:message code="register.stepOne.required.star"/> </span></label>
		<div class="col-lg-5 col-md-5 col-sm-5 col-xs-12">
				<form:input type="text"  autocomplete="off" path="billToZipCode" name="billToZipCode" id="billPostalCode" class="form-control alphanumeric margintop ${className}" data-msg-required="<spring:message code='register.stepTwo.required.postalcode'/>"/>
		</div>
		 <div class="cellReg col-lg-3 col-md-3 col-sm-3 col-xs-12 margintop"><div class="registerError"></div></div>
	</div>
</div>


<div class="Company-Information-block1">
	<div class="row">
		<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12"
			style="color: #0a8caa;">
			<strong><spring:message code='register.stepTwo.shipToHeader'/></strong>
		</div>
		<div
			class="col-lg-8 col-md-8 col-sm-8 col-xs-12 checkbox checkbox-info ship-to-check">
			<%-- <input id="same-bill" class="styled" type="checkbox"> 
			<label for="same-bill"> Same as Bill-To Location </label> --%>
				<input type="checkbox" id="billToLocation" />  <label class="bill" for="billToLocation"><spring:message code='register.stepTwo.same.as.billto'/> </label>
		</div>
	</div>
	<div class="row">
		<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 margintop">
			<label	for="shipCountry" class="getErrorMessage" data="<spring:message code='register.stepTwo.required.country'/>"><spring:message code="register.stepTwo.country"/>
			<span class="redStar"><spring:message code="register.stepOne.required.star"/></span> </label>
		</div>
		<div class="col-lg-5 col-md-5 col-sm-5 col-xs-12">
			<form:select id="shipCountry" name="shipToCountry" class="selectpicker margintop ${className}" data-width="100%" data-msg-required="Please enter Country!" path="shipToCountry">
									<form:option value=""><spring:message code="register.stepTwo.required.Selectcountry"/></form:option>
								  	<form:options itemValue="isocode" itemLabel="name" items="${countryList}"/>
			</form:select>
		</div>
		<div class="cellReg col-lg-3 col-md-3 col-sm-3 col-xs-12 margintop"><div class="registerError"></div></div>
    </div>
	<div class="row">
		<label for="shipAddress1"  class="col-lg-4 col-md-4 col-sm-4 col-xs-12 margintop getErrorMessage" data="<spring:message code='register.stepTwo.required.billAddressOne'/>"><spring:message code='register.stepTwo.shipAddressOne'/><span class="redStar"><spring:message code="register.stepOne.required.star"/></span> </label>
		<div class="col-lg-5 col-md-5 col-sm-5 col-xs-12">
				<form:input type="text" autocomplete="off" path="shipToLine1" name="shipAddress1" id="shipAddress1" class="form-control margintop ${className}" data-msg-required="<spring:message code='register.stepTwo.required.address'/>"/>
		</div>
		 <div class="cellReg col-lg-3 col-md-3 col-sm-3 col-xs-12 margintop"><div class="registerError"></div></div>
	</div>
	<div class="row">
			<label for="shipAddress2" class="col-lg-4 col-md-4 col-sm-4 col-xs-12 margintop"><spring:message code='register.stepTwo.shipAddressTwo'/></label>  
		<div class="col-lg-5 col-md-5 col-sm-5 col-xs-12">
				<form:input type="text" class="form-control margintop" autocomplete="off" path="shipToLine2" name="shipAddress2" id="shipAddress2" />
		</div>
	</div>
	<div class="row">
		<label for="shipCity"  class="col-lg-4 col-md-4 col-sm-4 col-xs-12 margintop getErrorMessage" data="<spring:message code='register.stepTwo.required.city'/>"><spring:message code="register.stepTwo.city"/> <span class="redStar"><spring:message code="register.stepOne.required.star"/></span></label>
		<div class="col-lg-5 col-md-5 col-sm-5 col-xs-12">
				<form:input type="text"  autocomplete="off" path="shipToCity" name="shipCity" id="shipCity" class="form-control margintop comboInput ${className}" data-msg-required="<spring:message code='profile.myprofile.cityError'/>" style="width: 100%"/>
		</div>
		 <div class="cellReg col-lg-3 col-md-3 col-sm-3 col-xs-12 margintop"><div class="registerError"></div></div>
	</div>
	<div class="row">
		<label for="shipToState"  class="col-lg-4 col-md-4 col-sm-4 col-xs-12 margintop getErrorMessage" data="<spring:message code='register.stepTwo.required.state'/>"><spring:message code='register.stepTwo.state'/><span class="redStar"><spring:message code="register.stepOne.required.star"/></span></label>
		<div class="col-lg-5 col-md-5 col-sm-5 col-xs-12">
				<form:input id="shipToState" path="shipToState" name="shipState" class="required form-control margintop ${className}" style="width: 100%" data-msg-required="<spring:message code='register.state'/>" />
									
				
		</div>
		 <div class="cellReg col-lg-3 col-md-3 col-sm-3 col-xs-12 margintop"><div class="registerError"></div></div>
	</div>
	
	<div class="hide noUsShipState">
	 <div class="cellReg">
                                <label for="shipState" ><spring:message code='register.stepTwo.state'/></label>                                
                                <form:input type="text" disabled="true" autocomplete="off" path="shipToState" name="shipToState" id="noUsShipState" class="${className}" />
                                                      
                            </div>
              </div>              
	<div class="row">
		<label for="shipToZipCode"  class="col-lg-4 col-md-4 col-sm-4 col-xs-12 margintop getErrorMessage" data="<spring:message code='register.stepTwo.required.zipcode'/>"><spring:message code='register.stepTwo.zipcode'/> <span class="redStar"><spring:message code="register.stepOne.required.star"/></span></label>
		<div class="col-lg-5 col-md-5 col-sm-5 col-xs-12">
				 <form:input type="text"  autocomplete="off" path="shipToZipCode" name="shipToZipCode" id="shipPostalCode" class="form-control shipToCountry alphanumeric margintop ${className}" data-msg-required="<spring:message code='register.zipcode'/>"/>
		</div>
		 <div class="cellReg col-lg-3 col-md-3 col-sm-3 col-xs-12 margintop"><div class="registerError"></div></div>
	</div>
</div>


<div class="Company-Information-block1">
	<div><spring:message code='register.stepTwo.taxtext'/></div>
	<div class="margintop10">
		<div class="radio radio-info radio-inline">
				<form:radiobutton path="salesAndUseTaxFlag" value="true" name="yes" checked="checked" id="yes" class="${className}" data-msg-required="" />
                <label class="bill" for="yes"  class="getErrorMessage" data="<spring:message code='register.stepTwo.required.taxtext'/>"><spring:message code='register.stepTwo.yes'/></label>
		</div>
		<div class="radio radio-info radio-inline">
			<form:radiobutton name="no" id="no" path="salesAndUseTaxFlag" value="false"/>
			<label class="bill" for="no"><spring:message code='register.stepTwo.no'/></label>	
		</div>
		<div class="width280"><div class="registerError"></div></div>
		<p class="selNo" style="display:none;"><spring:message code="register.stepTwo.notice.text"/></p>
	</div>
	

	<div class="margintop">
				<label for="estAmount" class="getErrorMessage" data="<spring:message code='register.initial.order'/>"><spring:message code="register.stepTwo.opening.order"/> 
					<span class="redStar"><spring:message code="register.stepOne.required.star"/></span> 
				</label>
	</div>
	<div class="row">
		
		<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
			<form:select id="estAmount" path="initialOpeningOrderAmount" name="estAmount" class="selectpicker margintop10 widthSel ${className}" data-width="100%" data-msg-required="<spring:message code='register.initial.order'/>" >
									<option value=""><spring:message code='register.stepTwo.required.Select.text'/></option>
									<c:if test="${not empty estimatedAmount}">
									<form:options items="${estimatedAmount}"/>
									</c:if>
			</form:select>
			
		</div>
		<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 profile-note">
			<div><spring:message code="register.stepTwo.opening.note"/></div>
		</div>

		<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 cellReg width280" style="margin-top:5px"><div class="registerError"></div></div> 
	</div>
	<div class="row">
		<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 margintop10">
			<div><spring:message code="register.stepTwo.estimatedamount"/><span class="redStar"><spring:message code="register.stepOne.required.star"/></span></div>
		</div>
		<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 margintop10">
		<label style="display:none" for="estimatedAmountPerYear"  class="getErrorMessage" data="<spring:message code='register.stepTwo.required.estimatedamount'/>"></label>
			 <form:input path="estimatedAmountPerYear"  autocomplete="off"  id="signup-ename-pyear estimatedAmountPerYear" name="estimatedAmountPerYear" class="form-control ${className}" data-msg-required="<spring:message code='register.amount'/>"/>
		</div>
		<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 cellReg" style="margin-top:18px"><div class="registerError"></div></div>
	</div>
	
	<div class="margintop">
		<spring:message code="register.stepTwo.products"/><span class="redStar"><spring:message code="register.stepOne.required.star"/></span>
	</div>
	<div class="row product-block">
		<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
		<label for="medicalProductsPurchase" class="getErrorMessage" data="<spring:message code='profile.myprofile.productpurchased'/>"></label>
			 <div class="checkbox checkbox-info checkboxmargin margintop10 liststyle">
				
					
					<c:if test="${not empty purchaseProduct}">
									<!-- for loop -->							
					 <form:checkboxes items="${purchaseProduct}" id="product-1 medicalProductsPurchase" path="medicalProductsPurchase"  element="li" cssClass="${className}" data-msg-required="" />
					
			</c:if>
			<div class="cellReg width450" id="error-msg-chck"><div class="registerError"></div></div>
			
			</div>
				
		</div>
		
	</div>
</div>




