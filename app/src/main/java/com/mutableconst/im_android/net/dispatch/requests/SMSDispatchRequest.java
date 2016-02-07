package com.mutableconst.im_android.net.dispatch.requests;

import com.mutableconst.im_android.file.server.Server;
import com.mutableconst.im_android.net.HttpClient;

import java.util.Map;

public class SMSDispatchRequest extends DispatchRequest
{

    public SMSDispatchRequest(Server server)
    {
        super(server);
    }

    @Override
    public boolean doDispatch(HttpClient client)
    {

        return false;
    }

    @Override
    public String getRoute()
    {
        return null;
    }

    @Override
    public Map<String, String> getParams()
    {
        return null;
    }
}
