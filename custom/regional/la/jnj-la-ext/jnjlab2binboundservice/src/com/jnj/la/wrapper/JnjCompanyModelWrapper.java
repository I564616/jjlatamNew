package com.jnj.la.wrapper;

import com.jnj.la.core.model.JnJCompanyModel;


/**
 * This wrapper contains a {@link JnJCompanyModel} with a sector by each country that must to be processed
 *
 * Created by aadrian2 on 02/02/2017.
 */
public class JnjCompanyModelWrapper
{

	private JnJCompanyModel jnjCompanyModel;
	private Boolean sectorEnabled;

	public JnjCompanyModelWrapper()
	{
		this.sectorEnabled = Boolean.FALSE;
	}

	public JnjCompanyModelWrapper(final JnJCompanyModel jnjCompanyModel)
	{
		this.jnjCompanyModel = jnjCompanyModel;
		this.sectorEnabled = Boolean.FALSE;
	}

	public JnJCompanyModel getJnjCompanyModel()
	{
		return jnjCompanyModel;
	}

	public void setJnjCompanyModel(final JnJCompanyModel jnjCompanyModel)
	{
		this.jnjCompanyModel = jnjCompanyModel;
	}

	public Boolean getSectorEnabled()
	{
		return sectorEnabled;
	}

	public void setSectorEnabled(final Boolean sectorEnabled)
	{
		this.sectorEnabled = sectorEnabled;
	}
}
