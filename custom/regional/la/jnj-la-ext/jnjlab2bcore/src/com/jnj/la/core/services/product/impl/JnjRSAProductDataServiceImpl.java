package com.jnj.la.core.services.product.impl;

import com.jnj.core.model.JnjIntegrationRSACronJobModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.apache.commons.collections4.CollectionUtils;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.dto.JnJProductDescriptionDTO;
import com.jnj.core.dto.JnjProductDetailsDTO;
import com.jnj.core.dto.JnjUomConversionDTO;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.dao.product.JnjRSAProductDao;
import com.jnj.la.core.dto.JnJLaProductDTO;
import com.jnj.la.core.dto.JnjProductSalesOrgDTO;
import com.jnj.la.core.dto.product.JnjRSAProductDTO;
import com.jnj.la.core.dto.product.JnjRSAProductDescriptionDTO;
import com.jnj.la.core.dto.product.JnjRSAProductSalesOrgDTO;
import com.jnj.la.core.dto.product.JnjRSAProductUnitDTO;
import com.jnj.la.core.services.product.JnjRSAProductDataService;
import com.jnj.la.core.util.JnjLaCoreUtil;

import de.hybris.platform.servicelayer.config.ConfigurationService;


public class JnjRSAProductDataServiceImpl implements JnjRSAProductDataService
{
	private static final Logger LOG = Logger.getLogger(JnjRSAProductDataServiceImpl.class);

	protected JnjRSAProductDao jnjRSAProductDao;

	protected ConfigurationService configurationService;

	@Override
	public List<JnJLaProductDTO> pullProductsFromRSA(final Date lowerDate, final Date upperDate, final JnjIntegrationRSACronJobModel cronJob, final String sector)
	{
		final String methodName = "pullProductsFromRSA()";
		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_PRODUCT_NAME, methodName, Logging.BEGIN_OF_METHOD,
				JnjRSAProductDataServiceImpl.class);
        LOG.debug("sector - "+ sector);
		final List<JnjRSAProductDTO> rsaProducts = jnjRSAProductDao.getProducts(lowerDate, upperDate, cronJob, sector);
		if (null != rsaProducts) {
		LOG.info("Testing Product count - "+rsaProducts.size());
		}

		final List<JnJLaProductDTO> jnjProducts = new ArrayList<>();
		if (rsaProducts != null && !rsaProducts.isEmpty())
		{
			mapJnJProduct(rsaProducts, jnjProducts);
		}
		else
		{
			JnjGTCoreUtil.logInfoMessage(Logging.UPSERT_PRODUCT_NAME, methodName, "No data pulled from RSA material view",
					JnjRSAProductDataServiceImpl.class);
		}

		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_PRODUCT_NAME, methodName, Logging.END_OF_METHOD,
				JnjRSAProductDataServiceImpl.class);
		return jnjProducts;
	}

	@Override
	public List<String> getUniqueDivisionsFromRSA(final Date lowerDate, final Date upperDate, final String sector) {
		final String methodName = "getUniqueDivisionsFromRSA()";
		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_PRODUCT_NAME, methodName, Logging.BEGIN_OF_METHOD,
				JnjRSAProductDataServiceImpl.class);

		final List<String> rsaProductDivisions = jnjRSAProductDao.getUniqueDivisionsFromRSA(lowerDate, upperDate, sector);

		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_PRODUCT_NAME, methodName, Logging.END_OF_METHOD,
				JnjRSAProductDataServiceImpl.class);

		return rsaProductDivisions;
	}

	@Override
	public Date getLastUpdatedDateForLatestRecord() {
		return jnjRSAProductDao.getLastUpdatedDateForLatestRecord();
	}

	private void mapJnJProduct(final List<JnjRSAProductDTO> rsaProducts, final List<JnJLaProductDTO> jnjProducts)
	{
		final String methodName = "populateJnJProduct";
		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_PRODUCT_NAME, methodName, Logging.BEGIN_OF_METHOD,
				JnjRSAProductDataServiceImpl.class);
		
		List<JnjRSAProductSalesOrgDTO> jnjRsaProductSalesOrgList = new ArrayList<>();
		List<JnjRSAProductDescriptionDTO> jnjRsaProductDescriptionList = new ArrayList<>();
		List<JnjRSAProductUnitDTO> jnjRSAUomConversionList = new ArrayList<>();
		final int productCountLimit = configurationService.getConfiguration().getInt(Jnjlab2bcoreConstants.PRODUCT_COUNT_LIMIT, 2000);
		if (CollectionUtils.isNotEmpty(rsaProducts)) {

			List<String> products = new ArrayList<>();
			for (final JnjRSAProductDTO rsaProduct : rsaProducts) {
				products.add(rsaProduct.getMaterialNum());
			}

			if (products.size() <= productCountLimit) {
				jnjRsaProductSalesOrgList.addAll(getProductSalesOrgs(products));
				jnjRsaProductDescriptionList.addAll(getProductDescription(products));
				jnjRSAUomConversionList.addAll(getProductUOM(products));

			} else {
				int prodListSize = rsaProducts.size();
				int count = prodListSize / productCountLimit;
				LOG.debug("count "+count);
			
				for (int i = 0; i <= count + 1; i++) {	
					
					if ((productCountLimit * i)> prodListSize) {
						break;
					}						
					int startIndex = productCountLimit * i;
					int endIndex = calculateEndIndex(productCountLimit, prodListSize, i);
					
					List<String> subProductList = products.subList(startIndex, endIndex);
					jnjRsaProductSalesOrgList.addAll(getProductSalesOrgs(subProductList));
					jnjRsaProductDescriptionList.addAll(getProductDescription(subProductList));
					jnjRSAUomConversionList.addAll(getProductUOM(subProductList));				   
				   
				}
				LOG.debug("sales org size "+jnjRsaProductSalesOrgList.size());
			}

		}


		populateProductDTO(rsaProducts, jnjProducts, jnjRsaProductSalesOrgList,
				jnjRsaProductDescriptionList, jnjRSAUomConversionList);
		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_PRODUCT_NAME, methodName, Logging.END_OF_METHOD,
				JnjRSAProductDataServiceImpl.class);
	}

	private List<JnjRSAProductSalesOrgDTO> getProductSalesOrgs(final List<String> subProductList) {
		List<JnjRSAProductSalesOrgDTO> salesOrgs = jnjRSAProductDao.getProductSalesOrg(subProductList);
	    return CollectionUtils.isNotEmpty(salesOrgs)?salesOrgs:new ArrayList<>();
	}
	
	private List<JnjRSAProductDescriptionDTO> getProductDescription(final List<String> subProductList) {
		List<JnjRSAProductDescriptionDTO> descriptions = jnjRSAProductDao.getProductDescription(subProductList);
	    return CollectionUtils.isNotEmpty(descriptions)?descriptions:new ArrayList<>();
	}
	
	private List<JnjRSAProductUnitDTO> getProductUOM(final List<String> subProductList) {
		List<JnjRSAProductUnitDTO> uomList = jnjRSAProductDao.getProductUomConversion(subProductList);		   	
		return CollectionUtils.isNotEmpty(uomList)?uomList:new ArrayList<>();
	}
	
	private int calculateEndIndex(final int productCountLimit, final int prodListSize, final int loopIteration) {
		return (productCountLimit * (loopIteration + 1))<prodListSize?(productCountLimit * (loopIteration + 1)):prodListSize;
	}
	
	private void populateProductDTO(final List<JnjRSAProductDTO> rsaProducts, final List<JnJLaProductDTO> jnjProducts,
			List<JnjRSAProductSalesOrgDTO> jnjRsaProductSalesOrgList,
			List<JnjRSAProductDescriptionDTO> jnjRsaProductDescriptionList,
			List<JnjRSAProductUnitDTO> jnjRSAUomConversionList) {
		
		final String methodName = "populateProductDTO";
		
		for (final JnjRSAProductDTO rsaProduct : rsaProducts)
		{
			final JnJLaProductDTO jnjProduct = new JnJLaProductDTO();

			jnjProduct.setBaseUnit(rsaProduct.getBaseUnitCd());
			jnjProduct.setCatalogId(rsaProduct.getProdCd());
			jnjProduct.setCategory(rsaProduct.getCategoryCd());
			jnjProduct.setCode(rsaProduct.getMaterialNum());
			jnjProduct.setFranchise(rsaProduct.getFranchiseCd());
			jnjProduct.setMaterialType(rsaProduct.getMaterialType());
			jnjProduct.setOriginCountry(rsaProduct.getOriginCountry());
			jnjProduct.setEan(rsaProduct.getMaterialBaseNum());
			jnjProduct.setSector(rsaProduct.getBusinessSector());
			jnjProduct.setLastUpdateDate(rsaProduct.getLastUpdatedDate());
			jnjProduct.setDivision(rsaProduct.getDivision());

			JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_PRODUCT_NAME, methodName, "Logging division " + rsaProduct.getDivision()
							+  " for material : "+rsaProduct.getProdCd(), JnjRSAProductDataServiceImpl.class);

			final JnjProductDetailsDTO jnjProductDetailsDto = new JnjProductDetailsDTO();
			jnjProductDetailsDto.setCountry(rsaProduct.getOriginCountry());
			jnjProductDetailsDto.setVrkmeUnitMeasure(rsaProduct.getBaseUnitCd());
			if (jnjProduct.getJnJProductDetailsDTO() != null)
			{
				jnjProduct.getJnJProductDetailsDTO().add(jnjProductDetailsDto);
			}
			else
			{
				final List<JnjProductDetailsDTO> jnjProductDetailsDTOList = new ArrayList<>();
				jnjProductDetailsDTOList.add(jnjProductDetailsDto);
				jnjProduct.setJnJProductDetailsDTO(jnjProductDetailsDTOList);
			}

			List<JnjRSAProductSalesOrgDTO> productSalesOrgList = jnjRsaProductSalesOrgList.stream().filter( i -> i.getMaterialNum().equals(rsaProduct.getMaterialNum())).toList();
			List<JnjRSAProductDescriptionDTO> productDescriptionList = jnjRsaProductDescriptionList.stream().filter( i -> i.getMaterialNum().equals(rsaProduct.getMaterialNum())).toList();
			List<JnjRSAProductUnitDTO> productUomConversionList = jnjRSAUomConversionList.stream().filter( i -> i.getMaterialNum().equals(rsaProduct.getMaterialNum())).toList();
			
			mapJnjProductLists(jnjProduct, productSalesOrgList, productDescriptionList, productUomConversionList);

			jnjProducts.add(jnjProduct);
		}
	}
	
	
	private void mapJnjProductLists(final JnJLaProductDTO jnjProduct, final List<JnjRSAProductSalesOrgDTO> jnjRsaProductSalesOrgList, final List<JnjRSAProductDescriptionDTO> jnjRsaProductDescriptionList, final List<JnjRSAProductUnitDTO> jnjRSAUomConversionList)
	{
		final String methodName = "mapJnjProductLists";
		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_PRODUCT_NAME, methodName, Logging.BEGIN_OF_METHOD,
				JnjRSAProductDataServiceImpl.class);

		if (jnjRsaProductSalesOrgList != null && !jnjRsaProductSalesOrgList.isEmpty())
		{
			final List<JnjProductSalesOrgDTO> jnjProductSalesOrgList = new ArrayList<>();
			mapJnjProductSalesOrgs(jnjRsaProductSalesOrgList, jnjProductSalesOrgList);
			jnjProduct.setJnjProductSalesOrgDto(jnjProductSalesOrgList);
		}

		if (jnjRsaProductDescriptionList != null && !jnjRsaProductDescriptionList.isEmpty())
		{
			final List<JnJProductDescriptionDTO> jnjProductDescriptionList = new ArrayList<>();
			mapJnjProductDescription(jnjRsaProductDescriptionList, jnjProductDescriptionList);
			jnjProduct.setJnJProductDescriptionDTO(jnjProductDescriptionList);
		}

		if (jnjRSAUomConversionList != null && !jnjRSAUomConversionList.isEmpty())
		{
			final List<JnjUomConversionDTO> jnjProductUomConversionList = new ArrayList<>();
			mapJnjProductUomConversion(jnjRSAUomConversionList, jnjProductUomConversionList);
			jnjProduct.setJnjUomConversionDTO(jnjProductUomConversionList);
		}
		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_PRODUCT_NAME, methodName, Logging.END_OF_METHOD,
				JnjRSAProductDataServiceImpl.class);
	}

	private void mapJnjProductUomConversion(final List<JnjRSAProductUnitDTO> jnjRSAUomConversionList,
			final List<JnjUomConversionDTO> jnjProductUomConversionList)
	{
		final String methodName = "mapJnjProductUomConversion";
		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_PRODUCT_NAME, methodName, Logging.BEGIN_OF_METHOD,
				JnjRSAProductDataServiceImpl.class);

		for (final JnjRSAProductUnitDTO rsaProductUnit : jnjRSAUomConversionList)
		{		           
				
			final JnjUomConversionDTO uomConversion = new JnjUomConversionDTO();
			uomConversion.setAlternativeUom(rsaProductUnit.getAltUnitCd());
			uomConversion.setDenominatorUom(rsaProductUnit.getUomConvDenom());
			uomConversion.setHeight(JnjLaCoreUtil.getDoubleValue(rsaProductUnit.getHeight()));
			uomConversion.setLength(JnjLaCoreUtil.getDoubleValue(rsaProductUnit.getDepth()));
			uomConversion.setNumeratorUom(rsaProductUnit.getUomConvNumerator());
			uomConversion.setShippingUnit(rsaProductUnit.getWeightUomCd());
			uomConversion.setShippingWeight(rsaProductUnit.getGrossWeightQty());
			uomConversion.setUnitDimension(rsaProductUnit.getUnitDimension());
			uomConversion.setWidth(JnjLaCoreUtil.getDoubleValue(rsaProductUnit.getWidth()));
			uomConversion.setUnitOfMeasure(rsaProductUnit.getUnitMeasure());
			jnjProductUomConversionList.add(uomConversion);
		}
		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_PRODUCT_NAME, methodName, Logging.END_OF_METHOD,
				JnjRSAProductDataServiceImpl.class);
	}

	private void mapJnjProductDescription(final List<JnjRSAProductDescriptionDTO> jnjRSAProductDescriptionList,
			final List<JnJProductDescriptionDTO> jnjProductDescriptionList)
	{
		final String methodName = "mapJnjProductDescription";
		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_PRODUCT_NAME, methodName, Logging.BEGIN_OF_METHOD,
				JnjRSAProductDataServiceImpl.class);

		for (final JnjRSAProductDescriptionDTO rsaProductDescription : jnjRSAProductDescriptionList)
		{

			final String language = rsaProductDescription.getProdDescLangCd();
			switch (language) {
			case "P":
				jnjProductDescriptionList
						.add(getLanguageIsoCode(rsaProductDescription, Jnjlab2bcoreConstants.LANGUAGE_ISOCODE_PT));
				break;
			case "S":
				jnjProductDescriptionList
						.add(getLanguageIsoCode(rsaProductDescription, Jnjlab2bcoreConstants.LANGUAGE_ISOCODE_ES));
				break;
			case "E":
				jnjProductDescriptionList
						.add(getLanguageIsoCode(rsaProductDescription, Jnjlab2bcoreConstants.LANGUAGE_ISOCODE_EN));
				break;
			default:
				break;
			}

		}
		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_PRODUCT_NAME, methodName, Logging.END_OF_METHOD,
				JnjRSAProductDataServiceImpl.class);
	}

	private JnJProductDescriptionDTO getLanguageIsoCode(final JnjRSAProductDescriptionDTO rsaProductDescription,
			final String languageIsocode)
	{
		final JnJProductDescriptionDTO productDescription = new JnJProductDescriptionDTO();
		productDescription.setDescription(rsaProductDescription.getProdDesc());
		productDescription.setLanguage(languageIsocode);
		productDescription.setName(rsaProductDescription.getProdName());
		return productDescription;
	}

	private void mapJnjProductSalesOrgs(final List<JnjRSAProductSalesOrgDTO> jnjRSAProductSalesOrgList,
			final List<JnjProductSalesOrgDTO> jnjProductSalesOrgList)
	{
		final String methodName = "mapJnjProductSalesOrgs";
		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_PRODUCT_NAME, methodName, Logging.BEGIN_OF_METHOD,
				JnjRSAProductDataServiceImpl.class);

		final List<String> validSalesOrgs = new ArrayList<>();
		for (final JnjRSAProductSalesOrgDTO rsaProductSalesOrg : jnjRSAProductSalesOrgList)
		{
                        
			final String salesOrg = rsaProductSalesOrg.getSalesOrg();
			
			if (!validSalesOrgs.contains(salesOrg))
			{
				validSalesOrgs.add(salesOrg);
				final JnjProductSalesOrgDTO productSalesOrg = new JnjProductSalesOrgDTO();
				productSalesOrg.setColdChainProduct(rsaProductSalesOrg.getColdChainProduct());
				productSalesOrg.setDeliveryUnit(rsaProductSalesOrg.getDelvUnit());
				productSalesOrg.setSalesUnitOfMeasure(rsaProductSalesOrg.getSalesUnit());
				if (rsaProductSalesOrg.getEcommerceFlag() != null
						&& rsaProductSalesOrg.getEcommerceFlag().equals(Jnjb2bCoreConstants.Y_STRING))
				{
					productSalesOrg.setEcommerceFlag(Boolean.TRUE);
				}
				else
				{
					productSalesOrg.setEcommerceFlag(Boolean.FALSE);
				}

				productSalesOrg.setStatus(rsaProductSalesOrg.getStatus());
				productSalesOrg.setUnit(rsaProductSalesOrg.getBaseUnit());
				productSalesOrg.setSalesOrganization(salesOrg);
				jnjProductSalesOrgList.add(productSalesOrg);
			}
		}
		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_PRODUCT_NAME, methodName, Logging.END_OF_METHOD,
				JnjRSAProductDataServiceImpl.class);
	}

	public ConfigurationService getConfigurationService() {
		return configurationService;
	}

	public void setConfigurationService(final ConfigurationService configurationService) {
		this.configurationService = configurationService;
	}

	public JnjRSAProductDao getJnjRSAProductDao() {
		return jnjRSAProductDao;
	}

	public void setJnjRSAProductDao(JnjRSAProductDao jnjRSAProductDao) {
		this.jnjRSAProductDao = jnjRSAProductDao;
	}
}
