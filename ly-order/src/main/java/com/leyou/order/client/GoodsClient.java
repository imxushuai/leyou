package com.leyou.order.client;

import com.leyou.api.GoodsApi;
import com.leyou.common.util.LeyouConstants;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(LeyouConstants.SERVICE_ITEM)
public interface GoodsClient extends GoodsApi {
}
