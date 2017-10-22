package com.pjcdarker.learn;

import com.pjcdarker.learn.mail.MailUtil;
import com.pjcdarker.learn.mail.bean.MailBean;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

/**
 * @author pjcdarker
 * @created 10/21/2017.
 */
public class MailTest {

    @Test
    public void send() {
        MailBean mailBean = new MailBean();
        String fromAddress = "";
        mailBean.setFromAddress(fromAddress);
        mailBean.setToAddresses(Arrays.asList(""));
        mailBean.setSubject("test");
        mailBean.setMessageContent("测试");
        MailUtil.send(mailBean);
    }

    @Test
    public void read() {
        MailUtil.read();
    }
}
