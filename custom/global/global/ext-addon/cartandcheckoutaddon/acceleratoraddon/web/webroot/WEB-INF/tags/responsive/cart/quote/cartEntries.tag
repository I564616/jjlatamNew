<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="label"  uri="/WEB-INF/tld/addons/loginaddon/message.tld"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/responsive/cart"%>
<%@ attribute name="isQuoteResultPage" required="true" type="java.lang.Boolean"%>
<%@ taglib prefix="standardCart"	tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/standard"%>

<script type="text/javascript">
	// set vars
	/*<![CDATA[*/
	var cartRemoveItem = true;
	/*]]>*/
</script>

<!--Order Derail Row starts-->
<form:form id="UpdateMultipleEntriesInCartForm" action="cart/updateAll" method="post" commandName="UpdateMultipleEntriesInCartForm">
</form:form>
<div class="hidden-xs">
	<table id="datatab-desktop" class="table table-bordered table-striped sorting-table-lines price-inquiry-table  error-on-top">
		<thead>
			<tr>
				<th class="no-sort text-left" ><spring:message code="cart.review.entry.number"/></th><!-- style="width: 60px" -->
				<th class="no-sort text-left"> <spring:message code="cart.validate.product" /></th>
				<th class="no-sort cartqty"> <spring:message  code="cart.review.entry.quantity" /> 
					<c:if test="${!isQuoteResultPage}">
						<div class="cart-update-all-link">
							<a class="cartUpdateAllbtn cartUpdateAllbutton" id="cartUpdateAllbutton" href="javascript:;">
								<spring:message  code="cart.review.entry.Updateall" /></a>
						</div>
					</c:if>
				</th>
				<th class="no-sort cartprice"> <spring:message  code="cart.validate.unitPrice" /></th>
				<th class="no-sort total-thead carttotal"> <spring:message  code="cart.review.entry.total" /></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${cartData.entries}" var="entry" varStatus="count">
				<tr>
				<td>${count.count}</td>
					<td class="text-left jnj-img-txt">
						<c:choose>
							<c:when test="${isQuoteResultPage}">
								<standardCart:productDescription entry="${entry}" showRemoveLink="false" />
							</c:when>
							<c:otherwise>
								<standardCart:productDescription entry="${entry}" showRemoveLink="true" />
							</c:otherwise>
						</c:choose>
					</td>
					<td>
						<div class="cart-update-link">
							<c:if test="${not isQuoteResultPage}">
								<c:if test="${entry.updateable}">
									<ycommerce:testId code="cart_product_updateQuantity">
										<a href="javascript:;" class="UpdateQtyLinkCart updateQuantityProduct" id="QuantityProduct_${entry.entryNumber}">
											<spring:theme  code="basket.page.update" /></a>
									</ycommerce:testId>
								</c:if>
							</c:if>
						</div>
						<div class="floatLeft column3">
							<c:choose>
								<c:when test="${isQuoteResultPage}">
									<div class="lbox">
										<p>
											<span>${entry.quantity}</span>
										</p>
										<p>
											<label class="descSmall block" for="quantOne">${entry.product.deliveryUnit}
												(${entry.product.numerator}&nbsp;${entry.product.salesUnit})</label>
										</p>
									</div>
								</c:when>
								<c:otherwise>
									<div class="lbox">
										<c:url value="/cart/update" var="cartUpdateFormAction" />
										<form:form id="updateCartForm${entry.entryNumber}_desktop" action="${cartUpdateFormAction}" method="post"
											commandName="updateQuantityForm${entry.entryNumber}">
											<input type="hidden" name="entryNumber" value="${entry.entryNumber}" />
											<input type="hidden" name="productCode" value="${entry.product.code}" />
											<input type="hidden" name="initialQuantity" value="${entry.quantity}" />
											<ycommerce:testId code="cart_product_quantity">
												<p>
													<span> 
													<form:input  disabled="${not entry.updateable}" type="text" size="3"
															maxlength="6" id="quantity${entry.entryNumber}"
															class="insertInput form-control qtyUpdateTextBox"
															entryNumber="${entry.entryNumber}" path="quantity" /></span>
												</p>
											</ycommerce:testId>
											<p>
												<label class="descSmall block" for="quantOne">${entry.product.deliveryUnit}
													(${entry.product.numerator}&nbsp;${entry.product.salesUnit})</label>
											</p>
										</form:form>
										<!-- Hide lot/comment if division is part of configured divisions -->
										<c:forEach var="division" items="${divisionList}">
											<c:if  test="${division eq entry.product.mddSpecification.division}">
												<p class="log">
													<span>
														 <label class="descSmall block" for="LotComment${entry.entryNumber}">
															<spring:message code="cart.review.entry.lotComment" /></label>
															<input type='text' class='insertInput lotCommentInput'
															title="<spring:message code="cart.common.code.enter"/>" id="LotComment_${entry.entryNumber}"
															value="${entry.lotComment}" />
													</span>
												</p>
											</c:if>
										</c:forEach>
									</div>
								</c:otherwise>
							</c:choose>
							<ycommerce:testId code="cart_product_removeProduct">
								<p>
									<a href="javascript:void();" id="RemoveProduct_${entry.entryNumber}" class="smallFont submitRemoveProduct">
										<spring:message  code="cart.review.productDesc.removeItem" /> 
										<%-- <spring:theme code="basket.page.removeItem"/> --%>
									</a>
								</p>
							</ycommerce:testId>
						</div>
					</td>
					<td></td>
					<td></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>
<!-- Table collapse for mobile device-->
<div class=" Subcontainer visible-xs hidden-lg hidden-sm hidden-md">
	<table id="datatab-mobile" class="table table-bordered table-striped sorting-table-lines error-on-top" >
		<thead>
			<tr>
				<th class="no-sort text-left"><spring:message code="cart.validate.product"/></th>
				<th class="no-sort"><spring:message code="cart.review.entry.quantity"/>
			 		<div class="cart-update-all-link"><a class="cartUpdateAllbtn cartUpdateAllbutton" id="cartUpdateAllbutton" href="javascript:;"><spring:message code="cart.review.entry.Updateall"/></a></div> 
				</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${cartData.entries}" var="entry"  varStatus="count">
			<tr>
				<td class="text-left">
					<c:choose>
						<c:when test="${isQuoteResultPage}">
							<standardCart:productDescription entry="${entry}" showRemoveLink="false" />
						</c:when>
						<c:otherwise>
							<standardCart:productDescription entry="${entry}" showRemoveLink="true" />
						</c:otherwise>
					</c:choose>
				</td>
				<td>
					<div class="cart-update-link">
						<c:if test="${not isQuoteResultPage}">
							<c:if test="${entry.updateable}">
								<ycommerce:testId code="cart_product_updateQuantity">
									<a href="javascript:;" class="UpdateQtyLinkCart updateQuantityProduct" id="QuantityProduct_${entry.entryNumber}_mobile">
										<spring:theme  code="basket.page.update" />
									</a>
								</ycommerce:testId>
							</c:if>
						</c:if>
					</div>
					<div class="floatLeft column3">
						<c:choose>
							<c:when test="${isQuoteResultPage}">
								<div class="lbox">
									<p>
										<span>${entry.quantity}</span>
									</p>
									<p>
										<label class="descSmall block" for="quantOne">${entry.product.deliveryUnit}
											(${entry.product.numerator}&nbsp;${entry.product.salesUnit})</label>
									</p>
								</div>
							</c:when>
							<c:otherwise>
								<div class="lbox">
									<c:url value="/cart/update" var="cartUpdateFormAction" />
									<form:form id="updateCartForm${entry.entryNumber}_mobile" action="${cartUpdateFormAction}" method="post" 
										commandName="updateQuantityForm${entry.entryNumber}">
										<input type="hidden" name="entryNumber" value="${entry.entryNumber}" />
										<input type="hidden" name="productCode" value="${entry.product.code}" />
										<input type="hidden" name="initialQuantity" value="${entry.quantity}" />
										<ycommerce:testId code="cart_product_quantity">
											<p>
												<span> 
													<form:input  disabled="${not entry.updateable}" type="text" size="3" maxlength="6" id="quantity${entry.entryNumber}"
														class="insertInput form-control qtyUpdateTextBox" entryNumber="${entry.entryNumber}" path="quantity" />
												</span>
											</p>
										</ycommerce:testId>
										<p>
											<label class="descSmall block" for="quantOne">${entry.product.deliveryUnit}
												(${entry.product.numerator}&nbsp;${entry.product.salesUnit})</label>
										</p>
									</form:form>
									<!-- Hide lot/comment if division is part of configured divisions -->
									<c:forEach var="division" items="${divisionList}">
										<c:if  test="${division eq entry.product.mddSpecification.division}">
											<p class="log">
												<span>
														<label class="descSmall block" for="LotComment${entry.entryNumber}">
															<spring:message code="cart.review.entry.lotComment" />
														</label>
														<input type='text' class='insertInput lotCommentInput' title="<spring:message code="cart.common.code.enter"/>" id="LotComment_${entry.entryNumber}" value="${entry.lotComment}" />
												</span>
											</p>
										</c:if>
									</c:forEach>
								</div>
							</c:otherwise>
						</c:choose>
						<ycommerce:testId code="cart_product_removeProduct">
							<p>
								<a href="javascript:void();" id="RemoveProduct_${entry.entryNumber}" class="smallFont submitRemoveProduct">
									<spring:message  code="cart.review.productDesc.removeItem" /> 
								</a>
							</p>
						</ycommerce:testId>
					</div>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
</div>