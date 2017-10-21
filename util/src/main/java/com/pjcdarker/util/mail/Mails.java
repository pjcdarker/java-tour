package com.pjcdarker.util.mail;

import com.pjcdarker.util.mail.bean.MailBean;

import java.util.concurrent.CompletableFuture;

/**
 * @author pjc
 * @create 2016-10-10
 */
public class Mails {

    private Mails() {

    }

    public static Mails getInstance() {
        return new Mails();
    }

    public void send(MailBean mailBean) {
        CompletableFuture.runAsync(() -> {
            try {
                new MailSender().sendMail(mailBean);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void read() {
        try {
            new MailReader().read();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
