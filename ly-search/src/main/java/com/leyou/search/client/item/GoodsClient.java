package com.leyou.search.client.item;

import com.leyou.api.GoodsApi;
import com.leyou.common.util.LeyouConstans;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * 商品微服务 - 商品接口
 */
@FeignClient(LeyouConstans.SERVICE_ITEM)
public interface GoodsClient extends GoodsApi {

}
