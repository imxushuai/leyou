package com.leyou.auth.client;

import com.leyou.api.UserApi;
import com.leyou.common.util.LeyouConstants;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(LeyouConstants.SERVICE_USER)
public interface UserClient extends UserApi {
}
