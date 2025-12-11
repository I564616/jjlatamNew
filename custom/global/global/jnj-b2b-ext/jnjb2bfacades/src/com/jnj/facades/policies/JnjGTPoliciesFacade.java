package com.jnj.facades.policies;

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;

public interface JnjGTPoliciesFacade {

	public String getCookiePolicies() throws CMSItemNotFoundException;

	public String getTermsAndConditionsPolicies() throws CMSItemNotFoundException;

	public String getPrivacyPolicies() throws CMSItemNotFoundException;

	public String getLegalNotices() throws CMSItemNotFoundException;

	public String getUsefulLinks() throws CMSItemNotFoundException;

}
