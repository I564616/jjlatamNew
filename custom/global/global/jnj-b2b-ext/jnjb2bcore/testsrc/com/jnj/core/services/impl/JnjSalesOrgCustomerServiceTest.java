/**
 * 
 */
package com.jnj.core.services.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.model.JnJSalesOrgCustomerModel;
import com.jnj.core.model.JnjSalesOrgAndSplProdMapModel;
import com.jnj.core.util.JnjGetCurrentDefaultB2BUnitUtil;


/**
 * @author sandeep.y.kumar
 * 
 */
@UnitTest
public class JnjSalesOrgCustomerServiceTest
{
	private static final String TEST_SALES_ORG = "BR01";

	private DefaultJnjSalesOrgCustService jnJsalesOrgCustService;
	@Mock
	private ModelService mockModelService;
	@Mock
	private JnJSalesOrgCustomerModel mockJnJSalesOrgCustomerModel;
	@Mock
	private JnjGetCurrentDefaultB2BUnitUtil jnjGetCurrentDefaultB2BUnitUtil;
	@Mock
	private FlexibleSearchService flexibleSearchService;

	private List<JnJSalesOrgCustomerModel> salesOrgList;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		jnJsalesOrgCustService = new DefaultJnjSalesOrgCustService();
		jnJsalesOrgCustService.setModelService(mockModelService);
		jnJsalesOrgCustService.setJnjGetCurrentDefaultB2BUnitUtil(jnjGetCurrentDefaultB2BUnitUtil);
		jnJsalesOrgCustService.setFlexibleSearchService(flexibleSearchService);
		salesOrgList = new ArrayList<JnJSalesOrgCustomerModel>();
	}

	@Test
	public void testSaveItemModel()
	{
		//given
		Mockito.when(mockModelService.create(JnJSalesOrgCustomerModel.class)).thenReturn(mockJnJSalesOrgCustomerModel);
		final JnJB2BUnitModel jnJB2BUnitModel = new JnJB2BUnitModel();
		//mockProduct = Mockito.mock(JnJProductModel.class);
		final JnJSalesOrgCustomerModel jnJSalesOrgCustomerModel = mockModelService.create(JnJSalesOrgCustomerModel.class);

		jnJsalesOrgCustService.setModelService(mockModelService);
		jnJSalesOrgCustomerModel.setCustomerId(jnJB2BUnitModel);
		//given
		doNothing().when(mockModelService).save(mockJnJSalesOrgCustomerModel);
		//when
		final boolean saved = jnJsalesOrgCustService.saveItemModel(jnJSalesOrgCustomerModel);
		//then
		Assert.assertTrue(saved);
	}

	@Test
	public void testGetSalesOrgsForCurrentUserReturnOneObject() throws Exception
	{
		salesOrgList.add(new JnJSalesOrgCustomerModel());
		final JnJB2BUnitModel unit = new JnJB2BUnitModel();
		Mockito.when(jnjGetCurrentDefaultB2BUnitUtil.getDefaultB2BUnit()).thenReturn(unit);
		final JnJSalesOrgCustomerModel jnjSalesOrgCustomerModel = new JnJSalesOrgCustomerModel();
		jnjSalesOrgCustomerModel.setCustomerId(unit);
		Mockito.when(flexibleSearchService.getModelsByExample((JnJSalesOrgCustomerModel) any())).thenReturn(salesOrgList);
		final List<JnJSalesOrgCustomerModel> salesOrgs = jnJsalesOrgCustService.getSalesOrgsForCurrentUser();
		Assert.assertEquals(1, salesOrgs.size());
	}

	@Test
	public void testGetSalesOrgsForCurrentUserReturnEmpty() throws Exception
	{
		final JnJB2BUnitModel unit = new JnJB2BUnitModel();
		Mockito.when(jnjGetCurrentDefaultB2BUnitUtil.getDefaultB2BUnit()).thenReturn(unit);
		final JnJSalesOrgCustomerModel jnjSalesOrgCustomerModel = new JnJSalesOrgCustomerModel();
		jnjSalesOrgCustomerModel.setCustomerId(unit);

		Mockito.when(flexibleSearchService.getModelsByExample((JnJSalesOrgCustomerModel) any())).thenReturn(salesOrgList);
		final List<JnJSalesOrgCustomerModel> salesOrgs = jnJsalesOrgCustService.getSalesOrgsForCurrentUser();
		Assert.assertEquals(0, salesOrgs.size());
	}

	@Test
	public void testGetSectorAndSalesOrgMapping() throws Exception
	{
		final JnJSalesOrgCustomerModel salesOrg = new JnJSalesOrgCustomerModel();
		salesOrg.setSalesOrg(TEST_SALES_ORG);
		salesOrg.setSector("Pharma");
		final JnJSalesOrgCustomerModel salesOrg2 = new JnJSalesOrgCustomerModel();
		salesOrg2.setSalesOrg("BR02");
		salesOrg2.setSector("MDD");
		salesOrgList.add(salesOrg);
		salesOrgList.add(salesOrg2);

		final JnJB2BUnitModel unit = new JnJB2BUnitModel();
		Mockito.when(jnjGetCurrentDefaultB2BUnitUtil.getDefaultB2BUnit()).thenReturn(unit);
		final JnJSalesOrgCustomerModel jnjSalesOrgCustomerModel = new JnJSalesOrgCustomerModel();
		jnjSalesOrgCustomerModel.setCustomerId(unit);
		Mockito.when(flexibleSearchService.getModelsByExample((JnJSalesOrgCustomerModel) any())).thenReturn(salesOrgList);

		final Map<String, String> salesOrgMap = jnJsalesOrgCustService.getSectorAndSalesOrgMapping();
		Assert.assertEquals(2, salesOrgMap.size());
		Assert.assertTrue(salesOrgMap.containsKey("Pharma"));
		Assert.assertTrue(salesOrgMap.containsKey("MDD"));
	}

	@Test
	public void testCheckColdChainStorageHavingColdStorage() throws Exception
	{
		final JnjSalesOrgAndSplProdMapModel salesOrgAndSplProdMap = new JnjSalesOrgAndSplProdMapModel();
		salesOrgAndSplProdMap.setSalesOrg(TEST_SALES_ORG);
		salesOrgAndSplProdMap.setHandleColdChain(Boolean.TRUE);
		Mockito.when(flexibleSearchService.getModelByExample((JnjSalesOrgAndSplProdMapModel) any())).thenReturn(
				salesOrgAndSplProdMap);
		final boolean result = jnJsalesOrgCustService.checkColdChainStorage(TEST_SALES_ORG);
		Assert.assertTrue(result);
	}

	@Test
	public void testCheckColdChainStorageNotHavingColdStorage() throws Exception
	{
		final JnjSalesOrgAndSplProdMapModel salesOrgAndSplProdMap = new JnjSalesOrgAndSplProdMapModel();
		salesOrgAndSplProdMap.setSalesOrg(TEST_SALES_ORG);
		salesOrgAndSplProdMap.setHandleColdChain(Boolean.FALSE);
		Mockito.when(flexibleSearchService.getModelByExample((JnjSalesOrgAndSplProdMapModel) any())).thenReturn(
				salesOrgAndSplProdMap);
		final boolean result = jnJsalesOrgCustService.checkColdChainStorage(TEST_SALES_ORG);
		Assert.assertFalse(result);
	}
}
