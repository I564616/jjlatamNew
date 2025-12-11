<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<div class="modal fade jnj-popup changeAcntPopup in" id="changeFranchisePopupForUsr" role="dialog" style="z-index: 1040; display: block;">
  <div class="modal-dialog modalcls modal-md" style="position: absolute; left: 374.5px;">
    <!-- Modal content-->
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close clsBtn" data-dismiss="modal">
          <spring:message code='homePage.newReturn.close' />
        </button>
        <h4 class="modal-title">
          <spring:message code='user.management.modify.franchises.text' />
        </h4>
      </div>
     <!--  <div class="checkbox checkbox-info selectchkbox">
        <div id="errorDiv" class="error"></div>
      </div> -->
      <!-- Fix for AAOL-6504 -->   
	
	<div class="panel-group" id="franchiseError" style="display: none">
		<div class="panel panel-danger">
			<div class="panel-heading">
				<div class="panel-title">
					<div class="row">
						<div class=" col-lg-11">
							<span class="glyphicon glyphicon-ban-circle"></span>
							<spring:message code="user.management.NoFranchiseError"/>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	
	
      <div class="checkbox checkbox-info selectchkbox selectAllHeader">
        <input id="franchiseSelectAll" class="styled franchise-thead-chckbox" type="checkbox" checked="checked"> <label
          for="franchiseSelectAll" id="contract-head-chck-label"><spring:message code='product.search.selectAll' /></label>
      </div>
      <div class="modalbody modal-content-scroll">
        <c:set var="selectedFranchiseComma" value=""></c:set>
        <c:if test="${not empty allowedFranchise}">
          <c:forEach items="${allowedFranchise}" var="franchise" varStatus="count">
            <c:choose>
              <c:when test="${empty selectedFranchiseComma}">
                <c:set var="selectedFranchiseComma" value="${franchise.code}"></c:set>
              </c:when>
              <c:otherwise>
                <c:set var="selectedFranchiseComma" value="${selectedFranchiseComma},${franchise.code}"></c:set>
              </c:otherwise>
            </c:choose>
          </c:forEach>
        </c:if>
        <form:input id="selectedFranchiseComma" value="${selectedFranchiseComma}" type="hidden" path="allowedFranchise"></form:input>
        <c:forEach items="${allFranchise}" var="franchise" varStatus="count">
          <div class="accountsMargin">
            <div class="checkbox checkbox-info selectchkbox display-table-cell">
              <c:choose>
                <c:when test="${empty fn:trim(selectedFranchiseComma) || fn:contains(selectedFranchiseComma,franchise.code)}">
                  <input id="${franchise.code}" class="styled franchise-tcell-chckbox" type="checkbox" value="${franchise.name}" name="chkFranchise"
                    checked="checked">
                </c:when>
                <c:otherwise>
                  <input id="${franchise.code}" class="styled franchise-tcell-chckbox" type="checkbox" value="${franchise.name}" name="chkFranchise">
                </c:otherwise>
              </c:choose>
              <label for="${franchise.code}"></label>
            </div>
            <div class="contract-check-content display-table-cell">
              <div>${franchise.name}</div>
            </div>
          </div>
        </c:forEach>
      </div>
      <div class="btn-mobile padding25">
        <button type="button" class="btn btnclsnormal reset  width20percent" id="franchisePopUpCancel">
          <spring:message code='profile.cancel' />
        </button>
        <button type="button" class="btn btnclsactive searchbtn pull-right width20percent" id="franchisePopUpOk">
          <spring:message code='profile.ok' />
        </button>
      </div>
    </div>
  </div>
</div>
