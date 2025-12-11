<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="deliveryMode" required="true" type="de.hybris.platform.commercefacades.order.data.DeliveryModeData" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>

<spring:url value="/checkout/single/summary/getDeliveryModes.json" var="getDeliveryModesUrl"/>
<spring:url value="/checkout/single/summary/setDeliveryMode.json" var="setDeliveryModeUrl"/>
<script type="text/javascript"> // set vars
var getDeliveryModesUrl = '${getDeliveryModesUrl}';
var setDeliveryModeUrl = '${setDeliveryModeUrl}';
</script>

<script id="deliveryModeSummaryTemplate" type="text/x-jquery-tmpl">

	<div class="checkout_summary_flow_c {{if deliveryMode}}complete{{/if}}" id="checkout_summary_deliverymode_div">
		<div class="item_container_holder">
			<div class="title_holder">
				<div class="title">
					<div class="title-top">
						<span></span>
					</div>
				</div>
				<h2>
					<spring:theme code="checkout.summary.deliveryMode.header" htmlEscape="false"/>
					<span></span></h2>
			</div>
			<div class="item_container">
				<ul>
					{{if deliveryMode}}
					<li>{{= deliveryMode.name}}&nbsp;({{= deliveryMode.code}})</li>
					<li>{{= deliveryMode.description}}&nbsp;{{if deliveryMode.deliveryCost}}&nbsp;-&nbsp;{{= deliveryMode.deliveryCost.formattedValue}}{{/if}}</li>
					{{else}}
					<li>
						<spring:theme code="checkout.summary.deliveryMode.noneSelected"/>
					</li>
					{{/if}}
				</ul>
			</div>
		</div>
		{{if deliveryMode}}
		<ycommerce:testId code="checkout_changeDeliveryMode_element">
			<a href="#" class="edit_complete change_mode_button">
				<spring:theme code="checkout.summary.deliveryMode.editDeliveryMethod"/>
			</a>
		</ycommerce:testId>
		{{else}}
		<ycommerce:testId code="checkout_changeDeliveryMode_element">
			<button class="form change_mode_button">
				<spring:theme code="checkout.summary.deliveryMode.editDeliveryMethod"/>
			</button>
		</ycommerce:testId>
		{{/if}}
	</div>

</script>

<script id="deliveryModesTemplate" type="text/x-jquery-tmpl">

	{{if !deliveryModes.length}}
	<spring:theme code="text.checkout.noDeliveryModes"/>
	{{/if}}
	{{if deliveryModes.length}}
	{{each deliveryModes}}

	<div class="form_field-container">
		<div class="form_field-label select">
			<label for='{{= $value.code}}'>{{= $value.name}}&nbsp;-&nbsp;{{= $value.description}}&nbsp;-&nbsp;{{= $value.deliveryCost.formattedValue}}</label>
		</div>
		<div class="form_field-input select">
			{{if $index == 0}}
			<input type="radio" name="delivery" value="{{= $value.code}}" id="{{= $value.code}}" checked="checked"/>
			{{else}}
			<input type="radio" name="delivery" value="{{= $value.code}}" id="{{= $value.code}}"/>
			{{/if}}
		</div>
	</div>
	{{/each}}
	{{/if}}

</script>

<script id="deliveryModeButton" type="text/x-jquery-tmpl">
	{{if deliveryModes.length}}
	<ycommerce:testId code="checkout_chooseSelectedDeliveryMethod">
		<button class="form" id="use_this_delivery_method">
			<spring:theme code="checkout.summary.deliveryMode.useThisDeliveryMethod"/>
		</button>
	</ycommerce:testId>
	{{/if}}
</script>


<c:set value="${not empty deliveryMode}" var="deliveryModeOk"/>

<div class="checkout_summary_flow_c ${deliveryModeOk ? 'complete' : ''}" id="checkout_summary_deliverymode_div">
	<div class="item_container_holder">
		<ycommerce:testId code="checkout_deliveryModeData_text">
			<div class="title_holder">
				<div class="title">
					<div class="title-top">
						<span></span>
					</div>
				</div>
				<h2>
					<spring:theme code="checkout.summary.deliveryMode.header" htmlEscape="false"/>
					<span></span></h2>
			</div>

			<div class="item_container">
				<ul>
					<c:choose>
						<c:when test="${deliveryModeOk}">
							<li>${deliveryMode.name} (${deliveryMode.code})</li>

							<li class="deliverymode-description" title="${deliveryMode.description}&nbsp;-&nbsp;${deliveryMode.deliveryCost.formattedValue}">${deliveryMode.description}&nbsp;-&nbsp;${deliveryMode.deliveryCost.formattedValue}</li>
						</c:when>
						<c:otherwise>
							<li>
								<spring:theme code="checkout.summary.deliveryMode.noneSelected"/>
							</li>
						</c:otherwise>
					</c:choose>
				</ul>
			</div>
		</ycommerce:testId>
	</div>

	<ycommerce:testId code="checkout_changeDeliveryMode_element">
		<c:choose>
			<c:when test="${deliveryModeOk}">
				<a href="#" class="edit_complete change_mode_button">
					<spring:theme code="checkout.summary.deliveryMode.editDeliveryMethod"/>
				</a>
			</c:when>
			<c:otherwise>
				<button class="form change_mode_button">
					<spring:theme code="checkout.summary.deliveryMode.editDeliveryMethod"/>
				</button>
			</c:otherwise>
		</c:choose>
	</ycommerce:testId>
</div>

<div style="display:none">
	<div id="popup_checkout_delivery_modes">
		<div class="span-20 last">
			<div class="item_container_holder">
				<div class="title_holder">
					<div class="title">
						<div class="title-top">
							<span></span>
						</div>
					</div>
					<h2>
						<spring:theme code="checkout.summary.deliveryMode.selectDeliveryMethod"/>
					</h2>
				</div>
				<div class="item_container">
					<p>
						<spring:theme code="checkout.summary.deliveryMode.selectDeliveryMethodForOrder"/>
					</p>
					<form>
						<ycommerce:testId code="checkout_deliveryModes">
							<div id="delivery_modes_dl" >
								<%-- available delivery modes for the cart --%>
							</div>
						</ycommerce:testId>
					<span style="display: block; clear: both;" id="delivery_modes_button">
                        <%-- delivery mode select button --%>
                    </span>
					</form>
				</div>
			</div>
		</div>
	</div>
</div>
