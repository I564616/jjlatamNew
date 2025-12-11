<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="priceData" required="true" type="de.hybris.platform.commercefacades.product.data.PriceData"%>
<%@ attribute name="quantity" required="true" type="java.lang.String"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%--
 Tag to render a currency formatted price.
 Includes the currency symbol for the specific currency.
--%>
<fmt:parseNumber var="formattedBasePrice" integerOnly="true" type="number" value="${priceData.value}" />
<fmt:parseNumber var="qtyAsNumber" integerOnly="true" type="number" value="${quantity}" />
${fn:substring(priceData.formattedValue, 0, 1)}${formattedBasePrice*qtyAsNumber}.00
