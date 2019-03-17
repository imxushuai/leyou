package com.leyou.search.client.item;

import com.leyou.api.CategoryApi;
import com.leyou.common.util.LeyouConstans;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * 商品微服务 - 分类接口
 */
@FeignClient(LeyouConstans.SERVICE_ITEM)
public interface CategoryClient extends CategoryApi {

}
