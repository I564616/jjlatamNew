<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/loginaddon/responsive/template"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%> 
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common"%>
<%@ taglib prefix="breadcrumb"	tagdir="/WEB-INF/tags/responsive/nav/breadcrumb"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/addons/loginaddon/message.tld"%>
<%@ taglib prefix="standardCart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/standard"%>
<%@ taglib prefix="commonTags" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/common"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<div class="mainbody-container">
										<div class="hidden-xs">
											<table id="datatab-desktop" class="table table-bordered table-striped sorting-table-lines">
												<thead>
												  <tr>
													<th class="no-sort text-left text-uppercase"><spring:message code="cart.validate.product" /></th>
													<th class="no-sort text-left text-uppercase"><spring:message code="cart.validate.shippingMethod" /></th>
													<th class="no-sort text-uppercase"><spring:message code="cart.validate.quantity" /></th>
													<th class="no-sort text-uppercase"><spring:message code="cart.validate.unitPrice"/></th>
													<th class="no-sort total-thead text-uppercase"><spring:message code="cart.validate.total" /></th>
												  </tr>
												</thead>
												<tbody>
													<c:forEach items="${cartData.entries}" var="entry"  varStatus="count">
													<tr id="orderentry-${count.count}">
														<td class="text-left">
			                                    					 <standardCart:productDescriptionBeforeValidation entry="${entry}" showRemoveLink="true" showStatus="false"/>
			                                             </td>
														<td>${entry.shippingMethod}<br>
															<c:if test="${not entry.expandableSchedules}">
																	
																		<commonTags:entryLevelDates entry="${entry}" />
																	
																</c:if>
														</td>
														<td>
															<div>${entry.quantity}</div>
															<p class="thirdline"><spring:message code="product.detail.addToCart.unit"/> ${entry.product.deliveryUnit} (${entry.product.numerator}&nbsp;${entry.product.salesUnit})</p>
														</td>
														<td >
														<format:price priceData="${entry.basePrice}"/>							           
														</td>
														<td class="totalrps">
														<ycommerce:testId code="cart_totalProductPrice_label">
														<format:price priceData="${entry.totalPrice}"/>
														</ycommerce:testId> </td>
													</tr>
													</c:forEach>
												
												</tbody>
											</table>
										</div>
									
									
									<!-- Table collapse for mobile device-->
							
									<div class="row visible-xs hidden-lg hidden-sm hidden-md">
										<div class="col-xs-12">
											<table id="datatab-mobile" class="table table-bordered table-striped sorting-table-lines">
												<thead>
													<tr>
													<th class="no-sort text-left text-uppercase"><spring:message code="cart.validate.product" /></th></tr>
												</thead>
												<tbody>
												<c:forEach items="${cartData.entries}" var="entry"  varStatus="count">
													<tr>
														<td class="text-left">
															 <standardCart:productDescriptionBeforeValidation entry="${entry}" showRemoveLink="true" showStatus="false"/>
															<p class="text-uppercase"><spring:message code="cart.validate.shippingMethod" /></p>
															<P>${entry.shippingMethod}<br>
															<c:if test="${not entry.expandableSchedules}">
																	
																		<commonTags:entryLevelDates entry="${entry}" />
																	
																
																</c:if></P>
															<p class="text-uppercase"><spring:message code="cart.validate.quantityQty"/></p>
															<p>${entry.quantity}</p>
															<p class="thirdline"> ${entry.product.deliveryUnit} (${entry.product.numerator}&nbsp;${entry.product.salesUnit})</p>
															<p class="text-uppercase"><spring:message code="cart.validate.unitPrice"/></p>
															<p><format:price priceData="${entry.basePrice}"/></p>
															<p class="text-uppercase"><spring:message code="cart.validate.total" /></p>
															<p><ycommerce:testId code="cart_totalProductPrice_label">
														<format:price priceData="${entry.totalPrice}"/>
														</ycommerce:testId>
														</p>
														</td>
													</tr>
												</c:forEach>	
													
														
												</tbody>
											</table>
										</div>
									</div>	
									
									<!--Accordian Ends here -->
									
									<!-- Start - Total Price Summary -->
										<div class="row basecontainer">
										
												<table class="total-summary-table">
													<tr>
														<td class="total-summary-label text-uppercase"><spring:message code="cart.validate.cartTotal.subTotal"/></td>
														<td class="total-summary-cost totalrps no-right-pad"><format:price priceData="${cartData.subTotal}" /></td> 
														<td class="hidden"></td>
													</tr>
													<tr class="summary-bline">
														<td class="total-summary-label text-uppercase"><spring:message code="cart.review.entry.shipping"/></td>
														<td class="total-summary-cost">--</td>
													</tr>
													<tr class="total-price-row">
														<td class="total-summary-label text-uppercase"><spring:message code="cart.validate.cartTotal.totals"/></td>
														<td class="total-summary-cost totalsum no-right-pad"><format:price priceData="${cartData.totalPrice}"/></td>
														<td class="hidden"></td>
													</tr>
												</table>
												
										</div>
									<!-- End - Total Price Summary -->
									</div>