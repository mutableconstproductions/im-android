package com.mutableconst.im_android.net.dispatch.requests;

import com.mutableconst.im_android.file.server.Server;
import com.mutableconst.im_android.net.HttpClient;

import java.util.Map;

public abstract class DispatchRequest
{
    private Server[] targetServers;

    public DispatchRequest(Server targetServer)
    {
        this(new Server[]{targetServer});
    }

    public DispatchRequest(Server[] targetServers)
    {
        this.setTargetServers(targetServers);
    }

    //public abstract boolean isSimpleRequest();

    public abstract boolean doDispatch(HttpClient client);

    public abstract String getRoute();

    public abstract Map<String, String> getParams();

    public Server[] getTargetServers()
    {
        return targetServers;
    }

    public void setTargetServers(Server[] targetServers)
    {
        this.targetServers = targetServers;
    }
}
