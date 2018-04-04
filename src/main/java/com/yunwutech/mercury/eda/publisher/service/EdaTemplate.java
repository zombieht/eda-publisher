package com.yunwutech.mercury.eda.publisher.service;


import com.yunwutech.mercury.eda.publisher.client.CreateMessageDTO;
import com.yunwutech.mercury.eda.publisher.client.MpsFeignClient;
import com.yunwutech.mercury.eda.publisher.domain.EdaEvent;
import com.yunwutech.mercury.eda.publisher.domain.enumeration.MessageType;
import com.yunwutech.mercury.eda.publisher.domain.enumeration.SystemType;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Service
public class EdaTemplate {
    @Autowired
    DataSource dataSource;

    @Autowired
    private MpsFeignClient mpsFeignClient;

    @Transactional
    public void createEvent(SystemType fromSystem,Long received,SystemType toSystem,String messageContent,Long identitier,Integer notice,MessageType messageType) {
        EdaEvent edaEvent = new EdaEvent();
        edaEvent.setFromSystem(fromSystem);
        edaEvent.setReceived(received);
        edaEvent.setToSystem(toSystem);
        edaEvent.setMessageContent(messageContent);
        edaEvent.setIdentitier(identitier);
        edaEvent.setNotice(notice);
        edaEvent.setMessageType(messageType);
        create(edaEvent);
    }

    @Transactional
    public EdaEvent create(EdaEvent event) {
        Connection conn = null;
        PreparedStatement pstmt = null;


        try {
            conn = DataSourceUtils.getConnection(dataSource);
            String guid = UUID.randomUUID().toString() + RandomStringUtils.randomNumeric(8);

            String sql = "INSERT INTO `eda_event` ( `from_system`, `has_read`, `identitier`, " +
                    "`message_content`, `message_type`, `notice`, `received`, `send_time`, `sender`, `to_system`,`guid`) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, event.getFromSystem().name());
            pstmt.setBoolean(2, false);
            pstmt.setLong(3, event.getIdentitier());
            pstmt.setString(4, event.getMessageContent());
            pstmt.setString(5, event.getMessageType().name());
            pstmt.setInt(6, event.getNotice());
            pstmt.setLong(7, event.getReceived());
            pstmt.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setString(9, "System");
            pstmt.setString(10, event.getToSystem().name());
            pstmt.setString(11, guid);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            try {
                if (pstmt != null)
                    pstmt.close();
            } catch (SQLException se) {
            }
        }
        return event;

    }


    @Scheduled(fixedDelay = 1000)
    @Transactional
    public void sendMessage() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DataSourceUtils.getConnection(dataSource);
            String sql = "SELECT * FROM eda_event ";
            pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Long id = rs.getLong(1);
                CreateMessageDTO event = new CreateMessageDTO();
                event.setFromSystem(SystemType.valueOf(rs.getString(6)));
                event.setGuid(rs.getString(16));
                event.setIdentitier(rs.getLong(8));
                event.setMessageContent(rs.getString(9));
                event.setMessageType(MessageType.valueOf(rs.getString(10)));
                event.setNotice(rs.getInt(11));
                event.setReceived(rs.getLong(12));
                event.setSender(rs.getString(14));
                event.setSendTime(LocalDateTime.ofInstant(rs.getTimestamp(13).toInstant(), ZoneId.systemDefault()));
                event.setToSystem(SystemType.valueOf(rs.getString(15)));
                try {
                    mpsFeignClient.postEdaEvent(event);
                    String update = "delete from eda_event where id='" + id + "'";
                    PreparedStatement psupdate = conn.prepareStatement(update);
                    psupdate.executeUpdate();
                    psupdate.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            try {
                if (pstmt != null)
                    pstmt.close();
            } catch (SQLException se) {
            }

        }


    }

}
