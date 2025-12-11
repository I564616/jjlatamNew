<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="template"
	tagdir="/WEB-INF/tags/addons/loginaddon/responsive/template"%>
<%@ taglib prefix="cartLa"
	tagdir="/WEB-INF/tags/addons/jnjlacartandcheckoutaddon/responsive/cart"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common"%>
<%@ taglib prefix="breadcrumb"
	tagdir="/WEB-INF/tags/responsive/nav/breadcrumb"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/desktop/product"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/desktop/nav"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="nav"
	tagdir="/WEB-INF/tags/addons/jnjlaservicepageaddon/responsive/nav"%>
<%@ taglib prefix="services"
	tagdir="/WEB-INF/tags/addons/jnjlaservicepageaddon/responsive/services"%>
<%@ taglib prefix="templateLa" tagdir="/WEB-INF/tags/addons/jnjlaloginaddon/responsive/template"%>
	
<templateLa:page pageTitle="${pageTitle}">

	<div id="breadcrumb" class="breadcrumb">
		<breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}" />
	</div>

	<div class="row">
		<div class="col-lg-12 col-md-12 margintop15"></div>
	</div>

	<!-- Start - Body Content -->
	<section>
		<div class="row jnj-body-padding shipmentContainer"
			id="jnj-body-content">
			<div class="col-lg-12 col-md-12 checkoutshipping"
				id="uploadOrderDetail">
				<div class="row">
					<div class="col-lg-12 col-md-12">
						<div class="row"></div>
						<div class="row">
							<div
								class="col-xs-12 col-sm-12 col-md-12 col-lg-12 headingTxt content">
								<h1>
									<spring:message code="text.uploadOrder.orderDetails" />
								</h1>
							</div>
						</div>
						<div class="addresspane">
							<div class="row shipping-row-padding">
								<div
									class="col-lg-12 col-md-12 col-sm-12 col-xs-12 ship-address-pane">
									<div class="row">
										<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
											<label class="pull-left boldtext label-lineHeight "><spring:message
													code="text.uploadOrder.trackingID" />&nbsp; </label> <span
												class="paddingLeft10px">${dataList.trackingID}</span>
										</div>
										<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
											<label class="pull-left boldtext label-lineHeight "><spring:message
													code="text.uploadOrder.po" /></label> <span class="paddingLeft10px">${dataList.poNumber}</span>
										</div>
									</div>
								</div>
							</div>
							<div class="row shipping-address">
								<div
									class="col-lg-12 col-md-12 col-sm-12 col-xs-12 shipmentAddressDetails">
									<div class="row jnjPanelbg">
										<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
											<div
												class="pull-left label-lineHeight form-consignment-label-select">
												<spring:message code="text.uploadOrder.document" />
												:
											</div>
											<div class="pull-left boldtext form-consignment-input-select">
												<c:choose>
													<c:when test="${not empty dataList.docName}">
														<strong>${dataList.docName}</strong>
													</c:when>
													<c:otherwise>
														<strong>&nbsp;</strong>
													</c:otherwise>
												</c:choose>
											</div>
										</div>
										<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
											<div
												class="pull-left label-lineHeight form-consignment-label-select">
												<spring:message code="text.uploadOrder.user" />
												:
											</div>
											<div class="pull-left boldtext form-consignment-input-select">
												<c:choose>
													<c:when test="${not empty dataList.user}">
														<strong>${dataList.user}</strong>
													</c:when>
													<c:otherwise>
														<strong>&nbsp;</strong>
													</c:otherwise>
												</c:choose>
											</div>
										</div>
										<div
											class="col-lg-6 col-md-6 col-sm-6 col-xs-12 margintop20px">
											<div
												class="pull-left label-lineHeight form-consignment-label-select">
												<spring:message code="text.uploadOrder.customer" />
												:
											</div>
											<div class="pull-left boldtext form-consignment-input-select">
												<c:choose>
													<c:when test="${not empty dataList.customer}">
														<strong>${dataList.customer}</strong>
													</c:when>
													<c:otherwise>
														<strong>&nbsp;</strong>
													</c:otherwise>
												</c:choose>
											</div>
										</div>
										<div
											class="col-lg-6 col-md-6 col-sm-6 col-xs-12 margintop20px">
											<div
												class="pull-left label-lineHeight form-consignment-label-select">
												<spring:message code="text.uploadOrder.date" />
												:
											</div>
											<div class="pull-left boldtext form-consignment-input-select">
												<c:choose>
													<c:when test="${not empty dataList.date}">
														<strong>${dataList.date}</strong>
													</c:when>
													<c:otherwise>
														<strong>&nbsp;</strong>
													</c:otherwise>
												</c:choose>
											</div>
										</div>
										<div
											class="col-lg-6 col-md-6 col-sm-6 col-xs-12 margintop20px">
											<div
												class="pull-left label-lineHeight form-consignment-label-select">
												<spring:message code="text.uploadOrder.status" />
												:
											</div>
											<div class="pull-left boldtext form-consignment-input-select">
												<c:choose>
													<c:when test="${not empty dataList.status}">
														<c:if test="${dataList.status =='Error'}">
															<strong><spring:message
																	code="text.uploadOrder.error" /></strong>
														</c:if>
														<c:if test="${dataList.status =='Sent With Restrictions'}">
															<strong><spring:message
																	code="text.uploadOrder.partialSuccess" /></strong>
														</c:if>
														<c:if test="${dataList.status =='Sent Successfully'}">
															<strong><spring:message
																	code="text.uploadOrder.success" /></strong>
														</c:if>
													</c:when>
													<c:otherwise>
														<strong>&nbsp;</strong>
													</c:otherwise>
												</c:choose>
											</div>
										</div>
									</div>

								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="row error-panel">
					<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
						<div class="panel-group hidden-xs hidden-sm">
							<div class="panel panel-danger">
								<div class="panel-heading">
									<h4 class="panel-title">
										<span> <c:forEach items="${dataList.errorMessageList}"
												var="entry" varStatus="count">
												<c:set var="rowClass" value="odd" />
												<c:if test="${count.count%2 ne 0}">
													<c:set var="rowClass" value="even" />
												</c:if>
												<div>
													<span class="descMid"><label:message
															messageCode="${entry}" /></span>
												</div>
											</c:forEach>
										</span>
									</h4>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</section>
	<!-- End - Body Content -->

	<section>
		<div class="row" id="jnj-body-content">
			<div class="col-lg-12 col-md-12" id="selectanaddress">

				<!-- Modal -->
				<div class="modal fade" id="selectaddresspopup" role="dialog">
					<div class="modal-dialog modalcls">

						<!-- Modal content-->
						<div class="modal-content popup">
							<div class="modal-header">
								<button type="button" class="close clsBtn" data-dismiss="modal">
								    <spring:message code="la.popup.close"/>
								</button>
								<h4 class="modal-title selectTitle">Select an Address</h4>
							</div>
							<div class="modal-body">
								<div class="form-group searchArea">
									<input type="text" class="form-control searchBox" id="usr"
										placeholder="Search for an address"> <span
										class="glyphicon glyphicon-search searchBtn pull-right"></span>
								</div>

								<div class="list-group listclass">
									<div class="odd-row">
										<div class="list-group-item-text descTxt">
											<div class="address-txt">J&J Consumer Products</div>
											<div class="address-txt">Skillman, New Jersey</div>
										</div>
									</div>
									<div class="even-row">
										<!-- <p class="list-group-item-text descTxt"><br></p> -->
										<div class="list-group-item-text descTxt">
											<div class="address-txt">University Medical Center at
												Princeton in Plansboro</div>
											<div class="address-txt">Plainsboro, New Jersey</div>
										</div>
									</div>
								</div>
							</div>
							<div class="row ftrcls">
								<button type="button" class="btn btnclsactive pull-right"
									data-dismiss="modal">CHANGE ADDRESS</button>
							</div>
						</div>

					</div>
				</div>
				<!--End of Modal pop-up-->
			</div>
		</div>
	</section>
	</body>
</templateLa:page>