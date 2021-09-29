package com.ivoronline.springboot_services_tls_twoway_client;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.io.InputStream;
import java.net.URI;
import java.security.KeyStore;

@Component
public class MyRunner implements CommandLineRunner {

  //PROPERTIES
  String serverURL = "https://localhost:8085/Hello";

  //===============================================================================
  // RUN
  //===============================================================================
  @Override
  public void run(String... args) throws Exception {

    //GET REQUEST FACTORY (for One-Way TLS)
    HttpComponentsClientHttpRequestFactory requestFactory = UtilTLS.getRequestFactoryForTwoWayTLS();

    //SEND REQUEST
    RestTemplate    restTemplate = new RestTemplate();
                    restTemplate.setRequestFactory(requestFactory);
    String result = restTemplate.getForObject(new URI(serverURL), String.class);

    //DISPLAY RESULT
    System.out.println(result);

  }

}
