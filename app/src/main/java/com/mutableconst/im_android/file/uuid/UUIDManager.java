package com.mutableconst.im_android.file.uuid;

import android.content.Context;

import com.mutableconst.im_android.MainActivity;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.UUID;

public class UUIDManager
{
    private static UUIDManager instance;
    private static String cachedUUID;
    private static Context context;

    private UUIDManager(MainActivity context)
    {
        this.context = context;
    }

    public static UUIDManager getInstance()
    {
        if (instance == null)
        {
            //throw new UninitializedUUIDManagerException("Please call init on UUIDManager before using it.");
        }
        return instance;
    }

    public static synchronized void init(MainActivity context)
    {
        if (instance == null)
        {
            instance = new UUIDManager(context);
        }
    }

    public void initializeUUID()
    {
        if (getUUID() == null)
        {
            try
            {
                String uuid = UUID.randomUUID().toString();
                FileOutputStream uuidFileHandle = context.openFileOutput("uuid", Context.MODE_PRIVATE);
                uuidFileHandle.write(uuid.getBytes());
                uuidFileHandle.close();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public static String getUUID()
    {
        if (cachedUUID != null)
        {
            return cachedUUID;
        }
        else
        {
            try
            {
                FileInputStream uuidFileHandle = context.openFileInput("uuid");
                InputStreamReader isr = new InputStreamReader(uuidFileHandle);
                BufferedReader bufferedReader = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null)
                {
                    sb.append(line);
                }
                cachedUUID = sb.toString().trim();
                return cachedUUID;
            } catch (IOException e)
            {
                return null;
            }
        }
    }

    class UninitializedUUIDManagerException extends IllegalStateException
    {
        /* Thrown if UUID Manager wasn't properly initialized before use */
        public UninitializedUUIDManagerException(String s)
        {
            super(s);
        }

    }
}
