package sample;

import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.marklogic.client.DatabaseClient;
import com.marklogic.client.DatabaseClientFactory;
import com.marklogic.client.DatabaseClientFactory.DigestAuthContext;
import com.marklogic.client.DatabaseClientFactory.SecurityContext;

public class Demo {

  public static void main(String[] args) {
    SecurityContext securityContext = new DigestAuthContext("admin", "admin");
    securityContext
        .withSSLContext(SimpleX509TrustManager.newSSLContext(), new SimpleX509TrustManager())
        .withSSLHostnameVerifier(DatabaseClientFactory.SSLHostnameVerifier.ANY);

    DatabaseClient databaseClient = DatabaseClientFactory
        .newClient("localhost", 8011, securityContext);

    System.out.println(databaseClient.checkConnection().isConnected());
  }
}

class SimpleX509TrustManager implements X509TrustManager {

  /**
   * Factory method for creating a simple SSLContext that uses this class as its TrustManager.
   */
  public static SSLContext newSSLContext() {
    return newSSLContext("TLSv1.2");
  }

  public static SSLContext newSSLContext(String protocol) {
    try {
      SSLContext sslContext = SSLContext.getInstance(protocol);
      sslContext.init(null, new TrustManager[]{new SimpleX509TrustManager()}, null);
      return sslContext;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void checkClientTrusted(X509Certificate[] chain, String authType) {
  }

  @Override
  public void checkServerTrusted(X509Certificate[] chain, String authType) {
  }

  @Override
  public X509Certificate[] getAcceptedIssuers() {
    return new X509Certificate[0];
  }

}
