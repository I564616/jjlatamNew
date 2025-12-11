<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="template"
	tagdir="/WEB-INF/tags/addons/loginaddon/responsive/template"%>

<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>

<%@ taglib prefix="messageLabel"
	uri="/WEB-INF/tld/addons/loginaddon/message.tld"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%-- <%@ taglib prefix="home" tagdir="/WEB-INF/tags/addons/loginaddon/responsive/home"%> --%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/responsive/nav"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%-- <%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%> --%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common"%>
<%@ taglib prefix="breadcrumb"
	tagdir="/WEB-INF/tags/responsive/nav/breadcrumb"%>

<script type="text/javascript">
function AlertIt() {
var answer = confirm ("Products Not Yet Added....!")
if (answer)
 window.location="home/buildorder/resetBuildOrder"; 
}
</script> 


<template:page pageTitle="${pageTitle}">
	<form:form name="productQuantityForm" id="productQuantityForm"
		action="javascript:;">
		

<section>
<div class="row jnj-body-padding" id="jnj-body-content">
	<div class="col-lg-12 col-md-12 mobile-no-pad">
		<div id="buildorderpage">
		
		<ul class="breadcrumb">
			<li><a href="${webroot}/en/mdd-deCMSsite/home">Home</a></li>
			<li><a href="">Build an Order</a></li>
		</ul>


		<div class="row content">
			<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
				<strong>Build an Order</strong> <a href="#"><span
					class="badge quescls">?</span></a>
			</div>

			 <div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 addToCartBtnHide">
				<button type="submit" class="btn btnclsactive pull-right addtoCart">ADD TO CART</button>
				<a href="home/buildorder/resetBuildOrder" class="canceltxt pull-right">Cancel</a>
			</div> 
		</div>

		<div class="row table-padding">
			<div class="col-lg-12 col-md-12">
			<input type="button" id="errorQuickCart" class="tertiarybtn homeCartErrors left" style="display:none;" value="<spring:message code='homePage.errorDetails' />"/>
<div class="registerError"></div>
		<!-- START DEFECT 32656 -->
		<div class="registersucess"></div>
				<div class="row subcontent1">
					<div class="col-lg-12 col-md-12 col-xs-12">Enter Products &
						Quantity</div>
				</div>

				<div class="row subcontent2">
					<div class="form-group col-lg-5 col-md-5 col-sm-5 col-xs-12">
						<label for="usr" class="subtxt" data-msg-required="<spring:message code="cart.common.validNumber"></spring:message>" ${disableInputBox}>Product Number </label> <input
							type="text" id="productCode" class="form-control boxprop"
							id="usr">
					</div>
					
					
					
					<div
						class="form-group col-lg-4 col-md-4 col-sm-4 col-xs-12 quantitymbox">
						<label for="usr" class="subtxt ">Quantity</label> <input
							type="text" id="qty" class=" qtyUpdateTextBox form-control boxprop quantitybox "
							id="usr">
					</div>
					<div class="cartbtn col-lg-3 col-md-3 col-sm-3 col-xs-12 ">
						<button type="button" id="addToOrder" 	class="btn btnclsnormal pull-right ">ADD TO ORDER</button>
					</div>
				</div>


				<div class="row">
				<input type="hidden" name="numbrOfProductLines" id="numberOfProductLines" value="10"/>
					<c:if test="${not empty orderform}">
						<table id="ordersTable"
							class="table table-bordered table-striped hidden-xs sorting-table">
							<thead>
								<tr>
									<th class="text-left"><spring:theme
											code="text.account.buildorder.productCode"
											text="PRODUCT NUMBER" /></th>
									<th class="text-left no-sort"><spring:theme
											code="text.account.buildorder.customerProductCode"
											text="PRODUCT NAME" /></th>
									<th class="no-sort"><spring:theme
											code="text.account.buildorder.quantity" text="QUANTITY" />
								
									</th>
											
											
							
									<th class="no-sort"></th>
								</tr>
							</thead>
							<tbody class="tabdata">
						      
								<c:forEach items="${orderform.lineItems}" var="entry"
									varStatus="status">

									<tr>
										 
									 <td class="text-left">${entry.product.code}
									 <input type="hidden" value="${entry.product.code}" class="form-control txtWidth" id="productId${status.index}" readonly="readonly">
									  </td> 
										<td class="text-left">${entry.product.name}</td>
										<td><input type="text" class="qtyUpdateTextBox form-control txtWidth " id="quantity${status.index}" value="${entry.quantity}" entryNumber="${entry.quantity}"
>
											</td>

										<td><a name="${entry.product.code}"
											class="button_neg_cls removeProd" value="remove" >Delete</a>
										</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>

					</c:if>
				</div>
			</div>
		</div>
		<div class="row subcontent3">
			<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 addToCartBtnHide">
				<button type="submit" class="btn btnclsactive pull-right addtoCart">ADD TO CART</button>
				<!-- <a href="home/buildorder/resetBuildOrder" onclick="" id ="thickboxButton" class="canceltxt pull-right">Cancel</a> -->
				<!-- <button type="button" class="btn btnclsactive pull-right addtoCart Cancel" id="cancelOrder">CANCEL</button> -->
				<a href="javascript:AlertIt();" id ="thickboxButton" class="canceltxt pull-right">Cancel</a>
			</div>
		</div>
		


		

</div>
</div>
</div>
</section>


	</form:form>


</template:page>
<script>
	/* function test(){
		alert("clicked");
	} */
</script>

