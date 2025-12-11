<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/loginaddon/responsive/template"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/addons/jnjlabrowseandsearchaddon/responsive/product"%>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/responsive/cart"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/addons/jnjb2bbrowseandsearch/responsive/common"%>
<%@ taglib prefix="breadcrumb" tagdir="/WEB-INF/tags/addons/jnjb2bbrowseandsearch/responsive/nav/breadcrumb"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="templateLa" tagdir="/WEB-INF/tags/addons/jnjlaloginaddon/responsive/template"%>

<c:url value="/p/${product.catalogId}/generateProductDetailsPDF" var="productDetailURL" />
<templateLa:page pageTitle="${pageTitle}">
    <div class="productdetailspage">
   <input id="products-bought-together-title" type="hidden" value="<spring:message code="product.carousel.products.bought.together.title"/>"/>
   <input id="add-to-cart-text" type="hidden" value="<spring:message code="product.detail.addToCart.addToCart"/>"/>
   	<input type="hidden" value="productDetailsPage" id="screenName" />
   	<input type="hidden" value="${product.baseMaterialNumber}" id="product_baseMaterialNumber" />
   	<input type="hidden" value="${numberOfProductPerSlide}" id="numberOfProductPerSlide" />
   	<input type="hidden" value="${cabCarouselHideFlag}" id="cabCarouselHideFlag" />
        <breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}" />
        <div class="dwnld-barcode-btns pull-right">
        <p>
        <strong><spring:message code="product.search.download"/>:</strong>
        <a href="${productDetailURL}" id="dwn-btn"><spring:message code="category.pdf"/></a>
        </p>
        </div>
        <section>
            <!--main Div-->
            <div id="productImageDetails"   class="row col-lg-12 col-md-12 col-sm-12 col-xs-12 padding0 boxshadow">
                <!--Image section start-->
                <div class="col-lg-5 col-sm-5 col-md-5 col-xs-12 productImageGallery">
                    <div class="imageThumbnails col-lg-12 col-sm-12 col-md-12 col-xs-12">
                        <div class="carouseldata">

                            <product:productPrimaryAndImageCarousel product="${product}" format="product" galleryImages="${galleryImages}" />

                        </div>
                        <p class="img_disclaimer">
                        <strong>
                            <spring:message code="product.detail.illustrative.image" />
                        </strong>
                        </p>
                    </div>
                </div>
                <!------------xx-------------Image section end--------------xx-------------->
                <!--Description section-->
                <div class="col-lg-7 col-sm-7 col-md-7 pageBlock prodDetailDescMd col-xs-12 productspanel">
                    <div style="margin: 30px 15px">
                        <div id="product_title">${product.name}</div>
                        <div id="product_code"  class="m-t-10">
                            <span> <spring:message code="product.detail.basic.productCode"/> &nbsp; </span>
                              <span>  ${product.catalogId} </span>
                              <span>(<spring:message code="product.detail.multiple.of"/>&nbsp; ${product.multiplicity})</span>
                        </div>
                        <div id="gtin_status" class="m-t-10"><c:if test="${not empty product.gtin}">
                            <span><spring:message code="product.detail.basic.gtin" /> &nbsp; </span>
                             <span>  ${product.gtin}  </span>
                            </c:if>
                        </div>

                        <c:set value="${product.status ne 'ACTIVE'}" var="disabled" />
                        <c:set value="0" var="index" />
                        <hr>
                        <input type="checkbox" hidden="true" title="check" class="selprod"	id="${product.baseMaterialNumber}" checked="checked" style="display: none"
                            index="${index}" />
                        <div class="display-table-row pull-left">                          
								<c:choose>
									<c:when test="${(empty pharmaCommercialUserGroupFlag || pharmaCommercialUserGroupFlag eq false) && (empty mddCommercialUserGroupFlag || mddCommercialUserGroupFlag eq false)}">
									<div class="display-table-cell">
										<label style="dislay: inline-block; line-height: 35px">
											<spring:message code="product.detail.addToCart.quantity" />
											&nbsp;
										</label> <input class="ProdTbox cat3Input form-control" type="text"
											value="0" title="check" id="quantity${index}" maxlength="6"
											name="qty" />
									</div>
									<div class="display-table-cell">
											<div id="sub-qty-mdd-prod" class="qty-box">
												<button id="decrement-qty" style="cursor: pointer;">-</button>
											</div>
										</div>
										<div class="display-table-cell">
											<div id="sub-qty-mdd-prod" class="qty-box">
												<button id="increment-qty" style="cursor: pointer;">+</button>
											</div>
										</div>
									</c:when>
									<c:otherwise>
									<div class="display-table-cell">
										<label style="dislay: inline-block; line-height: 35px">
											<spring:message code="product.detail.addToCart.quantity" />
											&nbsp;
										</label> 
										<input class="ProdTbox cat3Input form-control" type="text"
											disabled="disabled" value="0" title="check" id="quantity${index}" maxlength="6"
											name="qty" /></div>
										<div class="display-table-cell">
											<div id="sub-qty-mdd-prod" class="qty-box">
												<button id="decrement-qty" disabled style="cursor: pointer;">-</button>
											</div>
										</div>
										<div class="display-table-cell">
											<div id="sub-qty-mdd-prod" class="qty-box">
												<button id="increment-qty" disabled style="cursor: pointer;">+</button>
											</div>
										</div>
									</c:otherwise>
								</c:choose>						
                          
                        </div>
                        <div class="display-table-cell text-uppercase margintop15px pull-right" style="padding-left: 20px">
                            <input type="hidden" name="numbrOfProductLines" id="numberOfProductLines" value="10"/>
                            <input type="hidden" value="${product.baseMaterialNumber}" class="form-control txtWidth" id="productId${index}" readonly="readonly"/>
                           <c:choose>
                                <c:when test="${not disabled && ((empty pharmaCommercialUserGroupFlag || pharmaCommercialUserGroupFlag eq false) && (empty mddCommercialUserGroupFlag || mddCommercialUserGroupFlag eq false))}">                                   
                                    <input type="button" class="laAddToCart primarybtn ${disabled}-buttonDisable  anchorwhiteText"  value="<spring:message code="product.detail.addToCart.addToCart"/>" id="${product.baseMaterialNumber}" index="${index}" disableProductRow= "${disabled}"/>
                                </c:when>
                                <c:otherwise>
                                   <input type="button" class="laAddToCart primarybtn ${disabled}-buttonDisable  anchorwhiteText btn btn-primary disabled" disabled="disabled" value="<spring:message code="product.detail.addToCart.addToCart"/>" id="${product.baseMaterialNumber}" index="${index}" disableProductRow= "${disabled}"/>   
                                </c:otherwise>
                            </c:choose>                           
                        </div>

                    <br> <br>
                    <hr>
                    <c:if test="${!pdpDescHideFlag}">
                        <div class="middle-content">
                            <p class="sub-head2">${product.description}</p>
                        </div>
                        <div class="middle-content no-border">
                            <p class="overview-label">
                                <c:if test="${product.shortOverview != null}">
                                    <hr>
                                    <strong><spring:message code="product.detail.shortOverviewName" /></strong>
                                </c:if>
                            </p>
                            <p class="overview-desc">${product.shortOverview}</p>
                        </div>
                    </c:if>
                    <div class="bottom-content">
                        <div class="franchiseImageBox pull-right">
                             <img  src="${franchiseLogo}" alt ="${imgAlttext}"/>
                        </div>
                    </div>
                    </div>
                </div>
            </div>
        </section>

        <div  id="contractPopuppage">
            <div class="modal fade" id="contractpopup" role="dialog" data-firstLogin='true'>
                <div class="modal-dialog modalcls">
                    <div class="modal-content popup">
                        <div class="modal-header">
                            <button type="button" class="close clsBtn" data-bs-dismiss="modal">
                                <spring:message code="account.change.popup.close"/>
                            </button>
                            <h4 class="modal-title selectTitle">
                                <spring:message code="contract.page.addprod"/>
                            </h4>
                        </div>
                        <div class="modal-body">
                            <div class="panel panel-danger">
                                <div class="panel-heading">
                                    <h4 class="panel-title">
                                        <table class="contract-popup-table">
                                            <tr>
                                                <td>
                                                    <div class="glyphicon glyphicon-ok"></div>
                                                </td>
                                                <td>
                                                    <div class="info-text">
                                                        <spring:message code="contract.page.infotext"/>
                                                    </div>
                                                </td>
                                            </tr>
                                        </table>
                                    </h4>
                                </div>
                            </div>
                            <div>
                                <spring:message code="contract.page.msg"/>
                            </div>
                            <div class="continueques">
                                <spring:message code="contract.page.continue"/>
                            </div>
                        </div>
                        <div class="modal-footer ftrcls">
                            <a href="#" class="pull-left canceltxt" data-bs-dismiss="modal" id="cancel-btn-addtocart">
                                <spring:message code="cart.common.cancel"/>
                            </a>
                            <button type="button" class="btn btnclsactive" data-bs-dismiss="modal" id="accept-btn-addtocart" >
                                <spring:message code="contract.page.accept"/>
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <c:if test="${product.status eq 'ACTIVE'}">
            <div class="registerError">
                <label class="error" style="color: red">${product.errorMessage}</label>
            </div>
        </c:if>
        <product:mddProductDetailTabs/>
		<c:if test="${!rpCarouselHideFlag}">
          <cms:pageSlot position="RelatedProducts" var="comp" >
             <cms:component component="${comp}" />
          </cms:pageSlot>
        </c:if>
        <div class="scrollBox"></div>
        <div class="scrollBoxMob"></div>
       </div>
    <div class="row body-margin-bottom"></div>
</templateLa:page>