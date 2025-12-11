<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/responsive/cart"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<c:choose>
	<c:when test="${errorFlag == null || errorFlag == 0 }">
		<cart:dropShipAccounts />
	</c:when>
	<c:otherwise>
~Error Occurred~
</c:otherwise>
</c:choose>