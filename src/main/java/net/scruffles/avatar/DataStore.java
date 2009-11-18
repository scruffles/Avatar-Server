package net.scruffles.avatar;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.thoughtworks.xstream.XStream;

public class DataStore {

    private final static DataStore instance = new DataStore();

    private final ReadWriteLock LOCK = new ReentrantReadWriteLock();

    private Map<String, UserInfo> infoByHash;

    private static final String CONFIG_FILE = "avatarConfig.xml";
    private boolean isInitialized = false;
    private File configDir;

    public static DataStore getInstance() {
        if (!instance.isInitialized) {
            throw new RuntimeException("must initialize first");
        }
        return instance;
    }

    public UserInfo lookupInfo(String hash) {
        LOCK.readLock().lock();
        try {
            return infoByHash.get(hash);
        }
        finally {
            LOCK.readLock().unlock();
        }
    }

    public void addUserInfo(UserInfo userInfo) {
        LOCK.writeLock().lock();
        try {
            infoByHash.put(userInfo.getHash(), userInfo);
        }
        finally {
            LOCK.writeLock().unlock();
        }
    }

    public void persist() {
        File configFile = new File(configDir, CONFIG_FILE);
        FileOutputStream fos = null;

        LOCK.readLock().lock();
        try {
            fos = new FileOutputStream(configFile);
            String string = new XStream().toXML(infoByHash);
            fos.write(string.getBytes());
            fos.close();
            System.out.println("Saving configuration to " + configFile.getAbsolutePath());
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        finally {
            LOCK.readLock().unlock();
            closeQuietly(fos);
        }
    }

    public void load(File configDir) {
        this.configDir = configDir;
        FileInputStream stream = null;
        LOCK.writeLock().lock();
        try {
            if (infoByHash == null) {
                instance.infoByHash = new HashMap<String, UserInfo>();
            }
            File configFile = new File(configDir, CONFIG_FILE);
            if (!configFile.exists()) {
                System.out.println("File doesn't exist: " + configFile.getAbsolutePath());
                return;
            }
            System.out.println("Reading configuration from " + configFile.getAbsolutePath());
            stream = new FileInputStream(configFile);
            infoByHash.clear();
            infoByHash.putAll((Map<? extends String, ? extends UserInfo>)new XStream().fromXML(stream));
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        finally {
            LOCK.writeLock().unlock();
            closeQuietly(stream);
        }
    }

    private void closeQuietly(Closeable stream) {
        try {
            if (stream != null) {
                stream.close();
            }
        }
        catch (IOException ignore) {
        }
    }

    public static void initialize(File configDir) {
        DataStore instance = DataStore.instance;
        instance.load(configDir);
        for (UserInfo userInfo : instance.infoByHash.values()) {
            System.out.println(userInfo);
        }
        instance.isInitialized = true;
    }
}