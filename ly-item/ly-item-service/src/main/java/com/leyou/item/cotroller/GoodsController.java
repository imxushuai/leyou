/**
 * Copyright © 2019-Now imxushuai
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.leyou.item.cotroller;

import com.leyou.BO.SpuBO;
import com.leyou.common.enums.LyExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.vo.PageResult;
import com.leyou.item.service.GoodsService;
import com.leyou.pojo.Sku;
import com.leyou.pojo.Spu;
import com.leyou.pojo.SpuDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

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
    @GetMapping("/spu/page")
    public ResponseEntity<PageResult<SpuBO>> querySpuByPage(@RequestParam(value = "page", defaultValue = "1") int page,
                                                            @RequestParam(value = "rows", defaultValue = "5") int rows,
                                                            @RequestParam(value = "sortBy", required = false) String sortBy,
                                                            @RequestParam(value = "desc", required = false, defaultValue = "false") Boolean desc,
                                                            @RequestParam(value = "key", required = false) String key,
                                                            @RequestParam(value = "saleable", required = false) Boolean saleable) {
        return ResponseEntity.ok(goodsService.querySpuByPage(page, rows, sortBy, desc, key, saleable));
    }

    /**
     * 新增商品
     *
     * @param goods 商品
     * @return Spu
     */
    @PostMapping
    public ResponseEntity<Spu> addGoods(@RequestBody SpuBO goods) {
        if (goods == null)
            throw new LyException(LyExceptionEnum.PARAM_CANNOT_BE_NULL);

        // 保存
        return ResponseEntity.status(HttpStatus.CREATED).body(goodsService.addGoods(goods));
    }

    /**
     * 编辑商品
     *
     * @param goods 商品
     * @return Spu
     */
    @PutMapping
    public ResponseEntity<Spu> saveGoods(@RequestBody SpuBO goods) {
        if (goods == null)
            throw new LyException(LyExceptionEnum.PARAM_CANNOT_BE_NULL);

        // 保存
        return ResponseEntity.ok(goodsService.saveGoods(goods));
    }

    /**
     * 按商品ID查询商品描述
     *
     * @param spuId 商品ID
     * @return SpuDetail
     */
    @GetMapping("/spu/detail/{spuId}")
    public ResponseEntity<SpuDetail> querySpuDetailById(@PathVariable("spuId") Long spuId) {
        if (spuId == null) {
            throw new LyException(LyExceptionEnum.PARAM_CANNOT_BE_NULL);
        }
        return ResponseEntity.ok(goodsService.querySpuDetailById(spuId));
    }

    /**
     * 按商品ID查询该商品所有规格的商品列表
     *
     * @param spuId 商品ID
     * @return List<Sku>
     */
    @GetMapping("/sku/list/{spuId}")
    public ResponseEntity<List<Sku>> querySkuListById(@PathVariable("spuId") Long spuId) {
        if (spuId == null) {
            throw new LyException(LyExceptionEnum.PARAM_CANNOT_BE_NULL);
        }
        return ResponseEntity.ok(goodsService.querySkuListById(spuId));
    }

    /**
     * 上架下架
     *
     * @param skuId  skuID
     * @param status 修改后的商品状态
     * @return Sku
     */
    @GetMapping("/sku/status/{skuId}")
    public ResponseEntity<Sku> updateSkuStatus(@PathVariable("skuId") Long skuId,
                                               @RequestParam("status") Boolean status) {
        return ResponseEntity.ok(goodsService.updateGoodsStatus(skuId, status));
    }

    /**
     * 删除指定spuId的商品
     *
     * @param spuId 商品ID
     * @return Spu 被删除的spu
     */
    @DeleteMapping("/{spuId}")
    public ResponseEntity<Spu> deleteGoods(@PathVariable("spuId") Long spuId) {
        return ResponseEntity.ok(goodsService.deleteGoods(spuId));
    }

    /**
     * 查询spu信息
     *
     * @param spuId 商品ID
     * @return Spu
     */
    @GetMapping("/{spuId}")
    public ResponseEntity<SpuBO> queryGoodsById(@PathVariable("spuId") Long spuId) {
        return ResponseEntity.ok(goodsService.queryGoodsById(spuId));
    }

    /**
     * 查询sku信息
     *
     * @param skuId skuId
     * @return Sku 商品sku信息
     */
    @GetMapping("/sku/{skuId}")
    public ResponseEntity<Sku> querySkuById(@PathVariable("skuId") Long skuId) {
        return ResponseEntity.ok(goodsService.querySkuById(skuId));
    }
}
