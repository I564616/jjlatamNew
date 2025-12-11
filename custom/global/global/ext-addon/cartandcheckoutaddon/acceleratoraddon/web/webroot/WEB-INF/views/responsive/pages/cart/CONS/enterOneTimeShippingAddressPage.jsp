	<%@ taglib prefix="c" uri="jakarta.tags.core"%>
	<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
	<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
	<c:url value="/cart/saveOneTimeShipping" var="saveOneTimeShipping" />
	<!-- One Time Shipping Address Popup window Start-->
	
	<div class="lightboxtemplate" id="acc" >
		<form:form method="post" action="${saveOneTimeShipping}" id="oneTimeShippingAddressForm">
			<h2>Enter One Time Shipping Address</h2>
			<div class="marTop20 ttBlock oneTimeShippingAddress">
				<ul>
					<li>
						<div class="cell"><label for="name"> <label:message messageCode="cart.common.name"/> <span class="redStar">*</span></label></div>
						<div class="cell"><input type="text" name="name" id="name" class="required" data-msg-required="<spring:message code='cart.shippingaddress.name'/>"/></div>
						<div><div class="registerError"></div></div>
					</li>
					<li>
						<div class="cell"><label for="address1"><label:message messageCode="cart.common.address1"/><span class="redStar">*</span></label></div>
						<div class="cell"><input type="text" name="address1" id="address1" class="required" data-msg-required="<spring:message code='register.stepTwo.required.billAddressOne'/>"/></div>
						<div><div class="registerError"></div></div>
					</li>
					<li>
						<div class="cell"><label for="address1"><label:message messageCode="cart.common.address2"/></label></div>
						<div class="cell"><input type="text" name="address2" id="address2" /></div>
						<div><div class="registerError"></div></div>
					</li>
					<li>
						<div class="cell"><label for="city"><label:message messageCode="cart.common.city"/> <span class="redStar">*</span></label></div>
						<div class="cell"><input type="text" name="city" id="city" class="required" data-msg-required="<spring:message code='register.stepTwo.required.city'/>"/></div>
						<div><div class="registerError"></div></div>
					</li>
					<li>
						<div class="cell"><label for="state"><label:message messageCode="cart.common.state"/><span class="redStar">*</span></label></div>
						<div class="cell">
							<select id="state" class="required" name="state" data-msg-required="<spring:message code='register.stepTwo.required.state'/>">
							  <option value=""><spring:message code='signup.state.select'/></option>
							  <c:forEach items="${regionCodes}" var="code">
										<option value="${code.isocode}">${code.name}</option>
										</c:forEach>
							</select>
						</div>
						<div><div class="registerError"></div></div>
					</li>
					<li>
						<div class="cell"><label for="postalCode"><label:message messageCode="cart.common.postalCode"/><span class="redStar">*</span></label></div>
						<div class="cell"><input type="text" name="postalCode" id="postalCode" class="required digits" data-msg-required="<spring:message code='register.stepTwo.required.postalcode'/>"/></div>
						<div><div class="registerError"></div></div>
					</li>
					<!-- Start - Added for OS-164  Hema -->
					<li>
						<div class="cell"><label for="attnLine"><label:message messageCode="cart.common.attnLine"/></label></div>
						<div class="cell"><input type="text" name="attnLine" id="attnLine"  placeholder="<spring:message code='register.contactNamePhone'/>"/></div>
						<div><div class="registerError"></div></div>
					</li>
					<!-- End - Added for OS-164 Hema -->
				</ul>
			</div>			
			<div class='popupButtonWrapper txtRight'>
				<span class="floatLeft"><input type="button" onclick='parent.$.colorbox.close(); return false;' class="tertiarybtn" value="<spring:message code='cart.common.cancel'/>" /></span>
				<span><input type="submit" class='primarybtn' value="<spring:message code='cart.common.submit'/>"/></span>
			</div>               
		</form:form>
		</div>
	<!--  One Time Shipping Address Popup window End -->
	
	<!-- <div id="enterOneTimeShippingAddress" >
		<div class="lightboxtemplate" id="saveAsTemplate">
		<form action="${saveOneTimeShipping}" method="POST">
			<h2><label:message messageCode="cart.common.enterOneTimeShipToAddress"/></h2>
			<div class="sectionBlock body">
				<div><label:message messageCode="cart.common.name"/><span class="redStar">*</span>
				&nbsp;&nbsp;&nbsp;<span><input type="text" id="name" name="name" 
										value="${name}"></span></div>
				<div><label:message messageCode="cart.common.address1"/><span class="redStar">*</span>
				<span><input type="text" id="address1" name="address1" 
										value="${address1}"></span>
				</div>
				<div><label:message messageCode="cart.common.address2"/>
				<span><input type="text" id="address2" name="address2"
										value="${address2}"></span>
				</div>
				<div><label:message messageCode="cart.common.city"/><span class="redStar">*</span>
				<span><input type="text" id="city" name="city"
										value="${city}"></span>
				</div>
				<div><label:message messageCode="cart.common.state"/><span class="redStar">*</span>
				<span><input type="text" id="state" name="state" 
										value="${state}"></span>
				</div>
				<div><label:message messageCode="cart.common.postalCode"/><span class="redStar">*</span>
				<span><input type="text" id="postalCode" name="postalCode"
										value="${postalCode}"></span>
				</div>
			</div>
				<div class="okecancel ttBlock">					
					<div class='popupButtonWrapper txtRight'>
						<span class="floatLeft"><a
							onclick='parent.$.colorbox.close(); return false;' href="#"
							class="tertiarybtn"><label:message messageCode="cart.review.saveTemplate.cancel"/></a></span> 
						<span><input type="submit" class='saveOneTimeShippingAddress' value="OK"/></span>
					</div>					
				</div>
			</form>
		</div>
	</div>
	 -->