package com.mutableconst.im_android.net;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class HttpClient
{
    private Activity activity;
    private String serverUrl;

    private static final String HTTP_ENCODING = "UTF-8";

    public enum HttpMethod
    {
        GET, POST, PUT, DELETE
    }

    public enum HttpResultType
    {
        JSON, TEXT
    }

    public HttpClient(String serverUrl, Activity activity)
    {
        this.serverUrl = serverUrl;
        this.activity = activity;
    }

    public HttpClient(String serverUrl)
    {
        this(serverUrl, null);
    }

    private class HttpRequest extends AsyncTask<String, Void, String>
    {

        private HttpMethod method;
        private String requestUrl;
        private Map<String, String> params;

        private String result;

        public HttpRequest(HttpMethod method, String requestUrl, Map<String, String> params)
        {
            this.method = method;
            this.requestUrl = requestUrl;
            this.params = params;
        }

        @Override
        protected String doInBackground(String... strings)
        {
            return doRequest();
        }

        protected void onPostExecute(String result)
        {
            this.result = result;
        }

        private String doRequest()
        {
            InputStream resultStream = null;
            try
            {
                if (params != null && params.size() > 0 && method == HttpMethod.GET)
                {
                    String queryString = Util.mapToQueryString(params);
                    requestUrl = requestUrl + "?" + queryString;
                }

                URL url;
                if (requestUrl == null)
                {
                    url = new URL(serverUrl);
                }
                else
                {
                    url = new URL(serverUrl + "/" + requestUrl);
                }
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(5000 /* milliseconds */);

                conn.setRequestMethod(method.name());

                conn.setDoInput(true);
                if (method != HttpMethod.GET)
                {
                    conn.setRequestProperty("Content-Type", "application/json");
                    /* Add paramaters to the body of a POST PUT or DELETE request */
                    if (params != null && params.size() > 0)
                    {
                        JSONObject json = new JSONObject(params);
                        String jsonString = json.toString();
                        System.out.println("Sending: " + jsonString);
                        byte[] postDataBytes = jsonString.getBytes(HTTP_ENCODING);
                        conn.getOutputStream().write(postDataBytes);
                    }

                }

                // Starts the query
                conn.connect();

                int response = conn.getResponseCode();

                resultStream = conn.getInputStream();

                String result = IOUtils.toString(resultStream, HTTP_ENCODING);

                System.out.println(result);

                return result;
            } catch (Exception e)
            {
                e.printStackTrace();
            } finally
            {
                if (resultStream != null)
                {
                    try
                    {
                        resultStream.close();
                    } catch (IOException e)
                    {
                        System.err.println("Ooops");
                    }
                }
            }
            return "Error";
        }

    }

    public String getRequest(String route, Map<String, String> params) throws NetworkUnavailableException
    {
        return httpRequest(HttpMethod.GET, route, params);
    }

    public String postRequest(String route, Map<String, String> params) throws NetworkUnavailableException
    {
        return httpRequest(HttpMethod.POST, route, params);
    }

    public String putRequest(String route, Map<String, String> params) throws NetworkUnavailableException
    {
        return httpRequest(HttpMethod.PUT, route, params);
    }

    public String deleteRequest(String route, Map<String, String> params) throws NetworkUnavailableException
    {
        return httpRequest(HttpMethod.DELETE, route, params);
    }

    private String httpRequest(HttpMethod method, String route, Map<String, String> params) throws NetworkUnavailableException
    {
        String result = null;
        boolean networkAvailable = true;
        /* If you wish to attempt a HTTP request without validating the network is available first, then the activity isn't needed */
        if (this.activity != null)
        {
            ConnectivityManager connMgr = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            networkAvailable = networkInfo != null && networkInfo.isConnected();
        }
        if (networkAvailable)
        {
            HttpRequest asyncRequest = new HttpRequest(method, route, params);
            asyncRequest.execute("");
            try
            {
                result = asyncRequest.get();
            } catch (ExecutionException | InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            throw new NetworkUnavailableException();

        }
        return result;
    }

}
