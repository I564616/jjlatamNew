<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="component" tagdir="/WEB-INF/tags/shared/component" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>

<ycommerce:testId code="product_wholeProduct">
<c:set value="${ycommerce:productImage(product, 'thumbnail')}" var="primaryImage"/>
<c:choose>
	<c:when test="${not empty productReferences and component.maximumNumberProducts > 0}">
		<div class="relatedProdSection hidden-xs">
									<div class="row">
										<div class="col-lg-12 relatedProductLabel">
											${title}
										</div>
										<div class="col-lg-11 col-md-11 col-sm-11 hidden-xs hrLine"></div>
										<div class="col-lg-1 col-md-1 col-sm-1 hidden-xs swipePageHolder">
											<!-- Add Pagination -->
											<div class="swiper-pagination sp1"></div>
										</div>
										<div class="swiper-container s1 boxshadow hidden-xs">
											<div class="swiper-wrapper">
														<c:forEach end="${component.maximumNumberProducts}" items="${productReferences}" var="productReference" varStatus="count">
															<div class="col-lg-3 productHolder" id="product_${count.count}">
															<c:url value="${productReference.target.url}" var="productQuickViewUrl"/>
																<div class="imgPlaceHolder">
																	<a href="${productQuickViewUrl}">
																	 <product:productPrimaryImage product="${productReference.target}" format="thumbnail" />
																	</a>
																</div>
																<div class="imgProductDescrp">
																	<p class="productCode"><a href="${productQuickViewUrl}">${productReference.target.code}</a></p>
																	<p class="prodName">
																		<a href="${productQuickViewUrl}">
																			${not empty productReference.target.name ?
																					productReference.target.name : productReference.target.name}
																		</a>
																	</p>
																</div>

																<div class="likeProductHold">

																	<div><a class="btn btnclsactive addToCartBtn addToCartLaCarousel" ><spring:message
																				code="product.detail.addToCart.addToCart" /></a></div>

																</div>
															</div>
														</c:forEach>
											</div>
											<div class="swiper-button-next next-1">></div>
											<div class="swiper-button-prev prev-1"><</div>
											</div>
										</div>

									</div>

									<div class="relatedProdSection hidden-lg hidden-md hidden-sm">
									<div class="row">
										<div class="col-lg-12 relatedProductLabel">
												${title}
										</div>
										<div class="col-lg-11 col-xs-12 hrLine"></div>
										<div class="swiper-container s3 boxshadow hidden-lg hidden-sm hidden-md">
											<div class="swiper-wrapper">
												<c:forEach end="${component.maximumNumberProducts}" items="${productReferences}" var="productReference" varStatus="count">
													<div class="swiper-slide">
														<div class="row productSwiperHolder productSwiperHolderMob">
																<div class="col-lg-3 productHolder productHolderMobile">
																	<c:url value="${productReference.target.url}" var="productQuickViewUrl"/>
																	<div class="imgPlaceHolder">
																	  <a href="${productQuickViewUrl}">
																		<product:productPrimaryImage product="${productReference.target}" format="thumbnail"/>
																	  </a>
																	</div>
																	<div class="imgProductDescrp">
																		<p class="productCode"><a href="${productQuickViewUrl}">${productReference.target.code}</a></p>
																		<p class="prodName"><a href="${productQuickViewUrl}">${productReference.target.name}</a></p>
																	</div>

															<div class="likeProductHold">

															<div><a href="#" class="btn btnclsactive addToCartBtn addToCartLaCarousel"><spring:message
																				code="product.detail.addToCart.addToCart" /></a></div>

																	</div>
																</div>
														</div>
													</div>
												</c:forEach>
											</div>
											<div class="swiper-button-next next-3">></div>
											<div class="swiper-button-prev prev-3"><</div>
										</div>

									</div>
								</div>


	</c:when>

	<c:otherwise>
		<component:emptyComponent/>
	</c:otherwise>
</c:choose>
</ycommerce:testId>