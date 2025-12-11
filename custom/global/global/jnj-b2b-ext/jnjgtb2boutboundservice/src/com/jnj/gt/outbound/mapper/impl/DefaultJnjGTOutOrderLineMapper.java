package com.jnj.gt.outbound.mapper.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.gt.outbound.mapper.JnjGTOutOrderLineMapper;
import com.jnj.itsusmpl00082.sg910_btb_in0498_salesorder_global_source_v1_webservice.salesorderws.OutOrderLines3;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.util.Config;

/**
 * @author nsinha7
 *
 */
public class DefaultJnjGTOutOrderLineMapper implements JnjGTOutOrderLineMapper {
	protected static final Logger LOGGER = Logger.getLogger(DefaultJnjGTOutOrderLineMapper.class);
	@Override
	public List<OutOrderLines3> createValidationOutOrderList(CartModel cartModel) {
		
		String incProdId = Config.getParameter(Jnjb2bCoreConstants.BonusItem.INC_MAT_ID);
		String exProdId = Config.getParameter(Jnjb2bCoreConstants.BonusItem.EXC_MAT_ID	);
		int minQuantity = Integer.valueOf(Config.getParameter(Jnjb2bCoreConstants.BonusItem.MIN_QLFY_QTY));
		double bonusItemQty = Double.valueOf(Config.getParameter(Jnjb2bCoreConstants.BonusItem.BONUS_ITEM_QTY));
		
		double dropShipFee = 10.0;
		double totalFreightFees = 10.0;
		double minimumOrderFee = 10.0;		
		
		List<OutOrderLines3> outOrderList = new ArrayList<OutOrderLines3>();		
		for(AbstractOrderEntryModel abstOrderEntModel : cartModel.getEntries())
		{
			Integer lineNumber = 0;
			OutOrderLines3 outOrderLine = new OutOrderLines3();
			
			//Changes for Bonus Item starts
			OutOrderLines3 incBonusLine = null;
			OutOrderLines3 exBonusLine = null;
			Double netValue = 0.0;
			
			outOrderLine.setMaterialNumber(abstOrderEntModel.getProduct().getCode());
			outOrderLine.setMaterialEntered(abstOrderEntModel.getProduct().getCode());
			outOrderLine.setItemCategory(Jnjb2bCoreConstants.BonusItem.CHRGD_ITEM_CATEGORY);

			if(outOrderLine.getMaterialEntered().equalsIgnoreCase(incProdId)){
				
				long quantity  = (abstOrderEntModel.getQuantity());
				int matQuantity = (int)quantity;
				int multiplier = (matQuantity) / minQuantity;
//				double multiplier = (abstOrderEntModel.getQuantity()) / minQuantity;
				if(multiplier>=1){
					incBonusLine = createBonusLine(abstOrderEntModel,multiplier);
					double chrgdQty = abstOrderEntModel.getQuantity()- (multiplier*bonusItemQty);
					outOrderLine.setMaterialQuantity(Double.toString(chrgdQty).replace(".0", ""));
				}
				else{
					outOrderLine.setMaterialQuantity(abstOrderEntModel.getQuantity().toString());
				}
				
			}
			else if(outOrderLine.getMaterialEntered().equalsIgnoreCase(exProdId)){
				long quantity  = (abstOrderEntModel.getQuantity());
				int matQuantity = (int)quantity;
				int multiplier = (matQuantity) / minQuantity;
//				double multiplier = (abstOrderEntModel.getQuantity()) / minQuantity;
				if(multiplier>=1){
					exBonusLine = createBonusLine(abstOrderEntModel,multiplier);
					outOrderLine.setMaterialQuantity(abstOrderEntModel.getQuantity().toString());
				}
				else{
					outOrderLine.setMaterialQuantity(abstOrderEntModel.getQuantity().toString());
				}
				
			}
			else{
				outOrderLine.setMaterialQuantity(abstOrderEntModel.getQuantity().toString());
			}
			
			if(outOrderLine.getMaterialQuantity()!=null && abstOrderEntModel.getBasePrice()!=null){
				netValue= Double.parseDouble(outOrderLine.getMaterialQuantity())*abstOrderEntModel.getBasePrice();
				LOGGER.info("netValue = outOrderLine.getMaterialQuantity() * abstOrderEntModel.getBasePrice() : "+netValue);
			}
			outOrderLine.setNetValue(netValue.toString());
			//Changes for Bonus Item ends
			
			outOrderLine.setDropshipFee(Double.valueOf(dropShipFee).toString());
			outOrderLine.setFreightFees(Double.valueOf(totalFreightFees).toString());
			outOrderLine.setMinimumOrderFee(Double.valueOf(minimumOrderFee).toString());
			outOrderLine.setTaxes(Double.valueOf(minimumOrderFee).toString());			
			outOrderLine.setLineNumber((lineNumber++).toString());
			outOrderList.add(outOrderLine);
			
			//Add Bonus item line to the list.
			if(incBonusLine!=null){
				outOrderList.add(incBonusLine);
			}
			if(exBonusLine!=null){
				outOrderList.add(exBonusLine);
			}		
			

		}
		
		return outOrderList;
	}
	
	/**
	 * This method is used to create Out order line for Bonus Item
	 * @param abstOrderEntModel
	 * @param multiplier
	 * @return
	 */
	private OutOrderLines3 createBonusLine(AbstractOrderEntryModel abstOrderEntModel, double multiplier) {	
		
		double bonusItemQty = Double.valueOf(Config.getParameter(Jnjb2bCoreConstants.BonusItem.BONUS_ITEM_QTY));
		OutOrderLines3 outOrderLine = new OutOrderLines3();
		outOrderLine.setMaterialEntered(abstOrderEntModel.getProduct().getCode());		
		outOrderLine.setMaterialQuantity(Double.toString(multiplier*bonusItemQty));
		outOrderLine.setItemCategory(Jnjb2bCoreConstants.BonusItem.FREE_ITEM_CATEGORY);		
		return outOrderLine;
		
	}

	@Override
	public List<OutOrderLines3> createConfirmationOutOrderList(OrderModel orderModel) {
		
		String incProdId = Config.getParameter(Jnjb2bCoreConstants.BonusItem.INC_MAT_ID);
		String exProdId = Config.getParameter(Jnjb2bCoreConstants.BonusItem.EXC_MAT_ID	);
		int minQuantity = Integer.valueOf(Config.getParameter(Jnjb2bCoreConstants.BonusItem.MIN_QLFY_QTY));
		double bonusItemQty = Double.valueOf(Config.getParameter(Jnjb2bCoreConstants.BonusItem.BONUS_ITEM_QTY));
		
		double totalDiscounts = 10.0;
		double dropShipFee = 10.0;
		double totalFreightFees = 10.0;
		double minimumOrderFee = 10.0;
		double taxes = 10.0;
		
		
		List<OutOrderLines3> outOrderLineList = new ArrayList<OutOrderLines3>();
		Integer count = 0;
		for(AbstractOrderEntryModel orderEntry : orderModel.getEntries()){
			Integer lineNumber = count++;
			OutOrderLines3 outOrderLine = new OutOrderLines3();
			
			//Changes for Bonus Item starts
			OutOrderLines3 incBonusLine = null;
			OutOrderLines3 exBonusLine = null;
			Double netValue = 0.0;

			outOrderLine.setMaterialNumber(orderEntry.getProduct().getCode());
			outOrderLine.setMaterialEntered(orderEntry.getProduct().getCode());
			outOrderLine.setItemCategory(Jnjb2bCoreConstants.BonusItem.CHRGD_ITEM_CATEGORY);

			if(outOrderLine.getMaterialEntered().equalsIgnoreCase(incProdId)){
				long quantity  = (orderEntry.getQuantity());
				int matQuantity = (int)quantity;
				int multiplier = (matQuantity) / minQuantity;
//				double multiplier = (orderEntry.getQuantity()) / minQuantity;
				if(multiplier>=1){
					incBonusLine = createBonusLine(orderEntry,multiplier);
					double chrgdQty = orderEntry.getQuantity()- (multiplier*bonusItemQty);
					outOrderLine.setMaterialQuantity(Double.toString(chrgdQty).replace(".0", ""));
				}
				else{
					outOrderLine.setMaterialQuantity(orderEntry.getQuantity().toString());
				}
				
			}
			else if(outOrderLine.getMaterialEntered().equalsIgnoreCase(exProdId)){
				long quantity  = (orderEntry.getQuantity());
				int matQuantity = (int)quantity;
				int multiplier = (matQuantity) / minQuantity;
//				double multiplier = (orderEntry.getQuantity()) / minQuantity;
				if(multiplier>=1){
					exBonusLine = createBonusLine(orderEntry,multiplier);
					outOrderLine.setMaterialQuantity(orderEntry.getQuantity().toString());
				}
				else{
					outOrderLine.setMaterialQuantity(orderEntry.getQuantity().toString());
				}
				
			}
			else{
				outOrderLine.setMaterialQuantity(orderEntry.getQuantity().toString());

			}
			
			if(outOrderLine.getMaterialQuantity()!=null && orderEntry.getBasePrice()!=null){
				netValue= Double.parseDouble(outOrderLine.getMaterialQuantity())*orderEntry.getBasePrice();
			}
			outOrderLine.setNetValue(netValue.toString());
			//Changes for Bonus Item ends.

			outOrderLine.setLineNumber(lineNumber.toString());
			outOrderLine.setSalesUOM(orderEntry.getSalesUOM().toString());
			outOrderLine.setDiscounts(Double.valueOf(totalDiscounts).toString());
			outOrderLine.setDropshipFee(Double.valueOf(dropShipFee).toString());
			outOrderLine.setMinimumOrderFee(Double.valueOf(minimumOrderFee).toString());
			outOrderLine.setFreightFees(Double.valueOf(totalFreightFees).toString());
			outOrderLine.setTaxes(Double.valueOf(taxes).toString());
			
			outOrderLineList.add(outOrderLine);
			//Add Bonus item line to the list.
			if(incBonusLine!=null){
				outOrderLineList.add(incBonusLine);
			}
			if(exBonusLine!=null){
				outOrderLineList.add(exBonusLine);
			}
			
		}
	
		return outOrderLineList;
	}

}
