<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="templategt" tagdir="/WEB-INF/tags/responsive/template" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="fn" uri="jakarta.tags.functions" %>
<templategt:replacejavascriptVariables /> 

<script type="text/javascript" src="${commonResourcePath}/js/jquery-3.5.1.min.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/jquery-migrate-3.0.1.min.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/bootstrap-5.1.3.bundle.min.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/tableHeadFixer.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/dataTables.bootstrap.min.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/bootstrap-datepicker.js"></script>

<!-- Change for language specific date format -->
<c:set var="dateParts" value="${fn:split(language, '_')}" />
<script type="text/javascript" src="${commonResourcePath}/js/bootstrap-datepicker.${dateParts[0]}.js"></script>

<script type="text/javascript" src="${commonResourcePath}/js/bootstrap-select.min.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/bootstrap3-typeahead.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/jquery.numeric.js"></script>
<!-- Added to Avoid validator error -->
<script type="text/javascript" src="${commonResourcePath}/js/jquery.validate.min.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/jquery.form.js"></script>
<!-- mobile pagiation slider files -->
<script type="text/javascript" src="${commonResourcePath}/js/swiper.min.js"></script>

<script type="text/javascript" src="${commonResourcePath}/js/slick.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/pgwslideshow.js"></script>
<!-- Added for Date Picker AAOL-5513 -->
<script type="text/javascript" src="${commonResourcePath}/js/jquery-date-format.js"></script>

<!-- mobile pagiation slider -->
<script type="text/javascript" src="${commonResourcePath}/js/pwdwidget.js"></script>


<script type="text/javascript" src="${commonResourcePath}/js/main.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/acc.main.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/pgwslideshow.js"></script>

<script type="text/javascript" src="${commonResourcePath}/js/acc.cartremoveitem.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/acc.cartPage.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/acc.createTemplate.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/acc.cartCheckoutPage.js"></script>


<!-- <script type="text/javascript" src="${commonResourcePath}/js/jquery.form-3.09.js"></script> -->
<%-- j query 1.5.1 --%>
 <%--  <script type="text/javascript" src="${commonResourcePath}/js/jquery-1.7.2.min.js"></script>   --%>

<%-- j query query 2.1.7 --%>
<!-- <script type="text/javascript" src="${commonResourcePath}/js/jquery.query-2.1.7.js"></script> --> 
<%-- jquery tabs dependencies --%>
<!-- <script type="text/javascript" src="${commonResourcePath}/js/jQuery-UI-v1.13.2.js"></script> -->
<!-- <script type="text/javascript" src="${commonResourcePath}/js/waypoints.min.js"></script> -->
 <script type="text/javascript" src="${commonResourcePath}/js/acc.emailpreference.js"></script>

 <script type="text/javascript" src="${commonResourcePath}/js/acc.usermanagementPopup.js"></script>




<script type="text/javascript" src="${commonResourcePath}/js/jquery.jcarousel-core.min.js"></script>


<%-- j query templates --%>
<!-- <script type="text/javascript" src="${commonResourcePath}/js/jquery.tmpl-1.0.0pre.min.js"></script> -->


<script type="text/javascript" src="${commonResourcePath}/js/jquery.blockUI.js"></script>





<%-- <script type="text/javascript" src="${commonResourcePath}/js/jquery.colorbox-min.js"></script> --%>


<%-- <script type="text/javascript" src="${commonResourcePath}/js/acc.reports.js"></script> --%>
<script type="text/javascript" src="${commonResourcePath}/js/common.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/acc.register.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/acc.langselector.js"></script>
<!-- popup related js file  -->
<script type="text/javascript" src="${commonResourcePath}/js/acc.changeaccountpopup.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/acc.changeDropShipAccount.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/acc.changeAddressPopup.js"></script>


<%-- We removed this file from loading, because it duplicates the quantity of products added to the cart
<script type="text/javascript" src="${commonResourcePath}/js/acc.addToCartHome.js"></script> --%>
<script type="text/javascript" src="${commonResourcePath}/js/acc.usermanagement.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/acc.userSearchPagination.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/acc.passwordExpireUserManagement.js"></script>

<%-- Slide Viewer --%>
<!-- <script type="text/javascript" src="${commonResourcePath}/js/jquery.slideviewer.custom.1.2.js"></script> -->
<!-- <script type="text/javascript" src="${commonResourcePath}/js/jquery.easing.1.3.js"></script> -->
<%-- Wait for images --%>
<!-- <script type="text/javascript" src="${commonResourcePath}/js/jquery.waitforimages.min.js"></script> -->
<%-- Scroll to --%>
<!-- <script type="text/javascript" src="${commonResourcePath}/js/jquery.scrollTo-1.4.2-min.js"></script> -->
<!-- <script type="text/javascript" src="${commonResourcePath}/js/jquery.ui.stars-3.0.1.min.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/jquery.form-3.09.js"></script> -->
<%-- BeautyTips  --%>
<%-- <script type="text/javascript" src="${commonResourcePath}/js/jquery.bgiframe-2.1.2.min.js"></script> --%>

<script type="text/javascript" src="${commonResourcePath}/js/jquery.bgiframe.js"></script>


<!--[if IE]><script type="text/javascript" src="${commonResourcePath}/js/excanvas-r3.compiled.js"></script>-->
<!-- <script type="text/javascript" src="${commonResourcePath}/js/jquery.bt-0.9.5-rc1.min.js"></script> -->
<%-- rama --%>
<script type="text/javascript" src="${commonResourcePath}/js/jquery.MultiFile.js"></script>
<%-- PasswordStrength  --%>
<script type="text/javascript" src="${commonResourcePath}/js/jquery.pstrength.custom-1.2.0.js"></script> 
<!-- <script type="text/javascript" src="${commonResourcePath}/js/acc.autocomplete.js"></script> -->

<script type="text/javascript" src="${commonResourcePath}/js/masonry.pkgd.js"></script>



<%-- <script type="text/javascript" src="${commonResourcePath}/js/recaptcha_ajax.js"></script> --%>

<!--<script type="text/javascript" src="${commonResourcePath}/js/acc.userlocation.js"></script>  -->
<%-- Custom ACC JS --%>
<!-- <script type="text/javascript" src="${commonResourcePath}/js/acc.track.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/acc.common.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/acc.cms.js"></script> -->

<script type="text/javascript" src="${commonResourcePath}/js/acc.product.js"></script> 
<!--<script type="text/javascript" src="${commonResourcePath}/js/acc.refinements.js"></script> 
<script type="text/javascript" src="${commonResourcePath}/js/acc.storefinder.js"></script> 
<script type="text/javascript" src="${commonResourcePath}/js/acc.carousel.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/acc.autocomplete.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/acc.pstrength.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/acc.password.js"></script>-->
<script type="text/javascript" src="${commonResourcePath}/js/acc.langselector.js"></script>
<%-- <script type="text/javascript" src="${commonResourcePath}/js/acc.minicart.js"></script> 
<script type="text/javascript" src="${commonResourcePath}/js/acc.userlocation.js"></script>--%>
<!-- <script type="text/javascript" src="${commonResourcePath}/js/acc.langcurrencyselector.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/acc.paginationsort.js"></script>  

<script type="text/javascript" src="${commonResourcePath}/js/acc.checkout.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/acc.cart.js"></script> 
<script type="text/javascript" src="${commonResourcePath}/js/acc.approval.js"></script>

<script type="text/javascript" src="${commonResourcePath}/js/acc.quote.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/acc.negotiatequote.js"></script>-->

<!-- <script type="text/javascript" src="${commonResourcePath}/js/acc.paymentmethod.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/acc.placeorder.js"></script>

<script type="text/javascript" src="${commonResourcePath}/js/acc.address.js"></script> -->
<script type="text/javascript" src="${commonResourcePath}/js/acc.refresh.js"></script>
<!-- <script type="text/javascript" src="${commonResourcePath}/js/acc.stars.js"></script> -->
<script type="text/javascript" src="${commonResourcePath}/js/acc.forgotpassword.js"></script>

<%-- accessible-tabs  --%>
<!-- <script type="text/javascript" src="${commonResourcePath}/js/jquery.accessible-tabs-1.9.7.min.js"></script>  
 <script type="text/javascript" src="${commonResourcePath}/js/acc.productDetail.js"></script> 
 <script type="text/javascript" src="${commonResourcePath}/js/acc.producttabs.js"></script> -->	

<script type="text/javascript" src="${commonResourcePath}/js/acc.orderHistory.js"></script>


<%-- b2b files  --%>
<%-- <script type="text/javascript" src="${themeResourcePath}/js/jquery.currencies.min.js"></script>

<script type="text/javascript" src="${themeResourcePath}/js/jquery.treeview.js"></script>
<script type="text/javascript" src="${themeResourcePath}/js/acc.mycompany.js"></script>
<script type="text/javascript" src="${themeResourcePath}/js/acc.futurelink.js"></script>
<script type="text/javascript" src="${themeResourcePath}/js/acc.search.js"></script>
<script type="text/javascript" src="${themeResourcePath}/js/acc.checkoutB2B.js"></script> --%>
<script type="text/javascript" src="${commonResourcePath}/js/acc.productorderform.js"></script>

 <script type="text/javascript" src="${commonResourcePath}/js/acc.formcheck.js"></script> 

<!-- For Contract Page -->
<!-- <script type="text/javascript" src="${commonResourcePath}/js/acc.contractDownload.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/acc.contractdetail.js"></script> -->
<%-- <script type="text/javascript" src="${commonResourcePath}/js/acc.helpContacUs.js"></script> --%>
<script type="text/javascript" src="${commonResourcePath}/js/acc.privacypolicypopup.js"></script>
 <script type="text/javascript" src="${commonResourcePath}/js/acc.legalnoticepopup.js"></script>
 <script type="text/javascript" src="${commonResourcePath}/js/acc.termconditionpopup.js"></script>
 <!-- Added for Remember Me Issue Fix -->
 <!-- <script type="text/javascript" src="${commonResourcePath}/js/loginaddon.js"></script> --> 

<%-- AddOn JavaScript files --%>
<c:forEach items="${addOnJavaScriptPaths}" var="addOnJavaScript">
    <script type="text/javascript" src="${addOnJavaScript}"></script>
</c:forEach>


 <script type="text/javascript" src="${commonResourcePath}/js/bootstrap-filestyle.min.js"></script>
<%-- Fix for Webkit Browsers (Needs to be loaded last)  --%>
<!-- <script type="text/javascript" src="${commonResourcePath}/js/acc.skiplinks.js"></script> -->

<!--Added for serialization-->
<script type="text/javascript" src="${commonResourcePath}/js/acc.serialization.js"></script>

<script type="text/javascript" src="${commonResourcePath}/js/swiper.js"></script>



