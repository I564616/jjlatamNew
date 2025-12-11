package com.jnj.gt.mapper.product;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.util.Config;

import java.util.Collection;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.enums.JnjGTSourceSysId;
import com.jnj.core.enums.RecordStatus;
import com.jnj.core.model.JnJProductModel;
import com.jnj.core.model.JnjGTProductLotModel;
import com.jnj.exceptions.BusinessException;
import com.jnj.gt.constants.Jnjgtb2binboundserviceConstants;
import com.jnj.gt.constants.Jnjgtb2binboundserviceConstants.Logging;
import com.jnj.gt.constants.Jnjgtb2binboundserviceConstants.Product;
import com.jnj.gt.mapper.JnjAbstractMapper;
import com.jnj.gt.model.JnjGTIntProductLotMasterModel;
import com.jnj.gt.service.common.JnjGTFeedService;
import com.jnj.gt.service.product.JnjGTLotMasterFeedService;
import com.jnj.gt.service.product.JnjGTProductFeedService;
import com.jnj.gt.util.JnjGTInboundUtil;


/**
 * This Mapper class is responsible for storing the Lot Master details using <code>JnjGTIntProductLotMasterModel</code>
 * in <code>JnJProductModel</code>.
 * 
 * @author t.e.sharma
 */
public class JnjGTProductLotMasterMapper extends JnjAbstractMapper
{
	/**
	 * The Instance of <code>Logger</code>.
	 */
	Logger LOGGER = Logger.getLogger(JnjGTProductLotMasterMapper.class);
	/**
	 * The Instance of <code>jnjGTFeedService</code>.
	 */
	private JnjGTFeedService jnjGTFeedService;

	/**
	 * The Instance of <code>jnjGTLotMasterFeedService</code>.
	 */
	private JnjGTLotMasterFeedService jnjGTLotMasterFeedService;

	@Autowired
	private CatalogVersionService catalogVersionService;


	@Autowired
	private JnjGTProductFeedService jnjGTProductFeedService;

	@Override
	public void processIntermediateRecords()
	{
		final Collection<JnjGTIntProductLotMasterModel> pendingIntRecords = (Collection<JnjGTIntProductLotMasterModel>) jnjGTFeedService
				.getRecordsByStatus(JnjGTIntProductLotMasterModel._TYPECODE, RecordStatus.PENDING);

		for (final JnjGTIntProductLotMasterModel jnjGTIntProductLotMasterModel : pendingIntRecords)
		{
			try
			{
				processIntermediateModel(jnjGTIntProductLotMasterModel);
				LOGGER.info("LOT MASTER Intermediate record with PRODUCT SKU CODE: "
						+ jnjGTIntProductLotMasterModel.getProductSkuCode() + ", was successfully processed. ");
				getjnjGTFeedService().updateIntermediateRecord(jnjGTIntProductLotMasterModel, RecordStatus.SUCCESS, false, null);
			}
			catch (final BusinessException e)
			{
				LOGGER.error("Lot Master Intermediate record with PRODUCT SKU CODE: "
						+ jnjGTIntProductLotMasterModel.getProductSkuCode() + ", could NOT be processed. Error: " + e.getMessage());
				getjnjGTFeedService().updateIntermediateRecord(jnjGTIntProductLotMasterModel, null, true, e.getMessage(),
						Logging.LOT_MASTER, jnjGTIntProductLotMasterModel.getProductSkuCode());
			}
			catch (final Exception e)
			{
				LOGGER.error("Lot Master Intermediate record with PRODUCT SKU CODE: "
						+ jnjGTIntProductLotMasterModel.getProductSkuCode() + ", could NOT be processed. Error: " + e.getMessage());
				getjnjGTFeedService().updateIntermediateRecord(jnjGTIntProductLotMasterModel, null, true, e.getMessage(),
						Logging.LOT_MASTER, jnjGTIntProductLotMasterModel.getProductSkuCode());
			}
		}
	}

	/**
	 * Process intermediate <code>JnjGTIntProductLotMasterModel</code>.
	 * 
	 * @param jnjGTIntProductLotMasterModel
	 * @throws BusinessException
	 * 
	 */
	public void processIntermediateModel(final JnjGTIntProductLotMasterModel jnjGTIntProductLotMasterModel)
			throws BusinessException
	{
		final String productSkuCode = jnjGTIntProductLotMasterModel.getProductSkuCode();
		final JnJProductModel product = jnjGTProductFeedService.getProductByCode(productSkuCode,
				getStgCatalogVersionByProduct(jnjGTIntProductLotMasterModel.getSrcSysId()));

		if (product == null)
		{
			final String errorMessage = "Could NOT find a Product corresponding to the PRODUCT SKU CODE: " + productSkuCode
					+ ". Lot Info assocaited with the product SKU cannot be processed.";
			throw new BusinessException(errorMessage);
		}

		final boolean isOcdProduct = (product.getSalesOrgCode() != null)
				&& Config.getParameter(Product.OCD_PRODUCT_DIVISIONS).equals(product.getSalesOrgCode());
		//Getting the existing lot information of the product
		//final Set<JnjGTProductLotModel> existingLotInformation = product.getLotInformation();
		//Creating the new lot information of the product
		//	final Set<JnjGTProductLotModel> updatedLotInformation = new HashSet<JnjGTProductLotModel>();

		//Adding the existing information into the new Hashset
		//updatedLotInformation.addAll(existingLotInformation);

		//Doing Get Model By example For The Uid
		JnjGTProductLotModel jnjGTProductLotModel = new JnjGTProductLotModel();
		jnjGTProductLotModel.setUid(createUidForProductLot(jnjGTIntProductLotMasterModel));
		jnjGTProductLotModel = jnjGTLotMasterFeedService.getProductLotByExample(jnjGTProductLotModel);
		//If Model returned as Null, creating a new Model updating It and then setting it in the Collection.
		if (jnjGTProductLotModel == null)
		{
			jnjGTProductLotModel = (JnjGTProductLotModel) getjnjGTLotMasterFeedService().createNewItem(JnjGTProductLotModel.class);
		}
		populateProductLotInfo(jnjGTProductLotModel, jnjGTIntProductLotMasterModel, isOcdProduct);
		getjnjGTLotMasterFeedService().saveItem(jnjGTProductLotModel);
		//updatedLotInformation.add(jnjGTProductLotModel);
		//try
		//{
		//getjnjGTLotMasterFeedService().saveItems(updatedLotInformation);
		//	product.setLotInformation(updatedLotInformation);
		//	getjnjGTLotMasterFeedService().saveItem(product);
		//	}
		//	catch (final BusinessException e)
		//	{
		//		LOGGER.error("Saving/Removal of Product Lot Information Records have caused an exception.");
		//		throw e;
		//	}
	}

	/**
	 * Updates/Populates the <code>JnjGTProductLotModel</code> attributes using values from
	 * <code>JnjGTIntProductLotMasterModel></code>.
	 * 
	 * @param jnjGTProductLotModel
	 * @param jnjGTIntProductLotMasterModel
	 */
	private void populateProductLotInfo(final JnjGTProductLotModel jnjGTProductLotModel,
			final JnjGTIntProductLotMasterModel jnjGTIntProductLotMasterModel, final boolean isOcdProduct)
	{

		jnjGTProductLotModel.setUid(createUidForProductLot(jnjGTIntProductLotMasterModel));
		/** CR 88 [AR] : Set Vendor Batch as Lot Number value if it's an OCD product **/
		jnjGTProductLotModel.setLotNumber((isOcdProduct) ? jnjGTIntProductLotMasterModel.getVendorBatch()
				: jnjGTIntProductLotMasterModel.getLotNumber());
		jnjGTProductLotModel.setCompanyCode(jnjGTIntProductLotMasterModel.getCompanyCode());
		jnjGTProductLotModel.setVendorBatch((isOcdProduct) ? jnjGTIntProductLotMasterModel.getLotNumber()
				: jnjGTIntProductLotMasterModel.getVendorBatch());
		jnjGTProductLotModel.setExpirationDate(jnjGTIntProductLotMasterModel.getExpirationDate());
		jnjGTProductLotModel.setProductCode(jnjGTIntProductLotMasterModel.getProductSkuCode());

	}

	/**
	 * @param jnjGTProductLotModel
	 * @param jnjGTIntProductLotMasterModel
	 * @return
	 */
	private String createUidForProductLot(final JnjGTIntProductLotMasterModel jnjGTIntProductLotMasterModel)
	{
		return jnjGTIntProductLotMasterModel.getLotNumber() + Jnjb2bCoreConstants.SYMBOl_PIPE
				+ jnjGTIntProductLotMasterModel.getCompanyCode() + Jnjb2bCoreConstants.SYMBOl_PIPE
				+ jnjGTIntProductLotMasterModel.getDivision() + Jnjb2bCoreConstants.SYMBOl_PIPE
				+ jnjGTIntProductLotMasterModel.getProductSkuCode();
	}

	/**
	 * @return the jnjGTFeedService
	 */
	public JnjGTFeedService getjnjGTFeedService()
	{
		return jnjGTFeedService;
	}

	/**
	 * @return the jnjGTLotMasterFeedService
	 */
	public JnjGTLotMasterFeedService getjnjGTLotMasterFeedService()
	{
		return jnjGTLotMasterFeedService;
	}

	/**
	 * @param jnjGTFeedService
	 *           the jnjGTFeedService to set
	 */
	public void setjnjGTFeedService(final JnjGTFeedService jnjGTFeedService)
	{
		this.jnjGTFeedService = jnjGTFeedService;
	}

	/**
	 * @param jnjGTLotMasterFeedService
	 *           the jnjGTLotMasterFeedService to set
	 */
	public void setjnjGTLotMasterFeedService(final JnjGTLotMasterFeedService jnjGTLotMasterFeedService)
	{
		this.jnjGTLotMasterFeedService = jnjGTLotMasterFeedService;
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
	public void processIntermediateRecords(String facadeBeanId) {
		// TODO Auto-generated method stub
		
	}
}
