package com.jnj.gt.mapper.invoicedata;

import de.hybris.platform.b2b.services.B2BOrderService;
import de.hybris.platform.b2bacceleratorservices.company.CompanyB2BCommerceService;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.RegionModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.util.Config;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.commons.MessageCode;
import com.jnj.commons.Severity;
import com.jnj.core.enums.JnjGTSourceSysId;
import com.jnj.core.enums.RecordStatus;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.model.JnJProductModel;
import com.jnj.core.model.JnjGTInvoiceEntryLotModel;
import com.jnj.core.model.JnjGTInvoiceEntryModel;
import com.jnj.core.model.JnjGTInvoiceModel;
import com.jnj.core.model.JnjGTInvoicePriceModel;
import com.jnj.core.model.JnjGTVariantProductModel;
import com.jnj.core.services.JnjGTInvoiceService;
import com.jnj.core.services.JnjGTOrderService;
import com.jnj.core.services.address.JnjGTAddressService;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.exceptions.BusinessException;
import com.jnj.gt.constants.Jnjgtb2binboundserviceConstants;
import com.jnj.gt.constants.Jnjgtb2binboundserviceConstants.Invoice;
import com.jnj.gt.constants.Jnjgtb2binboundserviceConstants.Order;
import com.jnj.gt.mapper.JnjAbstractMapper;
import com.jnj.gt.mapper.order.JnjGTOrderSyncDataLoadMapper;
import com.jnj.gt.model.JnjGTIntInvoiceEntryLotModel;
import com.jnj.gt.model.JnjGTIntInvoiceEntryModel;
import com.jnj.gt.model.JnjGTIntInvoiceModel;
import com.jnj.gt.model.JnjGTIntInvoicePriceModel;
import com.jnj.gt.service.common.JnjGTFeedService;
import com.jnj.gt.service.invoice.JnjGTInvoiceFeedService;
import com.jnj.gt.service.product.JnjGTProductFeedService;
import com.jnj.gt.util.JnjGTInboundUtil;



/**
 * Mapper class responsible to fetchall Intermediate Pending records and converts all data into
 * <code>JnjGTInvoiceModel</code> along with all validations and business logics.
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjGTLoadInvoiceDataLoadMapper extends JnjAbstractMapper
{

	/**
	 * The Constant Instance of <code>Logger</code>.
	 */
	private static Logger LOGGER = Logger.getLogger(JnjGTLoadInvoiceDataLoadMapper.class);

	/**
	 * Private instance of <code>JnjGTInvoiceFeedService</code>.
	 */
	@Autowired
	private JnjGTInvoiceFeedService jnjGTInvoiceFeedService;

	/**
	 * Private instance of <code>CatalogVersionService</code>.
	 */
	@Autowired
	private CatalogVersionService catalogVersionService;

	/**
	 * Private instance of <code>CommonI18NService</code>.
	 */
	@Autowired
	private CommonI18NService commonI18NService;

	/**
	 * Private instance of <code>FlexibleSearchService</code>.
	 */
	@Autowired
	private FlexibleSearchService flexibleSearchService;

	/**
	 * Private instance of <code>CompanyB2BCommerceService</code>.
	 */
	@Autowired
	private CompanyB2BCommerceService companyB2BCommerceService;

	/**
	 * Private instance of <code>ModelService</code>.
	 */
	@Autowired
	private ModelService modelService;

	/**
	 * Private instance of <code>B2BOrderService</code>.
	 */
	@Autowired
	private B2BOrderService b2bOrderService;

	/**
	 * Private instance of <code>jnjGTFeedService</code>.
	 */
	@Autowired
	private JnjGTFeedService jnjGTFeedService;

	/**
	 * Private instance of <code>JnjGTAddressService</code>.
	 */
	@Autowired
	private JnjGTAddressService jnjGTAddressService;

	/**
	 * Private instance of <code>JnjGTInvoiceService</code>.
	 */
	@Autowired
	JnjGTInvoiceService jnjGTInvoiceService;

	/**
	 * Private instance of <code>jnjGTProductFeedService</code>
	 */
	@Autowired
	private JnjGTProductFeedService jnjGTProductFeedService;

	/**
	 * Private instance of <code>JnjGTOrderService</code>
	 */
	@Autowired
	private JnjGTOrderService jnjGTOrderService;

	/**
	 * Private instance of <code>jnjGTOrderSyncDataLoadMapper</code>
	 */
	@Autowired
	private JnjGTOrderSyncDataLoadMapper jnjGTOrderSyncDataLoadMapper;

	/**
	 * Constant for MDD key.
	 */
	private static final String CONSUMER_SRC_SYS = "CONSUMER";

	/**
	 * Constant for MDD key.
	 */
	private static final String MDD_SRC_SYS = "MDD";

	private static final String DEFAULT_CURRENCY_CODE = "USD";

	@Override
	public void processIntermediateRecords()
	{
		final List<JnjGTIntInvoiceModel> pendingIntRecords = (List<JnjGTIntInvoiceModel>) jnjGTFeedService.getRecordsByStatus(
				JnjGTIntInvoiceModel._TYPECODE, RecordStatus.PENDING);

		if (pendingIntRecords == null || pendingIntRecords.isEmpty())
		{
			LOGGER.error("JnjGTLoadInvoiceDataLoadMapper : processIntermediateRecords(): No Intermediate Invoice Model found to be proceed");
			return;
		}
		else
		{
			for (final JnjGTIntInvoiceModel JnjGTIntInvoiceModel : pendingIntRecords)
			{
				try
				{
					processIntermediateInvoiceModel(JnjGTIntInvoiceModel);

					LOGGER.info("SUCCESS: Invoice Intermediate Record for the Invoice Doc Number: "
							+ JnjGTIntInvoiceModel.getInvoiceDocNum() + "has been persisted successfully.");


					jnjGTFeedService.updateIntermediateRecord(JnjGTIntInvoiceModel, RecordStatus.SUCCESS, false, null);
				}
				catch (final BusinessException e)
				{
					LOGGER.error("Exception occured while processing INVOICE Intermediate Records for the Invoice Doc Num: "
							+ JnjGTIntInvoiceModel.getInvoiceDocNum() + "\n" + e.getMessage() + "\n Updating WRITE Dashboard.", e);
					jnjGTFeedService.updateIntermediateRecord(JnjGTIntInvoiceModel, RecordStatus.PENDING, true, e.getMessage());
				}
				catch (final Exception e)
				{
					LOGGER.error("Exception occured while processing INVOICE Intermediate Records for the Invoice Doc Num: "
							+ JnjGTIntInvoiceModel.getInvoiceDocNum() + "\n" + e.getMessage() + "\n Updating WRITE Dashboard.", e);
					jnjGTFeedService.updateIntermediateRecord(JnjGTIntInvoiceModel, RecordStatus.PENDING, true, e.getMessage());
				}
			}
		}

	}

	/**
	 * process the single intermediate Invoice model check if existing model exists for this intermediate record update
	 * it otherwise create new and save it.
	 * 
	 * @param jnjGTIntInvoiceModel
	 * @throws BusinessException
	 */
	public void processIntermediateInvoiceModel(final JnjGTIntInvoiceModel jnjGTIntInvoiceModel) throws BusinessException
	{
		boolean isMddSrcSystem = false;
		JnjGTInvoiceModel jnjGTInvoiceModel = null;
		// fetching existing invoice model if exists.
		jnjGTInvoiceModel = jnjGTInvoiceService.getInvoiceByInvoiceNum(jnjGTIntInvoiceModel.getInvoiceDocNum());
		if (jnjGTInvoiceModel == null)
		{
			jnjGTInvoiceModel = modelService.create(JnjGTInvoiceModel.class);
		}
		jnjGTInvoiceModel.setBillingDate(jnjGTIntInvoiceModel.getBillingDate());
		jnjGTInvoiceModel.setInvoiceDocNum(jnjGTIntInvoiceModel.getInvoiceDocNum());
		jnjGTInvoiceModel.setSalesOrg(jnjGTIntInvoiceModel.getSalesOrg());
		jnjGTInvoiceModel.setSalesOrgDistChannel(jnjGTIntInvoiceModel.getSalesOrgDistChannel());
		jnjGTInvoiceModel.setSalesOrgDivCode(jnjGTIntInvoiceModel.getSalesOrgDivCode());
		jnjGTInvoiceModel.setBillingType(jnjGTIntInvoiceModel.getBillingType());

		jnjGTInvoiceModel.setShipToPONum(jnjGTIntInvoiceModel.getShipToPONum());
		jnjGTInvoiceModel.setPayerAccGLN(jnjGTIntInvoiceModel.getPayerAccGLN());
		jnjGTInvoiceModel.setSoldToAccGLN(jnjGTIntInvoiceModel.getSoldToAccGLN());
		jnjGTInvoiceModel.setBillToAccGLN(jnjGTIntInvoiceModel.getBillToAccGLN());
		jnjGTInvoiceModel.setShipToAccGLN(jnjGTIntInvoiceModel.getShipToAccGLN());
		jnjGTInvoiceModel.setInvoiceNum(jnjGTIntInvoiceModel.getInvoiceDocNum());

		final String currencyIsoCode = jnjGTIntInvoiceModel.getCurrency();
		if (currencyIsoCode == null)
		{
			final String errorMessage = "CURRENCY ISO CODE IS NULL FOR THE INVOICE NUMBER: "
					+ jnjGTIntInvoiceModel.getInvoiceDocNum() + ", CANNOT PROCESS THE RECORD.";
			throw new BusinessException(errorMessage);
		}

		jnjGTInvoiceModel.setPoOrderNum(jnjGTIntInvoiceModel.getPoOrderNum());
		jnjGTInvoiceModel.setNetValue(jnjGTIntInvoiceModel.getNetValue());

		jnjGTInvoiceModel.setPayerAccNum(jnjGTIntInvoiceModel.getPayerAccNum());

		final JnJB2BUnitModel unit = (JnJB2BUnitModel) companyB2BCommerceService.getUnitForUid(jnjGTIntInvoiceModel
				.getSoldToAccNum());
		jnjGTInvoiceModel.setSoldToAccNum(unit);
		jnjGTInvoiceModel.setPoOrderNum(jnjGTIntInvoiceModel.getPoOrderNum());
		jnjGTInvoiceModel.setTaxAmt(jnjGTIntInvoiceModel.getTaxAmt());
		jnjGTInvoiceModel.setShipToAccNum(jnjGTIntInvoiceModel.getShipToAccNum());
		jnjGTInvoiceModel.setBillToAccNum(jnjGTIntInvoiceModel.getBillToAccNum());

		CatalogVersionModel catalogVersion = null;
		if (JnjGTSourceSysId.MDD.toString().equals(JnjGTInboundUtil.fetchValidSourceSysId(jnjGTIntInvoiceModel.getSourceSysId())))
		{
			isMddSrcSystem = true;
			try
			{
				catalogVersion = catalogVersionService.getCatalogVersion(Jnjgtb2binboundserviceConstants.Product.MDD_CATALOG_ID,
						Jnjgtb2binboundserviceConstants.Product.ONLINE_CATALOG_VERSION);
			}
			catch (final UnknownIdentifierException exp)
			{
				throw new BusinessException(exp.getMessage());
			}

			if (jnjGTIntInvoiceModel.getShipToAccNum() != null)
			{
				final List<AddressModel> addressModels = jnjGTAddressService.getAddressByIdandOnwerType(jnjGTIntInvoiceModel
						.getShipToAccNum());
				final AddressModel cloneAddress = (addressModels.size() > 0) ? jnjGTAddressService.cloneAddress(addressModels.get(0))
						: null;
				jnjGTInvoiceModel.setShipToAddress(cloneAddress);
			}

			/**
			 * For MDD, process currency irrespective of being USD or not.
			 */
			jnjGTInvoiceModel.setCurrency(getCurrencyFromIsoCode(currencyIsoCode));
		}
		else if (JnjGTSourceSysId.CONSUMER.toString().equals(
				JnjGTInboundUtil.fetchValidSourceSysId(jnjGTIntInvoiceModel.getSourceSysId())))
		{
			AddressModel shipToAdd = jnjGTInvoiceModel.getShipToAddress();
			if (shipToAdd == null)
			{
				shipToAdd = modelService.create(AddressModel.class);
			}
			try
			{
				catalogVersion = catalogVersionService.getCatalogVersion(
						Jnjgtb2binboundserviceConstants.Product.CONSUMER_USA_CATALOG_ID,
						Jnjgtb2binboundserviceConstants.Product.ONLINE_CATALOG_VERSION);
			}
			catch (final UnknownIdentifierException e)
			{
				throw new BusinessException(e.getMessage());
			}

			/**
			 * For Consumer records, process only those currency correpsonding to USD.
			 */
			if (DEFAULT_CURRENCY_CODE.equals(currencyIsoCode))
			{
				final CurrencyModel currency = getCurrencyFromIsoCode(currencyIsoCode);
				jnjGTInvoiceModel.setCurrency(currency);
			}
			else
			{
				final String errorMessage = "CURRENCY [" + currencyIsoCode
						+ "] NOT APPROPRIATE (OTHER THAN USD) FOR THE INVOICE NUMBER: " + jnjGTIntInvoiceModel.getInvoiceDocNum();
				throw new BusinessException(errorMessage);
			}

			if (StringUtils.isNotEmpty(jnjGTIntInvoiceModel.getShipToCountry()))
			{
				RegionModel region = null;
				CountryModel countryModel = null;
				try
				{
					countryModel = commonI18NService.getCountry(jnjGTIntInvoiceModel.getShipToCountry());
					shipToAdd.setCountry(countryModel);
					region = commonI18NService.getRegion(countryModel, jnjGTIntInvoiceModel.getShipToState());
				}
				catch (final UnknownIdentifierException e)
				{
					throw new BusinessException(e.getMessage());
				}

				if (region != null)
				{
					shipToAdd.setRegion(region);
				}
			}

			final StringBuilder street = new StringBuilder();

			if (jnjGTIntInvoiceModel.getShipToStreet1() != null)
			{
				street.append(jnjGTIntInvoiceModel.getShipToStreet1().trim());
			}
			if (jnjGTIntInvoiceModel.getShipToStreet2() != null)
			{
				street.append(jnjGTIntInvoiceModel.getShipToStreet2().trim());
			}
			if (jnjGTIntInvoiceModel.getShipToStreet3() != null)
			{
				street.append(jnjGTIntInvoiceModel.getShipToStreet3().trim());
			}

			shipToAdd.setOwner(jnjGTInvoiceModel);
			shipToAdd.setStreetname(street.toString());
			shipToAdd.setLine1(jnjGTIntInvoiceModel.getShipToName1());
			shipToAdd.setLine2(jnjGTIntInvoiceModel.getShipToName2());
			shipToAdd.setLine3(jnjGTIntInvoiceModel.getShipToName3());
			shipToAdd.setLine4(jnjGTIntInvoiceModel.getShipToName4());
			shipToAdd.setTown(jnjGTIntInvoiceModel.getShipToCity());
			shipToAdd.setPostalcode(jnjGTIntInvoiceModel.getShipToPostal());
			jnjGTInvoiceModel.setShipToAddress(shipToAdd);
		}

		processIntermediateInvoiceEntryModel(jnjGTInvoiceModel, catalogVersion, isMddSrcSystem);

		if (jnjGTInvoiceModel.getOrder() == null)
		{
			throw new BusinessException("COULDN'T PROCESS AND PERSIST INVOICE WITH INVOICE NUMBER: "
					+ jnjGTIntInvoiceModel.getInvoiceDocNum() + ". EXCEPTION: NO ORDER FOUND FROM ANY OF THE INVOICE ENTRY LINE");
		}

		if (isMddSrcSystem)
		{
			processIntermediatePriceModel(jnjGTInvoiceModel);
			setOtherFeesAndPrices(jnjGTInvoiceModel, jnjGTIntInvoiceModel);
		}
		else
		{
			double taxAmount = 0.0;
			double netValue = 0.0;
			taxAmount = (jnjGTIntInvoiceModel.getTaxAmt() != null) ? Double.valueOf(jnjGTIntInvoiceModel.getTaxAmt()).doubleValue()
					: taxAmount;
			netValue = (jnjGTIntInvoiceModel.getNetValue() != null) ? Double.valueOf(jnjGTIntInvoiceModel.getNetValue())
					.doubleValue() : netValue;
			jnjGTInvoiceModel.setInvoiceTotalAmount(Double.valueOf(netValue + taxAmount));
			jnjGTInvoiceModel.setTotalTax(Double.valueOf(taxAmount));


		}
		modelService.save(jnjGTInvoiceModel);

		if (isMddSrcSystem)
		{
			updateOrderAndOrderEntryStatus(jnjGTInvoiceModel.getOrder());
		}
	}

	/**
	 * this method process the intermediate invoice entry model check if existing model exists for this intermediate
	 * record update it. otherwise create new and save it.
	 * 
	 * @param jnjGTInvoiceModel
	 * @param order
	 * @param catalogVersion
	 * @return Collection<JnjGTInvoiceEntryModel>
	 * @throws BusinessException
	 */
	public void processIntermediateInvoiceEntryModel(final JnjGTInvoiceModel jnjGTInvoiceModel,
			final CatalogVersionModel catalogVersion, final boolean isMddSrcSystem) throws BusinessException
	{
		final String invoiceDocNumber = jnjGTInvoiceModel.getInvoiceDocNum();
		final List<JnjGTIntInvoiceEntryModel> pendingIntInvoiceEntryRecords = jnjGTInvoiceFeedService
				.getInvoiceEntry(invoiceDocNumber);

		final List<JnjGTInvoiceEntryModel> invoiceEntries = new ArrayList<JnjGTInvoiceEntryModel>();
		if (pendingIntInvoiceEntryRecords == null || pendingIntInvoiceEntryRecords.isEmpty())
		{
			final String msg = "JnjGTLoadInvoiceDataLoadMapper : processIntermediateInvoiceEntryModel() :: No Intermediate Invoice Entry Model found to be proceed";
			LOGGER.error(msg);
			throw new BusinessException(msg, MessageCode.BUSINESS_EXCEPTION, Severity.BUSINESS_EXCEPTION);
		}
		else
		{
			JnjGTInvoiceEntryModel jnjGTInvoiceEntryModel;
			double totalNetValue = 0.0;
			double subTotal3 = 0.0;

			for (final JnjGTIntInvoiceEntryModel jnjGTIntInvoiceEntryModel : pendingIntInvoiceEntryRecords)
			{
				jnjGTInvoiceEntryModel = jnjGTInvoiceService.getInvoiceEntryByInvoiceNumAndLineItemNum(
						jnjGTIntInvoiceEntryModel.getInvoiceNum(), jnjGTIntInvoiceEntryModel.getLineNum());

				if (jnjGTInvoiceEntryModel == null)
				{
					jnjGTInvoiceEntryModel = modelService.create(JnjGTInvoiceEntryModel.class);
				}
				if (jnjGTIntInvoiceEntryModel.getProduct() == null)
				{
					throw new BusinessException("COULD NOT PROCESS INVOICE RECORD WITH CODE: "
							+ jnjGTIntInvoiceEntryModel.getInvoiceNum() + " AND MATERIAL AS NULL");
				}
				try
				{
					final JnJProductModel product = jnjGTProductFeedService.getProductByCode(jnjGTIntInvoiceEntryModel.getProduct(),
							catalogVersion);

					if (product == null)
					{
						throw new BusinessException("Could NOT find product corresponding to code:  "
								+ jnjGTIntInvoiceEntryModel.getProduct() + " | and Catalog version: " + catalogVersion);
					}
					else
					{
						/*** If the found product itself is a base, then set it otherwise set base product from it. ***/
						if (product.getMaterialBaseProduct() == null)
						{
							jnjGTInvoiceEntryModel.setProduct(product);
						}
						else
						{
							jnjGTInvoiceEntryModel.setProduct(product.getMaterialBaseProduct());
						}

						/*** Set Reference variant, if available, in the entry. ***/
						final JnjGTVariantProductModel referenceVariant = jnjGTProductFeedService
								.getDeliveryGtinEnabledVariant(product);
						jnjGTInvoiceEntryModel.setReferencedVariant(referenceVariant);
					}
				}
				catch (final UnknownIdentifierException e)
				{
					throw new BusinessException(e.getMessage());
				}
				catch (final IllegalArgumentException e)
				{
					throw new BusinessException(e.getMessage());
				}
				jnjGTInvoiceEntryModel.setLineNum(jnjGTIntInvoiceEntryModel.getLineNum());
				jnjGTInvoiceEntryModel.setHighLvlItemNum(jnjGTIntInvoiceEntryModel.getHighLvlItemNum());

				long lineQty = 0;
				if (jnjGTIntInvoiceEntryModel.getQty() != null)
				{
					final Double quantity = Double.valueOf(jnjGTIntInvoiceEntryModel.getQty());
					lineQty = (quantity != null) ? quantity.longValue() : 0;
					jnjGTInvoiceEntryModel.setQty((quantity != null) ? Long.valueOf(lineQty) : null);
				}

				jnjGTInvoiceEntryModel.setNetTotalValue(jnjGTIntInvoiceEntryModel.getNetTotalValue());
				jnjGTInvoiceEntryModel.setProductDivison(jnjGTIntInvoiceEntryModel.getProductDivison());
				jnjGTInvoiceEntryModel.setPlant(jnjGTIntInvoiceEntryModel.getPlant());
				jnjGTInvoiceEntryModel.setSubTotal1(jnjGTIntInvoiceEntryModel.getSubTotal1());
				jnjGTInvoiceEntryModel.setSubTotal2(jnjGTIntInvoiceEntryModel.getSubTotal2());
				jnjGTInvoiceEntryModel.setSubTotal3(jnjGTIntInvoiceEntryModel.getSubTotal3());
				jnjGTInvoiceEntryModel.setSubTotal4(jnjGTIntInvoiceEntryModel.getSubTotal4());
				jnjGTInvoiceEntryModel.setOrderNum(jnjGTIntInvoiceEntryModel.getOrderNum());
				jnjGTInvoiceEntryModel.setSalesUOM(getUnitOfMeasurement(jnjGTIntInvoiceEntryModel.getSalesUOM(), null));
				jnjGTInvoiceEntryModel.setDeliveryNum(jnjGTIntInvoiceEntryModel.getDeliveryNum());
				jnjGTInvoiceEntryModel.setOriginalInvoiceNum(jnjGTIntInvoiceEntryModel.getOriginalInvoiceNum());
				jnjGTInvoiceEntryModel.setNetValuePerUnit(jnjGTIntInvoiceEntryModel.getNetValuePerUnit());
				jnjGTInvoiceEntryModel.setInvoiceNum(jnjGTIntInvoiceEntryModel.getInvoiceNum());
				jnjGTInvoiceEntryModel.setCarrierCode(jnjGTIntInvoiceEntryModel.getCarrierCode());
				jnjGTInvoiceEntryModel.setGtin(jnjGTIntInvoiceEntryModel.getGtin());
				jnjGTInvoiceEntryModel.setOrderedGtin(jnjGTIntInvoiceEntryModel.getOrderedGtin());
				jnjGTInvoiceEntryModel.setShipDate(jnjGTIntInvoiceEntryModel.getShipDate());
				jnjGTInvoiceEntryModel.setTaxValue(jnjGTIntInvoiceEntryModel.getTaxValue());

				if (isMddSrcSystem)
				{
					jnjGTInvoiceEntryModel.setLots(processIntermediateLotModel(invoiceDocNumber,
							jnjGTIntInvoiceEntryModel.getLineNum()));
				}
				if (jnjGTInvoiceModel.getOrder() == null)
				{
					try
					{
						final OrderModel order = jnjGTOrderService.getOrderBySapOrderNumber(jnjGTInvoiceEntryModel.getOrderNum(), null);
						jnjGTInvoiceModel.setOrder(order);
					}
					catch (final UnknownIdentifierException e)
					{
						throw new BusinessException(e.getMessage());
					}
				}

				try
				{
					if (jnjGTIntInvoiceEntryModel.getSubTotal3() != null)
					{
						jnjGTInvoiceEntryModel.setExtendedPrice(Double.valueOf(jnjGTIntInvoiceEntryModel.getSubTotal3()));
					}

					/*** Set and cumulate values only when quantity > 0. ***/
					if (Long.valueOf(lineQty).longValue() > 0)
					{
						/*** Sub Total value for MDD ***/
						if (jnjGTIntInvoiceEntryModel.getNetTotalValue() != null)
						{
							totalNetValue = totalNetValue + Double.valueOf(jnjGTIntInvoiceEntryModel.getNetTotalValue()).doubleValue();
						}

						/*** Sub Total value for Consumer ***/
						if (jnjGTIntInvoiceEntryModel.getSubTotal3() != null)
						{
							subTotal3 = subTotal3 + Double.valueOf(jnjGTIntInvoiceEntryModel.getSubTotal3()).doubleValue();

						}
					}
				}
				catch (final NumberFormatException exception)
				{
					LOGGER.error("Exception while processing Net Value/Sub Total 3 values for Invoice Number: "
							+ jnjGTInvoiceModel.getInvoiceDocNum() + ". Exception: " + exception.getMessage());
				}
				invoiceEntries.add(jnjGTInvoiceEntryModel);
			}
			modelService.saveAll(invoiceEntries);
			jnjGTInvoiceModel.setSubTotalPrice(isMddSrcSystem ? Double.valueOf(totalNetValue) : Double.valueOf(subTotal3));
			if (!isMddSrcSystem)
			{
				jnjGTInvoiceModel.setNetValue(String.valueOf(totalNetValue));
				jnjGTInvoiceModel.setAdjustedRateAllowance(Double.valueOf(subTotal3 - totalNetValue));
			}
		}
		jnjGTInvoiceModel.setEntries(invoiceEntries);
	}

	/**
	 * Finds existing UoM corresponding to the code, and if not found creates new <code>UnitModel</code> with the code.
	 * 
	 * @param code
	 * @param name
	 * @return UnitModel
	 */
	private UnitModel getUnitOfMeasurement(final String code, final String name)
	{
		UnitModel unitModel = null;
		if (code == null)
		{
			return null;
		}
		else
		{
			try
			{
				unitModel = jnjGTProductFeedService.getUnitByCode(code);
				if (unitModel == null)
				{
					LOGGER.info("Could not find any exiting Unit corresponding to the code: " + code + ", creating a new unit.");
					unitModel = jnjGTProductFeedService.createNewItem(UnitModel.class);
					unitModel.setCode(code);
					unitModel.setName(name);
					unitModel.setUnitType(Jnjgtb2binboundserviceConstants.Product.JNJ_UNIT_TYPE);
					jnjGTProductFeedService.saveItem(unitModel);
				}
			}
			catch (final BusinessException exception)
			{
				LOGGER.error("Error while creating unit of measurement for unit code: " + code + ". Exception: "
						+ exception.getMessage());
			}
		}
		return unitModel;
	}

	/**
	 * This method process the intermediate invoice entry lot model check if existing model exists for this intermediate
	 * record update it. otherwise create new and save it.
	 * 
	 * @param invoiceDocNum
	 * @param invoiceEntryNum
	 * @return List<JnjGTInvoiceEntryLotModel>
	 */
	public List<JnjGTInvoiceEntryLotModel> processIntermediateLotModel(final String invoiceDocNum, final String invoiceEntryNum)
	{
		final List<JnjGTIntInvoiceEntryLotModel> pendingIntRecords = jnjGTInvoiceFeedService.getInvoiceEntryLot(invoiceDocNum,
				invoiceEntryNum);

		final List<JnjGTInvoiceEntryLotModel> invoiceEntryLots = new ArrayList<JnjGTInvoiceEntryLotModel>();

		if (pendingIntRecords == null || pendingIntRecords.isEmpty())
		{
			LOGGER.error("JnjGTLoadInvoiceDataLoadMapper : processIntermediateLotModel() :: No Intermediate Invoice Lot Model found to be proceed");
		}
		else
		{
			JnjGTInvoiceEntryLotModel jnjGTInvoiceEntryLotModel;

			for (final JnjGTIntInvoiceEntryLotModel jnjGTIntInvoiceEntryLotModel : pendingIntRecords)
			{

				jnjGTInvoiceEntryLotModel = jnjGTInvoiceService.getInvoiceEntryLotByInvNumLineItemAndLotNum(invoiceDocNum,
						invoiceEntryNum, jnjGTIntInvoiceEntryLotModel.getLotNum());

				if (jnjGTInvoiceEntryLotModel == null)
				{
					jnjGTInvoiceEntryLotModel = modelService.create(JnjGTInvoiceEntryLotModel.class);
				}
				jnjGTInvoiceEntryLotModel.setInvoiceNum(jnjGTIntInvoiceEntryLotModel.getInvoiceNum());
				jnjGTInvoiceEntryLotModel.setLotNum(jnjGTIntInvoiceEntryLotModel.getLotNum());
				jnjGTInvoiceEntryLotModel.setLotQty(jnjGTIntInvoiceEntryLotModel.getLotQty());
				jnjGTInvoiceEntryLotModel.setExpDate(jnjGTIntInvoiceEntryLotModel.getExpiryDate());
				jnjGTInvoiceEntryLotModel.setVendorBatch(jnjGTIntInvoiceEntryLotModel.getVendorBatch());
				jnjGTInvoiceEntryLotModel.setInvoiceLineNum(invoiceEntryNum);

				invoiceEntryLots.add(jnjGTInvoiceEntryLotModel);
			}
		}
		return invoiceEntryLots;
	}

	/**
	 * this method process the intermediate invoice price model it removes the existing prices associated with the
	 * invoice model then creates new inventory prices models corresponding the intermediate models.
	 * 
	 * @param jnjGTInvoiceModel
	 */
	public void processIntermediatePriceModel(final JnjGTInvoiceModel jnjGTInvoiceModel)
	{
		final String invoiceDocNum = jnjGTInvoiceModel.getInvoiceDocNum();

		final List<JnjGTIntInvoicePriceModel> pendingIntRecords = jnjGTInvoiceFeedService.getInvoicePrice(invoiceDocNum);
		final List<JnjGTInvoicePriceModel> invoicePrices = new ArrayList<JnjGTInvoicePriceModel>();
		if (pendingIntRecords == null || pendingIntRecords.isEmpty())
		{
			LOGGER.error("JnjGTLoadInvoiceDataLoadMapper : processIntermediatePriceModel() :: No Intermediate Invoice Price Model found to be proceed");
		}
		else
		{
			// removing existing prices
			final List<JnjGTInvoicePriceModel> jnjGTInvoicePrices = jnjGTInvoiceService.getInvoicePricesByInvoiceNum(invoiceDocNum);
			modelService.removeAll(jnjGTInvoicePrices);

			double totalDropShipFee = 0.0;
			double totalMinOrderQtyFee = 0.0;
			double totalFreightFee = 0.0;
			double totalHsaPromotionFee = 0.0;
			double totalTaxes = 0.0;
			final double totalManualFee = 0.0;
			final double totatExpediteFee = 0.0;
			final List<String> taxFeeKeys = JnJCommonUtil.getValues(Order.PRC_COND_TYPE_MDD_TAX,
					Jnjgtb2binboundserviceConstants.COMMA_STRING);
			final List<String> freightFeeKey = JnJCommonUtil.getValues(Order.PRC_COND_TYPE_FREIGHT_FEE,
					Jnjgtb2binboundserviceConstants.COMMA_STRING);

			JnjGTInvoicePriceModel jnjGTInvoicePriceModel;
			for (final JnjGTIntInvoicePriceModel jnjGTIntInvoicePriceModel : pendingIntRecords)
			{
				jnjGTInvoicePriceModel = modelService.create(JnjGTInvoicePriceModel.class);
				jnjGTInvoicePriceModel.setHeaderPriceValue(jnjGTIntInvoicePriceModel.getHeaderPriceValue());
				jnjGTInvoicePriceModel.setConditionType(jnjGTIntInvoicePriceModel.getConditionType());
				jnjGTInvoicePriceModel.setInvoiceNum(jnjGTIntInvoicePriceModel.getInvoiceNum());
				jnjGTInvoicePriceModel.setBusinessSector(jnjGTIntInvoicePriceModel.getBusinessSector());
				jnjGTInvoicePriceModel.setSourceSysId(jnjGTIntInvoicePriceModel.getSourceSysId());
				jnjGTInvoicePriceModel.setRecTimestamp(jnjGTIntInvoicePriceModel.getRecTimestamp());

				if (null != jnjGTIntInvoicePriceModel.getConditionType())
				{
					try
					{
						final double headerPrice = (jnjGTIntInvoicePriceModel.getHeaderPriceValue() != null) ? Double.valueOf(
								jnjGTIntInvoicePriceModel.getHeaderPriceValue()).doubleValue() : 0.0;

						if (freightFeeKey.contains(jnjGTIntInvoicePriceModel.getConditionType()))
						{
							totalFreightFee = totalFreightFee + headerPrice;
						}
						else if (taxFeeKeys.contains(jnjGTIntInvoicePriceModel.getConditionType()))
						{
							totalTaxes = totalTaxes + headerPrice;
						}
						else if (Config.getParameter(Invoice.DROP_SHIP_CONDITION_TYPE).contains(
								jnjGTIntInvoicePriceModel.getConditionType()))
						{
							totalDropShipFee = totalDropShipFee + headerPrice;
						}
						else if (Config.getParameter(Invoice.MIN_ORDER_QTY_CONDITION_TYPE).contains(
								jnjGTIntInvoicePriceModel.getConditionType()))
						{
							totalMinOrderQtyFee = totalMinOrderQtyFee + headerPrice;
						}
						else if ((Config.getParameter(Invoice.HSA_CONDITION_TYPE)
								.contains(jnjGTIntInvoicePriceModel.getConditionType())))
						{
							totalHsaPromotionFee = totalHsaPromotionFee + headerPrice;
						}
					}
					catch (final NumberFormatException exception)
					{
						LOGGER.error("Cannot set Freight, Tax, Drop Ship, Minimum Order or HSA Promotion fees, exception occured while converting Header price value: "
								+ jnjGTIntInvoicePriceModel.getHeaderPriceValue()
								+ ", for Invoice Number: "
								+ jnjGTInvoiceModel.getInvoiceDocNum() + ". Exception: " + exception.getMessage());
					}
				}
				else
				{
					LOGGER.error("PRICE CONDITION TYPE IS NULL FOR INVOICE WITH DOC NUMBER: " + invoiceDocNum
							+ " , CANNOT PROCESS PRICES.");
				}
				invoicePrices.add(jnjGTInvoicePriceModel);
			}

			jnjGTInvoiceModel.setTotalDropShipFee(Double.valueOf(totalDropShipFee));
			jnjGTInvoiceModel.setTotalminimumOrderFee(Double.valueOf(totalMinOrderQtyFee));
			jnjGTInvoiceModel.setTotalHsaPromotion(Double.valueOf(totalHsaPromotionFee));
			jnjGTInvoiceModel.setTotalFreightFees(Double.valueOf(totalFreightFee));
			jnjGTInvoiceModel.setTotalTax(Double.valueOf(totalTaxes));
			jnjGTInvoiceModel.setTotalManualFee(Double.valueOf(totalManualFee));
			jnjGTInvoiceModel.setTotalExpediteFee(Double.valueOf(totatExpediteFee));

		}

		jnjGTInvoiceModel.setPrices(invoicePrices);
	}

	/**
	 * Calculates and sets the following pricing and charges for an Invoice:
	 * 
	 * <ul>
	 * <li>Total Fees</li>
	 * <li>Other Charges</li>
	 * <li>Invoice Total</li>
	 * </ul>
	 * 
	 * @param jnjGTInvoiceModel
	 * @param jnjGTIntInvoiceModel
	 */
	private void setOtherFeesAndPrices(final JnjGTInvoiceModel jnjGTInvoiceModel, final JnjGTIntInvoiceModel jnjGTIntInvoiceModel)
	{
		final double dropShipFees = (jnjGTInvoiceModel.getTotalDropShipFee() != null) ? jnjGTInvoiceModel.getTotalDropShipFee()
				.doubleValue() : 0.0;

		final double minOrderQtyFees = (jnjGTInvoiceModel.getTotalminimumOrderFee() != null) ? jnjGTInvoiceModel
				.getTotalminimumOrderFee().doubleValue() : 0.0;

		final double hsaPromotionFees = (jnjGTInvoiceModel.getTotalHsaPromotion() != null) ? jnjGTInvoiceModel
				.getTotalHsaPromotion().doubleValue() : 0.0;

		final double subTotalPrice = (jnjGTInvoiceModel.getSubTotalPrice() != null) ? jnjGTInvoiceModel.getTotalHsaPromotion()
				.doubleValue() : 0.0;


		double headerNetValue = 0.0;
		try
		{
			headerNetValue = (jnjGTIntInvoiceModel.getNetValue() != null) ? Double.valueOf(jnjGTIntInvoiceModel.getNetValue())
					.doubleValue() : 0.0;
		}
		catch (final NumberFormatException exception)
		{
			LOGGER.error("Exception while converting Tax Amount price value: " + jnjGTIntInvoiceModel.getTaxAmt()
					+ ", for Invoice Number: " + jnjGTInvoiceModel.getInvoiceDocNum() + ". Exception: " + exception.getMessage());
		}


		final double totalFees = dropShipFees + minOrderQtyFees;
		final double otherCharges = headerNetValue + hsaPromotionFees - (subTotalPrice + totalFees);

		jnjGTInvoiceModel.setTotalFees(Double.valueOf(totalFees));
		jnjGTInvoiceModel.setOtherCharges(Double.valueOf(otherCharges));

	}

	/**
	 * Updates and saves status of the Order and its associated entries from <code>jnjGTOrderSyncDataLoadMapper</code>.
	 * 
	 * @param order
	 */
	private void updateOrderAndOrderEntryStatus(final OrderModel order)
	{
		if (CollectionUtils.isNotEmpty(order.getEntries()))
		{
			for (final AbstractOrderEntryModel entry : order.getEntries())
			{
				try
				{
					jnjGTOrderSyncDataLoadMapper.populateMddOrderEntryStatus(order, entry);
					modelService.save(entry);
				}
				catch (final Exception exception)
				{
					LOGGER.error("ERROR WHILE UPDATING ORDER ENTRY STATUS FOR ORDER | SAP ORDER NUMBER: " + order.getSapOrderNumber()
							+ " | LINE NUMBER: " + entry.getSapOrderlineNumber() + " | EXCEPTION: ", exception);
					continue;
				}
			}
		}

		try
		{
			jnjGTOrderSyncDataLoadMapper.populateMddOrderStatus(order);
			modelService.save(order);
		}
		catch (final Exception exception)
		{
			LOGGER.error("ERROR WHILE UPDATING ORDER STATUS FOR ORDER: " + order.getSapOrderNumber() + " | EXCEPTION: ", exception);
		}
	}

	/**
	 * Retrieve currency based on iso code received.
	 * 
	 * @param isoCode
	 * @return CurrencyModel
	 * @throws BusinessException
	 */
	private CurrencyModel getCurrencyFromIsoCode(final String isoCode) throws BusinessException
	{
		CurrencyModel currency = null;
		try
		{
			currency = commonI18NService.getCurrency(isoCode);
		}
		catch (final UnknownIdentifierException exception)
		{
			throw new BusinessException(exception.getMessage());
		}
		return currency;
	}

	@Override
	public void processIntermediateRecords(final String facadeBeanId)
	{
		// TODO Auto-generated method stub

	}
}
