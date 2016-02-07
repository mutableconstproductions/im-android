package com.mutableconst.im_android.net;

import java.util.Map;

public class Util
{
    public static String mapToQueryString(Map<String, String> params)
    {
        boolean initial = true;
        StringBuilder queryBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet())
        {
            if (initial)
            {
                queryBuilder.append(entry.getKey()).append("=").append(entry.getValue());
                initial = false;
            }
            else
            {
                queryBuilder.append("&").append(entry.getKey()).append("=").append(entry.getValue());
            }
        }
        return queryBuilder.toString();
    }
}
