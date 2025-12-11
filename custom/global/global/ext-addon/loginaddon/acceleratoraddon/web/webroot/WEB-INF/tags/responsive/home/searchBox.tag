<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>



<%-- <div id="global-search-txt-holder"  class="pull-right">
   <form name="search_form" method="get" action="<c:url value="/search"/>">
   <ycommerce:testId code="header_search_input">
   <input id="global-search-txt" class="form-control text placeholder ui-autocomplete-input" type="text" name="text" maxlength="100" placeholder='<spring:message code="header.information.search.text"/>' autocomplete="off" role="textbox" aria-autocomplete="list" aria-haspopup="true" style="color: rgb(204, 204, 204);">
    
	
	  </ycommerce:testId>
			<ycommerce:testId code="header_search_button">
			
			<button class="glyphicon glyphicon-search no-bnt-style" alt="" id="search-icon"/></button> 
	
	 
				</ycommerce:testId>	
	</form>								
									
</div> --%>
<div class="padding-0-tablet col-lg-4 col-md-4 col-sm-12 col-xs-12 order-sm-3 order-xs-3 order-lg-2 order-md-2 pull-right padding25_header">
	<div id="global-search-txt-holder" class="pull-right">
		<form name="search_form" method="get" action="<c:url value="/search"/>">
   			<ycommerce:testId code="header_search_input">
				<!-- <input type="text" class="form-control" id="global-search-txt" placeholder="Search Products"> -->
				  <input id="global-search-txt" class="form-control text ui-autocomplete-input" type="text" name="text" maxlength="100" placeholder='<spring:message code="header.information.search.text"/>' autocomplete="off" role="textbox" aria-autocomplete="list" aria-haspopup="true" style="color: rgb(204, 204, 204);">
			</ycommerce:testId>	
			<ycommerce:testId code="header_search_button">
				<!-- <span class="glyphicon glyphicon-search" id="search-icon"></span> -->
				<button class="glyphicon glyphicon-search no-bnt-style" alt="" id="search-icon"/>
					<i class="bi bi-search" style="-webkit-text-stroke: 1px;"></i>
				</button>  
			</ycommerce:testId>	
				<span class="fa fa-times" id="clear-txt-icon"></span>
		</form>			
	</div>
</div>