<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<c:url value="/my-account/template" var="templatePageURL" />
<div class='lightboxtemplate lightboxwidthsize1 sam saveorderastemplate'>
	<h2>Save order as template</h2>
	<div class='lightboxbody'>
		<c:choose>
			<c:when test="${templateSaved}">
				<div class='sectionBlock section1'>
				<p>Your order has been saved as template.</p>
				</div>
			</c:when>
			<c:otherwise>
				<div class='sectionBlock section1'>
					<p>Your order has not been saved as template.</p>
				</div>
			</c:otherwise>
		</c:choose>
		<div class='buttonWrapper txtCenter'>
			<a class='primarybtn' id="savetemplateYES" href="javascript:$.colorbox.close();">OK</a>
		</div>
	</div>
</div>