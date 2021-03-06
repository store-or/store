package com.core.http;

import com.core.job.CurrentThreadJob;
import com.core.job.JobException;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.HttpConnectionParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * Created by laizy on 2017/6/7.
 */
public abstract class HttpJob<T> extends CurrentThreadJob<T> {
    private static final Logger logger = LoggerFactory.getLogger(HttpJob.class);
    protected static HttpClient httpClient;

    static {
        httpClient = createHttpClient();
    }

    private static HttpClient createHttpClient() {
        if (httpClient != null) {
            return httpClient;
        }
        try {
            SSLContext ctx = SSLContext.getInstance("TLS");
            X509TrustManager tm = new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                }
            };
            ctx.init(null, new TrustManager[]{tm}, null);
            SSLSocketFactory ssf = new SSLSocketFactory(ctx, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            // 设置http https支持
            SchemeRegistry schReg = new SchemeRegistry();
            schReg.register(new Scheme("http", 80, PlainSocketFactory
                    .getSocketFactory()));
            schReg.register(new Scheme("https",443, ssf));
            httpClient = new DefaultHttpClient(new PoolingClientConnectionManager(schReg));
        } catch (Exception ex) {
            logger.error("create https client support fail:"+ ex.getMessage(), ex);
        }
        return httpClient;
    }
    public static void setMaxPerRoute(HttpHost httpHost, int count) {
        PoolingClientConnectionManager connectionManager = (PoolingClientConnectionManager) httpClient.getConnectionManager();
        connectionManager.setMaxPerRoute(new HttpRoute(httpHost), count);
    }

    public static void setDefaultMaxPerRoute(int count) {
        PoolingClientConnectionManager connectionManager = (PoolingClientConnectionManager)httpClient.getConnectionManager();
        connectionManager.setDefaultMaxPerRoute(count);
    }

    public static void setMaxTotal(int count) {
        PoolingClientConnectionManager connectionManager = (PoolingClientConnectionManager)httpClient.getConnectionManager();
        connectionManager.setMaxTotal(count);
    }
    public static void setMaxBufferSize(int size) {
        int defaultSize = 8192;
        if(size < defaultSize) {
            HttpConnectionParams.setSocketBufferSize(httpClient.getParams(), defaultSize);
        }else {
            HttpConnectionParams.setSocketBufferSize(httpClient.getParams(), size);
        }
    }
    protected HttpRequestBase httpRequest;
    protected HttpResponse httpResponse;
    protected long startTime = -1;
    protected T result;

    public HttpJob() {
    }

    @Override
    public T getResult() {
        return result;
    }

    protected long getTimeout() {
        return 120 * 1000;
    }

    @Override
    public long startTime() {
        return startTime;
    }

    @Override
    public void run() throws JobException {
        startTime = System.currentTimeMillis();
        try {
            setHttpRequest();
            httpResponse = httpClient.execute(httpRequest);
            handler(httpResponse);
        } catch (Exception e) {
            throw new JobException(e.getMessage(), e);
        } finally {
            if (httpRequest != null) {
                httpRequest.releaseConnection();
            }
        }
    }

    protected abstract void setHttpRequest() throws HttpJobException;

    protected abstract void handler(HttpResponse httpResponse) throws HttpJobException;

    @Override
    public void about(Thread thread) throws JobException {
        httpRequest.abort();
    }

    @Override
    public boolean checkTimeout() {
        return System.currentTimeMillis() > startTime() + getTimeout();
    }


}
