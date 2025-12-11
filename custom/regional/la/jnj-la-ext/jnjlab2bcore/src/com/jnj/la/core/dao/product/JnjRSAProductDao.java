package com.jnj.la.core.dao.product;


import java.util.Date;
import java.util.List;

import com.jnj.core.model.JnjIntegrationRSACronJobModel;
import com.jnj.la.core.dto.product.JnjRSAProductDTO;
import com.jnj.la.core.dto.product.JnjRSAProductDescriptionDTO;
import com.jnj.la.core.dto.product.JnjRSAProductSalesOrgDTO;
import com.jnj.la.core.dto.product.JnjRSAProductUnitDTO;


public interface JnjRSAProductDao
{

	List<JnjRSAProductDTO> getProducts(final Date lowerDate, final Date upperDate, final JnjIntegrationRSACronJobModel cronJob, final String sector);

	List<JnjRSAProductDescriptionDTO> getProductDescription(final List<String> materialNum);

	List<JnjRSAProductSalesOrgDTO> getProductSalesOrg(final List<String> materialNum);

	List<JnjRSAProductUnitDTO> getProductUomConversion(final List<String> materialNum);

    List<String> getUniqueDivisionsFromRSA(final Date lowerDate, final Date upperDate, final String sector);

	Date getLastUpdatedDateForLatestRecord();
}
