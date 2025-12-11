<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ attribute name="deliveryAddress" required="true" type="com.jnj.facades.data.JnjGTAddressData" %>
<%@ attribute name="companyName" required="false" type="java.lang.String" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
	<c:choose>
		<c:when test="${deliveryAddress.companyName ne null}">
			<p class="subtext boldtext companyName">${deliveryAddress.companyName}</p>
		</c:when>
		<c:otherwise>
			<p class="subtext boldtext companyName">${deliveryAddress.firstName} ${deliveryAddress.lastName} </p>
		</c:otherwise>
	</c:choose>
	<p class="subtext  lineOne">${deliveryAddress.line1}<%-- , ${deliveryAddress.line2} --%></p>
	<!-- <p class="subtext boldtext lineTwo"></p>  -->
	<p class="subtext  countryName">${deliveryAddress.town},<%--  ${deliveryAddress.region.name},  --%>${deliveryAddress.postalCode}</p>
