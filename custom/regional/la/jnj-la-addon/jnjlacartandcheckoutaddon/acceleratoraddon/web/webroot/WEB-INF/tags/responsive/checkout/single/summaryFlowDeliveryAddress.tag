<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="deliveryAddress" required="true" type="de.hybris.platform.commercefacades.user.data.AddressData" %>
<%@ attribute name="costCenter" required="true" type="de.hybris.platform.b2bacceleratorfacades.order.data.B2BCostCenterData" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="checkout" tagdir="/WEB-INF/tags/responsive/checkout/single" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>

<spring:url value="/checkout/single/summary/getDeliveryAddresses.json" var="getDeliveryAddressesUrl" />
<spring:url value="/checkout/single/summary/setDeliveryAddress.json" var="setDeliveryAddressUrl" />
<spring:url value="/checkout/single/summary/getDeliveryAddressForm.json" var="getDeliveryAddressFormUrl" />
<spring:url value="/checkout/single/summary/setDefaultAddress.json" var="setDefaultAddressUrl" />
<spring:url value="/_ui/responsive/common/images/spinner.gif" var="spinnerUrl" />

<script type="text/javascript"> // set vars
	var getDeliveryAddressesUrl = '${getDeliveryAddressesUrl}';
	var getDeliveryAddressFormUrl = '${getDeliveryAddressFormUrl}';
	var setDeliveryAddressUrl = '${setDeliveryAddressUrl}';
	var setDefaultAddressUrl = '${setDefaultAddressUrl}';
	var spinnerUrl = '${spinnerUrl}';
</script>

<script id="deliveryAddressSummaryTemplate" type="text/x-jquery-tmpl">
	<div class="checkout_summary_flow_c {{if deliveryAddress}}complete{{/if}}" id="checkout_summary_deliveryaddress_div">
		<div class="item_container_holder">
			<div class="title_holder">
				<div class="title">
					<div class="title-top">
						<span></span>
					</div>
				</div>
				<h2><spring:theme code="checkout.summary.deliveryAddress.header" htmlEscape="false"/><span></span></h2>
			</div>

			<div class="item_container">
				<ul>
					{{if deliveryAddress}}
						<li>{{= deliveryAddress.title}}&nbsp;{{= deliveryAddress.firstName}}&nbsp;{{= deliveryAddress.lastName}}</li>
						<li>{{= deliveryAddress.line1}}</li>
						<li>{{= deliveryAddress.line2}}</li>
						<li>{{= deliveryAddress.town}}</li>
						<li>{{= deliveryAddress.postalCode}}</li>
					{{else}}
						<li><spring:theme code="checkout.summary.deliveryAddress.noneSelected"/></li>
					{{/if}}
				</ul>
			</div>
		</div>

		{{if deliveryAddress}}
				<ycommerce:testId code="checkout_changeAddress_element">
				<a href="#" class="edit_complete change_address_button" data-address-id="{{= deliveryAddress.id}}"><spring:theme code="checkout.summary.deliveryAddress.editDeliveryAddressButton"/></a>
				</ycommerce:testId>
		{{else}}
				<ycommerce:testId code="checkout_changeAddress_element">
				<button class="form change_address_button" data-address-id=""><spring:theme code="checkout.summary.deliveryAddress.enterDeliveryAddressButton"/></button>
				</ycommerce:testId>
		{{/if}}
	</div>
</script>



<script id="deliveryAddressesTemplate" type="text/x-jquery-tmpl">
	{{if !addresses.length}}
		<spring:theme code="checkout.summary.deliveryAddress.noExistingAddresses"/>
	{{/if}}
	{{if addresses.length}}
		<form>
			{{each addresses}}
				<div class="existing_address">
					<div class="left">
						<div class="checkmark">
							{{if defaultAddress}}
								<theme:image code="img.iconSelected" alt="Selected"/>
							{{/if}}
						</div>
						{{if $value.editable}}
							<button class="form left edit" data-address-id="{{= $value.id}}"><spring:theme code="checkout.summary.deliveryAddress.edit"/></button>
						{{/if}}
					</div>
					<ul>
						<li>{{= $value.title}}&nbsp;{{= $value.firstName}}&nbsp;{{= $value.lastName}}</li>
						<li>{{= $value.line1}}</li>
						<li>{{= $value.line2}}</li>
						<li>{{= $value.town}}</li>
						<li>{{= $value.postalCode}}</li>
					</ul>
					{{if !(defaultAddress) && $value.editable}}
						<button class="form right default" data-address="{{= $value.id}}"><spring:theme code="text.setDefault" text="Set as default" /></button>
					{{/if}}
					<button class="positive right pad_left use_address" data-address-id="{{= $value.id}}"><spring:theme code="checkout.summary.deliveryAddress.useThisAddress"/></button>
				</div>
			{{/each}}
		</form>
	{{/if}}
</script>

<c:set value="${not empty deliveryAddress}" var="deliveryAddressOk"/>
<div class="checkout_summary_flow_c ${deliveryAddressOk ? 'complete' : ''}" id="checkout_summary_deliveryaddress_div">
	<div class="item_container_holder">
		<ycommerce:testId code="checkout_deliveryAddressData_text">
			<div class="title_holder">
				<div class="title">
					<div class="title-top">
						<span></span>
					</div>
				</div>
				<h2><spring:theme code="checkout.summary.deliveryAddress.header" htmlEscape="false"/><span></span></h2>
			</div>

			<div class="item_container">
				<ul>
					<c:choose>
						<c:when test="${deliveryAddressOk}">
							<li>${fn:escapeXml(deliveryAddress.title)}&nbsp;${fn:escapeXml(deliveryAddress.firstName)}&nbsp;${fn:escapeXml(deliveryAddress.lastName)}</li>
							<li>${fn:escapeXml(deliveryAddress.line1)}</li>
							<li>${fn:escapeXml(deliveryAddress.line2)}</li>
							<li>${fn:escapeXml(deliveryAddress.town)}</li>
							<li>${fn:escapeXml(deliveryAddress.postalCode)}</li>
							<li>${fn:escapeXml(deliveryAddress.country.name)}</li>
						</c:when>
						<c:otherwise>
							<li><spring:theme code="checkout.summary.deliveryAddress.noneSelected"/></li>
						</c:otherwise>
					</c:choose>
				</ul>
			</div>
		</ycommerce:testId>
	</div>

	<ycommerce:testId code="checkout_changeAddress_element">
		<c:choose>
			<c:when test="${deliveryAddressOk}">
				<a href="#" class="edit_complete change_address_button" data-address-id="${deliveryAddress.id}"><spring:theme code="checkout.summary.deliveryAddress.editDeliveryAddressButton"/></a>
			</c:when>
			<c:otherwise>
				<button class="form change_address_button" data-address-id="${deliveryAddress.id}"><spring:theme code="checkout.summary.deliveryAddress.enterDeliveryAddressButton"/></button>
			</c:otherwise>
		</c:choose>
	</ycommerce:testId>
</div>

<checkout:deliveryAddressPopup />
