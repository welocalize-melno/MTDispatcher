package com.globalsight.machineTranslation.mstranslator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import com.globalsight.everest.webapp.pagehandler.administration.tmprofile.TMProfileConstants;
import com.globalsight.util.StringUtil;
import com.microsofttranslator.api.V2.adm.AdmAccessToken;

public class MSMTUtil implements TMProfileConstants
{
    private static final Logger logger = Logger.getLogger(MSMTUtil.class);
    
    public MSMTUtil()
    {
    }
    
    public static String getMsAccessToken(String clientId, String clientSecret,
			String subscriptionKey) {
		String accessToken = null;
		if (StringUtil.isNotEmpty(clientId)
				&& StringUtil.isNotEmpty(clientSecret)) {
			accessToken = getAccessToken(clientId, clientSecret);
		} else if (StringUtil.isNotEmpty(subscriptionKey)) {
			accessToken = getAccessToken(subscriptionKey);
		}

		return accessToken;
	}

	public static String getAccessToken(String clientId, String clientSecret) {
		String accessToken = null;
		int count = 0;
		boolean gotten = false;

		while (!gotten && count < 3) {
			++count;

			try {
				HttpPost e = new HttpPost(
						"https://datamarket.accesscontrol.windows.net/v2/OAuth2-13");
				ArrayList params = new ArrayList();
				params.add(new BasicNameValuePair("grant_type",
						"client_credentials"));
				params.add(new BasicNameValuePair("client_id", clientId));
				params.add(new BasicNameValuePair("client_secret", clientSecret));
				params.add(new BasicNameValuePair("scope",
						"http://api.microsofttranslator.com"));
				e.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
				CloseableHttpResponse httpResponse = HttpClients.custom()
						.build().execute(e);
				if (httpResponse.getStatusLine().getStatusCode() == 200) {
					String strResult = EntityUtils.toString(httpResponse
							.getEntity());
					ObjectMapper mapper = new ObjectMapper();
					AdmAccessToken adm = (AdmAccessToken) mapper.readValue(
							strResult, AdmAccessToken.class);
					accessToken = "Bearer " + adm.getAccess_token();
					gotten = true;
				}
			} catch (Exception arg10) {
				logger.error("Failed to get access token from MS Translator with error: "
						+ arg10.getMessage());
				if (logger.isDebugEnabled()) {
					logger.error(arg10);
				}
			}
		}

		return accessToken;
	}

	public static String getAccessToken(String subscriptionKey) {
		String accessToken = null;
		int count = 0;
		boolean gotten = false;

		while (!gotten && count < 3) {
			++count;
			CloseableHttpClient httpclient = null;

			try {
				HttpPost e = new HttpPost(
						"https://api.cognitive.microsoft.com/sts/v1.0/issueToken");
				e.setHeader("Ocp-Apim-Subscription-Key", subscriptionKey);
				httpclient = HttpClients.custom().build();
				CloseableHttpResponse httpResponse = httpclient.execute(e);
				if (httpResponse.getStatusLine().getStatusCode() == 200) {
					String strResult = EntityUtils.toString(httpResponse
							.getEntity());
					accessToken = "Bearer " + strResult;
					gotten = true;
				}
			} catch (Exception arg10) {
				logger.error("Failed to get access token from MS Translator with error: "
						+ arg10.getMessage());
				if (logger.isDebugEnabled()) {
					logger.error(arg10);
				}
			} finally {
				shutdownHttpClient(httpclient);
			}
		}

		return accessToken;
	}

	private static void shutdownHttpClient(CloseableHttpClient httpclient) {
		if (httpclient != null) {
			try {
				httpclient.close();
				httpclient = null;
			} catch (IOException arg1) {
				logger.warn(arg1);
			}

		}
	}
}
