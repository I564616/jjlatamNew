<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="documentType" required="true" type="java.lang.String" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/responsive/cart" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="home" tagdir="/WEB-INF/tags/addons/loginaddon/responsive/home" %>
<c:url value="/my-account/contract/addToCart" var="contractFormAction" />
<c:set value="ZCQ" var="quantityBasedContract"></c:set>
<div class="row">
   <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
      <input  type="button" class="btn btnclsactive pull-right addtoCart contract-add-to-cart primarybtn addContractToCart"
         value= '<spring:message code="contract.detail.button.addtocart" />'/>
   </div>
</div>
<form:form id="addContractToCartForm" action="${contractFormAction}" method="post" modelAttribute="contractForm">
   <input type="hidden" name="eCCContractNum" value="${contractData.eccContractNum}">
   <input type="hidden" name="indirectCustomer" value="${contractData.indirectCustomer}">
   <input type="hidden" id="selected-product-codes" value="" name="selectedProducts">
   <div id="ContractDetailsPage">
      <div class="row">
         <div class="col-lg-12 col-md-12">
            <div class="d-none d-sm-block jnj-panel-for-table mainbody-container">
               <table id="datatab-desktop" class="table table-bordered table-striped contract-detail-desktab">
                  <thead>
                     <tr>
                        <th class="no-sort text-uppercase">
                           <div class="checkbox checkbox-info selectchkbox">
                              <input id="contract-select-all" class="styled contract-thead-chckbox" type="checkbox"> <!--  selectall_contract -->
                              <label for="contract-select-all"  id="contract-head-chck-label">
                                 <spring:message code="search.result.selectAll"/>
                              </label>
                           </div>
                        </th>
                        <th class="no-sort text-uppercase">
                           <spring:message code="contract.itemPrice"/>
                        </th>
                        <c:if test="${documentType == 'ZCQ'}">
                           <th class="no-sort text-uppercase contract-cnu-head">
                              <spring:message code="contract.unitOfMeasure"/>
                           </th>
                           <th class="no-sort text-uppercase">
                              <spring:message code="contract.contractQty"/>
                           </th>
                           <th class="no-sort text-uppercase">
                              <spring:message code="contract.consumed"/>
                           </th>
                           <th class="no-sort text-uppercase">
                              <spring:message code="contract.balance"/>
                           </th>
                        </c:if>
                         <c:if test="${documentType == 'ZCI'}">
                           <th class="no-sort text-uppercase contract-cnu-head">
                              <spring:message code="contract.unitOfMeasure"/>
                           </th>
                           <th class="no-sort text-uppercase">
                              <spring:message code="contract.contractQty"/>
                           </th>
                           <th class="no-sort text-uppercase">
                              <spring:message code="contract.consumed"/>
                           </th>
                           <th class="no-sort text-uppercase">
                              <spring:message code="contract.balance"/>
                           </th>
                        </c:if>
                        <c:if test="${documentType == 'ZCV'}">
                           <th class="no-sort text-uppercase">
                              <span>
                              <spring:message code="contract.totalValue"/>
                           </th>
                           <th class="no-sort text-uppercase">
                              <span>
                              <spring:message code="contract.consumedValue"/>
                           </th>
                           <th class="no-sort text-uppercase">
                              <spring:message code="contract.contractBalance"/>
                           </th>
                        </c:if>
                     </tr>
                  </thead>
                  <tbody>
                     <c:forEach items="${contractEntryList}" var="contractEntry" varStatus="status">
                        <c:url value="${contractEntry.product.url}" var="productUrl"/>
                        <tr class="contractDetailRow ${status.index %2 == 0 ? 'even' : 'odd'}" id="contractDetailRow_${contractEntry.active}">
                           <td class="column0">
                              <div class="display-table-row">
                                 <div class="checkbox checkbox-info selectchkbox display-table-cell">
                                    <input class="styled contract-tcell-chckbox" type="checkbox" title="check" id="${contractEntry.productCode}"  > <!-- id="contract-check-1"  class="select_contract"-->
                                    <label for="${contractEntry.productCode}"></label>
                                 </div>
                                 <div class="contract-check-content display-table-cell"  style="width: 290px">
                                    <a href="${productUrl}">
                                       <spring:message code="product.jnj.id"/>
                                       #&nbsp;${contractEntry.productCode}
                                    </a>
                                    <div> ${contractEntry.productName} </div>
                                 </div>
                              </div>
                           </td>
                           <td class="column1">
                              <c:choose>
                                 <c:when test="${not empty contractEntry.contractProductPrice}">
                                    <span class="priceActive">${contractEntry.contractProductPrice}</span>
                                 </c:when>
                                 <c:otherwise>
                                    <span class="priceActive">&nbsp;</span>
                                 </c:otherwise>
                              </c:choose>
                           </td>
                           <c:if test="${documentType == quantityBasedContract}">
                              <td class="column2">
                                 <c:choose>
                                    <c:when test="${not empty contractEntry.prodSalesUnit}">
                                       <span>${contractEntry.prodSalesUnit}&nbsp;</span>
                                    </c:when>
                                    <c:otherwise>
                                       <span>&nbsp;</span>
                                    </c:otherwise>
                                 </c:choose>
                              </td>
                              <td class="column3">
                                 <c:choose>
                                    <c:when test="${not empty contractEntry.contractQty}">
                                       <span>${contractEntry.contractQty}&nbsp;</span>
                                    </c:when>
                                    <c:otherwise>
                                       <span>&nbsp;</span>
                                    </c:otherwise>
                                 </c:choose>
                              </td>
                              <td class="column4">
                                 <c:choose>
                                    <c:when test="${not empty contractEntry.consumedQty}">
                                       <span>${contractEntry.consumedQty}&nbsp;</span>
                                    </c:when>
                                    <c:otherwise>
                                       <span>&nbsp;</span>
                                    </c:otherwise>
                                 </c:choose>
                              </td>
                              <td class="column5">
                                 <c:choose>
                                    <c:when test="${not empty contractEntry.contractBalanceQty}">
                                       <span>${contractEntry.contractBalanceQty}&nbsp;</span>
                                    </c:when>
                                    <c:otherwise>
                                       <span>&nbsp;</span>
                                    </c:otherwise>
                                 </c:choose>
                              </td>
                           </c:if>
                            <c:if test="${documentType == 'ZCI'}">
                              <td class="column2">
                                 <c:choose>
                                    <c:when test="${not empty contractEntry.prodSalesUnit}">
                                       <span>${contractEntry.prodSalesUnit}&nbsp;</span>
                                    </c:when>
                                    <c:otherwise>
                                       <span>&nbsp;</span>
                                    </c:otherwise>
                                 </c:choose>
                              </td>
                              <td class="column3">
                                 <c:choose>
                                    <c:when test="${not empty contractEntry.contractQty}">
                                       <span>${contractEntry.contractQty}&nbsp;</span>
                                    </c:when>
                                    <c:otherwise>
                                       <span>&nbsp;</span>
                                    </c:otherwise>
                                 </c:choose>
                              </td>
                              <td class="column4">
                                 <c:choose>
                                    <c:when test="${not empty contractEntry.consumedQty}">
                                       <span>${contractEntry.consumedQty}&nbsp;</span>
                                    </c:when>
                                    <c:otherwise>
                                       <span>&nbsp;</span>
                                    </c:otherwise>
                                 </c:choose>
                              </td>
                              <td class="column5">
                                 <c:choose>
                                    <c:when test="${not empty contractEntry.contractBalanceQty}">
                                       <span>${contractEntry.contractBalanceQty}&nbsp;</span>
                                    </c:when>
                                    <c:otherwise>
                                       <span>&nbsp;</span>
                                    </c:otherwise>
                                 </c:choose>
                              </td>
                           </c:if>
                           <c:if test="${documentType == 'ZCV'}">
                              <c:forEach items="${cntrctEntryMap}" var="entry"  varStatus="count">
                                 <c:if test="${contractEntry.productCode eq entry.key}">
                                    <td class="column2">
                                       <c:choose>
                                          <c:when test="${not empty entry.value.totalAmount}">
                                             <span>
                                                <format:price priceData="${entry.value.totalAmount}" hideZeroValue="true"/>
                                                &nbsp;
                                             </span>
                                          </c:when>
                                          <c:otherwise>
                                             <span>&nbsp;</span>
                                          </c:otherwise>
                                       </c:choose>
                                    </td>
                                    <td class="column3">
                                       <c:choose>
                                          <c:when test="${not empty entry.value.consumedAmount}">
                                             <span>
                                                <format:price priceData="${entry.value.consumedAmount}"/>
                                                &nbsp;
                                             </span>
                                          </c:when>
                                          <c:otherwise>
                                             <span>&nbsp;</span>
                                          </c:otherwise>
                                       </c:choose>
                                    </td>
                                    <td class="column4">
                                       <c:choose>
                                          <c:when test="${not empty entry.value.balanceAmount}">
                                             <span>
                                                <format:price priceData="${entry.value.balanceAmount}" hideZeroValue="true"/>
                                                &nbsp;
                                             </span>
                                          </c:when>
                                          <c:otherwise>
                                             <span>&nbsp;</span>
                                          </c:otherwise>
                                       </c:choose>
                                    </td>
                                 </c:if>
                              </c:forEach>
                           </c:if>
                        </tr>
                     </c:forEach>
               </table>
            </div>
         </div>
      </div>
      <!-- Table collapse for ipad device-->
      <!--Accordian for ipad Ends here -->
      <!-- Table collapse for mobile device -->
      <div class="d-block d-sm-none jnj-panel-for-table mainbody-container">
         <table id="datatab-mobile" class="table table-bordered table-striped bordernone mobile-table contract-detail-mobi">
            <thead>
               <tr>
                  <th class="no-sort text-uppercase">
                     <div class="checkbox checkbox-info selectchkbox">
                        <input id="contract-select-all-mobi" class="styled contract-thead-chckbox" type="checkbox">   <!-- selectall_contract -->
                        <label for="contract-select-all-mobi"  id="contract-head-chck-label">
                           <spring:message code="search.result.selectAll"/>
                        </label>
                     </div>
                  </th>
               </tr>
            </thead>
            <tbody>
               <c:forEach items="${contractEntryList}" var="contractEntry" varStatus="mobstatus">
                  <tr>
                     <td class="vlign-top orderno">
                        <div class="display-table-row">
                           <div class="display-table-cell" style="vertical-align:top">
                              <div class="checkbox checkbox-info selectchkbox display-table-cell contract-tcell-chckbox-mob">
                                 <input  class="styled contract-tcell-chckbox" type="checkbox" id="${contractEntry.productCode}">  <!-- id="contract-check-mobi-1" -->
                                 <label for="contract-check-mobi-${mobstatus.count}"></label>
                              </div>
                           </div>
                           <div class="display-table-cell" style="vertical-align:top">
                              <div class="display-table-row">
                                 <div class="display-table-cell">
                                    <a data-bs-toggle="collapse" data-bs-parent="#accordion" href="#collapse${mobstatus.count}" class="toggle-link panel-collapsed skyBlue ipadacctoggle">
                                    <span class="glyphicon glyphicon-plus skyBlue toggle-plus-minus"></span>
                                    </a>
                                 </div>
                                 <div class="display-table-cell">
                                    <div class="contract-check-content display-table-cell">
                                       <a href="${productUrl}">
                                          <spring:message code="product.jnj.id"/>
                                          #&nbsp;${contractEntry.productCode}
                                       </a>
                                       <div> ${contractEntry.productName} </div>
                                    </div>
                                 </div>
                              </div>
                           </div>
                        </div>
                        <div id="collapse${mobstatus.count}" class="panel-collapse collapse">
                           <div class="panel-body">
                              <div class="sub-details-row">
                                 <div class="mob-view-label uppercase">
                                    <spring:message code="contract.itemPrice"/>
                                 </div>
                                 <c:choose>
                                    <c:when test="${not empty contractEntry.contractProductPrice}">
                                       <div>${contractEntry.contractProductPrice}</div>
                                    </c:when>
                                    <c:otherwise>
                                       <div>&nbsp;</div>
                                    </c:otherwise>
                                 </c:choose>
                              </div>
                              <c:if test="${documentType == quantityBasedContract}">
                                 <div class="sub-details-row">
                                    <div class="mob-view-label uppercase">
                                       <spring:message code="contract.unitOfMeasure"/>
                                    </div>
                                    <c:choose>
                                       <c:when test="${not empty contractEntry.prodSalesUnit}">
                                          <div>${contractEntry.prodSalesUnit}&nbsp;</div>
                                       </c:when>
                                       <c:otherwise>
                                          <div>&nbsp;</div>
                                       </c:otherwise>
                                    </c:choose>
                                 </div>
                                 <div class="sub-details-row">
                                    <div class="mob-view-label uppercase">
                                       <spring:message code="contract.contractQty"/>
                                    </div>
                                    <c:choose>
                                       <c:when test="${not empty contractEntry.contractQty}">
                                          <div>${contractEntry.contractQty}&nbsp;</div>
                                       </c:when>
                                       <c:otherwise>
                                          <div>&nbsp;</div>
                                       </c:otherwise>
                                    </c:choose>
                                 </div>
                                 <div class="sub-details-row">
                                    <div class="mob-view-label uppercase">
                                       <spring:message code="contract.consumed"/>
                                    </div>
                                    <c:choose>
                                       <c:when test="${not empty contractEntry.consumedQty}">
                                          <div>${contractEntry.consumedQty}&nbsp;</div>
                                       </c:when>
                                       <c:otherwise>
                                          <div>&nbsp;</div>
                                       </c:otherwise>
                                    </c:choose>
                                 </div>
                                 <div class="sub-details-row">
                                    <div class="mob-view-label uppercase">
                                       <spring:message code="contract.balance"/>
                                    </div>
                                    <c:choose>
                                       <c:when test="${not empty contractEntry.contractBalanceQty}">
                                          <div>${contractEntry.contractBalanceQty}&nbsp;</div>
                                       </c:when>
                                       <c:otherwise>
                                          <div>&nbsp;</div>
                                       </c:otherwise>
                                    </c:choose>
                                 </div>
                              </c:if>
                                <c:if test="${documentType = 'ZCI' }">
                                 <div class="sub-details-row">
                                    <div class="mob-view-label uppercase">
                                       <spring:message code="contract.unitOfMeasure"/>
                                    </div>
                                    <c:choose>
                                       <c:when test="${not empty contractEntry.prodSalesUnit}">
                                          <div>${contractEntry.prodSalesUnit}&nbsp;</div>
                                       </c:when>
                                       <c:otherwise>
                                          <div>&nbsp;</div>
                                       </c:otherwise>
                                    </c:choose>
                                 </div>
                                 <div class="sub-details-row">
                                    <div class="mob-view-label uppercase">
                                       <spring:message code="contract.contractQty"/>
                                    </div>
                                    <c:choose>
                                       <c:when test="${not empty contractEntry.contractQty}">
                                          <div>${contractEntry.contractQty}&nbsp;</div>
                                       </c:when>
                                       <c:otherwise>
                                          <div>&nbsp;</div>
                                       </c:otherwise>
                                    </c:choose>
                                 </div>
                                 <div class="sub-details-row">
                                    <div class="mob-view-label uppercase">
                                       <spring:message code="contract.consumed"/>
                                    </div>
                                    <c:choose>
                                       <c:when test="${not empty contractEntry.consumedQty}">
                                          <div>${contractEntry.consumedQty}&nbsp;</div>
                                       </c:when>
                                       <c:otherwise>
                                          <div>&nbsp;</div>
                                       </c:otherwise>
                                    </c:choose>
                                 </div>
                                 <div class="sub-details-row">
                                    <div class="mob-view-label uppercase">
                                       <spring:message code="contract.balance"/>
                                    </div>
                                    <c:choose>
                                       <c:when test="${not empty contractEntry.contractBalanceQty}">
                                          <div>${contractEntry.contractBalanceQty}&nbsp;</div>
                                       </c:when>
                                       <c:otherwise>
                                          <div>&nbsp;</div>
                                       </c:otherwise>
                                    </c:choose>
                                 </div>
                              </c:if>
                              <c:if test="${documentType == 'ZCV'}">
                                 <c:forEach items="${cntrctEntryMap}" var="entry"  varStatus="count">
                                    <c:if test="${contractEntry.productCode eq entry.key}">
                                       <div class="sub-details-row">
                                          <div class="mob-view-label uppercase">
                                             <spring:message code="contract.totalValue"/>
                                          </div>
                                          <c:choose>
                                             <c:when test="${not empty entry.value.totalAmount}">
                                                <div>
                                                   <format:price priceData="${entry.value.totalAmount}" hideZeroValue="true"/>
                                                   &nbsp;
                                                </div>
                                             </c:when>
                                             <c:otherwise>
                                                <div>&nbsp;</div>
                                             </c:otherwise>
                                          </c:choose>
                                       </div>
                                       <div class="sub-details-row">
                                          <div class="mob-view-label uppercase">
                                             <spring:message code="contract.consumedValue"/>
                                          </div>
                                          <c:choose>
                                             <c:when test="${not empty entry.value.consumedAmount}">
                                                <div>
                                                   <format:price priceData="${entry.value.consumedAmount}"/>
                                                   &nbsp;
                                                </div>
                                             </c:when>
                                             <c:otherwise>
                                                <div>&nbsp;</div>
                                             </c:otherwise>
                                          </c:choose>
                                       </div>
                                       <div class="sub-details-row">
                                          <div class="mob-view-label uppercase">
                                             <spring:message code="contract.contractBalance"/>
                                          </div>
                                          <c:choose>
                                             <c:when test="${not empty entry.value.balanceAmount}">
                                                <div>
                                                   <format:price priceData="${entry.value.balanceAmount}" hideZeroValue="true"/>
                                                   &nbsp;
                                                </div>
                                             </c:when>
                                             <c:otherwise>
                                                <div>&nbsp;</div>
                                             </c:otherwise>
                                          </c:choose>
                                       </div>
                                    </c:if>
                                 </c:forEach>
                              </c:if>
                           </div>
                        </div>
                     </td>
                  </tr>
               </c:forEach>
            </tbody>
         </table>
      </div>
   </div>
   <!-- Accordian for mobile ends here -->

   <div class="row">
      <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
         <input  type="button" class="btn btnclsactive pull-right contract-add-to-cart-bottom primarybtn addContractToCart" value= '<spring:message code="contract.detail.button.addtocart" />'/>
         <div class="showError col-lg-3 col-md-3 col-sm-3 col-xs-3">
            <input id="errorTemplateCart" class="btn btnclsactive contract-add-to-cart-bottom homeCartErrors" style="display:none" type="button" value='<spring:message code="homePage.errorDetails"/>' />
         </div>
         <div class="error_details  contract-add-to-cart-bottom  col-lg-3 col-md-3 col-sm-3 col-xs-3" style="display:none;color: red;" >
             <ul></ul>
         </div>
      </div>
   </div>
   <!-- Add to cart Modal pop-up to identify  contract or non contract start-->
   <div  id="contractPopuppage">
      <!-- Modal -->
      <div class="modal fade" id="contractpopup" role="dialog" data-firstLogin='true'>
         <div class="modal-dialog modalcls">
            <div class="modal-content popup">
               <div class="modal-header">
                  <button type="button" class="close clsBtn" data-bs-dismiss="modal">
                     <spring:message code="account.change.popup.close"/>
                  </button>
                  <h4 class="modal-title selectTitle" id="contractTitle"></h4>
                  <input type="hidden" id="nonContractMsgTitle"  value="<spring:message code="contract.page.addprod"/>" />
                  <input type="hidden" id="contractMsgTitle"  value="<spring:message code="contract.page.addprod2"/>" />
                  <input type="hidden" id="multiContractMsgTitle"  value="<spring:message code="contract.page.addprod3"/>" />
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
                                    <div class="info-text" id="popInfoText"></div>
                                    <input type="hidden" id="nonContractMsgInfoText" value="<spring:message code="contract.page.infotext"/>" />
                                    <input type="hidden" id="contractMsgInfoText"  value="<spring:message code="contract.page.infotext2"/>" />
                                    <input type="hidden" id="multiContractMsgInfoText"  value="<spring:message code="contract.page.infotext3"/>" />
                                 </td>
                              </tr>
                           </table>
                        </h4>
                     </div>
                  </div>
                  <div id="contractMessage"></div>
                  <input type="hidden" id="nonContractMessageText" value="<spring:message code="contract.page.msg"/>" />
                  <input type="hidden" id="contractMessageText"  value="<spring:message code="contract.page.msg2"/>" />
                  <input type="hidden" id="multiContractMessageText"  value="<spring:message code="contract.page.msg3"/>" />
                  <div class="continueques">
                     <spring:message code="contract.page.continue"/>
                  </div>
               </div>
               <div class="modal-footer ftrcls">
                  <a href="#" class="pull-left canceltxt" data-bs-dismiss="modal" id="cancel-btn-addtocart1">
                     <spring:message code="cart.common.cancel"/>
                  </a>
                  <button type="button" class="btn btnclsactive" data-bs-dismiss="modal" id="accept-btn-addtocart1" >
                     <spring:message code="contract.page.accept"/>
                  </button>
               </div>
            </div>
         </div>
      </div>
      <!-- Add to cart Modal pop-up to identify  contract or non contract end-->
   </div>
</form:form>