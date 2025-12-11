package com.jnj.gt.mapper.product;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.variants.model.VariantProductModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import jakarta.annotation.Resource;

import jakarta.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.enums.JnjGTSourceSysId;
import com.jnj.core.enums.RecordStatus;
import com.jnj.core.model.JnJProductModel;
import com.jnj.core.model.JnjGTCpscContactModel;
import com.jnj.core.model.JnjGTCpscTestDetailModel;
import com.jnj.core.model.JnjGTProductCpscDetailModel;
import com.jnj.core.model.JnjGTVariantProductModel;
import com.jnj.exceptions.BusinessException;
import com.jnj.gt.constants.Jnjgtb2binboundserviceConstants;
import com.jnj.gt.constants.Jnjgtb2binboundserviceConstants.Logging;
import com.jnj.core.services.JnJGTProductService;
import com.jnj.gt.mapper.JnjAbstractMapper;
import com.jnj.gt.model.JnjGTIntCpscContactDetailModel;
import com.jnj.gt.model.JnjGTIntCpscDetailsModel;
import com.jnj.gt.model.JnjGTIntCpscTestDetailModel;
import com.jnj.gt.service.common.JnjGTFeedService;
import com.jnj.gt.service.product.JnjGTCpscFeedService;
import com.jnj.gt.service.product.JnjGTProductFeedService;
import com.jnj.gt.util.JnjGTInboundUtil;



/**
 * The Mapper class responsible to process the CPSC details using <code>JnjGTIntCpscDetailsModel</code> in
 * <code>JnJProductModel</code>.
 * 
 */

public class JnjGTProductCPSIAMapper extends JnjAbstractMapper
{
	/**
	 * The Instance of <code>Logger</code>.
	 */
	Logger LOGGER = Logger.getLogger(JnjGTProductCPSIAMapper.class);

	/**
	 * The Instance of <code>jnjGTCpscFeedService</code>.
	 */
	@Autowired
	private JnjGTCpscFeedService jnjGTCpscFeedService;

	/**
	 * The Instance of <code>jnjGTProductFeedService</code>.
	 */
	@Autowired
	private JnjGTProductFeedService jnjGTProductFeedService;

	/**
	 * The Instance of <code>jnjGTFeedService</code>.
	 */
	@Autowired
	private JnjGTFeedService jnjGTFeedService;


	@Autowired
	private CatalogVersionService catalogVersionService;

	/**
	 * The Instance of <code>JnJGTProductService</code>.
	 */
	@Resource(name = "productService")
	JnJGTProductService productService;

	private static final String CS = "CS";

	@Override
	public void processIntermediateRecords()
	{
		final List<JnjGTIntCpscDetailsModel> pendingIntRecords = (List<JnjGTIntCpscDetailsModel>) jnjGTFeedService
				.getRecordsByStatus(JnjGTIntCpscDetailsModel._TYPECODE, RecordStatus.PENDING);

		for (final JnjGTIntCpscDetailsModel jnjIntCpscDetailsModel : pendingIntRecords)
		{
			try
			{
				processIntermediateModel(jnjIntCpscDetailsModel);
				LOGGER.info("CPSC Intermediate record with PRODUCT SKU CODE: " + jnjIntCpscDetailsModel.getProductSkuCode()
						+ " , Lot Number:" + jnjIntCpscDetailsModel.getLotNumber() + " and Delete Indicator:" + Logging.HYPHEN
						+ jnjIntCpscDetailsModel.getDeleteIndicatorCPSIA() + ", was successfully processed. ");
				getjnjGTFeedService().updateIntermediateRecord(jnjIntCpscDetailsModel, RecordStatus.SUCCESS, false, null);
			}
			catch (final BusinessException businessException)
			{
				LOGGER.error(
						"CPSC Intermediate record with PRODUCT SKU CODE: " + Logging.HYPHEN
								+ jnjIntCpscDetailsModel.getProductSkuCode() + " and Lot Number:" + Logging.HYPHEN
								+ jnjIntCpscDetailsModel.getLotNumber() + ", could NOT be processed. Error: " + Logging.HYPHEN
								+ businessException.toString(), businessException);
				getjnjGTFeedService().updateIntermediateRecord(jnjIntCpscDetailsModel, null, true, businessException.getMessage(),
						Logging.CPSIA, jnjIntCpscDetailsModel.getProductSkuCode());
			}
			catch (final Exception exception)
			{
				LOGGER.error(
						"CPSC Intermediate record with PRODUCT SKU CODE: " + Logging.HYPHEN
								+ jnjIntCpscDetailsModel.getProductSkuCode() + " and Lot Number:" + Logging.HYPHEN
								+ jnjIntCpscDetailsModel.getLotNumber() + ", could NOT be processed. Error: " + Logging.HYPHEN
								+ exception.toString(), exception);
				getjnjGTFeedService().updateIntermediateRecord(jnjIntCpscDetailsModel, null, true, exception.getMessage(),
						Logging.CPSIA, jnjIntCpscDetailsModel.getProductSkuCode());
			}
		}
	}

	/**
	 * Gets the jnj na feed service.
	 * 
	 * @return the jnjGTFeedService
	 */
	public JnjGTFeedService getjnjGTFeedService()
	{
		return jnjGTFeedService;
	}

	/**
	 * Process intermediate <code>JnjGTIntCpscDetailsModel</code>.
	 * 
	 * @param jnjIntCpscDetailsModel
	 *           the jnj int cpsc details model
	 * @throws BusinessException
	 *            the business exception
	 */

	public void processIntermediateModel(final JnjGTIntCpscDetailsModel jnjIntCpscDetailsModel) throws BusinessException
	{
		final JnJProductModel JnJProductModel = jnjGTProductFeedService.getProductByCode(
				jnjIntCpscDetailsModel.getProductSkuCode(), getStgCatalogVersionByProduct(jnjIntCpscDetailsModel.getSrcSystem()));

		if (JnJProductModel == null)
		{
			final String errorMessage = "Could NOT find a Product correponding to the PRODUCT SKU CODE: "
					+ jnjIntCpscDetailsModel.getProductSkuCode() + " and LOT NUMBER: " + jnjIntCpscDetailsModel.getLotNumber();
			throw new BusinessException(errorMessage);
		}

		/* Get Existing JnjGTProductCpscDetailModel model associated with the Product */
		final Set<JnjGTProductCpscDetailModel> cpscDetails = new HashSet<JnjGTProductCpscDetailModel>(
				JnJProductModel.getCpsiDetails());

		if (CollectionUtils.isEmpty(cpscDetails))
		{
			if (!jnjIntCpscDetailsModel.getDeleteIndicatorCPSIA().booleanValue())
			{
				final JnjGTProductCpscDetailModel jnjGTProductCpscDetailModel = (JnjGTProductCpscDetailModel) getjnjGTCpscFeedService()
						.createNewItem(JnjGTProductCpscDetailModel.class);
				cpscDetails.add(setCPSCDetails(jnjIntCpscDetailsModel, jnjGTProductCpscDetailModel, JnJProductModel));
			}
			else
			{
				final String errorMessage = "The Delete Indicator for this record was TRUE";
				throw new BusinessException(errorMessage);
			}
		}
		else
		{
			boolean found = false;
			for (final JnjGTProductCpscDetailModel jnjGTProductCpscDetailModel : cpscDetails)
			{
				String jnjGTIntCpscUid = jnjIntCpscDetailsModel.getProductSkuCode();
				if (null != jnjIntCpscDetailsModel.getLotNumber())
				{
					jnjGTIntCpscUid = jnjGTIntCpscUid + Jnjb2bCoreConstants.SYMBOl_PIPE + jnjIntCpscDetailsModel.getLotNumber();
				}
				if (StringUtils.equals(jnjGTProductCpscDetailModel.getUid(), jnjGTIntCpscUid))
				{
					found = true;

					if (setCPSCDetails(jnjIntCpscDetailsModel, jnjGTProductCpscDetailModel, JnJProductModel) == null)
					{
						cpscDetails.remove(jnjGTProductCpscDetailModel);
						break;
					}

				}
			}
			if (!found)
			{
				if (!jnjIntCpscDetailsModel.getDeleteIndicatorCPSIA().booleanValue())
				{
					final JnjGTProductCpscDetailModel jnjGTProductCpscDetailModel = (JnjGTProductCpscDetailModel) getjnjGTCpscFeedService()
							.createNewItem(JnjGTProductCpscDetailModel.class);
					cpscDetails.add(setCPSCDetails(jnjIntCpscDetailsModel, jnjGTProductCpscDetailModel, JnJProductModel));
				}
				else
				{
					final String errorMessage = "The Delete Indicator for this record was TRUE";
					throw new BusinessException(errorMessage);
				}
			}
		}

		JnJProductModel.setCpsiDetails(cpscDetails);

		/* Saving JnJProductModel */
		getjnjGTCpscFeedService().saveItem(JnJProductModel);
	}

	public JnjGTProductCpscDetailModel setCPSCDetails(final JnjGTIntCpscDetailsModel jnjIntCpscDetailsModel,
			final JnjGTProductCpscDetailModel jnjGTProductCpscDetailModel, final JnJProductModel product) throws BusinessException
	{
		String intCpscDetailUid = null;
		if (!jnjIntCpscDetailsModel.getDeleteIndicatorCPSIA().booleanValue())
		{
			intCpscDetailUid = jnjIntCpscDetailsModel.getProductSkuCode();
			if (null != jnjIntCpscDetailsModel.getLotNumber())
			{
				intCpscDetailUid = intCpscDetailUid + Jnjb2bCoreConstants.SYMBOl_PIPE + jnjIntCpscDetailsModel.getLotNumber(); //ProductSkuCode as Unique ID for jnjGTProductCpscDetailModel
			}

			jnjGTProductCpscDetailModel.setUid(intCpscDetailUid); //ProductSkuCode as Unique ID for jnjGTProductCpscDetailModel
			jnjGTProductCpscDetailModel.setProductCode(jnjIntCpscDetailsModel.getProductSkuCode());

			/** Fetching the product variants **/
			final Collection<VariantProductModel> variants = product.getVariants();

			/** Iterating over the variants **/
			for (final VariantProductModel variantProduct : variants)
			{
				/** Checking if the unit matches "CS" and the variant is an instance of JnjGTVariantProductModel **/
				if (CS.equalsIgnoreCase(variantProduct.getUnit().getCode()) && variantProduct instanceof JnjGTVariantProductModel)
				{
					/** Setting the outer-case value **/
					jnjGTProductCpscDetailModel.setOutercase(((JnjGTVariantProductModel) variantProduct).getOuterCaseCode());
					break;
				}
			}

			jnjGTProductCpscDetailModel.setUpcCode(jnjIntCpscDetailsModel.getUpcCode());
			jnjGTProductCpscDetailModel.setCpscRuleDescription(jnjIntCpscDetailsModel.getCpscRuleDescription());
			jnjGTProductCpscDetailModel.setMfgCompanyCode(jnjIntCpscDetailsModel.getMfgCompanyCode());
			jnjGTProductCpscDetailModel.setCpsiComments(jnjIntCpscDetailsModel.getCpsiComments());
			jnjGTProductCpscDetailModel.setCertificateCreateDate(jnjIntCpscDetailsModel.getCertificateCreateDate());
			jnjGTProductCpscDetailModel.setCertificateCreater(jnjIntCpscDetailsModel.getCertificateCreater());
			jnjGTProductCpscDetailModel.setCertificateModifiedDate(jnjIntCpscDetailsModel.getCertificateModifiedDate());
			jnjGTProductCpscDetailModel.setModifiedBy(jnjIntCpscDetailsModel.getModifiedBy());
			jnjGTProductCpscDetailModel.setAddressLotNumber(jnjIntCpscDetailsModel.getAddressLotNumber());
			jnjGTProductCpscDetailModel.setMfdDate(jnjIntCpscDetailsModel.getMfdDate());
			jnjGTProductCpscDetailModel.setLotNumber(jnjIntCpscDetailsModel.getLotNumber());
			jnjGTProductCpscDetailModel.setModifiedDate(jnjIntCpscDetailsModel.getModifiedDate());
			jnjGTProductCpscDetailModel.setDeleted(jnjIntCpscDetailsModel.getDeleteIndicatorCPSIA());
		}

		final Set<JnjGTCpscContactModel> cpscContactModels = getCpscContactDetails(
				jnjGTProductCpscDetailModel.getCpscContactDetails(), jnjIntCpscDetailsModel.getProductSkuCode(),
				jnjIntCpscDetailsModel.getLotNumber());

		final Set<JnjGTCpscTestDetailModel> cpscTestModels = getCpscStudyDetails(jnjGTProductCpscDetailModel.getCpscTestDetails(),
				jnjIntCpscDetailsModel.getProductSkuCode(), jnjIntCpscDetailsModel.getLotNumber());

		/* Remove Parent JnjGTProductCpscDetailModel when child are empty and Delete Indicator is TRUE */
		if (cpscContactModels.isEmpty() && cpscTestModels.isEmpty()
				&& jnjIntCpscDetailsModel.getDeleteIndicatorCPSIA().booleanValue())
		{
			/* Remove JnjGTProductCpscDetailModel */
			jnjGTCpscFeedService.removeItem(jnjGTProductCpscDetailModel);
			return null;
		}
		else
		{
			/* Setting values for CpscContactDetails & CpscTestDetails in CpscDetailModel And Saving it */
			jnjGTProductCpscDetailModel.setCpscContactDetails(cpscContactModels);
			jnjGTProductCpscDetailModel.setCpscTestDetails(cpscTestModels);
			getjnjGTCpscFeedService().saveItem(jnjGTProductCpscDetailModel);
			return jnjGTProductCpscDetailModel;
		}
	}


	/**
	 * Process <code>JnjGTProductCpscDetailModel </code>.
	 * 
	 * @param existingCpscContactModels
	 *           the existing cpsc contact models
	 * @param productSkuCode
	 *           the product sku code
	 * @return the cpsc contact details
	 * @throws BusinessException
	 *            the business exception
	 */
	public Set<JnjGTCpscContactModel> getCpscContactDetails(final Set<JnjGTCpscContactModel> existingCpscContactModels,
			final String productSkuCode, final String lotNumber) throws BusinessException
	{
		final Set<JnjGTCpscContactModel> finalCpscContactModels = new HashSet<JnjGTCpscContactModel>();

		/* Adding Updated JnjGTCpscContactModels to the Final Set */
		if (null != existingCpscContactModels)
		{
			finalCpscContactModels.addAll(existingCpscContactModels);
		}

		/* Fetching JnjGTIntCpscContactDetailModel from Hybris for given productSkuCode */
		final List<JnjGTIntCpscContactDetailModel> jnjIntCpscContactdetails = new ArrayList<>();
		jnjIntCpscContactdetails.addAll(getjnjGTCpscFeedService().getCpscContactDetailByProductCodeAndLotNumber(productSkuCode,
				lotNumber));

		for (final JnjGTIntCpscContactDetailModel jnjGTIntCpscContactDetailModel : jnjIntCpscContactdetails)
		{
			JnjGTCpscContactModel jnjGTCpscContactModel = null;

			String intCpscContactDetailsUid = null;
			intCpscContactDetailsUid = jnjGTIntCpscContactDetailModel.getProductSkuCode() + Jnjb2bCoreConstants.SYMBOl_PIPE
					+ jnjGTIntCpscContactDetailModel.getAddressType();

			if (null != jnjGTIntCpscContactDetailModel.getLotNumber())
			{
				intCpscContactDetailsUid = intCpscContactDetailsUid + Jnjb2bCoreConstants.SYMBOl_PIPE
						+ jnjGTIntCpscContactDetailModel.getLotNumber();
			}

			boolean modelFound = false;
			if (null != jnjGTIntCpscContactDetailModel.getAddressType())
			{
				if (null != existingCpscContactModels)
				{
					/* Check and Update Existing JnjGTCpscContactModel */
					for (final JnjGTCpscContactModel existingCpscContactModel : existingCpscContactModels)
					{

						if (existingCpscContactModel.getUid().equals(intCpscContactDetailsUid))
						{
							modelFound = true;
							if (jnjGTIntCpscContactDetailModel.getDeleteIndicatorContact().booleanValue())
							{
								/* Removing exiting model as its delete indicator is true */
								if (finalCpscContactModels.contains(existingCpscContactModel))
								{
									finalCpscContactModels.remove(existingCpscContactModel);
									jnjGTCpscFeedService.removeItem(existingCpscContactModel);
								}
							}
							else
							{
								/* Update existing JnjGTCpscContactModel */
								setContactDetail(existingCpscContactModel, jnjGTIntCpscContactDetailModel, intCpscContactDetailsUid);
							}
						}
					}
				}

				if (!modelFound && !jnjGTIntCpscContactDetailModel.getDeleteIndicatorContact().booleanValue())
				{
					/* Create New JnjGTProductCpscDetailModel and add to existing JnjGTProductCpscDetailModel */
					jnjGTCpscContactModel = (JnjGTCpscContactModel) getjnjGTCpscFeedService().createNewItem(
							JnjGTCpscContactModel.class);
					setContactDetail(jnjGTCpscContactModel, jnjGTIntCpscContactDetailModel, intCpscContactDetailsUid);
					finalCpscContactModels.add(jnjGTCpscContactModel);
				}
			}
		}

		return finalCpscContactModels;
	}

	/**
	 * Sets the contact detail.
	 * 
	 * @param jnjGTCpscContactModel
	 *           the jnj na cpsc contact model
	 * @param jnjIntCpscContactDetailModel
	 *           the jnj int cpsc contact detail model
	 * @param uid
	 * 
	 * @throws BusinessException
	 *            the business exception
	 */
	private void setContactDetail(final JnjGTCpscContactModel jnjGTCpscContactModel,
			final JnjGTIntCpscContactDetailModel jnjIntCpscContactDetailModel, final String uid) throws BusinessException
	{
		//'jnjGTProductCpscDetailModel UID + Address Type' as Unique ID for jnjGTProductCpscDetailModel
		jnjGTCpscContactModel.setUid(uid);
		jnjGTCpscContactModel.setAddressType(jnjIntCpscContactDetailModel.getAddressType());
		jnjGTCpscContactModel.setName(jnjIntCpscContactDetailModel.getName());
		jnjGTCpscContactModel.setAddressline1(jnjIntCpscContactDetailModel.getAddressline1());
		jnjGTCpscContactModel.setAddressline2(jnjIntCpscContactDetailModel.getAddressline2());
		jnjGTCpscContactModel.setCity(jnjIntCpscContactDetailModel.getCity());
		jnjGTCpscContactModel.setState(jnjIntCpscContactDetailModel.getState());
		jnjGTCpscContactModel.setZipCode(jnjIntCpscContactDetailModel.getZipCode());
		jnjGTCpscContactModel.setCountry(jnjIntCpscContactDetailModel.getCountry());
		jnjGTCpscContactModel.setPhoneNumber(jnjIntCpscContactDetailModel.getPhoneNumber());
		jnjGTCpscContactModel.setEmailId(jnjIntCpscContactDetailModel.getEmailId());
		jnjGTCpscContactModel.setAddressAdditionalInfo(jnjIntCpscContactDetailModel.getAddressAdditionalInfo());

		getjnjGTCpscFeedService().saveItem(jnjGTCpscContactModel);

	}

	/**
	 * Process <code>JnjGTProductCpscDetailModel</code>>.
	 * 
	 * @param existingCpscTestModels
	 *           the existing cpsc test models
	 * @param productSkuCode
	 *           the product sku code
	 * @return the cpsc study details
	 * @throws BusinessException
	 *            the business exception
	 */
	public Set<JnjGTCpscTestDetailModel> getCpscStudyDetails(final Set<JnjGTCpscTestDetailModel> existingCpscTestModels,
			final String productSkuCode, final String lotNumber) throws BusinessException
	{

		final Set<JnjGTCpscTestDetailModel> finalCpscTestModels = new HashSet<JnjGTCpscTestDetailModel>();

		/* Adding Updated JnjGTCpscContactModels to the Final Set */
		if (null != existingCpscTestModels)
		{
			finalCpscTestModels.addAll(existingCpscTestModels);
		}
		/* Fetching JnjGTIntCpscContactDetailModel from Hybris for given productSkuCode */
		final List<JnjGTIntCpscTestDetailModel> jnjIntCpscTestDetails = new ArrayList<>();
		jnjIntCpscTestDetails.addAll(getjnjGTCpscFeedService()
				.getCpscTestDetailByProductCodeAndLotNumber(productSkuCode, lotNumber));

		for (final JnjGTIntCpscTestDetailModel jnjGTIntCpscTestDetailModel : jnjIntCpscTestDetails)
		{
			JnjGTCpscTestDetailModel jnjGTCpscTestDetailModel = null;

			String intCpscTestDetailsUid = null;
			intCpscTestDetailsUid = jnjGTIntCpscTestDetailModel.getProductSkuCode() + Jnjb2bCoreConstants.SYMBOl_PIPE
					+ jnjGTIntCpscTestDetailModel.getStudyNumber();
			if (null != jnjGTIntCpscTestDetailModel.getLotNumber())
			{
				intCpscTestDetailsUid = intCpscTestDetailsUid + Jnjb2bCoreConstants.SYMBOl_PIPE
						+ jnjGTIntCpscTestDetailModel.getLotNumber();
			}

			boolean modelFound = false;
			if (null != jnjGTIntCpscTestDetailModel.getStudyNumber())
			{
				if (null != existingCpscTestModels)
				{
					/* Check and Update Existing JnjGTCpscContactModel */
					for (final JnjGTCpscTestDetailModel existingCpscTestModel : existingCpscTestModels)
					{

						if (existingCpscTestModel.getUid().equals(intCpscTestDetailsUid))
						{
							modelFound = true;
							if (jnjGTIntCpscTestDetailModel.getDeleteIndicatorTest().booleanValue())
							{
								/* Removing exiting model as its delete indicator is true */
								if (finalCpscTestModels.contains(existingCpscTestModel))
								{
									finalCpscTestModels.remove(existingCpscTestModel);
									jnjGTCpscFeedService.removeItem(existingCpscTestModel);
								}
							}
							else
							{
								/* Update existing JnjGTCpscContactModel */
								setTestDetail(existingCpscTestModel, jnjGTIntCpscTestDetailModel, intCpscTestDetailsUid);
							}
						}//End of If Loop for checking existing JnjGTCpscContactModel with INT model
					}//End of For loop for existingCpscTestModels
				}//End of If for Null Check of existingCpscTestModels
				if (!modelFound && !jnjGTIntCpscTestDetailModel.getDeleteIndicatorTest().booleanValue())
				{
					/* Create New JnjGTProductCpscDetailModel and add to existing JnjGTProductCpscDetailModel */
					jnjGTCpscTestDetailModel = (JnjGTCpscTestDetailModel) getjnjGTCpscFeedService().createNewItem(
							JnjGTCpscTestDetailModel.class);
					setTestDetail(jnjGTCpscTestDetailModel, jnjGTIntCpscTestDetailModel, intCpscTestDetailsUid);
					finalCpscTestModels.add(jnjGTCpscTestDetailModel);
				}
			}
		}//End of For loop for jnjIntCpscTestDetails


		return finalCpscTestModels;

	}

	/**
	 * Sets the test detail.
	 * 
	 * @param jnjGTCpscTestDetailModel
	 *           the jnj na cpsc test detail model
	 * @param jnjIntCpscTestDetailModel
	 *           the jnj int cpsc test detail model
	 * @param uid
	 * @throws BusinessException
	 *            the business exception
	 */
	private void setTestDetail(final JnjGTCpscTestDetailModel jnjGTCpscTestDetailModel,
			final JnjGTIntCpscTestDetailModel jnjIntCpscTestDetailModel, final String uid) throws BusinessException
	{
		//'jnjGTProductCpscDetailModel UID + Study Number' as Unique ID for jnjGTProductCpscDetailModel
		jnjGTCpscTestDetailModel.setUid(uid);
		jnjGTCpscTestDetailModel.setStudyNumber(jnjIntCpscTestDetailModel.getStudyNumber());
		jnjGTCpscTestDetailModel.setTestingDate(jnjIntCpscTestDetailModel.getTestingDate());
		jnjGTCpscTestDetailModel.setThirdPartyName(jnjIntCpscTestDetailModel.getThirdPartyName());
		jnjGTCpscTestDetailModel.setAddressLine1(jnjIntCpscTestDetailModel.getAddressLine1());
		jnjGTCpscTestDetailModel.setAddressLine2(jnjIntCpscTestDetailModel.getAddressLine2());
		jnjGTCpscTestDetailModel.setCity(jnjIntCpscTestDetailModel.getCity());
		jnjGTCpscTestDetailModel.setState(jnjIntCpscTestDetailModel.getState());
		jnjGTCpscTestDetailModel.setZipCode(jnjIntCpscTestDetailModel.getZipCode());
		jnjGTCpscTestDetailModel.setCountry(jnjIntCpscTestDetailModel.getCountry());
		jnjGTCpscTestDetailModel.setPhoneNumber(jnjIntCpscTestDetailModel.getPhoneNumber());
		jnjGTCpscTestDetailModel.setStudyComments(jnjIntCpscTestDetailModel.getStudyComments());

		getjnjGTCpscFeedService().saveItem(jnjGTCpscTestDetailModel);
	}

	/**
	 * Gets the jnj na cpsc feed service.
	 * 
	 * @return the jnj na cpsc feed service
	 */
	public JnjGTCpscFeedService getjnjGTCpscFeedService()
	{
		return jnjGTCpscFeedService;
	}

	/**
	 * Fetches the STG Catalog version by product.
	 */
	private CatalogVersionModel getStgCatalogVersionByProduct(final String srcSystemID)
	{
		String catalogID = Jnjgtb2binboundserviceConstants.Product.CONSUMER_USA_CATALOG_ID;
		if (isMDD(srcSystemID))
		{
			catalogID = Jnjgtb2binboundserviceConstants.Product.MDD_CATALOG_ID;
		}
		return catalogVersionService.getCatalogVersion(catalogID, Jnjgtb2binboundserviceConstants.Product.STAGED_CATALOG_VERSION);
	}

	/**
	 * returns true if the product is MDD else returns false
	 * 
	 * @param srcSystemID
	 * @return
	 */
	private boolean isMDD(final String srcSystemID)
	{
		return JnjGTSourceSysId.MDD.toString().equals(JnjGTInboundUtil.fetchValidSourceSysId(srcSystemID));
	}

	@Override
	public void processIntermediateRecords(final String facadeBeanId)
	{
		// TODO Auto-generated method stub

	}

}
