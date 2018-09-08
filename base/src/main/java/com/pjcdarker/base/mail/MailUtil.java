package com.pjcdarker.base.mail;

import com.pjcdarker.base.mail.bean.MailBean;

import java.util.concurrent.CompletableFuture;

/**
 * @author pjcdarker
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
