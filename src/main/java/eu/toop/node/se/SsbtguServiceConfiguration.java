package eu.toop.node.se;

import org.apache.http.client.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import eu.toop.node.util.WSClient;
import se.bolagsverket.schema.ssbtgu.v2.grundlaggandeuppgifter.GrundlaggandeUppgifterSvar;

@Configuration
public class SsbtguServiceConfiguration {

	@Value("${ssbtgu.service.to.uri}")
	private String ssbtguServiceToUri;
	@Value("${ssbtgu.service.action.uri}")
	private String ssbtguServiceActionUri;
	@Value("${ssbtgu.service.context.path}")
	private String ssbtguServiceContextPath;
	
	@Autowired
	private HttpClient wsHttpClient;
	
	@Bean
    public Jaxb2Marshaller getActivateMarshaller(){
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath(ssbtguServiceContextPath);
        return marshaller;
    }
    
    @Bean
    public WSClient<GrundlaggandeUppgifterSvar> ssbtguService() throws Exception {
    	SsbtguServiceImpl client = new SsbtguServiceImpl();
    	client.setMarshaller(getActivateMarshaller());
    	client.setUnmarshaller(getActivateMarshaller());
    	client.setActionURI(ssbtguServiceActionUri);
    	client.setToURI(ssbtguServiceToUri);
    	client.setDefaultUri(ssbtguServiceToUri);
    	client.setHttpClient(wsHttpClient);
        return client;
    }
	
}
