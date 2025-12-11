<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div  class="col-lg-12 col-sm-12 col-md-12 col-xs-12 padding0 boxshadow">
    <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 headertxt">
        <spring:message code="product.detail.specification.specification"/>
    </div>
    <div class="col-lg-12 col-sm-12 col-md-12 col-xs-12 evencolor">
        <div class="col-lg-3 col-md-3 col-sm-9 col-xs-12 boldtxt">
            <spring:message code="product.search.delivered.uom"/>
        </div>
        <div class="col-lg-9 col-md-9 col-sm-9 col-xs-12">
            <div>${product.deliveryUnitCode}&nbsp; (${product.deliveryUnit})</div>
            <div>
                <spring:message code="product.search.each"/>&nbsp; ${product.deliveryUnitCode}
                <spring:message code="product.search.contains"/> ${product.numeratorDUOM} &nbsp; ${product.baseUnitCode}
            </div>
        </div>
    </div>
    <div class="col-lg-12 col-sm-12 col-md-12 col-xs-12 oddcolor">
        <div class="col-lg-3 col-md-3 col-sm-9 col-xs-12 boldtxt margintop15px">
            <spring:message code="product.search.sold.uom"/>
        </div>
        <div class="col-lg-9 col-md-9 col-sm-9 col-xs-12 margintop15px">
            <div>${product.salesUnitCode}&nbsp; (${product.salesUnit})</div>
            <div>
                <spring:message code="product.search.each"/>&nbsp; ${product.salesUnitCode}
                <spring:message code="product.search.contains"/> ${product.numeratorSUOM} &nbsp; ${product.baseUnitCode}
            </div>
        </div>
    </div>
    <c:if test="${not empty product.shipWeight}">
        <div class="col-lg-12 col-sm-12 col-md-12 col-xs-12 evencolor">
            <div class="col-lg-3 col-md-3 col-sm-9 col-xs-12 boldtxt">
                <spring:message code="product.detail.ship.weight"/>
            </div>
            <div class="col-lg-9 col-md-9 col-sm-9 col-xs-12">
                ${product.shipWeight}&nbsp;${product.shippingUnit}
            </div>
        </div>
    </c:if>
    <c:if test="${not empty product.width &&  not empty product.height && not empty product.length}">
        <div class="col-lg-12 col-sm-12 col-md-12 col-xs-12 oddcolor">
            <div class="col-lg-3 col-md-3 col-sm-9 col-xs-12 boldtxt">
                <spring:message code="product.detail.volume"/>
            </div>
            <div class="col-lg-9 col-md-9 col-sm-9 col-xs-12">
                ${product.length} x ${product.width} x ${product.height}&nbsp;${product.volumeUnit}
            </div>
        </div>
    </c:if>
    <div class="col-lg-12 col-sm-12 col-md-12 col-xs-12 evencolor">
        <div class="col-lg-3 col-md-3 col-sm-9 col-xs-12 boldtxt">
            <spring:message code="product.detail.ship.country.origin"/>
        </div>
        <div class="col-lg-9 col-md-9 col-sm-9 col-xs-12">
            ${product.originCountry}
        </div>
    </div>
    <div class="col-lg-12 col-sm-12 col-md-12 col-xs-12 padding10px">
        <div class="col-lg-12 col-sm-12 col-md-12 col-xs-12">
            <spring:message code="product.final.details.price.msg" />
        </div>
        <div class="col-lg-12 col-sm-12 col-md-12 col-xs-12">
            <spring:message code="product.final.details.deliveryUom.msg" />
        </div>
    </div>
	
	<c:if test="${not empty product.dataSheets}">
	<div class="boldtxt downloadpadding"><spring:message code="product.detail.specification.downloads"/></div>
	<ul>
		<li class="${index % 2 == 0 ? 'even' : 'odd'}">
			<div class="col-lg-9 col-md-9 col-sm-9 col-xs-12">
				<c:forEach var="dataSheetName" items="${product.dataSheets}"
					varStatus="status">
					<c:url
						value="${product.url}/downloadProductDetails/${dataSheetName.key}"
						var="downloadProductDetailsUrl" />
					<a href="${downloadProductDetailsUrl}">${dataSheetName.value}</a>
					<br />
				</c:forEach>
			</div> <c:set var="index" value="${index + 1}" scope="page" />
		</li>
		</ul>
	</c:if>
</div>