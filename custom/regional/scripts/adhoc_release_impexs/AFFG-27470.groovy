import de.hybris.platform.servicelayer.search.*;
import com.jnj.la.core.jalo.JnJProductSalesOrg;
import com.jnj.la.core.model.JnJProductSalesOrgModel;
import de.hybris.platform.servicelayer.model.ModelService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

final Logger LOG = Logger.getLogger("Update sales orgs");

flexibleSearchService = spring.getBean("flexibleSearchService");

modelService = spring.getBean("modelService");

query = "select {pk} from {JnJProductSalesOrg} where {creationtime} >= '2023-01-01' and {creationtime}  < '2023-01-01'";

result = flexibleSearchService.search(query);

LOG.info("Result: " + result.count);

if (CollectionUtils.isNotEmpty(result.getResult())) {
int count = 0;
  for (item in result.getResult()) {    
  try {
  if (StringUtils.isNotEmpty(item.getSalesOrg()) && item.getSalesOrg().contains("Master")) {
  item.setActive(false);
  } else {
  item.setActive(true);
  }
  modelService.save(item);  
} 
catch(Exception exe) {
count++;
		
LOG.error("Error processing record = "+item.getPk(), exe);

}

}

LOG.info("No of failed records ="+count);

}

masterQuery = "select {pk} from {JnJProductSalesOrg} where {salesOrg} in ('AR_Master','BR_Master','MX_Master','PE_Master','PR_Master','EC_Master','CL_Master','PA_Master','UY_Master','CO_Master') and  {creationtime} >= '2023-08-01' and {creationtime}  < '2023-10-14'";

result1 = flexibleSearchService.search(masterQuery);

LOG.info("Result: " + result1.count);

if (CollectionUtils.isNotEmpty(result1.getResult())) {

int count1 = 0;
  for (item in result1.getResult()) {    
  try {
  item.setActive(false);
  modelService.save(item);    
} 
catch(Exception exe) {
count1++;
		
LOG.error("Error processing record = "+item.getPk(), exe);

}

}

LOG.info("No of failed records ="+count1);

}

return "Groovy Rocks Active flag updated in JnjProductSalesOrg"