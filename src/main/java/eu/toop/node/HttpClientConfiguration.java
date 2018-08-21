package eu.toop.node;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ws.transport.http.HttpComponentsMessageSender.RemoveSoapHeadersInterceptor;

@Configuration
public class HttpClientConfiguration extends ProxiedHttpClientConfiguration {

	@Bean
	public HttpClient wsHttpClient() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
		SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
                .loadTrustMaterial(acceptingTrustStrategy())
                .build();
		SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);
	    CloseableHttpClient httpClient = HttpClients.custom()
	    				.addInterceptorFirst(new RemoveSoapHeadersInterceptor())
	    				.setProxy(proxy())
	                    .setSSLSocketFactory(csf)
	                    .build();
	    return httpClient;
	}
	
}
