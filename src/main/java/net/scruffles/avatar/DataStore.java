package net.scruffles.avatar;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DataStore {

    private static final ReadWriteLock LOCK = new ReentrantReadWriteLock();

    private static Map<String, UserInfo> infoByHash = new HashMap<String, UserInfo>();

    public static UserInfo lookupInfo(String hash) {
        LOCK.readLock().lock();
        try {
            return infoByHash.get(hash);
        }
        finally {
            LOCK.readLock().unlock();
        }
    }

    public static void addUserInfo(UserInfo userInfo) {
        LOCK.writeLock().lock();
        try {
            infoByHash.put(userInfo.getHash(), userInfo);
        }
        finally {
            LOCK.writeLock().unlock();
        }
    }

    // todo get this from a persistent store
    static {
        addUserInfo(new UserInfo("bryan@scruffles.net", "bryan.jpg"));
        addUserInfo(new UserInfo("andrew@scruffles.net", "andrew.jpg"));
        addUserInfo(new UserInfo("kimberly@scruffles.net", "kimberly.jpg"));

        for (UserInfo userInfo : infoByHash.values()) {
            System.out.println(userInfo);
        }
    }
}