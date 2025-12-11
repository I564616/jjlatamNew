/**
 * Copyright: Copyright © 2019
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */
package com.jnj.core.event;

import de.hybris.platform.commerceservices.event.AbstractCommerceUserEvent;

public class JnjLAResetPasswordEvent extends AbstractCommerceUserEvent {
	
	private String businessEmail;
	private String resetPwdURL;
	private String logoURL;
	private String baseUrl;

	public String getBusinessEmail() {
		return businessEmail;
	}

	public void setBusinessEmail(final String businessEmail) {
		this.businessEmail = businessEmail;
	}

	public String getResetPwdURL() {
		return resetPwdURL;
	}

	public void setResetPwdURL(final String resetPwdURL) {
		this.resetPwdURL = resetPwdURL;
	}

	public String getLogoURL() {
		return logoURL;
	}

	public void setLogoURL(final String logoURL) {
		this.logoURL = logoURL;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(final String baseUrl) {
		this.baseUrl = baseUrl;
	}
	

}
