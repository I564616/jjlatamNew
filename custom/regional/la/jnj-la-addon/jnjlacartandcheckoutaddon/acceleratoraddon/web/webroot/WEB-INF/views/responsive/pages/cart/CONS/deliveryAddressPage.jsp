<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="house" tagdir="/WEB-INF/tags/responsive/cart/house"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<c:choose>
	<c:when test="${errorFlag == null || errorFlag == 0 }">
		<house:deliveryAddress deliveryAddress="${deliveryAddress}" />
	</c:when>
	<c:otherwise>
~Error Occurred~
</c:otherwise>
</c:choose>
