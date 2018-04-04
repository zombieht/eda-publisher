package com.yunwutech.mercury.eda.publisher.client;

import org.springframework.web.bind.annotation.*;

@AuthorizedFeignClient(name = "MPS", fallback = MpsFeignClientFallback.class)
public interface MpsFeignClient {

    @RequestMapping(value = "/api/message", method = RequestMethod.POST)
    void postEdaEvent(@RequestBody CreateMessageDTO createMessageDTO);
}
