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

    private static Map<String, UserInfo> infoByHash;

    // todo allow the user to choose the location of the config file
    private static final String CONFIG_FILE = "avatarConfig.xml";

    private DataStore() {
        infoByHash = new HashMap<String, UserInfo>();
        load();
        for (UserInfo userInfo : infoByHash.values()) {
            System.out.println(userInfo);
        }
    }

    public static DataStore getInstance() {
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
        File configFile = new File(CONFIG_FILE);
        FileOutputStream fos = null;

        LOCK.readLock().lock();
        try {
            fos = new FileOutputStream(configFile);
            String string = new XStream().toXML(infoByHash);
            fos.write(string.getBytes());
            fos.close();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        finally {
            LOCK.readLock().unlock();
            closeQuietly(fos);
        }
    }

    public void load() {
        FileInputStream stream = null;
        LOCK.writeLock().lock();
        try {
            File configFile = new File(CONFIG_FILE);
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
            stream.close();
        }
        catch (IOException ignore) {
        }
    }
}