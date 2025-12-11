<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ attribute name="deliveryAddress" required="true" type="com.jnj.facades.data.JnjGTAddressData" %>
<%@ attribute name="companyName" required="false" type="java.lang.String" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
	<c:choose>
		<c:when test="${companyName ne null}">
			<p class="forceDarkColorText companyName">${companyName}</p>
		</c:when>
		<c:otherwise>
			<p class="forceDarkColorText companyName">${deliveryAddress.firstName} ${deliveryAddress.lastName} </p>
		</c:otherwise>
	</c:choose>
	<p class="forceDarkColorText lineOne">${deliveryAddress.line1}, ${deliveryAddress.line2}</p>
	<p class="forceDarkColorText lineTwo"></p> 
	<p class="forceDarkColorText countryName">${deliveryAddress.town}
	
	<c:if test="${not empty deliveryAddress.region.name}">
	, ${deliveryAddress.region.name}
	</c:if>
	<c:if test="${not empty deliveryAddress.postalCode}">
	, ${deliveryAddress.postalCode}
	</c:if>
	</p>
