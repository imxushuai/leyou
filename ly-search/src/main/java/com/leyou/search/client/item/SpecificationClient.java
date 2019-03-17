package com.leyou.search.client.item;

import com.leyou.api.SpecificationApi;
import com.leyou.common.util.LeyouConstans;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * 商品微服务 - 规格接口
 */
@FeignClient(LeyouConstans.SERVICE_ITEM)
public interface SpecificationClient extends SpecificationApi {
}
