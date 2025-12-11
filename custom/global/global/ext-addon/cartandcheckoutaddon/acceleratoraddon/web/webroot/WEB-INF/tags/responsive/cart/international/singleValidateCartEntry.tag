<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="label" uri="/WEB-INF/tld/addons/loginaddon/message.tld"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart"%>
<%@ taglib prefix="standardCart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/standard"%>
<%@ taglib prefix="commonTags" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/common"%>
<%@ taglib prefix="international" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/international"%>
<script type="text/javascript"> // set vars
/*<![CDATA[*/ var cartRemoveItem = true;
/*]]>*/
</script>
<form:form id="UpdateMultipleEntriesInCartForm" action="updateAll" commandName="UpdateMultipleEntriesInCartForm"></form:form>
<div id="AddItemsCartpage">
	<div class="mainbody-container">
		
		<div class="hidden-xs">
		<div class="dropshipment-label-value">
				<div class="dropshipment-label" style="margin-top:10px"> <strong><spring:message
							code="cart.common.orderType" /></strong>
							
					<span>&nbsp;<spring:message code="cart.common.orderType.${cartData.orderType}" /></span>
					
				</div>
			</div>		<table id="datatab-desktop"	class="table table-bordered table-striped sorting-table-lines fee-price-table">
		<thead>
					<tr>
						<th class="no-sort text-left text-uppercase">
						<spring:message	code="cart.review.entry.numberText" />
						</th>
						<th class="no-sort text-left">
						<spring:message	code="cart.validate.product" /></th>
						<th class="no-sort"><spring:message code="cart.validate.quantity" />
								<div class="cart-update-all-link">
								<a class="cartUpdateAllbtn cartUpdateAllbutton"	id="cartUpdateAllbutton" href="javascript:;">
								<spring:message	code="cart.review.entry.Updateall" />
								</a>
						    	</div>
						</th>
						<th class="no-sort unitprice-thead">
						<spring:message	code="cart.validate.unitPrice" /></th>
						<th class="no-sort total-thead fee-cell">
						<spring:message code="cart.review.entry.total" />
						</th>
					</tr>
				</thead>
				<tbody>
		 
					<c:forEach items="${cartData.entries}" var="entry"	varStatus="count">
						<tr id="orderentry-${count.count}">
							<td class="text-left">${count.count}</td>
							<td class="text-left">
							<standardCart:productDescriptionBeforeValidation entry="${entry}" priceError="${priceValidationErrorMsg}"
							showRemoveLink="true" showStatus="false" /></td>
							<td class="valign-middle">
								<div class="cart-update-link">
									<a href="javascript:void();" id="quantity${entry.entryNumber}_update" entryNum="${entry.entryNumber}" class="qtyUpdateTextBox1">
									<spring:message	code="cart.review.productDesc.updateItem" /></a>
								</div> 
								<c:url value="/cart/update" var="cartUpdateFormAction" /> 
								<form:form	id="updateCartForm${entry.entryNumber}_desktop"	action="${cartUpdateFormAction}" method="post" commandName="updateQuantityForm${entry.entryNumber}">
									<c:if test="${freeGoodsMap ne null}">
										<c:set var="freeObject"	value="${freeGoodsMap[entry.product.code]}" />
									</c:if>
									<input type="hidden" name="entryNumber"	value="${entry.entryNumber}" />
									<input type="hidden" name="productCode"	value="${entry.product.code}" />
									<input type="hidden" name="initialQuantity"	value="${entry.quantity}" />
									<div>
										<ycommerce:testId code="cart_product_quantity">

											<form:label cssClass="skip" path="quantity"	for="quantity${entry.entryNumber}"></form:label>
											<form:input disabled="${not entry.updateable}"  type="text"  id="quantity${entry.entryNumber}" entryNumber="${entry.entryNumber}" class="insertInput qtyUpdateTextBox qtyUpdateTextBox form-control txtWidth" path="quantity"  onclick="hideFreeGood('${entry.product.code}', this,'${freeObject.orderedQuantity}');"/>
										</ycommerce:testId>
									</div>
									<p class="thirdline">
										<spring:message code="product.detail.addToCart.unit" />	${entry.product.deliveryUnit}(${entry.product.numerator}&nbsp;${entry.product.salesUnit})
									</p>

									<ycommerce:testId code="cart_product_removeProduct">
										<p>
											<a href="javascript:void();"id="RemoveProduct_${entry.entryNumber}"	class="smallFont submitRemoveProduct">
											<spring:message	code="cart.review.productDesc.removeItem" />
											 </a>
										</p>
									</ycommerce:testId>
									<p class="msgHighlight">${entry.product.hazmatCode}</p>
								</form:form>

							</td>
							<td class="valign-middle" id="basePrice_${entry.entryNumber}">
							      <c:set var="contains" value="false" />
							     <c:forEach var="item" items="${kitValidationErrorMsg}">
									<c:if test="${item == entry.product.code}">
										<c:set var="contains" value="true" />
									</c:if>
								</c:forEach>
								<c:choose>
									<c:when test="${contains eq true}">
 									 </c:when>
									<c:otherwise>
  									 <format:price priceData="${entry.basePrice}" />
  									</c:otherwise>
								</c:choose>
							</td>
							<td class="valign-middle totalrps fee-cell"	id="totalPrice_${entry.entryNumber}">
							
							 <c:set var="contains" value="false" />
							     <c:forEach var="item" items="${kitValidationErrorMsg}">
									<c:if test="${item == entry.product.code}">
										<c:set var="contains" value="true" />
										
									</c:if>
								</c:forEach>
								<c:choose>
									<c:when test="${contains eq true}">
 									 </c:when>
									<c:otherwise>
  									<ycommerce:testId code="cart_totalProductPrice_label">
									<format:price priceData="${entry.totalPrice}" />
								  </ycommerce:testId>
  									</c:otherwise>
								</c:choose>
							
								
								</td>
						</tr>
						<!-- Changes for Bonus Item -->
						<c:if test="${freeGoodsMap ne null}">
							<c:set var="valueObject" value="${freeGoodsMap[entry.product.code]}" />
							<c:if test="${not empty valueObject.itemCategory}">
								<tr class="noborder" id="freeGood${entry.product.code}">
									<td class="text-right">
									<standardCart:productDescriptionBeforeValidation entry="${entry}" priceError="${priceValidationErrorMsg}" showRemoveLink="false" showStatus="false" />
									</td>
									<td>
										<div class="text-center">
											<c:url value="/cart/update" var="cartUpdateFormAction" />
											<form:form id="updateCartForm${entry.entryNumber}"	action="${cartUpdateFormAction}" method="post"	commandName="updateQuantityForm${entry.entryNumber}">
												<input type="hidden" name="entryNumber"	value="${entry.entryNumber}" />
												<input type="hidden" name="productCode"	value="${entry.product.code}" />
												<input type="hidden" name="initialQuantity"	value="${entry.quantity}" />
												<input type="hidden" size="4" value="${valueObject.materialQuantity}" id="freeGoodQuantity${entry.product.code}" />
												<div id="freeGoodQuantity${entry.product.code}">${valueObject.materialQuantity}</div>
												<p class="thirdline">
													<spring:message code="product.detail.addToCart.unit" />	${entry.product.deliveryUnit}(${entry.product.numerator}&nbsp;${entry.product.salesUnit})
												</p>
											</form:form>
											<c:forEach var="division" items="${divisionList}">
												<c:if test="${division eq entry.product.salesOrgCode}">
													<p class="log">
														<span> <label class="descSmall block"
															for="LotComment_${entry.entryNumber}"><spring:message
																	code="cart.review.entry.lotComment" /></label> <input
															type='text' class='insertInput lotCommentInput'
															title="<spring:message code="cart.common.code.enter"/>"
															id="LotComment_${entry.entryNumber}"
															value="${entry.lotComment}" />
														</span>
													</p>
												</c:if>
											</c:forEach>
										</div>
									</td>
									<td class="valign-middle">
									<spring:message	code="cart.freeitem.message" />
									</td>
									<td class="valign-middle"></td>
								</tr>
							</c:if>
						</c:if>
					</c:forEach>
					
					
                  <!-- Changes for Bonus Item -->
						<c:if test="${freeGoodsMap ne null}">
							<c:set var="valueObject"
								value="${freeGoodsMap[entry.product.code]}" />
							<c:if test="${not empty valueObject.itemCategory}">
								<tr class="noborder" id="freeGood${entry.product.code}">
									<td class="text-right"><standardCart:productDescriptionBeforeValidation
											entry="${entry}" priceError="${priceValidationErrorMsg}" showRemoveLink="false" showStatus="false" /></td>
									<td>
										<div class="text-center">
											<c:url value="/cart/update" var="cartUpdateFormAction" />
											<form:form id="updateCartForm${entry.entryNumber}"
												action="${cartUpdateFormAction}" method="post"
												commandName="updateQuantityForm${entry.entryNumber}">
												<input type="hidden" name="entryNumber"
													value="${entry.entryNumber}" />
												<input type="hidden" name="productCode"
													value="${entry.product.code}" />
												<input type="hidden" name="initialQuantity"
													value="${entry.quantity}" />

												<input type="hidden" size="4"
													value="${valueObject.materialQuantity}"
													id="freeGoodQuantity${entry.product.code}" />
												<div id="freeGoodQuantity${entry.product.code}">${valueObject.materialQuantity}</div>
												<p class="thirdline">
													<spring:message code="product.detail.addToCart.unit" />
													${entry.product.deliveryUnit}
													(${entry.product.numerator}&nbsp;${entry.product.salesUnit})
												</p>

											</form:form>
										</div>
									</td>
									<td class="valign-middle"><spring:message
											code="cart.freeitem.message" /></td>
									<td class="valign-middle"></td>
								</tr>
							</c:if>
						</c:if>

				</tbody>
		
				</tbody>
		</table>
		
		
		</div>
														<!-- Start - Mobile implementation --> 

		<div class="visible-xs hidden-lg hidden-sm hidden-md">
			<table id="datatab-mobile"
				class="table table-bordered table-striped sorting-table-lines">
				<thead>
					<tr>
						<th class="no-sort text-left"><spring:message code="cart.validate.product" />
						<div class="cart-update-all-link"><a class="cartUpdateAllbtn cartUpdateAllbutton" id="cartUpdateAllbutton" href="javascript:;"><spring:message code="cart.review.entry.Updateall"/></a></div> 
						</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${cartData.entries}" var="entry"
						varStatus="count">
						<tr id="orderentry-${count.count}">
							<td class="text-left">
							<div class="display-row">
									<div class="table-cell jnj-toggle-sign">
										<a href="#collapse-${count.count}" data-toggle="collapse" data-parent="#accordion" class="toggle-link panel-collapsed">
											<span class="glyphicon glyphicon-plus"></span>
											</a>	
									</div>
									<div class="table-cell">
										<standardCart:productDescription entry="${entry}" showRemoveLink="true" showStatus="false" />
									</div>
								</div>
								<div id="collapse-${count.count}"
									class="panel-collapse collapse img-accordian">
									<c:url value="/cart/update" var="cartUpdateFormAction" />
									<form:form id="updateCartForm${entry.entryNumber}_desktop"
										action="${cartUpdateFormAction}" method="post"
										commandName="updateQuantityForm${entry.entryNumber}">
										<c:if test="${freeGoodsMap ne null}">
											<c:set var="freeObject"
												value="${freeGoodsMap[entry.product.code]}" />
										</c:if>
										<input type="hidden" name="entryNumber"
											value="${entry.entryNumber}" />
										<input type="hidden" name="productCode"
											value="${entry.product.code}" />
										<input type="hidden" name="initialQuantity"
											value="${entry.quantity}" />
										<div>
											<ycommerce:testId code="cart_product_quantity">
											<div class="cart-update-link">
												<a href="javascript:void();" id="quantity${entry.entryNumber}_mobile_update" entryNum="${entry.entryNumber}" class="qtyUpdateTextBox1 click_updateBtn_mobile">
													<spring:message code="cart.review.productDesc.updateItem" />
												</a>
											</div>
											
												<form:label cssClass="skip" path="quantity"
													for="quantity${entry.entryNumber}"></form:label>
												<form:input disabled="${not entry.updateable}" type="text"
													id="quantity${entry.entryNumber}_mobile"
													entryNumber="${entry.entryNumber}"
													class="insertInput qtyUpdateTextBox qtyUpdateTextBox form-control txtWidth"
													path="quantity"
													onclick="hideFreeGood('${entry.product.code}', this,'${freeObject.orderedQuantity}');" />
											</ycommerce:testId>
										</div>
										<p class="thirdline">
											<spring:message code="product.detail.addToCart.unit" />
											${entry.product.deliveryUnit}(${entry.product.numerator}&nbsp;${entry.product.salesUnit})
										</p>

										<ycommerce:testId code="cart_product_removeProduct">
											<p>
												<a href="javascript:void();"
													id="RemoveProduct_${entry.entryNumber}"
													class="smallFont submitRemoveProduct"> <spring:message
														code="cart.review.productDesc.removeItem" />
												</a>
											</p>
										</ycommerce:testId>
										<p class="msgHighlight">${entry.product.hazmatCode}</p>
									</form:form>
									<a href="javascript:void();" id="quantity${entry.entryNumber}"
										entryNum="${entry.entryNumber}" class="qtyUpdateTextBox1">
										<spring:message code="cart.review.productDesc.updateItem" />
									</a>
									<p><spring:message	code="cart.validate.unitPrice" /></p>
									<p id="basePrice_${entry.entryNumber}">
										<format:price priceData="${entry.basePrice}" />
									</p>
									<p><spring:message code="cart.review.entry.total" /></p>
									<p id="totalPrice_${entry.entryNumber}">
										<ycommerce:testId code="cart_totalProductPrice_label">
											<format:price priceData="${entry.totalPrice}" />
										</ycommerce:testId>
									</p>
								</div></td>
						</tr>
					</c:forEach>

				</tbody>
			</table>
		</div>
		
									<!-- End - Mobile implementation -->

	</div>
</div>


