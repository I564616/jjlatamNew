<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<div  class="col-lg-12 col-sm-12 col-md-12 col-xs-12 padding0 boxshadow">
<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 headertxt"><spring:message code="product.detail.specification.specification"/></div>
<div class="col-lg-12 col-sm-12 col-md-12 col-xs-12 evencolor">
	<div class="col-lg-3 col-md-3 col-sm-9 col-xs-12 boldtxt"><spring:message code="product.detail.specification.franchise"/></div>
	<div class="col-lg-9 col-md-9 col-sm-9 col-xs-12">${product.mddSpecification.franchise}</div>
	<div class="col-lg-3 col-md-3 col-sm-9 col-xs-12 boldtxt"><spring:message code="product.detail.mddSpecification.division"/></div>
	<div class="col-lg-9 col-md-9 col-sm-9 col-xs-12">${product.mddSpecification.division}</div>
</div>
<div class="col-lg-12 col-sm-12 col-md-12 col-xs-12 oddcolor">
	<div class="col-lg-3 col-md-3 col-sm-12 col-xs-12 boldtxt"><spring:message code="product.detail.specification.status"/></div>
	<div class="col-lg-9 col-md-9 col-sm-12 col-xs-12">${product.status}</div>
</div>
<div class="col-lg-12 col-sm-12 col-md-12 col-xs-12 evencolor">
	<div class="col-lg-3 col-md-3 col-sm-9 col-xs-12 boldtxt"><spring:message code="product.detail.mddSpecification.kitComponentName"/></div>
	
	
	
	<div class="col-lg-9 col-md-9 col-sm-9 col-xs-12 spacediv">&nbsp;&nbsp;
	<c:forEach items="${product.mddSpecification.kitComponentNames}" var="kitName" varStatus="status">
								<span class="contentText">${kitName}</span>
								<c:if test="${!status.last}" >,</c:if>  
	</c:forEach>
	</div>
	
	
	
	
	<div class="col-lg-3 col-md-3 col-sm-9 col-xs-12 boldtxt"><spring:message code="product.detail.mddSpecification.kitComponentDescription"/></div>
	<div class="col-lg-9 col-md-9 col-sm-9 col-xs-12 spacediv">&nbsp;&nbsp;
	<c:forEach items="${product.mddSpecification.kitComponentDesc}" var="kitDescription" varStatus="status">
								${kitDescription}<c:if test="${!status.last}" >,</c:if>  
    </c:forEach>
	
	</div>
	
	
	
	<div class="col-lg-3 col-md-3 col-sm-9 col-xs-12 boldtxt"><spring:message code="product.detail.mddSpecification.kitComponetUnit"/></div>
	<div class="col-lg-9 col-md-9 col-sm-9 col-xs-12 spacediv">&nbsp;&nbsp;
	<c:forEach items="${product.mddSpecification.kitComponentUnits}" var="kitUnit" varStatus="status">
								${kitUnit}
								<c:if test="${!status.last}" >,</c:if>  
						</c:forEach>
	</div>
</div>
<div class="col-lg-12 col-sm-12 col-md-12 col-xs-12 oddcolor">
	<div class="col-lg-3 col-md-3 col-sm-9 col-xs-12 boldtxt"><spring:message code="product.detail.mddSpecification.eachGtin"/></div>
	<div class="col-lg-9 col-md-9 col-sm-9 col-xs-12 spacediv">&nbsp;&nbsp;${product.mddSpecification.eachGtin}</div>
	<div class="col-lg-3 col-md-3 col-sm-9 col-xs-12 boldtxt"><spring:message code="product.detail.mddSpecification.caseGtin"/></div>
	<div class="col-lg-9 col-md-9 col-sm-9 col-xs-12">${product.mddSpecification.caseGtin}</div>
</div>
<div class="col-lg-12 col-sm-12 col-md-12 col-xs-12 evencolor">
	<div class="col-lg-3 col-md-3 col-sm-9 col-xs-12 boldtxt"><spring:message code="product.detail.mddSpecification.unitOfMeasure"/></div>
	<div class="col-lg-9 col-md-9 col-sm-9 col-xs-12">
		<c:if test="${product.mddSpecification.salesUom != null}">
			<span class="contentText">${product.mddSpecification.salesUom.name}
				(${product.mddSpecification.salesUom.numerator} &nbsp;
				${product.mddSpecification.salesUom.lwrPackagingLvlUomCode})</span>
			<p><spring:message code='popup.product.sellQty'/></p>
		</c:if>

	</div>
	<div class="col-lg-3 col-md-3 col-sm-9 col-xs-12 boldtxt"><spring:message code="product.detail.mddSpecification.deliveryUnitOfMeasure"/></div>
	<div class="col-lg-9 col-md-9 col-sm-9 col-xs-12">
		<c:if
			test="${product.mddSpecification.deliveryUom != null && product.mddSpecification.salesUom != null}">
			<div>
				<c:set value="${product.mddSpecification.deliveryUom}"	var="deliveryUom" />
				<c:set value="${product.mddSpecification.salesUom}" var="salesUom" />
				${deliveryUom.packagingLevelQty/salesUom.packagingLevelQty}
				${product.mddSpecification.salesUom.name}
				(${(deliveryUom.packagingLevelQty/salesUom.packagingLevelQty) * product.mddSpecification.salesUom.numerator}
				&nbsp; ${product.mddSpecification.salesUom.lwrPackagingLvlUomCode})
			</div>
			<div><spring:message code='popup.product.shipQty'/></div>
		</c:if>
	</div>
</div>
<c:set value="${product.mddSpecification.deliveryUom}" var="deliveryUom" />
<div class="col-lg-12 col-sm-12 col-md-12 col-xs-12 oddcolor">
	<div class="col-lg-3 col-md-3 col-sm-9 col-xs-12 boldtxt"><spring:message code="product.detail.mddSpecification.depth"/></div>
	<div class="col-lg-9 col-md-9 col-sm-9 col-xs-12">${deliveryUom.depth} ${deliveryUom.depthLinearUom}</div><br>
	<div class="col-lg-3 col-md-3 col-sm-9 col-xs-12 boldtxt"><spring:message code="product.detail.mddSpecification.height"/></div>
	<div class="col-lg-9 col-md-9 col-sm-9 col-xs-12">${deliveryUom.height} ${deliveryUom.heightLinearUom}</div><br>
	<div class="col-lg-3 col-md-3 col-sm-9 col-xs-12 boldtxt"><spring:message code="product.detail.mddSpecification.width"/></div>
	<div class="col-lg-9 col-md-9 col-sm-9 col-xs-12">${deliveryUom.width} ${deliveryUom.widthLinearUom}</div><br>
	<div class="col-lg-3 col-md-3 col-sm-9 col-xs-12 boldtxt"><spring:message code="product.detail.mddSpecification.volume"/></div>
	<div class="col-lg-9 col-md-9 col-sm-9 col-xs-12">${deliveryUom.volume} ${deliveryUom.volumeLinearUom}</div><br>
	<div class="col-lg-3 col-md-3 col-sm-9 col-xs-12 boldtxt"><spring:message code="product.detail.mddSpecification.weight"/></div>
	<div class="col-lg-9 col-md-9 col-sm-9 col-xs-12">${deliveryUom.weight} ${deliveryUom.weightLinearUom}</div>
	
</div>
</div>