<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>



<div id="changeAddress" class="hide" >    
			<div class="lightboxtemplate" >	
			<h2><spring:message code="cart.review.select.shippingAddress"/></h2>
				<div class="ttBlock">
				<c:url value="/cart/updateShippingAddress" var="updateShippingAdd" />
				<form id="changeAddForm" method="post" action="${updateShippingAdd}">
					<div class="sectionBlock body">
						<div class="ttHead">				
							<div class="hcolumn1"><spring:message code="change.address.select"/></div>
							<div class="hcolumn3"><spring:message code="change.address.shipTo"/></div>
							<div class="hcolumn2"><spring:message code="profile.myprofile.address"/></div>
							<div class="hcolumn3"><spring:message code="profile.myprofile.city"/> &amp; <spring:message code="change.address.state"/></div>
							<div class="hcolumn4"><spring:message code="change.address.zip"/></div>
						</div>
						<div class="ttBody">
							<c:forEach var="shippingAddress" items="${shippingAddressess}" varStatus="count">						
								<div class="ttRow">
									<div class="column1"><input type="radio" name="shippingAddress" value="${shippingAddress.id}"> </div>
									<div class="column3 labelText">${shippingAddress.companyName}</div>
									<div class="column2"><label for="${shippingAddress.id}">${shippingAddress.companyName},${shippingAddress.line1},${shippingAddress.line2}</label></div>
									<!-- <div class="column3 labelText">${shippingAddress.country.name}</div> -->
									<div class="column3 labelText">${shippingAddress.town}</div>
									<div class="column4 labelText">${shippingAddress.postalCode}</div>
								</div>
							</c:forEach	>						
						</div>
					</div>
				<div id="changeAddressButton" class='popupButtonWrapper txtRight'>
				<span class="floatLeft" id="closeChangeAddress"><input type="button"
						class="tertiarybtn closePopup" value="<spring:message code="cart.review.close"/>"/></span>
						<span id="okChangeAddress"><input id="laSubmitChangeAddress"
						type="button" class="primarybtn" value="<spring:message code="cart.review.ok"/>"></span>
				</div>               
			</form>
			</div>
		</div>
	</div>