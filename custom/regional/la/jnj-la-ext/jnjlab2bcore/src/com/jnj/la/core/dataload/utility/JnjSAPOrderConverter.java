
package com.jnj.la.core.dataload.utility;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.dto.order.JnjOrderDTO;
import com.jnj.core.dto.order.JnjOrderLineDTO;
import com.jnj.core.dto.order.JnjOrderSchLineDTO;
import com.jnj.core.enums.JnjOrderTypesEnum;
import com.jnj.core.model.JnjDeliveryScheduleModel;
import com.jnj.core.services.address.JnjGTAddressService;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.exceptions.BusinessException;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants.Order;
import com.jnj.la.core.services.order.JnjLAOrderService;
import com.jnj.la.core.services.order.impl.DefaultJnjLASAPOrdersService;
import com.jnj.la.core.util.JnjLaCommonUtil;
import com.jnj.la.core.util.JnjLaCoreUtil;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.order.strategies.ordercloning.impl.DefaultOrderPartOfMembersCloningStrategy;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.DiscountValue;
import de.hybris.platform.util.TaxValue;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import static com.jnj.la.core.util.JnjLaCommonUtil.getLocaleForCountryIsoCode;

public class JnjSAPOrderConverter {

    private static final String EMPTY_DOUBLE = "0.0";
    private static final String ORDER_CONVERTER = "JnjSAPOrderConverter";
    private static final String ORDER_TYPE_ZSE2 = "ZSE2";
    private static final String ORDER_TYPE_ZSR2 = "ZSR2";
    private static final String UPDATE_DELIVERY_ADDRESS = "updateDeliveryAddress()";
    private static final String IS_SITE_PRODUCTION = "is.site.production";
    private static final String CENCA = "cenca";
    private static final String BR = "br";
     
    @Autowired
    private KeyGenerator orderCodeGenerator;

    @Autowired
    private UserService userService;

    @Autowired
    private JnjLAOrderService jnjLAOrderService;

    @Autowired
    private DefaultJnjLASAPOrdersService jnjSAPOrdersService;

    @Autowired
    private JnjGTAddressService jnjGTAddressService;

    @Autowired
    private DefaultOrderPartOfMembersCloningStrategy defaultOrderPartOfMembersCloningStrategy;
    
	private CommonI18NService commonI18NService;
    
    private BaseStoreService baseStoreService;
    
    private BaseSiteService baseSiteService;
    
    private static ConfigurationService configurationService;
      
    /**
     * Creates new version of JnJ Order.
     *
     * @param order    the order
     * @param sapOrder the sap order
     * @throws BusinessException the business exception
     */
    public void createNewOrder(final OrderModel order, final JnjOrderDTO sapOrder) throws BusinessException {
        final String METHOD_NAME = "createNewOrder()";
        JnjGTCoreUtil.logDebugMessage(ORDER_CONVERTER, METHOD_NAME, Logging.BEGIN_OF_METHOD, JnjSAPOrderConverter.class);
        order.setSapOrderNumber(sapOrder.getSAPOrderNumber());
        order.setCode(generateOrderCode());
        order.setDate(JnjLaCommonUtil.getRsaDate(sapOrder.getStartDate(), Order.RSA_DATE_FORMAT));
        convertFromSAPOrder(order, sapOrder, true);
        JnjGTCoreUtil.logDebugMessage(ORDER_CONVERTER, METHOD_NAME, Logging.END_OF_METHOD, JnjSAPOrderConverter.class);
    }

    private String generateOrderCode() {
        final String METHOD_NAME = "generateOrderCode()";
        JnjGTCoreUtil.logDebugMessage(ORDER_CONVERTER, METHOD_NAME, Logging.BEGIN_OF_METHOD, JnjSAPOrderConverter.class);
        final Object generatedValue = orderCodeGenerator.generate();
        if (generatedValue instanceof String) {
            JnjGTCoreUtil.logDebugMessage(ORDER_CONVERTER, METHOD_NAME, Logging.END_OF_METHOD, JnjSAPOrderConverter.class);
            return (String) generatedValue;
        } else {
            JnjGTCoreUtil.logDebugMessage(ORDER_CONVERTER, METHOD_NAME, Logging.END_OF_METHOD, JnjSAPOrderConverter.class);
            return String.valueOf(generatedValue);
        }
    }


    /**
     * Updates an existing version of JnJ order.
     *
     * @param order    the order
     * @param sapOrder the sap order
     * @throws BusinessException the business exception
     */
    public void updateExistingOrder(final OrderModel order, final JnjOrderDTO sapOrder) throws BusinessException {
        convertFromSAPOrder(order, sapOrder, false);
    }

    private void convertFromSAPOrder(final OrderModel order, final JnjOrderDTO sapOrder, final Boolean isCreating) throws BusinessException {
        final String METHOD_NAME = "convertFromSAPOrder()";
        JnjGTCoreUtil.logDebugMessage(ORDER_CONVERTER, METHOD_NAME, Logging.BEGIN_OF_METHOD, JnjSAPOrderConverter.class);
        try {
            final B2BUnitModel unit = getB2bUnit(sapOrder.getSoldToNumber());
            if (unit == null) {
                final String errorMessage = "Customer " + sapOrder.getSoldToNumber() + " is not in the platform";
                JnjGTCoreUtil.logWarnMessage(ORDER_CONVERTER, METHOD_NAME, errorMessage, JnjSAPOrderConverter.class);
                throw new BusinessException(errorMessage);
            }
            order.setUnit(unit);
            if (null == order.getUser()) {
                final UserModel user = userService.getUserForUID(Jnjlab2bcoreConstants.UpsertCustomer.DUMMY_USER);
                if (null != user) {
                    order.setUser(user);
                } else {
                    final String errorMessage = "No valid User Found for B2bUnit with Uid[" + sapOrder.getSoldToNumber() + "].";
                    JnjGTCoreUtil.logWarnMessage(ORDER_CONVERTER, METHOD_NAME, errorMessage, JnjSAPOrderConverter.class);
                    throw new BusinessException(errorMessage);
                }
            }
			if(order.getSapOrderNumber() == null && sapOrder.getSAPOrderNumber() != null)
            {
            	order.setSapOrderNumber(sapOrder.getSAPOrderNumber());
            }
            updateDeliveryAddress(sapOrder.getShipToNumber(), order);
            order.setPurchaseOrderNumber(sapOrder.getCustomerPONumber());
            order.setNamedDeliveryDate(JnjLaCommonUtil.getRsaDate(sapOrder.getNamedDeliveryDate(), Order.RSA_DATE_FORMAT));
            order.setSalesOrganizationCode(sapOrder.getSalesOrganizationCode());
            order.setForbiddenSales(sapOrder.getForbiddenSales());
            order.setContractNumber(sapOrder.getContractNumber());
            order.setCompleteDelivery(sapOrder.getCompleteDelivery());
            if (isCreating) {
                // The status is initially saved as PENDING
                // Later, in another cronjob, is updated to its definitive status
                order.setStatus(OrderStatus.PENDING);
            } else {
                order.setStatus(jnjLAOrderService.calculateOrderHeaderStatus(order));
            }
            order.setCurrency(jnjLAOrderService.getCurrentCurrency(sapOrder.getSalesOrganizationCode()));

            order.setDate(JnjLaCommonUtil.getRsaDate(sapOrder.getStartDate(), Order.RSA_DATE_FORMAT));
            order.setTotalNetValue(Double.valueOf(ObjectUtils.defaultIfNull(sapOrder.getTotalNetPrice(), EMPTY_DOUBLE)));
            order.setSubtotal(Double.valueOf(ObjectUtils.defaultIfNull(sapOrder.getTotalNetPrice(), EMPTY_DOUBLE)));
            setOrderDetailsDownloadFields(order, sapOrder);
            order.setSalesOrderRejectionStatus(sapOrder.getSalesOrderRejectionStatus());
            order.setSalesOrderCreditStatus(sapOrder.getSalesOrderCreditStatus());    
            
            setLanguageCurrencyBaseSite(order,sapOrder);         
            
            setOrderType(order, sapOrder);
            updateCreditHoldOnOrder(order);
        } catch (final BusinessException e) {
            throw e;
        } catch (final Exception e) {
            JnjGTCoreUtil.logErrorMessage(ORDER_CONVERTER, METHOD_NAME, "convertFromSAPOrder(): An error occurred while converting from SAPOrder." + "Exception: " + e.getMessage(), e, JnjSAPOrderConverter.class);
            throw new BusinessException(e.getMessage());
        }

        JnjGTCoreUtil.logDebugMessage(ORDER_CONVERTER, METHOD_NAME, Logging.END_OF_METHOD, JnjSAPOrderConverter.class);
    }

    private void setOrderType(OrderModel order, JnjOrderDTO sapOrder) throws BusinessException {
        if (StringUtils.isNotEmpty(sapOrder.getDeliveryMode())) {

            final JnjOrderTypesEnum type;
            if (ORDER_TYPE_ZSE2.equalsIgnoreCase(sapOrder.getDeliveryMode()) || ORDER_TYPE_ZSR2.equalsIgnoreCase(sapOrder.getDeliveryMode())) {
                type = JnjOrderTypesEnum.ZSER2;
            } else {
                type = JnjOrderTypesEnum.valueOf(sapOrder.getDeliveryMode());
            }

            order.setOrderType(type);
            order.setJnjOrderType(jnjLAOrderService.getOrderType(type.getCode()));
        }
    }

    private void updateCreditHoldOnOrder(final OrderModel order) {
        final String METHOD_NAME = "updateCreditHoldOnOrder()";
        JnjGTCoreUtil.logDebugMessage(ORDER_CONVERTER, METHOD_NAME, Logging.BEGIN_OF_METHOD, JnjSAPOrderConverter.class);

        if (StringUtils.equalsIgnoreCase(order.getSalesOrderRejectionStatus(), Config.getParameter(Jnjb2bCoreConstants.OUTSALESORDERREJECTIONSTATUS_B))
            || StringUtils.equalsIgnoreCase(order.getSalesOrderRejectionStatus(), Config.getParameter(Jnjb2bCoreConstants.OUTSALESORDERREJECTIONSTATUS_C))
            || StringUtils.equalsIgnoreCase(order.getSalesOrderCreditStatus(), Config.getParameter(Jnjb2bCoreConstants.OUTSALESORDERREJECTIONSTATUS_B))
            || StringUtils.equalsIgnoreCase(order.getSalesOrderCreditStatus(), Config.getParameter(Jnjb2bCoreConstants.OUTSALESORDERREJECTIONSTATUS_C))) {

            order.setHoldCreditCardFlag(Boolean.TRUE);
        } else {
            order.setHoldCreditCardFlag(Boolean.FALSE);
        }

        JnjGTCoreUtil.logDebugMessage(ORDER_CONVERTER, METHOD_NAME, Logging.END_OF_METHOD, JnjSAPOrderConverter.class);
    }

    /**
     * Creates the new order entry.
     *
     * @param existingOrder the existing order
     * @param newOrderEntry the new order entry
     * @param sapOrderEntry the sap order entry
     * @throws BusinessException the business exception
     */
    public void createNewOrderEntry(final OrderModel existingOrder, final OrderEntryModel newOrderEntry, final JnjOrderLineDTO sapOrderEntry) throws BusinessException {
        convertFromSAPOrderEntry(existingOrder, newOrderEntry, sapOrderEntry);
    }

    private void convertFromSAPOrderEntry(final OrderModel order, final OrderEntryModel orderEntry, final JnjOrderLineDTO sapOrderEntry) throws BusinessException {
        final String METHOD_NAME = "convertFromSAPOrderEntry()";
        JnjGTCoreUtil.logDebugMessage(ORDER_CONVERTER, METHOD_NAME, Logging.BEGIN_OF_METHOD, JnjSAPOrderConverter.class);

        try {

            final CountryModel countryModel = getValidCountry(order);

            final Double taxValue = defineTaxValues(orderEntry, sapOrderEntry, countryModel);

            final Double discountValue = Double.valueOf(ObjectUtils.defaultIfNull(sapOrderEntry.getDiscount(), EMPTY_DOUBLE));
            final Double freightCharges = Double.valueOf(ObjectUtils.defaultIfNull(sapOrderEntry.getFreightcharges(),EMPTY_DOUBLE));
            final Double expeditedFee = Double.valueOf(ObjectUtils.defaultIfNull(sapOrderEntry.getExpeditedFee(), EMPTY_DOUBLE));
            final Double handlingFee = Double.valueOf(ObjectUtils.defaultIfNull(sapOrderEntry.getHandlingFee(), EMPTY_DOUBLE));
            final Double minimumOrderFee = Double.valueOf(ObjectUtils.defaultIfNull(sapOrderEntry.getMinimumOrderFee(), EMPTY_DOUBLE));
            final Double dropShipFee = Double.valueOf(ObjectUtils.defaultIfNull(sapOrderEntry.getDropShipFee(), EMPTY_DOUBLE));
            final Double insurance = Double.valueOf(ObjectUtils.defaultIfNull(sapOrderEntry.getInsurance(), EMPTY_DOUBLE));
            final Double internationalFreight = Double.valueOf(ObjectUtils.defaultIfNull(sapOrderEntry.getInternationalFreight(), EMPTY_DOUBLE));
            final Double grossPrice = Double.valueOf(ObjectUtils.defaultIfNull(sapOrderEntry.getGrossPrice(), EMPTY_DOUBLE));
            final Double taxValueForGrossPrice = Double.valueOf(ObjectUtils.defaultIfNull(sapOrderEntry.getTax(), EMPTY_DOUBLE));
            final Double entryTotalFee = minimumOrderFee + dropShipFee + handlingFee + expeditedFee;

            orderEntry.setQuantity(Double.valueOf(sapOrderEntry.getQuantity()).longValue());

            final Double basePrice = Double.valueOf(sapOrderEntry.getUnitPrice());
            final Double entryTotalPrice = Double.valueOf(ObjectUtils.defaultIfNull(sapOrderEntry.getNetPrice(), EMPTY_DOUBLE));
            orderEntry.setBasePrice(basePrice);
            orderEntry.setTotalPrice(entryTotalPrice);

            orderEntry.setDiscountValues(createDiscountValues(discountValue, countryModel));

            setUnit(orderEntry, sapOrderEntry.getSalesUOM());

            orderEntry.setFreightcharges(freightCharges);
            orderEntry.setExpeditedFee(expeditedFee);
            orderEntry.setHandlingFee(handlingFee);
            orderEntry.setMinimumOrderFee(minimumOrderFee);
            orderEntry.setDropshipFee(dropShipFee);
            orderEntry.setInsurance(insurance);
            orderEntry.setInternationalFreight(internationalFreight);
            orderEntry.setGrossPrice(grossPrice);
            orderEntry.setIndirectCustomer(sapOrderEntry.getIndirectCustomer());

            order.setTotalExpeditedFees(order.getTotalExpeditedFees() + expeditedFee);
            order.setTotalFee(order.getTotalFee() + entryTotalFee);
            order.setTotalHandlingFee(order.getTotalHandlingFee() + handlingFee);
            order.setTotalDropShipFee(order.getTotalDropShipFee() + dropShipFee);
            order.setTotalminimumOrderFee(order.getTotalminimumOrderFee() + minimumOrderFee);
            order.setTotalFreightFees(order.getTotalFreightFees() + freightCharges);
            order.setTotalPrice(order.getTotalPrice() + entryTotalPrice);
            order.setTotalTax(order.getTotalTax() + taxValue);
            order.setTotalDiscounts(order.getTotalDiscounts() + discountValue);
            order.setTotalInsurance(order.getTotalInsurance() + insurance);

            final Double existingGrossPrice = order.getTotalGrossPrice();
            if (Double.compare(existingGrossPrice, 0.0D) != 0){
                order.setTotalGrossPrice(existingGrossPrice + taxValueForGrossPrice);
            } else {
                order.setTotalGrossPrice(taxValueForGrossPrice + order.getTotalNetValue());
            }

            orderEntry.setGTSHold(sapOrderEntry.getGTSHold());
            orderEntry.setReasonForRejection(sapOrderEntry.getReasonForRejection());
            orderEntry.setLineOverallStatus(sapOrderEntry.getLineOverallStatus());
            orderEntry.setDeliveryStatus(sapOrderEntry.getDeliveryStatus());
            orderEntry.setLineInvoiceStatus(sapOrderEntry.getLineInvoiceStatus());
            orderEntry.setMaterialEntered(sapOrderEntry.getMaterialEntered());
            orderEntry.setSapOrderlineNumber(sapOrderEntry.getEntryNumber());
        } catch (final Exception e) {
            JnjGTCoreUtil.logErrorMessage(ORDER_CONVERTER, METHOD_NAME, "convertFromSAPOrderEntry(): An error occurred while converting from SAPOrderEntry: " + e.getMessage(), e, JnjSAPOrderConverter.class);
            throw new BusinessException(e.getMessage());
        }

        JnjGTCoreUtil.logDebugMessage(ORDER_CONVERTER, METHOD_NAME, Logging.END_OF_METHOD, JnjSAPOrderConverter.class);
    }

    private CountryModel getValidCountry(OrderModel order) throws BusinessException {
        final B2BUnitModel b2bUnitModel = order.getUnit();
        if (null == b2bUnitModel) {
            throw new BusinessException("No B2bUnit associated with Order having order No [" + order.getCode() + "]");
        }

        final CountryModel countryModel = b2bUnitModel.getCountry();
        if (null == countryModel) {
            throw new BusinessException("No Country associated with B2bUnit with ID [" + b2bUnitModel.getUid() + "] for Order having order No [" + order.getCode() + "]");
        }
        return countryModel;
    }

    private Double defineTaxValues(OrderEntryModel orderEntry, JnjOrderLineDTO sapOrderEntry, CountryModel countryModel) {
        Double taxValue;
        final List<String> taxesLocalCountriesList = JnjLaCoreUtil.getCountriesList(Jnjlab2bcoreConstants.KEY_TAXES_LOCAL_COUNTRIES);
        if (JnjLaCoreUtil.isCountryValidForACountriesList(countryModel.getIsocode(), taxesLocalCountriesList)) {
            taxValue = Double.valueOf(ObjectUtils.defaultIfNull(sapOrderEntry.getTaxesLocal(), EMPTY_DOUBLE));
        } else {
            taxValue = Double.valueOf(ObjectUtils.defaultIfNull(sapOrderEntry.getTax(), EMPTY_DOUBLE));
        }
        orderEntry.setTaxValues(createTaxValues(taxValue, countryModel));
        return taxValue;
    }

    /**
     * Converts an SAP schedule line into a Hybris based delivery scheduled line.
     *
     * @param orderEntry       the existing order entry
     * @param deliverySchedule the delivery schedule
     * @param scheduleLine     the schedule line
     * @throws BusinessException the business exception
     */
    public void convertFromSAPScheduleLine(final OrderEntryModel orderEntry, final JnjDeliveryScheduleModel deliverySchedule, final JnjOrderSchLineDTO scheduleLine) throws BusinessException {
        final String METHOD_NAME = "convertFromSAPScheduleLine()";
        JnjGTCoreUtil.logDebugMessage(ORDER_CONVERTER, METHOD_NAME, Logging.BEGIN_OF_METHOD, JnjSAPOrderConverter.class);

        try {
            deliverySchedule.setOwnerEntry(orderEntry);
            deliverySchedule.setLineNumber(scheduleLine.getLineNumber());
            deliverySchedule.setDeliveryDate(JnjLaCommonUtil.getRsaDate(scheduleLine.getDeliveryDate(), Order.RSA_DATE_FORMAT));

            if (StringUtils.isNotBlank(scheduleLine.getProofOfDeliveryDate())) {
                deliverySchedule.setProofOfDeliveryDate(JnjLaCommonUtil.getRsaDate(scheduleLine.getProofOfDeliveryDate(), Order.RSA_DATE_FORMAT));
            }

            if (StringUtils.isNotBlank(scheduleLine.getCarrierExpectedDeliveryDate())) {
                deliverySchedule.setCarrierExpectedDeliveryDate(JnjLaCommonUtil.getRsaDate(
                        scheduleLine.getCarrierExpectedDeliveryDate(), Order.RSA_DATE_FORMAT));
            }

            final Long quantity = Double.valueOf(scheduleLine.getConfirmedQuantity()).longValue();
            final Long roundedQuantity = Double.valueOf(scheduleLine.getRoundedQuantity()).longValue();
            deliverySchedule.setRoundedQuantity(roundedQuantity.toString());
            deliverySchedule.setQty(quantity);
            deliverySchedule.setOwnerEntry(orderEntry);
        } catch (Exception e) {
            JnjGTCoreUtil.logErrorMessage(ORDER_CONVERTER, METHOD_NAME, "convertFromSAPScheduleLine(): An error occurred while converting from SAPScheduleLine. Exception: ", e, JnjSAPOrderConverter.class);
            throw new BusinessException(e.getMessage());
        }

        JnjGTCoreUtil.logDebugMessage(ORDER_CONVERTER, METHOD_NAME, Logging.END_OF_METHOD, JnjSAPOrderConverter.class);
    }

    private B2BUnitModel getB2bUnit(final String shipToNumber) {
        final String METHOD_NAME = "getB2bUnit()";
        JnjGTCoreUtil.logDebugMessage(ORDER_CONVERTER, METHOD_NAME, Logging.BEGIN_OF_METHOD, JnjSAPOrderConverter.class);
        B2BUnitModel b2bUnit = null;
        try {
            b2bUnit = jnjSAPOrdersService.getUnitForUid(shipToNumber);
        } catch (final Exception e) {
            JnjGTCoreUtil.logErrorMessage(ORDER_CONVERTER, METHOD_NAME, "Error retrieving UnitForUid. Exception: " + e.getMessage(), e, JnjSAPOrderConverter.class);
        }

        JnjGTCoreUtil.logDebugMessage(ORDER_CONVERTER, METHOD_NAME, Logging.END_OF_METHOD, JnjSAPOrderConverter.class);
        return b2bUnit;
    }

    private void updateDeliveryAddress(final String shipToNumber, final OrderModel order) {
        JnjGTCoreUtil.logDebugMessage(ORDER_CONVERTER, UPDATE_DELIVERY_ADDRESS, Logging.BEGIN_OF_METHOD, JnjSAPOrderConverter.class);
        try {
            final List<AddressModel> incomingAddresses = jnjGTAddressService.getAddressByIdandOnwerType(shipToNumber);
            if (!CollectionUtils.isEmpty(incomingAddresses)) {
                setIncomingShipToAddress(shipToNumber, order, incomingAddresses);
            }

            JnjGTCoreUtil.logDebugMessage(ORDER_CONVERTER, UPDATE_DELIVERY_ADDRESS, "Incoming Ship To Address with ID [" + shipToNumber + "] does not exist in Hybris for Order [" + order.getSapOrderNumber() + "]", JnjSAPOrderConverter.class);
        } catch (final Exception exception) {
            JnjGTCoreUtil.logErrorMessage(ORDER_CONVERTER, UPDATE_DELIVERY_ADDRESS, "Exception Occurred While Setting/Updating Delivery Address for Order [ " + order.getSapOrderNumber() + "]. " + Logging.HYPHEN + exception.getLocalizedMessage(), exception, JnjSAPOrderConverter.class);
        }

        JnjGTCoreUtil.logDebugMessage(ORDER_CONVERTER, UPDATE_DELIVERY_ADDRESS, Logging.END_OF_METHOD, JnjSAPOrderConverter.class);
    }

    private void setIncomingShipToAddress(String shipToNumber, OrderModel order, List<AddressModel> incomingAddresses) {
        final AddressModel incomingShipToAddress = incomingAddresses.get(0);
        if (null != incomingShipToAddress && incomingShipToAddress.getShippingAddress()) {
            if (defaultOrderPartOfMembersCloningStrategy.addressNeedsCloning(incomingShipToAddress, order)) {
                final AddressModel deliveryAddressClone = defaultOrderPartOfMembersCloningStrategy.cloneAddressForOrder(incomingShipToAddress, order);
                order.setDeliveryAddress(deliveryAddressClone);
            }
            JnjGTCoreUtil.logDebugMessage(ORDER_CONVERTER, UPDATE_DELIVERY_ADDRESS, "No Address Update required for address with ID [" + shipToNumber + "] for Order [" + order.getSapOrderNumber() + "]", JnjSAPOrderConverter.class);
        }
        JnjGTCoreUtil.logDebugMessage(ORDER_CONVERTER, UPDATE_DELIVERY_ADDRESS, "No Valid Ship To Address with ID [" + shipToNumber + "] found in Hybris for Order [" + order.getSapOrderNumber() + "]", JnjSAPOrderConverter.class);
    }

    private void setUnit(final OrderEntryModel orderEntry, final String salesUOM) {
        orderEntry.setUnit(jnjSAPOrdersService.getUnitOfMeasurement(salesUOM));
    }

    private void setOrderDetailsDownloadFields(final OrderModel order, final JnjOrderDTO sapOrder) {
        final String METHOD_NAME = "setOrderDetailsDownloadFields()";
        JnjGTCoreUtil.logDebugMessage(ORDER_CONVERTER, METHOD_NAME, Logging.BEGIN_OF_METHOD, JnjSAPOrderConverter.class);

        order.setPayFromNumber(sapOrder.getPayFromNumber());
        order.setDistributionChannel(sapOrder.getDistributionChannel());
        order.setDivision(sapOrder.getDivision());
        order.setSalesOrderOverallStatus(sapOrder.getSalesOrderOverallStatus());
        order.setSalesOrderDeliveryStatus(sapOrder.getSalesOrderDeliveryStatus());
        order.setSalesOrderRejectionStatus(sapOrder.getSalesOrderRejectionStatus());
        order.setSalesOrderCreditStatus(sapOrder.getSalesOrderCreditStatus());
        order.setInvoiceStatus(sapOrder.getInvoiceStatus());
        order.setSalesOrderDataCompleteness(sapOrder.getSalesOrderDataCompleteness());
        order.setHeaderDeliveryBlock(sapOrder.getHeaderDeliveryBlock());
        try {
            order.setOrderChannel(jnjLAOrderService.getOrderChannel(sapOrder.getPoType()));
        } catch (final BusinessException businessException) {
            JnjGTCoreUtil.logWarnMessage(ORDER_CONVERTER, METHOD_NAME, "Cannot Fetch Order Channel for order [" + order.getSapOrderNumber() + "]." + Logging.HYPHEN + businessException.getLocalizedMessage(), businessException, JnjSAPOrderConverter.class);
        }
        JnjGTCoreUtil.logDebugMessage(ORDER_CONVERTER, METHOD_NAME, Logging.END_OF_METHOD, JnjSAPOrderConverter.class);
    }

    private Collection<TaxValue> createTaxValues(final Double taxValue, final CountryModel countryModel) {
        final TaxValue tax = new TaxValue(Jnjb2bCoreConstants.Order.TAX_VALUE, 0.0D, false, taxValue, getCountryIsoCode(countryModel));
        final Collection<TaxValue> taxValues = new ArrayList<>();
        taxValues.add(tax);
        return taxValues;
    }

    private List<DiscountValue> createDiscountValues(final Double discountValue, final CountryModel countryModel) {
        final DiscountValue discount = new DiscountValue(Jnjb2bCoreConstants.Order.DISCOUNT_VALUE, 0.0D, false, discountValue, getCountryIsoCode(countryModel));
        final List<DiscountValue> discountValues = new ArrayList<>();
        discountValues.add(discount);
        return discountValues;
    }

    private String getCountryIsoCode(CountryModel countryModel) {
        if (countryModel == null) {
            return null;
        } else {
            return countryModel.getIsocode();
        }
    } 
    
    private void setLanguageCurrencyBaseSite(final OrderModel order, final JnjOrderDTO sapOrder)
    {        
        final String METHOD_NAME = "setLanguageCurrencyBaseSite()";        
        final String isoCode= sapOrder.getSalesOrganizationCode().substring(0, 2);            
        JnjGTCoreUtil.logInfoMessage(ORDER_CONVERTER, METHOD_NAME, isoCode, JnjSAPOrderConverter.class);
        final String language = getLocaleForCountryIsoCode(isoCode.toLowerCase(Locale.ENGLISH)).getLanguage();            
        order.setLanguage(commonI18NService.getLanguage(language));
        final String lcIsoCode=isoCode.toLowerCase(Locale.ENGLISH);
        final String countryIsoCode=JnjLaCommonUtil.getCountryOrCenca(lcIsoCode);
        final String baseStore = countryIsoCode.concat("BaseStore");
        JnjGTCoreUtil.logInfoMessage(ORDER_CONVERTER, METHOD_NAME, "baseStore: "+baseStore, JnjSAPOrderConverter.class);
        order.setStore(baseStoreService.getBaseStoreForUid(baseStore));
		if (isProductionSite() && StringUtils.equalsAnyIgnoreCase(countryIsoCode,BR,CENCA)) {
			order.setSite(baseSiteService.getBaseSiteForUID(countryIsoCode + "CMSite"));
		} else {
			order.setSite(baseSiteService.getBaseSiteForUID(countryIsoCode + "CMSsite"));
		}   
    }
   
    private static boolean isProductionSite(){
    	return configurationService.getConfiguration().getBoolean(IS_SITE_PRODUCTION);
    }
    public void setBaseSiteService(BaseSiteService baseSiteService) {
        this.baseSiteService = baseSiteService;
    }

    public void setBaseStoreService(BaseStoreService baseStoreService) {
        this.baseStoreService = baseStoreService;
    }
    
    public void setCommonI18NService(CommonI18NService commonI18NService) {
        this.commonI18NService = commonI18NService;
    }

	public static ConfigurationService getConfigurationService() {
		return configurationService;
	}
     
	public static void setConfigurationService(final ConfigurationService configurationService) {
		JnjSAPOrderConverter.configurationService = configurationService;
	}
}
