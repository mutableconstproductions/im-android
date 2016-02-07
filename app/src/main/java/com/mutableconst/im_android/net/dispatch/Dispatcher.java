package com.mutableconst.im_android.net.dispatch;

import android.app.Activity;

import com.mutableconst.im_android.net.HttpClient;
import com.mutableconst.im_android.net.dispatch.requests.DispatchRequest;

import java.util.concurrent.ConcurrentLinkedQueue;

public class Dispatcher
{

    private boolean paused;

    private ConcurrentLinkedQueue<DispatchRequest> requestQueue;

    private Activity activity;

    public Dispatcher(Activity activity)
    {
        this.activity = activity;
        this.paused = true;
        this.requestQueue = new ConcurrentLinkedQueue<>();
    }

    public boolean dispatch(DispatchRequest dispatchRequest)
    {
        requestQueue.add(dispatchRequest);
        return true;
    }

    public boolean dispatch(DispatchRequest dispatchRequest, boolean immediately)
    {
        if (immediately)
        {
            return syncDispatchHandler(dispatchRequest);
        }
        else
        {
            return dispatch(dispatchRequest);
        }
    }

    public void start()
    {
        this.paused = false;
        new Thread()
        {
            public void run()
            {
                asyncDispatchHandler();
            }
        }.start();
    }

    public void stop()
    {
        this.paused = true;
    }

    private void asyncDispatchHandler()
    {
        while (!paused)
        {
            if (requestQueue.size() > 0)
            {
                DispatchRequest request = requestQueue.peek();
                if (request != null)
                {
                    boolean dispatchSucc = syncDispatchHandler(request);
                    if (dispatchSucc)
                    {
                        requestQueue.remove();
                    }
                }
            }
            try
            {
                Thread.sleep(5000);
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }

    private boolean syncDispatchHandler(DispatchRequest request)
    {
        HttpClient client = new HttpClient(request.getTargetServers()[0].getServerUrl());
        return request.doDispatch(client);
    }

}
