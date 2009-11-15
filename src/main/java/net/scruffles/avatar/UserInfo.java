package net.scruffles.avatar;

import java.math.BigInteger;
import java.security.MessageDigest;

public class UserInfo {
    private String hash;
    private String emailAddress;
    private String iconLocation;

    public UserInfo(String emailAddress, String iconLocation) {
        this.emailAddress = emailAddress;
        this.iconLocation = iconLocation;
        hash = getMd5Hash(emailAddress.toLowerCase());
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getIconLocation() {
        return iconLocation;
    }

    public void setIconLocation(String iconLocation) {
        this.iconLocation = iconLocation;
    }

    private String getMd5Hash(String s) {
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(s.getBytes(), 0, s.length());
            String md5 = new BigInteger(1, m.digest()).toString(16);
            return md5;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return "UserInfo: " + emailAddress + " - " + hash + " - " + iconLocation;
    }
}
