<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="cartData" required="true" type="de.hybris.platform.commercefacades.order.data.CartData" %>

<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/reponsive/product" %>

<script id="cartItemsTemplate" type="text/x-jquery-tmpl">
 	{{tmpl({entries:entries},{appliedProductPromotion:appliedProductPromotions,potentialProductPromotion:potentialProductPromotions}) '#entriesTemplate'}}
</script>

<script id="entriesTemplate" type="text/x-jquery-tmpl">
{{each entries}}
	<tr>
		<td headers="header1" class="product_image">
			{{tmpl($value.product,{name:name}) '#iterateProductImagesTemplate'}}
		</td>
		<td headers="header2" class="product_details">
			<h2>
				<ycommerce:testId code="orderDetails_productName_link">
					<a href={{= "<c:url value="/"/>"+product.url}}>{{= product.name}}</a>
				</ycommerce:testId>
			</h2>
			{{tmpl($value.product,{productUrl:$value.product.url}) '#baseOptionSelectedTemplate'}}
			<ul class ="cart-promotions">
				{{tmpl({potentialProductPromotions:$item.potentialProductPromotion},{entryNumber:entryNumber}) '#iteratePotentialPromotionEntriesTemplate'}}
				{{tmpl({appliedProductPromotions:$item.appliedProductPromotion},{entryNumber:entryNumber}) '#iterateAppliedPromotionEntriesTemplate'}}
			</ul>
		</td>
		<td headers="header3" class="quantity">
			<ycommerce:testId code="orderDetails_productQuantity_label">{{= quantity}}</ycommerce:testId>
		</td>
		<td headers="header4">
			<ycommerce:testId code="orderDetails_productItemPrice_label">{{= basePrice.formattedValue}}</ycommerce:testId>
		</td>
		<td headers="header5" class="total">
			<ycommerce:testId code="orderDetails_productTotalPrice_label">{{= totalPrice.formattedValue}}</ycommerce:testId>
		</td>
	</tr>
{{/each}}
</script>

<script id="iterateProductImagesTemplate" type="text/x-jquery-tmpl">
	{{each images}}
		{{if imageType=='PRIMARY' && format=='thumbnail'}}
			<img src={{= url}} alt={{= $item.name}} title={{= $item.name}}/>
		{{/if}}
	{{/each}}
</script>

<script id="iteratePotentialPromotionEntriesTemplate" type="text/x-jquery-tmpl">
{{each potentialProductPromotions}}
		{{tmpl($value,{entryNumber:$item.entryNumber,description:description}) '#iterateConsumePromotionEntriesTemplate'}}
{{/each}}
</script>


<script id="iterateAppliedPromotionEntriesTemplate" type="text/x-jquery-tmpl">
{{each appliedProductPromotions}}
		{{tmpl($value,{entryNumber:$item.entryNumber,description:description}) '#iterateConsumePromotionEntriesTemplate'}}
{{/each}}
</script>

<script id="iterateConsumePromotionEntriesTemplate" type="text/x-jquery-tmpl">
{{each consumedEntries}}
	{{if orderEntryNumber==$item.entryNumber && adjustedUnitPrice > 0 }}
		<li class="cart-promotions-potential"><span>{{= $item.description}}</span></li>
	{{/if}}
{{/each}}
</script>

<script id="baseOptionSelectedTemplate" type="text/x-jquery-tmpl">
	{{each baseOptions}}
 			{{if selected && selected.url==$item.productUrl}}
				{{each selected.variantOptionQualifiers}}
					<dl>
						<dt>{{= name}}:</dt>
						<dd>{{= value}}</dd>
					</dl>
				{{/each}}
			{{/if}}
	{{/each}}
</script>


<div class="item_container_holder">
	<div class="title_holder">
		<div class="title">
			<div class="title-top">
				<span></span>
			</div>
		</div>
		<h2><spring:theme code="basket.page.title.yourItems"/></h2>
	</div>
	<div class="item_container">
		<table id="your_cart">
			<thead>
				<tr>
					<th id="cartheader1"><span class="hidden">Product</span></th>
					<th id="cartheader2"><span class="hidden">Product Details</span></th>
					<th id="cartheader3"><spring:theme code="basket.page.quantity"/></th>
					<th id="cartheader4"><spring:theme code="basket.page.itemPrice"/></th>
					<th id="cartheader5"><spring:theme code="basket.page.total"/></th>
				</tr>
			</thead>
			<tbody id="cart_items_tbody">
				<c:forEach items="${allItems}" var="entry">
					<c:url value="${entry.product.url}" var="productUrl"/>
					<tr>
						<td headers="cartheader1" class="product_image">
							<a href="${productUrl}">
								<product:productPrimaryImage product="${entry.product}" format="thumbnail"/>
							</a>
						</td>
						<td headers="cartheader2" class="product_details">
						    <ycommerce:testId code="cart_product_name">
                                <h2><a href="${productUrl}">${entry.product.name}</a></h2>
                            </ycommerce:testId>
                            <c:forEach items="${entry.product.baseOptions}" var="option">
                                <c:if test="${not empty option.selected and option.selected.url eq entry.product.url}">
                                    <c:forEach items="${option.selected.variantOptionQualifiers}" var="selectedOption">
                                	    <dl>
                                		    <dt>${selectedOption.name}:</dt>
                                			<dd>${selectedOption.value}</dd>
                                		</dl>
                                	</c:forEach>
                                </c:if>
                            </c:forEach>
                            <c:if test="${not empty cartData.potentialProductPromotions}">
                                <ul class="cart-promotions">
                                    <c:forEach items="${cartData.potentialProductPromotions}" var="promotion">
                                	    <c:set var="displayed" value="false"/>
                                        <c:forEach items="${promotion.consumedEntries}" var="consumedEntry">
                                            <c:if test="${not displayed && consumedEntry.orderEntryNumber == entry.entryNumber
                                            && not empty promotion.description}">
                                                <c:set var="displayed" value="true"/>
                                                    <li class="cart-promotions-potential">
                                                        <ycommerce:testId code="cart_promotion_label">
                                                            <span>${promotion.description}</span>
                                                        </ycommerce:testId>
                                                    </li>
                                            </c:if>
                                        </c:forEach>
                                	</c:forEach>
                                </ul>
                            </c:if>
                            <c:if test="${not empty cartData.appliedProductPromotions}">
                                <ul class="cart-promotions">
                                    <c:forEach items="${cartData.appliedProductPromotions}" var="promotion">
                                        <c:set var="displayed" value="false"/>
                                        <c:forEach items="${promotion.consumedEntries}" var="consumedEntry">
                                            <c:if test="${not displayed && consumedEntry.orderEntryNumber == entry.entryNumber}">
                                                <c:set var="displayed" value="true"/>
                                                <li class="cart-promotions-applied">
                                                    <ycommerce:testId code="cart_appliedPromotion_label">
                                                        <span>${promotion.description}</span>
                                                    </ycommerce:testId>
                                                </li>
                                            </c:if>
                                        </c:forEach>
                                    </c:forEach>
                                </ul>
                            </c:if>
						</td>
						<td headers="cartheader3" class="quantity">
							<ycommerce:testId code="orderDetails_productQuantity_label">${entry.quantity}</ycommerce:testId>
						</td>
						<td headers="cartheader4">
							<ycommerce:testId code="orderDetails_productItemPrice_label"><format:price priceData="${entry.basePrice}"/></ycommerce:testId>
						</td>
						<td headers="cartheader5" class="total">
							<ycommerce:testId code="orderDetails_productTotalPrice_label"><format:price priceData="${entry.totalPrice}"/></ycommerce:testId>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
</div>
