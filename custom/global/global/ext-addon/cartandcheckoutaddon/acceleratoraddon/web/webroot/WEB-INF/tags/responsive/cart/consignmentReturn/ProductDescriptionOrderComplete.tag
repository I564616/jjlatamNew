<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ attribute name="entry" type="com.jnj.facades.data.JnjGTOrderEntryData" required="true"%>
<%@ attribute name="rowcount" type="java.lang.Integer" required="false"%>
<%@ attribute name="showRemoveLink" type="java.lang.Boolean" required="false"%>
<%@ attribute name="showStatus" type="java.lang.Boolean" required="false"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<c:url value="${entry.product.url}" var="productUrl" />
<c:url value="${entry.product.code}" var="productCode" />
<a href="#mobi-collapse${rowcount}" data-toggle="collapse" data-parent="#accordion" class="toggle-link panel-collapsed">
  <div class="display-row">
    <div class="table-cell jnj-toggle-sign hidden-lg hidden-md hidden-sm">
      <span class="glyphicon glyphicon-plus"></span>
    </div>
    <div class="table-cell">
      <c:choose>
        <c:when test="${!entry.product.isProdViewable || empty productUrl}">
          <product:productPrimaryImage product="${entry.product}" format="cartIcon" />
        </c:when>
        <c:otherwise>
          <a href="${productUrl}" class="imgprop" data="${productCode}"><product:productPrimaryImage product="${entry.product}" format="cartIcon" /></a>
        </c:otherwise>
      </c:choose>
    </div>
    <div class="table-cell">
      <div class="Tablesubtxt Tabsubtxt">
        <p class="firstline" title="${entry.product.name}">
          <c:choose>
            <c:when test="${!entry.product.isProdViewable || empty productUrl}">
							${entry.product.name}
					</c:when>
            <c:otherwise>
              <strong><a href="${productUrl}" data="${productCode}">${entry.product.name}</a></strong>
            </c:otherwise>
          </c:choose>
        </p>
        <p class="secondline">
          <spring:message code="cart.review.productDesc.jnJID" />
          <span class="strong">${entry.product.code}</span>
        </p>
        <c:if test="${not empty entry.product.gtin}">
          <p class="secondline">
            <spring:message code="cart.review.productDesc.gtnNumber" />${entry.product.gtin}</p>
        </c:if>
        <c:if test="${not empty entry.contractNumber}">
          <p class="secondline">
            <spring:message code="cart.review.productDesc.contractNumber" />${entry.contractNumber}</p>
        </c:if>
        <c:if test="${showStatus}">
          <c:if test="${not empty entry.status}">
            <p class="secondline">
              <spring:message code="cart.review.productDesc.status" />${entry.status}</p>
          </c:if>
        </c:if>
        <p class="msgHighlight">${entry.product.hazmatCode}</p>
      </div>
    </div>
  </div>
</a>
<%-- <c:choose>
	<c:when test="${!entry.product.isProdViewable || empty productUrl}">
		<product:productPrimaryImage product="${entry.product}" format="cartIcon"/>
	</c:when>
	<c:otherwise>
		<a href="${productUrl}" class="imgprop" data="${productCode}"><product:productPrimaryImage product="${entry.product}" format="cartIcon" /></a>
	</c:otherwise>
</c:choose> --%>
<%-- <span class="Tablesubtxt Tabsubtxt">
<p class="firstline">
<h4 title="${entry.product.name}">
		<c:choose>
			<c:when test="${!entry.product.isProdViewable || empty productUrl}">
					${entry.product.name}
			</c:when>
			<c:otherwise>
				<a href="${productUrl}" data="${productCode}">${entry.product.name}</a>
			</c:otherwise>
		</c:choose>
	</h4></p>
	 <p class="secondline"><spring:message code="cart.review.productDesc.jnJID"/><span class="strong">${entry.product.code}</span>
	</p>
	<c:if test="${not empty entry.product.gtin}">
	<p class="secondline"><spring:message code="cart.review.productDesc.gtnNumber"/>${entry.product.gtin}</p>
	</c:if>
	<c:if test="${not empty entry.contractNumber}">
	<p class="secondline"><spring:message code="cart.review.productDesc.contractNumber"/>${entry.contractNumber}</p>
	</c:if>
	<c:if test="${showStatus}">
	<c:if test="${not empty entry.status}">
		<p class="secondline"><spring:message code="cart.review.productDesc.status"/>${entry.status}</p>
	</c:if>
	</c:if>
	<p class="msgHighlight">${entry.product.hazmatCode}</p>
	
</span> --%>