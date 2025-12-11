<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ attribute name="deliveryAddress" required="true" type="com.jnj.facades.data.JnjGTAddressData" %>
<%@ attribute name="companyName" required="false" type="java.lang.String" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
	<c:choose>
		<c:when test="${deliveryAddress.companyName ne null}">
			<div class="subtext  companyName">${deliveryAddress.companyName}</div>
		</c:when>
		<c:otherwise>
			<div class="subtext  companyName">${deliveryAddress.firstName} ${deliveryAddress.lastName} </div>
		</c:otherwise>
	</c:choose>
	<div class="subtext  lineOne">${deliveryAddress.line1}
	<c:if test="${not empty deliveryAddress.line1}">,</c:if>
	${deliveryAddress.line2}</div>
	<div class="subtext  lineTwo"></div> 
	<div class="subtext  countryName">${deliveryAddress.town}
	<c:if test="${not empty deliveryAddress.town}">,</c:if>
	 ${deliveryAddress.region.name}
	 <c:if test="${not empty deliveryAddress.region.name}">,</c:if>
	  ${deliveryAddress.postalCode}</div>
  <div class="subtext  companyName">${deliveryAddress.taxid}</div>