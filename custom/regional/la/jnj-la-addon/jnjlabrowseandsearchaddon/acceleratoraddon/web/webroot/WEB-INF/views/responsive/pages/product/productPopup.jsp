<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/loginaddon/responsive/template"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/addons/jnjb2bbrowseandsearch/responsive/product"%>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/responsive/cart"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/addons/jnjb2bbrowseandsearch/responsive/common"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="breadcrumb" tagdir="/WEB-INF/tags/addons/jnjb2bbrowseandsearch/responsive/nav/breadcrumb"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/addons/jnjlabrowseandsearchaddon/responsive/product"%>

<div class="modal fade jnj-popup" id="product-detail-popup"	role="dialog">
    <div class="modal-dialog modalcls modal-lg la-modal">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close clsBtn" data-dismiss="modal">
                    <spring:message code="account.change.popup.close"/>
                </button>
                <h4 class="modal-title">${product.name}</h4>
            </div>
            <div class="modal-body">
				<div class="productspanel">
					<div id="product_code">
						<span>
				            <product:productPrimaryImage product="${entry.product}" format="cartIcon" />
							<strong>&nbsp; ${product.catalogId}</strong>
						</span>
					</div>
					<div id="gtin_status">
						<c:if test="${not empty product.gtin}">
							<span>
								<strong><spring:message code="product.detail.basic.gtin" /></strong>
								&nbsp;${product.gtin}
							</span>
						</c:if>
					</div>
					<div>
					    </br>
					    <p><strong><spring:message code="product.detail.specification.specification"/></strong><p>
					</div>
					<div>
					    <strong><spring:message code="product.search.delivered.uom"/></strong>
                        &nbsp;${product.deliveryUnitCode}&nbsp;(${product.deliveryUnit})
                        <div>
                            <spring:message code="product.search.each"/>&nbsp;${product.deliveryUnitCode}&nbsp;
                            <spring:message code="product.search.contains"/>&nbsp;${product.numeratorDUOM} &nbsp; ${product.baseUnitCode}
                        </div>
					</div>
					<div>
                        <strong><spring:message code="product.search.sold.uom"/></strong>
                        &nbsp;${product.salesUnitCode}&nbsp;(${product.salesUnit})
                        <div>
                            <spring:message code="product.search.each"/>&nbsp;${product.salesUnitCode}&nbsp;
                            <spring:message code="product.search.contains"/>&nbsp; ${product.numeratorSUOM} &nbsp; ${product.baseUnitCode}
                        </div>
					</div>
					<c:if test="${not empty product.shipWeight}">
                        <div>
                            <strong><spring:message code="product.detail.ship.weight"/></strong>
                            &nbsp;${product.shipWeight}&nbsp;${product.shippingUnit}
                        </div>
                    </c:if>
                    <c:if test="${not empty product.width &&  not empty product.height && not empty product.length}">
                        <div>
                            <strong><spring:message code="product.detail.volume"/></strong>
                            &nbsp;${product.length}&nbsp;x&nbsp;${product.width}&nbsp;x&nbsp;${product.height}&nbsp;${product.volumeUnit}
                        </div>
                    </c:if>
					<div>
                        <strong><spring:message code="product.detail.ship.country.origin"/></strong>
                        &nbsp;${product.originCountry}
					</div>
					<div>
					    </br>
                        <spring:message code="product.final.details.deliveryUom.msg" />
					</div>
				</div>
            </div>
        </div>
    </div>
</div>