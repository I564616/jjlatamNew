<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<product:productAddToCartPanel product="${product}" allowAddToCart="${empty showAddToCart ? true : showAddToCart}" isMain="true" />