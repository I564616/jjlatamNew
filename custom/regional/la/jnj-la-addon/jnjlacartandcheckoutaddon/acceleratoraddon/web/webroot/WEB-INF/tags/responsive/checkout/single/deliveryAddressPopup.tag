<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="checkout" tagdir="/WEB-INF/tags/responsive/checkout/single" %>
<%@ taglib prefix="formUtil" tagdir="/WEB-INF/tags/responsive/form" %>

<div style="display:none">
	<div class="test" id="popup_checkout_delivery_address">
		<div class="span-20" id="checkout_delivery_address">
			<checkout:deliveryAddressForm/>
			<div class="span-10 last">
				<div class="item_container_holder">
					<div class="title_holder">
						<div class="title">
							<div class="title-top">
								<span></span>
							</div>
						</div>
						<h2>
							<spring:theme code="checkout.summary.deliveryAddress.selectExistingAddress"/>
						</h2>
					</div>
					<ycommerce:testId code="checkout_existingAddresses">
						<div class="item_container delivery_addresses_list">
							<%-- List of delivery addresses is injected here --%>
						</div>
					</ycommerce:testId>
				</div>
			</div>
		</div>
	</div>
	<div class="test" id="popup_select_delivery_address">
		<div class="span-10 last" id="select_checkout_delivery_address">
			<div class="span-10 last">
				<div class="item_container_holder">
					<div class="title_holder">
						<div class="title">
							<div class="title-top">
								<span></span>
							</div>
						</div>
						<h2>
							<spring:theme code="checkout.summary.deliveryAddress.selectExistingAddress"/>
						</h2>
					</div>
					<ycommerce:testId code="select_checkout_existingAddresses">
						<div class="item_container select_from_delivery_addresses_list">
							<%-- List of delivery addresses is injected here --%>
						</div>
					</ycommerce:testId>
				</div>
			</div>
		</div>
	</div>
</div>
