<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/addons/jnjb2bbrowseandsearch/responsive/product" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="label" uri="/WEB-INF/tld/addons/loginaddon/message.tld"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart"%>
<%@ taglib prefix="standardCart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/standard"%>
<%@ taglib prefix="standardCartLa" tagdir="/WEB-INF/tags/addons/jnjlacartandcheckoutaddon/responsive/cart/standard"%>
<%@ attribute name="jnjCartData" required="true" type="com.jnj.facades.data.JnjGTCartData"%>
<%@ taglib prefix="commonTags" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/common"%>
<%@ attribute name="varRowCount" required="true" type="java.lang.Integer"%>
<%@ attribute name="dropshipmentErrorData" required="true" type="java.util.Map"%>
<%@ attribute name="validationPage" required="false" type="java.lang.Boolean"%>

<input type="hidden" value="false ${refreshSapPrice}" id="refreshSapPrice">
 <!--Order Derail Row starts-->
 <form:form id="UpdateMultipleEntriesInCartForm" action="updateAll" method="post" modelAttribute="UpdateMultipleEntriesInCartForm">
 	  </form:form>
<!-- <div class="orderDetailPanel"> -->
<%-- <span><span class="txtFont"><spring:message code="cart.common.orderType" /></span><b><spring:message code="cart.common.orderType.${jnjCartData.orderType}" /></b></span> --%>
<div class="hidden-xs dropShipmentTable" id="dropShipmentTable-${varRowCount}">
	<div class="dropshipment-label-value">
		<label class="dropshipment-label"> <strong><spring:message
					code="cart.common.orderType" /></strong>
			<div class="dropshipment-value">
				<spring:message code="cart.common.orderType.${jnjCartData.orderType}" />
			</div>
		</label>
	</div>
    <table id="datatab-desktop" class="table table-bordered table-striped sorting-table" data-paging="false" data-info="false">
        <thead>
            <tr>
                <th class="no-sort text-left">
                    <spring:message code="cart.validate.product"/>
                </th>
                <c:if test="${displayIndirectCustomerHeader}">
                    <th class="text-left no-sort"><spring:theme code="text.account.buildorder.indirectCustomer"
                            text="INDIRECT CUSTOMER" />
                    </th>
                </c:if>
                <c:if test="${displayIndirectPayerHeader}">
                    <th class="text-left no-sort">
                        <spring:theme code="text.account.buildorder.indirectPayer" text="INDIRECT PAYER" /></th>
                </c:if>
                <th class="no-sort">
                    <spring:message code="cart.validate.quantity"/>
                    <div class="cart-update-all-link">
                        <a class="cartUpdateAllbtn cartUpdateAllbutton"  href="javascript:;">
                            <spring:message code="cart.review.entry.Updateall"/>
                        </a>
                    </div>
                </th>
                <th class="no-sort"><spring:message code="cart.validate.unitPrice"/></th>
                <th class="no-sort"><spring:message code="cart.review.entry.total"/></th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${jnjCartData.entries}" var="entry"  varStatus="count">
                <tr  id="orderentry-${count.count}">
                    <td class="text-left">
                        <c:if test="${dropshipmentErrorData.containsKey(entry.product.code)}">

                            <p style="font-size:13px;color:#b41601">
                            <spring:message code="cart.review.dropshipmentProductError" />
                            </p>
                        </c:if>
                        <standardCartLa:productDescriptionBeforeValidation entry="${entry}" showRemoveLink="true" showStatus="false"/>
                        <div class="txtWidth text-left">
							<c:choose>
								<c:when test="${cartData.partialDelivFlag && !cartData.holdCreditCardFlag && validationPage && showATPFlagMap.get(entry)}">
									<standardCartLa:entryMoreInfo entry="${entry}" showInfoLbl="true" showDescLbl="false"/>
								</c:when>
								<c:otherwise>
						       		<fmt:formatDate pattern="MM/dd/yyyy" value="${entry.expectedDeliveryDate}" />
						    	</c:otherwise>
							</c:choose>
					    </div>
                     </td>
                    <!-- Checking indirect customer line level flag -->
                    <c:if test="${displayIndirectCustomerLine[count.index]}">
                        <td class="txtWidth">
                            <p class="descMid" id="indirectCustomerId${count.index}">${entry.indirectCustomer}</p>
                            <p class="descMid" id="indirectCustomerName${count.index}">${entry.indirectCustomerName}</p>
                        </td>
                    </c:if>
                    <c:if test="${displayIndirectCustomerHeader eq true && (empty displayIndirectCustomerLine || displayIndirectCustomerLine[count.index] eq false)}">
                        <td></td>
                    </c:if>

                    <!-- Checking indirect payer line level flag -->
                    <c:if test="${displayIndirectPayerLine[count.index]}">
                        <td class="txtWidth">
                            <p class="descMid" id="indirectPayerId${count.index}">${entry.indirectPayer}</p>
                            <p class="descMid" id="indirectPayerName${count.index}" >${entry.indirectPayerName}</p>
                        </td>
                    </c:if>
                    <c:if test="${displayIndirectPayerHeader eq true && (empty displayIndirectPayerLine || displayIndirectPayerLine[count.index] eq false)}">
                        <td></td>
                    </c:if>
                    <td class="valign-middle">
                       
                        <c:url value="/cart/update" var="cartUpdateFormAction" />
                        <form:form id="updateCartForm${entry.entryNumber}" action="${cartUpdateFormAction}" method="post" modelAttribute="updateQuantityForm${entry.entryNumber}">
                            <input type="hidden" name="entryNumber" value="${entry.entryNumber}"/>
                            <input type="hidden" name="productCode" value="${entry.product.code}"/>
                            <input type="hidden" name="initialQuantity" value="${entry.quantity}"/>
                            <div>
                                <ycommerce:testId code="cart_product_quantity">
                                    <form:label cssClass="skip" path="quantity" for="quantity${entry.entryNumber}"></form:label>
                                    <c:choose>
                                    <c:when test="${entry.availabilityStatus eq 'notAvailable' || entry.availabilityStatus eq 'partialyAvailable' || entry.product.productStatusCode eq 'D3'}">
                                    <c:if test="${validateOrderData.validateOrderResponse}">
                                    <c:if  test="${displaySubstitutes eq true && entry.product.productReferences.size() >0 }">
										 <button type="button" class="btn btn-primary" id="testModal" data-bs-toggle="modal" data-bs-target="#myModal" style="display:block"><spring:message code="cart.validate.substitute.popup.button.subsProducts"/></button>
									</c:if>
								    </c:if>
									 </c:when>
									  </c:choose>
                          <div class="cart-update-link SubsProdPaddingMargin1" >
                            <a href="javascript:void(0);" id="quantity_${entry.entryNumber}" entryNum="${entry.entryNumber}" class="laUpdateQuantityProduct" >
                                <spring:message code="cart.review.productDesc.updateItem"/>
                            </a>
                          </div>
                                             	<!-- Modal -->
                                             <!-- Large modal -->
                                             
                                   <c:choose>
                                    <c:when test="${entry.availabilityStatus eq 'notAvailable' || entry.availabilityStatus eq 'partialyAvailable' || entry.product.productStatusCode eq 'D3'}">
                                    <c:if test="${validateOrderData.validateOrderResponse}">
                                    <c:if  test="${displaySubstitutes eq true && entry.product.productReferences.size() >0 }">
                                         										   																
																
									</c:if>
                                    <form:input disabled="${not entry.updateable}"  type="text"  id="quantity_${entry.entryNumber}" entryNumber="${entry.entryNumber}" class=" laQtyUpdateTextBox form-control txtWidth redBorder" path="quantity"/>
                                    </c:if>
                                    </c:when>
                                    <c:otherwise>
                                    <form:input disabled="${not entry.updateable}"  type="text"  id="quantity_${entry.entryNumber}" entryNumber="${entry.entryNumber}" class=" laQtyUpdateTextBox form-control txtWidth" path="quantity"/>
                                    </c:otherwise>
                                    </c:choose>
                                    &nbsp;${entry.product.salesUnitCode}
                                </ycommerce:testId>
                            </div>
                            <p class="thirdline">
                                <spring:message code="product.multiple" />&nbsp;${entry.product.multiplicity}
                            </p>
                            <ycommerce:testId code="cart_product_removeProduct">
                                <p>
                                    <a href="javascript:void(0);" id="RemoveProduct_${entry.entryNumber}"
                                        class="smallFont laSubmitRemoveProduct"><spring:message code="cart.review.productDesc.removeItem"/>
                                    </a></p>
                            </ycommerce:testId>
                            <p class="msgHighlight">${entry.product.hazmatCode}</p>
                        </form:form>

                    </td>

                    <td class="valign-middle" id="basePrice_${entry.entryNumber}">
                        <format:price priceData="${entry.basePrice}"/>
                    </td>

                    <td  class="valign-middle " id="totalPrice_${entry.entryNumber}">
                            <ycommerce:testId code="cart_totalProductPrice_label">
                                <format:price priceData="${entry.totalPrice}"/>
                            </ycommerce:testId>
                    </td>

                    <!-- Free item -->
                    <c:if test="${freeGoodsMap ne null}">
               		    <c:set var="valueObject" value="${freeGoodsMap[entry.product.code]}" />
                 		    <c:if test="${not empty valueObject.itemCategory}">

         			            <!-- Bonus item -->
             			            <c:if test="${freeGoodsMap[entry.product.code] == valueObject.materialNumber}">
                                        <tr class="noborder" id="freeGood${entry.product.code}">
                          			        <td class="text-right">
                           			            <standardCart:productDescriptionBeforeValidation entry="${entry}" showRemoveLink="false" showStatus="false" />
                           			        </td>
                           			        <td>
                          	    		        <div class="text-center">
                           					        <c:url value="/cart/update" var="cartUpdateFormAction" />
                           					        <form:form id="updateCartForm${entry.entryNumber}"
                           						        action="${cartUpdateFormAction}" method="post"
                           						        modelAttribute="updateQuantityForm${entry.entryNumber}">
                           						        <input type="hidden" name="entryNumber"	value="${entry.entryNumber}" />
                           						        <input type="hidden" name="productCode"	value="${entry.product.code}" />
                           						        <input type="hidden" name="initialQuantity" value="${entry.quantity}" />
                           						        <input type="hidden" size="4" value="${valueObject.materialQuantity}"
                           						            id="freeGoodQuantity${entry.product.code}" />
                           						        <div id="freeGoodQuantity${entry.product.code}">${valueObject.materialQuantity}</div>
                               							    <p class="thirdline">
                           								        ${entry.product.deliveryUnit}
                            							    </p>
                            					    </form:form>
                            				    </div>
                            			    </td>
                            			    <td class="valign-middle"></td>
                            			    <td class="valign-middle"> <spring:message code="cart.freeitem.message" /> </td>
                            		    </tr>
                 			        </c:if>

                 			        <!-- Alternate material -->
                                    <c:if test="${freeGoodsMap[entry.product.code] != valueObject.materialNumber}">
                                        <tr class="noborder">
                 				    	    <td class="text-right">
                 				    	        <div class="display-row">
                 				    	            <div class="table-cell" style="vertical-align: middle;">
                                                        <product:productPrimaryImage product="${entry.product}" format="cartIcon" />
                                                    </div>
                 				    	            <div class="table-cell" style="vertical-align: middle;"></div>
                                                        <div class="table-cell" style="padding-left: 15px;">
                                                            <span class="Tablesubtxt">
                                                                <p class="secondline">
                                                                    <spring:message code="cart.review.productDesc.jnJID"/>
                                                                    <span class="strong"> ${valueObject.materialNumber}</span>
                                                                </p>
                                                            </span>
                                                        </div>
                                                    </div>
                                                </div>
                 						    </td>
                 					        <c:if test="${not empty entry.indirectCustomer}">
                                                <td class="valign-middle"></td>
                                            </c:if>
                 					        <td>
                 						        <div class="text-center"> ${valueObject.materialQuantity} &nbsp; ${valueObject.salesUOM} </div>
                 						    </td>
                 						    <td class="valign-middle">
                 						        <span class="txt-nowrap">
                 						            <spring:message code="cart.freeitem.message" />
                 						        </span>
                 						    </td>
                 						    <td class="valign-middle"></td>
                 					    </tr>
                 			        </c:if>
                                </c:if>
              			    </c:if>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</div>

		<c:set var="cartCount" value="0"/>
		<c:forEach items="${cartData.entries}" var="entry" varStatus="count">
			<c:set var="cartCount" value="${count.count}"/>
		</c:forEach>
		<input type="hidden" name="numberOfItemscart" id="numberOfItemscart" value="${cartCount}"/>
					
	<div class="row basecontainer">
	
		<standardCartLa:multiValidateCartTotalItem cartData="${jnjCartData}" />
	</div>
	
 <div class="modal fade substituteModal" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel">
                                               <div class="modal-dialog modal-lg" role="document">
                                                  <div class="modal-content">
                                                             <div class="modal-header SubsProdModelBorder" >
                                                                 <button type="button" class="close clsBtn" data-bs-dismiss="modal" id="select-accnt-close">
                                                                   <spring:message code="cart.validate.substitute.popup.button.close"/>
                                                                 </button>
                                                                 <h4 class="modal-title selectTitle">
                                                                    <spring:message code="cart.validate.substitute.popup.heading"/>
                                                                 </h4>
                                                             </div>

                                                             <div class="modal-body">
                                                             <p><spring:message code="cart.validate.substitute.popup.notification.message"/></p>
                                                             <c:url value="/cart/replaceProducts" var="jnjLaProductReplacementAction" />
                                                             <form:form method="post" id="jnjLaProductReplacementForm" name="jnjLaProductReplacementForm" action="${jnjLaProductReplacementAction}">
                                                                                 <table id="datatab-desktop" data-paging="false" data-info="false"
                                                                                 class="table table-bordered table-striped">
                                                                                 <thead>
                                                                                      <tr>
                                                                                     
                                                                                          <th class="SubsProdPaddingMargin2"><label class="noWrapLabel" ><input  type='checkbox' id='headercheck_${entry.entryNumber}' class="margin:0 10px"/><spring:message code="cart.validate.substitute.popup.tableHeading.selectAll"/></label></th>
                                                                                          <th class="SubsProdPaddingMargin4" ><spring:message code="cart.validate.substitute.popup.tableHeading.line"/></th>
                                                                                          <th><spring:message code="cart.validate.substitute.popup.tableHeading.odrItemCode"/></th>
                                                                                          <th><spring:message code="cart.validate.substitute.popup.tableHeading.odrItemDes"/></th>
                                                                                          <th><spring:message code="cart.validate.substitute.popup.tableHeading.odrItemQuan"/></th>
                                                                                          <th><spring:message code="cart.validate.substitute.popup.tableHeading.proItemCode"/></th>
                                                                                          <th><spring:message code="cart.validate.substitute.popup.tableHeading.proItemDes"/></th>
                                                                                          <th><spring:message code="cart.validate.substitute.popup.tableHeading.proItemQuan"/></th>
                                                                                        </tr>
                                                                                   </thead>

                                                                                   <tbody>
                                                                                   
                                                                                   <tr>
                                                                                   
                                                                                   
                                                                                   </tr>
                                                                                   <c:forEach items="${cartData.entries}" var="entry" varStatus="count">
                                                                                   <c:if test="${not empty entry.product.productReferences}">
                                                                                     <tr class='substituteTr'>
                                                                                     
                                                                                         <td><input class="singleSelect" type='checkbox' id='check_${entry.entryNumber}'/></td>
                                                                                         <td class='entryNumber' name="entryNumber">${entry.entryNumber}</td>
                                                                                         <td class='productCode' name="productCode">${entry.product.code}</td>
                                                                                         <input type="hidden" value="${entry.entryNumber}" name="originalCartEntryNumber"/>
                                                                                         <input type="hidden" value="${entry.product.code}" name="originalProductCode"/>
                                                                                         
                                                                                         <td>
                                                                                             <p>${entry.product.name}</p>
                                                                                         </td>

                                                                                         <td>${entry.quantity}</td>
                                                                                         
                                                                                         <td>
                                                                                          <span class="orderCodeData">
                                                                                            <c:forEach items="${entry.product.productReferences}" var="reference" varStatus="count">
                                                                                             <p>
                                                                                                <input class="replacementProductCode_${count.index}" type="radio" id="code1" name="replacementProductCode_${entry.product.code}" value="${reference.target.code}">
                                                                                                <label class="label-class" for="code1">${reference.target.code}</label>
                                                                                              </p>
                                                                                               </c:forEach>
                                                                                            <input type="hidden" value="" name="replacementProductCode" id="replacementProductCode"/>
                                                                                          </span>

                                                                                          </td>
                                                                                             
                                                                                         <td> <c:forEach items="${entry.product.productReferences}" var="reference" varStatus="count">
                                                                                                 <p>${reference.target.name}</p>
                                                                                                 </c:forEach>  
                                                                                         </td>
                                                                                         
                                                                                         <td>
                                                                                             <p>
                                                                                                 <input type="text" class="form-control quantity" id="exampleInputPassword1" name="replacementProductQuantity" style=" text-align: center; " value="${entry.quantity}">
                                                                                             </p>
                                                                                         </td>
                                                                                      
                                                                                         
                                                                                   </tr>
                                                                                   </c:if>
                                                                                   </c:forEach>
                                                                                   
                                                                                   </tbody>

                                                                                 </table>
                                                                                 </form:form>
                                                                              </div>


                                                             <div class="modal-footer">
                                                                 <button type="button" class="btn btnclsactive "  id="substituteAdd">
                                                                         <spring:message code="cart.validate.substitute.popup.button.add"/>
                                                                 </button>

                                                                 <button type="button" class="close clsBtn floatleft" data-bs-dismiss="modal" id="select-accnt-close">
                                                                    <spring:message code="cart.validate.substitute.popup.button.cancel"/>
                                                                 </button>

                                                             </div>
                                                         </div>
                                               </div>
                                             </div>
