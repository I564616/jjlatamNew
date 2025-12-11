/**
 *
 */
package com.jnj.la.core.dto;

import java.util.ArrayList;
import java.util.List;


/**
 * The Class JnjContractDTO.
 */
public class JnjContractDTO
{

	/** The customer num. */
	private String customerNum;

	/** The e cc contract num. */
	private String eCCContractNum;

	/** The c rm contract num. */
	private String cRMContractNum;

	/** The tender num. */
	private String tenderNum;

	/** The start date. */
	private String startDate;

	/** The end date. */
	private String endDate;

	/** The contract order reason. */
	private String contractOrderReason;

	/** The document type. */
	private String documentType;

	/** The indirect customer. */
	private String indirectCustomer;

	/** The total amount. */
	private String totalAmount;

	/** The balance amount. */
	private String balanceAmount;

	/** The status. */
	private String status;

	/** The i doc no. */
	private String iDocNo;

	/** The file name. */
	private String fileName;

	/** The product list. */
	private List<JnjContractProductDTO> productList;

	/** The activation. */
	private Boolean activation;

	/** The last update date. */
	private String lastUpdatedDate;


	/**
	 * @return the lastUpdatedDate
	 */
	public String getLastUpdatedDate()
	{
		return lastUpdatedDate;
	}

	/**
	 * @param lastUpdatedDate
	 *           the lastUpdatedDate to set
	 */
	public void setLastUpdatedDate(final String lastUpdatedDate)
	{
		this.lastUpdatedDate = lastUpdatedDate;
	}

	/**
	 * Gets the customer num.
	 *
	 * @return the customerNum
	 */
	public String getCustomerNum()
	{
		return customerNum;
	}

	/**
	 * Sets the customer num.
	 *
	 * @param customerNum
	 *           the customerNum to set
	 */
	public void setCustomerNum(final String customerNum)
	{
		this.customerNum = customerNum;
	}

	/**
	 * Gets the e cc contract num.
	 *
	 * @return the eCCContractNum
	 */
	public String geteCCContractNum()
	{
		return eCCContractNum;
	}

	/**
	 * Sets the e cc contract num.
	 *
	 * @param eCCContractNum
	 *           the eCCContractNum to set
	 */
	public void seteCCContractNum(final String eCCContractNum)
	{
		this.eCCContractNum = eCCContractNum;
	}

	/**
	 * Gets the c rm contract num.
	 *
	 * @return the cRMContractNum
	 */
	public String getcRMContractNum()
	{
		return cRMContractNum;
	}

	/**
	 * Sets the c rm contract num.
	 *
	 * @param cRMContractNum
	 *           the cRMContractNum to set
	 */
	public void setcRMContractNum(final String cRMContractNum)
	{
		this.cRMContractNum = cRMContractNum;
	}

	/**
	 * Gets the tender num.
	 *
	 * @return the tenderNum
	 */
	public String getTenderNum()
	{
		return tenderNum;
	}

	/**
	 * Sets the tender num.
	 *
	 * @param tenderNum
	 *           the tenderNum to set
	 */
	public void setTenderNum(final String tenderNum)
	{
		this.tenderNum = tenderNum;
	}

	/**
	 * Gets the start date.
	 *
	 * @return the startDate
	 */
	public String getStartDate()
	{
		return startDate;
	}

	/**
	 * Sets the start date.
	 *
	 * @param startDate
	 *           the startDate to set
	 */
	public void setStartDate(final String startDate)
	{
		this.startDate = startDate;
	}

	/**
	 * Gets the end date.
	 *
	 * @return the endDate
	 */
	public String getEndDate()
	{
		return endDate;
	}

	/**
	 * Sets the end date.
	 *
	 * @param endDate
	 *           the endDate to set
	 */
	public void setEndDate(final String endDate)
	{
		this.endDate = endDate;
	}

	/**
	 * Gets the contract order reason.
	 *
	 * @return the contractOrderReason
	 */
	public String getContractOrderReason()
	{
		return contractOrderReason;
	}

	/**
	 * Sets the contract order reason.
	 *
	 * @param contractOrderReason
	 *           the contractOrderReason to set
	 */
	public void setContractOrderReason(final String contractOrderReason)
	{
		this.contractOrderReason = contractOrderReason;
	}

	/**
	 * Gets the document type.
	 *
	 * @return the documentType
	 */
	public String getDocumentType()
	{
		return documentType;
	}

	/**
	 * Sets the document type.
	 *
	 * @param documentType
	 *           the documentType to set
	 */
	public void setDocumentType(final String documentType)
	{
		this.documentType = documentType;
	}

	/**
	 * Gets the indirect customer.
	 *
	 * @return the indirectCustomer
	 */
	public String getIndirectCustomer()
	{
		return indirectCustomer;
	}

	/**
	 * Sets the indirect customer.
	 *
	 * @param indirectCustomer
	 *           the indirectCustomer to set
	 */
	public void setIndirectCustomer(final String indirectCustomer)
	{
		this.indirectCustomer = indirectCustomer;
	}

	/**
	 * Gets the total amount.
	 *
	 * @return the totalAmount
	 */
	public String getTotalAmount()
	{
		return totalAmount;
	}

	/**
	 * Sets the total amount.
	 *
	 * @param totalAmount
	 *           the totalAmount to set
	 */
	public void setTotalAmount(final String totalAmount)
	{
		this.totalAmount = totalAmount;
	}

	/**
	 * Gets the balance amount.
	 *
	 * @return the balanceAmount
	 */
	public String getBalanceAmount()
	{
		return balanceAmount;
	}

	/**
	 * Sets the balance amount.
	 *
	 * @param balanceAmount
	 *           the balanceAmount to set
	 */
	public void setBalanceAmount(final String balanceAmount)
	{
		this.balanceAmount = balanceAmount;
	}

	/**
	 * Gets the product list.
	 *
	 * @return the productList
	 */
	public List<JnjContractProductDTO> getProductList()
	{
		return new ArrayList<>(productList);
	}

	/**
	 * Sets the product list.
	 *
	 * @param productList
	 *           the productList to set
	 */
	public void setProductList(final List<JnjContractProductDTO> productList)
	{
		this.productList = new ArrayList<>(productList);
	}

	/**
	 * Gets the status.
	 *
	 * @return the statusHeader
	 */
	public String getStatus()
	{
		return status;
	}

	/**
	 * Sets the status.
	 *
	 * @param status
	 *           the statusHeader to set
	 */
	public void setStatus(final String status)
	{
		this.status = status;
	}

	/**
	 * Gets the activation.
	 *
	 * @return the activation
	 */
	public Boolean getActivation()
	{
		return activation;
	}

	/**
	 * Sets the activation.
	 *
	 * @param activation
	 *           the activation to set
	 */
	public void setActivation(final Boolean activation)
	{
		this.activation = activation;
	}

	/**
	 * Gets the i doc no.
	 *
	 * @return the iDocNo
	 */
	public String getiDocNo()
	{
		return iDocNo;
	}

	/**
	 * Sets the i doc no.
	 *
	 * @param iDocNo
	 *           the iDocNo to set
	 */
	public void setiDocNo(final String iDocNo)
	{
		this.iDocNo = iDocNo;
	}

	/**
	 * Gets the file name.
	 *
	 * @return the fileName
	 */
	public String getFileName()
	{
		return fileName;
	}

	/**
	 * Sets the file name.
	 *
	 * @param fileName
	 *           the fileName to set
	 */
	public void setFileName(final String fileName)
	{
		this.fileName = fileName;
	}


}
