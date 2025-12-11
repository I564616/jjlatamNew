<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="messageLabel" uri="/WEB-INF/tld/message.tld"%>
<%@ attribute name="qaId" required="true" type="java.lang.String"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>

<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" id="accordion" style="padding-left: 0px; padding-right: 0px">
    <div class="help-accordian panel">
        <div class="help-accordian-header">
            <a data-bs-toggle="collapse" data-bs-parent="#accordion" href="#collapse${qaId}" class="ref_no toggle-link-la panel-collapsed">
                <span class="bi bi-plus help-accordian-icon"></span>
                <messageLabel:message messageCode="help.faq.question.${qaId}" />
            </a>
        </div>
        <div class="help-accordian-body panel-collapse collapse" id="collapse${qaId}">
            <messageLabel:message messageCode="help.faq.answer.${qaId}" />
            <cms:component uid="userGuideTranslationLink" />
        </div>
    </div>
</div>