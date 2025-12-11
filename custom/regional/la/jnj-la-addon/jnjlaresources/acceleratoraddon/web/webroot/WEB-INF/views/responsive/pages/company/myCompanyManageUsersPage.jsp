<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/loginaddon/responsive/template"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/addons/jnjb2bbrowseandsearch/responsive/nav"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="resource" tagdir="/WEB-INF/tags/addons/jnjglobalresources/responsive/resource"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="breadcrumb" tagdir="/WEB-INF/tags/addons/jnjb2bbrowseandsearch/responsive/nav/breadcrumb"%>

<spring:url value="/my-company/organization-management/manage-users/create" var="manageUsersUrl" />
<spring:url value="/resources/usermanagement" var="searchUserUrl" />
<%@ taglib prefix="templateLa" tagdir="/WEB-INF/tags/addons/jnjlaloginaddon/responsive/template"%>

<templateLa:page pageTitle="${pageTitle}">
    <div id="userManagement">
        <ul class="breadcrumb">
            <li>
                <breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}" />
            </li>
        </ul>
        <div class="row content">
            <div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 userManagementheading">
                <spring:message code="account.page.usermanagement" />
            </div>
            <div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
            <c:if test="${placeOrderResComUserGrpFlag eq false}">
                <a href="#"><button type="button" class="btn btnclsactive pull-right validateprice" id="createUserProfileUserMangement"><spring:message code="account.page.usermanagement.button.createuser" /></button></a>
            </c:if>
            </div>
        </div>
        <form:form id="searchUserForm" action="${searchUserUrl}" modelAttribute="searchUserForm" method="POST" autocomplete="false">
            <div class="mainbody-container">
                <div class="d-none d-sm-block">
                    <table class="table table-bordered table-striped lasorting-table" id="datatab-desktop">
                        <thead>
                            <tr>
                                <th class="text-left text-uppercase"><spring:message code="usermanagement.header.profileName" /></th>
                                <th class="no-sort text-left text-uppercase"><spring:message code="usermanagement.header.role" /></th>
                                <th class="text-left text-uppercase"><spring:message code="userSearch.header.email" /></th>
                                <th class="no-sort text-left text-uppercase"><spring:message code="usermanagement.header.status" /></th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${searchPageData.results}" var="user">
                               <c:choose>
                                      <c:when test="${(pharmaCommercialUserGroupFlag eq true)}">
                                       <!-- PHARMA Commercial group -->
                                          <tr>
                                              <td class="text-left">
                                                       ${user.firstName}&nbsp;${user.lastName}
                                              </td>
                                              <td class="text-left">
                                                  <c:forEach items="${user.roles}" var="role">
                                                      <div>
                                                          <c:if test="${role != 'b2bcustomergroup'}">
                                                              ${role}
                                                          </c:if>
                                                      </div>
                                                  </c:forEach>
                                              </td>
                                              <td>
                                                  ${user.email}
                                              </td>
                                              <td>
                                                  <span>
                                                      <c:set var="userStatus" value="${fn:replace(user.status,' ','.')}"/>
                                                      <spring:message code="usermanagement.userstatus.${fn:toUpperCase(userStatus)}" />
                                                  </span>
                                              </td>
                                          </tr>
                                          <!-- End PHARMA Commercial group -->
                                          </c:when>
                                          <c:when test="${(mddCommercialUserGroupFlag eq true)}">
                                          <!-- MDD Commercial group -->
                                              <tr>
                                                  <td class="text-left">
                                                           ${user.firstName}&nbsp;${user.lastName}
                                                  </td>
                                                  <td class="text-left">
                                                      <c:forEach items="${user.roles}" var="role">
                                                          <div>
                                                              <c:if test="${role != 'b2bcustomergroup'}">
                                                                  ${role}
                                                              </c:if>
                                                          </div>
                                                      </c:forEach>
                                                  </td>
                                                  <td>
                                                      ${user.email}
                                                  </td>
                                                  <td>
                                                      <span>
                                                          <c:set var="userStatus" value="${fn:replace(user.status,' ','.')}"/>
                                                          <spring:message code="usermanagement.userstatus.${fn:toUpperCase(userStatus)}" />
                                                      </span>
                                                  </td>
                                              </tr>
                                              <!-- End MDD Commercial group -->
                                      </c:when>
                                      <c:when test="${(pharmaCommercialUserGroupFlag eq false) && (mddCommercialUserGroupFlag eq false)}">
                                           <!--  Admin User  -->
                                                <tr>
                                                    <td class="text-left">
                                                            <spring:url value="/resources/usermanagement/edit" var="viewUserUrl">
                                                                <spring:param name="user" value="${user.uid}" />
                                                            </spring:url>
                                                            <span data-toggle="modal" data-target="#product-detail-popup">
                                                                <div>
                                                                    <c:forEach items="${user.roles}" var="role">
                                                                        <div>
                                                                            <c:if test="${role == 'jnjGTAdminGroup'}">
                                                                                <c:set var="adminrole" value="true"/>
                                                                            </c:if>
                                                                        </div>
                                                                    </c:forEach>
                                                                    <c:if test="${adminrole eq true}">${user.firstName}&nbsp;${user.lastName}</c:if>
                                                                    <c:if test="${adminrole ne true}"><a class="editUserProfileUserMangement" data="${viewUserUrl}" href="${viewUserUrl}">${user.firstName}&nbsp;${user.lastName}</a></c:if>
                                                                    <c:set var="adminrole" value="false"/>
                                                                </div>
                                                            </span>
                                                    </td>
                                                    <td class="text-left">
                                                        <c:forEach items="${user.roles}" var="role">
                                                            <div>
                                                                <c:if test="${role != 'b2bcustomergroup'}">
                                                                    ${role}
                                                                </c:if>
                                                            </div>
                                                        </c:forEach>
                                                    </td>
                                                    <td>
                                                       <a href="${viewUserUrl}">${user.email}</a>
                                                    </td>
                                                    <td>
                                                        <span>
                                                            <c:set var="userStatus" value="${fn:replace(user.status,' ','.')}"/>
                                                            <spring:message code="usermanagement.userstatus.${fn:toUpperCase(userStatus)}" />
                                                        </span>
                                                    </td>
                                                </tr>
                                                 <!-- End Admin User  -->
                                      </c:when>
                                      <c:otherwise>

                                      </c:otherwise>
                                   </c:choose>
                           </c:forEach>
                        </tbody>
                    </table>
                </div>
                <!-- Table collapse for mobile device-->
                <div class="d-block d-sm-none" style="overflow-x: scroll">
                    <table class="table table-bordered table-striped lasorting-table">
                        <thead>
                            <tr>
                                <th class="text-left"><spring:message code="usermanagement.header.profileName" /></th>
                                <th class="no-sort text-left"><spring:message code="usermanagement.header.role" /></th>
                                <th class="text-left text-uppercase"><spring:message code="userSearch.header.email" /></th>
                                <th class="no-sort text-left"><spring:message code="usermanagement.header.status" /></th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${searchPageData.results}" var="user">
                               <c:choose>
                                     <c:when test="${(pharmaCommercialUserGroupFlag eq true)}">
                                        <!-- PHARMA Commercial group -->
                                           <tr>
                                               <td class="text-left">
                                                        ${user.firstName}&nbsp;${user.lastName}
                                               </td>
                                               <td class="text-left">
                                                    <c:forEach items="${user.roles}" var="role">
                                                       <p>
                                                           <c:if test="${role != 'b2bcustomergroup'}">
                                                               <spring:message code="b2busergroup.${role}.name" />
                                                           </c:if>
                                                       </p>
                                                   </c:forEach>
                                                   &nbsp;
                                               </td>
                                               <td>${user.email}</td>
                                               <td>
                                                   <span>${user.status}</span>
                                               </td>
                                           </tr>
                                           <!-- End PHARMA Commercial group -->
                                           </c:when>
                                           <c:when test="${(mddCommercialUserGroupFlag eq true)}">
                                           <!-- MDD Commercial group -->
                                               <tr>
                                                   <td class="text-left">
                                                            ${user.firstName}&nbsp;${user.lastName}
                                                   </td>
                                                   <td class="text-left">
                                                       <c:forEach items="${user.roles}" var="role">
                                                           <p>
                                                               <c:if test="${role != 'b2bcustomergroup'}">
                                                                   <spring:message code="b2busergroup.${role}.name" />
                                                               </c:if>
                                                           </p>
                                                       </c:forEach>
                                                       &nbsp;
                                                   </td>
                                                   <td>
                                                       ${user.email}
                                                   </td>
                                                   <td><span>${user.status}</span></td>
                                               </tr>
                                               <!-- End MDD Commercial group -->
                                       </c:when>
                                       <c:when test="${(pharmaCommercialUserGroupFlag eq false) && (mddCommercialUserGroupFlag eq false)}">
                                          <!--  Admin User  -->
                                               <tr>
                                                   <td class="text-left">
                                                           <spring:url value="/resources/usermanagement/edit" var="viewUserUrl">
                                                              <spring:param name="user" value="${user.uid}" />
                                                          </spring:url>
                                                          <span data-toggle="modal" data-target="#product-detail-popup">
                                                              <p><a class="editUserProfileUserMangement1" data="${viewUserUrl}" href="${viewUserUrl}">${user.firstName}&nbsp;${user.lastName}</a></p>
                                                          </span>
                                                   </td>
                                                   <td class="text-left">
                                                       <c:forEach items="${user.roles}" var="role">
                                                           <p>
                                                               <c:if test="${role != 'b2bcustomergroup'}">
                                                                   <spring:message code="b2busergroup.${role}.name" />
                                                               </c:if>
                                                           </p>
                                                       </c:forEach>
                                                       &nbsp;
                                                   </td>
                                                   <td>
                                                      <a href="${viewUserUrl}">${user.email}</a>
                                                   </td>
                                                   <td><span>${user.status}</span></td>
                                               </tr>
                                                <!-- End Admin User  -->
                                       </c:when>
                                       <c:otherwise>

                                       </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </form:form>
    </div>
</templateLa:page>