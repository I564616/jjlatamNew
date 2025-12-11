<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<div class="col-lg-12 col-sm-12 col-md-12 col-xs-12 padding0 boxshadow">
	<div class="col-lg-12 col-sm-12 col-md-12 col-xs-12 padding0">
		<c:choose>
			<c:when
				test="${(not empty product.brandDescription or not empty product.documentsList) and !pdpTabsHideFlag}">
				<c:choose>
					<c:when
						test="${not empty product.brandDescription and not empty product.documentsList}">
						<div
							class="col-lg-4 col-sm-4 col-md-4 col-xs-4 product-desc-tab threePdpTabs lineHeight40"
							id="tab_1">
							<spring:message code="product.detail.aboutTheBrandTab" />
						</div>
						<div
							class="col-lg-4 col-sm-4 col-md-4 col-xs-4 product-desc-tab active threePdpTabs lineHeight40"
							id="tab_2">
							<spring:message code="product.detail.productDetailsTab" />
						</div>
						<div
							class="col-lg-4 col-sm-4 col-md-4 col-xs-4 product-desc-tab threePdpTabs"
							id="tab_3">
							<spring:message code="product.detail.additionalInformationTab" />
						</div>
					</c:when>
					<c:when
						test="${not empty product.brandDescription and empty product.documentsList}">
						<div
							class="col-lg-6 col-sm-6 col-md-6 col-xs-6 product-desc-tab twoPdpTabs lineHeight40"
							id="tab_1">
							<spring:message code="product.detail.aboutTheBrandTab" />
						</div>
						<div
							class="col-lg-6 col-sm-6 col-md-6 col-xs-6 product-desc-tab active twoPdpTabs lineHeight40"
							id="tab_2">
							<spring:message code="product.detail.productDetailsTab" />
						</div>
					</c:when>
					<c:otherwise>
						<div
							class="col-lg-6 col-sm-6 col-md-6 col-xs-6 product-desc-tab active twoPdpTabs lineHeight40"
							id="tab_2">
							<spring:message code="product.detail.productDetailsTab" />
						</div>
						<div
							class="col-lg-6 col-sm-6 col-md-6 col-xs-6 product-desc-tab twoPdpTabs"
							id="tab_3">
							<spring:message code="product.detail.additionalInformationTab" />
						</div>
					</c:otherwise>
				</c:choose>
			</c:when>
			<c:otherwise>
				<div
					class="col-lg-12 col-sm-12 col-md-12 col-xs-12 product-desc-tab active"
					id="tab_2">
					<spring:message code="product.detail.productDetailsTab" />
				</div>
			</c:otherwise>
		</c:choose>
	</div>
	<div
		class="col-lg-12 col-sm-12 col-md-12 col-xs-12 tab-description-box">
		<c:if test="${not empty product.brandDescription and !pdpTabsHideFlag}">
			<div id="desc1" class="tab_description">
				${product.brandDescription}</div>
		</c:if>
		<div id="desc2" class="tab_description active">
			<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 padding0">
			    <c:if test="${not empty product.classAttributeFeatureMap and not empty product.structuredClassifications and !pdpTabsHideFlag}">
                    <div class="col-lg-5 col-md-5 col-sm-5 col-xs-12">
                        <table class="product-attributes">
                            <thead>
                                <tr>
                                    <th colspan="2"><spring:message
                                            code="product.detail.attributes" /></th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="structuredClassification"
                                    items="${product.structuredClassifications}">
                                    <tr>
                                        <c:choose>
                                            <c:when test="${structuredClassification.key eq 'OTHER ATTRIBUTES'}">
                                                <td class="sublevel" colspan="2">
                                                    <spring:message code="product.detail.other.attribute" />
                                                </td>
                                            </c:when>
                                            <c:otherwise>
                                                <td class="sublevel" colspan="2">${structuredClassification.key}</td>
                                            </c:otherwise>
                                        </c:choose>
                                    </tr>
                                    <c:forEach var="rootFeature"
                                        items="${structuredClassification.value}">
                                        <tr>
                                            <td>${rootFeature.key}</td>
                                            <td>${rootFeature.value}</td>
                                        </tr>
                                    </c:forEach>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:if>
				<div class="col-lg-7 col-md-7 col-sm-7 col-xs-12">
					<table class="product-specifications">
						<thead>
							<tr>
								<th colspan="2"><spring:message
										code="product.detail.product.specifications" /></th>
							</tr>
						</thead>
						<tbody>
							<c:if test="${not empty product.catalogId}">
								<tr>
									<td><spring:message
											code="product.detail.basic.productCode" /></td>
									<td>${product.catalogId}</td>
								</tr>
							</c:if>
							<c:if test="${not empty product.mddSpecification.division}">
								<tr>
									<td><spring:message
											code="product.detail.mddSpecification.division" /></td>
									<td>${product.mddSpecification.division}</td>
								</tr>
							</c:if>
							<c:if test="${not empty product.status}">
								<tr>
									<td><spring:message
											code="product.detail.specification.status" /></td>
									<td>${product.status}</td>
								</tr>
							</c:if>
							<c:if test="${not empty product.deliveryUnitCode}">
                            	 <tr>
                            	 	 <td><spring:message
                            				 code="product.search.delivered.uom" /></td>
                            		 <td>${product.deliveryUnitCode}&nbsp; (${product.deliveryUnit})<br>
                            		     <spring:message code="product.search.each"/>&nbsp; ${product.deliveryUnitCode}
                                         <spring:message code="product.search.contains"/> ${product.numeratorDUOM} &nbsp; ${product.baseUnitCode}
                            		 </td>
                            	 </tr>
                            </c:if>
                            <c:if test="${not empty product.salesUnitCode}">
                                 <tr>
                            		 <td><spring:message code="product.search.sold.uom" /></td>
                            		 <td>${product.salesUnitCode}&nbsp; (${product.salesUnit})<br>
                            		     <spring:message code="product.search.each"/>&nbsp; ${product.salesUnitCode}
                                         <spring:message code="product.search.contains"/> ${product.numeratorSUOM} &nbsp; ${product.baseUnitCode}
                            		 </td>
                            	 </tr>
                            </c:if>
							<c:if test="${not empty product.length}">
								<tr>
									<td><spring:message
											code="product.detail.mddSpecification.depth" /></td>
									<td>${product.length}</td>
								</tr>
							</c:if>
							<c:if test="${not empty product.height}">
								<tr>
									<td><spring:message
											code="product.detail.mddSpecification.height" /></td>
									<td>${product.height}</td>
								</tr>
							</c:if>
							<c:if test="${not empty product.width}">
								<tr>
									<td><spring:message
											code="product.detail.mddSpecification.width" /></td>
									<td>${product.width}</td>
								</tr>
							</c:if>
							<c:if test="${not empty product.volume}">
								<tr>
									<td><spring:message
											code="product.detail.volume" /></td>
									<td>${product.volume}&nbsp;${product.volumeUnit}</td>
								</tr>
							</c:if>
							<c:if test="${not empty product.shipWeight}">
								<tr>
									<td><spring:message
											code="product.detail.ship.weight" /></td>
									<td>${product.shipWeight}&nbsp;${product.shippingUnit}</td>
								</tr>
							</c:if>
							<c:if test="${not empty product.originCountry}">
                            	<tr>
                            		<td><spring:message
                            				 code="product.detail.ship.country.origin" /></td>
                            		<td>${product.originCountry}</td>
                            	</tr>
                            </c:if>
						</tbody>
					</table>
				</div>
			</div>
		</div>

		<div id="desc3" class="tab_description">
		    <c:if test="${not empty product.documentsList and !pdpTabsHideFlag}">
                <c:forEach items="${product.documentsList}" var="productDocuments"
                    varStatus="count">
                    <p class="additional_info_tab">
                        <c:choose>
                            <c:when test="${fn:contains(productDocuments.urlLink, 'youtube.com') || fn:contains(productDocuments.name, 'video')}">
                                <a href="${productDocuments.urlLink}" target="_blank">
                                <em class="fa fa-youtube"></em>${productDocuments.name}</a>
                            </c:when>
                            <c:when test="${fn:contains(productDocuments.name, '.pdf')}">
                                <a href="${productDocuments.urlLink}" target="_blank">
                                <em class="fa fa-file-pdf"></em>${productDocuments.name}</a>
                            </c:when>
                            <c:otherwise>
                                <a href="${productDocuments.urlLink}" target="_blank">
                                <em class="fa fa-external-link"></em>${productDocuments.name}</a>
                            </c:otherwise>
                        </c:choose>
                    </p>
                </c:forEach>
			</c:if>
		</div>
	</div>
</div>