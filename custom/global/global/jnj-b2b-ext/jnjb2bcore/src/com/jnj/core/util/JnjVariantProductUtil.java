package com.jnj.core.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.dto.JnjCanonicalDTO;
import com.jnj.core.model.JnJProductModel;
import com.jnj.core.model.JnjGTVariantProductModel;
import com.jnj.core.model.JnjIntegrationCronJobModel;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

public class JnjVariantProductUtil {
	@Autowired
	FlexibleSearchService flexibleSearchService;
	
	
	public FlexibleSearchService getFlexibleSearchService() {
		return flexibleSearchService;
	}


	public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService) {
		this.flexibleSearchService = flexibleSearchService;
	}


	private final String VARIANT_QUERY_FOR_MDD = "SELECT {variant:pk} FROM {JnjGTVariantProduct as variant JOIN JnJProduct as product ON {variant:baseProduct}={product:pk} "
			+ "JOIN Unit as uom on {variant:unit}={uom:pk}} where {uom:code}=?unit and {product:code} = ?code and {product:catalogVersion}=?catalogVersion";
	private final String VARIANT_QUERY_FOR_CONSUMER = "SELECT {variant:pk} FROM {JnjGTVariantProduct as variant JOIN JnJProduct as product ON "
			+ "{variant:baseProduct}={product:pk} JOIN Unit as uom on {uom:pk}={variant:unit} } where {uom:code}=?unit and  ({product:code}=?code"
			+ " OR ( {product:materialBaseProduct} is not null  AND { product:materialBaseProduct} in ( {{ select {baseProduct:pk} from {JnJProduct as baseProduct} where {baseProduct:code}= ?code }} ) ) ) and {product:catalogVersion}=?catalogVersion";
	
	
	public JnjGTVariantProductModel fetchVariantForUpdatingPrice(JnjIntegrationCronJobModel cronjobModel,JnjCanonicalDTO jnjCanonicalDTO, Map<String, String> resultSetMap){
		StringBuilder searchQuery = new StringBuilder();
		final Map<String, String> queryParams = new HashMap<>();

		String productVariant=resultSetMap.get(jnjCanonicalDTO.getSourceColumn());
		String unit = productVariant.substring(productVariant.lastIndexOf("|")+1, productVariant.length());
		String code = productVariant.substring(0,productVariant.lastIndexOf("|"));
		
		
		queryParams.put(JnjGTVariantProductModel.UNIT, unit);
		queryParams.put(JnJProductModel.CODE, code);
		queryParams.put(JnJProductModel.CATALOGVERSION, cronjobModel.getCatalogVersion().getPk().getLongValueAsString());

		final FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(searchQuery);

		flexibleSearchQuery.addQueryParameters(queryParams);

		final List<JnjGTVariantProductModel> result = getFlexibleSearchService().<JnjGTVariantProductModel> search(
				flexibleSearchQuery).getResult();

		
		return result.get(0);
		
		
	}

}
