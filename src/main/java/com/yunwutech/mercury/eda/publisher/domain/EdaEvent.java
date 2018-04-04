package com.yunwutech.mercury.eda.publisher.domain;

import com.yunwutech.mercury.eda.publisher.domain.enumeration.MessageType;
import com.yunwutech.mercury.eda.publisher.domain.enumeration.SystemType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;


@ApiModel(description = "消息事件表")
public class EdaEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "发起系统")
    private SystemType fromSystem;

    @ApiModelProperty(value = "接收人")
    private Long received;

    @ApiModelProperty(value = "接收系统")
    private SystemType toSystem;

    @ApiModelProperty(value = "消息内容")
    private String messageContent;

    @ApiModelProperty(value = "系统可识别标识")
    private Long identitier;

    @ApiModelProperty(value = "通知接收者")
    private Integer notice;

    @ApiModelProperty(value = "消息类型")
    private MessageType messageType;

    public SystemType getFromSystem() {
        return fromSystem;
    }

    public void setFromSystem(SystemType fromSystem) {
        this.fromSystem = fromSystem;
    }

    public Long getReceived() {
        return received;
    }

    public void setReceived(Long received) {
        this.received = received;
    }

    public SystemType getToSystem() {
        return toSystem;
    }

    public void setToSystem(SystemType toSystem) {
        this.toSystem = toSystem;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public Long getIdentitier() {
        return identitier;
    }

    public void setIdentitier(Long identitier) {
        this.identitier = identitier;
    }

    public Integer getNotice() {
        return notice;
    }

    public void setNotice(Integer notice) {
        this.notice = notice;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }


}
