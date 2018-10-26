package com.snm.model;

import com.snm.component.PersistentCookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public abstract class AbstractHttpClient {

    @Autowired
    private PersistentCookieStore cookieStore = null;

    public RequestConfig requestConfig = null;

    public HttpClientContext context = null;

    private CloseableHttpClient httpClient = null;

    public AbstractHttpClient(){
        context = HttpClientContext.create();
        context.setCookieStore(cookieStore);
    }

    /**
     * 返回一个SSL连接的CloseableHttpClient实例
     *
     * @return
     */
    public CloseableHttpClient createSSLClientDefault() {
        if (httpClient == null){
            httpClient = createSSLClient();
        }

        return httpClient;
    }

    /**
     * 返回一个SSL连接的CloseableHttpClient实例
     *
     * @return
     */
    public CloseableHttpClient createSSLClient() {
        try {
            // 配置超时时间（连接服务端超时1秒，请求数据返回超时2秒）
            requestConfig = RequestConfig.custom().setConnectTimeout(120000).setSocketTimeout(60000)
                    .setConnectionRequestTimeout(60000).build();

            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(
                    null, new TrustStrategy() {
                        // 信任所有
                        public boolean isTrusted(X509Certificate[] chain,
                                                 String authType) throws CertificateException {
                            return true;
                        }
                    }).build();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                    sslContext);

            return HttpClients
                    .custom()
                    .setRedirectStrategy(new DefaultRedirectStrategy())
                    .setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy())
                    .setDefaultCookieStore(cookieStore)
                    .setSSLSocketFactory(sslsf)
                    .setDefaultRequestConfig(requestConfig)
                    .setDefaultSocketConfig(
                            SocketConfig.custom().setSoTimeout(5000).build())
                    .build();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        return HttpClients.createDefault();
    }

    public HttpClientContext getContext() {
        return context;
    }

    public void setContext(HttpClientContext context) {
        this.context = context;
    }
}
