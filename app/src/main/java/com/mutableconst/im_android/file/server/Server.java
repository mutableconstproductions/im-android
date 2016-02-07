package com.mutableconst.im_android.file.server;

public class Server
{
    private String serverUrl;
    private String serverUUID;
    private byte[] serverEncryptionKey;

    private boolean registeredWithClient;

    private static final String SERVER_UUID_KEY_TAG = "serverUUID";
    private static final String SERVER_URL_KEY_TAG = "serverUrl";
    private static final String SERVER_ENCRYPTION_KEY_TAG = "serverEncryptionKey";

    public Server(String serverUrl, String serverUUID, byte[] serverEncryptionKey)
    {
        setServerUrl(serverUrl);
        setServerUUID(serverUUID);
        setServerEncryptionKey(serverEncryptionKey);
    }

    public Server(String serializedServer)
    {
        String[] entries = serializedServer.split("\n");
        for (int i = 0; i < entries.length; i++)
        {
            String line = entries[i];
            String[] entry = line.split(":");
            if (entry[0].equals(SERVER_UUID_KEY_TAG))
            {
                setServerUUID(entry[1]);
            }
            else if (entry[0].equals(SERVER_ENCRYPTION_KEY_TAG))
            {
                setServerEncryptionKey(entry[1].getBytes());
            }
            else if (entry[0].equals(SERVER_URL_KEY_TAG))
            {
                setServerUrl(entry[1]);
            }
        }
    }

    public String getServerUrl()
    {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl)
    {
        this.serverUrl = serverUrl;
    }

    public String getServerUUID()
    {
        return serverUUID;
    }

    public void setServerUUID(String serverUUID)
    {
        this.serverUUID = serverUUID;
    }

    public byte[] getServerEncryptionKey()
    {
        return serverEncryptionKey;
    }

    public void setServerEncryptionKey(byte[] serverEncryptionKey)
    {
        this.serverEncryptionKey = serverEncryptionKey;
    }

    public byte[] getBytes()
    {
        StringBuilder builder = new StringBuilder();
        builder.append(SERVER_UUID_KEY_TAG).append(":").append(getServerUUID()).append("\n");
        builder.append(SERVER_URL_KEY_TAG).append(":").append(getServerUrl()).append("\n");
        builder.append(SERVER_ENCRYPTION_KEY_TAG).append(":").append(getServerEncryptionKey());
        return builder.toString().getBytes();
    }

    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("URL:").append(getServerUrl()).append("\n");
        builder.append("Key:").append(new String(getServerEncryptionKey())).append("\n");
        builder.append("UUID:").append(getServerUUID()).append("\n");
        return builder.toString();
    }

}
