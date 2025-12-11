<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/desktop/product"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>

<c:forEach items="${orderEntryDatas}" var="orderEntry">
		<div class="reportBody">
			<!--View Details row Starts-->
			<div class="reportRow">
				<div class="column1">
					<span>${orderEntry.orderLine}</span>
				</div>
				<div class="column2">
					<span>${orderEntry.cutReason}</span>
				</div>
				<div class="column3">
					<span>${orderEntry.cutQuantity}</span>
				</div>
				<div class="column4">
					<span>${orderEntry.orderQuantity}</span>
				</div>
				<div class="column5">
					<span>${orderEntry.unitOfMeasure}</span>
				</div>
				<div class="column6">
					<c:url value="${orderEntry.productUrl}" var="productURL" />
					<span><a href="${productURL}">${orderEntry.productCode}</a></span>
				</div>
				<div class="column7">
					<span>${orderEntry.productName}</span>
				</div>
				<div class="column8">
					<span>${orderEntry.itemPrice}</span>
				</div>
				<div class="column9">
					<span>${orderEntry.extendedPrice}</span>
				</div>
			</div>
			<!--View Details row Ends-->
		</div>
</c:forEach>