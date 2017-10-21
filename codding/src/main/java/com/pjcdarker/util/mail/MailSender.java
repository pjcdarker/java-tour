package com.pjcdarker.util.mail;

import com.pjcdarker.util.mail.bean.MailBean;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author pjcdarker
 * @created 10/21/2017.
 */
public class MailSender {

    public static final MailSender instance = new MailSender();

    private MailSession mailSession;

    private MailSender() {
        mailSession = MailSession.getInstance();
    }

    void sendMail(MailBean mailBean) throws Exception {
        Objects.requireNonNull(mailBean, "mailBean require Not Null");

        InternetAddress from = new InternetAddress(mailBean.getFromAddress());
        MimeMessage message = mailSession.getMessage();
        message.setFrom(from);
        message.setSubject(mailBean.getSubject());

        if (!mailBean.getToAddresses().isEmpty()) {
            List<InternetAddress> toAddresses = new ArrayList<>();
            for (String addresses : mailBean.getToAddresses()) {
                toAddresses.add(new InternetAddress(addresses));
            }
            message.setRecipients(Message.RecipientType.TO, toAddresses.toArray(new InternetAddress[toAddresses.size()]));
        }

        if (!mailBean.getCc().isEmpty()) {
            List<InternetAddress> addresses = new ArrayList<>();
            for (String cc : mailBean.getCc()) {
                addresses.add(new InternetAddress(cc));
            }
            message.setRecipients(Message.RecipientType.CC, addresses.toArray(new InternetAddress[addresses.size()]));
        }
        Transport transport = mailSession.getTransport();
        try {
            String content = mailBean.getMessageContent();
            String[] attachments = mailBean.getAttachments();
            if (attachments != null && attachments.length > 0) {
                Multipart multipart = this.addAttachment(attachments, content);
                message.setContent(multipart);
            } else {
                message.setText(content, "UTF-8");
            }
            transport.send(message);
        } catch (Exception e) {
            throw e;
        } finally {
            transport.close();
        }
    }


    private Multipart addAttachment(String[] attachments, String messageContent) throws MessagingException {
        Multipart multipart = new MimeMultipart();
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(messageContent + "", "text/html;charset=utf-8");
        multipart.addBodyPart(messageBodyPart);
        for (String attachment : attachments) {
            MimeBodyPart attachmentPart = new MimeBodyPart();
            DataSource source = new FileDataSource(attachment);
            attachmentPart.setDataHandler(new DataHandler(source));
            attachmentPart.setFileName(attachment);
            multipart.addBodyPart(attachmentPart);
        }
        return multipart;
    }
}
