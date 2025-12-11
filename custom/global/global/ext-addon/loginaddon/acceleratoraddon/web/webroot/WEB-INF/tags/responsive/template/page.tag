<%-- <%@ tag body-content="scriptless" trimDirectiveWhitespaces="true"%> --%>
<%@ attribute name="pageTitle" required="false" rtexprvalue="true"%>
<%@ attribute name="pageCss" required="false" fragment="true"%>
<%@ attribute name="pageScripts" required="false" fragment="true"%>

<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="header"	tagdir="/WEB-INF/tags/addons/loginaddon/responsive/common/header"%>
<%@ taglib prefix="footer" tagdir="/WEB-INF/tags/responsive/common/footer"%>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/responsive/cart"%>
<%@ taglib prefix="nav"	tagdir="/WEB-INF/tags/addons/loginaddon/responsive/nav"%>


<template:master pageTitle="${pageTitle}">
	<jsp:attribute name="pageCss">
		<jsp:invoke fragment="pageCss" />
	</jsp:attribute>

	<jsp:attribute name="pageScripts">
		<jsp:invoke fragment="pageScripts" />
	</jsp:attribute>
	<jsp:body >
	
		<div class="jnj-container full-height">
		<div class="row full-height" id="main-container">
		    <!-- Start - Userdetails & Menu -->
		   <div class="col-lg-3 col-md-3 pushmenu pushmenu-left" id="jnj-usr-details-menu">
		   <nav:leftnavigation />
		   </div>
		   	    <!-- end - Userdetails & Menu -->
		   
		   <div class="col-lg-9 col-md-9 col-sm-12 col-xs-12 full-height" id="content-area">
		   <!-- HEADER start -->
		   <header id="jnj-main-header">
		   
					<header:header />
					</header>
		   <!-- HEADER end -->
		   <!-- Start - Body Content -->
		   <section>
						 <div class="row jnj-body-padding" id="jnj-body-content">
						    <div class="col-lg-12 col-md-12">
						        <jsp:doBody />
				           </div>
				          
						</div>  
		   </section>
		   
		    <!-- end - Body Content -->
		    <div class="footer-area">
		   
					<footer:footer />
				
			</div>	
					
		   </div>
		</div>
		</div>
		
	</jsp:body>
</template:master>
