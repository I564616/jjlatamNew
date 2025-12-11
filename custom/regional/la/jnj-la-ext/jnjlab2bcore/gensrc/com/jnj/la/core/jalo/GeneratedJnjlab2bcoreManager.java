/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at Nov 28, 2025, 5:16:52 PM                    ---
 * ----------------------------------------------------------------
 */
package com.jnj.la.core.jalo;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.jalo.JnJB2bCustomer;
import com.jnj.core.jalo.JnJInvoiceOrder;
import com.jnj.core.jalo.JnjContract;
import com.jnj.core.jalo.JnjContractEntry;
import com.jnj.core.jalo.JnjDeliverySchedule;
import com.jnj.core.jalo.JnjDropShipmentDetails;
import com.jnj.core.jalo.JnjLaudo;
import com.jnj.core.jalo.JnjOrdEntStsMapping;
import com.jnj.core.jalo.JnjUomConversion;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.jalo.ExportERPOrderCronjob;
import com.jnj.la.core.jalo.JnJCompany;
import com.jnj.la.core.jalo.JnJLaB2BUnit;
import com.jnj.la.core.jalo.JnJLaProduct;
import com.jnj.la.core.jalo.JnJLaUserAccountPreference;
import com.jnj.la.core.jalo.JnJProductSalesOrg;
import com.jnj.la.core.jalo.JnJUploadedInvoiceDate;
import com.jnj.la.core.jalo.JnjCDLExportJob;
import com.jnj.la.core.jalo.JnjCmirConversion;
import com.jnj.la.core.jalo.JnjCommercialUomConversion;
import com.jnj.la.core.jalo.JnjCronJob;
import com.jnj.la.core.jalo.JnjIndirectFormProcess;
import com.jnj.la.core.jalo.JnjIndirectPayer;
import com.jnj.la.core.jalo.JnjIntLoadTranslation;
import com.jnj.la.core.jalo.JnjLAConsolidatedEmailRecipients;
import com.jnj.la.core.jalo.JnjLADailyEmailRecipients;
import com.jnj.la.core.jalo.JnjLAEmailMatrix;
import com.jnj.la.core.jalo.JnjLAImmediateEmailRecipients;
import com.jnj.la.core.jalo.JnjLAOrderPermutationMatrix;
import com.jnj.la.core.jalo.JnjLAOrderStatusChangeProcess;
import com.jnj.la.core.jalo.JnjLAResetPasswordEmailProcess;
import com.jnj.la.core.jalo.JnjLaActiveUserReportEmailProcess;
import com.jnj.la.core.jalo.JnjLaConsolidatedEmailProcess;
import com.jnj.la.core.jalo.JnjLaCronJobMonitoingEmailBusinessProcess;
import com.jnj.la.core.jalo.JnjLaCronJobMonitoring;
import com.jnj.la.core.jalo.JnjLaInvoiceNotificationEmailProcess;
import com.jnj.la.core.jalo.JnjLaOpenOrdersReportTemplate;
import com.jnj.la.core.jalo.JnjLaOrderArchivalCronJob;
import com.jnj.la.core.jalo.JnjLaSAPFailedOrdersReportEmailProcess;
import com.jnj.la.core.jalo.JnjLaSalesOrgDivisionMapping;
import com.jnj.la.core.jalo.JnjLaSoldToShipToSpecialCase;
import com.jnj.la.core.jalo.JnjLaudoDeleteCronJob;
import com.jnj.la.core.jalo.JnjOrderArchivalJobConfig;
import com.jnj.la.core.jalo.JnjOrderArchivalResult;
import com.jnj.la.core.jalo.JnjOrderChannel;
import com.jnj.la.core.jalo.JnjOrderType;
import com.jnj.la.core.jalo.JnjProductCatalogsSyncJob;
import com.jnj.la.core.jalo.JnjSAPErrorTranslationTable;
import com.jnj.la.core.jalo.JnjSellOutReport;
import com.jnj.la.core.jalo.JnjUploadOrder;
import com.jnj.la.core.jalo.JnjUploadOrderSHA;
import com.jnj.la.core.jalo.LoadTranslation;
import de.hybris.platform.acceleratorservices.constants.AcceleratorServicesConstants;
import de.hybris.platform.acceleratorservices.jalo.email.EmailAttachment;
import de.hybris.platform.b2b.jalo.B2BCustomer;
import de.hybris.platform.b2b.jalo.B2BUnit;
import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.commerceservices.jalo.OrgUnit;
import de.hybris.platform.cronjob.jalo.CronJob;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LItem;
import de.hybris.platform.jalo.c2l.Country;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.jalo.link.Link;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.order.Order;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.jalo.user.UserGroup;
import de.hybris.platform.util.OneToManyHandler;
import de.hybris.platform.util.Utilities;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Generated class for type <code>Jnjlab2bcoreManager</code>.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedJnjlab2bcoreManager extends Extension
{
	/**
	* {@link OneToManyHandler} for handling 1:n JNJLAINVOICES's relation attributes from 'many' side.
	**/
	protected static final OneToManyHandler<JnJInvoiceOrder> JNJINVOICE_ORDERRELATIONJNJLAINVOICESHANDLER = new OneToManyHandler<JnJInvoiceOrder>(
	Jnjb2bCoreConstants.TC.JNJINVOICEORDER,
	false,
	"order",
	null,
	false,
	true,
	CollectionType.COLLECTION
	);
	/**
	* {@link OneToManyHandler} for handling 1:n ERRORRECORDS's relation attributes from 'many' side.
	**/
	protected static final OneToManyHandler<EmailAttachment> CRONJOBERRORRECORDSRELATIONERRORRECORDSHANDLER = new OneToManyHandler<EmailAttachment>(
	AcceleratorServicesConstants.TC.EMAILATTACHMENT,
	true,
	"cronJob",
	null,
	false,
	true,
	CollectionType.LIST
	);
	/** Relation ordering override parameter constants for JnJB2bUnitToContractRel from ((jnjlab2bcore))*/
	protected static String JNJB2BUNITTOCONTRACTREL_SRC_ORDERED = "relation.JnJB2bUnitToContractRel.source.ordered";
	protected static String JNJB2BUNITTOCONTRACTREL_TGT_ORDERED = "relation.JnJB2bUnitToContractRel.target.ordered";
	/** Relation disable markmodifed parameter constants for JnJB2bUnitToContractRel from ((jnjlab2bcore))*/
	protected static String JNJB2BUNITTOCONTRACTREL_MARKMODIFIED = "relation.JnJB2bUnitToContractRel.markmodified";
	protected static final Map<String, Map<String, AttributeMode>> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, Map<String, AttributeMode>> ttmp = new HashMap();
		Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put("eCCContractNum", AttributeMode.INITIAL);
		ttmp.put("com.jnj.core.jalo.JnjContractEntry", Collections.unmodifiableMap(tmp));
		tmp = new HashMap<String, AttributeMode>();
		tmp.put("indirectCustomer", AttributeMode.INITIAL);
		tmp.put("indirectPayer", AttributeMode.INITIAL);
		tmp.put("totalFee", AttributeMode.INITIAL);
		tmp.put("orderChannel", AttributeMode.INITIAL);
		tmp.put("jnjOrderType", AttributeMode.INITIAL);
		tmp.put("holdCreditCardFlag", AttributeMode.INITIAL);
		tmp.put("freeItem", AttributeMode.INITIAL);
		tmp.put("freeItemsQuanity", AttributeMode.INITIAL);
		tmp.put("freeItemsAvailabilityStatus", AttributeMode.INITIAL);
		tmp.put("freeItemUnit", AttributeMode.INITIAL);
		tmp.put("freeGoodScheduleLines", AttributeMode.INITIAL);
		tmp.put("isContractCart", AttributeMode.INITIAL);
		tmp.put("retryAttempts", AttributeMode.INITIAL);
		tmp.put("empenhoFilesFullPath", AttributeMode.INITIAL);
		tmp.put("externalOrderRefNumber", AttributeMode.INITIAL);
		tmp.put("customerFreightType", AttributeMode.INITIAL);
		tmp.put("complementaryInfo", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.jalo.order.AbstractOrder", Collections.unmodifiableMap(tmp));
		tmp = new HashMap<String, AttributeMode>();
		tmp.put("indirectCustomerName", AttributeMode.INITIAL);
		tmp.put("indirectPayer", AttributeMode.INITIAL);
		tmp.put("indirectPayerName", AttributeMode.INITIAL);
		tmp.put("gTSHold", AttributeMode.INITIAL);
		tmp.put("expectedPrice", AttributeMode.INITIAL);
		tmp.put("freeItem", AttributeMode.INITIAL);
		tmp.put("freeItemsLogicOriginalQuantity", AttributeMode.INITIAL);
		tmp.put("freeItemsQuanity", AttributeMode.INITIAL);
		tmp.put("freeItemsAvailabilityStatus", AttributeMode.INITIAL);
		tmp.put("freeItemUnit", AttributeMode.INITIAL);
		tmp.put("freeGoodScheduleLines", AttributeMode.INITIAL);
		tmp.put("showInfoLbl", AttributeMode.INITIAL);
		tmp.put("showDescLbl", AttributeMode.INITIAL);
		tmp.put("carrierEstDeliveryDateStatus", AttributeMode.INITIAL);
		tmp.put("carrierActualDeliveryDateStatus", AttributeMode.INITIAL);
		tmp.put("backorderStatus", AttributeMode.INITIAL);
		tmp.put("lineOverallStatus", AttributeMode.INITIAL);
		tmp.put("deliveryStatus", AttributeMode.INITIAL);
		tmp.put("lineInvoiceStatus", AttributeMode.INITIAL);
		tmp.put("invoicedQuantity", AttributeMode.INITIAL);
		tmp.put("shippedQuantity", AttributeMode.INITIAL);
		tmp.put("deliveredQuantity", AttributeMode.INITIAL);
		tmp.put("previousStatus", AttributeMode.INITIAL);
		tmp.put("pendingImmediateEmail", AttributeMode.INITIAL);
		tmp.put("pendingDailyEmail", AttributeMode.INITIAL);
		tmp.put("pendingConsolidatedEmail", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.jalo.order.AbstractOrderEntry", Collections.unmodifiableMap(tmp));
		tmp = new HashMap<String, AttributeMode>();
		tmp.put("shipper", AttributeMode.INITIAL);
		tmp.put("principal", AttributeMode.INITIAL);
		tmp.put("shipperMD", AttributeMode.INITIAL);
		ttmp.put("com.jnj.core.jalo.JnjDropShipmentDetails", Collections.unmodifiableMap(tmp));
		tmp = new HashMap<String, AttributeMode>();
		tmp.put("eligibleUserGroup", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.jalo.c2l.Country", Collections.unmodifiableMap(tmp));
		tmp = new HashMap<String, AttributeMode>();
		tmp.put("roundedQuantity", AttributeMode.INITIAL);
		tmp.put("proofOfDeliveryDate", AttributeMode.INITIAL);
		tmp.put("carrierExpectedDeliveryDate", AttributeMode.INITIAL);
		ttmp.put("com.jnj.core.jalo.JnjDeliverySchedule", Collections.unmodifiableMap(tmp));
		tmp = new HashMap<String, AttributeMode>();
		tmp.put("height", AttributeMode.INITIAL);
		tmp.put("width", AttributeMode.INITIAL);
		tmp.put("length", AttributeMode.INITIAL);
		tmp.put("shippingWeight", AttributeMode.INITIAL);
		tmp.put("shippingUnit", AttributeMode.INITIAL);
		tmp.put("volumeUnit", AttributeMode.INITIAL);
		tmp.put("isoCode", AttributeMode.INITIAL);
		ttmp.put("com.jnj.core.jalo.JnjUomConversion", Collections.unmodifiableMap(tmp));
		tmp = new HashMap<String, AttributeMode>();
		tmp.put("showPlannedDate", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.category.jalo.Category", Collections.unmodifiableMap(tmp));
		tmp = new HashMap<String, AttributeMode>();
		tmp.put("customerCode", AttributeMode.INITIAL);
		tmp.put("accountManager", AttributeMode.INITIAL);
		tmp.put("accountPreferences", AttributeMode.INITIAL);
		tmp.put("commercialUserFlag", AttributeMode.INITIAL);
		tmp.put("commercialUserSector", AttributeMode.INITIAL);
		ttmp.put("com.jnj.core.jalo.JnJB2bCustomer", Collections.unmodifiableMap(tmp));
		tmp = new HashMap<String, AttributeMode>();
		tmp.put("updatedByFile", AttributeMode.INITIAL);
		tmp.put("updatedByFileDate", AttributeMode.INITIAL);
		tmp.put("carrierEstimateDeliveryDate", AttributeMode.INITIAL);
		tmp.put("carrierConfirmedDeliveryDate", AttributeMode.INITIAL);
		tmp.put("order", AttributeMode.INITIAL);
		ttmp.put("com.jnj.core.jalo.JnJInvoiceOrder", Collections.unmodifiableMap(tmp));
		tmp = new HashMap<String, AttributeMode>();
		tmp.put("backorderStatus", AttributeMode.INITIAL);
		tmp.put("carrierEstDeliveryDateStatus", AttributeMode.INITIAL);
		tmp.put("carrierActualDeliveryDateStatus", AttributeMode.INITIAL);
		ttmp.put("com.jnj.core.jalo.JnjOrdEntStsMapping", Collections.unmodifiableMap(tmp));
		tmp = new HashMap<String, AttributeMode>();
		tmp.put("batchRequired", AttributeMode.INITIAL);
		ttmp.put("com.jnj.core.jalo.JnjLaudo", Collections.unmodifiableMap(tmp));
		tmp = new HashMap<String, AttributeMode>();
		tmp.put("sapFailedOrderReportEmailSent", AttributeMode.INITIAL);
		tmp.put("emailProcess", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.jalo.order.Order", Collections.unmodifiableMap(tmp));
		tmp = new HashMap<String, AttributeMode>();
		tmp.put("cronJob", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.acceleratorservices.jalo.email.EmailAttachment", Collections.unmodifiableMap(tmp));
		DEFAULT_INITIAL_ATTRIBUTES = ttmp;
	}
	@Override
	public Map<String, AttributeMode> getDefaultAttributeModes(final Class<? extends Item> itemClass)
	{
		Map<String, AttributeMode> ret = new HashMap<>();
		final Map<String, AttributeMode> attr = DEFAULT_INITIAL_ATTRIBUTES.get(itemClass.getName());
		if (attr != null)
		{
			ret.putAll(attr);
		}
		return ret;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJB2bCustomer.accountManager</code> attribute.
	 * @return the accountManager
	 */
	public String getAccountManager(final SessionContext ctx, final B2BCustomer item)
	{
		return (String)item.getProperty( ctx, Jnjlab2bcoreConstants.Attributes.JnJB2bCustomer.ACCOUNTMANAGER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJB2bCustomer.accountManager</code> attribute.
	 * @return the accountManager
	 */
	public String getAccountManager(final JnJB2bCustomer item)
	{
		return getAccountManager( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJB2bCustomer.accountManager</code> attribute. 
	 * @param value the accountManager
	 */
	public void setAccountManager(final SessionContext ctx, final B2BCustomer item, final String value)
	{
		item.setProperty(ctx, Jnjlab2bcoreConstants.Attributes.JnJB2bCustomer.ACCOUNTMANAGER,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJB2bCustomer.accountManager</code> attribute. 
	 * @param value the accountManager
	 */
	public void setAccountManager(final JnJB2bCustomer item, final String value)
	{
		setAccountManager( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJB2bCustomer.accountPreferences</code> attribute.
	 * @return the accountPreferences
	 */
	public List<JnJLaUserAccountPreference> getAccountPreferences(final SessionContext ctx, final B2BCustomer item)
	{
		List<JnJLaUserAccountPreference> coll = (List<JnJLaUserAccountPreference>)item.getProperty( ctx, Jnjlab2bcoreConstants.Attributes.JnJB2bCustomer.ACCOUNTPREFERENCES);
		return coll != null ? coll : Collections.EMPTY_LIST;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJB2bCustomer.accountPreferences</code> attribute.
	 * @return the accountPreferences
	 */
	public List<JnJLaUserAccountPreference> getAccountPreferences(final JnJB2bCustomer item)
	{
		return getAccountPreferences( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJB2bCustomer.accountPreferences</code> attribute. 
	 * @param value the accountPreferences
	 */
	public void setAccountPreferences(final SessionContext ctx, final B2BCustomer item, final List<JnJLaUserAccountPreference> value)
	{
		item.setProperty(ctx, Jnjlab2bcoreConstants.Attributes.JnJB2bCustomer.ACCOUNTPREFERENCES,value == null || !value.isEmpty() ? value : null );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJB2bCustomer.accountPreferences</code> attribute. 
	 * @param value the accountPreferences
	 */
	public void setAccountPreferences(final JnJB2bCustomer item, final List<JnJLaUserAccountPreference> value)
	{
		setAccountPreferences( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjContract.accounts</code> attribute.
	 * @return the accounts
	 */
	public Set<B2BUnit> getAccounts(final SessionContext ctx, final GenericItem item)
	{
		final List<B2BUnit> items = item.getLinkedItems( 
			ctx,
			false,
			Jnjlab2bcoreConstants.Relations.JNJB2BUNITTOCONTRACTREL,
			"B2BUnit",
			null,
			false,
			false
		);
		return new LinkedHashSet<B2BUnit>(items);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjContract.accounts</code> attribute.
	 * @return the accounts
	 */
	public Set<B2BUnit> getAccounts(final JnjContract item)
	{
		return getAccounts( getSession().getSessionContext(), item );
	}
	
	public long getAccountsCount(final SessionContext ctx, final JnjContract item)
	{
		return item.getLinkedItemsCount(
			ctx,
			false,
			Jnjlab2bcoreConstants.Relations.JNJB2BUNITTOCONTRACTREL,
			"B2BUnit",
			null
		);
	}
	
	public long getAccountsCount(final JnjContract item)
	{
		return getAccountsCount( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjContract.accounts</code> attribute. 
	 * @param value the accounts
	 */
	public void setAccounts(final SessionContext ctx, final GenericItem item, final Set<B2BUnit> value)
	{
		item.setLinkedItems( 
			ctx,
			false,
			Jnjlab2bcoreConstants.Relations.JNJB2BUNITTOCONTRACTREL,
			null,
			value,
			false,
			false,
			Utilities.getMarkModifiedOverride(JNJB2BUNITTOCONTRACTREL_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjContract.accounts</code> attribute. 
	 * @param value the accounts
	 */
	public void setAccounts(final JnjContract item, final Set<B2BUnit> value)
	{
		setAccounts( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to accounts. 
	 * @param value the item to add to accounts
	 */
	public void addToAccounts(final SessionContext ctx, final GenericItem item, final B2BUnit value)
	{
		item.addLinkedItems( 
			ctx,
			false,
			Jnjlab2bcoreConstants.Relations.JNJB2BUNITTOCONTRACTREL,
			null,
			Collections.singletonList(value),
			false,
			false,
			Utilities.getMarkModifiedOverride(JNJB2BUNITTOCONTRACTREL_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to accounts. 
	 * @param value the item to add to accounts
	 */
	public void addToAccounts(final JnjContract item, final B2BUnit value)
	{
		addToAccounts( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from accounts. 
	 * @param value the item to remove from accounts
	 */
	public void removeFromAccounts(final SessionContext ctx, final GenericItem item, final B2BUnit value)
	{
		item.removeLinkedItems( 
			ctx,
			false,
			Jnjlab2bcoreConstants.Relations.JNJB2BUNITTOCONTRACTREL,
			null,
			Collections.singletonList(value),
			false,
			false,
			Utilities.getMarkModifiedOverride(JNJB2BUNITTOCONTRACTREL_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from accounts. 
	 * @param value the item to remove from accounts
	 */
	public void removeFromAccounts(final JnjContract item, final B2BUnit value)
	{
		removeFromAccounts( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.backorderStatus</code> attribute.
	 * @return the backorderStatus
	 */
	public EnumerationValue getBackorderStatus(final SessionContext ctx, final AbstractOrderEntry item)
	{
		return (EnumerationValue)item.getProperty( ctx, Jnjlab2bcoreConstants.Attributes.AbstractOrderEntry.BACKORDERSTATUS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.backorderStatus</code> attribute.
	 * @return the backorderStatus
	 */
	public EnumerationValue getBackorderStatus(final AbstractOrderEntry item)
	{
		return getBackorderStatus( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.backorderStatus</code> attribute. 
	 * @param value the backorderStatus
	 */
	public void setBackorderStatus(final SessionContext ctx, final AbstractOrderEntry item, final EnumerationValue value)
	{
		item.setProperty(ctx, Jnjlab2bcoreConstants.Attributes.AbstractOrderEntry.BACKORDERSTATUS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.backorderStatus</code> attribute. 
	 * @param value the backorderStatus
	 */
	public void setBackorderStatus(final AbstractOrderEntry item, final EnumerationValue value)
	{
		setBackorderStatus( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjOrdEntStsMapping.backorderStatus</code> attribute.
	 * @return the backorderStatus
	 */
	public String getBackorderStatus(final SessionContext ctx, final GenericItem item)
	{
		return (String)item.getProperty( ctx, Jnjlab2bcoreConstants.Attributes.JnjOrdEntStsMapping.BACKORDERSTATUS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjOrdEntStsMapping.backorderStatus</code> attribute.
	 * @return the backorderStatus
	 */
	public String getBackorderStatus(final JnjOrdEntStsMapping item)
	{
		return getBackorderStatus( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjOrdEntStsMapping.backorderStatus</code> attribute. 
	 * @param value the backorderStatus
	 */
	public void setBackorderStatus(final SessionContext ctx, final GenericItem item, final String value)
	{
		item.setProperty(ctx, Jnjlab2bcoreConstants.Attributes.JnjOrdEntStsMapping.BACKORDERSTATUS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjOrdEntStsMapping.backorderStatus</code> attribute. 
	 * @param value the backorderStatus
	 */
	public void setBackorderStatus(final JnjOrdEntStsMapping item, final String value)
	{
		setBackorderStatus( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLaudo.batchRequired</code> attribute.
	 * @return the batchRequired
	 */
	public Boolean isBatchRequired(final SessionContext ctx, final GenericItem item)
	{
		return (Boolean)item.getProperty( ctx, Jnjlab2bcoreConstants.Attributes.JnjLaudo.BATCHREQUIRED);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLaudo.batchRequired</code> attribute.
	 * @return the batchRequired
	 */
	public Boolean isBatchRequired(final JnjLaudo item)
	{
		return isBatchRequired( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLaudo.batchRequired</code> attribute. 
	 * @return the batchRequired
	 */
	public boolean isBatchRequiredAsPrimitive(final SessionContext ctx, final JnjLaudo item)
	{
		Boolean value = isBatchRequired( ctx,item );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjLaudo.batchRequired</code> attribute. 
	 * @return the batchRequired
	 */
	public boolean isBatchRequiredAsPrimitive(final JnjLaudo item)
	{
		return isBatchRequiredAsPrimitive( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLaudo.batchRequired</code> attribute. 
	 * @param value the batchRequired
	 */
	public void setBatchRequired(final SessionContext ctx, final GenericItem item, final Boolean value)
	{
		item.setProperty(ctx, Jnjlab2bcoreConstants.Attributes.JnjLaudo.BATCHREQUIRED,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLaudo.batchRequired</code> attribute. 
	 * @param value the batchRequired
	 */
	public void setBatchRequired(final JnjLaudo item, final Boolean value)
	{
		setBatchRequired( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLaudo.batchRequired</code> attribute. 
	 * @param value the batchRequired
	 */
	public void setBatchRequired(final SessionContext ctx, final JnjLaudo item, final boolean value)
	{
		setBatchRequired( ctx, item, Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjLaudo.batchRequired</code> attribute. 
	 * @param value the batchRequired
	 */
	public void setBatchRequired(final JnjLaudo item, final boolean value)
	{
		setBatchRequired( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.carrierActualDeliveryDateStatus</code> attribute.
	 * @return the carrierActualDeliveryDateStatus
	 */
	public EnumerationValue getCarrierActualDeliveryDateStatus(final SessionContext ctx, final AbstractOrderEntry item)
	{
		return (EnumerationValue)item.getProperty( ctx, Jnjlab2bcoreConstants.Attributes.AbstractOrderEntry.CARRIERACTUALDELIVERYDATESTATUS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.carrierActualDeliveryDateStatus</code> attribute.
	 * @return the carrierActualDeliveryDateStatus
	 */
	public EnumerationValue getCarrierActualDeliveryDateStatus(final AbstractOrderEntry item)
	{
		return getCarrierActualDeliveryDateStatus( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.carrierActualDeliveryDateStatus</code> attribute. 
	 * @param value the carrierActualDeliveryDateStatus
	 */
	public void setCarrierActualDeliveryDateStatus(final SessionContext ctx, final AbstractOrderEntry item, final EnumerationValue value)
	{
		item.setProperty(ctx, Jnjlab2bcoreConstants.Attributes.AbstractOrderEntry.CARRIERACTUALDELIVERYDATESTATUS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.carrierActualDeliveryDateStatus</code> attribute. 
	 * @param value the carrierActualDeliveryDateStatus
	 */
	public void setCarrierActualDeliveryDateStatus(final AbstractOrderEntry item, final EnumerationValue value)
	{
		setCarrierActualDeliveryDateStatus( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjOrdEntStsMapping.carrierActualDeliveryDateStatus</code> attribute.
	 * @return the carrierActualDeliveryDateStatus
	 */
	public String getCarrierActualDeliveryDateStatus(final SessionContext ctx, final GenericItem item)
	{
		return (String)item.getProperty( ctx, Jnjlab2bcoreConstants.Attributes.JnjOrdEntStsMapping.CARRIERACTUALDELIVERYDATESTATUS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjOrdEntStsMapping.carrierActualDeliveryDateStatus</code> attribute.
	 * @return the carrierActualDeliveryDateStatus
	 */
	public String getCarrierActualDeliveryDateStatus(final JnjOrdEntStsMapping item)
	{
		return getCarrierActualDeliveryDateStatus( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjOrdEntStsMapping.carrierActualDeliveryDateStatus</code> attribute. 
	 * @param value the carrierActualDeliveryDateStatus
	 */
	public void setCarrierActualDeliveryDateStatus(final SessionContext ctx, final GenericItem item, final String value)
	{
		item.setProperty(ctx, Jnjlab2bcoreConstants.Attributes.JnjOrdEntStsMapping.CARRIERACTUALDELIVERYDATESTATUS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjOrdEntStsMapping.carrierActualDeliveryDateStatus</code> attribute. 
	 * @param value the carrierActualDeliveryDateStatus
	 */
	public void setCarrierActualDeliveryDateStatus(final JnjOrdEntStsMapping item, final String value)
	{
		setCarrierActualDeliveryDateStatus( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJInvoiceOrder.carrierConfirmedDeliveryDate</code> attribute.
	 * @return the carrierConfirmedDeliveryDate
	 */
	public Date getCarrierConfirmedDeliveryDate(final SessionContext ctx, final GenericItem item)
	{
		return (Date)item.getProperty( ctx, Jnjlab2bcoreConstants.Attributes.JnJInvoiceOrder.CARRIERCONFIRMEDDELIVERYDATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJInvoiceOrder.carrierConfirmedDeliveryDate</code> attribute.
	 * @return the carrierConfirmedDeliveryDate
	 */
	public Date getCarrierConfirmedDeliveryDate(final JnJInvoiceOrder item)
	{
		return getCarrierConfirmedDeliveryDate( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJInvoiceOrder.carrierConfirmedDeliveryDate</code> attribute. 
	 * @param value the carrierConfirmedDeliveryDate
	 */
	public void setCarrierConfirmedDeliveryDate(final SessionContext ctx, final GenericItem item, final Date value)
	{
		item.setProperty(ctx, Jnjlab2bcoreConstants.Attributes.JnJInvoiceOrder.CARRIERCONFIRMEDDELIVERYDATE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJInvoiceOrder.carrierConfirmedDeliveryDate</code> attribute. 
	 * @param value the carrierConfirmedDeliveryDate
	 */
	public void setCarrierConfirmedDeliveryDate(final JnJInvoiceOrder item, final Date value)
	{
		setCarrierConfirmedDeliveryDate( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.carrierEstDeliveryDateStatus</code> attribute.
	 * @return the carrierEstDeliveryDateStatus
	 */
	public EnumerationValue getCarrierEstDeliveryDateStatus(final SessionContext ctx, final AbstractOrderEntry item)
	{
		return (EnumerationValue)item.getProperty( ctx, Jnjlab2bcoreConstants.Attributes.AbstractOrderEntry.CARRIERESTDELIVERYDATESTATUS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.carrierEstDeliveryDateStatus</code> attribute.
	 * @return the carrierEstDeliveryDateStatus
	 */
	public EnumerationValue getCarrierEstDeliveryDateStatus(final AbstractOrderEntry item)
	{
		return getCarrierEstDeliveryDateStatus( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.carrierEstDeliveryDateStatus</code> attribute. 
	 * @param value the carrierEstDeliveryDateStatus
	 */
	public void setCarrierEstDeliveryDateStatus(final SessionContext ctx, final AbstractOrderEntry item, final EnumerationValue value)
	{
		item.setProperty(ctx, Jnjlab2bcoreConstants.Attributes.AbstractOrderEntry.CARRIERESTDELIVERYDATESTATUS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.carrierEstDeliveryDateStatus</code> attribute. 
	 * @param value the carrierEstDeliveryDateStatus
	 */
	public void setCarrierEstDeliveryDateStatus(final AbstractOrderEntry item, final EnumerationValue value)
	{
		setCarrierEstDeliveryDateStatus( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjOrdEntStsMapping.carrierEstDeliveryDateStatus</code> attribute.
	 * @return the carrierEstDeliveryDateStatus
	 */
	public String getCarrierEstDeliveryDateStatus(final SessionContext ctx, final GenericItem item)
	{
		return (String)item.getProperty( ctx, Jnjlab2bcoreConstants.Attributes.JnjOrdEntStsMapping.CARRIERESTDELIVERYDATESTATUS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjOrdEntStsMapping.carrierEstDeliveryDateStatus</code> attribute.
	 * @return the carrierEstDeliveryDateStatus
	 */
	public String getCarrierEstDeliveryDateStatus(final JnjOrdEntStsMapping item)
	{
		return getCarrierEstDeliveryDateStatus( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjOrdEntStsMapping.carrierEstDeliveryDateStatus</code> attribute. 
	 * @param value the carrierEstDeliveryDateStatus
	 */
	public void setCarrierEstDeliveryDateStatus(final SessionContext ctx, final GenericItem item, final String value)
	{
		item.setProperty(ctx, Jnjlab2bcoreConstants.Attributes.JnjOrdEntStsMapping.CARRIERESTDELIVERYDATESTATUS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjOrdEntStsMapping.carrierEstDeliveryDateStatus</code> attribute. 
	 * @param value the carrierEstDeliveryDateStatus
	 */
	public void setCarrierEstDeliveryDateStatus(final JnjOrdEntStsMapping item, final String value)
	{
		setCarrierEstDeliveryDateStatus( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJInvoiceOrder.carrierEstimateDeliveryDate</code> attribute.
	 * @return the carrierEstimateDeliveryDate
	 */
	public Date getCarrierEstimateDeliveryDate(final SessionContext ctx, final GenericItem item)
	{
		return (Date)item.getProperty( ctx, Jnjlab2bcoreConstants.Attributes.JnJInvoiceOrder.CARRIERESTIMATEDELIVERYDATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJInvoiceOrder.carrierEstimateDeliveryDate</code> attribute.
	 * @return the carrierEstimateDeliveryDate
	 */
	public Date getCarrierEstimateDeliveryDate(final JnJInvoiceOrder item)
	{
		return getCarrierEstimateDeliveryDate( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJInvoiceOrder.carrierEstimateDeliveryDate</code> attribute. 
	 * @param value the carrierEstimateDeliveryDate
	 */
	public void setCarrierEstimateDeliveryDate(final SessionContext ctx, final GenericItem item, final Date value)
	{
		item.setProperty(ctx, Jnjlab2bcoreConstants.Attributes.JnJInvoiceOrder.CARRIERESTIMATEDELIVERYDATE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJInvoiceOrder.carrierEstimateDeliveryDate</code> attribute. 
	 * @param value the carrierEstimateDeliveryDate
	 */
	public void setCarrierEstimateDeliveryDate(final JnJInvoiceOrder item, final Date value)
	{
		setCarrierEstimateDeliveryDate( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjDeliverySchedule.carrierExpectedDeliveryDate</code> attribute.
	 * @return the carrierExpectedDeliveryDate - Carrier expected date of delivery of the schedule line
	 */
	public Date getCarrierExpectedDeliveryDate(final SessionContext ctx, final GenericItem item)
	{
		return (Date)item.getProperty( ctx, Jnjlab2bcoreConstants.Attributes.JnjDeliverySchedule.CARRIEREXPECTEDDELIVERYDATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjDeliverySchedule.carrierExpectedDeliveryDate</code> attribute.
	 * @return the carrierExpectedDeliveryDate - Carrier expected date of delivery of the schedule line
	 */
	public Date getCarrierExpectedDeliveryDate(final JnjDeliverySchedule item)
	{
		return getCarrierExpectedDeliveryDate( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjDeliverySchedule.carrierExpectedDeliveryDate</code> attribute. 
	 * @param value the carrierExpectedDeliveryDate - Carrier expected date of delivery of the schedule line
	 */
	public void setCarrierExpectedDeliveryDate(final SessionContext ctx, final GenericItem item, final Date value)
	{
		item.setProperty(ctx, Jnjlab2bcoreConstants.Attributes.JnjDeliverySchedule.CARRIEREXPECTEDDELIVERYDATE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjDeliverySchedule.carrierExpectedDeliveryDate</code> attribute. 
	 * @param value the carrierExpectedDeliveryDate - Carrier expected date of delivery of the schedule line
	 */
	public void setCarrierExpectedDeliveryDate(final JnjDeliverySchedule item, final Date value)
	{
		setCarrierExpectedDeliveryDate( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJB2bCustomer.commercialUserFlag</code> attribute.
	 * @return the commercialUserFlag
	 */
	public Boolean isCommercialUserFlag(final SessionContext ctx, final B2BCustomer item)
	{
		return (Boolean)item.getProperty( ctx, Jnjlab2bcoreConstants.Attributes.JnJB2bCustomer.COMMERCIALUSERFLAG);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJB2bCustomer.commercialUserFlag</code> attribute.
	 * @return the commercialUserFlag
	 */
	public Boolean isCommercialUserFlag(final JnJB2bCustomer item)
	{
		return isCommercialUserFlag( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJB2bCustomer.commercialUserFlag</code> attribute. 
	 * @return the commercialUserFlag
	 */
	public boolean isCommercialUserFlagAsPrimitive(final SessionContext ctx, final JnJB2bCustomer item)
	{
		Boolean value = isCommercialUserFlag( ctx,item );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJB2bCustomer.commercialUserFlag</code> attribute. 
	 * @return the commercialUserFlag
	 */
	public boolean isCommercialUserFlagAsPrimitive(final JnJB2bCustomer item)
	{
		return isCommercialUserFlagAsPrimitive( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJB2bCustomer.commercialUserFlag</code> attribute. 
	 * @param value the commercialUserFlag
	 */
	public void setCommercialUserFlag(final SessionContext ctx, final B2BCustomer item, final Boolean value)
	{
		item.setProperty(ctx, Jnjlab2bcoreConstants.Attributes.JnJB2bCustomer.COMMERCIALUSERFLAG,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJB2bCustomer.commercialUserFlag</code> attribute. 
	 * @param value the commercialUserFlag
	 */
	public void setCommercialUserFlag(final JnJB2bCustomer item, final Boolean value)
	{
		setCommercialUserFlag( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJB2bCustomer.commercialUserFlag</code> attribute. 
	 * @param value the commercialUserFlag
	 */
	public void setCommercialUserFlag(final SessionContext ctx, final JnJB2bCustomer item, final boolean value)
	{
		setCommercialUserFlag( ctx, item, Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJB2bCustomer.commercialUserFlag</code> attribute. 
	 * @param value the commercialUserFlag
	 */
	public void setCommercialUserFlag(final JnJB2bCustomer item, final boolean value)
	{
		setCommercialUserFlag( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJB2bCustomer.commercialUserSector</code> attribute.
	 * @return the commercialUserSector
	 */
	public String getCommercialUserSector(final SessionContext ctx, final B2BCustomer item)
	{
		return (String)item.getProperty( ctx, Jnjlab2bcoreConstants.Attributes.JnJB2bCustomer.COMMERCIALUSERSECTOR);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJB2bCustomer.commercialUserSector</code> attribute.
	 * @return the commercialUserSector
	 */
	public String getCommercialUserSector(final JnJB2bCustomer item)
	{
		return getCommercialUserSector( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJB2bCustomer.commercialUserSector</code> attribute. 
	 * @param value the commercialUserSector
	 */
	public void setCommercialUserSector(final SessionContext ctx, final B2BCustomer item, final String value)
	{
		item.setProperty(ctx, Jnjlab2bcoreConstants.Attributes.JnJB2bCustomer.COMMERCIALUSERSECTOR,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJB2bCustomer.commercialUserSector</code> attribute. 
	 * @param value the commercialUserSector
	 */
	public void setCommercialUserSector(final JnJB2bCustomer item, final String value)
	{
		setCommercialUserSector( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.complementaryInfo</code> attribute.
	 * @return the complementaryInfo - Complementary Information
	 */
	public String getComplementaryInfo(final SessionContext ctx, final AbstractOrder item)
	{
		return (String)item.getProperty( ctx, Jnjlab2bcoreConstants.Attributes.AbstractOrder.COMPLEMENTARYINFO);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.complementaryInfo</code> attribute.
	 * @return the complementaryInfo - Complementary Information
	 */
	public String getComplementaryInfo(final AbstractOrder item)
	{
		return getComplementaryInfo( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.complementaryInfo</code> attribute. 
	 * @param value the complementaryInfo - Complementary Information
	 */
	public void setComplementaryInfo(final SessionContext ctx, final AbstractOrder item, final String value)
	{
		item.setProperty(ctx, Jnjlab2bcoreConstants.Attributes.AbstractOrder.COMPLEMENTARYINFO,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.complementaryInfo</code> attribute. 
	 * @param value the complementaryInfo - Complementary Information
	 */
	public void setComplementaryInfo(final AbstractOrder item, final String value)
	{
		setComplementaryInfo( getSession().getSessionContext(), item, value );
	}
	
	public ExportERPOrderCronjob createExportERPOrderCronjob(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( Jnjlab2bcoreConstants.TC.EXPORTERPORDERCRONJOB );
			return (ExportERPOrderCronjob)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating ExportERPOrderCronjob : "+e.getMessage(), 0 );
		}
	}
	
	public ExportERPOrderCronjob createExportERPOrderCronjob(final Map attributeValues)
	{
		return createExportERPOrderCronjob( getSession().getSessionContext(), attributeValues );
	}
	
	public JnjCDLExportJob createJnjCDLExportJob(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( Jnjlab2bcoreConstants.TC.JNJCDLEXPORTJOB );
			return (JnjCDLExportJob)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating JnjCDLExportJob : "+e.getMessage(), 0 );
		}
	}
	
	public JnjCDLExportJob createJnjCDLExportJob(final Map attributeValues)
	{
		return createJnjCDLExportJob( getSession().getSessionContext(), attributeValues );
	}
	
	public JnjCmirConversion createJnjCmirConversion(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( Jnjlab2bcoreConstants.TC.JNJCMIRCONVERSION );
			return (JnjCmirConversion)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating JnjCmirConversion : "+e.getMessage(), 0 );
		}
	}
	
	public JnjCmirConversion createJnjCmirConversion(final Map attributeValues)
	{
		return createJnjCmirConversion( getSession().getSessionContext(), attributeValues );
	}
	
	public JnjCommercialUomConversion createJnjCommercialUomConversion(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( Jnjlab2bcoreConstants.TC.JNJCOMMERCIALUOMCONVERSION );
			return (JnjCommercialUomConversion)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating JnjCommercialUomConversion : "+e.getMessage(), 0 );
		}
	}
	
	public JnjCommercialUomConversion createJnjCommercialUomConversion(final Map attributeValues)
	{
		return createJnjCommercialUomConversion( getSession().getSessionContext(), attributeValues );
	}
	
	public JnJCompany createJnJCompany(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( Jnjlab2bcoreConstants.TC.JNJCOMPANY );
			return (JnJCompany)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating JnJCompany : "+e.getMessage(), 0 );
		}
	}
	
	public JnJCompany createJnJCompany(final Map attributeValues)
	{
		return createJnJCompany( getSession().getSessionContext(), attributeValues );
	}
	
	public JnjCronJob createJnjCronJob(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( Jnjlab2bcoreConstants.TC.JNJCRONJOB );
			return (JnjCronJob)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating JnjCronJob : "+e.getMessage(), 0 );
		}
	}
	
	public JnjCronJob createJnjCronJob(final Map attributeValues)
	{
		return createJnjCronJob( getSession().getSessionContext(), attributeValues );
	}
	
	public JnjIndirectFormProcess createJnjIndirectFormProcess(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( Jnjlab2bcoreConstants.TC.JNJINDIRECTFORMPROCESS );
			return (JnjIndirectFormProcess)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating JnjIndirectFormProcess : "+e.getMessage(), 0 );
		}
	}
	
	public JnjIndirectFormProcess createJnjIndirectFormProcess(final Map attributeValues)
	{
		return createJnjIndirectFormProcess( getSession().getSessionContext(), attributeValues );
	}
	
	public JnjIndirectPayer createJnjIndirectPayer(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( Jnjlab2bcoreConstants.TC.JNJINDIRECTPAYER );
			return (JnjIndirectPayer)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating JnjIndirectPayer : "+e.getMessage(), 0 );
		}
	}
	
	public JnjIndirectPayer createJnjIndirectPayer(final Map attributeValues)
	{
		return createJnjIndirectPayer( getSession().getSessionContext(), attributeValues );
	}
	
	public JnjIntLoadTranslation createJnjIntLoadTranslation(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( Jnjlab2bcoreConstants.TC.JNJINTLOADTRANSLATION );
			return (JnjIntLoadTranslation)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating JnjIntLoadTranslation : "+e.getMessage(), 0 );
		}
	}
	
	public JnjIntLoadTranslation createJnjIntLoadTranslation(final Map attributeValues)
	{
		return createJnjIntLoadTranslation( getSession().getSessionContext(), attributeValues );
	}
	
	public JnjLaActiveUserReportEmailProcess createJnjLaActiveUserReportEmailProcess(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( Jnjlab2bcoreConstants.TC.JNJLAACTIVEUSERREPORTEMAILPROCESS );
			return (JnjLaActiveUserReportEmailProcess)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating JnjLaActiveUserReportEmailProcess : "+e.getMessage(), 0 );
		}
	}
	
	public JnjLaActiveUserReportEmailProcess createJnjLaActiveUserReportEmailProcess(final Map attributeValues)
	{
		return createJnjLaActiveUserReportEmailProcess( getSession().getSessionContext(), attributeValues );
	}
	
	public JnJLaB2BUnit createJnJLaB2BUnit(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( Jnjlab2bcoreConstants.TC.JNJLAB2BUNIT );
			return (JnJLaB2BUnit)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating JnJLaB2BUnit : "+e.getMessage(), 0 );
		}
	}
	
	public JnJLaB2BUnit createJnJLaB2BUnit(final Map attributeValues)
	{
		return createJnJLaB2BUnit( getSession().getSessionContext(), attributeValues );
	}
	
	public JnjLaConsolidatedEmailProcess createJnjLaConsolidatedEmailProcess(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( Jnjlab2bcoreConstants.TC.JNJLACONSOLIDATEDEMAILPROCESS );
			return (JnjLaConsolidatedEmailProcess)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating JnjLaConsolidatedEmailProcess : "+e.getMessage(), 0 );
		}
	}
	
	public JnjLaConsolidatedEmailProcess createJnjLaConsolidatedEmailProcess(final Map attributeValues)
	{
		return createJnjLaConsolidatedEmailProcess( getSession().getSessionContext(), attributeValues );
	}
	
	public JnjLAConsolidatedEmailRecipients createJnjLAConsolidatedEmailRecipients(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( Jnjlab2bcoreConstants.TC.JNJLACONSOLIDATEDEMAILRECIPIENTS );
			return (JnjLAConsolidatedEmailRecipients)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating JnjLAConsolidatedEmailRecipients : "+e.getMessage(), 0 );
		}
	}
	
	public JnjLAConsolidatedEmailRecipients createJnjLAConsolidatedEmailRecipients(final Map attributeValues)
	{
		return createJnjLAConsolidatedEmailRecipients( getSession().getSessionContext(), attributeValues );
	}
	
	public JnjLaCronJobMonitoingEmailBusinessProcess createJnjLaCronJobMonitoingEmailBusinessProcess(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( Jnjlab2bcoreConstants.TC.JNJLACRONJOBMONITOINGEMAILBUSINESSPROCESS );
			return (JnjLaCronJobMonitoingEmailBusinessProcess)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating JnjLaCronJobMonitoingEmailBusinessProcess : "+e.getMessage(), 0 );
		}
	}
	
	public JnjLaCronJobMonitoingEmailBusinessProcess createJnjLaCronJobMonitoingEmailBusinessProcess(final Map attributeValues)
	{
		return createJnjLaCronJobMonitoingEmailBusinessProcess( getSession().getSessionContext(), attributeValues );
	}
	
	public JnjLaCronJobMonitoring createJnjLaCronJobMonitoring(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( Jnjlab2bcoreConstants.TC.JNJLACRONJOBMONITORING );
			return (JnjLaCronJobMonitoring)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating JnjLaCronJobMonitoring : "+e.getMessage(), 0 );
		}
	}
	
	public JnjLaCronJobMonitoring createJnjLaCronJobMonitoring(final Map attributeValues)
	{
		return createJnjLaCronJobMonitoring( getSession().getSessionContext(), attributeValues );
	}
	
	public JnjLADailyEmailRecipients createJnjLADailyEmailRecipients(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( Jnjlab2bcoreConstants.TC.JNJLADAILYEMAILRECIPIENTS );
			return (JnjLADailyEmailRecipients)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating JnjLADailyEmailRecipients : "+e.getMessage(), 0 );
		}
	}
	
	public JnjLADailyEmailRecipients createJnjLADailyEmailRecipients(final Map attributeValues)
	{
		return createJnjLADailyEmailRecipients( getSession().getSessionContext(), attributeValues );
	}
	
	public JnjLAEmailMatrix createJnjLAEmailMatrix(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( Jnjlab2bcoreConstants.TC.JNJLAEMAILMATRIX );
			return (JnjLAEmailMatrix)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating JnjLAEmailMatrix : "+e.getMessage(), 0 );
		}
	}
	
	public JnjLAEmailMatrix createJnjLAEmailMatrix(final Map attributeValues)
	{
		return createJnjLAEmailMatrix( getSession().getSessionContext(), attributeValues );
	}
	
	public JnjLAImmediateEmailRecipients createJnjLAImmediateEmailRecipients(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( Jnjlab2bcoreConstants.TC.JNJLAIMMEDIATEEMAILRECIPIENTS );
			return (JnjLAImmediateEmailRecipients)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating JnjLAImmediateEmailRecipients : "+e.getMessage(), 0 );
		}
	}
	
	public JnjLAImmediateEmailRecipients createJnjLAImmediateEmailRecipients(final Map attributeValues)
	{
		return createJnjLAImmediateEmailRecipients( getSession().getSessionContext(), attributeValues );
	}
	
	public JnjLaInvoiceNotificationEmailProcess createJnjLaInvoiceNotificationEmailProcess(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( Jnjlab2bcoreConstants.TC.JNJLAINVOICENOTIFICATIONEMAILPROCESS );
			return (JnjLaInvoiceNotificationEmailProcess)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating JnjLaInvoiceNotificationEmailProcess : "+e.getMessage(), 0 );
		}
	}
	
	public JnjLaInvoiceNotificationEmailProcess createJnjLaInvoiceNotificationEmailProcess(final Map attributeValues)
	{
		return createJnjLaInvoiceNotificationEmailProcess( getSession().getSessionContext(), attributeValues );
	}
	
	public JnjLaOpenOrdersReportTemplate createJnjLaOpenOrdersReportTemplate(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( Jnjlab2bcoreConstants.TC.JNJLAOPENORDERSREPORTTEMPLATE );
			return (JnjLaOpenOrdersReportTemplate)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating JnjLaOpenOrdersReportTemplate : "+e.getMessage(), 0 );
		}
	}
	
	public JnjLaOpenOrdersReportTemplate createJnjLaOpenOrdersReportTemplate(final Map attributeValues)
	{
		return createJnjLaOpenOrdersReportTemplate( getSession().getSessionContext(), attributeValues );
	}
	
	public JnjLaOrderArchivalCronJob createJnjLaOrderArchivalCronJob(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( Jnjlab2bcoreConstants.TC.JNJLAORDERARCHIVALCRONJOB );
			return (JnjLaOrderArchivalCronJob)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating JnjLaOrderArchivalCronJob : "+e.getMessage(), 0 );
		}
	}
	
	public JnjLaOrderArchivalCronJob createJnjLaOrderArchivalCronJob(final Map attributeValues)
	{
		return createJnjLaOrderArchivalCronJob( getSession().getSessionContext(), attributeValues );
	}
	
	public JnjLAOrderPermutationMatrix createJnjLAOrderPermutationMatrix(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( Jnjlab2bcoreConstants.TC.JNJLAORDERPERMUTATIONMATRIX );
			return (JnjLAOrderPermutationMatrix)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating JnjLAOrderPermutationMatrix : "+e.getMessage(), 0 );
		}
	}
	
	public JnjLAOrderPermutationMatrix createJnjLAOrderPermutationMatrix(final Map attributeValues)
	{
		return createJnjLAOrderPermutationMatrix( getSession().getSessionContext(), attributeValues );
	}
	
	public JnjLAOrderStatusChangeProcess createJnjLAOrderStatusChangeProcess(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( Jnjlab2bcoreConstants.TC.JNJLAORDERSTATUSCHANGEPROCESS );
			return (JnjLAOrderStatusChangeProcess)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating JnjLAOrderStatusChangeProcess : "+e.getMessage(), 0 );
		}
	}
	
	public JnjLAOrderStatusChangeProcess createJnjLAOrderStatusChangeProcess(final Map attributeValues)
	{
		return createJnjLAOrderStatusChangeProcess( getSession().getSessionContext(), attributeValues );
	}
	
	public JnJLaProduct createJnJLaProduct(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( Jnjlab2bcoreConstants.TC.JNJLAPRODUCT );
			return (JnJLaProduct)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating JnJLaProduct : "+e.getMessage(), 0 );
		}
	}
	
	public JnJLaProduct createJnJLaProduct(final Map attributeValues)
	{
		return createJnJLaProduct( getSession().getSessionContext(), attributeValues );
	}
	
	public JnjLAResetPasswordEmailProcess createJnjLAResetPasswordEmailProcess(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( Jnjlab2bcoreConstants.TC.JNJLARESETPASSWORDEMAILPROCESS );
			return (JnjLAResetPasswordEmailProcess)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating JnjLAResetPasswordEmailProcess : "+e.getMessage(), 0 );
		}
	}
	
	public JnjLAResetPasswordEmailProcess createJnjLAResetPasswordEmailProcess(final Map attributeValues)
	{
		return createJnjLAResetPasswordEmailProcess( getSession().getSessionContext(), attributeValues );
	}
	
	public JnjLaSalesOrgDivisionMapping createJnjLaSalesOrgDivisionMapping(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( Jnjlab2bcoreConstants.TC.JNJLASALESORGDIVISIONMAPPING );
			return (JnjLaSalesOrgDivisionMapping)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating JnjLaSalesOrgDivisionMapping : "+e.getMessage(), 0 );
		}
	}
	
	public JnjLaSalesOrgDivisionMapping createJnjLaSalesOrgDivisionMapping(final Map attributeValues)
	{
		return createJnjLaSalesOrgDivisionMapping( getSession().getSessionContext(), attributeValues );
	}
	
	public JnjLaSAPFailedOrdersReportEmailProcess createJnjLaSAPFailedOrdersReportEmailProcess(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( Jnjlab2bcoreConstants.TC.JNJLASAPFAILEDORDERSREPORTEMAILPROCESS );
			return (JnjLaSAPFailedOrdersReportEmailProcess)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating JnjLaSAPFailedOrdersReportEmailProcess : "+e.getMessage(), 0 );
		}
	}
	
	public JnjLaSAPFailedOrdersReportEmailProcess createJnjLaSAPFailedOrdersReportEmailProcess(final Map attributeValues)
	{
		return createJnjLaSAPFailedOrdersReportEmailProcess( getSession().getSessionContext(), attributeValues );
	}
	
	public JnjLaSoldToShipToSpecialCase createJnjLaSoldToShipToSpecialCase(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( Jnjlab2bcoreConstants.TC.JNJLASOLDTOSHIPTOSPECIALCASE );
			return (JnjLaSoldToShipToSpecialCase)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating JnjLaSoldToShipToSpecialCase : "+e.getMessage(), 0 );
		}
	}
	
	public JnjLaSoldToShipToSpecialCase createJnjLaSoldToShipToSpecialCase(final Map attributeValues)
	{
		return createJnjLaSoldToShipToSpecialCase( getSession().getSessionContext(), attributeValues );
	}
	
	public JnjLaudoDeleteCronJob createJnjLaudoDeleteCronJob(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( Jnjlab2bcoreConstants.TC.JNJLAUDODELETECRONJOB );
			return (JnjLaudoDeleteCronJob)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating JnjLaudoDeleteCronJob : "+e.getMessage(), 0 );
		}
	}
	
	public JnjLaudoDeleteCronJob createJnjLaudoDeleteCronJob(final Map attributeValues)
	{
		return createJnjLaudoDeleteCronJob( getSession().getSessionContext(), attributeValues );
	}
	
	public JnJLaUserAccountPreference createJnJLaUserAccountPreference(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( Jnjlab2bcoreConstants.TC.JNJLAUSERACCOUNTPREFERENCE );
			return (JnJLaUserAccountPreference)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating JnJLaUserAccountPreference : "+e.getMessage(), 0 );
		}
	}
	
	public JnJLaUserAccountPreference createJnJLaUserAccountPreference(final Map attributeValues)
	{
		return createJnJLaUserAccountPreference( getSession().getSessionContext(), attributeValues );
	}
	
	public JnjOrderArchivalJobConfig createJnjOrderArchivalJobConfig(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( Jnjlab2bcoreConstants.TC.JNJORDERARCHIVALJOBCONFIG );
			return (JnjOrderArchivalJobConfig)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating JnjOrderArchivalJobConfig : "+e.getMessage(), 0 );
		}
	}
	
	public JnjOrderArchivalJobConfig createJnjOrderArchivalJobConfig(final Map attributeValues)
	{
		return createJnjOrderArchivalJobConfig( getSession().getSessionContext(), attributeValues );
	}
	
	public JnjOrderArchivalResult createJnjOrderArchivalResult(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( Jnjlab2bcoreConstants.TC.JNJORDERARCHIVALRESULT );
			return (JnjOrderArchivalResult)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating JnjOrderArchivalResult : "+e.getMessage(), 0 );
		}
	}
	
	public JnjOrderArchivalResult createJnjOrderArchivalResult(final Map attributeValues)
	{
		return createJnjOrderArchivalResult( getSession().getSessionContext(), attributeValues );
	}
	
	public JnjOrderChannel createJnjOrderChannel(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( Jnjlab2bcoreConstants.TC.JNJORDERCHANNEL );
			return (JnjOrderChannel)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating JnjOrderChannel : "+e.getMessage(), 0 );
		}
	}
	
	public JnjOrderChannel createJnjOrderChannel(final Map attributeValues)
	{
		return createJnjOrderChannel( getSession().getSessionContext(), attributeValues );
	}
	
	public JnjOrderType createJnjOrderType(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( Jnjlab2bcoreConstants.TC.JNJORDERTYPE );
			return (JnjOrderType)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating JnjOrderType : "+e.getMessage(), 0 );
		}
	}
	
	public JnjOrderType createJnjOrderType(final Map attributeValues)
	{
		return createJnjOrderType( getSession().getSessionContext(), attributeValues );
	}
	
	public JnjProductCatalogsSyncJob createJnjProductCatalogsSyncJob(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( Jnjlab2bcoreConstants.TC.JNJPRODUCTCATALOGSSYNCJOB );
			return (JnjProductCatalogsSyncJob)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating JnjProductCatalogsSyncJob : "+e.getMessage(), 0 );
		}
	}
	
	public JnjProductCatalogsSyncJob createJnjProductCatalogsSyncJob(final Map attributeValues)
	{
		return createJnjProductCatalogsSyncJob( getSession().getSessionContext(), attributeValues );
	}
	
	public JnJProductSalesOrg createJnJProductSalesOrg(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( Jnjlab2bcoreConstants.TC.JNJPRODUCTSALESORG );
			return (JnJProductSalesOrg)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating JnJProductSalesOrg : "+e.getMessage(), 0 );
		}
	}
	
	public JnJProductSalesOrg createJnJProductSalesOrg(final Map attributeValues)
	{
		return createJnJProductSalesOrg( getSession().getSessionContext(), attributeValues );
	}
	
	public JnjSAPErrorTranslationTable createJnjSAPErrorTranslationTable(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( Jnjlab2bcoreConstants.TC.JNJSAPERRORTRANSLATIONTABLE );
			return (JnjSAPErrorTranslationTable)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating JnjSAPErrorTranslationTable : "+e.getMessage(), 0 );
		}
	}
	
	public JnjSAPErrorTranslationTable createJnjSAPErrorTranslationTable(final Map attributeValues)
	{
		return createJnjSAPErrorTranslationTable( getSession().getSessionContext(), attributeValues );
	}
	
	public JnjSellOutReport createJnjSellOutReport(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( Jnjlab2bcoreConstants.TC.JNJSELLOUTREPORT );
			return (JnjSellOutReport)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating JnjSellOutReport : "+e.getMessage(), 0 );
		}
	}
	
	public JnjSellOutReport createJnjSellOutReport(final Map attributeValues)
	{
		return createJnjSellOutReport( getSession().getSessionContext(), attributeValues );
	}
	
	public JnJUploadedInvoiceDate createJnJUploadedInvoiceDate(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( Jnjlab2bcoreConstants.TC.JNJUPLOADEDINVOICEDATE );
			return (JnJUploadedInvoiceDate)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating JnJUploadedInvoiceDate : "+e.getMessage(), 0 );
		}
	}
	
	public JnJUploadedInvoiceDate createJnJUploadedInvoiceDate(final Map attributeValues)
	{
		return createJnJUploadedInvoiceDate( getSession().getSessionContext(), attributeValues );
	}
	
	public JnjUploadOrder createJnjUploadOrder(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( Jnjlab2bcoreConstants.TC.JNJUPLOADORDER );
			return (JnjUploadOrder)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating JnjUploadOrder : "+e.getMessage(), 0 );
		}
	}
	
	public JnjUploadOrder createJnjUploadOrder(final Map attributeValues)
	{
		return createJnjUploadOrder( getSession().getSessionContext(), attributeValues );
	}
	
	public JnjUploadOrderSHA createJnjUploadOrderSHA(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( Jnjlab2bcoreConstants.TC.JNJUPLOADORDERSHA );
			return (JnjUploadOrderSHA)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating JnjUploadOrderSHA : "+e.getMessage(), 0 );
		}
	}
	
	public JnjUploadOrderSHA createJnjUploadOrderSHA(final Map attributeValues)
	{
		return createJnjUploadOrderSHA( getSession().getSessionContext(), attributeValues );
	}
	
	public LoadTranslation createLoadTranslation(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( Jnjlab2bcoreConstants.TC.LOADTRANSLATION );
			return (LoadTranslation)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating LoadTranslation : "+e.getMessage(), 0 );
		}
	}
	
	public LoadTranslation createLoadTranslation(final Map attributeValues)
	{
		return createLoadTranslation( getSession().getSessionContext(), attributeValues );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EmailAttachment.cronJob</code> attribute.
	 * @return the cronJob - assigned CronJob
	 */
	public CronJob getCronJob(final SessionContext ctx, final EmailAttachment item)
	{
		return (CronJob)item.getProperty( ctx, Jnjlab2bcoreConstants.Attributes.EmailAttachment.CRONJOB);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EmailAttachment.cronJob</code> attribute.
	 * @return the cronJob - assigned CronJob
	 */
	public CronJob getCronJob(final EmailAttachment item)
	{
		return getCronJob( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>EmailAttachment.cronJob</code> attribute. 
	 * @param value the cronJob - assigned CronJob
	 */
	protected void setCronJob(final SessionContext ctx, final EmailAttachment item, final CronJob value)
	{
		if ( ctx == null) 
		{
			throw new JaloInvalidParameterException( "ctx is null", 0 );
		}
		// initial-only attribute: make sure this attribute can be set during item creation only
		if ( ctx.getAttribute( "core.types.creation.initial") != Boolean.TRUE )
		{
			throw new JaloInvalidParameterException( "attribute '"+Jnjlab2bcoreConstants.Attributes.EmailAttachment.CRONJOB+"' is not changeable", 0 );
		}
		item.setProperty(ctx, Jnjlab2bcoreConstants.Attributes.EmailAttachment.CRONJOB,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>EmailAttachment.cronJob</code> attribute. 
	 * @param value the cronJob - assigned CronJob
	 */
	protected void setCronJob(final EmailAttachment item, final CronJob value)
	{
		setCronJob( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJB2bCustomer.customerCode</code> attribute.
	 * @return the customerCode
	 */
	public String getCustomerCode(final SessionContext ctx, final B2BCustomer item)
	{
		return (String)item.getProperty( ctx, Jnjlab2bcoreConstants.Attributes.JnJB2bCustomer.CUSTOMERCODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJB2bCustomer.customerCode</code> attribute.
	 * @return the customerCode
	 */
	public String getCustomerCode(final JnJB2bCustomer item)
	{
		return getCustomerCode( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJB2bCustomer.customerCode</code> attribute. 
	 * @param value the customerCode
	 */
	public void setCustomerCode(final SessionContext ctx, final B2BCustomer item, final String value)
	{
		item.setProperty(ctx, Jnjlab2bcoreConstants.Attributes.JnJB2bCustomer.CUSTOMERCODE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJB2bCustomer.customerCode</code> attribute. 
	 * @param value the customerCode
	 */
	public void setCustomerCode(final JnJB2bCustomer item, final String value)
	{
		setCustomerCode( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.customerFreightType</code> attribute.
	 * @return the customerFreightType
	 */
	public String getCustomerFreightType(final SessionContext ctx, final AbstractOrder item)
	{
		return (String)item.getProperty( ctx, Jnjlab2bcoreConstants.Attributes.AbstractOrder.CUSTOMERFREIGHTTYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.customerFreightType</code> attribute.
	 * @return the customerFreightType
	 */
	public String getCustomerFreightType(final AbstractOrder item)
	{
		return getCustomerFreightType( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.customerFreightType</code> attribute. 
	 * @param value the customerFreightType
	 */
	public void setCustomerFreightType(final SessionContext ctx, final AbstractOrder item, final String value)
	{
		item.setProperty(ctx, Jnjlab2bcoreConstants.Attributes.AbstractOrder.CUSTOMERFREIGHTTYPE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.customerFreightType</code> attribute. 
	 * @param value the customerFreightType
	 */
	public void setCustomerFreightType(final AbstractOrder item, final String value)
	{
		setCustomerFreightType( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.deliveredQuantity</code> attribute.
	 * @return the deliveredQuantity
	 */
	public Long getDeliveredQuantity(final SessionContext ctx, final AbstractOrderEntry item)
	{
		return (Long)item.getProperty( ctx, Jnjlab2bcoreConstants.Attributes.AbstractOrderEntry.DELIVEREDQUANTITY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.deliveredQuantity</code> attribute.
	 * @return the deliveredQuantity
	 */
	public Long getDeliveredQuantity(final AbstractOrderEntry item)
	{
		return getDeliveredQuantity( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.deliveredQuantity</code> attribute. 
	 * @return the deliveredQuantity
	 */
	public long getDeliveredQuantityAsPrimitive(final SessionContext ctx, final AbstractOrderEntry item)
	{
		Long value = getDeliveredQuantity( ctx,item );
		return value != null ? value.longValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.deliveredQuantity</code> attribute. 
	 * @return the deliveredQuantity
	 */
	public long getDeliveredQuantityAsPrimitive(final AbstractOrderEntry item)
	{
		return getDeliveredQuantityAsPrimitive( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.deliveredQuantity</code> attribute. 
	 * @param value the deliveredQuantity
	 */
	public void setDeliveredQuantity(final SessionContext ctx, final AbstractOrderEntry item, final Long value)
	{
		item.setProperty(ctx, Jnjlab2bcoreConstants.Attributes.AbstractOrderEntry.DELIVEREDQUANTITY,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.deliveredQuantity</code> attribute. 
	 * @param value the deliveredQuantity
	 */
	public void setDeliveredQuantity(final AbstractOrderEntry item, final Long value)
	{
		setDeliveredQuantity( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.deliveredQuantity</code> attribute. 
	 * @param value the deliveredQuantity
	 */
	public void setDeliveredQuantity(final SessionContext ctx, final AbstractOrderEntry item, final long value)
	{
		setDeliveredQuantity( ctx, item, Long.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.deliveredQuantity</code> attribute. 
	 * @param value the deliveredQuantity
	 */
	public void setDeliveredQuantity(final AbstractOrderEntry item, final long value)
	{
		setDeliveredQuantity( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.deliveryStatus</code> attribute.
	 * @return the deliveryStatus
	 */
	public String getDeliveryStatus(final SessionContext ctx, final AbstractOrderEntry item)
	{
		return (String)item.getProperty( ctx, Jnjlab2bcoreConstants.Attributes.AbstractOrderEntry.DELIVERYSTATUS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.deliveryStatus</code> attribute.
	 * @return the deliveryStatus
	 */
	public String getDeliveryStatus(final AbstractOrderEntry item)
	{
		return getDeliveryStatus( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.deliveryStatus</code> attribute. 
	 * @param value the deliveryStatus
	 */
	public void setDeliveryStatus(final SessionContext ctx, final AbstractOrderEntry item, final String value)
	{
		item.setProperty(ctx, Jnjlab2bcoreConstants.Attributes.AbstractOrderEntry.DELIVERYSTATUS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.deliveryStatus</code> attribute. 
	 * @param value the deliveryStatus
	 */
	public void setDeliveryStatus(final AbstractOrderEntry item, final String value)
	{
		setDeliveryStatus( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjContractEntry.eCCContractNum</code> attribute.
	 * @return the eCCContractNum
	 */
	public String getECCContractNum(final SessionContext ctx, final GenericItem item)
	{
		return (String)item.getProperty( ctx, Jnjlab2bcoreConstants.Attributes.JnjContractEntry.ECCCONTRACTNUM);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjContractEntry.eCCContractNum</code> attribute.
	 * @return the eCCContractNum
	 */
	public String getECCContractNum(final JnjContractEntry item)
	{
		return getECCContractNum( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjContractEntry.eCCContractNum</code> attribute. 
	 * @param value the eCCContractNum
	 */
	public void setECCContractNum(final SessionContext ctx, final GenericItem item, final String value)
	{
		item.setProperty(ctx, Jnjlab2bcoreConstants.Attributes.JnjContractEntry.ECCCONTRACTNUM,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjContractEntry.eCCContractNum</code> attribute. 
	 * @param value the eCCContractNum
	 */
	public void setECCContractNum(final JnjContractEntry item, final String value)
	{
		setECCContractNum( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Country.eligibleUserGroup</code> attribute.
	 * @return the eligibleUserGroup
	 */
	public UserGroup getEligibleUserGroup(final SessionContext ctx, final Country item)
	{
		return (UserGroup)item.getProperty( ctx, Jnjlab2bcoreConstants.Attributes.Country.ELIGIBLEUSERGROUP);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Country.eligibleUserGroup</code> attribute.
	 * @return the eligibleUserGroup
	 */
	public UserGroup getEligibleUserGroup(final Country item)
	{
		return getEligibleUserGroup( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Country.eligibleUserGroup</code> attribute. 
	 * @param value the eligibleUserGroup
	 */
	public void setEligibleUserGroup(final SessionContext ctx, final Country item, final UserGroup value)
	{
		item.setProperty(ctx, Jnjlab2bcoreConstants.Attributes.Country.ELIGIBLEUSERGROUP,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Country.eligibleUserGroup</code> attribute. 
	 * @param value the eligibleUserGroup
	 */
	public void setEligibleUserGroup(final Country item, final UserGroup value)
	{
		setEligibleUserGroup( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Order.emailProcess</code> attribute.
	 * @return the emailProcess
	 */
	public JnjLaConsolidatedEmailProcess getEmailProcess(final SessionContext ctx, final Order item)
	{
		return (JnjLaConsolidatedEmailProcess)item.getProperty( ctx, Jnjlab2bcoreConstants.Attributes.Order.EMAILPROCESS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Order.emailProcess</code> attribute.
	 * @return the emailProcess
	 */
	public JnjLaConsolidatedEmailProcess getEmailProcess(final Order item)
	{
		return getEmailProcess( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Order.emailProcess</code> attribute. 
	 * @param value the emailProcess
	 */
	public void setEmailProcess(final SessionContext ctx, final Order item, final JnjLaConsolidatedEmailProcess value)
	{
		item.setProperty(ctx, Jnjlab2bcoreConstants.Attributes.Order.EMAILPROCESS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Order.emailProcess</code> attribute. 
	 * @param value the emailProcess
	 */
	public void setEmailProcess(final Order item, final JnjLaConsolidatedEmailProcess value)
	{
		setEmailProcess( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.empenhoFilesFullPath</code> attribute.
	 * @return the empenhoFilesFullPath
	 */
	public List<String> getEmpenhoFilesFullPath(final SessionContext ctx, final AbstractOrder item)
	{
		List<String> coll = (List<String>)item.getProperty( ctx, Jnjlab2bcoreConstants.Attributes.AbstractOrder.EMPENHOFILESFULLPATH);
		return coll != null ? coll : Collections.EMPTY_LIST;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.empenhoFilesFullPath</code> attribute.
	 * @return the empenhoFilesFullPath
	 */
	public List<String> getEmpenhoFilesFullPath(final AbstractOrder item)
	{
		return getEmpenhoFilesFullPath( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.empenhoFilesFullPath</code> attribute. 
	 * @param value the empenhoFilesFullPath
	 */
	public void setEmpenhoFilesFullPath(final SessionContext ctx, final AbstractOrder item, final List<String> value)
	{
		item.setProperty(ctx, Jnjlab2bcoreConstants.Attributes.AbstractOrder.EMPENHOFILESFULLPATH,value == null || !value.isEmpty() ? value : null );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.empenhoFilesFullPath</code> attribute. 
	 * @param value the empenhoFilesFullPath
	 */
	public void setEmpenhoFilesFullPath(final AbstractOrder item, final List<String> value)
	{
		setEmpenhoFilesFullPath( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CronJob.errorRecords</code> attribute.
	 * @return the errorRecords - list of email attachments
	 */
	public List<EmailAttachment> getErrorRecords(final SessionContext ctx, final CronJob item)
	{
		return (List<EmailAttachment>)CRONJOBERRORRECORDSRELATIONERRORRECORDSHANDLER.getValues( ctx, item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CronJob.errorRecords</code> attribute.
	 * @return the errorRecords - list of email attachments
	 */
	public List<EmailAttachment> getErrorRecords(final CronJob item)
	{
		return getErrorRecords( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.expectedPrice</code> attribute.
	 * @return the expectedPrice
	 */
	public String getExpectedPrice(final SessionContext ctx, final AbstractOrderEntry item)
	{
		return (String)item.getProperty( ctx, Jnjlab2bcoreConstants.Attributes.AbstractOrderEntry.EXPECTEDPRICE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.expectedPrice</code> attribute.
	 * @return the expectedPrice
	 */
	public String getExpectedPrice(final AbstractOrderEntry item)
	{
		return getExpectedPrice( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.expectedPrice</code> attribute. 
	 * @param value the expectedPrice
	 */
	public void setExpectedPrice(final SessionContext ctx, final AbstractOrderEntry item, final String value)
	{
		item.setProperty(ctx, Jnjlab2bcoreConstants.Attributes.AbstractOrderEntry.EXPECTEDPRICE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.expectedPrice</code> attribute. 
	 * @param value the expectedPrice
	 */
	public void setExpectedPrice(final AbstractOrderEntry item, final String value)
	{
		setExpectedPrice( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.externalOrderRefNumber</code> attribute.
	 * @return the externalOrderRefNumber
	 */
	public String getExternalOrderRefNumber(final SessionContext ctx, final AbstractOrder item)
	{
		return (String)item.getProperty( ctx, Jnjlab2bcoreConstants.Attributes.AbstractOrder.EXTERNALORDERREFNUMBER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.externalOrderRefNumber</code> attribute.
	 * @return the externalOrderRefNumber
	 */
	public String getExternalOrderRefNumber(final AbstractOrder item)
	{
		return getExternalOrderRefNumber( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.externalOrderRefNumber</code> attribute. 
	 * @param value the externalOrderRefNumber
	 */
	public void setExternalOrderRefNumber(final SessionContext ctx, final AbstractOrder item, final String value)
	{
		item.setProperty(ctx, Jnjlab2bcoreConstants.Attributes.AbstractOrder.EXTERNALORDERREFNUMBER,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.externalOrderRefNumber</code> attribute. 
	 * @param value the externalOrderRefNumber
	 */
	public void setExternalOrderRefNumber(final AbstractOrder item, final String value)
	{
		setExternalOrderRefNumber( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.freeGoodScheduleLines</code> attribute.
	 * @return the freeGoodScheduleLines
	 */
	public List<JnjDeliverySchedule> getFreeGoodScheduleLines(final SessionContext ctx, final AbstractOrder item)
	{
		List<JnjDeliverySchedule> coll = (List<JnjDeliverySchedule>)item.getProperty( ctx, Jnjlab2bcoreConstants.Attributes.AbstractOrder.FREEGOODSCHEDULELINES);
		return coll != null ? coll : Collections.EMPTY_LIST;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.freeGoodScheduleLines</code> attribute.
	 * @return the freeGoodScheduleLines
	 */
	public List<JnjDeliverySchedule> getFreeGoodScheduleLines(final AbstractOrder item)
	{
		return getFreeGoodScheduleLines( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.freeGoodScheduleLines</code> attribute. 
	 * @param value the freeGoodScheduleLines
	 */
	public void setFreeGoodScheduleLines(final SessionContext ctx, final AbstractOrder item, final List<JnjDeliverySchedule> value)
	{
		item.setProperty(ctx, Jnjlab2bcoreConstants.Attributes.AbstractOrder.FREEGOODSCHEDULELINES,value == null || !value.isEmpty() ? value : null );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.freeGoodScheduleLines</code> attribute. 
	 * @param value the freeGoodScheduleLines
	 */
	public void setFreeGoodScheduleLines(final AbstractOrder item, final List<JnjDeliverySchedule> value)
	{
		setFreeGoodScheduleLines( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.freeGoodScheduleLines</code> attribute.
	 * @return the freeGoodScheduleLines
	 */
	public List<JnjDeliverySchedule> getFreeGoodScheduleLines(final SessionContext ctx, final AbstractOrderEntry item)
	{
		List<JnjDeliverySchedule> coll = (List<JnjDeliverySchedule>)item.getProperty( ctx, Jnjlab2bcoreConstants.Attributes.AbstractOrderEntry.FREEGOODSCHEDULELINES);
		return coll != null ? coll : Collections.EMPTY_LIST;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.freeGoodScheduleLines</code> attribute.
	 * @return the freeGoodScheduleLines
	 */
	public List<JnjDeliverySchedule> getFreeGoodScheduleLines(final AbstractOrderEntry item)
	{
		return getFreeGoodScheduleLines( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.freeGoodScheduleLines</code> attribute. 
	 * @param value the freeGoodScheduleLines
	 */
	public void setFreeGoodScheduleLines(final SessionContext ctx, final AbstractOrderEntry item, final List<JnjDeliverySchedule> value)
	{
		item.setProperty(ctx, Jnjlab2bcoreConstants.Attributes.AbstractOrderEntry.FREEGOODSCHEDULELINES,value == null || !value.isEmpty() ? value : null );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.freeGoodScheduleLines</code> attribute. 
	 * @param value the freeGoodScheduleLines
	 */
	public void setFreeGoodScheduleLines(final AbstractOrderEntry item, final List<JnjDeliverySchedule> value)
	{
		setFreeGoodScheduleLines( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.freeItem</code> attribute.
	 * @return the freeItem
	 */
	public String getFreeItem(final SessionContext ctx, final AbstractOrder item)
	{
		return (String)item.getProperty( ctx, Jnjlab2bcoreConstants.Attributes.AbstractOrder.FREEITEM);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.freeItem</code> attribute.
	 * @return the freeItem
	 */
	public String getFreeItem(final AbstractOrder item)
	{
		return getFreeItem( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.freeItem</code> attribute. 
	 * @param value the freeItem
	 */
	public void setFreeItem(final SessionContext ctx, final AbstractOrder item, final String value)
	{
		item.setProperty(ctx, Jnjlab2bcoreConstants.Attributes.AbstractOrder.FREEITEM,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.freeItem</code> attribute. 
	 * @param value the freeItem
	 */
	public void setFreeItem(final AbstractOrder item, final String value)
	{
		setFreeItem( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.freeItem</code> attribute.
	 * @return the freeItem
	 */
	public String getFreeItem(final SessionContext ctx, final AbstractOrderEntry item)
	{
		return (String)item.getProperty( ctx, Jnjlab2bcoreConstants.Attributes.AbstractOrderEntry.FREEITEM);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.freeItem</code> attribute.
	 * @return the freeItem
	 */
	public String getFreeItem(final AbstractOrderEntry item)
	{
		return getFreeItem( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.freeItem</code> attribute. 
	 * @param value the freeItem
	 */
	public void setFreeItem(final SessionContext ctx, final AbstractOrderEntry item, final String value)
	{
		item.setProperty(ctx, Jnjlab2bcoreConstants.Attributes.AbstractOrderEntry.FREEITEM,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.freeItem</code> attribute. 
	 * @param value the freeItem
	 */
	public void setFreeItem(final AbstractOrderEntry item, final String value)
	{
		setFreeItem( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.freeItemsAvailabilityStatus</code> attribute.
	 * @return the freeItemsAvailabilityStatus
	 */
	public String getFreeItemsAvailabilityStatus(final SessionContext ctx, final AbstractOrder item)
	{
		return (String)item.getProperty( ctx, Jnjlab2bcoreConstants.Attributes.AbstractOrder.FREEITEMSAVAILABILITYSTATUS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.freeItemsAvailabilityStatus</code> attribute.
	 * @return the freeItemsAvailabilityStatus
	 */
	public String getFreeItemsAvailabilityStatus(final AbstractOrder item)
	{
		return getFreeItemsAvailabilityStatus( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.freeItemsAvailabilityStatus</code> attribute. 
	 * @param value the freeItemsAvailabilityStatus
	 */
	public void setFreeItemsAvailabilityStatus(final SessionContext ctx, final AbstractOrder item, final String value)
	{
		item.setProperty(ctx, Jnjlab2bcoreConstants.Attributes.AbstractOrder.FREEITEMSAVAILABILITYSTATUS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.freeItemsAvailabilityStatus</code> attribute. 
	 * @param value the freeItemsAvailabilityStatus
	 */
	public void setFreeItemsAvailabilityStatus(final AbstractOrder item, final String value)
	{
		setFreeItemsAvailabilityStatus( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.freeItemsAvailabilityStatus</code> attribute.
	 * @return the freeItemsAvailabilityStatus
	 */
	public String getFreeItemsAvailabilityStatus(final SessionContext ctx, final AbstractOrderEntry item)
	{
		return (String)item.getProperty( ctx, Jnjlab2bcoreConstants.Attributes.AbstractOrderEntry.FREEITEMSAVAILABILITYSTATUS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.freeItemsAvailabilityStatus</code> attribute.
	 * @return the freeItemsAvailabilityStatus
	 */
	public String getFreeItemsAvailabilityStatus(final AbstractOrderEntry item)
	{
		return getFreeItemsAvailabilityStatus( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.freeItemsAvailabilityStatus</code> attribute. 
	 * @param value the freeItemsAvailabilityStatus
	 */
	public void setFreeItemsAvailabilityStatus(final SessionContext ctx, final AbstractOrderEntry item, final String value)
	{
		item.setProperty(ctx, Jnjlab2bcoreConstants.Attributes.AbstractOrderEntry.FREEITEMSAVAILABILITYSTATUS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.freeItemsAvailabilityStatus</code> attribute. 
	 * @param value the freeItemsAvailabilityStatus
	 */
	public void setFreeItemsAvailabilityStatus(final AbstractOrderEntry item, final String value)
	{
		setFreeItemsAvailabilityStatus( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.freeItemsLogicOriginalQuantity</code> attribute.
	 * @return the freeItemsLogicOriginalQuantity - Stores the original quantity ordered for entries with inclusive and exclusive logic for free items.
	 */
	public String getFreeItemsLogicOriginalQuantity(final SessionContext ctx, final AbstractOrderEntry item)
	{
		return (String)item.getProperty( ctx, Jnjlab2bcoreConstants.Attributes.AbstractOrderEntry.FREEITEMSLOGICORIGINALQUANTITY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.freeItemsLogicOriginalQuantity</code> attribute.
	 * @return the freeItemsLogicOriginalQuantity - Stores the original quantity ordered for entries with inclusive and exclusive logic for free items.
	 */
	public String getFreeItemsLogicOriginalQuantity(final AbstractOrderEntry item)
	{
		return getFreeItemsLogicOriginalQuantity( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.freeItemsLogicOriginalQuantity</code> attribute. 
	 * @param value the freeItemsLogicOriginalQuantity - Stores the original quantity ordered for entries with inclusive and exclusive logic for free items.
	 */
	public void setFreeItemsLogicOriginalQuantity(final SessionContext ctx, final AbstractOrderEntry item, final String value)
	{
		item.setProperty(ctx, Jnjlab2bcoreConstants.Attributes.AbstractOrderEntry.FREEITEMSLOGICORIGINALQUANTITY,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.freeItemsLogicOriginalQuantity</code> attribute. 
	 * @param value the freeItemsLogicOriginalQuantity - Stores the original quantity ordered for entries with inclusive and exclusive logic for free items.
	 */
	public void setFreeItemsLogicOriginalQuantity(final AbstractOrderEntry item, final String value)
	{
		setFreeItemsLogicOriginalQuantity( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.freeItemsQuanity</code> attribute.
	 * @return the freeItemsQuanity
	 */
	public String getFreeItemsQuanity(final SessionContext ctx, final AbstractOrder item)
	{
		return (String)item.getProperty( ctx, Jnjlab2bcoreConstants.Attributes.AbstractOrder.FREEITEMSQUANITY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.freeItemsQuanity</code> attribute.
	 * @return the freeItemsQuanity
	 */
	public String getFreeItemsQuanity(final AbstractOrder item)
	{
		return getFreeItemsQuanity( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.freeItemsQuanity</code> attribute. 
	 * @param value the freeItemsQuanity
	 */
	public void setFreeItemsQuanity(final SessionContext ctx, final AbstractOrder item, final String value)
	{
		item.setProperty(ctx, Jnjlab2bcoreConstants.Attributes.AbstractOrder.FREEITEMSQUANITY,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.freeItemsQuanity</code> attribute. 
	 * @param value the freeItemsQuanity
	 */
	public void setFreeItemsQuanity(final AbstractOrder item, final String value)
	{
		setFreeItemsQuanity( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.freeItemsQuanity</code> attribute.
	 * @return the freeItemsQuanity
	 */
	public String getFreeItemsQuanity(final SessionContext ctx, final AbstractOrderEntry item)
	{
		return (String)item.getProperty( ctx, Jnjlab2bcoreConstants.Attributes.AbstractOrderEntry.FREEITEMSQUANITY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.freeItemsQuanity</code> attribute.
	 * @return the freeItemsQuanity
	 */
	public String getFreeItemsQuanity(final AbstractOrderEntry item)
	{
		return getFreeItemsQuanity( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.freeItemsQuanity</code> attribute. 
	 * @param value the freeItemsQuanity
	 */
	public void setFreeItemsQuanity(final SessionContext ctx, final AbstractOrderEntry item, final String value)
	{
		item.setProperty(ctx, Jnjlab2bcoreConstants.Attributes.AbstractOrderEntry.FREEITEMSQUANITY,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.freeItemsQuanity</code> attribute. 
	 * @param value the freeItemsQuanity
	 */
	public void setFreeItemsQuanity(final AbstractOrderEntry item, final String value)
	{
		setFreeItemsQuanity( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.freeItemUnit</code> attribute.
	 * @return the freeItemUnit
	 */
	public String getFreeItemUnit(final SessionContext ctx, final AbstractOrder item)
	{
		return (String)item.getProperty( ctx, Jnjlab2bcoreConstants.Attributes.AbstractOrder.FREEITEMUNIT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.freeItemUnit</code> attribute.
	 * @return the freeItemUnit
	 */
	public String getFreeItemUnit(final AbstractOrder item)
	{
		return getFreeItemUnit( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.freeItemUnit</code> attribute. 
	 * @param value the freeItemUnit
	 */
	public void setFreeItemUnit(final SessionContext ctx, final AbstractOrder item, final String value)
	{
		item.setProperty(ctx, Jnjlab2bcoreConstants.Attributes.AbstractOrder.FREEITEMUNIT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.freeItemUnit</code> attribute. 
	 * @param value the freeItemUnit
	 */
	public void setFreeItemUnit(final AbstractOrder item, final String value)
	{
		setFreeItemUnit( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.freeItemUnit</code> attribute.
	 * @return the freeItemUnit
	 */
	public String getFreeItemUnit(final SessionContext ctx, final AbstractOrderEntry item)
	{
		return (String)item.getProperty( ctx, Jnjlab2bcoreConstants.Attributes.AbstractOrderEntry.FREEITEMUNIT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.freeItemUnit</code> attribute.
	 * @return the freeItemUnit
	 */
	public String getFreeItemUnit(final AbstractOrderEntry item)
	{
		return getFreeItemUnit( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.freeItemUnit</code> attribute. 
	 * @param value the freeItemUnit
	 */
	public void setFreeItemUnit(final SessionContext ctx, final AbstractOrderEntry item, final String value)
	{
		item.setProperty(ctx, Jnjlab2bcoreConstants.Attributes.AbstractOrderEntry.FREEITEMUNIT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.freeItemUnit</code> attribute. 
	 * @param value the freeItemUnit
	 */
	public void setFreeItemUnit(final AbstractOrderEntry item, final String value)
	{
		setFreeItemUnit( getSession().getSessionContext(), item, value );
	}
	
	@Override
	public String getName()
	{
		return Jnjlab2bcoreConstants.EXTENSIONNAME;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.gTSHold</code> attribute.
	 * @return the gTSHold
	 */
	public String getGTSHold(final SessionContext ctx, final AbstractOrderEntry item)
	{
		return (String)item.getProperty( ctx, Jnjlab2bcoreConstants.Attributes.AbstractOrderEntry.GTSHOLD);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.gTSHold</code> attribute.
	 * @return the gTSHold
	 */
	public String getGTSHold(final AbstractOrderEntry item)
	{
		return getGTSHold( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.gTSHold</code> attribute. 
	 * @param value the gTSHold
	 */
	public void setGTSHold(final SessionContext ctx, final AbstractOrderEntry item, final String value)
	{
		item.setProperty(ctx, Jnjlab2bcoreConstants.Attributes.AbstractOrderEntry.GTSHOLD,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.gTSHold</code> attribute. 
	 * @param value the gTSHold
	 */
	public void setGTSHold(final AbstractOrderEntry item, final String value)
	{
		setGTSHold( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjUomConversion.height</code> attribute.
	 * @return the height
	 */
	public Double getHeight(final SessionContext ctx, final GenericItem item)
	{
		return (Double)item.getProperty( ctx, Jnjlab2bcoreConstants.Attributes.JnjUomConversion.HEIGHT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjUomConversion.height</code> attribute.
	 * @return the height
	 */
	public Double getHeight(final JnjUomConversion item)
	{
		return getHeight( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjUomConversion.height</code> attribute. 
	 * @return the height
	 */
	public double getHeightAsPrimitive(final SessionContext ctx, final JnjUomConversion item)
	{
		Double value = getHeight( ctx,item );
		return value != null ? value.doubleValue() : 0.0d;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjUomConversion.height</code> attribute. 
	 * @return the height
	 */
	public double getHeightAsPrimitive(final JnjUomConversion item)
	{
		return getHeightAsPrimitive( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjUomConversion.height</code> attribute. 
	 * @param value the height
	 */
	public void setHeight(final SessionContext ctx, final GenericItem item, final Double value)
	{
		item.setProperty(ctx, Jnjlab2bcoreConstants.Attributes.JnjUomConversion.HEIGHT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjUomConversion.height</code> attribute. 
	 * @param value the height
	 */
	public void setHeight(final JnjUomConversion item, final Double value)
	{
		setHeight( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjUomConversion.height</code> attribute. 
	 * @param value the height
	 */
	public void setHeight(final SessionContext ctx, final JnjUomConversion item, final double value)
	{
		setHeight( ctx, item, Double.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjUomConversion.height</code> attribute. 
	 * @param value the height
	 */
	public void setHeight(final JnjUomConversion item, final double value)
	{
		setHeight( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.holdCreditCardFlag</code> attribute.
	 * @return the holdCreditCardFlag
	 */
	public Boolean isHoldCreditCardFlag(final SessionContext ctx, final AbstractOrder item)
	{
		return (Boolean)item.getProperty( ctx, Jnjlab2bcoreConstants.Attributes.AbstractOrder.HOLDCREDITCARDFLAG);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.holdCreditCardFlag</code> attribute.
	 * @return the holdCreditCardFlag
	 */
	public Boolean isHoldCreditCardFlag(final AbstractOrder item)
	{
		return isHoldCreditCardFlag( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.holdCreditCardFlag</code> attribute. 
	 * @return the holdCreditCardFlag
	 */
	public boolean isHoldCreditCardFlagAsPrimitive(final SessionContext ctx, final AbstractOrder item)
	{
		Boolean value = isHoldCreditCardFlag( ctx,item );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.holdCreditCardFlag</code> attribute. 
	 * @return the holdCreditCardFlag
	 */
	public boolean isHoldCreditCardFlagAsPrimitive(final AbstractOrder item)
	{
		return isHoldCreditCardFlagAsPrimitive( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.holdCreditCardFlag</code> attribute. 
	 * @param value the holdCreditCardFlag
	 */
	public void setHoldCreditCardFlag(final SessionContext ctx, final AbstractOrder item, final Boolean value)
	{
		item.setProperty(ctx, Jnjlab2bcoreConstants.Attributes.AbstractOrder.HOLDCREDITCARDFLAG,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.holdCreditCardFlag</code> attribute. 
	 * @param value the holdCreditCardFlag
	 */
	public void setHoldCreditCardFlag(final AbstractOrder item, final Boolean value)
	{
		setHoldCreditCardFlag( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.holdCreditCardFlag</code> attribute. 
	 * @param value the holdCreditCardFlag
	 */
	public void setHoldCreditCardFlag(final SessionContext ctx, final AbstractOrder item, final boolean value)
	{
		setHoldCreditCardFlag( ctx, item, Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.holdCreditCardFlag</code> attribute. 
	 * @param value the holdCreditCardFlag
	 */
	public void setHoldCreditCardFlag(final AbstractOrder item, final boolean value)
	{
		setHoldCreditCardFlag( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.indirectCustomer</code> attribute.
	 * @return the indirectCustomer
	 */
	public String getIndirectCustomer(final SessionContext ctx, final AbstractOrder item)
	{
		return (String)item.getProperty( ctx, Jnjlab2bcoreConstants.Attributes.AbstractOrder.INDIRECTCUSTOMER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.indirectCustomer</code> attribute.
	 * @return the indirectCustomer
	 */
	public String getIndirectCustomer(final AbstractOrder item)
	{
		return getIndirectCustomer( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.indirectCustomer</code> attribute. 
	 * @param value the indirectCustomer
	 */
	public void setIndirectCustomer(final SessionContext ctx, final AbstractOrder item, final String value)
	{
		item.setProperty(ctx, Jnjlab2bcoreConstants.Attributes.AbstractOrder.INDIRECTCUSTOMER,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.indirectCustomer</code> attribute. 
	 * @param value the indirectCustomer
	 */
	public void setIndirectCustomer(final AbstractOrder item, final String value)
	{
		setIndirectCustomer( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.indirectCustomerName</code> attribute.
	 * @return the indirectCustomerName
	 */
	public String getIndirectCustomerName(final SessionContext ctx, final AbstractOrderEntry item)
	{
		return (String)item.getProperty( ctx, Jnjlab2bcoreConstants.Attributes.AbstractOrderEntry.INDIRECTCUSTOMERNAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.indirectCustomerName</code> attribute.
	 * @return the indirectCustomerName
	 */
	public String getIndirectCustomerName(final AbstractOrderEntry item)
	{
		return getIndirectCustomerName( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.indirectCustomerName</code> attribute. 
	 * @param value the indirectCustomerName
	 */
	public void setIndirectCustomerName(final SessionContext ctx, final AbstractOrderEntry item, final String value)
	{
		item.setProperty(ctx, Jnjlab2bcoreConstants.Attributes.AbstractOrderEntry.INDIRECTCUSTOMERNAME,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.indirectCustomerName</code> attribute. 
	 * @param value the indirectCustomerName
	 */
	public void setIndirectCustomerName(final AbstractOrderEntry item, final String value)
	{
		setIndirectCustomerName( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.indirectPayer</code> attribute.
	 * @return the indirectPayer
	 */
	public String getIndirectPayer(final SessionContext ctx, final AbstractOrder item)
	{
		return (String)item.getProperty( ctx, Jnjlab2bcoreConstants.Attributes.AbstractOrder.INDIRECTPAYER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.indirectPayer</code> attribute.
	 * @return the indirectPayer
	 */
	public String getIndirectPayer(final AbstractOrder item)
	{
		return getIndirectPayer( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.indirectPayer</code> attribute. 
	 * @param value the indirectPayer
	 */
	public void setIndirectPayer(final SessionContext ctx, final AbstractOrder item, final String value)
	{
		item.setProperty(ctx, Jnjlab2bcoreConstants.Attributes.AbstractOrder.INDIRECTPAYER,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.indirectPayer</code> attribute. 
	 * @param value the indirectPayer
	 */
	public void setIndirectPayer(final AbstractOrder item, final String value)
	{
		setIndirectPayer( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.indirectPayer</code> attribute.
	 * @return the indirectPayer
	 */
	public String getIndirectPayer(final SessionContext ctx, final AbstractOrderEntry item)
	{
		return (String)item.getProperty( ctx, Jnjlab2bcoreConstants.Attributes.AbstractOrderEntry.INDIRECTPAYER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.indirectPayer</code> attribute.
	 * @return the indirectPayer
	 */
	public String getIndirectPayer(final AbstractOrderEntry item)
	{
		return getIndirectPayer( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.indirectPayer</code> attribute. 
	 * @param value the indirectPayer
	 */
	public void setIndirectPayer(final SessionContext ctx, final AbstractOrderEntry item, final String value)
	{
		item.setProperty(ctx, Jnjlab2bcoreConstants.Attributes.AbstractOrderEntry.INDIRECTPAYER,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.indirectPayer</code> attribute. 
	 * @param value the indirectPayer
	 */
	public void setIndirectPayer(final AbstractOrderEntry item, final String value)
	{
		setIndirectPayer( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.indirectPayerName</code> attribute.
	 * @return the indirectPayerName
	 */
	public String getIndirectPayerName(final SessionContext ctx, final AbstractOrderEntry item)
	{
		return (String)item.getProperty( ctx, Jnjlab2bcoreConstants.Attributes.AbstractOrderEntry.INDIRECTPAYERNAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.indirectPayerName</code> attribute.
	 * @return the indirectPayerName
	 */
	public String getIndirectPayerName(final AbstractOrderEntry item)
	{
		return getIndirectPayerName( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.indirectPayerName</code> attribute. 
	 * @param value the indirectPayerName
	 */
	public void setIndirectPayerName(final SessionContext ctx, final AbstractOrderEntry item, final String value)
	{
		item.setProperty(ctx, Jnjlab2bcoreConstants.Attributes.AbstractOrderEntry.INDIRECTPAYERNAME,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.indirectPayerName</code> attribute. 
	 * @param value the indirectPayerName
	 */
	public void setIndirectPayerName(final AbstractOrderEntry item, final String value)
	{
		setIndirectPayerName( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.invoicedQuantity</code> attribute.
	 * @return the invoicedQuantity
	 */
	public Long getInvoicedQuantity(final SessionContext ctx, final AbstractOrderEntry item)
	{
		return (Long)item.getProperty( ctx, Jnjlab2bcoreConstants.Attributes.AbstractOrderEntry.INVOICEDQUANTITY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.invoicedQuantity</code> attribute.
	 * @return the invoicedQuantity
	 */
	public Long getInvoicedQuantity(final AbstractOrderEntry item)
	{
		return getInvoicedQuantity( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.invoicedQuantity</code> attribute. 
	 * @return the invoicedQuantity
	 */
	public long getInvoicedQuantityAsPrimitive(final SessionContext ctx, final AbstractOrderEntry item)
	{
		Long value = getInvoicedQuantity( ctx,item );
		return value != null ? value.longValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.invoicedQuantity</code> attribute. 
	 * @return the invoicedQuantity
	 */
	public long getInvoicedQuantityAsPrimitive(final AbstractOrderEntry item)
	{
		return getInvoicedQuantityAsPrimitive( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.invoicedQuantity</code> attribute. 
	 * @param value the invoicedQuantity
	 */
	public void setInvoicedQuantity(final SessionContext ctx, final AbstractOrderEntry item, final Long value)
	{
		item.setProperty(ctx, Jnjlab2bcoreConstants.Attributes.AbstractOrderEntry.INVOICEDQUANTITY,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.invoicedQuantity</code> attribute. 
	 * @param value the invoicedQuantity
	 */
	public void setInvoicedQuantity(final AbstractOrderEntry item, final Long value)
	{
		setInvoicedQuantity( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.invoicedQuantity</code> attribute. 
	 * @param value the invoicedQuantity
	 */
	public void setInvoicedQuantity(final SessionContext ctx, final AbstractOrderEntry item, final long value)
	{
		setInvoicedQuantity( ctx, item, Long.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.invoicedQuantity</code> attribute. 
	 * @param value the invoicedQuantity
	 */
	public void setInvoicedQuantity(final AbstractOrderEntry item, final long value)
	{
		setInvoicedQuantity( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.isContractCart</code> attribute.
	 * @return the isContractCart
	 */
	public Boolean isIsContractCart(final SessionContext ctx, final AbstractOrder item)
	{
		return (Boolean)item.getProperty( ctx, Jnjlab2bcoreConstants.Attributes.AbstractOrder.ISCONTRACTCART);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.isContractCart</code> attribute.
	 * @return the isContractCart
	 */
	public Boolean isIsContractCart(final AbstractOrder item)
	{
		return isIsContractCart( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.isContractCart</code> attribute. 
	 * @return the isContractCart
	 */
	public boolean isIsContractCartAsPrimitive(final SessionContext ctx, final AbstractOrder item)
	{
		Boolean value = isIsContractCart( ctx,item );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.isContractCart</code> attribute. 
	 * @return the isContractCart
	 */
	public boolean isIsContractCartAsPrimitive(final AbstractOrder item)
	{
		return isIsContractCartAsPrimitive( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.isContractCart</code> attribute. 
	 * @param value the isContractCart
	 */
	public void setIsContractCart(final SessionContext ctx, final AbstractOrder item, final Boolean value)
	{
		item.setProperty(ctx, Jnjlab2bcoreConstants.Attributes.AbstractOrder.ISCONTRACTCART,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.isContractCart</code> attribute. 
	 * @param value the isContractCart
	 */
	public void setIsContractCart(final AbstractOrder item, final Boolean value)
	{
		setIsContractCart( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.isContractCart</code> attribute. 
	 * @param value the isContractCart
	 */
	public void setIsContractCart(final SessionContext ctx, final AbstractOrder item, final boolean value)
	{
		setIsContractCart( ctx, item, Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.isContractCart</code> attribute. 
	 * @param value the isContractCart
	 */
	public void setIsContractCart(final AbstractOrder item, final boolean value)
	{
		setIsContractCart( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjUomConversion.isoCode</code> attribute.
	 * @return the isoCode - Contains the incomming ISO Code for the Unit.
	 */
	public String getIsoCode(final SessionContext ctx, final GenericItem item)
	{
		return (String)item.getProperty( ctx, Jnjlab2bcoreConstants.Attributes.JnjUomConversion.ISOCODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjUomConversion.isoCode</code> attribute.
	 * @return the isoCode - Contains the incomming ISO Code for the Unit.
	 */
	public String getIsoCode(final JnjUomConversion item)
	{
		return getIsoCode( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjUomConversion.isoCode</code> attribute. 
	 * @param value the isoCode - Contains the incomming ISO Code for the Unit.
	 */
	public void setIsoCode(final SessionContext ctx, final GenericItem item, final String value)
	{
		item.setProperty(ctx, Jnjlab2bcoreConstants.Attributes.JnjUomConversion.ISOCODE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjUomConversion.isoCode</code> attribute. 
	 * @param value the isoCode - Contains the incomming ISO Code for the Unit.
	 */
	public void setIsoCode(final JnjUomConversion item, final String value)
	{
		setIsoCode( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BUnit.jnjContracts</code> attribute.
	 * @return the jnjContracts
	 */
	public Set<JnjContract> getJnjContracts(final SessionContext ctx, final B2BUnit item)
	{
		final List<JnjContract> items = item.getLinkedItems( 
			ctx,
			true,
			Jnjlab2bcoreConstants.Relations.JNJB2BUNITTOCONTRACTREL,
			"JnjContract",
			null,
			false,
			false
		);
		return new LinkedHashSet<JnjContract>(items);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BUnit.jnjContracts</code> attribute.
	 * @return the jnjContracts
	 */
	public Set<JnjContract> getJnjContracts(final B2BUnit item)
	{
		return getJnjContracts( getSession().getSessionContext(), item );
	}
	
	public long getJnjContractsCount(final SessionContext ctx, final B2BUnit item)
	{
		return item.getLinkedItemsCount(
			ctx,
			true,
			Jnjlab2bcoreConstants.Relations.JNJB2BUNITTOCONTRACTREL,
			"JnjContract",
			null
		);
	}
	
	public long getJnjContractsCount(final B2BUnit item)
	{
		return getJnjContractsCount( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BUnit.jnjContracts</code> attribute. 
	 * @param value the jnjContracts
	 */
	public void setJnjContracts(final SessionContext ctx, final B2BUnit item, final Set<JnjContract> value)
	{
		item.setLinkedItems( 
			ctx,
			true,
			Jnjlab2bcoreConstants.Relations.JNJB2BUNITTOCONTRACTREL,
			null,
			value,
			false,
			false,
			Utilities.getMarkModifiedOverride(JNJB2BUNITTOCONTRACTREL_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BUnit.jnjContracts</code> attribute. 
	 * @param value the jnjContracts
	 */
	public void setJnjContracts(final B2BUnit item, final Set<JnjContract> value)
	{
		setJnjContracts( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to jnjContracts. 
	 * @param value the item to add to jnjContracts
	 */
	public void addToJnjContracts(final SessionContext ctx, final B2BUnit item, final JnjContract value)
	{
		item.addLinkedItems( 
			ctx,
			true,
			Jnjlab2bcoreConstants.Relations.JNJB2BUNITTOCONTRACTREL,
			null,
			Collections.singletonList(value),
			false,
			false,
			Utilities.getMarkModifiedOverride(JNJB2BUNITTOCONTRACTREL_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to jnjContracts. 
	 * @param value the item to add to jnjContracts
	 */
	public void addToJnjContracts(final B2BUnit item, final JnjContract value)
	{
		addToJnjContracts( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from jnjContracts. 
	 * @param value the item to remove from jnjContracts
	 */
	public void removeFromJnjContracts(final SessionContext ctx, final B2BUnit item, final JnjContract value)
	{
		item.removeLinkedItems( 
			ctx,
			true,
			Jnjlab2bcoreConstants.Relations.JNJB2BUNITTOCONTRACTREL,
			null,
			Collections.singletonList(value),
			false,
			false,
			Utilities.getMarkModifiedOverride(JNJB2BUNITTOCONTRACTREL_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from jnjContracts. 
	 * @param value the item to remove from jnjContracts
	 */
	public void removeFromJnjContracts(final B2BUnit item, final JnjContract value)
	{
		removeFromJnjContracts( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Order.jnjlaInvoices</code> attribute.
	 * @return the jnjlaInvoices
	 */
	public Collection<JnJInvoiceOrder> getJnjlaInvoices(final SessionContext ctx, final Order item)
	{
		return JNJINVOICE_ORDERRELATIONJNJLAINVOICESHANDLER.getValues( ctx, item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Order.jnjlaInvoices</code> attribute.
	 * @return the jnjlaInvoices
	 */
	public Collection<JnJInvoiceOrder> getJnjlaInvoices(final Order item)
	{
		return getJnjlaInvoices( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Order.jnjlaInvoices</code> attribute. 
	 * @param value the jnjlaInvoices
	 */
	public void setJnjlaInvoices(final SessionContext ctx, final Order item, final Collection<JnJInvoiceOrder> value)
	{
		JNJINVOICE_ORDERRELATIONJNJLAINVOICESHANDLER.setValues( ctx, item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Order.jnjlaInvoices</code> attribute. 
	 * @param value the jnjlaInvoices
	 */
	public void setJnjlaInvoices(final Order item, final Collection<JnJInvoiceOrder> value)
	{
		setJnjlaInvoices( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to jnjlaInvoices. 
	 * @param value the item to add to jnjlaInvoices
	 */
	public void addToJnjlaInvoices(final SessionContext ctx, final Order item, final JnJInvoiceOrder value)
	{
		JNJINVOICE_ORDERRELATIONJNJLAINVOICESHANDLER.addValue( ctx, item, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to jnjlaInvoices. 
	 * @param value the item to add to jnjlaInvoices
	 */
	public void addToJnjlaInvoices(final Order item, final JnJInvoiceOrder value)
	{
		addToJnjlaInvoices( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from jnjlaInvoices. 
	 * @param value the item to remove from jnjlaInvoices
	 */
	public void removeFromJnjlaInvoices(final SessionContext ctx, final Order item, final JnJInvoiceOrder value)
	{
		JNJINVOICE_ORDERRELATIONJNJLAINVOICESHANDLER.removeValue( ctx, item, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from jnjlaInvoices. 
	 * @param value the item to remove from jnjlaInvoices
	 */
	public void removeFromJnjlaInvoices(final Order item, final JnJInvoiceOrder value)
	{
		removeFromJnjlaInvoices( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.jnjOrderType</code> attribute.
	 * @return the jnjOrderType
	 */
	public JnjOrderType getJnjOrderType(final SessionContext ctx, final AbstractOrder item)
	{
		return (JnjOrderType)item.getProperty( ctx, Jnjlab2bcoreConstants.Attributes.AbstractOrder.JNJORDERTYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.jnjOrderType</code> attribute.
	 * @return the jnjOrderType
	 */
	public JnjOrderType getJnjOrderType(final AbstractOrder item)
	{
		return getJnjOrderType( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.jnjOrderType</code> attribute. 
	 * @param value the jnjOrderType
	 */
	public void setJnjOrderType(final SessionContext ctx, final AbstractOrder item, final JnjOrderType value)
	{
		item.setProperty(ctx, Jnjlab2bcoreConstants.Attributes.AbstractOrder.JNJORDERTYPE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.jnjOrderType</code> attribute. 
	 * @param value the jnjOrderType
	 */
	public void setJnjOrderType(final AbstractOrder item, final JnjOrderType value)
	{
		setJnjOrderType( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjUomConversion.length</code> attribute.
	 * @return the length
	 */
	public Double getLength(final SessionContext ctx, final GenericItem item)
	{
		return (Double)item.getProperty( ctx, Jnjlab2bcoreConstants.Attributes.JnjUomConversion.LENGTH);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjUomConversion.length</code> attribute.
	 * @return the length
	 */
	public Double getLength(final JnjUomConversion item)
	{
		return getLength( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjUomConversion.length</code> attribute. 
	 * @return the length
	 */
	public double getLengthAsPrimitive(final SessionContext ctx, final JnjUomConversion item)
	{
		Double value = getLength( ctx,item );
		return value != null ? value.doubleValue() : 0.0d;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjUomConversion.length</code> attribute. 
	 * @return the length
	 */
	public double getLengthAsPrimitive(final JnjUomConversion item)
	{
		return getLengthAsPrimitive( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjUomConversion.length</code> attribute. 
	 * @param value the length
	 */
	public void setLength(final SessionContext ctx, final GenericItem item, final Double value)
	{
		item.setProperty(ctx, Jnjlab2bcoreConstants.Attributes.JnjUomConversion.LENGTH,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjUomConversion.length</code> attribute. 
	 * @param value the length
	 */
	public void setLength(final JnjUomConversion item, final Double value)
	{
		setLength( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjUomConversion.length</code> attribute. 
	 * @param value the length
	 */
	public void setLength(final SessionContext ctx, final JnjUomConversion item, final double value)
	{
		setLength( ctx, item, Double.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjUomConversion.length</code> attribute. 
	 * @param value the length
	 */
	public void setLength(final JnjUomConversion item, final double value)
	{
		setLength( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.lineInvoiceStatus</code> attribute.
	 * @return the lineInvoiceStatus
	 */
	public String getLineInvoiceStatus(final SessionContext ctx, final AbstractOrderEntry item)
	{
		return (String)item.getProperty( ctx, Jnjlab2bcoreConstants.Attributes.AbstractOrderEntry.LINEINVOICESTATUS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.lineInvoiceStatus</code> attribute.
	 * @return the lineInvoiceStatus
	 */
	public String getLineInvoiceStatus(final AbstractOrderEntry item)
	{
		return getLineInvoiceStatus( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.lineInvoiceStatus</code> attribute. 
	 * @param value the lineInvoiceStatus
	 */
	public void setLineInvoiceStatus(final SessionContext ctx, final AbstractOrderEntry item, final String value)
	{
		item.setProperty(ctx, Jnjlab2bcoreConstants.Attributes.AbstractOrderEntry.LINEINVOICESTATUS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.lineInvoiceStatus</code> attribute. 
	 * @param value the lineInvoiceStatus
	 */
	public void setLineInvoiceStatus(final AbstractOrderEntry item, final String value)
	{
		setLineInvoiceStatus( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.lineOverallStatus</code> attribute.
	 * @return the lineOverallStatus
	 */
	public String getLineOverallStatus(final SessionContext ctx, final AbstractOrderEntry item)
	{
		return (String)item.getProperty( ctx, Jnjlab2bcoreConstants.Attributes.AbstractOrderEntry.LINEOVERALLSTATUS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.lineOverallStatus</code> attribute.
	 * @return the lineOverallStatus
	 */
	public String getLineOverallStatus(final AbstractOrderEntry item)
	{
		return getLineOverallStatus( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.lineOverallStatus</code> attribute. 
	 * @param value the lineOverallStatus
	 */
	public void setLineOverallStatus(final SessionContext ctx, final AbstractOrderEntry item, final String value)
	{
		item.setProperty(ctx, Jnjlab2bcoreConstants.Attributes.AbstractOrderEntry.LINEOVERALLSTATUS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.lineOverallStatus</code> attribute. 
	 * @param value the lineOverallStatus
	 */
	public void setLineOverallStatus(final AbstractOrderEntry item, final String value)
	{
		setLineOverallStatus( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJInvoiceOrder.order</code> attribute.
	 * @return the order
	 */
	public Order getOrder(final SessionContext ctx, final GenericItem item)
	{
		return (Order)item.getProperty( ctx, Jnjlab2bcoreConstants.Attributes.JnJInvoiceOrder.ORDER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJInvoiceOrder.order</code> attribute.
	 * @return the order
	 */
	public Order getOrder(final JnJInvoiceOrder item)
	{
		return getOrder( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJInvoiceOrder.order</code> attribute. 
	 * @param value the order
	 */
	public void setOrder(final SessionContext ctx, final GenericItem item, final Order value)
	{
		item.setProperty(ctx, Jnjlab2bcoreConstants.Attributes.JnJInvoiceOrder.ORDER,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJInvoiceOrder.order</code> attribute. 
	 * @param value the order
	 */
	public void setOrder(final JnJInvoiceOrder item, final Order value)
	{
		setOrder( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.orderChannel</code> attribute.
	 * @return the orderChannel
	 */
	public JnjOrderChannel getOrderChannel(final SessionContext ctx, final AbstractOrder item)
	{
		return (JnjOrderChannel)item.getProperty( ctx, Jnjlab2bcoreConstants.Attributes.AbstractOrder.ORDERCHANNEL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.orderChannel</code> attribute.
	 * @return the orderChannel
	 */
	public JnjOrderChannel getOrderChannel(final AbstractOrder item)
	{
		return getOrderChannel( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.orderChannel</code> attribute. 
	 * @param value the orderChannel
	 */
	public void setOrderChannel(final SessionContext ctx, final AbstractOrder item, final JnjOrderChannel value)
	{
		item.setProperty(ctx, Jnjlab2bcoreConstants.Attributes.AbstractOrder.ORDERCHANNEL,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.orderChannel</code> attribute. 
	 * @param value the orderChannel
	 */
	public void setOrderChannel(final AbstractOrder item, final JnjOrderChannel value)
	{
		setOrderChannel( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.pendingConsolidatedEmail</code> attribute.
	 * @return the pendingConsolidatedEmail
	 */
	public Boolean isPendingConsolidatedEmail(final SessionContext ctx, final AbstractOrderEntry item)
	{
		return (Boolean)item.getProperty( ctx, Jnjlab2bcoreConstants.Attributes.AbstractOrderEntry.PENDINGCONSOLIDATEDEMAIL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.pendingConsolidatedEmail</code> attribute.
	 * @return the pendingConsolidatedEmail
	 */
	public Boolean isPendingConsolidatedEmail(final AbstractOrderEntry item)
	{
		return isPendingConsolidatedEmail( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.pendingConsolidatedEmail</code> attribute. 
	 * @return the pendingConsolidatedEmail
	 */
	public boolean isPendingConsolidatedEmailAsPrimitive(final SessionContext ctx, final AbstractOrderEntry item)
	{
		Boolean value = isPendingConsolidatedEmail( ctx,item );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.pendingConsolidatedEmail</code> attribute. 
	 * @return the pendingConsolidatedEmail
	 */
	public boolean isPendingConsolidatedEmailAsPrimitive(final AbstractOrderEntry item)
	{
		return isPendingConsolidatedEmailAsPrimitive( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.pendingConsolidatedEmail</code> attribute. 
	 * @param value the pendingConsolidatedEmail
	 */
	public void setPendingConsolidatedEmail(final SessionContext ctx, final AbstractOrderEntry item, final Boolean value)
	{
		item.setProperty(ctx, Jnjlab2bcoreConstants.Attributes.AbstractOrderEntry.PENDINGCONSOLIDATEDEMAIL,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.pendingConsolidatedEmail</code> attribute. 
	 * @param value the pendingConsolidatedEmail
	 */
	public void setPendingConsolidatedEmail(final AbstractOrderEntry item, final Boolean value)
	{
		setPendingConsolidatedEmail( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.pendingConsolidatedEmail</code> attribute. 
	 * @param value the pendingConsolidatedEmail
	 */
	public void setPendingConsolidatedEmail(final SessionContext ctx, final AbstractOrderEntry item, final boolean value)
	{
		setPendingConsolidatedEmail( ctx, item, Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.pendingConsolidatedEmail</code> attribute. 
	 * @param value the pendingConsolidatedEmail
	 */
	public void setPendingConsolidatedEmail(final AbstractOrderEntry item, final boolean value)
	{
		setPendingConsolidatedEmail( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.pendingDailyEmail</code> attribute.
	 * @return the pendingDailyEmail
	 */
	public Boolean isPendingDailyEmail(final SessionContext ctx, final AbstractOrderEntry item)
	{
		return (Boolean)item.getProperty( ctx, Jnjlab2bcoreConstants.Attributes.AbstractOrderEntry.PENDINGDAILYEMAIL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.pendingDailyEmail</code> attribute.
	 * @return the pendingDailyEmail
	 */
	public Boolean isPendingDailyEmail(final AbstractOrderEntry item)
	{
		return isPendingDailyEmail( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.pendingDailyEmail</code> attribute. 
	 * @return the pendingDailyEmail
	 */
	public boolean isPendingDailyEmailAsPrimitive(final SessionContext ctx, final AbstractOrderEntry item)
	{
		Boolean value = isPendingDailyEmail( ctx,item );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.pendingDailyEmail</code> attribute. 
	 * @return the pendingDailyEmail
	 */
	public boolean isPendingDailyEmailAsPrimitive(final AbstractOrderEntry item)
	{
		return isPendingDailyEmailAsPrimitive( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.pendingDailyEmail</code> attribute. 
	 * @param value the pendingDailyEmail
	 */
	public void setPendingDailyEmail(final SessionContext ctx, final AbstractOrderEntry item, final Boolean value)
	{
		item.setProperty(ctx, Jnjlab2bcoreConstants.Attributes.AbstractOrderEntry.PENDINGDAILYEMAIL,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.pendingDailyEmail</code> attribute. 
	 * @param value the pendingDailyEmail
	 */
	public void setPendingDailyEmail(final AbstractOrderEntry item, final Boolean value)
	{
		setPendingDailyEmail( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.pendingDailyEmail</code> attribute. 
	 * @param value the pendingDailyEmail
	 */
	public void setPendingDailyEmail(final SessionContext ctx, final AbstractOrderEntry item, final boolean value)
	{
		setPendingDailyEmail( ctx, item, Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.pendingDailyEmail</code> attribute. 
	 * @param value the pendingDailyEmail
	 */
	public void setPendingDailyEmail(final AbstractOrderEntry item, final boolean value)
	{
		setPendingDailyEmail( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.pendingImmediateEmail</code> attribute.
	 * @return the pendingImmediateEmail
	 */
	public Boolean isPendingImmediateEmail(final SessionContext ctx, final AbstractOrderEntry item)
	{
		return (Boolean)item.getProperty( ctx, Jnjlab2bcoreConstants.Attributes.AbstractOrderEntry.PENDINGIMMEDIATEEMAIL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.pendingImmediateEmail</code> attribute.
	 * @return the pendingImmediateEmail
	 */
	public Boolean isPendingImmediateEmail(final AbstractOrderEntry item)
	{
		return isPendingImmediateEmail( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.pendingImmediateEmail</code> attribute. 
	 * @return the pendingImmediateEmail
	 */
	public boolean isPendingImmediateEmailAsPrimitive(final SessionContext ctx, final AbstractOrderEntry item)
	{
		Boolean value = isPendingImmediateEmail( ctx,item );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.pendingImmediateEmail</code> attribute. 
	 * @return the pendingImmediateEmail
	 */
	public boolean isPendingImmediateEmailAsPrimitive(final AbstractOrderEntry item)
	{
		return isPendingImmediateEmailAsPrimitive( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.pendingImmediateEmail</code> attribute. 
	 * @param value the pendingImmediateEmail
	 */
	public void setPendingImmediateEmail(final SessionContext ctx, final AbstractOrderEntry item, final Boolean value)
	{
		item.setProperty(ctx, Jnjlab2bcoreConstants.Attributes.AbstractOrderEntry.PENDINGIMMEDIATEEMAIL,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.pendingImmediateEmail</code> attribute. 
	 * @param value the pendingImmediateEmail
	 */
	public void setPendingImmediateEmail(final AbstractOrderEntry item, final Boolean value)
	{
		setPendingImmediateEmail( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.pendingImmediateEmail</code> attribute. 
	 * @param value the pendingImmediateEmail
	 */
	public void setPendingImmediateEmail(final SessionContext ctx, final AbstractOrderEntry item, final boolean value)
	{
		setPendingImmediateEmail( ctx, item, Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.pendingImmediateEmail</code> attribute. 
	 * @param value the pendingImmediateEmail
	 */
	public void setPendingImmediateEmail(final AbstractOrderEntry item, final boolean value)
	{
		setPendingImmediateEmail( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.previousStatus</code> attribute.
	 * @return the previousStatus
	 */
	public EnumerationValue getPreviousStatus(final SessionContext ctx, final AbstractOrderEntry item)
	{
		return (EnumerationValue)item.getProperty( ctx, Jnjlab2bcoreConstants.Attributes.AbstractOrderEntry.PREVIOUSSTATUS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.previousStatus</code> attribute.
	 * @return the previousStatus
	 */
	public EnumerationValue getPreviousStatus(final AbstractOrderEntry item)
	{
		return getPreviousStatus( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.previousStatus</code> attribute. 
	 * @param value the previousStatus
	 */
	public void setPreviousStatus(final SessionContext ctx, final AbstractOrderEntry item, final EnumerationValue value)
	{
		item.setProperty(ctx, Jnjlab2bcoreConstants.Attributes.AbstractOrderEntry.PREVIOUSSTATUS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.previousStatus</code> attribute. 
	 * @param value the previousStatus
	 */
	public void setPreviousStatus(final AbstractOrderEntry item, final EnumerationValue value)
	{
		setPreviousStatus( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjDropShipmentDetails.principal</code> attribute.
	 * @return the principal
	 */
	public String getPrincipal(final SessionContext ctx, final GenericItem item)
	{
		return (String)item.getProperty( ctx, Jnjlab2bcoreConstants.Attributes.JnjDropShipmentDetails.PRINCIPAL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjDropShipmentDetails.principal</code> attribute.
	 * @return the principal
	 */
	public String getPrincipal(final JnjDropShipmentDetails item)
	{
		return getPrincipal( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjDropShipmentDetails.principal</code> attribute. 
	 * @param value the principal
	 */
	public void setPrincipal(final SessionContext ctx, final GenericItem item, final String value)
	{
		item.setProperty(ctx, Jnjlab2bcoreConstants.Attributes.JnjDropShipmentDetails.PRINCIPAL,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjDropShipmentDetails.principal</code> attribute. 
	 * @param value the principal
	 */
	public void setPrincipal(final JnjDropShipmentDetails item, final String value)
	{
		setPrincipal( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjDeliverySchedule.proofOfDeliveryDate</code> attribute.
	 * @return the proofOfDeliveryDate - Actual date of delivery of the schedule line
	 */
	public Date getProofOfDeliveryDate(final SessionContext ctx, final GenericItem item)
	{
		return (Date)item.getProperty( ctx, Jnjlab2bcoreConstants.Attributes.JnjDeliverySchedule.PROOFOFDELIVERYDATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjDeliverySchedule.proofOfDeliveryDate</code> attribute.
	 * @return the proofOfDeliveryDate - Actual date of delivery of the schedule line
	 */
	public Date getProofOfDeliveryDate(final JnjDeliverySchedule item)
	{
		return getProofOfDeliveryDate( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjDeliverySchedule.proofOfDeliveryDate</code> attribute. 
	 * @param value the proofOfDeliveryDate - Actual date of delivery of the schedule line
	 */
	public void setProofOfDeliveryDate(final SessionContext ctx, final GenericItem item, final Date value)
	{
		item.setProperty(ctx, Jnjlab2bcoreConstants.Attributes.JnjDeliverySchedule.PROOFOFDELIVERYDATE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjDeliverySchedule.proofOfDeliveryDate</code> attribute. 
	 * @param value the proofOfDeliveryDate - Actual date of delivery of the schedule line
	 */
	public void setProofOfDeliveryDate(final JnjDeliverySchedule item, final Date value)
	{
		setProofOfDeliveryDate( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.retryAttempts</code> attribute.
	 * @return the retryAttempts
	 */
	public Integer getRetryAttempts(final SessionContext ctx, final AbstractOrder item)
	{
		return (Integer)item.getProperty( ctx, Jnjlab2bcoreConstants.Attributes.AbstractOrder.RETRYATTEMPTS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.retryAttempts</code> attribute.
	 * @return the retryAttempts
	 */
	public Integer getRetryAttempts(final AbstractOrder item)
	{
		return getRetryAttempts( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.retryAttempts</code> attribute. 
	 * @return the retryAttempts
	 */
	public int getRetryAttemptsAsPrimitive(final SessionContext ctx, final AbstractOrder item)
	{
		Integer value = getRetryAttempts( ctx,item );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.retryAttempts</code> attribute. 
	 * @return the retryAttempts
	 */
	public int getRetryAttemptsAsPrimitive(final AbstractOrder item)
	{
		return getRetryAttemptsAsPrimitive( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.retryAttempts</code> attribute. 
	 * @param value the retryAttempts
	 */
	public void setRetryAttempts(final SessionContext ctx, final AbstractOrder item, final Integer value)
	{
		item.setProperty(ctx, Jnjlab2bcoreConstants.Attributes.AbstractOrder.RETRYATTEMPTS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.retryAttempts</code> attribute. 
	 * @param value the retryAttempts
	 */
	public void setRetryAttempts(final AbstractOrder item, final Integer value)
	{
		setRetryAttempts( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.retryAttempts</code> attribute. 
	 * @param value the retryAttempts
	 */
	public void setRetryAttempts(final SessionContext ctx, final AbstractOrder item, final int value)
	{
		setRetryAttempts( ctx, item, Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.retryAttempts</code> attribute. 
	 * @param value the retryAttempts
	 */
	public void setRetryAttempts(final AbstractOrder item, final int value)
	{
		setRetryAttempts( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjDeliverySchedule.roundedQuantity</code> attribute.
	 * @return the roundedQuantity
	 */
	public String getRoundedQuantity(final SessionContext ctx, final GenericItem item)
	{
		return (String)item.getProperty( ctx, Jnjlab2bcoreConstants.Attributes.JnjDeliverySchedule.ROUNDEDQUANTITY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjDeliverySchedule.roundedQuantity</code> attribute.
	 * @return the roundedQuantity
	 */
	public String getRoundedQuantity(final JnjDeliverySchedule item)
	{
		return getRoundedQuantity( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjDeliverySchedule.roundedQuantity</code> attribute. 
	 * @param value the roundedQuantity
	 */
	public void setRoundedQuantity(final SessionContext ctx, final GenericItem item, final String value)
	{
		item.setProperty(ctx, Jnjlab2bcoreConstants.Attributes.JnjDeliverySchedule.ROUNDEDQUANTITY,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjDeliverySchedule.roundedQuantity</code> attribute. 
	 * @param value the roundedQuantity
	 */
	public void setRoundedQuantity(final JnjDeliverySchedule item, final String value)
	{
		setRoundedQuantity( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Order.sapFailedOrderReportEmailSent</code> attribute.
	 * @return the sapFailedOrderReportEmailSent
	 */
	public Boolean isSapFailedOrderReportEmailSent(final SessionContext ctx, final Order item)
	{
		return (Boolean)item.getProperty( ctx, Jnjlab2bcoreConstants.Attributes.Order.SAPFAILEDORDERREPORTEMAILSENT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Order.sapFailedOrderReportEmailSent</code> attribute.
	 * @return the sapFailedOrderReportEmailSent
	 */
	public Boolean isSapFailedOrderReportEmailSent(final Order item)
	{
		return isSapFailedOrderReportEmailSent( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Order.sapFailedOrderReportEmailSent</code> attribute. 
	 * @return the sapFailedOrderReportEmailSent
	 */
	public boolean isSapFailedOrderReportEmailSentAsPrimitive(final SessionContext ctx, final Order item)
	{
		Boolean value = isSapFailedOrderReportEmailSent( ctx,item );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Order.sapFailedOrderReportEmailSent</code> attribute. 
	 * @return the sapFailedOrderReportEmailSent
	 */
	public boolean isSapFailedOrderReportEmailSentAsPrimitive(final Order item)
	{
		return isSapFailedOrderReportEmailSentAsPrimitive( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Order.sapFailedOrderReportEmailSent</code> attribute. 
	 * @param value the sapFailedOrderReportEmailSent
	 */
	public void setSapFailedOrderReportEmailSent(final SessionContext ctx, final Order item, final Boolean value)
	{
		item.setProperty(ctx, Jnjlab2bcoreConstants.Attributes.Order.SAPFAILEDORDERREPORTEMAILSENT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Order.sapFailedOrderReportEmailSent</code> attribute. 
	 * @param value the sapFailedOrderReportEmailSent
	 */
	public void setSapFailedOrderReportEmailSent(final Order item, final Boolean value)
	{
		setSapFailedOrderReportEmailSent( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Order.sapFailedOrderReportEmailSent</code> attribute. 
	 * @param value the sapFailedOrderReportEmailSent
	 */
	public void setSapFailedOrderReportEmailSent(final SessionContext ctx, final Order item, final boolean value)
	{
		setSapFailedOrderReportEmailSent( ctx, item, Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Order.sapFailedOrderReportEmailSent</code> attribute. 
	 * @param value the sapFailedOrderReportEmailSent
	 */
	public void setSapFailedOrderReportEmailSent(final Order item, final boolean value)
	{
		setSapFailedOrderReportEmailSent( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.shippedQuantity</code> attribute.
	 * @return the shippedQuantity
	 */
	public Long getShippedQuantity(final SessionContext ctx, final AbstractOrderEntry item)
	{
		return (Long)item.getProperty( ctx, Jnjlab2bcoreConstants.Attributes.AbstractOrderEntry.SHIPPEDQUANTITY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.shippedQuantity</code> attribute.
	 * @return the shippedQuantity
	 */
	public Long getShippedQuantity(final AbstractOrderEntry item)
	{
		return getShippedQuantity( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.shippedQuantity</code> attribute. 
	 * @return the shippedQuantity
	 */
	public long getShippedQuantityAsPrimitive(final SessionContext ctx, final AbstractOrderEntry item)
	{
		Long value = getShippedQuantity( ctx,item );
		return value != null ? value.longValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.shippedQuantity</code> attribute. 
	 * @return the shippedQuantity
	 */
	public long getShippedQuantityAsPrimitive(final AbstractOrderEntry item)
	{
		return getShippedQuantityAsPrimitive( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.shippedQuantity</code> attribute. 
	 * @param value the shippedQuantity
	 */
	public void setShippedQuantity(final SessionContext ctx, final AbstractOrderEntry item, final Long value)
	{
		item.setProperty(ctx, Jnjlab2bcoreConstants.Attributes.AbstractOrderEntry.SHIPPEDQUANTITY,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.shippedQuantity</code> attribute. 
	 * @param value the shippedQuantity
	 */
	public void setShippedQuantity(final AbstractOrderEntry item, final Long value)
	{
		setShippedQuantity( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.shippedQuantity</code> attribute. 
	 * @param value the shippedQuantity
	 */
	public void setShippedQuantity(final SessionContext ctx, final AbstractOrderEntry item, final long value)
	{
		setShippedQuantity( ctx, item, Long.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.shippedQuantity</code> attribute. 
	 * @param value the shippedQuantity
	 */
	public void setShippedQuantity(final AbstractOrderEntry item, final long value)
	{
		setShippedQuantity( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjDropShipmentDetails.shipper</code> attribute.
	 * @return the shipper
	 */
	public String getShipper(final SessionContext ctx, final GenericItem item)
	{
		return (String)item.getProperty( ctx, Jnjlab2bcoreConstants.Attributes.JnjDropShipmentDetails.SHIPPER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjDropShipmentDetails.shipper</code> attribute.
	 * @return the shipper
	 */
	public String getShipper(final JnjDropShipmentDetails item)
	{
		return getShipper( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjDropShipmentDetails.shipper</code> attribute. 
	 * @param value the shipper
	 */
	public void setShipper(final SessionContext ctx, final GenericItem item, final String value)
	{
		item.setProperty(ctx, Jnjlab2bcoreConstants.Attributes.JnjDropShipmentDetails.SHIPPER,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjDropShipmentDetails.shipper</code> attribute. 
	 * @param value the shipper
	 */
	public void setShipper(final JnjDropShipmentDetails item, final String value)
	{
		setShipper( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjDropShipmentDetails.shipperMD</code> attribute.
	 * @return the shipperMD
	 */
	public String getShipperMD(final SessionContext ctx, final GenericItem item)
	{
		return (String)item.getProperty( ctx, Jnjlab2bcoreConstants.Attributes.JnjDropShipmentDetails.SHIPPERMD);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjDropShipmentDetails.shipperMD</code> attribute.
	 * @return the shipperMD
	 */
	public String getShipperMD(final JnjDropShipmentDetails item)
	{
		return getShipperMD( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjDropShipmentDetails.shipperMD</code> attribute. 
	 * @param value the shipperMD
	 */
	public void setShipperMD(final SessionContext ctx, final GenericItem item, final String value)
	{
		item.setProperty(ctx, Jnjlab2bcoreConstants.Attributes.JnjDropShipmentDetails.SHIPPERMD,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjDropShipmentDetails.shipperMD</code> attribute. 
	 * @param value the shipperMD
	 */
	public void setShipperMD(final JnjDropShipmentDetails item, final String value)
	{
		setShipperMD( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjUomConversion.shippingUnit</code> attribute.
	 * @return the shippingUnit
	 */
	public String getShippingUnit(final SessionContext ctx, final GenericItem item)
	{
		return (String)item.getProperty( ctx, Jnjlab2bcoreConstants.Attributes.JnjUomConversion.SHIPPINGUNIT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjUomConversion.shippingUnit</code> attribute.
	 * @return the shippingUnit
	 */
	public String getShippingUnit(final JnjUomConversion item)
	{
		return getShippingUnit( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjUomConversion.shippingUnit</code> attribute. 
	 * @param value the shippingUnit
	 */
	public void setShippingUnit(final SessionContext ctx, final GenericItem item, final String value)
	{
		item.setProperty(ctx, Jnjlab2bcoreConstants.Attributes.JnjUomConversion.SHIPPINGUNIT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjUomConversion.shippingUnit</code> attribute. 
	 * @param value the shippingUnit
	 */
	public void setShippingUnit(final JnjUomConversion item, final String value)
	{
		setShippingUnit( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjUomConversion.shippingWeight</code> attribute.
	 * @return the shippingWeight
	 */
	public String getShippingWeight(final SessionContext ctx, final GenericItem item)
	{
		return (String)item.getProperty( ctx, Jnjlab2bcoreConstants.Attributes.JnjUomConversion.SHIPPINGWEIGHT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjUomConversion.shippingWeight</code> attribute.
	 * @return the shippingWeight
	 */
	public String getShippingWeight(final JnjUomConversion item)
	{
		return getShippingWeight( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjUomConversion.shippingWeight</code> attribute. 
	 * @param value the shippingWeight
	 */
	public void setShippingWeight(final SessionContext ctx, final GenericItem item, final String value)
	{
		item.setProperty(ctx, Jnjlab2bcoreConstants.Attributes.JnjUomConversion.SHIPPINGWEIGHT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjUomConversion.shippingWeight</code> attribute. 
	 * @param value the shippingWeight
	 */
	public void setShippingWeight(final JnjUomConversion item, final String value)
	{
		setShippingWeight( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.showDescLbl</code> attribute.
	 * @return the showDescLbl
	 */
	public Boolean isShowDescLbl(final SessionContext ctx, final AbstractOrderEntry item)
	{
		return (Boolean)item.getProperty( ctx, Jnjlab2bcoreConstants.Attributes.AbstractOrderEntry.SHOWDESCLBL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.showDescLbl</code> attribute.
	 * @return the showDescLbl
	 */
	public Boolean isShowDescLbl(final AbstractOrderEntry item)
	{
		return isShowDescLbl( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.showDescLbl</code> attribute. 
	 * @return the showDescLbl
	 */
	public boolean isShowDescLblAsPrimitive(final SessionContext ctx, final AbstractOrderEntry item)
	{
		Boolean value = isShowDescLbl( ctx,item );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.showDescLbl</code> attribute. 
	 * @return the showDescLbl
	 */
	public boolean isShowDescLblAsPrimitive(final AbstractOrderEntry item)
	{
		return isShowDescLblAsPrimitive( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.showDescLbl</code> attribute. 
	 * @param value the showDescLbl
	 */
	public void setShowDescLbl(final SessionContext ctx, final AbstractOrderEntry item, final Boolean value)
	{
		item.setProperty(ctx, Jnjlab2bcoreConstants.Attributes.AbstractOrderEntry.SHOWDESCLBL,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.showDescLbl</code> attribute. 
	 * @param value the showDescLbl
	 */
	public void setShowDescLbl(final AbstractOrderEntry item, final Boolean value)
	{
		setShowDescLbl( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.showDescLbl</code> attribute. 
	 * @param value the showDescLbl
	 */
	public void setShowDescLbl(final SessionContext ctx, final AbstractOrderEntry item, final boolean value)
	{
		setShowDescLbl( ctx, item, Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.showDescLbl</code> attribute. 
	 * @param value the showDescLbl
	 */
	public void setShowDescLbl(final AbstractOrderEntry item, final boolean value)
	{
		setShowDescLbl( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.showInfoLbl</code> attribute.
	 * @return the showInfoLbl
	 */
	public Boolean isShowInfoLbl(final SessionContext ctx, final AbstractOrderEntry item)
	{
		return (Boolean)item.getProperty( ctx, Jnjlab2bcoreConstants.Attributes.AbstractOrderEntry.SHOWINFOLBL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.showInfoLbl</code> attribute.
	 * @return the showInfoLbl
	 */
	public Boolean isShowInfoLbl(final AbstractOrderEntry item)
	{
		return isShowInfoLbl( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.showInfoLbl</code> attribute. 
	 * @return the showInfoLbl
	 */
	public boolean isShowInfoLblAsPrimitive(final SessionContext ctx, final AbstractOrderEntry item)
	{
		Boolean value = isShowInfoLbl( ctx,item );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.showInfoLbl</code> attribute. 
	 * @return the showInfoLbl
	 */
	public boolean isShowInfoLblAsPrimitive(final AbstractOrderEntry item)
	{
		return isShowInfoLblAsPrimitive( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.showInfoLbl</code> attribute. 
	 * @param value the showInfoLbl
	 */
	public void setShowInfoLbl(final SessionContext ctx, final AbstractOrderEntry item, final Boolean value)
	{
		item.setProperty(ctx, Jnjlab2bcoreConstants.Attributes.AbstractOrderEntry.SHOWINFOLBL,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.showInfoLbl</code> attribute. 
	 * @param value the showInfoLbl
	 */
	public void setShowInfoLbl(final AbstractOrderEntry item, final Boolean value)
	{
		setShowInfoLbl( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.showInfoLbl</code> attribute. 
	 * @param value the showInfoLbl
	 */
	public void setShowInfoLbl(final SessionContext ctx, final AbstractOrderEntry item, final boolean value)
	{
		setShowInfoLbl( ctx, item, Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.showInfoLbl</code> attribute. 
	 * @param value the showInfoLbl
	 */
	public void setShowInfoLbl(final AbstractOrderEntry item, final boolean value)
	{
		setShowInfoLbl( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Category.showPlannedDate</code> attribute.
	 * @return the showPlannedDate
	 */
	public Boolean isShowPlannedDate(final SessionContext ctx, final Category item)
	{
		return (Boolean)item.getProperty( ctx, Jnjlab2bcoreConstants.Attributes.Category.SHOWPLANNEDDATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Category.showPlannedDate</code> attribute.
	 * @return the showPlannedDate
	 */
	public Boolean isShowPlannedDate(final Category item)
	{
		return isShowPlannedDate( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Category.showPlannedDate</code> attribute. 
	 * @return the showPlannedDate
	 */
	public boolean isShowPlannedDateAsPrimitive(final SessionContext ctx, final Category item)
	{
		Boolean value = isShowPlannedDate( ctx,item );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Category.showPlannedDate</code> attribute. 
	 * @return the showPlannedDate
	 */
	public boolean isShowPlannedDateAsPrimitive(final Category item)
	{
		return isShowPlannedDateAsPrimitive( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Category.showPlannedDate</code> attribute. 
	 * @param value the showPlannedDate
	 */
	public void setShowPlannedDate(final SessionContext ctx, final Category item, final Boolean value)
	{
		item.setProperty(ctx, Jnjlab2bcoreConstants.Attributes.Category.SHOWPLANNEDDATE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Category.showPlannedDate</code> attribute. 
	 * @param value the showPlannedDate
	 */
	public void setShowPlannedDate(final Category item, final Boolean value)
	{
		setShowPlannedDate( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Category.showPlannedDate</code> attribute. 
	 * @param value the showPlannedDate
	 */
	public void setShowPlannedDate(final SessionContext ctx, final Category item, final boolean value)
	{
		setShowPlannedDate( ctx, item, Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Category.showPlannedDate</code> attribute. 
	 * @param value the showPlannedDate
	 */
	public void setShowPlannedDate(final Category item, final boolean value)
	{
		setShowPlannedDate( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.totalFee</code> attribute.
	 * @return the totalFee
	 */
	public Double getTotalFee(final SessionContext ctx, final AbstractOrder item)
	{
		return (Double)item.getProperty( ctx, Jnjlab2bcoreConstants.Attributes.AbstractOrder.TOTALFEE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.totalFee</code> attribute.
	 * @return the totalFee
	 */
	public Double getTotalFee(final AbstractOrder item)
	{
		return getTotalFee( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.totalFee</code> attribute. 
	 * @return the totalFee
	 */
	public double getTotalFeeAsPrimitive(final SessionContext ctx, final AbstractOrder item)
	{
		Double value = getTotalFee( ctx,item );
		return value != null ? value.doubleValue() : 0.0d;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.totalFee</code> attribute. 
	 * @return the totalFee
	 */
	public double getTotalFeeAsPrimitive(final AbstractOrder item)
	{
		return getTotalFeeAsPrimitive( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.totalFee</code> attribute. 
	 * @param value the totalFee
	 */
	public void setTotalFee(final SessionContext ctx, final AbstractOrder item, final Double value)
	{
		item.setProperty(ctx, Jnjlab2bcoreConstants.Attributes.AbstractOrder.TOTALFEE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.totalFee</code> attribute. 
	 * @param value the totalFee
	 */
	public void setTotalFee(final AbstractOrder item, final Double value)
	{
		setTotalFee( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.totalFee</code> attribute. 
	 * @param value the totalFee
	 */
	public void setTotalFee(final SessionContext ctx, final AbstractOrder item, final double value)
	{
		setTotalFee( ctx, item, Double.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.totalFee</code> attribute. 
	 * @param value the totalFee
	 */
	public void setTotalFee(final AbstractOrder item, final double value)
	{
		setTotalFee( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJInvoiceOrder.updatedByFile</code> attribute.
	 * @return the updatedByFile
	 */
	public JnJUploadedInvoiceDate getUpdatedByFile(final SessionContext ctx, final GenericItem item)
	{
		return (JnJUploadedInvoiceDate)item.getProperty( ctx, Jnjlab2bcoreConstants.Attributes.JnJInvoiceOrder.UPDATEDBYFILE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJInvoiceOrder.updatedByFile</code> attribute.
	 * @return the updatedByFile
	 */
	public JnJUploadedInvoiceDate getUpdatedByFile(final JnJInvoiceOrder item)
	{
		return getUpdatedByFile( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJInvoiceOrder.updatedByFile</code> attribute. 
	 * @param value the updatedByFile
	 */
	public void setUpdatedByFile(final SessionContext ctx, final GenericItem item, final JnJUploadedInvoiceDate value)
	{
		item.setProperty(ctx, Jnjlab2bcoreConstants.Attributes.JnJInvoiceOrder.UPDATEDBYFILE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJInvoiceOrder.updatedByFile</code> attribute. 
	 * @param value the updatedByFile
	 */
	public void setUpdatedByFile(final JnJInvoiceOrder item, final JnJUploadedInvoiceDate value)
	{
		setUpdatedByFile( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJInvoiceOrder.updatedByFileDate</code> attribute.
	 * @return the updatedByFileDate
	 */
	public Date getUpdatedByFileDate(final SessionContext ctx, final GenericItem item)
	{
		return (Date)item.getProperty( ctx, Jnjlab2bcoreConstants.Attributes.JnJInvoiceOrder.UPDATEDBYFILEDATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnJInvoiceOrder.updatedByFileDate</code> attribute.
	 * @return the updatedByFileDate
	 */
	public Date getUpdatedByFileDate(final JnJInvoiceOrder item)
	{
		return getUpdatedByFileDate( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJInvoiceOrder.updatedByFileDate</code> attribute. 
	 * @param value the updatedByFileDate
	 */
	public void setUpdatedByFileDate(final SessionContext ctx, final GenericItem item, final Date value)
	{
		item.setProperty(ctx, Jnjlab2bcoreConstants.Attributes.JnJInvoiceOrder.UPDATEDBYFILEDATE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnJInvoiceOrder.updatedByFileDate</code> attribute. 
	 * @param value the updatedByFileDate
	 */
	public void setUpdatedByFileDate(final JnJInvoiceOrder item, final Date value)
	{
		setUpdatedByFileDate( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjUomConversion.volumeUnit</code> attribute.
	 * @return the volumeUnit
	 */
	public Unit getVolumeUnit(final SessionContext ctx, final GenericItem item)
	{
		return (Unit)item.getProperty( ctx, Jnjlab2bcoreConstants.Attributes.JnjUomConversion.VOLUMEUNIT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjUomConversion.volumeUnit</code> attribute.
	 * @return the volumeUnit
	 */
	public Unit getVolumeUnit(final JnjUomConversion item)
	{
		return getVolumeUnit( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjUomConversion.volumeUnit</code> attribute. 
	 * @param value the volumeUnit
	 */
	public void setVolumeUnit(final SessionContext ctx, final GenericItem item, final Unit value)
	{
		item.setProperty(ctx, Jnjlab2bcoreConstants.Attributes.JnjUomConversion.VOLUMEUNIT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjUomConversion.volumeUnit</code> attribute. 
	 * @param value the volumeUnit
	 */
	public void setVolumeUnit(final JnjUomConversion item, final Unit value)
	{
		setVolumeUnit( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjUomConversion.width</code> attribute.
	 * @return the width
	 */
	public Double getWidth(final SessionContext ctx, final GenericItem item)
	{
		return (Double)item.getProperty( ctx, Jnjlab2bcoreConstants.Attributes.JnjUomConversion.WIDTH);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjUomConversion.width</code> attribute.
	 * @return the width
	 */
	public Double getWidth(final JnjUomConversion item)
	{
		return getWidth( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjUomConversion.width</code> attribute. 
	 * @return the width
	 */
	public double getWidthAsPrimitive(final SessionContext ctx, final JnjUomConversion item)
	{
		Double value = getWidth( ctx,item );
		return value != null ? value.doubleValue() : 0.0d;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JnjUomConversion.width</code> attribute. 
	 * @return the width
	 */
	public double getWidthAsPrimitive(final JnjUomConversion item)
	{
		return getWidthAsPrimitive( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjUomConversion.width</code> attribute. 
	 * @param value the width
	 */
	public void setWidth(final SessionContext ctx, final GenericItem item, final Double value)
	{
		item.setProperty(ctx, Jnjlab2bcoreConstants.Attributes.JnjUomConversion.WIDTH,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjUomConversion.width</code> attribute. 
	 * @param value the width
	 */
	public void setWidth(final JnjUomConversion item, final Double value)
	{
		setWidth( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjUomConversion.width</code> attribute. 
	 * @param value the width
	 */
	public void setWidth(final SessionContext ctx, final JnjUomConversion item, final double value)
	{
		setWidth( ctx, item, Double.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>JnjUomConversion.width</code> attribute. 
	 * @param value the width
	 */
	public void setWidth(final JnjUomConversion item, final double value)
	{
		setWidth( getSession().getSessionContext(), item, value );
	}
	
}
