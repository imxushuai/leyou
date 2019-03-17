package com.leyou.api;

import com.leyou.BO.SpuBO;
import com.leyou.common.enums.LyExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.vo.PageResult;
import com.leyou.pojo.Sku;
import com.leyou.pojo.SpuDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 商品对外接口
 */
public interface GoodsApi {
    /**
     * 按商品ID查询该商品所有规格的商品列表
     *
     * @param spuId 商品ID
     * @return List<Sku>
     */
    @GetMapping("goods/sku/list/{spuId}")
    List<Sku> querySkuListById(@PathVariable("spuId") Long spuId);

    /**
     * 按商品ID查询商品描述
     *
     * @param spuId 商品ID
     * @return SpuDetail
     */
    @GetMapping("goods/spu/detail/{spuId}")
    SpuDetail querySpuDetailById(@PathVariable("spuId") Long spuId);

    /**
     * 分页查询商品列表
     *
     * @param page     当前页码
     * @param rows     每页记录数
     * @param sortBy   排序字段
     * @param desc     是否降序
     * @param key      查询关键字
     * @param saleable 是否上架
     * @return List<SpuBO>
     */
    @GetMapping("goods/spu/page")
    PageResult<SpuBO> querySpuByPage(@RequestParam(value = "page", defaultValue = "1") int page,
                                                            @RequestParam(value = "rows", defaultValue = "5") int rows,
                                                            @RequestParam(value = "sortBy", required = false) String sortBy,
                                                            @RequestParam(value = "desc", defaultValue = "false") Boolean desc,
                                                            @RequestParam("key") String key,
                                                            @RequestParam(value = "saleable", required = false) Boolean saleable);
}
