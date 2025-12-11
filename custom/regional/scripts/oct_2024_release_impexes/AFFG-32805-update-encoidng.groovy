import de.hybris.platform.servicelayer.search.*;
import com.jnj.core.jalo.JnJB2bCustomer;
import de.hybris.platform.servicelayer.model.ModelService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

final Logger LOG = Logger.getLogger("Update customer password encoding");


flexibleSearchService = spring.getBean("flexibleSearchService");

modelService = spring.getBean("modelService");

query = "select {pk} from {JnJB2bCustomer} where {ssoLogin} = 1";

result = flexibleSearchService.search(query);

LOG.info("Result: " + result.count);

if (CollectionUtils.isNotEmpty(result.getResult())) {

int count = 0;
  for (item in result.getResult()) {    
  try { 
  item.setPasswordEncoding("bcrypt");
  modelService.save(item);  
  
} 
catch(Exception exe) {
count++;
		
LOG.error("Error processing record = "+item.getUid(), exe);

}

}

LOG.info("No of failed records ="+count);

}

return "Groovy Rocks for Update password encoding"