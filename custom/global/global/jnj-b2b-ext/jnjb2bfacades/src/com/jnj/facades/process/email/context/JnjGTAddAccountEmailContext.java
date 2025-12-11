/**
 * 
 */
package com.jnj.facades.process.email.context;

import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.commerceservices.model.process.StoreFrontCustomerProcessModel;
import de.hybris.platform.util.Config;

import java.util.HashMap;
import java.util.Map;
import jakarta.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.services.JnjConfigService;
import com.jnj.facades.process.email.context.CustomerEmailContext;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.facades.customer.JnjGTCustomerFacade;
import com.jnj.core.model.JnjGTAddAccountEmailProcessModel;


/**
 * @author komal.sehgal
 * 
 */
public class JnjGTAddAccountEmailContext extends CustomerEmailContext
{



	@Autowired
	protected JnjConfigService jnjConfigService;

	@Resource(name="GTCustomerFacade")
	protected JnjGTCustomerFacade jnjGTCustomerFacade;

	protected Map<String, Object> addAccountEmailMap = new HashMap<String, Object>();

	private static final String ACCOUNT_NAME = "accountName";

	private static final String SECTOR = "sector";
	private static final String FIRST_NAME = "firstName";
	private static final String LAST_NAME = "lastName";
	private static final String GLN = "gln";
	private static final String TYPE_BUISNESS = "typeOfBuisness";
	private static final String SUBSIDIARY = "subsidiary";
	private static final String SHIP_TO_COUNTRY = "shipToCountry";
	private static final String SHIP_TO_REGION = "shipToRegion";
	private static final String SHIP_TO_TOWN = "shipToTown";
	private static final String SHIP_TO_LINE1 = "shipToLine1";
	private static final String SHIP_TO_LINE2 = "shipToLine2";
	private static final String SHIP_TO_POSTAL = "shipToPostal";
	private static final String BILL_TO_COUNTRY = "billToCountry";
	private static final String BILL_TO_REGION = "billToRegion";
	private static final String BILL_TO_TOWN = "billToTown";
	private static final String BILL_TO_LINE1 = "billToLine1";
	private static final String BILL_TO_LINE2 = "billToLine2";
	private static final String BILL_TO_POSTAL = "billToPostal";
	private static final String INITIAL_OPENING_AMOUNT = "initailOpeningAmount";
	private static final String ESTIMATED_AMOUNT_YEAR = "estimatedAmountYear";
	private static final String SALES_USE_TAX_FLAG = "salesUseTax";
	private static final String PURCHASE_PRODUCT = "purchaseProduct";



	/**
	 * @return the jnjCustomerFormMap
	 */
	public Map<String, Object> getaddAccountEmailMap()
	{
		return addAccountEmailMap;
	}

	/**
	 * @param jnjCustomerFormMap
	 *           the jnjCustomerFormMap to set
	 */
	public void setAddAccountEmailMap(final Map<String, Object> addAccountEmailMap)
	{
		this.addAccountEmailMap = addAccountEmailMap;
	}


	@Override
	public void init(final StoreFrontCustomerProcessModel storeFrontCustomerProcessModel, final EmailPageModel emailPageModel)
	{
		super.init(storeFrontCustomerProcessModel, emailPageModel);
		if (storeFrontCustomerProcessModel instanceof JnjGTAddAccountEmailProcessModel)
		{
			final JnjGTAddAccountEmailProcessModel jnjGTAddAccountEmailProcessModel = (JnjGTAddAccountEmailProcessModel) storeFrontCustomerProcessModel;
			addAccountEmailMap.put(SECTOR, jnjGTAddAccountEmailProcessModel.getSector());
			addAccountEmailMap.put(EMAIL, jnjGTAddAccountEmailProcessModel.getEmail());
			addAccountEmailMap.put(FIRST_NAME, jnjGTAddAccountEmailProcessModel.getFirstName());
			addAccountEmailMap.put(LAST_NAME, jnjGTCustomerFacade.getCurrentGTCustomer().getLastName());
			addAccountEmailMap.put(ACCOUNT_NAME, jnjGTAddAccountEmailProcessModel.getAccountName());
			addAccountEmailMap.put(GLN, jnjGTAddAccountEmailProcessModel.getGln());
			addAccountEmailMap.put(TYPE_BUISNESS, jnjGTAddAccountEmailProcessModel.getTypeOfBuisness());
			addAccountEmailMap.put(SUBSIDIARY, jnjGTAddAccountEmailProcessModel.getSubsidiary());
			addAccountEmailMap.put(SHIP_TO_COUNTRY, jnjGTAddAccountEmailProcessModel.getShipToCountry());
			addAccountEmailMap.put(SHIP_TO_REGION, jnjGTAddAccountEmailProcessModel.getShipToRegion());
			addAccountEmailMap.put(SHIP_TO_TOWN, jnjGTAddAccountEmailProcessModel.getShipToTown());
			addAccountEmailMap.put(SHIP_TO_LINE1, jnjGTAddAccountEmailProcessModel.getShipToLine1());
			addAccountEmailMap.put(SHIP_TO_LINE2, jnjGTAddAccountEmailProcessModel.getShipToLine2());
			addAccountEmailMap.put(SHIP_TO_POSTAL, jnjGTAddAccountEmailProcessModel.getShipToPostalCode());
			addAccountEmailMap.put(BILL_TO_COUNTRY, jnjGTAddAccountEmailProcessModel.getBillToCountry());
			addAccountEmailMap.put(BILL_TO_REGION, jnjGTAddAccountEmailProcessModel.getBillToRegion());
			addAccountEmailMap.put(BILL_TO_TOWN, jnjGTAddAccountEmailProcessModel.getBillToTown());
			addAccountEmailMap.put(BILL_TO_LINE1, jnjGTAddAccountEmailProcessModel.getBillToLine1());
			addAccountEmailMap.put(BILL_TO_LINE2, jnjGTAddAccountEmailProcessModel.getBillToLine2());
			addAccountEmailMap.put(BILL_TO_POSTAL, jnjGTAddAccountEmailProcessModel.getBillToPostalCode());
			addAccountEmailMap.put(INITIAL_OPENING_AMOUNT, jnjGTAddAccountEmailProcessModel.getInitialOpeningOrderAmount());
			addAccountEmailMap.put(ESTIMATED_AMOUNT_YEAR, jnjGTAddAccountEmailProcessModel.getEstimatedAmountPerYear());
			addAccountEmailMap.put(PURCHASE_PRODUCT, jnjGTAddAccountEmailProcessModel.getProductsPurchased());

			if (jnjGTAddAccountEmailProcessModel.isSalesAndUseTaxFlag())
			{
				addAccountEmailMap.put(SALES_USE_TAX_FLAG, Jnjb2bCoreConstants.Profile.YES);
			}
			else
			{
				addAccountEmailMap.put(SALES_USE_TAX_FLAG, Jnjb2bCoreConstants.Profile.NO);
			}
			setAddAccountEmailMap(addAccountEmailMap);
			put(FROM_EMAIL, jnjGTAddAccountEmailProcessModel.getEmail());
			put(FROM_DISPLAY_NAME, jnjGTAddAccountEmailProcessModel.getFirstName());
		}
		put(EMAIL, Config.getParameter(Jnjb2bCoreConstants.Register.REGISTRATION_EMAIL_CSR_EMAIL_ADDRESS));
		put(DISPLAY_NAME, Config.getParameter(Jnjb2bCoreConstants.Register.REGISTRATION_EMAIL_CSR_EMAIL_ADDRESS));
	}
}
