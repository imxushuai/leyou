package com.leyou.search.client.item;

import com.leyou.api.BrandApi;
import com.leyou.common.util.LeyouConstans;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * 商品微服务 - 品牌接口
 */
@FeignClient(LeyouConstans.SERVICE_ITEM)
public interface BrandClient extends BrandApi {
}
