package com.yunwutech.mercury.eda.publisher.domain.enumeration;

import io.swagger.annotations.ApiModel;

/**
 * Created by yw on 2017/12/8.
 */
@ApiModel(description = "消息通知类型")
public enum MessageType {
    Order_Wait_Pay("订单待支付"), Order_Wait("订单待接单"), Order_Shop_Received("商家已接单"), Order_Stock_Notice("备货通知"), Order_Distribution_Wait("配送待接单"),
    Order_Completion("配送完成"), Order_Close("订单关闭"), Order_Apply_Cancel("申请取消订单"), Rights_Apply("申请维权"), Rights_Refund("维权退款通知"), Shop_Lack_Of_Stock("库存不足"),
    Order_Unnormal("异常订单");

    private String description;

    private MessageType(String description) {
        this.description = description;
    }

    private String description() {
        return description;
    }
}
