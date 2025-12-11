<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/loginaddon/responsive/template" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common" %>
<%@ taglib prefix="contactUs" tagdir="/WEB-INF/tags/responsive/contactus" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="breadcrumb" tagdir="/WEB-INF/tags/responsive/nav/breadcrumb" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="laCommon" tagdir="/WEB-INF/tags/addons/jnjlacartandcheckoutaddon/responsive/cart/common"%>
<%@ taglib prefix="templateLa" tagdir="/WEB-INF/tags/addons/jnjlaloginaddon/responsive/template"%>

<templateLa:page pageTitle="${pageTitle}">
    <div id="templateLanding">
        <c:url value="/templates" var="template" ></c:url>
        <form:form id="templates" action="${template}" method="POST" modelAttribute="JnjGTOrderTemplateForm" name="JnjGTOrderTemplateForm" >
		    <div class="row">
	    		<div class="col-lg-12 col-md-12">
				    <breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}" />
				    <div class="row content">
					    <div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
						    <spring:message code="template.page.labell" />
					    </div>
					    <div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
						    <button type="button" class="btn btnclsactive pull-right validateprice" id="createTemplate">
						        <spring:message code="template.page.create" />
						    </button>
					    </div>
                    </div>
			    </div>
		    </div>
		    <div class="marTop10 marBott10">
		        <c:if test="${deleteSuccessMsg  ne null}">
		 	        <laCommon:genericMessage messageCode="${deleteSuccessMsg}" icon="ok" panelClass="success" />
		        </c:if>
		        <c:if test="${successMsg  ne null}">
		 	        <laCommon:genericMessage messageCode="${successMsg}" icon="ok" panelClass="success" />
		        </c:if>
		    </div>
		    <div class="row jnj-panel mainbody-container" id="global-search-txt-holder">
		    <!-- Search  start here-->
                <div class="pull-left col-lg-3 col-sm-3 col-md-3 col-xs-12" style="padding-left: 10px">
					<b><label for="searchby">
					<spring:message	code="template.searchby.template.name" />:</label></b>
					<form:input path="searchby" type="hidden" id="searchby" var="searchSelectBox" value="SearchTemplateName"/>
					<c:if test="${empty searchText}">
						<c:set var="searchText" value="Search"></c:set>
					</c:if>
				</div>

			    <div id="" class="col-lg-3 col-sm-3 col-md-3 col-xs-12 searchArea">
				    <spring:message code="template.page.search" var ="searchplaceholder"/>
				    <form:input path="searchText" type="text" title="Enter Text" value="" placeholder="${searchplaceholder}"
				        class=" inputWidth marLeft form-control"/>
				        <i class="bi bi-search searchglyph "  onclick="$('#templates').submit();"></i>

			    </div>

			    <div class="pull-right rightContent col-lg-5 col-sm-5 col-md-5 col-xs-12">
			        <li class="right" style="display:none">
				        <span>
					        <span class="marginRight marTop7 textBlack">
					            <span class="labelText">
					                <spring:message code="template.total"/>:
					            </span>&nbsp;${totalTemplates}&nbsp;
					            <spring:message code="template.templates"/>
					        </span>
				        </span>
				        <span class="separator">
					        <label class="headercolumn3" for="showinGroups"><spring:message code="template.showingroupof"/>:</label>
					        <form:select path="showinGroups" id="showinGroups" class="showDdown">
						        <c:forEach items="${groupBy}" var="groupBySelectBox" >
							        <form:option value="${groupBySelectBox}">${groupBySelectBox}</form:option>
				  		        </c:forEach>
					        </form:select>
				        </span>
				    </li>

				    <strong><spring:message code="template.download"/></strong> &nbsp;
				    <a class="tertiarybtn marginRight excel downloadTemplateExcel" href="#">
				        <spring:message code="template.excel.button"/>
				    </a>
				    <span class="pipesymbol">|</span> &nbsp;
				    <a class="tertiarybtn pdf downloadTemplatePDF" href="#">
				        <spring:message code="template.pdf.button"/>
				    </a>
			    </div>
		    </div>

		    <div class="row">
	    		<div class="col-lg-12 col-md-12">
		    		<div class="d-none d-lg-block d-xl-block   jnj-panel-for-table mainbody-container">
			    		<table id="datatab-desktop" class="table table-bordered table-striped lasorting-table">
				    		<thead>
					    		<tr>
						    		<th class="no-sort"><spring:message	code="template.table.header01" /></th>
							    	<%-- <th class="no-sort"><spring:message code="template.table.header2" /></th> --%>
							    	<th class="no-sort"><spring:message code="template.table.header03" /></th>
							    	<th><spring:message code="template.table.header04" /></th>
								    <th class="no-sort"><spring:message	code="template.table.header05" /></th>
								    <th class="no-sort"><spring:message	code="template.table.header06" /></th>
	    						</tr>
		    				</thead>
				    		<tbody>
			    				<c:forEach items="${orderTemplate}" var="orderTemplate" varStatus="count">
						    		<tr>
									    <td>
									        <c:set	value="templates/templateDetail/${orderTemplate.code}"	var="templateDetailLink"></c:set>
									        <a href="${templateDetailLink}" id="temp-temporderdetail">${orderTemplate.templateName }</a>
									    </td>
									    <%-- <td>${orderTemplate.templateNumber }</td> --%>
									    <td>${orderTemplate.author }</td>
									    <td><fmt:formatDate value="${orderTemplate.createdOn}" pattern="MM/dd/yyyy" /></td>
									    <td><spring:message	code="template.table.status.${orderTemplate.shareStatus }" /></td>
									    <td>${orderTemplate.lines }</td>
								    </tr>
							    </c:forEach>
						    </tbody>
					    </table>
				    </div>
			    </div>
		    </div>

		    <!-- Table collapse for ipad device-->
			<div class="d-none d-md-none d-sm-block jnj-panel-for-table mainbody-container">
				<table id="datatab-tablet" class="table table-bordered table-striped sorting-table bordernone mobile-table">
					<thead>
						<tr>
							<th class="no-sort"><spring:message	code="template.table.header1" /></th>
							<th class="no-sort"><spring:message	code="template.table.header2" /></th>
							<th class="no-sort"><spring:message	code="template.table.header3" /></th>
							<th><spring:message code="template.table.header4" /></th>
							<th class="no-sort"><spring:message	code="template.table.header5" /></th>
							<th class="no-sort"><spring:message	code="template.table.header6" /></th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${orderTemplate}" var="orderTemplate"	varStatus="count">
							<tr>
								<td>
								    <c:set	value="templates/templateDetail/${orderTemplate.code}"	var="templateDetailLink"></c:set>
									<a	href="${templateDetailLink}" id="temp-temporderdetail">${orderTemplate.templateName }</a>
								</td>
								<td>${orderTemplate.templateNumber }</td>
								<td>${orderTemplate.author }</td>
								<td><fmt:formatDate value="${orderTemplate.createdOn}" pattern="MM/dd/yyyy" /></td>
								<td>${orderTemplate.shareStatus }</td>
								<td>${orderTemplate.lines }</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>

			<!--Accordian for ipad Ends here -->
			<!-- Table collapse for mobile device -->
			<div class="d-block d-sm-none  jnj-panel-for-table mainbody-container">
				<table id="datatab-mobile" class="table table-bordered table-striped sorting-table bordernone mobile-table">
					<thead>
						<tr>
							<th class="no-sort text-left product-img-mob-cell">Templates</th>
						</tr>
					</thead>
				    <tbody>
						<c:forEach items="${orderTemplate}" var="orderTemplate"	varStatus="count">
							<tr>
							    <td>
								    <a data-bs-toggle="collapse" data-bs-parent="#accordion" href="#collapse${count.count}" class="toggle-link panel-collapsed skyBlue">
										 <i class="bi bi-plus-lg"></i><span class="glyphicon glyphicon-plus skyBlue toggle-plus-minus"></span> ${orderTemplate.templateName }
								    </a>
								    <div id="collapse${count.count}" class="panel-collapse collapse">
										<div class="panel-body">
											<div class="sub-details-row">
												<div style="font-family:jnjlabelfont; font-size:10px">
												    <spring:message code="template.table.header1" />
												</div>
												<div>
												    <c:set value="templates/templateDetail/${orderTemplate.code}" var="templateDetailLink"></c:set>
								                    <a href="${templateDetailLink}" id="temp-temporderdetail">${orderTemplate.templateName}</a>
								                </div>
											</div>
						    				<div class="sub-details-row">
												<div style="font-family:jnjlabelfont; font-size:10px">
												    <spring:message code="template.table.header2" />
												</div>
												<div>${orderTemplate.templateNumber }</div>
											</div>
											<div class="sub-details-row">
												<div style="font-family:jnjlabelfont; font-size:10px">
												    <spring:message code="template.table.header3" />
												</div>
												<div>${orderTemplate.author }</div>
											</div>
											<div class="sub-details-row">
											    <div style="font-family:jnjlabelfont; font-size:10px">
											        <spring:message code="template.table.header4" />
											    </div>
											    <div><fmt:formatDate value="${orderTemplate.createdOn}" pattern="MM/dd/yyyy" /></div>
											</div>
										    <div class="sub-details-row">
												<div style="font-family:jnjlabelfont; font-size:10px">
												    <spring:message code="template.table.header5" />
												</div>
												<div>${orderTemplate.shareStatus }</div>
											</div>
											<div class="sub-details-row">
												<div style="font-family:jnjlabelfont; font-size:10px">
												    <spring:message code="template.table.header6" />
												</div>
											<div>${orderTemplate.lines }</div>
										    </div>
									    </div>
								    </div>
							    </td>
						    </tr>
					    </c:forEach>
				    </tbody>
			    </table>
		    </div>
            <!-- Accordian for mobile ends here -->
	    </form:form>
    </div>
</templateLa:page>