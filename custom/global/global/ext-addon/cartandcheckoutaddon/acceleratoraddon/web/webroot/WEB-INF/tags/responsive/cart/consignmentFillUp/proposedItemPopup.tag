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
<div class="modal fade" id="proposedItemOrder-popup" role="dialog">
	<div class="modal-dialog modalcls">
		<div class="modal-content popup">
			<div class="modal-header">
			  <button type="button" class="close clsBtn select-accnt-close" data-dismiss="modal" id="select-accnt-close">
			  <spring:message code="password.forgotPassword.close"/></button>
			  <h4 class="modal-title selectTitle"><spring:message code="proposed.item.title"/></h4>
			</div>
			<div class="modal-body proposed-detail-desktab">
				<div class="row">
					<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
						<div class="marginBottom15px"><spring:message code="proposed.item.msg"/></div>
					</div>
				</div>
				<!-- AAOL-6368 -->
				 <div class="panel-group" id="obsolote-popup-error">
					<div class="panel panel-danger">
						<div class="panel-heading">
							<div class="panel-title">	
								<span class="glyphicon glyphicon-ban-circle"></span>&nbsp; <spring:message code="proposed.error.msg"/>
							</div>
						</div>
					</div>										
				</div>
				<!-- AAOL-6368 -->
				<div class="row">
					<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 obsolote-table-holder">
						<table id="datatab-desktop" class="table table-bordered table-striped obsolote-table">
							<thead>
								<tr>
									<th class="no-sort text-uppercase" rowspan="1" colspan="1">
										<div class="checkbox checkbox-info selectchkbox selectAllHeader">  
											<input id="proposedItemSelectAll" name="proposedItemSelectAll" class="styled contract-thead-chckbox" type="checkbox">
										    <label for="proposedItemSelectAll"  id="contract-head-chck-label"><spring:message code="proposed.item.select"/></label>
										</div>
										</th>
										<th class="no-sort text-uppercase"><spring:message code="proposed.popup.order.lineItem"/></th>
										<th class="no-sort text-uppercase"><spring:message code="proposed.popup.order.itemcode"/></th>
										<th class="no-sort text-uppercase"><spring:message code="proposed.popup.order.desc"/></th>
										<th class="no-sort text-uppercase"><spring:message code="proposed.popup.order.qty"/></th>
										<th class="no-sort text-uppercase"><spring:message code="proposed.popup.proposed.itemcode"/></th>
										<th class="no-sort text-uppercase"><spring:message code="proposed.popup.proposed.desc"/></th>
										<th class="no-sort text-uppercase"><spring:message code="proposed.popup.proposed.qty"/></th>
								</tr>
							</thead>
							<tbody class="proposedListData">
							<!-- AAOL-6368 -->	
							<c:set var="counting" value="0"></c:set>
							<c:forEach items="${proposedOrderItemList}" var="proposedOrderItemObj" varStatus="count">
								<c:set var="counting" value="${counting+1}"></c:set>
								<tr class="group-odd">
									<td>
										<div class="display-table-row">
											<div class="checkbox checkbox-info selectchkbox display-table-cell">
												<input type="checkbox" class="proposedItemSelection" id="proItemSelection_${count.count}" data-name="${proposedOrderItemObj.getHybrisLineItemNo()}"/>
												<label for="contract-check-1"></label>
											</div>																				
										</div>
									</td>
								  	<td id="hybrisLineItemNo_${proposedOrderItemObj.getHybrisLineItemNo()}" style="vertical-align: top;">${count.count}</td> 
									<td id="orderItemCd_${proposedOrderItemObj.getHybrisLineItemNo()}" style="vertical-align: top;">${proposedOrderItemObj.getOrderItemCd()}</td>  
									<td id="orderItemDesc_${proposedOrderItemObj.getHybrisLineItemNo()}" width="25%" style="vertical-align: top;">${proposedOrderItemObj.getOrderItemDesc()}</td>
									<td id="orderItemQty_${proposedOrderItemObj.getHybrisLineItemNo()}" style="vertical-align: top;">${proposedOrderItemObj.getOrderItemQty()}</td>
									<td id="proposedItemCd_${proposedOrderItemObj.getHybrisLineItemNo()}" style="vertical-align: top;">
										<c:forEach items="${proposedOrderItemObj.getProposedItemCd()}" var="prodList" varStatus="count">
										<div class="radio radio-info replace-item-radio-btn">
											<input type="radio" id="row-${counting}-inlineRadio${count.count}" value="${prodList}" name="row-${counting}" class="propProd-radio" disabled>
											<label for="row-${counting}-inlineRadio${count.count}">${prodList}</label>
										</div>
										</c:forEach>
									</td>
									<td id="proposedItemdDesc_${proposedOrderItemObj.getHybrisLineItemNo()}" width="25%" style="vertical-align: top;white-space:nowrap; text-align: left;">
										<c:forEach items="${proposedOrderItemObj.getProposedItemDesc()}" var="prodDescList" varStatus="count">
											<div class="replace-item-desc">${prodDescList}</div>
										</c:forEach>
									</td>
									<td id="proposedItemQtyTD_${proposedOrderItemObj.getHybrisLineItemNo()}"  style="vertical-align: top;">
									<input id="origProposItemQty_${proposedOrderItemObj.getHybrisLineItemNo()}" value="${proposedOrderItemObj.getProposedItemQty()}" type="hidden" />
										<c:forEach items="${proposedOrderItemObj.getProposedItemDesc()}" var="prodDescList" varStatus="count">
											<input type="text" disabled="disabled" class="form-control prod-number replace-item-qty" id="proposedItemQty_${proposedOrderItemObj.getHybrisLineItemNo()}" 
											name="orderItemCd_${count.count}" value="${proposedOrderItemObj.getProposedItemQty()}" style="text-align: center;" data-prodnumber="prodnumber-${counting}">
										</c:forEach>
									</td>
								</tr>
								<!-- AAOL-6368 -->
								</c:forEach>	
							</tbody>
						</table>
					</div>																											
				</div>													
			</div>	
			<div class="modal-footer ftrcls">	
				<%-- <div id="select-accnt-error-order-history" style="color:#b41601;float:left"><spring:message code="account.change.title.select"/></div>  --%>
				<a href="#" class="pull-left return-order-cancel select-accnt-close"  data-dismiss="modal"><spring:message code='reports.account.selection.cancel' /></a>
				<button type="button" class="btn btnclsactive" id="submit-ProposedProd"><spring:message code='basket.add.to.cart' /></button>
				<!-- AAOL-6368 -->
			</div>
		</div>
	  
	</div>
</div>
<!--End of Modal pop-up-->
