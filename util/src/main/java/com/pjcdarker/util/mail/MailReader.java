package com.pjcdarker.util.mail;

import com.pjcdarker.util.mail.bean.MailSession;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author pjc
 * @created 2016-10-09
 */
public class MailReader {

    private final MailSession mailSession;
    private final Properties properties;

    MailReader() {
        mailSession = MailSession.getInstance();
        properties = mailSession.getProps();
    }

    void read() {
        try {
            Store store = mailSession.getSession().getStore(properties.getProperty("mail.store.protocol"));
            store.connect(properties.getProperty("mail.pop3.host", properties.getProperty("mail.username")),
                    properties.getProperty("mail.password"));

            Folder folder = store.getFolder("INBOX");
            folder.open(Folder.READ_WRITE);
            Message messages[] = folder.getMessages();
            int index = 0;
            for (int i = messages.length - 1; i >= 0; i--) {
                Message message = messages[i];
                Map dataStore = new HashMap();
                Address[] addresses = message.getFrom();
                InternetAddress address = (InternetAddress) addresses[0];
                String from = address.getAddress();
                String subject = message.getSubject();
                dataStore.put("from", from);
                dataStore.put("subject", subject);

                Object content = message.getContent();
                String comment = "";
                if (content instanceof String) {
                    comment = content.toString();
                } else if (content instanceof Multipart) {
                    Multipart multiPartcnt = (Multipart) content;
                    handleMultipart(multiPartcnt, dataStore);
                    comment = multiPartcnt.getBodyPart(0).getContent().toString();
                } else if (content instanceof Part) {
                    handlePart(message, dataStore);
                    Part part = (Part) content;
                    comment = part.getContent().toString();
                }

                dataStore.put("comment", comment);
                System.out.println("----From-------Subject------comment-------------------");
                System.out.println("From: " + from);
                System.out.println("Subject: " + subject);
                System.out.println("comment: " + comment);
                index++;
                if (index == 2) {
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleMultipart(Multipart multipart, Map dataStore) throws MessagingException, IOException {
        for (int i = 0, n = multipart.getCount(); i < n; i++) {
            handlePart(multipart.getBodyPart(i), dataStore);
        }
    }

    private void handlePart(Part part, Map dataStore) throws MessagingException, IOException {
        String disposition = part.getDisposition();
        if (disposition == null) {
            dataStore.put("comment", part.getContent().toString());
        } else if (disposition.equalsIgnoreCase(Part.ATTACHMENT)) {
            String filename = part.getFileName();
            dataStore.computeIfAbsent(Part.ATTACHMENT, k -> new ArrayList().add(filename));
        } else if (disposition.equalsIgnoreCase(Part.INLINE)) {

        } else {

        }
    }
}
