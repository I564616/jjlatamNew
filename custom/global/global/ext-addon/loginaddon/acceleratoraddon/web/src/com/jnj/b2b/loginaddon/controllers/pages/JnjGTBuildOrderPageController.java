package com.jnj.b2b.loginaddon.controllers.pages;

import com.jnj.b2b.loginaddon.controllers.LoginaddonControllerConstants;
import com.jnj.b2b.storefront.annotations.RequireHardLogIn;
import com.jnj.b2b.storefront.breadcrumb.ResourceBreadcrumbBuilder;
import com.jnj.b2b.storefront.controllers.pages.AbstractSearchPageController;
import com.jnj.b2b.storefront.controllers.util.GlobalMessages;
import com.jnj.facade.util.JnjCommonFacadeUtil;
import com.jnj.facades.buildorder.JnjBuildOrderFacade;
import com.jnj.facades.cart.JnjGTCartFacade;
import com.jnj.facades.data.JnjBuildOrderData;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.util.localization.Localization;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

@Controller
@Scope("tenant")
@RequestMapping("/")
public class JnjGTBuildOrderPageController extends AbstractSearchPageController {
	
	
	protected static final String PRODUCT = "product";
	protected static final String CART_DATA = "cartData";
	protected static final String ADD_STATUS = "addStatus";
	protected static final String LINES = "lines";
	
	protected static final String _0 = "0";
	
	protected static final Logger LOG = Logger.getLogger(JnJGTHomePageController.class);
	
	
	@Resource(name = "accountBreadcrumbBuilder")
	protected ResourceBreadcrumbBuilder accountBreadcrumbBuilder;
	
	
	@Resource(name="GTCartFacade")
	protected JnjGTCartFacade jnjGTCartFacade;
	
	@Resource(name="jnjBuildOrderFacade")
	protected JnjBuildOrderFacade jnjBuildOrderFacade;
	
	@Autowired
	protected SessionService sessionService;
	
	@Autowired
	protected ProductFacade productFacade;
	
	@Autowired
	protected JnjCommonFacadeUtil jnjCommonFacadeUtil;

	protected static final String BUILD_ORDER_PAGE = "BuildOrderpage";

	protected static final String BUILD_ORDER_PAGE_URL = "/buildorder";

	protected static final String REDIRECT_MY_ACCOUNT_BUILD_ORDER = REDIRECT_PREFIX
			+ "/buildorder";

	protected static final String BUILD_ORDER_ADD_ITEM_PAGE_URL = "/buildorder/addItem";
	protected static final String BUILD_ORDER_REMOVE_ITEM_PAGE_URL = "/buildorder/deleteItem";
	protected static final String BUILD_ORDER_SESSION_CLEAR_ITEM_PAGE_URL = "/buildorder/clearSessionItem";
	/*Added by Vijay*/
	protected static final String BUILD_ORDER_PRODCT_VALIDATE_URL = "/buildorder/productValidate";
	
	
	
	
	@GetMapping(BUILD_ORDER_PAGE_URL)
	 @RequireHardLogIn
	 public String buildOrder(final Model model, final HttpSession session) throws CMSItemNotFoundException
	 {
       final JnjBuildOrderData buildOrder = jnjBuildOrderFacade.getBuildOrderFromSession((JnjBuildOrderData) session
				.getAttribute(JnjBuildOrderFacade.BUILD_ORDER_SESSION_ATTRIBUTE));
       
       
		if (buildOrder != null && buildOrder.getLineItems().size() > 0)
		{
			model.addAttribute("orderform", buildOrder);
		}
		else
		{
			model.addAttribute("orderform", null);
		}
       
	    storeCmsPageInModel(model, getContentPageForLabelOrId(BUILD_ORDER_PAGE));
	     setUpMetaDataForContentPage(model, getContentPageForLabelOrId(BUILD_ORDER_PAGE));
	     
	     
	   model.addAttribute("breadcrumbs", accountBreadcrumbBuilder.getBreadcrumbs("text.account.buildorder.pageHeading"));
	   model.addAttribute("metaRobots", "no-index,no-follow");
	   
	   final Object showChangeAccountLink = sessionService.getAttribute(LoginaddonControllerConstants.Views.Pages.Home.SHOW_CHANGE_ACCOUNT);
		//LOGGER.info("showChangeAccountLink value : "+showChangeAccountLink);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			//LOGGER.info("entered condition...........showChangeAccountLink :"+showChangeAccountLink);
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
	  return  getView(LoginaddonControllerConstants.Views.Pages.Account.AccountBuildOrderpage);
	 }
	
	
	@PostMapping(BUILD_ORDER_ADD_ITEM_PAGE_URL)
	@RequireHardLogIn
	public String quickOrderAddItem(final Model model, @RequestParam("productCode") final String productCode,
			@RequestParam("qty") final String qtyString, final HttpSession session, final RedirectAttributes redirectModel)
			throws CMSItemNotFoundException
	{
		
		
		
		final JnjBuildOrderData buildOrder = jnjBuildOrderFacade.getBuildOrderFromSession((JnjBuildOrderData) session
				.getAttribute(JnjBuildOrderFacade.BUILD_ORDER_SESSION_ATTRIBUTE));
    
		LinkedHashMap<String, OrderEntryData> lineItems = (LinkedHashMap<String, OrderEntryData>) buildOrder.getLineItems();
		LinkedHashMap<String, OrderEntryData> returnLineItems=new LinkedHashMap<String, OrderEntryData>();
         buildOrder.setLineItems(lineItems);
     
     System.out.println("Build Order Page Controller.....................................");
		
		Long qty = 0L;

		if (productCode.equals("") && (qtyString.equals("")))
		{


			//session.setAttribute(JnjBuildOrderFacade.BUILD_ORDER_SESSION_ATTRIBUTE, buildOrder);
			model.addAttribute("orderform", buildOrder);
			storeCmsPageInModel(model, getContentPageForLabelOrId(BUILD_ORDER_PAGE));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(BUILD_ORDER_PAGE));
			model.addAttribute("breadcrumbs", accountBreadcrumbBuilder.getBreadcrumbs(null));
			model.addAttribute("metaRobots", "no-index,no-follow");
			GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.ERROR_MESSAGES_HOLDER,
					Localization.getLocalizedString("quickorder.productcode.empty"));
			return REDIRECT_MY_ACCOUNT_BUILD_ORDER;
		}
		else if (!productCode.equals(""))
		{

			if (qtyString.equals(""))
			{
				qty = 1L;
			}
			else
			{
				try
				{
					qty = Long.parseLong(qtyString);
				}
				catch (final Exception e)
				{
					LOG.error(e.getLocalizedMessage());
				}
			}

			
			
			
			//if (jnjBuildOrderFacade.productExistsInList(productCode, buildOrder))
			if(lineItems.containsKey(productCode))	
			{
				//jnjBuildOrderFacade.addItemToBuildOrder(buildOrder, productCode, qty);
				
				//Map<Integer, Integer> map = new HashMap<Integer, Integer>();
				Iterator<Map.Entry<String, OrderEntryData>> entries = lineItems.entrySet().iterator();
				while (entries.hasNext()) {
				    Map.Entry<String, OrderEntryData> entry = entries.next();
				   if(entry.getKey().equals(productCode))
				   {
				    OrderEntryData productDetail  = entry.getValue();
				    productDetail.setQuantity(productDetail.getQuantity()+qty);
				    
				    entry.setValue(productDetail);
				}

				}
						//final Long qty = entry.getQuantity();
		
				 
				
			}
			else
			{
				final OrderEntryData orderEntry = jnjBuildOrderFacade.getProductData(productCode, qty);
				
				if (orderEntry != null)
				{
					//jnjBuildOrderFacade.addItemToBuildOrder(buildOrder, productCode, qty);
					returnLineItems.put(productCode, orderEntry);
				}
				else
				{
					GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.ERROR_MESSAGES_HOLDER,
							"buildorder.addtocart.cmir.badData");
				}
				
				
			}
			returnLineItems.putAll(lineItems);
			buildOrder.setLineItems(returnLineItems);


		}
		session.setAttribute(JnjBuildOrderFacade.BUILD_ORDER_SESSION_ATTRIBUTE, buildOrder);
		Map<String,OrderEntryData> addedProducts = buildOrder.getLineItems();
		model.addAttribute("addedProducts", addedProducts.values());
	    System.out.println("addedProducts   code ==================="+addedProducts.size());
		
		storeCmsPageInModel(model, getContentPageForLabelOrId(BUILD_ORDER_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(BUILD_ORDER_PAGE));
		model.addAttribute("breadcrumbs", accountBreadcrumbBuilder.getBreadcrumbs(null));
		model.addAttribute("metaRobots", "no-index,no-follow");


		return REDIRECT_MY_ACCOUNT_BUILD_ORDER;
	}
	
	
	
	@PostMapping(BUILD_ORDER_REMOVE_ITEM_PAGE_URL)
	@RequireHardLogIn
	public String quickOrderDeleteItem(final Model model, @RequestParam("productCode") final String productCode,
			final HttpSession session) throws CMSItemNotFoundException
	{
		final JnjBuildOrderData buildOrder = jnjBuildOrderFacade.getBuildOrderFromSession((JnjBuildOrderData) session
				.getAttribute(JnjBuildOrderFacade.BUILD_ORDER_SESSION_ATTRIBUTE));

		jnjBuildOrderFacade.deleteItemFromBuildOrder(buildOrder, productCode);
		session.setAttribute(JnjBuildOrderFacade.BUILD_ORDER_SESSION_ATTRIBUTE, buildOrder);
		model.addAttribute("orderform", buildOrder);

		storeCmsPageInModel(model, getContentPageForLabelOrId(BUILD_ORDER_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(BUILD_ORDER_PAGE));
		model.addAttribute("breadcrumbs", accountBreadcrumbBuilder.getBreadcrumbs(null));
		model.addAttribute("metaRobots", "no-index,no-follow");

		return REDIRECT_MY_ACCOUNT_BUILD_ORDER;
	}
	
	
	@GetMapping(BUILD_ORDER_SESSION_CLEAR_ITEM_PAGE_URL)
	public void clearSessionItem(final HttpSession session) throws CMSItemNotFoundException
	{
		final JnjBuildOrderData buildOrder = jnjBuildOrderFacade.getBuildOrderFromSession(null);

		  session.setAttribute(JnjBuildOrderFacade.BUILD_ORDER_SESSION_ATTRIBUTE, buildOrder);
	}
	
	
	
	public String getView(final String view){
        return LoginaddonControllerConstants.ADDON_PREFIX + view;
 }
	/*Added by Vijay*/
 	@PostMapping(BUILD_ORDER_PRODCT_VALIDATE_URL)
 	@ResponseBody()
	public Boolean productValidate(final Model model, @RequestParam("productCode") final String productCode,@RequestParam("qty") final String qtyString,
			final HttpSession session){
		Long qty = 0L;
		if (qtyString.equals(""))
		{
			qty = 1L;
		}
		else
		{
			try
			{
				qty = Long.parseLong(qtyString);
			}
			catch (final Exception e)
			{
				LOG.error(e.getLocalizedMessage());
			}
		}
		final OrderEntryData orderEntry = jnjBuildOrderFacade.getProductData(productCode, qty);
		if (orderEntry != null){
			 return Boolean.TRUE;
		}else{
			 return Boolean.FALSE;
		}
       
 } 
	
}
