package com.jnj.core.util;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.dto.JnjCanonicalDTO;
import com.jnj.core.model.JnjConfigModel;
import com.jnj.core.services.JnjConfigService;
import de.hybris.platform.europe1.enums.UserPriceGroup;

import de.hybris.platform.core.HybrisEnumValue;
import de.hybris.platform.core.model.user.UserGroupModel;

public class JnjGTCustomerUtil {
	@Autowired
	private JnjConfigService jnjConfigService;
	public static final String CLASS_OF_TRADE_ID = "CoT Group";
	public static final String PRICE_BOOK = "PRICE_BOOK";
	public static final String INDICATOR = "INDICATOR";
	public static final String USER_PRICE_GROUP = "USER_PRICE_GROUP";
	public static final String PIPE_STRING="|";

	public HybrisEnumValue fetchUserPriceGroup(JnjCanonicalDTO jnjCanonicalDTO,LinkedHashMap<String, String> resultSetMap){
		
		 UserPriceGroup userPriceGrp= null;
		final List<JnjConfigModel> jnjConfigModelList = jnjConfigService.getConfigModelsByIdAndKey(CLASS_OF_TRADE_ID,
				resultSetMap.get(USER_PRICE_GROUP));
		if (CollectionUtils.isNotEmpty(jnjConfigModelList))
		{
			final String indicator = jnjConfigModelList.get(0).getValue();
			String userPriceGroup = indicator;
			if (StringUtils.isNotEmpty(resultSetMap.get(PRICE_BOOK)))
			{
				userPriceGroup = indicator.concat(PIPE_STRING).concat(resultSetMap.get(PRICE_BOOK));
			}
			userPriceGrp = UserPriceGroup.valueOf(userPriceGroup);
		
		}
		return userPriceGrp;
	
	}
}
 