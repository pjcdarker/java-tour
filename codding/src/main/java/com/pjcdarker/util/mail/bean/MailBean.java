package com.pjcdarker.util.mail.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author pjcdarker
 * @created 10/21/2017.
 */
public class MailBean implements Serializable {

    private String fromAddress;
    private List<String> toAddresses;
    private List<String> cc = new ArrayList<>();
    private String subject;
    private String messageContent;
    private String[] attachments;

    public List<String> getCc() {
        return cc;
    }

    public void setCc(List<String> cc) {
        this.cc = cc;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public List<String> getToAddresses() {
        return toAddresses;
    }

    public void setToAddresses(List toAddresses) {
        this.toAddresses = toAddresses;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String[] getAttachments() {
        return attachments;
    }

    public void setAttachments(String[] attachments) {
        this.attachments = attachments;
    }
}