package com.manoj.oye;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpVersion;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

public class Registration {
private static String api_key = "0004-cb734182-5466e6dc-7773-a2069e86";
private static String private_key = "0009-cb734182-5466e6dc-7776-32f56dfd";
public static String session_id;



public class MySSLSocketFactory extends SSLSocketFactory {
    SSLContext sslContext = SSLContext.getInstance("TLS");

    public MySSLSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
        super(truststore);

        TrustManager tm = new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };

        sslContext.init(null, new TrustManager[] { tm }, null);
    }

    @Override
    public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
        return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
    }

    @Override
    public Socket createSocket() throws IOException {
        return sslContext.getSocketFactory().createSocket();
    }
}

public HttpClient getNewHttpClient() {
    try {
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        trustStore.load(null, null);

        SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
        sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

        HttpParams params = new BasicHttpParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        registry.register(new Scheme("https", sf, 443));

        ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

        return new DefaultHttpClient(ccm, params);
    } catch (Exception e) {
        return new DefaultHttpClient();
    }
}


public String register_number(String phone_number)
{
try{
	
	       
	String URL = "https://api.motp.in/v1/OTP/"+api_key+"/"+session_id;
	HttpPost get_auth_str =new HttpPost(URL);
	BasicHttpResponse get_auth_str_response; 
	HttpClient client2 = getNewHttpClient();
	String body = "private="+private_key;
	StringEntity entity = new StringEntity(body,"UTF-8");
	get_auth_str.setEntity(entity);
	get_auth_str.setHeader("Content-Type", "application/x-www-form-urlencoded");
	get_auth_str_response = (BasicHttpResponse) client2.execute(get_auth_str);
	String auth_code_resp = EntityUtils.toString(get_auth_str_response.getEntity());
	JSONObject mainObject2 = new JSONObject(auth_code_resp);
	String status2 = mainObject2.getString("Status");
	String auth_code = mainObject2.getString("Result");
	
	System.out.println(auth_code);
	
	if(status2.compareToIgnoreCase("Success")!=0)
		return "getting auth code failed";

	return auth_code;
	
	 }catch(Exception e)
	 {
	 	e.printStackTrace();
	 	return e.getMessage();
	 }
}

public String initiatecall(String globalnumber) {
	try{
	// TODO Auto-generated method stub
	if(globalnumber.contains("+"))
		return "remove '+' from the mobile number";
	
	if(globalnumber.contains(" "))
		return "remove spaces from the mobile number";
	
	String URL = "https://api.mOTP.in/v1/"+api_key+"/"+globalnumber;
	HttpGet send_call =new HttpGet(URL);
	BasicHttpResponse send_call_response; 
	HttpClient client = getNewHttpClient();
    send_call_response = (BasicHttpResponse) client.execute(send_call);
	String response = EntityUtils.toString(send_call_response.getEntity());
	JSONObject mainObject = new JSONObject(response);
	String status = mainObject.getString("Status");
	session_id = mainObject.getString("Result");
	System.out.println(session_id);
	}
	catch(Exception e)
	{
		e.printStackTrace();
		System.out.println("hellllllllllllllllllllllllllooooooooooooooooooooooooooooo");
		System.out.println(e.getMessage());
	}
	return session_id;
	
}

}
