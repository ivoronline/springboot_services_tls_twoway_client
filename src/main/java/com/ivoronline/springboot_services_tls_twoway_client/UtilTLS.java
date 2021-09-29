package com.ivoronline.springboot_services_tls_twoway_client;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import javax.net.ssl.SSLContext;
import java.io.InputStream;
import java.security.KeyStore;

public class UtilTLS {

  //CLIENT TRUST STORE
  static String clientTrustStoreName     = "ClientTrustStore.jks";
  static String clientTrustStoreType     = "JKS";
  static String clientTrustStorePassword = "mypassword";

  //CLIENT KEY STORE (For Two-Way TLS)
  static String clientKeyStoreName       = "ClientKeyStore.jks";
  static String clientKeyStoreType       = "JKS";
  static String clientKeyStorePassword   = "mypassword";

  //=======================================================================================
  // GET REQUEST FACTORY
  //=======================================================================================
  public static HttpComponentsClientHttpRequestFactory getRequestFactoryForTwoWayTLS() throws Exception {

    //LOAD TRUST STORE
    ClassPathResource classPathResource = new ClassPathResource(clientTrustStoreName);
    InputStream       inputStream       = classPathResource.getInputStream();
    KeyStore          trustStore        = KeyStore.getInstance(clientTrustStoreType);
                      trustStore.load(inputStream, clientTrustStorePassword.toCharArray());

    //LOAD KEY STORE (For Two-Way TLS)
    ClassPathResource keyStoreResource      = new ClassPathResource(clientKeyStoreName);
    InputStream       keyStoreInputStream   = keyStoreResource.getInputStream();
    KeyStore          keyStore              = KeyStore.getInstance(clientKeyStoreType);
                      keyStore.load(keyStoreInputStream, clientKeyStorePassword.toCharArray());

    //CONFIGURE REQUEST FACTORY
    SSLContext sslContext = new SSLContextBuilder()
      .loadTrustMaterial(trustStore, null)
      .loadKeyMaterial  (keyStore, clientKeyStorePassword.toCharArray()) //For Two-Way TLS
      .build();

    SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(
      sslContext,
      NoopHostnameVerifier.INSTANCE
    );

    CloseableHttpClient httpClient= HttpClients
      .custom()
      .setSSLSocketFactory(socketFactory)
      .build();

    HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);

    //RETURN REQUEST FACTORY
    return requestFactory;

  }

}
