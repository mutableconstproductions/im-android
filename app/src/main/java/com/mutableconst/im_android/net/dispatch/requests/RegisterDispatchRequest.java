package com.mutableconst.im_android.net.dispatch.requests;

import com.mutableconst.im_android.file.server.Server;
import com.mutableconst.im_android.file.uuid.UUIDManager;
import com.mutableconst.im_android.net.HttpClient;
import com.mutableconst.im_android.net.NetworkUnavailableException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterDispatchRequest extends DispatchRequest
{
    private final String appName = "supernova_android";

    public RegisterDispatchRequest(Server server)
    {
        super(server);
    }

    @Override
    public boolean doDispatch(HttpClient client)
    {
        try
        {
            String result = client.postRequest(getRoute(), getParams());
            JSONObject jsonResult = new JSONObject(result);
            String status = jsonResult.getString("status");
            if (status.equals("success"))
            {
                return true;
            }
            return false;
        } catch (NetworkUnavailableException e)
        {
            return false;
        } catch (JSONException e)
        {
            return false;
        }
    }

    @Override
    public String getRoute()
    {
        return "register";
    }

    @Override
    public Map<String, String> getParams()
    {
        Map<String, String> params = new HashMap<String, String>();
        params.put("clientId", UUIDManager.getUUID());
        params.put("appName", appName);
        return params;
    }

}
