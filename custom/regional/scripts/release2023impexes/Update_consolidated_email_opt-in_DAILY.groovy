import de.hybris.platform.servicelayer.search.*; 
import com.jnj.la.core.enums.JnJEmailPeriodicity;
import com.jnj.la.core.jalo.JnJLaUserAccountPreference;
import com.jnj.core.jalo.JnJB2bCustomer;
import com.jnj.la.core.enums.JnJEmailFrequency;
import com.jnj.core.enums.JnjOrderTypesEnum;
import de.hybris.platform.servicelayer.model.ModelService;

flexibleSearchService = spring.getBean("flexibleSearchService");
modelService = spring.getBean("modelService");
List<JnjOrderTypesEnum> list = new ArrayList<JnjOrderTypesEnum>();
list.add(JnjOrderTypesEnum.ZOR);

query = "select {pref.pk} from {JnJLaUserAccountPreference as pref JOIN JnJEmailPeriodicity as period on {period.pk}={pref.periodicity} JOIN JnJB2bCustomer as user on {user.pk} = {pref.user}} where {period.code} in ('CONSOLIDATED') and {pref.consolidatedEmailFrequency} is null and {pref.orderTypes} is null";

result = flexibleSearchService.search(query);

for (item in result.getResult()) {
  if(item.getConsolidatedEmailFrequency() == null){
    item.setConsolidatedEmailFrequency(JnJEmailFrequency.DAILY);
	item.setOrderTypes(list);
	modelService.save(item);
  }
}
return "Groovy Rocks For Consolidated Email!"
