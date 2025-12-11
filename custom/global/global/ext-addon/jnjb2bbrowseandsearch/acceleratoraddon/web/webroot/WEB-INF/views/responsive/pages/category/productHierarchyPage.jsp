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

				<c:forEach items="${subCategoriesData}" var="parentCategory" varStatus="count">
					<div class="row">
						<div class="col-xs-12 col-sm-6 col-md-6 col-lg-6 headingTxt content">${parentCategory.key}</div>
					</div>
				</c:forEach>

				<div class="boxshadow">
					<div class="paddingCatBox">

						<div class="col-lg-5 col-md-5 col-sm-5 col-xs-12 paddingright0px paddingleft0px catalogLev1Box">

							<c:forEach items="${subCategoriesDatas}" var="subCategoryEntry" varStatus="count">
								<c:set value="${subCategoryEntry.key}" var="catData"/>

								<c:choose>
									<c:when test ="${catData.isLeafCategory}">
										<div class="catalogLev1 catLev1 hoverEnabled" id="cat${count.count}">
											<c:url value="${catData.url}" var="catUrl"></c:url>
											<a href="${catUrl}"><span class="cat_text">${catData.name}</span><em class="fa fa-angle-right" aria-hidden="true"></em></a>
										</div>
									</c:when>
									<c:otherwise>
										<div class="catalogLev1 catLev1 hoverEnabled" id="cat${count.count}">
											<a><span class="cat_text">${catData.name}</span><em class="fa fa-angle-right" aria-hidden="true"></em></a>
										</div>
									</c:otherwise>
								</c:choose>
							</c:forEach>
						</div>
						<div class="col-lg-7 col-md-7 col-sm-7 col-xs-12 paddingleft0px paddingright0px catalogLev2Box">
							<c:forEach items="${subCategoriesDatas}" var="subCategoryEntry" varStatus="count">
								<div class="catalogLev2" id="sub_cat${count.count}">

									<c:forEach items="${subCategoryEntry.value}" var="subcategoryData">

									<c:url value="${subcategoryData.url}" var="categoryUrl"></c:url>
											<div class="catLev2"><a href="${categoryUrl}">${subcategoryData.name}</a></div>
									</c:forEach>
								</div>
							</c:forEach>

						</div>
						<div class="clear">
					</div>
				</div>
			</div>
			</div>

		</div>
	</div>
</div>

</template:page>