<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<div class='lightboxtemplate lightboxwidthsize1 sam' id='sellout_popup'>
	<h2>
		<spring:message code="text.sellout.upload.header" />
	</h2>
	<div class='lightboxbody'>
		<div class='section1report marTop20'>
			<label for='companyname' style="margin-right:45px;"><spring:message code="text.sellout.popup.company" /></label> 
			<select id='companyId' class='first'>
				<option value="medical">
					<spring:message code="text.sellout.medical" />
				</option >
				<option value="pharma">
					<spring:message code="text.sellout.pharma" />
				</option>
			</select>
			<div class='filename' style='display: none;width:340px;'></div>
			<div id="errorMessage" class="error loginBtnTopMargin" style="display:none;"><p><spring:message code="text.sellout.upload.error"/></p></div>
			<form:form id="uploadForm" enctype="multipart/form-data" method="POST" modelAttribute="uploadForm" action="sellout">
				<div id="FileUpload">
					<span id="fileInfo" class="filename" style="width: 340px;margin-bottom:6px;display:none;"></span>
					<div id="fileUploadButtonContainer">
						<div id="firstblock" class="fileinputs">
							<input id="firstFile" type="file" class="file" name="file" />
							<div class="fakefile">
								<input class="secondarybtn" id="firstFakeButton" type="button" value='<spring:message code="text.sellout.browse.file"/>' />
							</div>
						</div>
						<input id="hddnCompany" type="hidden" name="company" value="Medical" />
						<table id="secondblock" cellpadding="0" cellspacing="0" style="display:none;">
							<tr>
								<td width="70%">
									<div id="second" class="fileinputs">
										<input id="secondFile" type="file" class="file" name="file" onclick="showFileInfo();" />
										<div class="fakefile">
											<input class="secondarybtn" id="secondFakeButton" type="button" value='<spring:message code="text.sellout.browse.file"/>'    />
										</div>
									</div>
								</td>
								<td>
									<input type="button" class="primarybtn submitUploadButton" value="<spring:message code='text.sellout.upload.file'/>" id="submitbutton1" onclick="return restrictFilesOnlyForSellOut('.txt','.xls','.xlsx');"/>
								</td>
							</tr>
						</table>
					</div>
				</div>
			</form:form>
		</div>
	</div>
</div>