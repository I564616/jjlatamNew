/**
 *
 */
package com.jnj.la.core.dto;

import org.apache.commons.lang3.StringUtils;
import com.jnj.core.dto.JnjGTSplitOrderInfo;

/**
 * @author mpanda3
 *
 */
public class JnjLatamSplitOrderInfo extends JnjGTSplitOrderInfo
{
	private String shipTo = StringUtils.EMPTY;
	private String soldTo = StringUtils.EMPTY;
	private String principal = StringUtils.EMPTY;
	private String docOrderType = StringUtils.EMPTY;
	private String salesOrganization = StringUtils.EMPTY;
	private String forbiddenFlag = StringUtils.EMPTY;
	private String sector = StringUtils.EMPTY;
	private String noProductError = StringUtils.EMPTY;
	private String country = StringUtils.EMPTY;
	private String destCountry = StringUtils.EMPTY;
	private String shipper = StringUtils.EMPTY;

	public String getShipper()
	{
		return shipper;
	}


	public void setShipper(final String shipper)
	{
		this.shipper = shipper;
	}

	/**
	 * @return the shipTo
	 */
	@Override
	public String getShipTo()
	{
		return shipTo;
	}

	/**
	 * @param shipTo
	 *           the shipTo to set
	 */
	@Override
	public void setShipTo(final String shipTo)
	{
		this.shipTo = shipTo;
	}

	/**
	 * @return the soldTo
	 */
	@Override
	public String getSoldTo()
	{
		return soldTo;
	}

	/**
	 * @param soldTo
	 *           the soldTo to set
	 */
	@Override
	public void setSoldTo(final String soldTo)
	{
		this.soldTo = soldTo;
	}

	/**
	 * @return the principal
	 */
	@Override
	public String getPrincipal()
	{
		return principal;
	}

	/**
	 * @param principal
	 *           the principal to set
	 */
	@Override
	public void setPrincipal(final String principal)
	{
		this.principal = principal;
	}

	/**
	 * @return the docOrderType
	 */

	public String getDocOrderType()
	{
		return docOrderType;
	}

	/**
	 * @param docOrderType
	 *           the docOrderType to set
	 */

	public void setDocOrderType(final String docOrderType)
	{
		this.docOrderType = docOrderType;
	}

	/**
	 * @return the salesOrganization
	 */

	public String getSalesOrganization()
	{
		return salesOrganization;
	}

	/**
	 * @param salesOrganization
	 *           the salesOrganization to set
	 */

	public void setSalesOrganization(final String salesOrganization)
	{
		this.salesOrganization = salesOrganization;
	}

	/**
	 * @return the forbiddenFlag
	 */
	@Override
	public String getForbiddenFlag()
	{
		return forbiddenFlag;
	}

	/**
	 * @param forbiddenFlag
	 *           the forbiddenFlag to set
	 */
	@Override
	public void setForbiddenFlag(final String forbiddenFlag)
	{
		this.forbiddenFlag = forbiddenFlag;
	}

	/**
	 * @return the sector
	 */
	@Override
	public String getSector()
	{
		return sector;
	}

	/**
	 * @param sector
	 *           the sector to set
	 */
	@Override
	public void setSector(final String sector)
	{
		this.sector = sector;
	}

	/**
	 * @return the noProductError
	 */
	@Override
	public String getNoProductError()
	{
		return noProductError;
	}

	/**
	 * @param noProductError
	 *           the noProductError to set
	 */
	@Override
	public void setNoProductError(final String noProductError)
	{
		this.noProductError = noProductError;
	}

	@Override
	public boolean equals(final Object object)
	{

		boolean result = false;

		if (object != null && object.getClass() == getClass())
		{

			final JnjLatamSplitOrderInfo splitOrderInfo = (JnjLatamSplitOrderInfo) object;

			if (splitOrderInfo != null
                    && StringUtils.equals(this.principal,splitOrderInfo.getPrincipal())
					&& StringUtils.equals(this.docOrderType,splitOrderInfo.getDocOrderType())
					&& StringUtils.equals(this.forbiddenFlag,splitOrderInfo.getForbiddenFlag())
					&& StringUtils.equals(this.destCountry,splitOrderInfo.getDestCountry())
					&& StringUtils.equals(this.sector,splitOrderInfo.getSector())
					&& StringUtils.equals(this.noProductError,splitOrderInfo.getNoProductError())
					&& StringUtils.equals(this.shipper,splitOrderInfo.getShipper()))
			{
				result = true;
			}
		}
		return result;
	}


	public String getCountry()
	{
		return country;
	}


	public void setCountry(final String country)
	{
		this.country = country;
	}

	/**
	 * @return the destCountry
	 */
	public String getDestCountry()
	{
		return destCountry;
	}

	/**
	 * @param destCountry
	 *           the destCountry to set
	 */
	public void setDestCountry(final String destCountry)
	{
		this.destCountry = destCountry;
	}

	/**
	 * The hashCode method is over ridden with shipper, principal and order type of SplitOrderInfo class.
	 */
	@Override
	public int hashCode()
	{
		int hash = 1;

		if (this.principal != null && this.principal.length() > 0)
		{
			hash = hash * this.principal.hashCode();
		}
		if (this.docOrderType != null && this.docOrderType.length() > 0)
		{
			hash = hash * this.docOrderType.hashCode();
		}
		if (this.destCountry != null && this.destCountry.length() > 0)
		{
			hash = hash * this.destCountry.hashCode();
		}
		if (this.shipper != null && this.shipper.length() > 0)
		{
			hash = hash * this.shipper.hashCode();
		}

		return hash;
	}
}
