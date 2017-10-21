package com.pjcdarker.util.mail.bean;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * @author pjc
 * @create 2016-10-09
 */
public class SMTPAuthenticator extends Authenticator {
    private final String masteruser;
    private final String masterpswd;

    SMTPAuthenticator(String user, String password) {
        masteruser = user;
        masterpswd = password;
    }

    public PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(masteruser, masterpswd);
    }
}
