package com.unisa.store.tsw_project.model.beans;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AdminBean {
    private String user;
    private String pass;

    public AdminBean(){ }

    public AdminBean(String user, String pass) {
        this.user = user;
        this.setPass(pass);
    }

    public String getUser() {
        return user;
    }

    public String getPass() {
        return pass;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPass(String pass) {
        try {
            MessageDigest digest =
                    MessageDigest.getInstance("SHA-256");
            digest.reset();
            digest.update(pass.getBytes(StandardCharsets.UTF_8));
            this.pass = String.format("%040x", new BigInteger(1, digest.digest()));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
