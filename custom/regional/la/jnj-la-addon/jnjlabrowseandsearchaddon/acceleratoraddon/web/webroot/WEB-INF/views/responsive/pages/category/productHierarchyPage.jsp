<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="template" 	tagdir="/WEB-INF/tags/addons/loginaddon/responsive/template"%>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/responsive/cart"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common"%>
<%@ taglib prefix="breadcrumb"  tagdir="/WEB-INF/tags/responsive/nav/breadcrumb"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>


<template:page pageTitle="${pageTitle}">
    <div id="catalog-page">
	    <div id="help-page">
		    <div class="row">
			    <div class="col-lg-12 col-md-12">
				    <ul class="breadcrumb">
					    <li><a class="active"><breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}"/></a></li>
				    </ul>

				    <c:forEach items="${subCategoriesDatas}" var="parentCategory" varStatus="countLevel1">
					    <div class="row">
						    <div class="col-xs-12 col-sm-6 col-md-6 col-lg-6 headingTxt content">${parentCategory.key.name}</div>
					    </div>

					    <div class="boxshadow">
                            <div class="paddingCatBox">
                                <div class="col-lg-5 col-md-5 col-sm-5 col-xs-12 paddingright0px paddingleft0px catalogLev1Box">
                                    <c:forEach items="${parentCategory.value}" var="subCategoryEntry" varStatus="countLevel2">
                                        <c:set value="${subCategoryEntry.key}" var="catData"/>
                                        <c:choose>
                                            <c:when test ="${catData.isLeafCategory}">
                                                <div class="catalogLev1 catLev1 hoverEnabled" id="cat${countLevel1.count}${countLevel2.count}">
                                                    <c:url value="${catData.url}" var="catUrl"></c:url>
                                                    <a href="${catUrl}"><span class="cat_text">${catData.name}</span><em class="fa fa-angle-right" aria-hidden="true"></em></a>
                                                </div>
                                            </c:when>
                                            <c:otherwise>
                                                <div class="catalogLev1 catLev1 hoverEnabled" id="cat${countLevel1.count}${countLevel2.count}">
                                                    <a><span class="cat_text">${catData.name}</span><em class="fa fa-angle-right" aria-hidden="true"></em></a>
                                                </div>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:forEach>

                                    <div class="catalogLev1 catLev1" id="cat${countLevel1.count}">
                                        <c:url value="${parentCategory.key.url}?showAllProducts=true" var="catUrl"></c:url>
                                        <a href="${catUrl}">
                                            <span class="cat_text">
                                                <c:choose>
                                                    <c:when test="${categoryCode eq 'Categories'}">
                                                        <spring:message code="product.hierarchy.see.all.products1"/>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <spring:message code="product.hierarchy.see.all.products2"/>
                                                    </c:otherwise>
                                                </c:choose>
                                            </span>
                                         </a>
                                    </div>
                                </div>
                                <div class="col-lg-7 col-md-7 col-sm-7 col-xs-12 paddingleft0px paddingright0px catalogLev2Box">
                                    <c:forEach items="${parentCategory.value}" var="subCategoryEntry" varStatus="countLevel2">
                                        <div class="catalogLev2" id="sub_cat${countLevel1.count}${countLevel2.count}">
                                            <c:choose>
                                                <c:when test="${categoryCode eq 'Categories'}">
                                                    <c:forEach items="${subCategoryEntry.value}" var="subcategoryData">
                                                        <c:url value="${subcategoryData.url}" var="categoryUrl"></c:url>
                                                        <div class="catLev2"><a href="${categoryUrl}">${subcategoryData.name}</a></div>
                                                    </c:forEach>
                                                    <c:url value="${subCategoryEntry.key.url}?showAllProducts=true" var="categoryUrl"></c:url>
                                                    <div class="catLev2"><a href="${categoryUrl}"><spring:message code="product.hierarchy.see.all.products2"/></a></div>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:url value="${subCategoryEntry.key.url}?showAllProducts=true" var="categoryUrl"></c:url>
                                                    <div class="catLev2"><a href="${categoryUrl}"><spring:message code="product.hierarchy.see.all.products2"/></a></div>
                                                    <c:forEach items="${subCategoryEntry.value}" var="subcategoryData">
                                                        <c:url value="${subcategoryData.url}" var="categoryUrl"></c:url>
                                                        <div class="catLev2"><a href="${categoryUrl}">${subcategoryData.name}</a></div>
                                                    </c:forEach>
                                                </c:otherwise>

                                            </c:choose>

                                        </div>
                                    </c:forEach>
                                </div>
                                <div class="clear"></div>
                            </div>
                        </div>
                        </br></br>
				    </c:forEach>
    			</div>
		    </div>
	    </div>
    </div>
</template:page>