<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 headertxt"><spring:message code="product.detail.specification.specification"/></div>
<!--  AAOL-6138 changes start -->
<c:set var="dateformat"> <spring:message code='date.dateformat'/></c:set>
<!--  AAOL-6138 changes end -->
<div class="col-lg-12 col-sm-12 col-md-12 col-xs-12 evencolor">
	<div class="col-lg-3 col-md-3 col-sm-9 col-xs-12 boldtxt"><spring:message code="product.detail.specification.franchise"/></div>
	<div class="col-lg-9 col-md-9 col-sm-9 col-xs-12">${product.consumerSpecification.franchise}</div></br>
	<div class="col-lg-3 col-md-3 col-sm-9 col-xs-12 boldtxt"><spring:message code="product.detail.consSpecification.brand"/></div>
	<div class="col-lg-9 col-md-9 col-sm-9 col-xs-12">${product.consumerSpecification.brand}</div></br>
	<div class="col-lg-3 col-md-3 col-sm-9 col-xs-12 boldtxt"><spring:message code="product.detail.specification.status"/></div>
	<div class="col-lg-9 col-md-9 col-sm-9 col-xs-12">${product.status}</div>
</div>
<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 headertxt"><spring:message code="product.detail.consSpecification.shippingInformation"/></div>
<div class="col-lg-12 col-sm-12 col-md-12 col-xs-12 oddcolor">
	<div class="col-lg-3 col-md-3 col-sm-12 col-xs-12 boldtxt"><spring:message code="product.detail.consSpecification.shippingUnitOfMeasure"/></div>
	<div class="col-lg-9 col-md-9 col-sm-12 col-xs-12">${product.consumerSpecification.shippingInfo.shippingUom}</div></br>
	<div class="col-lg-3 col-md-3 col-sm-12 col-xs-12 boldtxt"><spring:message code="product.detail.consSpecification.eachesPerCase"/></div>
	<div class="col-lg-9 col-md-9 col-sm-12 col-xs-12">${product.consumerSpecification.shippingInfo.eaPerCase}</div></br>
	<div class="col-lg-3 col-md-3 col-sm-12 col-xs-12 boldtxt"><spring:message code="product.detail.consSpecification.innerPacksPerCase"/></div>
	<div class="col-lg-9 col-md-9 col-sm-12 col-xs-12">${product.consumerSpecification.shippingInfo.ipPerCAse}</div></br>
	<div class="col-lg-3 col-md-3 col-sm-12 col-xs-12 boldtxt"><spring:message code="product.detail.consSpecification.eachesPerInnerPacks"/></div>
	<div class="col-lg-9 col-md-9 col-sm-12 col-xs-12">${product.consumerSpecification.shippingInfo.eaPerInnerPacks}</div></br>
	<div class="col-lg-3 col-md-3 col-sm-12 col-xs-12 boldtxt"><spring:message code="product.detail.consSpecification.shipEffectiveDate"/></div>
	<div class="col-lg-9 col-md-9 col-sm-12 col-xs-12"><fmt:formatDate pattern="${dateformat}" value="${product.consumerSpecification.shippingInfo.shipEffectiveDate}" /></div></br>
	<div class="col-lg-3 col-md-3 col-sm-12 col-xs-12 boldtxt"><spring:message code="product.detail.consSpecification.tiersPerPallet"/></div>
	<div class="col-lg-9 col-md-9 col-sm-12 col-xs-12"><fmt:formatNumber type="number" value="${product.consumerSpecification.shippingInfo.trPerPallet}" maxFractionDigits="2" /></div></br>
	<div class="col-lg-3 col-md-3 col-sm-12 col-xs-12 boldtxt"><spring:message code="product.detail.consSpecification.casesPerTiers"/></div>
	<div class="col-lg-9 col-md-9 col-sm-12 col-xs-12"><fmt:formatNumber type="number" value="${product.consumerSpecification.shippingInfo.csPerTier}" maxFractionDigits="2" /></div>
</div>
<c:set value="${product.consumerSpecification.caseInfo}" var="caseInfo" ></c:set>
<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 headertxt"><spring:message code="product.detail.consSpecification.caseInformation"/></div>
<div class="col-lg-12 col-sm-12 col-md-12 col-xs-12 oddcolor">
<div class="col-lg-3 col-md-3 col-sm-12 col-xs-12 boldtxt"><spring:message code="product.detail.consSpecification.caseGtin"/></div>
<div class="col-lg-9 col-md-9 col-sm-12 col-xs-12">${caseInfo.gtin}</div></br>
<div class="col-lg-3 col-md-3 col-sm-12 col-xs-12 boldtxt"><spring:message code="product.detail.consSpecification.caseDepth"/></div>
<div class="col-lg-9 col-md-9 col-sm-12 col-xs-12">${caseInfo.depth} ${caseInfo.depthLinearUom}</div></br>
<div class="col-lg-3 col-md-3 col-sm-12 col-xs-12 boldtxt"><spring:message code="product.detail.consSpecification.caseHeight"/></div>
<div class="col-lg-9 col-md-9 col-sm-12 col-xs-12">${caseInfo.height} ${caseInfo.heightLinearUom}</div></br>
<div class="col-lg-3 col-md-3 col-sm-12 col-xs-12 boldtxt"><spring:message code="product.detail.consSpecification.caseWidth"/></div>
<div class="col-lg-9 col-md-9 col-sm-12 col-xs-12">${caseInfo.width} ${caseInfo.widthLinearUom}</div></br>
<div class="col-lg-3 col-md-3 col-sm-12 col-xs-12 boldtxt"><spring:message code="product.detail.consSpecification.caseVolume"/></div>
<div class="col-lg-9 col-md-9 col-sm-12 col-xs-12">${caseInfo.volume} ${caseInfo.volumeLinearUom}</div></br>
<div class="col-lg-3 col-md-3 col-sm-12 col-xs-12 boldtxt"><spring:message code="product.detail.consSpecification.caseWeight"/></div>
<div class="col-lg-9 col-md-9 col-sm-12 col-xs-12">${caseInfo.weight} ${caseInfo.weightLinearUom}</div></br>
<div class="col-lg-3 col-md-3 col-sm-12 col-xs-12 boldtxt"><spring:message code="product.detail.consSpecification.casesPrice"/></div>
<div class="col-lg-9 col-md-9 col-sm-12 col-xs-12">${product.consumerSpecification.casePrice.formattedValue}</div>
</div>
<c:set value="${product.consumerSpecification.eachesInfo}" var="eachesInfo" ></c:set> 
<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 headertxt"><spring:message code="product.detail.consSpecification.eachesInformation"/></div>
<div class="col-lg-12 col-sm-12 col-md-12 col-xs-12 oddcolor">
<div class="col-lg-3 col-md-3 col-sm-12 col-xs-12 boldtxt"><spring:message code="product.detail.consSpecification.eachesGtin"/></div>
<div class="col-lg-9 col-md-9 col-sm-12 col-xs-12">${eachesInfo.gtin}</div></br>
<div class="col-lg-3 col-md-3 col-sm-12 col-xs-12 boldtxt"><spring:message code="product.detail.consSpecification.eachesDepth"/></div>
<div class="col-lg-9 col-md-9 col-sm-12 col-xs-12">${eachesInfo.depth} ${caseInfo.depthLinearUom}</div></br>
<div class="col-lg-3 col-md-3 col-sm-12 col-xs-12 boldtxt"><spring:message code="product.detail.consSpecification.eachesHeight"/></div>
<div class="col-lg-9 col-md-9 col-sm-12 col-xs-12">${eachesInfo.height} ${caseInfo.heightLinearUom}</div></br>
<div class="col-lg-3 col-md-3 col-sm-12 col-xs-12 boldtxt"><spring:message code="product.detail.consSpecification.eachesWidth"/></div>
<div class="col-lg-9 col-md-9 col-sm-12 col-xs-12">${eachesInfo.width} ${caseInfo.widthLinearUom}</div></br>
<div class="col-lg-3 col-md-3 col-sm-12 col-xs-12 boldtxt"><spring:message code="product.detail.consSpecification.eachesVolume"/></div>
<div class="col-lg-9 col-md-9 col-sm-12 col-xs-12">${eachesInfo.volume} ${caseInfo.volumeLinearUom}</div></br>
<div class="col-lg-3 col-md-3 col-sm-12 col-xs-12 boldtxt"><spring:message code="product.detail.consSpecification.eachesWeight"/></div>
<div class="col-lg-9 col-md-9 col-sm-12 col-xs-12">${eachesInfo.weight} ${caseInfo.weightLinearUom}</div></br>
<div class="col-lg-3 col-md-3 col-sm-12 col-xs-12 boldtxt"><spring:message code="product.detail.consSpecification.eachesPrice"/></div>
<div class="col-lg-9 col-md-9 col-sm-12 col-xs-12">${product.consumerSpecification.eachPrice.formattedValue}</div>
</div>
















