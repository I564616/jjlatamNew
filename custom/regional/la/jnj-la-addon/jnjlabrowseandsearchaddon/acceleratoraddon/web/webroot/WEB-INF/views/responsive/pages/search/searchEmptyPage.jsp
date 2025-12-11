<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/loginaddon/responsive/template" %>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/responsive/cart" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/responsive/nav" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common" %>
<%@ taglib prefix="breadcrumb" tagdir="/WEB-INF/tags/addons/jnjb2bbrowseandsearch/responsive/nav/breadcrumb" %>
<%@ taglib prefix="templateLa" tagdir="/WEB-INF/tags/addons/jnjlaloginaddon/responsive/template"%>

<templateLa:page pageTitle="${pageTitle}">
	
		<div class="col-lg-12 col-md-12">
			<div class="row">
				<div class="col-xs-12 col-md-12 col-sm-12 col-lg-12 headerLinks">
					<breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}"/>
				</div>
			</div>
			<h1><spring:message code="product.search.result.textfor" /></h1> 
	<c:if test="${not empty message}">
		<spring:theme code="${message}"/>
	</c:if>
	<c:if test="${specialCustomer eq true}">
	<div class="row">
				<div class="col-lg-12 col-md-12">
					<div class="table-padding no-search-result-msg"><spring:message code="header.information.account.customer.group"/></div>
				</div>
			</div>
	</c:if>
			<div class="row">
				<div class="col-lg-12 col-md-12">
					<div class="table-padding no-search-result-msg"> <spring:message code="product.search.noResults"/></div>
				</div>
			</div>
		</div>
</templateLa:page>