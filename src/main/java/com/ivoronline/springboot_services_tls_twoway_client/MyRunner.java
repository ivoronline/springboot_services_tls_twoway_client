package com.ivoronline.springboot_services_tls_twoway_client;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
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

  @Override
  public void run(String... args) throws Exception {

    //LOAD KEY STORE
    ClassPathResource classPathResource = new ClassPathResource("ClientKeyStore.jks");
    InputStream       inputStream       = classPathResource.getInputStream();
    KeyStore          keyStore          = KeyStore.getInstance("JKS");
                      keyStore.load(inputStream, "mypassword".toCharArray());

    //CREATE SSL CONTEXT
    SSLContext sslContext = new SSLContextBuilder()
      .loadTrustMaterial(null, new TrustSelfSignedStrategy())
      .loadKeyMaterial(keyStore, "mypassword".toCharArray())
      .build();

    //CREATE SOCKET FACTORY
    SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(
      sslContext,
      NoopHostnameVerifier.INSTANCE
    );

    //CREATE HTTP CLIENT
    CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(socketFactory).build();

    //CREATE REQUEST FACTORY
    HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);

    //CREATE REST TEMPLATE
    RestTemplate restTemplate = new RestTemplate();
                 restTemplate.setRequestFactory(requestFactory);

    //CALL SERVER
    String result = restTemplate.getForObject(new URI("https://localhost:8080/Hello"), String.class);

    //DISPLAY RESULT
    System.out.println(result);

  }

}
