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
				<div class="row">
					<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 content"><spring:message code="category.catalog"/></div>
					<!-- <div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 rightcontent">
						<button type="button" class="btn pull-right fullbtn one-half boldtext"><spring:message code="category.buttonText"/></button>
						<button type="button" class="btn pull-right fullbtn second-half boldtext"><spring:message code="product.detail.consSpecification.brand"/></button>
						<span class="sortby pull-right boldtext"><spring:message code='product.search.sort'/></span>	
					</div> -->
				</div>
			</div>
		</div>	
		
		<div class="boxshadow">		
		<div class="row">
      <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" id="catalog-wrapper">
   
			<c:forEach items="${subCategoriesDatas}" var="subCategoryEntry" varStatus="count">
				<div class="help-accordian border-right-grey pull-left catalog-datas catalog-column">
							<div class="help-accordian-header catalog-header">
								<a class="ref_no">${subCategoryEntry.key}</a>
							</div>
							<div class="catalog-accordian-body">
								<c:forEach items="${subCategoryEntry.value}" var="subcategoryData">
								<c:set value="${fn:length(subcategoryData.value)}" var="noOfCategories"/>	
								<fmt:parseNumber var="noOfCategoriesFirstColumn" integerOnly="true" type="number" value="${noOfCategories / 3}" />
					             <c:if test="${noOfCategories % 3 != 0}" >
						           <c:set value="${noOfCategoriesFirstColumn + 1}" var="noOfCategoriesFirstColumn"/>
			                  </c:if>
								<c:forEach begin="0" end="${noOfCategoriesFirstColumn-1}" items="${subcategoryData.value}" var="categoryData">
								<c:url value="${categoryData.url}" var="categoryUrl"></c:url>
										<div class="help-links"><a href="${categoryUrl}">${categoryData.name}</a></div>
								</c:forEach>
								</c:forEach>
							</div>
					 	</div> 
					
		</c:forEach>	
			
			
	</div>	
</div>	
</div>
</div>
</div>
						
</template:page>