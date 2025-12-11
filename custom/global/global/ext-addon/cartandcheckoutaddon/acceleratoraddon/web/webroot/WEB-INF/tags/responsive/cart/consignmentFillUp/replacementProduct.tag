<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<!-- Modal -->
<div class="modal fade" id="replacementItemOrder-popup" role="dialog">
	<div class="modal-dialog modalcls">
		<div class="modal-content popup">
			<div class="modal-header">
			  <button type="button" class="close clsBtn select-accnt-close" data-dismiss="modal" id="select-accnt-close">
			  <spring:message code="password.forgotPassword.close"/></button>
			  <h4 class="modal-title selectTitle"><spring:message code="replacement.item.title"/></h4>
			</div>
			<div class="modal-body replacement-detail-desktab">
				<div class="row">
					<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
						<div class="marginBottom15px"><spring:message code="replacement.item.msg"/></div>
					</div>
				</div>
				<!-- AAOL-6378 -->
				 <div class="panel-group" id="obsolote-popup-error">
					<div class="panel panel-danger">
						<div class="panel-heading">
							<div class="panel-title">	
								<span class="glyphicon glyphicon-ban-circle"></span>&nbsp; <spring:message code="proposed.error.msg"/> 
							</div>
						</div>
					</div>										
				</div>
				<!-- AAOL-6378 -->
				<div class="row">
					<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 obsolote-table-holder">
						<table id="datatab-desktop" class="table table-bordered table-striped obsolote-table">
							<thead>
								<tr>
									<th class="no-sort text-uppercase" rowspan="1" colspan="1">
										<div class="checkbox checkbox-info selectchkbox selectAllHeader">  
											<input id="replacementSelectAll" name="replacementSelectAll" class="styled contract-thead-chckbox" type="checkbox">
										    <label for="replacementItemSelectAll"  id="contract-head-chck-label"><spring:message code="replacement.item.select"/></label>
										</div>
										</th>
										<th class="no-sort text-uppercase"><spring:message code="replacement.popup.order.lineItem"/></th>
										<th class="no-sort text-uppercase"><spring:message code="replacement.popup.order.itemcode"/></th>
										<th class="no-sort text-uppercase"><spring:message code="replacement.popup.order.desc"/></th>
										<th class="no-sort text-uppercase"><spring:message code="replacement.popup.order.qty"/></th>
										<th class="no-sort text-uppercase"><spring:message code="replacement.popup.replacement.itemcode"/></th>
										<th class="no-sort text-uppercase"><spring:message code="replacement.popup.replacement.desc"/></th>
										<th class="no-sort text-uppercase"><spring:message code="replacement.popup.replacement.qty"/></th>
								</tr>
							</thead>
							<tbody class="replacementListData">	
							<!-- AAOL-6378 -->
							<c:set var="counting" value="0"></c:set>
							
							<c:forEach items="${replacementItemList}" var="replacementOrderItemObj" varStatus="count">
							<c:set var="counting" value="${counting+1}"></c:set>
								<tr class="group-odd">
									<td>
										<div class="display-table-row">
											<div class="checkbox checkbox-info selectchkbox display-table-cell">
												<input type="checkbox" class="replacementItemSelection" id="proItemSelection_${count.count}" data-name="${replacementOrderItemObj.getHybrisLineItemNo()}"/>
												<label for="contract-check-1"></label>
											</div>																				
										</div>
									</td>
								  	<td id="hybrisLineItemNo_${replacementOrderItemObj.getHybrisLineItemNo()}" style="vertical-align: top;">${replacementOrderItemObj.getHybrisLineItemNo()}</td> 
									<td id="orderItemCd_${replacementOrderItemObj.getHybrisLineItemNo()}" style="vertical-align: top;">${replacementOrderItemObj.getOrderItemCd()}</td>  
									<td id="orderItemDesc_${replacementOrderItemObj.getHybrisLineItemNo()}" width="25%" style="vertical-align: top;">${replacementOrderItemObj.getOrderItemDesc()}</td>
									<td id="orderItemQty_${replacementOrderItemObj.getHybrisLineItemNo()}" style="vertical-align: top;">${replacementOrderItemObj.getOrderItemQty()}</td>
									<td id="replacementItemCd_${replacementOrderItemObj.getHybrisLineItemNo()}"  style="vertical-align: top;">
										<c:forEach items="${replacementOrderItemObj.getProposedItemCd()}" var="proposedList" varStatus="count">
										
										<div class="radio radio-info replace-item-radio-btn">
											<input type="radio" id="row-${counting}-inlineRadio${count.count}" value="${proposedList}" name="row-${counting}" class="propProd-radio" disabled>
											<label for="row-${counting}-inlineRadio${count.count}">${proposedList}</label>
										</div>
										
										
										</c:forEach>
									</td> 
									
									<td id="replacementItemdDesc_${replacementOrderItemObj.getHybrisLineItemNo()}" width="25%" style="white-space:nowrap;vertical-align: top;text-align:left">
									<c:forEach items="${replacementOrderItemObj.getProposedItemDesc()}" var="proposedDescList" varStatus="count">
										<div class="replace-item-desc">${proposedDescList}</div>
										</c:forEach>
									
									</td>
									<td id="replacementItemQtyTD_${replacementOrderItemObj.getHybrisLineItemNo()}"  style="vertical-align: top;">
									<input id="origReplaceItemQty_${replacementOrderItemObj.getHybrisLineItemNo()}" value="${replacementOrderItemObj.getProposedItemQty()}" type="hidden" />
									<c:forEach items="${replacementOrderItemObj.getProposedItemDesc()}" var="proposedDescList" varStatus="count">
										<input type="text" disabled="disabled" data-prodnumber="prodnumber-${count.count}" class="form-control prod-number replace-item-qty" id="replacementItemQty_${count.count}" 
										name="orderItemCd_${count.count}" value="${replacementOrderItemObj.getProposedItemQty()}">
										</c:forEach> 
										<!-- AAOL-6378 -->
									</td>
								</tr>
								</c:forEach>	
							</tbody>
						</table>
					</div>																											
				</div>													
			</div>	
			<div class="modal-footer ftrcls">	
				<%-- <div id="select-accnt-error-order-history" style="color:#b41601;float:left"><spring:message code="account.change.title.select"/></div>  --%>
				<a href="#" class="pull-left return-order-cancel select-accnt-close"  data-dismiss="modal"><spring:message code='reports.account.selection.cancel' /></a>
				<button type="button" class="btn btnclsactive" id="submit-replacementProd"><spring:message code='basket.add.to.cart' /></button>
			</div>
		</div>
	  
	</div>
</div>
<!--End of Modal pop-up-->
