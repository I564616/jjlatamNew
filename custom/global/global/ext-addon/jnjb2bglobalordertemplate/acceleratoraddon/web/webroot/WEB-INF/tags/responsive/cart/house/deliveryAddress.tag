<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ attribute name="deliveryAddress" required="true" type="com.jnj.facades.data.JnjGTAddressData" %>
	<p class="forceDarkColorText companyName">${deliveryAddress.companyName}</p>
	<p class="forceDarkColorText lineOne">${deliveryAddress.line1}, ${deliveryAddress.line2}</p>
	<p class="forceDarkColorText lineTwo"></p> 
	<p class="forceDarkColorText countryName">${deliveryAddress.town}, ${deliveryAddress.region.name}, ${deliveryAddress.postalCode}</p>
	<p class="forceDarkColorText attnLine">${deliveryAddress.attnLine}</p>
