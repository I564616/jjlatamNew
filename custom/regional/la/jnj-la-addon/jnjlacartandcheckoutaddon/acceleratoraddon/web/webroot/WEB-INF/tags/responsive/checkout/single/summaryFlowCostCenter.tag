<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="costCenter" required="true" type="de.hybris.platform.b2bacceleratorfacades.order.data.B2BCostCenterData" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>

<spring:url value="/checkout/single/summary/getCostCenters.json" var="getCostCenters"/>
<spring:url value="/checkout/single/summary/getCheckoutCart.json" var="getCheckoutCartUrl" />
<spring:url value="/checkout/single/summary/setCostCenter.json" var="setCostCenterUrl" />
<spring:url value="/checkout/single/summary/updateCostCenter.json" var="updateCostCenterUrl" />
<script type="text/javascript"> // set vars

	var updateCostCenterUrl = '${updateCostCenterUrl}';
	var setCostCenterUrl = '${setCostCenterUrl}';

</script>

<div class="checkout_summary_flow_b" id="checkout_summary_costcenter_div">
	<div class="item_container_holder">
		<div class="title_holder">
			<div class="title">
				<div class="title-top">
					<span></span>
				</div>
			</div>
			<h2><spring:theme code="checkout.summary.costCenter.header" htmlEscape="false"/><span></span></h2>
		</div>

		<div class="item_container">

				<form:select id="CostCenter" path="costCenters" cssClass="card_date">
					<option value="" label="<spring:theme code='costCenter.title.pleaseSelect'/>">
					<form:options items="${costCenters}" itemValue="code" itemLabel="name"/>
				</form:select>

		</div>
	</div>
</div>
