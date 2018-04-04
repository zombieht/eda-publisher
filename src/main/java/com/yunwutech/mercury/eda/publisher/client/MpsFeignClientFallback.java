package com.yunwutech.mercury.eda.publisher.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MpsFeignClientFallback implements MpsFeignClient {
    private final Logger log = LoggerFactory.getLogger(MpsFeignClientFallback.class);

    @Override
    public void postEdaEvent(CreateMessageDTO createMessageDTO) {

    }
}
