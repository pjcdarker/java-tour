package com.pjcdarker.learn.mail;

import com.pjcdarker.learn.mail.bean.MailBean;

import java.util.concurrent.CompletableFuture;

/**
 * @author pjcdarker
 * @created 10/21/2017.
 */
public class MailUtil {

    public static void send(MailBean mailBean) {
        CompletableFuture.runAsync(() -> {
            try {
                MailSender.instance.sendMail(mailBean);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static void read() {
        try {
            MailReader.instance.read();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
