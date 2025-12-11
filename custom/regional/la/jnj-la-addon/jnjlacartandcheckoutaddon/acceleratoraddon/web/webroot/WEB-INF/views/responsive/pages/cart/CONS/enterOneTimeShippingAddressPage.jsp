	<%@ taglib prefix="c" uri="jakarta.tags.core"%>
	<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
	<c:url value="/cart/saveOneTimeShipping" var="saveOneTimeShipping" />
	<!-- One Time Shipping Address Popup window Start-->
	
	<div class="lightboxtemplate" id="acc" >
		<form method="post" action="${saveOneTimeShipping}" id="oneTimeShippingAddressForm">
			<h2>Enter One Time Shipping Address</h2>
			<div class="marTop20 ttBlock oneTimeShippingAddress">
				<ul>
					<li>
						<div class="cell"><label for="name"> <spring:message code="cart.common.name"/> <span class="redStar">*</span></label></div>
						<div class="cell"><input type="text" name="name" id="name" class="required" data-msg-required="Please Enter Name!"/></div>
						<div><div class="registerError"></div></div>
					</li>
					<li>
						<div class="cell"><label for="address1"><spring:message code="cart.common.address1"/><span class="redStar">*</span></label></div>
						<div class="cell"><input type="text" name="address1" id="address1" class="required" data-msg-required="Please Enter Address1!"/></div>
						<div><div class="registerError"></div></div>
					</li>
					<li>
						<div class="cell"><label for="address1"><spring:message code="cart.common.address2"/></label></div>
						<div class="cell"><input type="text" name="address2" id="address2" /></div>
						<div><div class="registerError"></div></div>
					</li>
					<li>
						<div class="cell"><label for="city"><spring:message code="cart.common.city"/> <span class="redStar">*</span></label></div>
						<div class="cell"><input type="text" name="city" id="city" class="required" data-msg-required="Please Enter City!"/></div>
						<div><div class="registerError"></div></div>
					</li>
					<li>
						<div class="cell"><label for="state"><spring:message code="cart.common.state"/><span class="redStar">*</span></label></div>
						<div class="cell">
							<select id="state" class="required" name="state" data-msg-required="Please enter State!">
							  <option value="">Select State</option>
							  <c:forEach items="${regionCodes}" var="code">
										<option value="${code.isocode}">${code.name}</option>
										</c:forEach>
							</select>
						</div>
						<div><div class="registerError"></div></div>
					</li>
					<li>
						<div class="cell"><label for="postalCode"><spring:message code="cart.common.postalCode"/><span class="redStar">*</span></label></div>
						<div class="cell"><input type="text" name="postalCode" id="postalCode" class="required digits" data-msg-required="Please Enter Postal Code!"/></div>
						<div><div class="registerError"></div></div>
					</li>
					<!-- Start - Added for OS-164  Hema -->
					<li>
						<div class="cell"><label for="attnLine"><spring:message code="cart.common.attnLine"/></label></div>
						<div class="cell"><input type="text" name="attnLine" id="attnLine"  placeholder="Enter Contact Name & Phone Number"/></div>
						<div><div class="registerError"></div></div>
					</li>
					<!-- End - Added for OS-164 Hema -->
				</ul>
			</div>			
			<div class='popupButtonWrapper txtRight'>
				<span class="floatLeft"><input type="button" onclick='parent.$.colorbox.close(); return false;' class="tertiarybtn" value="CANCEL" /></span>
				<span><input type="submit" class='primarybtn' value="SUBMIT"/></span>
			</div>               
		</form>
		</div>
	<!--  One Time Shipping Address Popup window End -->
	
	<!-- <div id="enterOneTimeShippingAddress" >
		<div class="lightboxtemplate" id="saveAsTemplate">
		<form action="${saveOneTimeShipping}" method="POST">
			<h2><spring:message code="cart.common.enterOneTimeShipToAddress"/></h2>
			<div class="sectionBlock body">
				<div><spring:message code="cart.common.name"/><span class="redStar">*</span>
				&nbsp;&nbsp;&nbsp;<span><input type="text" id="name" name="name" 
										value="${name}"></span></div>
				<div><spring:message code="cart.common.address1"/><span class="redStar">*</span>
				<span><input type="text" id="address1" name="address1" 
										value="${address1}"></span>
				</div>
				<div><spring:message code="cart.common.address2"/>
				<span><input type="text" id="address2" name="address2"
										value="${address2}"></span>
				</div>
				<div><spring:message code="cart.common.city"/><span class="redStar">*</span>
				<span><input type="text" id="city" name="city"
										value="${city}"></span>
				</div>
				<div><spring:message code="cart.common.state"/><span class="redStar">*</span>
				<span><input type="text" id="state" name="state" 
										value="${state}"></span>
				</div>
				<div><spring:message code="cart.common.postalCode"/><span class="redStar">*</span>
				<span><input type="text" id="postalCode" name="postalCode"
										value="${postalCode}"></span>
				</div>
			</div>
				<div class="okecancel ttBlock">					
					<div class='popupButtonWrapper txtRight'>
						<span class="floatLeft"><a
							onclick='parent.$.colorbox.close(); return false;' href="#"
							class="tertiarybtn"><spring:message code="cart.review.saveTemplate.cancel"/></a></span> 
						<span><input type="submit" class='saveOneTimeShippingAddress' value="OK"/></span>
					</div>					
				</div>
			</form>
		</div>
	</div>
	 -->