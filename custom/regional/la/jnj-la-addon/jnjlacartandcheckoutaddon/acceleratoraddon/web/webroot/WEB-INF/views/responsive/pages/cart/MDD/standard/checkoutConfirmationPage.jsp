<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/loginaddon/responsive/template"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/addons/jnjlacartandcheckoutaddon/responsive/cart"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common"%>
<%@ taglib prefix="breadcrumb"	tagdir="/WEB-INF/tags/responsive/nav/breadcrumb"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/addons/loginaddon/message.tld"%>
<%@ taglib prefix="standardCart" tagdir="/WEB-INF/tags/addons/jnjlacartandcheckoutaddon/responsive/cart/standard"%>
<%@ taglib prefix="commonTags" tagdir="/WEB-INF/tags/addons/jnjlacartandcheckoutaddon/responsive/cart/common"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="templateLa" tagdir="/WEB-INF/tags/addons/jnjlaloginaddon/responsive/template"%>
<%@ taglib prefix="lacommon" tagdir="/WEB-INF/tags/addons/jnjlacartandcheckoutaddon/responsive/cart/common"%>

<templateLa:page pageTitle="${pageTitle}">
	<input type="hidden"  id="orderconfirmationfreightType" value="${freightType}" />
	<div id="ordercompletePage" class="orderReviewpage">
		<div class="row">
			<div class="col-lg-12 col-md-12">
				<div class="row">
					<div class="col-lg-7 col-md-7">
						<breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}" />
					</div>
				</div>
				<div class="row headingTxt content">
					<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 orderreviewtxt">
						<spring:message code="cart.review.orderComplete" />
					</div>

				</div>
			</div>
		</div>

		<!--review start here for address and billing address sub total of the review   -->
		<div class="row table-padding">

				<c:choose>
					<c:when test="${orderData.hasError}">
					 <div class="error ">
					 		  <p style="font-size:auto;color:#b41601">
                                <lacommon:genericMessage messageCode="order.review.error" icon="ban-circle" panelClass="danger" />
                               </p>
                            </div>
						<%-- //<spring:message code="order.review.error"/> --%>
					</c:when>
					<c:otherwise>
					<div class="col-lg-7 col-md-7 col-sm-7 col-xs-12">
						<div class="order-column-x-50">
						    <p class="subhead boldtext text-uppercase">
		                		<spring:message code="cart.common.purchaseOrder" />
		                	</p>
		                	
							<c:choose>
		                        <c:when test="${splitOrder}">
		                            <div class="subtext boldtext">
		                                <c:forEach items="${orderDataList}" var="orderData">
		                                	
		                                    <div>
		                                        <div class="subtext boldtext">${orderData.purchaseOrderNumber}</div>
		                                    </div>
		                                </c:forEach>
		                            </div>
		                        </c:when>
		                        <c:otherwise>
		                            <div class="subtext boldtext">${orderData.purchaseOrderNumber}</div>
		                        </c:otherwise>
							</c:choose>
						</div>
						<div class="order-column-x-50">
						    <c:choose>
		                        <c:when test="${splitOrder}">
		                            <p class="subhead boldtext text-uppercase">
		                                <spring:message code="cart.confirmation.orderNumber" />
		                            </p>
		                            <div class="subtext boldtext">
		                                <c:if test="${not empty orderDataList}">
		                                    <div class="subtext boldtext">
		                                        <c:forEach items="${orderDataList}" var="orderData">
		                                         <c:url value="/order-history/order/${orderData.code}" var="orderDetailUrl" />
		                                            <a href="${orderDetailUrl}">${orderData.sapOrderNumber}</a><br>
		                                        </c:forEach>
		                                    </div>
		                                </c:if>
		                            </div>
		                        </c:when>
		                        <c:otherwise>
		                                <p class="subhead boldtext text-uppercase">
		                                    <spring:message code="cart.confirmation.orderNumber" />
		                                </p>
		                                <div class="subtext boldtext">
		                                	<c:url value="/order-history/order/${orderData.code}" var="orderDetailUrl" />
		                                    <a href="${orderDetailUrl}">${orderData.sapOrderNumber}</a>
		                                </div>
		                        </c:otherwise>
		                    </c:choose>
		                </div>
		                </div>
					</c:otherwise>
					
				</c:choose>
				<div class="modal fade jnj-popup" id="freight-fee-details" role="dialog">
    	
					<div class="modal-dialog modalcls modal-md">
						<div class="modal-content" style="width: 500px; margin-left: 170px">
							<div class="modal-header">
								<button type="button" class="close clsBtn" data-dismiss="modal"><spring:message code="cart.review.close" /></button>
								
							</div>
							<div class="modal-body">
								<div class="row">
									<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
										<div class="panel-group">
										   <div style="margin-left: 150px;color:red;font-size:22px;margin-top: -20px"><spring:message code="cart.review.orderComplete.reminder" /></div>
											<div  style="padding-left: 20px; font-size: 16px"><span style="font-weight:bold;font-size: 16px"><spring:message code="cart.review.orderComplete.freightNote" /></span><span style="font-size: 16px"><spring:message code="cart.review.orderComplete.freightmessage" /></span> </div>
										</div>
									</div>
									
								</div>
							</div>
						</div>
					</div>
				</div>
				
				<div class="order-column-x-100">
					<hr>
				</div>
				<!-- ship-to address start here -->
				<p class="subhead boldtext text-uppercase"><br>
					<spring:message code="cart.common.ShipToAdd" />
				</p>
				<cart:deliveryAddress deliveryAddress="${orderData.deliveryAddress}"
					companyName="${orderData.b2bUnitName}" />
				<!-- ship - to address ends here -->
			</div>
		</div>
		<!--review ends here for address and billing address sub total of the review   -->
		<br>
		<br>
		<standardCart:orderEntries />
	</div>
</templateLa:page>