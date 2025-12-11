<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ attribute name="step1" required="false" %>
<%@ attribute name="step2" required="false" %>
<%@ attribute name="step3" required="false" %>
<%@ attribute name="step4" required="false" %>
<%@ attribute name="stepbar" required="true" %>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<div class="progressbarNav sectionBlock">
        
        	<ul class="stepbar-${stepbar}">
            	<li class="step1 ${step1}"><strong>1</strong><span class="spanSkin"><span><spring:message code="cart.review.progressBar.cart"/></span><spring:message code="cart.review.progressBar.buildOrder"/></span></li>
                <li class="step2 ${step2}"><strong>2</strong><span class="spanSkin"><span><spring:message code="cart.review.progressBar.validate"/></span><spring:message code="cart.review.progressBar.pricingAvlbility"/></span></li>
                <li class="step3 ${step3}"><strong>3</strong><span class="spanSkin"><span><spring:message code="cart.review.progressBar.checkOut"/></span><spring:message code="cart.review.progressBar.finalizeOrder"/></span></li> 
                <li class="step4 ${step4}"><strong>4</strong> <span class="spanSkin"><span><spring:message code="cart.review.progressBar.order"/></span><spring:message code="cart.review.progressBar.confirmation"/></span></li>                               
            </ul>	
        </div>