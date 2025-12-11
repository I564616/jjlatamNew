<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

	<div id="changeDropShipAccount">
		<div class="lightboxtemplate" id="dropShipAccount">
			<h2>Drop-Ship Account</h2>
			<div class="drpship ttBlock">
				<c:url value="/cart/updateDropShipAccount" var="updateDropShipAdd" />
				<form id="changeDropShipAddForm" method="post"
					action="${updateDropShipAdd}">
					<table>
						<thead>
							<tr class="sectionBlock">								
									
										<th class="ttHead">
											<div class="hcolumn1">Select</div>
										</th>
										<th class="ttHead">
											<div class="hcolumn2">Account</div>
										</th>
										<th class="ttHead">
											<div class="hcolumn3">Address</div>
										</th>								
									
							</tr>
						</thead>
					<!--  	<div class="ttBody">
							<div class="ttRow">-->
							
								<c:forEach var="dropShipAccount" items="${dropShipAccounts}">
									<tr>
										<td>
											<div class="column1">
												<input type="radio" name="dropShipAccount" id="dropShipAccount"
													class="dropShipAccount" value="${dropShipAccount.jnjAddressId}" />
											</div>
										</td>
										<td>
											<div class="column2">
												<label for="drpType1">${dropShipAccount.jnjAddressId}</label>
											</div>
										</td>
										<td>
											<div class="column3">
												<span class="labelText">${dropShipAccount.formattedAddress}</span>
											</div>
										</td>
									</tr>
								</c:forEach>
							<!-- </div>
						</div> -->
						

					</table>
					<div class='popupButtonWrapper txtRight'>
						<span class="floatLeft"><input type="button"
							onclick='parent.$.colorbox.close(); return false;'
							class="tertiarybtn" value="Cancel"/></span><span><input
							id="submitChangeDropShipAdd" type="button" class='secondarybtn'
							value="OK" /></span>
					</div>
				</form>
			</div>
		</div>
	</div>
