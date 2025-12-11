<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="deliveredCart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/delivered"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/desktop/product" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!-- Modal -->
<div class="modal fade" id="selectSurgeonPopup" role="dialog"
	data-firstLogin='true'>
	<form:form method="post" id="selectSurgeonNameForm" action="javascript:;">
		<input type="hidden" id="loadMoreCounter" value="1"> <input type="hidden" id="hospitalId" value="1">
		<div class="modal-dialog md-lg">

			<div class="modal-content popup">
				<div class="modal-header">
					<a href="" type="button" class="close clsBtn" data-dismiss="modal" style="font-weight:normal"><spring:message code="popup.close"></spring:message></a>
					<h4 class="modal-title selectTitle">
						<spring:message code="cart.surgeon.select.title"></spring:message>
					</h4>
					<div class="modal-title subtitleText">
						<spring:message code="cart.surgeon.select.title.message"></spring:message>
					</div>
				</div>
				<div class="modal-body">
					<div class="panel panel-danger">
						<div class="panel-heading">
							<h4 class="panel-title">
								<span><spring:message code="cart.surgeon.select.radio"></spring:message></span>
							</h4>
						</div>
					</div>
					<div class="marginbottom20px radio radio-info">
						<input type="radio" name="radio" id="surgeonRadio" value="option1" name="radioInline" onclick=""> 
						<label for="surgeonRadio"> <spring:message code="find.surgeon.text"></spring:message> </label> 
						<input type="text" name="surgeonName" id="surgeonIdInput" surgeonId="${data.code}" class="form-control searchSurgeonInput" readonly>
					</div>
					<div class="form-group searchArea" >
						<input type="text" class="form-control searchBox" placeholder="<spring:message code="find.surgeon.text"/>"
							name="surgeonName" id="searchSurgeon" value="${searchPattern}" disabled>
						<button class="glyphicon glyphicon-search searchBtn pull-right" value="<spring:message code='orderHistoryUpdateSurgeonpPopup.search'/>"  id="slctSurgeonBtn" style="border: none; background: none" disabled></button>
					</div>
				</div>
				<div class="list-group listclass li-grey-area">
					<div id="changeAccountPopup"
						class="changeAccountContent changeAccountColorBox">

						<ul class="accountListPopUpUL" style="margin-bottom: 0px">
							<c:forEach items="${pagedSurgeonData.results}" var="data">
								<c:set value="${data.displayName}(${data.code})" var="name"></c:set>
								<li id="accountListPopUp" class="accountListPopUp surgeonaccountList odd odd-row">
								<a href="javascript:;" class="changeAccountUnit list-group-item anchorcls marginbottom0px surgeonDetail"
									id="${name}" surgeonId="${data.code}" hospitalID="${data.hospitalId}">
								<p class="list-group-item-heading usercode" id="${data.code}">
											<span class="surgeonNameOnly">${name}</span>
											${data.displayAddress}
										</p>

								</a></li>
							</c:forEach>

						</ul>
					</div>
				</div>
			<div class="row viewmorecol">
					<c:if test="${pagedSurgeonData.pagination.numberOfPages>1}">
						<div class="text-center viewmore-holder">
						<a type="button" style="display: block; text-align: center;"
							data=${loadMoreCounter + 1} href="javascript:;"
							class="surgeonloadmore"><spring:message
								code="cart.common.viewMore" /></a>
						</div>
					</c:if>
					
					<div
						class="col-lg-12 col-md-12 col-md-12 col-xs-12 radio radio-info">

						<input type="radio" name="radio" id="radio2"
							value="option2" name="radioInline2" onclick="javascript:;">
						<label for="radio2"><spring:message
								code="cart.surgeon.unknown"></spring:message></label> <input
							type="hidden" name="surgeonName" id="surgeonIdInputradio2" />
					</div>
					<div
						class="col-lg-12 col-md-12 col-md-12 col-xs-12 radio radio-info">
						<input type="radio" name="radio" id="radio3"
							value="option3" name="radioInline2" onclick="javascript:;">
						<label for="radio3"><spring:message code="cart.surgeon.select.manually"></spring:message></label>
					</div>
					<div class="col-lg-12 col-md-12 col-md-12 col-xs-12 surge-name-row">
						<label class="surge-name" for="surgeonIdInputradio3"><spring:message code="cart.common.surgeonName.select"></spring:message><span
							class="redStar">*</span></label> <input
							class="surge-inputbox form-control" readonly name="selectSurgeon"
							id="surgeonIdInputradio3" data-msg-required="<spring:message code="surgeon.selection.error"/>"></input>
						<div class="registerError" id ="registerErrorSurgeon"></div> 
					</div>
					
					<div class="size1of1 cell" id="globalErrorSuergeon" style="display: none">
							
							<div class="cell cell size1of1">
								<div class="myProfileGlobalError"
									style="margin: 0 0 20px 0;">
									<label style="" class="globalError"><spring:message
											code="surgeon.selection.error" /><br></label>
								</div>
							</div>
						</div>
				</div>
				<div class="modal-footer ftrcls">
					<a href="#" class="cancel pull-left" data-dismiss="modal"><spring:message
							code="cart.common.cancel" /></a>
							 <a id="submitSurgeonUpdate" href="javascript:;" type="button"
						class="btn btnclsactive submitSurgeonOk mobile-auto-btn" >
						<spring:message code="cart.common.ok" /></a>
						
				</div>
			</div>

		</div>
	</form:form>
</div>
<!-- Surgeon Update Success Message -->
<div class="modal fade jnj-popup-container" id="success-dialogPopupSurgeon"
	role="dialog" data-firstLogin='true'>
	<div class="modal-dialog modalcls">
		<div class="modal-content popup ">
			<div class="modal-header"></div>
			<div class="modal-body">
				<div class="panel-group">
					<div class="panel panel-success">
						<div class="panel-heading">
							<h4 class="panel-title">
								<span><span class="glyphicon glyphicon-ok"></span> <spring:message
										code="order.update.surgeon.success"></spring:message></span>
							</h4>

						</div>

					</div>

				</div>
			</div>
			<div class="modal-footer modal-footer-style">

				<button type="button" class="btn btnclsactive pull-right mobile-auto-btn"
					data-dismiss="modal" id="dialog-okSurgeon">
					<spring:message code="cart.common.ok" />
				</button>
			</div>

		</div>
	</div>
</div>

<!--End of Modal pop-up-->


