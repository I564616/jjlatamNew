<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/loginaddon/responsive/template"%>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/responsive/cart"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common"%>
<%@ taglib prefix="breadcrumb" tagdir="/WEB-INF/tags/responsive/nav/breadcrumb"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="templateLa" tagdir="/WEB-INF/tags/addons/jnjlaloginaddon/responsive/template"%>


<templateLa:page pageTitle="${pageTitle}">
	<div id="catalog-page">
		<div id="help-page">
			<div class="row">
				<div class="col-lg-12 col-md-12">
					<ul class="breadcrumb">
						<li><a class="active"><breadcrumb:breadcrumb
									breadcrumbs="${breadcrumbs}" /></a></li>
					</ul>
					<div class="row">
						<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 content">
						    <spring:message code="text.home.catalog"/>
						</div>
					</div>
				</div>
			</div>
			<div class="boxshadow">
				<div class="row">
					<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 ">
						<c:forEach items="${subCategoriesData}" var="subCategoryEntry" varStatus="count">
							<c:choose>
								<c:when test="${count.count mod 2 != 0}">
									<div class="help-accordian border-right-grey pull-left"
										style="width: 50%; padding-right: 0px !important">
										<div class="help-accordian-header catalog-header">
											<c:if test="${subCategoryEntry.key ne defaultCategory}">
											    <c:forEach items="${highCategoriesLevel}" var="highCategoryData" varStatus="count2">
											        <c:forEach items="${highCategoryData.value}" var="highLevelCategoryData">
											            <c:if test="${(count2.index eq count.index)}">
											                <c:choose>
                                                                <c:when test="${highLevelCategoryData.displayProducts}">
											                        <c:url value="${highLevelCategoryData.categoryData.url}" var="highCategoryUrl"></c:url>
											                            <a href="${highCategoryUrl}" class="ref_no showLink"
											                                id="showAll${count.index}${count2.index}">${subCategoryEntry.key}</a>
											                            <div class="showAll${count.index}${count2.index} tipPositionNormal tooltip"
											                                style="width: 200px; z-index: 1000; display: none;">
											                                <span class="imghArrow">&nbsp;</span>
                                                                            <span class="tooltiptext tooltipTextDetails">
                                                                                <spring:message code="show.all.category.page"/>
                                                                        </div>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <a class="ref_no">${subCategoryEntry.key}</a>
                                                                </c:otherwise>
							                                 </c:choose>
                                                        </c:if>
                                                    </c:forEach>
                                                </c:forEach>
											</c:if>
										</div>

										<div class="catalog-accordian-body">
											<c:forEach items="${subCategoryEntry.value}" var="subcategoryData">
												<c:url value="${subcategoryData.url}" var="categoryUrl"></c:url>
												<div class="help-links">
													<a href="${categoryUrl}">${subcategoryData.name}</a>
												</div>

											</c:forEach>
										</div>
									</div>

								</c:when>
								<c:otherwise>
									<div class="help-accordian border-left-grey pull-right"
										style="width: 50%; padding-left: 0px !important">
										<div class="help-accordian-header catalog-header">
											<c:if test="${subCategoryEntry.key ne defaultCategory}">
											    <c:forEach items="${highCategoriesLevel}" var="highCategoryData" varStatus="count2">
											        <c:forEach items="${highCategoryData.value}" var="highLevelCategoryData">
                                                        <c:if test="${(count2.index eq count.index)}">
											                <c:choose>
                                                                <c:when test="${highLevelCategoryData.displayProducts}">
											                        <c:url value="${highLevelCategoryData.categoryData.url}" var="highCategoryUrl"></c:url>
											                            <a href="${highCategoryUrl}" class="ref_no showLink"
											                                id="showAll${count.index}${count2.index}">${subCategoryEntry.key}</a>
											                            <div class="showAll${count.index}${count2.index} tipPositionNormal tooltip"
											                                style="width: 200px; z-index: 1000; display: none;">
											                                <span class="imghArrow">&nbsp;</span>
                                                                            <span class="tooltiptext tooltipTextDetails">
                                                                                <spring:message code="show.all.category.page"/>
                                                                        </div>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <a class="ref_no">${subCategoryEntry.key}</a>
                                                                </c:otherwise>
							                                 </c:choose>
                                                        </c:if>
                                                    </c:forEach>
                                                </c:forEach>
											</c:if>
										</div>

										<div class="catalog-accordian-body">
											<c:forEach items="${subCategoryEntry.value}"
												var="subcategoryData">
												<c:url value="${subcategoryData.url}" var="categoryUrl"></c:url>
												<div class="help-links">
													<a href="${categoryUrl}">${subcategoryData.name}</a>
												</div>
											</c:forEach>
										</div>
									</div>

								</c:otherwise>
							</c:choose>
						</c:forEach>
					</div>
				</div>
			</div>
		</div>
</templateLa:page>