<%@ taglib prefix="breadcrumb" tagdir="/WEB-INF/tags/responsive/nav/breadcrumb"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart" %>
<%@ taglib prefix="commonTags" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/common"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common"%>
<div id="AddItemsCartpage">
	<breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}" />
	<div class="row content">
		<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12"><spring:message code="cart.review.shoppingCart"/></div>

	</div>
	<div id="globalMessages">
				<common:globalMessages />
				<cart:cartRestoration />
				<cart:cartValidation />
			</div>
	<div class="mainbody-container">
	<commonTags:Addresses/>
	<commonTags:changeAddressDiv/>
	<commonTags:changeBillToAddress/> 
	<div class="row jnjPanelbg">	     
	<div id="noProduct" style="display:none;color:red"><spring:message	code="cart.productnum.empty" /></div>    
		<div id="noQty" style="display:none;color:red"><spring:message	code="cart.incorrect.Qty.empty" /></div>          
	     <form:form name="mltiAddToCartForm" id="mltiAddToCartForm" action="javascript:;">
	     	<div class="col-lg-3 col-md-3 col-sm-4 col-xs-12">
				<div class="enter-product-label"><spring:message
									code="cart.enterproducts.header" /></div>
				<div class="enter-product-label-disc"><spring:message
									code="cart.commaseperated.text" /></div>		
			</div>
			<div class="col-lg-6 col-md-6 col-sm-4 col-xs-12 align-middle marginProd">
				<div class="float-right-to-none">
		            <div class="price-txt-width">
		             	 <input type="text" id ="prodCode"  class="form-control" placeholder="<spring:message	code="cart.prodCode.text" />"></input>
		             </div> 
		             <div class="price-quantity"> 
		             	 <input type="text"  id ="prodQty" class="form-control" placeholder="<spring:message	code="cart.prodQty.text" />"></input>
		             </div> 
		         </div> 		
			</div>
			<div class="col-lg-3 col-md-3 col-sm-4 col-xs-12">
				<div class="full-width-btns">
					<input type="button" class="btn btnclsnormal new-add-tocart full-width-btns" id="addToCartForm_2" value="<spring:message code='homePage.addtocart' />">
					<input type="button" style="" id="errorMultiCart" class="tertiarybtn homeCartErrors btn btnclsactive new-error-detail-btn full-width-btns" value="<spring:message code='homePage.errorDetails' />" />
				</div>
			</div>	
			<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style="margin-top:8px">
				<div class="registersucess registersucess-style"></div>
			</div>
			
</form:form>			
	 </div>
		<div class="cart-empty-table">
			<table id="cart-empty-table"
				class="table table-bordered table-striped">
				<thead>
					<tr>
						<th class="no-sort text-left"><spring:message code="cart.validate.product"/></th>
						<th class="no-sort cartqty"><spring:message code="cart.review.entry.quantity"/> 
						</th>
						<th class="no-sort cartprice"><spring:message code="cart.validate.unitPrice"/></th>
						<th class="no-sort multitotal-thead paddingleft10px" ><spring:message code="cart.review.entry.total"/></th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td class="text-left jnj-img-txt" colspan="4">
							<div class="row cart-emtymsg-row">
								<div
									class="col-lg-12 col-md-12 col-xs-12 text-center empty-cart-msg"><spring:message code="acc.build.order.emptymsg"/></div>
								<div
									class="continuebtn col-lg-12 col-md-12 col-xs-12 text-center">
										<div class="empty-btn clearCart marginbottom20px" >
											<cms:pageSlot position="BuildOrderCatalog" var="feature" element="div">
												<cms:component component="${feature}"/>
											</cms:pageSlot>
										</div>
								</div>
							</div>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
</div>

<!-- Error Details Popup Start-->

<div class="modal fade jnj-popup" id="error-detail-popup"
			role="dialog">
			<div class="modal-dialog modalcls modal-md">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close clsBtn" data-dismiss="modal"><spring:message code="cart.review.close" /></button>
						<h4 class="modal-title"><spring:message code="homePage.errorDetails" /></h4>
					</div>
					<form:form method="post" action="javascript:;">
						<div class="modal-body">
							<div class="row">
								<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
									<div class="panel-group">
										<div class="panel panel-danger">
											  <div class="panel-heading">
												<h4 class="panel-title cart-error-msg">
												<span><span class="glyphicon glyphicon-ban-circle"></span>&nbsp;<spring:message code="homePage.errordetails.addfailed" /></span>
												</h4>
											  </div>
										</div>  
									</div>
								</div>
								<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
									<div class="scroll error-content" style="font-weight:bold">
									</div>
								</div>
							</div>
						</div>
					</form:form>
				</div>
			</div>
		</div>
		
		<!-- AAOL-6378 -->	
	<div id="replacement-line-item-holder" style="display: none;"></div>
		<!-- Error Details Popup End-->
