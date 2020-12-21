package cn.xinbee.mdrs.service;

import java.io.UnsupportedEncodingException;

public interface MailMessageService {

    void receiveOpen(String base64TrackInf) throws UnsupportedEncodingException;

}
