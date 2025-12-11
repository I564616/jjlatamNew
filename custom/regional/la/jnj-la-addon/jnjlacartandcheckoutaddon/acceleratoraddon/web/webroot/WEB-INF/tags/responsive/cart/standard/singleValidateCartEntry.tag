<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/addons/loginaddon/message.tld"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart"%>
<%@ taglib prefix="commonTags" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/common"%>
<%@ attribute name="validationPage" required="false" type="java.lang.Boolean"%>
<%@ taglib prefix="breadcrumb" tagdir="/WEB-INF/tags/responsive/nav/breadcrumb"%>
<%@ taglib prefix="standardCart" tagdir="/WEB-INF/tags/addons/jnjlacartandcheckoutaddon/responsive/cart/standard"%>
<%@ attribute name="dropshipmentErrorData" required="true" type="java.util.Map"%>
<script type="text/javascript">
	// set vars
	/*<![CDATA[*/var cartRemoveItem = true;
	/*]]>*/ 	
</script>
<form:form id="UpdateMultipleEntriesInCartForm" action="updateAll"
	modelAttribute="UpdateMultipleEntriesInCartForm">
</form:form>
<div id="AddItemsCartpage">
	<div class="mainbody-container">
		<div class="d-none d-sm-block">
			<table id="datatab-desktop"
				class="table table-bordered table-striped sorting-table"
				 data-paging="false" data-info="false" >
				<thead>
					<tr>
						<th class="no-sort text-left">
						    <spring:message code="cart.validate.product" /></th>
						<c:if test="${displayIndirectCustomerHeader}">
							<th class="text-left no-sort">
							    <spring:theme code="text.account.buildorder.indirectCustomer" text="INDIRECT CUSTOMER" />
							</th>
						</c:if>
						<c:if test="${displayIndirectPayerHeader}">
							<th class="text-left no-sort">
							    <spring:theme code="text.account.buildorder.indirectPayer" text="INDIRECT PAYER" />
							</th>
						</c:if>
						<th class="no-sort">
						    <spring:message	code="cart.validate.quantity" />
							<div class="cart-update-all-link">
								<a class="cartUpdateAllbtn cartUpdateAllbutton" id="cartUpdateAllbutton" href="javascript:;">
									<spring:message code="cart.review.entry.Updateall" />
								</a>
							</div>
						</th>
						<th class="no-sort">
						    <spring:message	code="cart.validate.unitPrice" />
						</th>
						<th class="no-sort">
						    <spring:message code="cart.review.entry.total" />
						</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${cartData.entries}" var="entry" varStatus="count">
						<tr id="orderentry-${count.count}">
							<td class="text-left">
							    <c:if test="${dropshipmentErrorData.containsKey(entry.product.code)}">
                              		<p style="font-size:13px;color:#b41601">
							       	    <spring:message code="cart.review.dropshipmentProductError" />
						      		</p>
                        		</c:if>
							<standardCart:productDescriptionBeforeValidation
									entry="${entry}" showRemoveLink="true" showStatus="false" />
								<div class="txtWidth text-left">
							        <c:choose>
								        <c:when test="${cartData.partialDelivFlag && !cartData.holdCreditCardFlag && validationPage && showATPFlagMap.get(entry)}">
									        <standardCart:entryMoreInfo entry="${entry}" showInfoLbl="true" showDescLbl="false"/>
								        </c:when>
                                        <c:otherwise>
						       		        <fmt:formatDate pattern="MM/dd/yyyy" value="${entry.expectedDeliveryDate}" />
						    	        </c:otherwise>
							        </c:choose>
					            </div>
                   			</td>
							<!-- Checking indirect customer line level flag -->
							<c:if test="${displayIndirectCustomerLine[count.index]}">
								<td class="txtWidth valign-middle">
									<p class="descMid" id="indirectCustomerId${count.index}">${entry.indirectCustomer}</p>
									<p class="descMid" id="indirectCustomerName${count.index}">${entry.indirectCustomerName}</p>
								</td>
							</c:if>
							<c:if
								test="${displayIndirectCustomerHeader eq true && (empty displayIndirectCustomerLine || displayIndirectCustomerLine[count.index] eq false)}">
								<td></td>
							</c:if>
							<!-- Checking indirect payer line level flag -->
							<c:if test="${displayIndirectPayerLine[count.index]}">
								<td class="txtWidth valign-middle">
									<p class="descMid" id="indirectPayerId${count.index}">${entry.indirectPayer}</p>
									<p class="descMid" id="indirectPayerName${count.index}">${entry.indirectPayerName}</p>
								</td>
							</c:if>
							<c:if
								test="${displayIndirectPayerHeader eq true && (empty displayIndirectPayerLine || displayIndirectPayerLine[count.index] eq false)}">
								<td></td>
							</c:if>
							<td class="valign-middle">
								
								<c:url value="/cart/update" var="cartUpdateFormAction" />
								<form:form id="updateCartForm${entry.entryNumber}"
									action="${cartUpdateFormAction}" method="post"
									modelAttribute="updateQuantityForm${entry.entryNumber}">
									<input type="hidden" name="entryNumber" value="${entry.entryNumber}" />
									<input type="hidden" name="productCode" value="${entry.product.code}" />
									<input type="hidden" name="initialQuantity" value="${entry.quantity}" />
									<div>
										<ycommerce:testId code="cart_product_quantity">
											<form:label cssClass="skip" path="quantity"
												for="quantity${entry.entryNumber}"></form:label>
											<c:choose>
											<c:when test="${entry.availabilityStatus eq 'notAvailable' || entry.availabilityStatus eq 'partialyAvailable' || entry.product.productStatusCode eq 'D3'}">
											<c:if test="${validateOrderData.validateOrderResponse}">
											<c:if  test="${displaySubstitutes eq true && entry.product.productReferences.size() >0 }">
										    <button type="button" class="btn btn-primary" id="testModal" data-bs-toggle="modal" data-bs-target="#myModal" ><spring:message code="cart.validate.substitute.popup.button.subsProducts"/></button>
										    </c:if>
										    </c:if>
										    </c:when>
										    </c:choose>
<div class="cart-update-link SubsProdPaddingMargin1">
									<a href="javascript:void(0);" id="quantity_${entry.entryNumber}"
										entryNum="${entry.entryNumber}" class="laUpdateQuantityProduct">
										<spring:message code="cart.review.productDesc.updateItem" />
									</a>
								</div>
                                             	<!-- Modal -->
                                             <!-- Large modal -->
                                             <!-- <button type="button" class="btn btn-primary" id="testModal" data-bs-toggle="modal" data-bs-target="#myModal" style="display:block">test modal</button>-->
                                         <c:choose>
											<c:when test="${entry.availabilityStatus eq 'notAvailable' || entry.availabilityStatus eq 'partialyAvailable' || entry.product.productStatusCode eq 'D3'}">
											<c:if test="${validateOrderData.validateOrderResponse}">
											<c:if  test="${displaySubstitutes eq true && entry.product.productReferences.size() >0 }">
                                            
                                             <!---end of modal pop-up -->
										   																
										   	</c:if>
											<form:input disabled="${not entry.updateable}" type="text"
												id="quantity_${entry.entryNumber}"
												entryNumber="${entry.entryNumber}"
												class=" laQtyUpdateTextBox form-control txtWidth redBorder focusRed"
												path="quantity" />
												
												 </c:if>
											</c:when>
											<c:otherwise>
											<form:input disabled="${not entry.updateable}" type="text"
												id="quantity_${entry.entryNumber}"
												entryNumber="${entry.entryNumber}"
												class=" laQtyUpdateTextBox form-control txtWidth"
												path="quantity" />
											</c:otherwise>
											</c:choose>
                                                 <div class="qntunitval">${entry.product.salesUnitCode}</div>
                                        </ycommerce:testId>
									</div>
									<p class="thirdline">
										<spring:message code="product.multiple" />
										&nbsp;${entry.product.multiplicity}
									</p>
									<ycommerce:testId code="cart_product_removeProduct">
										<p>
											<a href="javascript:void(0);"
												id="RemoveProduct_${entry.entryNumber}"
												class="smallFont laSubmitRemoveProduct">
												<spring:message	code="cart.review.productDesc.removeItem" />
											</a>
										</p>
									</ycommerce:testId>
									<p class="msgHighlight">${entry.product.hazmatCode}</p>
								</form:form>
							</td>
							<td class="valign-middle" id="basePrice_${entry.entryNumber}">
								<format:price priceData="${entry.basePrice}" />
							</td>
							<td class="valign-middle " id="totalPrice_${entry.entryNumber}">
								<ycommerce:testId code="cart_totalProductPrice_label">
									<format:price priceData="${entry.totalPrice}" />
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
		<!-- Table collapse for mobile device-->
		<div class="d-block d-sm-none">
			<table id="datatab-mobile"
				class="table table-bordered table-striped sorting-table">
				<thead>
					<tr>
						<th class="no-sort text-left"><spring:message
								code="cart.validate.product" /></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${cartData.entries}" var="entry"
						varStatus="count">
						<tr>
							<td class="text-left">
							    <standardCart:productDescriptionBeforeValidation
							        entry="${entry}" showRemoveLink="true" showStatus="false" />
								<p>
									<spring:message code="cart.validate.quantityQty" />
								</p>
								<c:url value="/cart/update" var="cartUpdateFormAction" />
								<form:form id="updateCartForm${entry.entryNumber}"
									action="${cartUpdateFormAction}" method="post"
									modelAttribute="updateQuantityForm${entry.entryNumber}">
									<input type="hidden" name="entryNumber" value="${entry.entryNumber}" />
									<input type="hidden" name="productCode" value="${entry.product.code}" />
									<input type="hidden" name="initialQuantity" value="${entry.quantity}" />
									<ycommerce:testId code="cart_product_quantity">
										<form:label cssClass="skip" path="quantity"
											for="quantity${entry.entryNumber}">
											<spring:theme code="basket.page.quantity" />
										</form:label>
										<c:choose>
										<c:when test="${entry.availabilityStatus eq 'notAvailable' || entry.availabilityStatus eq 'partialyAvailable'}">
										<c:if test="${validateOrderData.validateOrderResponse}">										
										<c:if  test="${displaySubstitutes eq true && entry.product.productReferences.size() >0 }">
										  <button type="button" class="btn btn-primary" id="testModal" data-bs-toggle="modal" data-bs-target="#myModal" style="display:block"><spring:message code="cart.validate.substitute.popup.button.subsProducts"/></button>

                                             	<!-- Modal -->
                                             <!-- Large modal -->
                                             <!-- <button type="button" class="btn btn-primary" id="testModal" data-bs-toggle="modal" data-bs-target="#myModal" style="display:block">test modal</button>-->

                                          
                                             <!---end of modal pop-up -->
										 </c:if>  																
										
										<form:input disabled="${not entry.updateable}" type="text"
											id="quantity_${entry.entryNumber}"
											entryNumber="${entry.entryNumber}"
											class=" laQtyUpdateTextBox form-control txtWidth redBorder"
											path="quantity" />
											
											</c:if>
										</c:when>
										<c:otherwise>
										<form:input disabled="${not entry.updateable}" type="text"
											id="quantity_${entry.entryNumber}"
											entryNumber="${entry.entryNumber}"
											class=" laQtyUpdateTextBox form-control txtWidth"
											path="quantity" />
										</c:otherwise>
										</c:choose>
									</ycommerce:testId>
									<ycommerce:testId code="cart_product_removeProduct">
										<a href="javascript:void(0);"
											id="RemoveProduct_${entry.entryNumber}"
											class="smallFont laSubmitRemoveProduct"> <spring:message
												code="cart.review.productDesc.removeItem" />
										</a>
									</ycommerce:testId>
									<p class="msgHighlight">${entry.product.hazmatCode}</p>
								</form:form>
								<p>
									<spring:message code="cart.validate.unitPrice" />
								</p>
								<p>
									<format:price priceData="${entry.basePrice}" />
								</p>
								<p>
									<spring:message code="cart.review.entry.total" />
								</p>
								<p>
									<ycommerce:testId code="cart_totalProductPrice_label">
										<p class="jnjID">
											<format:price priceData="${entry.totalPrice}" />
										</p>
									</ycommerce:testId>
								</p>
							</td>








						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		<!--Accordian Ends here -->
		<!-- Start - Total Price Summary -->
		<div class="row basecontainer">
			<table class="total-summary-table">
				<tr>
					<td class="total-summary-label"><spring:message
							code="cart.common.subTotal" /></td>
					<td class="total-summary-cost totalrps no-right-pad"><format:price
							priceData="${cartData.subTotal}" /></td>
				</tr>
				<c:if test="${cartData.totalFreightFees.value > 0}">
				    <tr class="summary-bline">
					    <td class="total-summary-label"><spring:message code="cart.review.shoppingCart.shipping" /></td>
					    <td class="total-summary-cost totalrps no-right-pad">
					        <format:price priceData="${cartData.totalFreightFees}" /></td>
				    </tr>
				</c:if>
				<c:if test="${cartData.totalFees.value > 0}">
				    <tr>
					    <td class="total-summary-label"><spring:message code="cart.total.totalFees" /></td>
					    <td class="total-summary-cost totalrps no-right-pad"><format:price
					        priceData="${cartData.totalFees}" /></td>
				    </tr>
				</c:if>
				<c:if test="${cartData.totalTax.value > 0}">
				    <tr>
					    <td class="total-summary-label"><spring:message code="order.history.taxes" /></td>
					    <td class="total-summary-cost totalrps no-right-pad"><format:price
					        priceData="${cartData.totalTax}" /></td>
				    </tr>
				</c:if>
				<c:if test="${cartData.discountTotal.value > 0}">
				    <tr>
					    <td class="total-summary-label"><spring:message code="order.history.discounts" /></td>
					    <td class="total-summary-cost totalrps no-right-pad"><format:price
							priceData="${cartData.discountTotal}" /></td>
				    </tr>
				</c:if>
				<tr class="total-price-row">
					<td class="total-summary-label"><spring:message
							code="cart.review.shoppingCart.total" /></td>
					<td class="total-summary-cost totalsum no-right-pad"><format:price
							priceData="${cartData.totalGrossPrice}" /></td>
				</tr>
			</table>
		</div>
	</div>
	<!-- End - Total Price Summary -->
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
                                                                                     
                                                                                          <th class="SubsProdPaddingMargin2" ><label class="noWrapLabel"><input  type='checkbox' id='headercheck_${entry.entryNumber}' class="paddingR8"/><spring:message code="cart.validate.substitute.popup.tableHeading.selectAll"/></label></th>
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
