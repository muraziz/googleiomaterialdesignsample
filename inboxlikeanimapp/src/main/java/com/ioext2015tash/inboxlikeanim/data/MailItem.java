package com.ioext2015tash.inboxlikeanim.data;

/**
 * Created by aziz on 6/4/15.
 */
public class MailItem {
    public int iconResId;
    public String msgTitle;
    public String msgBody;
    public String msgSender;

    MailItem(int iconResId, String msgTitle, String msgBody, String msgSender) {
        this.iconResId = iconResId;
        this.msgTitle = msgTitle;
        this.msgBody = msgBody;
        this.msgSender = msgSender;
    }
}
