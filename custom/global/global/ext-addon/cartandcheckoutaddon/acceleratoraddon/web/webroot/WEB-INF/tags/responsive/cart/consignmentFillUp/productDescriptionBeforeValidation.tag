<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ attribute name="entry" type="com.jnj.facades.data.JnjGTOrderEntryData" required="true"%>
<%@ attribute name="errorCode" type="java.lang.String" required="false"%>
<%@ attribute name="rowcount" type="java.lang.Integer" required="false"%>
<%@ attribute name="showRemoveLink" type="java.lang.Boolean" required="false"%>
<%@ attribute name="showStatus" type="java.lang.Boolean" required="false"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<c:url value="${entry.product.url}" var="productUrl" />
<c:url value="${entry.product.code}" var="productCode" />
<!-- consignmentFillUp/productDescriptionBeforeValidation.tag -->
<div class="display-row">
  <div class="table-cell jnj-toggle-sign hidden-lg hidden-md hidden-sm">
    <a href="#mobi-collapse${rowcount}" data-toggle="collapse" data-parent="#accordion" class="toggle-link panel-collapsed skyBlue ipadacctoggle">
      <span class="glyphicon glyphicon-plus skyBlue toggle-plus-minus"></span>
    </a>
  </div>
  <div class="table-cell" style="vertical-align: middle">
    <!-- <img src="images/lactaid.jpg" class="imgprop"></img> -->
    <c:choose>
      <c:when test="${!entry.product.isProdViewable || empty productUrl}">
        <product:productPrimaryImage product="${entry.product}" format="cartIcon" />
      </c:when>
      <c:otherwise>
        <a href="javascript:;" class="showProductDeatils anchor-img" data="${productCode}"><product:productPrimaryImage product="${entry.product}"
            format="cartIcon" /></a>
      </c:otherwise>
    </c:choose>
  </div>
  <div class="table-cell" style="vertical-align: middle">
    <div class="Tablesubtxt">
      <p class="firstline" title="${entry.product.name}">
        <c:choose>
          <c:when test="${!entry.product.isProdViewable || empty productUrl}">
						${entry.product.name}
				</c:when>
          <c:otherwise>
            <a href="javascript:;" class="showProductDeatils" data="${productCode}">${entry.product.name}</a>
          </c:otherwise>
        </c:choose>
      </p>
      <p class="secondline">
        <spring:message code="cart.review.productDesc.jnJID" />
        <span class="strong">&nbsp;${entry.product.code}</span>
      </p>
      <c:if test="${not empty entry.originalOrderItem}">
        <c:if test="${entry.product.code ne entry.originalOrderItem}">
          <p class="secondline">
            <spring:message code="validatecartentry.original.itemcode" />
            <span class="strong">&nbsp;${entry.originalOrderItem}</span>
          </p>
        </c:if>
      </c:if>
      <input type="hidden" name="isProposedItem" value="${entry.isProposed}" />
      <c:if test="${not empty entry.product.gtin}">
        <p class="secondline">
          <spring:message code="cart.review.productDesc.gtnNumber" />${entry.product.gtin}</p>
      </c:if>
      <c:if test="${not empty entry.contractNumber}">
        <!-- should not show contract no here. need to show in the shopping page top, so that using hide -->
        <p class="secondline hide">
          <span class="contractNumberText">${entry.contractNumber}</span>
        </p>
      </c:if>
      <c:if test="${showStatus}">
        <c:if test="${not empty entry.status}">
          <p class="secondline">
            <spring:message code="cart.review.productDesc.status" />${entry.status}</p>
        </c:if>
      </c:if>
      <p class="msgHighlight">${entry.product.hazmatCode}</p>
      <c:forEach var="item" items="${errorCode}">
        <c:if test="${item == entry.product.code}">
          <p class="error-prod-msg">
            <spring:message code="dropshipment.error.productNotavailable" />
          </p>
        </c:if>
      </c:forEach>
    </div>
  </div>
</div>
