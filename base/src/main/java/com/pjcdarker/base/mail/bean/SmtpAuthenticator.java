package com.pjcdarker.base.mail.bean;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * @author pjcdarker
 */
public class SmtpAuthenticator extends Authenticator {
    private final String username;
    private final String pw;

    public SmtpAuthenticator(String user, String password) {
        username = user;
        pw = password;
    }

    public PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, pw);
    }
}