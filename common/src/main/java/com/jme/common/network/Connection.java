package com.jme.common.network;

import android.text.TextUtils;
import com.jme.common.BuildConfig;
import com.jme.common.app.BaseApplication;
import com.jme.common.ui.config.RxBusConfig;
import com.jme.common.util.SharedPreUtils;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.TlsVersion;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;

/**
 * Created by zhangzhongqiang on 2015/7/29.
 * Refactored by Yanmin on 2016/3/16
 */
public class Connection {

    static Collection<? extends Certificate> dzInoutCert;

    private static ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.COMPATIBLE_TLS)
            .supportsTlsExtensions(true)
            .tlsVersions(TlsVersion.TLS_1_2, TlsVersion.TLS_1_1, TlsVersion.TLS_1_0)
            .cipherSuites(
                    CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                    CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                    CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256,
                    CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA,
                    CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA,
                    CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA,
                    CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA,
                    CipherSuite.TLS_ECDHE_ECDSA_WITH_RC4_128_SHA,
                    CipherSuite.TLS_ECDHE_RSA_WITH_RC4_128_SHA,
                    CipherSuite.TLS_DHE_RSA_WITH_AES_128_CBC_SHA,
                    CipherSuite.TLS_DHE_DSS_WITH_AES_128_CBC_SHA,
                    CipherSuite.TLS_DHE_RSA_WITH_AES_256_CBC_SHA)
            .build();


    public static OkHttpClient getUnsafeOkHttpClient(Interceptor interceptor) {
        OkHttpClient okHttpClient = null;
        try {

            X509TrustManager trustManager;
            SSLSocketFactory sslSocketFactory;
            // Create a trust manager that does not validate certificate chains
            trustManager = trustManagerForCertificates(trustedCertificatesInputStream());
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{trustManager}, null);
            sslSocketFactory = sslContext.getSocketFactory();

            HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();

            if (BuildConfig.COMMON_LOG_DEBUG)
                logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            else
                logInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);

            OkHttpClient.Builder builder = new OkHttpClient.Builder();

            builder.connectTimeout(60, TimeUnit.SECONDS);
            builder.readTimeout(60, TimeUnit.SECONDS);
            builder.writeTimeout(60, TimeUnit.SECONDS);
            builder.sslSocketFactory(sslSocketFactory, trustManager);
            builder.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request();
                    Request.Builder builder1 = request.newBuilder();
                    if (!TextUtils.isEmpty(SharedPreUtils.getString(BaseApplication.getContext(), RxBusConfig.HEADER_LOGIN_TOKEN))) {
                        builder1.addHeader(RxBusConfig.HEADER_LOGIN_TOKEN, SharedPreUtils.getString(BaseApplication.getContext(), RxBusConfig.HEADER_LOGIN_TOKEN));
                    }
                    Response response = chain.proceed(builder1.build());
                    return response;
                }
            });

            builder.interceptors().add(interceptor);
            builder.interceptors().add(logInterceptor);
            builder.retryOnConnectionFailure(true);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            }).build();
            okHttpClient = builder.connectionSpecs(Collections.singletonList(spec)).build();

            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static X509TrustManager trustManagerForCertificates(InputStream in) throws GeneralSecurityException {
        CertificateFactory cerficateFy = CertificateFactory.getInstance("X.509");
        dzInoutCert = cerficateFy.generateCertificates(in);

        // Put the certificates a key store.
        char[] password = "password".toCharArray(); // Any password will work.
        KeyStore keyStore = newEmptyKeyStore(password);
        int index = 0;
        for (Certificate certificate : dzInoutCert) {
            String certificateAlias = Integer.toString(index++);
            keyStore.setCertificateEntry(certificateAlias, certificate);
        }

        // Use it to build an X509 trust manager.
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(
                KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, password);
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
                TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStore);

        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
        if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
            throw new IllegalStateException("Unexpected default trust managers:"
                    + Arrays.toString(trustManagers));
        }
        return (X509TrustManager) trustManagers[0];
    }

    static KeyStore newEmptyKeyStore(char[] password) throws GeneralSecurityException {
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            InputStream in = null; // By convention, 'null' creates an empty key store.
            keyStore.load(in, password);
            return keyStore;
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }


    static InputStream trustedCertificatesInputStream() {
        // PEM files for root certificates of Comodo and Entrust. These two CAs are sufficient to view
        // https://publicobject.com (Comodo) and https://squareup.com (Entrust). But they aren't
        // sufficient to connect to most HTTPS sites including https://godaddy.com and https://visa.com.
        // Typically developers will need to get a PEM file from their organization's TLS administrator.

        final String XGLT = "-----BEGIN CERTIFICATE-----\n" +
                "MIIEDDCCA7GgAwIBAgIQAtazThN5bwvv4tiLTlV+ZjAKBggqhkjOPQQDAjByMQsw\n" +
                "CQYDVQQGEwJDTjElMCMGA1UEChMcVHJ1c3RBc2lhIFRlY2hub2xvZ2llcywgSW5j\n" +
                "LjEdMBsGA1UECxMURG9tYWluIFZhbGlkYXRlZCBTU0wxHTAbBgNVBAMTFFRydXN0\n" +
                "QXNpYSBUTFMgRUNDIENBMB4XDTE4MDYyOTAwMDAwMFoXDTE5MDYyOTEyMDAwMFow\n" +
                "HzEdMBsGA1UEAxMUbS5kcGMueGlhb2dlbGV0dS5jb20wWTATBgcqhkjOPQIBBggq\n" +
                "hkjOPQMBBwNCAAS49xlTQKz2n7Bckt3d9XHbFLrn9Hz2sdr0fMTTULtRRRPlTPlW\n" +
                "S0PsT4V2GSKNmVlef2HKCeD1e/CyYZ0B5Fqso4ICejCCAnYwHwYDVR0jBBgwFoAU\n" +
                "EoZEZiYIVCaPZTeyKU4mIeCTvtswHQYDVR0OBBYEFEK/fivQqbYB6PYlzg3Ustlc\n" +
                "Hfu7MB8GA1UdEQQYMBaCFG0uZHBjLnhpYW9nZWxldHUuY29tMA4GA1UdDwEB/wQE\n" +
                "AwIHgDAdBgNVHSUEFjAUBggrBgEFBQcDAQYIKwYBBQUHAwIwTAYDVR0gBEUwQzA3\n" +
                "BglghkgBhv1sAQIwKjAoBggrBgEFBQcCARYcaHR0cHM6Ly93d3cuZGlnaWNlcnQu\n" +
                "Y29tL0NQUzAIBgZngQwBAgEwgYEGCCsGAQUFBwEBBHUwczAlBggrBgEFBQcwAYYZ\n" +
                "aHR0cDovL29jc3AyLmRpZ2ljZXJ0LmNvbTBKBggrBgEFBQcwAoY+aHR0cDovL2Nh\n" +
                "Y2VydHMuZGlnaXRhbGNlcnR2YWxpZGF0aW9uLmNvbS9UcnVzdEFzaWFUTFNFQ0ND\n" +
                "QS5jcnQwCQYDVR0TBAIwADCCAQUGCisGAQQB1nkCBAIEgfYEgfMA8QB2AKS5CZC0\n" +
                "GFgUh7sTosxncAo8NZgE+RvfuON3zQ7IDdwQAAABZEqtbAQAAAQDAEcwRQIgLWqF\n" +
                "Ls9PpuFs3Dvl46obwCAFn4Hr1DyBueXwaA4QaVwCIQDu0Ouq01wfqyvjiPg8RRCV\n" +
                "IZ6AX/EyvD1M8Gy/lZ3gqwB3AId1v+dZfPiMQ5lfvfNu/1aNR1Y2/0q1YMG06v9e\n" +
                "oIMPAAABZEqtbNoAAAQDAEgwRgIhAPYFeXlAUyt+7o+50grxqKA+b+WGvw+/NSY8\n" +
                "bTl4WNZAAiEAz1D8jsRsXczLJpIpU411g58Xu/VVXS+mPgo+/sCu8KcwCgYIKoZI\n" +
                "zj0EAwIDSQAwRgIhAPN2hrL3JLvM5yRB6adDB58z0V8lQAJxvA5rRBFT7o78AiEA\n" +
                "tCk3QTrke0qQngJCgEheVKmb4zpZhtVz0KWhoRCtb0o=\n" +
                "-----END CERTIFICATE-----\n" +
                "-----BEGIN CERTIFICATE-----\n" +
                "MIID4zCCAsugAwIBAgIQBz/JpHsGAhj24Khq6fw+OzANBgkqhkiG9w0BAQsFADBh\n" +
                "MQswCQYDVQQGEwJVUzEVMBMGA1UEChMMRGlnaUNlcnQgSW5jMRkwFwYDVQQLExB3\n" +
                "d3cuZGlnaWNlcnQuY29tMSAwHgYDVQQDExdEaWdpQ2VydCBHbG9iYWwgUm9vdCBD\n" +
                "QTAeFw0xNzEyMDgxMjI4NTdaFw0yNzEyMDgxMjI4NTdaMHIxCzAJBgNVBAYTAkNO\n" +
                "MSUwIwYDVQQKExxUcnVzdEFzaWEgVGVjaG5vbG9naWVzLCBJbmMuMR0wGwYDVQQL\n" +
                "ExREb21haW4gVmFsaWRhdGVkIFNTTDEdMBsGA1UEAxMUVHJ1c3RBc2lhIFRMUyBF\n" +
                "Q0MgQ0EwWTATBgcqhkjOPQIBBggqhkjOPQMBBwNCAASdQvDzv44jBee0APcvKOWs\n" +
                "zZsRjc4j+L6DLlYOf9tSgvfOJplfMeDNDZzOQEcJbVPD+yekJQUmObCPOrgMhqMI\n" +
                "o4IBTzCCAUswHQYDVR0OBBYEFBKGRGYmCFQmj2U3silOJiHgk77bMB8GA1UdIwQY\n" +
                "MBaAFAPeUDVW0Uy7ZvCj4hsbw5eyPdFVMA4GA1UdDwEB/wQEAwIBhjAdBgNVHSUE\n" +
                "FjAUBggrBgEFBQcDAQYIKwYBBQUHAwIwEgYDVR0TAQH/BAgwBgEB/wIBADA0Bggr\n" +
                "BgEFBQcBAQQoMCYwJAYIKwYBBQUHMAGGGGh0dHA6Ly9vY3NwLmRpZ2ljZXJ0LmNv\n" +
                "bTBCBgNVHR8EOzA5MDegNaAzhjFodHRwOi8vY3JsMy5kaWdpY2VydC5jb20vRGln\n" +
                "aUNlcnRHbG9iYWxSb290Q0EuY3JsMEwGA1UdIARFMEMwNwYJYIZIAYb9bAECMCow\n" +
                "KAYIKwYBBQUHAgEWHGh0dHBzOi8vd3d3LmRpZ2ljZXJ0LmNvbS9DUFMwCAYGZ4EM\n" +
                "AQIBMA0GCSqGSIb3DQEBCwUAA4IBAQBZcGGhLE09CbQD5xP93NAuNC85G1BMa1OG\n" +
                "2Q01TWvvgp7Qt1wNfRLAnhQT5pb7kRs+E7nM4IS894ufmuL452q8gYaq5HmvOmfh\n" +
                "XMmL6K+eICfvyqjb/tSi8iy20ULO/TZhLhPor9tle52Yx811FG4i5vqwPIUEOEJ7\n" +
                "pXe6RPVoBiwi4rbLspQGD/vYqrj9OJV4JctoIhhGq+y/sozU6nBXHfhVSD3x+hkO\n" +
                "Ost6tyRq481IyUWQHcFtwda3gfMnaA3dsag2dtJz33RIJIUfxXmVK7w4YzHOHifn\n" +
                "7TYk8iNrDDLtql6vS8FjiUx3kJnI6zge1C9lUHhZ/aD3RiTJrwWI\n" +
                "-----END CERTIFICATE----- \n" +
                "\n";

        final String XGLE2 = "-----BEGIN CERTIFICATE-----\n" +
                "MIIEDDCCA7GgAwIBAgIQAtazThN5bwvv4tiLTlV+ZjAKBggqhkjOPQQDAjByMQsw\n" +
                "CQYDVQQGEwJDTjElMCMGA1UEChMcVHJ1c3RBc2lhIFRlY2hub2xvZ2llcywgSW5j\n" +
                "LjEdMBsGA1UECxMURG9tYWluIFZhbGlkYXRlZCBTU0wxHTAbBgNVBAMTFFRydXN0\n" +
                "QXNpYSBUTFMgRUNDIENBMB4XDTE4MDYyOTAwMDAwMFoXDTE5MDYyOTEyMDAwMFow\n" +
                "HzEdMBsGA1UEAxMUbS5kcGMueGlhb2dlbGV0dS5jb20wWTATBgcqhkjOPQIBBggq\n" +
                "hkjOPQMBBwNCAAS49xlTQKz2n7Bckt3d9XHbFLrn9Hz2sdr0fMTTULtRRRPlTPlW\n" +
                "S0PsT4V2GSKNmVlef2HKCeD1e/CyYZ0B5Fqso4ICejCCAnYwHwYDVR0jBBgwFoAU\n" +
                "EoZEZiYIVCaPZTeyKU4mIeCTvtswHQYDVR0OBBYEFEK/fivQqbYB6PYlzg3Ustlc\n" +
                "Hfu7MB8GA1UdEQQYMBaCFG0uZHBjLnhpYW9nZWxldHUuY29tMA4GA1UdDwEB/wQE\n" +
                "AwIHgDAdBgNVHSUEFjAUBggrBgEFBQcDAQYIKwYBBQUHAwIwTAYDVR0gBEUwQzA3\n" +
                "BglghkgBhv1sAQIwKjAoBggrBgEFBQcCARYcaHR0cHM6Ly93d3cuZGlnaWNlcnQu\n" +
                "Y29tL0NQUzAIBgZngQwBAgEwgYEGCCsGAQUFBwEBBHUwczAlBggrBgEFBQcwAYYZ\n" +
                "aHR0cDovL29jc3AyLmRpZ2ljZXJ0LmNvbTBKBggrBgEFBQcwAoY+aHR0cDovL2Nh\n" +
                "Y2VydHMuZGlnaXRhbGNlcnR2YWxpZGF0aW9uLmNvbS9UcnVzdEFzaWFUTFNFQ0ND\n" +
                "QS5jcnQwCQYDVR0TBAIwADCCAQUGCisGAQQB1nkCBAIEgfYEgfMA8QB2AKS5CZC0\n" +
                "GFgUh7sTosxncAo8NZgE+RvfuON3zQ7IDdwQAAABZEqtbAQAAAQDAEcwRQIgLWqF\n" +
                "Ls9PpuFs3Dvl46obwCAFn4Hr1DyBueXwaA4QaVwCIQDu0Ouq01wfqyvjiPg8RRCV\n" +
                "IZ6AX/EyvD1M8Gy/lZ3gqwB3AId1v+dZfPiMQ5lfvfNu/1aNR1Y2/0q1YMG06v9e\n" +
                "oIMPAAABZEqtbNoAAAQDAEgwRgIhAPYFeXlAUyt+7o+50grxqKA+b+WGvw+/NSY8\n" +
                "bTl4WNZAAiEAz1D8jsRsXczLJpIpU411g58Xu/VVXS+mPgo+/sCu8KcwCgYIKoZI\n" +
                "zj0EAwIDSQAwRgIhAPN2hrL3JLvM5yRB6adDB58z0V8lQAJxvA5rRBFT7o78AiEA\n" +
                "tCk3QTrke0qQngJCgEheVKmb4zpZhtVz0KWhoRCtb0o=\n" +
                "-----END CERTIFICATE-----\n";

        final String WX_LOGIN = "-----BEGIN CERTIFICATE-----\n" +
                "MIIFCTCCA/GgAwIBAgIQDK/JgIu8mXGqC9aWazNgZTANBgkqhkiG9w0BAQsFADBe\n" +
                "MQswCQYDVQQGEwJVUzEVMBMGA1UEChMMRGlnaUNlcnQgSW5jMRkwFwYDVQQLExB3\n" +
                "d3cuZGlnaWNlcnQuY29tMR0wGwYDVQQDExRHZW9UcnVzdCBSU0EgQ0EgMjAxODAe\n" +
                "Fw0xNzEyMzEwMDAwMDBaFw0xODEyMzExMjAwMDBaMHUxCzAJBgNVBAYTAkNOMRIw\n" +
                "EAYDVQQHDAnljZfkuqzluIIxLTArBgNVBAoMJOaxn+iLj+Wkp+azsOS/oeaBr+aK\n" +
                "gOacr+aciemZkOWFrOWPuDELMAkGA1UECxMCSVQxFjAUBgNVBAMMDSouanNkdHRl\n" +
                "Yy5jb20wggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQCyo7OiBrZKCOct\n" +
                "J4PoEU0MmUhNAqGyZDE7x677CWy/XiPpuZ7+dIkrD9yN4zYqZWMY7l7sDDtpSVdy\n" +
                "kYyDg0eADjbEGJmMrr+yfS5e5CZ0RRlDQF4pLwDUMPmmdderRV6D5S5CIJo/7sIX\n" +
                "4gOIpC/nz0P/MSZDxlmZ6XShqMQ/B1uE0Zb+Kj/SP0udVEKjVBAMBCScW4Inmo2B\n" +
                "/ydavLbWYlbpwhMT9zuYt/1hUKaoweGX5B5utuIraqopm98ws2Jgj5bRCTblTYQ8\n" +
                "nquPisyo1cG0eoog5MEeXNqIKGU3xPX76RrhYSwxgESOlKxTqC3EArI84k61/WCB\n" +
                "lOkZeqmzAgMBAAGjggGqMIIBpjAfBgNVHSMEGDAWgBSQWP+wnHWoUVR3se3yo0MW\n" +
                "OJ5sxTAdBgNVHQ4EFgQU1RfmPyX80OntPbtFYYa6oa0qlUEwJQYDVR0RBB4wHIIN\n" +
                "Ki5qc2R0dGVjLmNvbYILanNkdHRlYy5jb20wDgYDVR0PAQH/BAQDAgWgMB0GA1Ud\n" +
                "JQQWMBQGCCsGAQUFBwMBBggrBgEFBQcDAjA+BgNVHR8ENzA1MDOgMaAvhi1odHRw\n" +
                "Oi8vY2RwLmdlb3RydXN0LmNvbS9HZW9UcnVzdFJTQUNBMjAxOC5jcmwwTAYDVR0g\n" +
                "BEUwQzA3BglghkgBhv1sAQEwKjAoBggrBgEFBQcCARYcaHR0cHM6Ly93d3cuZGln\n" +
                "aWNlcnQuY29tL0NQUzAIBgZngQwBAgIwdQYIKwYBBQUHAQEEaTBnMCYGCCsGAQUF\n" +
                "BzABhhpodHRwOi8vc3RhdHVzLmdlb3RydXN0LmNvbTA9BggrBgEFBQcwAoYxaHR0\n" +
                "cDovL2NhY2VydHMuZ2VvdHJ1c3QuY29tL0dlb1RydXN0UlNBQ0EyMDE4LmNydDAJ\n" +
                "BgNVHRMEAjAAMA0GCSqGSIb3DQEBCwUAA4IBAQBrgQP40AOEe9A8uAZjfhTfvvmd\n" +
                "ff+AtH7j5Elm6B6/iyq0hRWlHSPw88+IT6JKUUIe7rvdF0dtIFAa6RUPbic2J6nb\n" +
                "lvqpjmk/GavEaT9WU5NCumhKyt7qjBq0+y99IHsWkvRtScNZtpTa70n132wasoFp\n" +
                "xVvQAqKlG485sR/LzeM0DKPtI/x2FJNH5+8VPMIEWgQvgwAMuZXxUVmZ/NKi5ioB\n" +
                "fGK//gwHAmkyBJJqaaVZ/mJ7esC0bU7k/Mq4/2QSUzDEEFGGaDzdDLUsg8TFkDPq\n" +
                "gYuRlUcXRfnn8VJaetW8gfel0xkgRXyR7OVIAkk6Nc+w5LWTdv7xZfp2m8SJ\n" +
                "-----END CERTIFICATE-----\n";


        return new Buffer()
//                .writeUtf8(DZ_INOUT_CERT)
//                .writeUtf8(SPOT_REDWINE_CERT)
//                .writeUtf8(PUER_WARNING_APP_LAN_CERT)
//                .writeUtf8(PUER_WARNING_APP_WAN_PRODUCE_CERT)
                .writeUtf8(XGLT)
                .writeUtf8(XGLE2)
                .writeUtf8(WX_LOGIN)
                .inputStream();
    }

}