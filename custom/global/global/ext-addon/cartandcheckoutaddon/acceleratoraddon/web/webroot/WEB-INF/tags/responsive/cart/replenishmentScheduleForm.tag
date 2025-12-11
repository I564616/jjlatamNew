<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="formUtil" tagdir="/WEB-INF/tags/responsive/form"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>

<spring:url value="/checkout/summary/placeOrder" var="placeOrderUrl" />
<spring:theme code="replenishmentScheduleForm.activateDaily" var="Daily"/>
<spring:theme code="replenishmentScheduleForm.activateWeekly" var="Weekly"/>
<spring:theme code="replenishmentScheduleForm.activateMonthly" var="Monthly"/>
<spring:theme code="text.store.dateformat.datepicker.selection" text="mm/dd/yy" var="dateForForDatePicker"/>

<script type="text/javascript"> // set vars
var dateForForDatePicker = '${dateForForDatePicker}';
var placeOrderFormreplenishmentRecurrence = '${placeOrderForm.replenishmentRecurrence}';
var placeOrderFormnDays = '${placeOrderForm.nDays}';
var placeOrderFormnthDayOfMonth = '${placeOrderForm.nthDayOfMonth}';
var placeOrderFormnegotiateQuote = '${placeOrderForm.negotiateQuote}';
var placeOrderFormreplenishmentOrder = '${placeOrderForm.replenishmentOrder}';
</script>



 <div class="span-20 last place-order-top" id="replenishment-schedule-div" style="display:none;">
	<div class="item_container_holder">
		<div class="title_holder">
			<div class="title">
				<div class="title-top">
					<span></span>
				</div>
			</div>
			<h2>
				<spring:theme code="checkout.summary.replenishmentScheduleForm.title" />
			</h2>
		</div>


		<div class="item_container">
			<div class="scheduleform  scheduleform_left">
				<div class="replenishmentFrequency_left"><formUtil:formInputBox idKey="replenishmentStartDate" labelKey="replenishmentScheduleForm.startDate" path="replenishmentStartDate" inputCSS="date" mandatory="true" /></div>
			</div>

			<div class="scheduleform scheduleformD">
				<div class="replenishmentFrequency"><form:radiobutton path="replenishmentRecurrence" id="replenishmentFrequencyD" label="${Daily}" value="DAILY" class="replenishmentfrequencyD" /></div>

				<label for="nDays">
					<spring:theme code="replenishmentScheduleForm.daily.days" />
					<div class="skip">
						(<spring:theme code="replenishmentScheduleForm.days"/>)
					</div>
				</label>
				<form:select id="nDays" path="nDays" >
					<form:options items="${nDays}" />
				</form:select>
				
				<spring:theme code="replenishmentScheduleForm.days"/>
			</div>

			<div class="scheduleform scheduleformW">
				<div class="replenishmentFrequency"><form:radiobutton path="replenishmentRecurrence" id="replenishmentFrequencyW" label="${Weekly}" value="WEEKLY" class="replenishmentfrequencyW" /></div>

				<div class="div_nWeeks1">
					<label for="daysOfWeek"><spring:theme code="replenishmentScheduleForm.weekly.daysOfWeek" /></label>
					<form:select id="daysOfWeek" path="nDaysOfWeek"  multiple="true">
						<form:options items="${daysOfWeek}" itemLabel="name" itemValue="code" />
					</form:select>
				</div>

				<div class="div_nWeeks2">
					<label for="nWeeks"><spring:theme code="replenishmentScheduleForm.weekly.weeks" /><div class="skip">(<spring:theme code="replenishmentScheduleForm.weeks"/>)</div></label>
					<form:select id="nWeeks" path="nWeeks" >
						<form:options items="${nthWeek}" />
					</form:select>
					<spring:theme code="replenishmentScheduleForm.weeks"/>
				</div>

			</div>

			<div class="scheduleform scheduleformM scheduleform_last">
				<div class="replenishmentFrequency"><form:radiobutton path="replenishmentRecurrence" id="replenishmentFrequencyM" label="${Monthly}" value="MONTHLY" class="replenishmentfrequencyM" /></div>

				<label for="nthDayOfMonth">
					<spring:theme code="replenishmentScheduleForm.monthly.day"/>
					<div class="skip">
						(<spring:theme code="replenishmentScheduleForm.month"/>)
					</div>
				</label>
				<form:select id="nthDayOfMonth" path="nthDayOfMonth" >
					<form:options items="${nthDayOfMonth}" />
				</form:select>
				<spring:theme code="replenishmentScheduleForm.month"/>
			</div>

		</div>
	</div>
	<div class="item_container_cancel_placeorder right">
		<form:input type="hidden" id="replenishmentOrder" path="replenishmentOrder" />
		<button type="button" class="positive right pad_right place-order" id="placeReplenishmentOrderButton">
				<spring:theme code="checkout.summary.placeReplenishmentOrder"/>
		</button>

		<a href="javascript:void(0)" class="edit_complete change_address_button" id="cancel-place-replenishmentOrder">
		<spring:theme code="checkout.summary.negotiateQuote.cancel"/></a>

	</div>
</div>
