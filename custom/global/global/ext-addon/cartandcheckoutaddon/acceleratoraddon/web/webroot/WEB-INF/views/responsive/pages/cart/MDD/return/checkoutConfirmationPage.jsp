<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="template"
	tagdir="/WEB-INF/tags/addons/loginaddon/responsive/template"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="cart"
	tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common"%>
<%@ taglib prefix="breadcrumb"
	tagdir="/WEB-INF/tags/responsive/nav/breadcrumb"%>
<%@ taglib prefix="label"
	uri="/WEB-INF/tld/addons/loginaddon/message.tld"%>
<%@ taglib prefix="standardCart"
	tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/standard"%>
<%@ taglib prefix="commonTags"
	tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/common"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="returnCart"
	tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/return"%>



<template:page pageTitle="${pageTitle}">
	<spring:url value="${continueUrl}" var="continueShoppingUrl"
		htmlEscape="true" />
	<!-- 	<div class="row jnj-body-padding" id="jnj-body-content">
							<div class=" col-lg-12 col-md-12 mobile-no-pad"> -->


	<div id="returnorderpage">
		<ul class="breadcrumb">
			<breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}" />
		</ul>

		<br>
		<c:if test="${not empty successMessage}">
			<div class="panel-group hidden-xs hidden-sm"
				style="margin: 5px 0 10px 0">
				<div class="panel-group hidden-xs hidden-sm"
					style="margin: 5px 0 20px 0">
					<div class="panel panel-success">
						<div class="panel-heading">
							<h4 class="panel-title">
								<c:if test="${not empty successMessage}">
									<div class="success">
										<p>
											<span class="glyphicon glyphicon-ok"></span>&nbsp;${successMessage}
										</p>
									</div>
								</c:if>
								<c:if test="${not empty excludedProducts}">
									<div class="error">
										<p>${excludedProducts}</p>
									</div>
								</c:if>
							</h4>
						</div>
					</div>
				</div>
			</div>
		</c:if>
		<div class="row">
			<div
				class="col-lg-12 col-md-12 col-sm-12 col-xs-12 headingTxt content">
				<spring:message code="cart.returnConfirmation.heading" />
				<!-- <a href="#"><span class="badge quescls">?</span></a> -->
			</div>

		</div>
		<div class="">
			<div class=" row col-lg-12 col-md-12 table-padding">
				<div class="row first-row-content borderbtm">
					<div class="col-lg-2 col-md-2 col-sm-2 col-xs-6">
						<div class="return-label-head">
							<spring:message code="cart.return.orderType"></spring:message>
						</div>
						<div class="return-head-content">
							<spring:message
								code="cart.common.orderType.${orderData.orderType}" />
						</div>
					</div>
					<div class="col-lg-2 col-md-2 col-sm-2 col-xs-6">
						<div class="return-label-head">
							<spring:message code="cart.return.Account"></spring:message>
						</div>
						<div class="return-head-content">${orderData.b2bUnitId}</div>
					</div>
					<div class="col-lg-2 col-md-2 col-sm-2 col-xs-5">
						<div class="return-label-head">GLN</div>
						<div class="return-head-content">${orderData.gln}</div>
					</div>
					<div class="col-lg-6 col-md-6 col-sm-6 col-xs-6 text-right "
						style="margin-top: 15px">
						<c:url
							value="/checkout/single/downloadOrderConfirmation/${orderData.code}"
							var="orderConfirmationDownload" />

						<span class="pull-right"><span class="link-txt boldtext"><spring:message
									code="cart.confirmation.download" /></span> <a
							class="tertiarybtn excel"
							href="${orderConfirmationDownload}?downloadType=EXCEL&&isReturnOrder=true"><spring:message
									code="cart.confirmation.excel" /></a> | <a class="tertiarybtn pdf"
							href="${orderConfirmationDownload}?downloadType=PDF&&isReturnOrder=true"><spring:message
									code="cart.confirmation.pdf" /></a> </span>
					</div>
				</div>
				<div
					class="row col-lg-12 col-md-12 col-sm-12 col-xs-12 shippingAddressDetails"
					id="orderdetailspage">
					<div
						class="row col-lg-12 col-md-12 col-sm-12 col-xs-12 returndetails">
						<div class="col-lg-4 col-md-4 col-sm-5 col-xs-12 referncepo">
							<h6 class="text-uppercase">
								<spring:message
									code="cart.return.returnGoodsAuthorizationNumber" />
							</h6>
							<p>${orderData.orderNumber}</p>
							<p>

								<c:if test="${orderData.rgaLinkURL}">
									<c:url
										value="/order-history/order/${orderData.code}/${orderData.orderNumber}/callRga/confirmationPage"
										var="rgaUrl" />


									<a id="rgaId" href="${rgaUrl}"><spring:message
											code="orderDetailPage.orderData.rgaLink" /></a>
								</c:if>
							</p>
						</div>
						<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 referncepo">
							<h6 class="text-uppercase">
								<label for="purchOrder"><spring:message
										code="cart.common.purchaseOrder" /></label>
							</h6>
							<p>${orderData.purchaseOrderNumber}</p>
						</div>
						<div
							class="col-lg-4 col-md-4 col-sm-3 col-xs-12 pull-right marginbtm20 referncepo">
							<h6 class="text-uppercase">
								<label for="reasonCode"><spring:message
										code="cart.return.returnInfo.reasonCode" /></label>
							</h6>
							<p>${orderData.reasonCodeReturn}
								:
								<spring:message code="${orderData.reasonCodeReturn}" />
							</p>
							<div class="order-status-cofirm">
								<h6 class="text-uppercase">
									<spring:message code="cart.confirmation.orderStatus" />
								</h6>
								<p>${orderData.statusDisplay}</p>
							</div>

						</div>
					</div>


					<c:if test="${orderData.customerPoNumber ne null}">
						<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 referncepo">
							<h6 class="text-uppercase">
								<label for="customerPo"><spring:message
										code="cart.return.returnInfo.customerReferencePO" /></label>
							</h6>
							<p class="text-uppercase">${orderData.customerPoNumber}</p>
						</div>
					</c:if>
				</div>

			</div>
		</div>
		<div class="continueShopBtn">
			<div class="float-right-to-none btnclsactive cartbtn">
				<a href="Catalog/c/Categories" class="anchorwhiteText"><spring:message
						code="cart.review.cartPageAction.continue"></spring:message></a>
			</div>
		</div>


		<div class="hidden-xs ">
			<table id="returnOrderTable"
				class="table table-bordered table-striped only-sort-table">
				<thead>
					<tr>
						<th class="text-left no-sort">#</th>
						<th class="text-left no-sort"><span><spring:message
									code="cart.validate.product" /></span></th>
						<th class="text-left no-sort"><span><spring:message
									code="cart.review.entry.quantity" /></span></th>
						<th class="text-left no-sort text-uppercase"><span><spring:message
									code="cart.return.entries.lotNumber" /></span></th>
						<th class="text-left no-sort text-uppercase"><span><spring:message
									code="cart.return.entries.poNumber" /></span></th>
						<th class="text-left no-sort text-uppercase"><span><spring:message
									code="cart.return.entries.invoiceNumber" /></span></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${orderData.entries}" var="entry"
						varStatus="count">
						<tr>
							<td class="text-left">${varStatus}</td>
							<td class="text-left jnj-img-txt">

								<div class="display-row">
									<standardCart:ProductDescriptionOrderComplete
										rowcount="${count.count}" entry="${entry}"
										showRemoveLink="true" showStatus="false" />
								</div>

							</td>
							<td class="text-center">
								<p>${entry.quantity}</p>
								<p class="thirdline">${entry.product.deliveryUnit}
									(${entry.product.numerator}&nbsp;${entry.product.salesUnit})</p> <!-- <a href="#">Remove</a> -->
							</td>
							<td><div>${entry.lotNumber}</div></td>
							<td><div>${entry.poNumber}</div></td>
							<td><div>${entry.returnInvNumber}</div></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		<div class="visible-xs hidden-lg hidden-sm hidden-md">
			<table id="datatab-mobile"
				class="table table-bordered table-striped sorting-table-lines returnOrderScreen">
				<thead>
					<tr>
						<th class="no-sort"><spring:message
								code="cart.validate.product" /></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${orderData.entries}" var="entry"
						varStatus="count">
						<tr>
							<td class="text-left"><returnCart:ProductDescriptionOrderComplete
									rowcount="${count.count}" entry="${entry}"
									showRemoveLink="true" showStatus="false" />

								<div id="mobi-collapse${count.count}"
									class="panel-collapse collapse img-accordian">
									<p>
										<spring:message code="cart.review.entry.quantity" />
									</p>
									${entry.quantity}
									<p class="thirdline">${entry.product.deliveryUnit}
										(${entry.product.numerator}&nbsp;${entry.product.salesUnit})</p>

									<p>
										<spring:message code="cart.return.entries.lotNumber" />
									</p>
									<p class="text-uppercase">
									<div class="text-uppercase">${entry.lotNumber}</div>
									</p>
									<p>
										<spring:message code="cart.return.entries.poNumber" />
									</p>
									<p>
									<div>${entry.poNumber}</div>
									</p>
									<p>
										<spring:message code="cart.return.entries.invoiceNumber" />
									</p>
									<p>
									<div>${entry.returnInvNumber}</div>
									</p>
								</div></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>

		<div class="continueShopBtn">
			<div class=" float-right-to-none btnclsactive cartbtn">
				<a href="Catalog/c/Categories" class="anchorwhiteText"><spring:message
						code="cart.review.cartPageAction.continue"></spring:message></a>
			</div>
		</div>

	</div>


	

</template:page>

