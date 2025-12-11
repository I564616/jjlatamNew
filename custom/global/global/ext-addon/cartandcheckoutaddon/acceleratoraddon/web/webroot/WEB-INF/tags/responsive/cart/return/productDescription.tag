<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<%@ attribute name="entry" type="com.jnj.facades.data.JnjGTOrderEntryData" required="true" %>
<%@ attribute name="showRemoveLink" type="java.lang.Boolean" required="false" %>
<%@ attribute name="showStatus" type="java.lang.Boolean" required="false" %>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<c:url value="${entry.product.url}" var="productUrl"/>
<c:url value="${entry.product.code}" var="productCode"/>
<div class="display-row">
	<div class="table-cell padding-right-img jnj-toggle-sign">
	 	<div class="prodImage">
		<!-- If a product is not viewable in the catalog, the product name will not be clickable.  -->
			<c:choose>
			<c:when test="${!entry.product.isProdViewable || empty productUrl}">
				<product:productPrimaryImage product="${entry.product}" format="cartIcon"/>
			</c:when>
			<c:otherwise>
				<a href="javascript:;" class="showProductDeatils" data="${productCode}"><product:productPrimaryImage product="${entry.product}" format="cartIcon"/></a>
			</c:otherwise>
			</c:choose>
		
		</div>
	</div>
 	<div class="table-cell" style="vertical-align:middle">
	 <div class="Tablesubtxt">
			<p class="firstline">
				<c:choose>
					<c:when test="${!entry.product.isProdViewable || empty productUrl}">
							${entry.product.name}
					</c:when>
					<c:otherwise>
						<a href="javascript:;" class="showProductDeatils" data="${productCode}">${entry.product.name}</a>
					</c:otherwise>
				</c:choose>
			</p>
			<p class="secondline"><spring:message code="cart.review.productDesc.jnJID"/><span class="strong">${entry.product.code}</p> 
			</p>
			<c:if test="${not empty entry.product.gtin}">
			<p class="secondline"><spring:message code="cart.review.productDesc.gtnNumber"/>${entry.product.gtin}</p>
			</c:if>
			<c:if test="${not empty entry.contractNumber}">
			<p class="secondline hide"><spring:message code="cart.review.productDesc.contractNumber"/>${entry.contractNumber}</span></p>  
			</c:if>
			<c:if test="${showStatus}">
			<c:if test="${not empty entry.status}">
				<p class="secondline"><spring:message code="cart.review.productDesc.status"/>${entry.status}</p>
			</c:if>
			</c:if>
			<p class="msgHighlight">${entry.product.hazmatCode}</p>
				<!--4069 story changes starts-->
			<c:forEach var="item" items="${priceError}">
				<c:if test="${item == entry.product.code}">
					<p class="error-prod-msg" style="color: red" >
						<spring:message code="cart.common.zeroPrice.priceNotAvailable" />
					</p>
				</c:if>
			</c:forEach>
				<!--4069 story changes ends-->
		</div>
 	</div> 
</div>

