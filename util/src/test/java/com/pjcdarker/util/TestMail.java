package com.pjcdarker.util;

import com.pjcdarker.util.mail.Mails;
import com.pjcdarker.util.mail.bean.MailBean;
import org.junit.Test;

import java.util.Arrays;

/**
 * @author pjc
 * @date 12/2/2016.
 */
public class TestMail {

    @Test
    public void send() {
        MailBean mailBean = new MailBean();
        String fromAddress = "pengjc01@163.com";
        mailBean.setFromAddress(fromAddress);
        mailBean.setToAddresses(Arrays.asList("pjcdarker@163.com"));
        mailBean.setSubject("test");
        mailBean.setMessageContent("测试");
        Mails.getInstance().send(mailBean);
    }

    @Test
    public void read() {
        Mails.getInstance().read();
    }
}
