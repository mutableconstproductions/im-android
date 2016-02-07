package com.mutableconst.im_android.net.dispatch.requests;

import com.mutableconst.im_android.file.server.Server;
import com.mutableconst.im_android.net.HttpClient;
import com.mutableconst.im_android.net.NetworkUnavailableException;

import java.util.Map;

public class IdentityDispatchRequest extends DispatchRequest
{

    public IdentityDispatchRequest(Server server)
    {
        super(server);
    }

    @Override
    public boolean doDispatch(HttpClient client)
    {
        try
        {
            client.getRequest(getRoute(), getParams());
        } catch (NetworkUnavailableException e)
        {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public String getRoute()
    {
        return "identity";
    }

    @Override
    public Map<String, String> getParams()
    {
        return null;
    }
}
