/**
 *
 */
package com.jnj.core.event;

import de.hybris.platform.commerceservices.event.AbstractCommerceUserEvent;

import java.util.Map;

import com.jnj.core.model.JnJB2BUnitModel;


/**
 * This class represents the event object for the email flow to export catalog.
 *
 * @author Accenture
 * @version 1.0
 */
public class JnjGTExportCatalogEvent extends AbstractCommerceUserEvent
{
	protected Map<String, String> exportCatalogData;
	protected JnJB2BUnitModel currentB2bUnit;
	protected String currentSite;

	public String getCurrentSite()
	{
		return currentSite;
	}

	public void setCurrentSite(final String currentSite)
	{
		this.currentSite = currentSite;
	}

	public JnjGTExportCatalogEvent(final Map<String, String> exportCatalogData, final JnJB2BUnitModel currentB2bUnit,
			final String currentSite)
	{
		super();
		this.exportCatalogData = exportCatalogData;
		this.currentB2bUnit = currentB2bUnit;
		this.currentSite = currentSite;
	}

	/**
	 * @return the exportCatalogData
	 */
	public Map<String, String> getExportCatalogData()
	{
		return exportCatalogData;
	}

	/**
	 * @param exportCatalogData
	 *           the exportCatalogData to set
	 */
	public void setExportCatalogData(final Map<String, String> exportCatalogData)
	{
		this.exportCatalogData = exportCatalogData;

	}

	public JnJB2BUnitModel getCurrentB2bUnit()
	{
		return currentB2bUnit;
	}

	public void setCurrentB2bUnit(final JnJB2BUnitModel currentB2bUnit)
	{
		this.currentB2bUnit = currentB2bUnit;
	}

}
