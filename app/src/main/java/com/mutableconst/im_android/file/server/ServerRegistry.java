package com.mutableconst.im_android.file.server;

import android.content.Context;

import com.mutableconst.im_android.MainActivity;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ServerRegistry
{
    private static ServerRegistry instance;

    private static Context context;
    private static List<Server> servers;

    public ServerRegistry(MainActivity context)
    {
        this.servers = new ArrayList<Server>();
        this.context = context;
    }

    public void loadServers()
    {
        File serverDir = new File(context.getFilesDir().toString());
        FileFilter fileFilter = new WildcardFileFilter("server_*");
        File[] serverFiles = serverDir.listFiles(fileFilter);
        for (int i = 0; i < serverFiles.length; i++)
        {
            try
            {
                System.out.println(serverFiles[i].getName().toString());
                FileInputStream serverFileHandle = context.openFileInput(serverFiles[i].getName());
                InputStreamReader isr = new InputStreamReader(serverFileHandle);
                String serializedServerEntry = IOUtils.toString(isr);
                System.out.println("Serialized:" + serializedServerEntry);
                Server serverEntry = new Server(serializedServerEntry);
                addServer(serverEntry);
            } catch (java.io.IOException e)
            {
                e.printStackTrace();
            }
        }

    }

    public static void saveServers()
    {
        for (Server entry : servers)
        {
            try
            {
                FileOutputStream serverFileHandle = context.openFileOutput("server_" + entry.getServerUUID(), Context.MODE_PRIVATE);
                serverFileHandle.write(entry.getBytes());
                serverFileHandle.close();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }


    public Server[] getServers()
    {
        return (Server[]) servers.toArray();
    }

    public void clear()
    {
        this.servers = new ArrayList<Server>();
    }

    public void print()
    {
        for (Server entry : servers)
        {
            System.out.println(entry.toString());
        }
    }

    public int serverCount()
    {
        return servers.size();
    }

    public String[] serverUrlList()
    {
        String[] urls = new String[serverCount()];
        for (int i = 0; i < serverCount(); i++)
        {
            urls[i] = servers.get(i).getServerUrl();
        }
        return urls;
    }

    public String[] serverUUIDList()
    {
        String[] uuids = new String[serverCount()];
        for (int i = 0; i < serverCount(); i++)
        {
            uuids[i] = servers.get(i).getServerUUID();
        }
        return uuids;
    }

    public boolean addServer(String uuid, String url, byte[] encryptionKey)
    {
        addServer(new Server(uuid, url, encryptionKey));
        return true;
    }

    public boolean addServer(Server entry)
    {
        servers.add(entry);
        return true;
    }

}
