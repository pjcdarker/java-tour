package com.pjcdarker.util.mail.bean;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * @author pjcdarker
 * @created 10/21/2017.
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