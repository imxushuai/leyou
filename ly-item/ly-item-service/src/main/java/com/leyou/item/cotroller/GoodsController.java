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
     */
    @GetMapping("/spu/page")
    public ResponseEntity<PageResult<SpuBO>> querySpuByPage(@RequestParam(value = "page", defaultValue = "1") int page,
                                                            @RequestParam(value = "rows", defaultValue = "5") int rows,
                                                            @RequestParam(value = "sortBy", required = false) String sortBy,
                                                            @RequestParam(value = "desc", defaultValue = "false") Boolean desc,
                                                            @RequestParam("key") String key,
                                                            @RequestParam(value = "saleable", required = false) Boolean saleable) {
        return ResponseEntity.ok(goodsService.querySpuByPage(page, rows, sortBy, desc, key, saleable));
    }

    /**
     * 新增商品
     */
    @PostMapping
    public ResponseEntity<Spu> addGoods(@RequestBody SpuBO goods) {
        if (goods == null)
            throw new LyException(LyExceptionEnum.PARAM_CANNOT_BE_NULL);

        // 保存
        return ResponseEntity.status(HttpStatus.CREATED).body(goodsService.addGoods(goods));
    }

    /**
     * 更新商品
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
     */
    @GetMapping("/sku/status/{skuId}")
    public ResponseEntity<Sku> updateSkuStatus(@PathVariable("skuId") Long skuId,
                                               @RequestParam("status") Boolean status) {
        return ResponseEntity.ok(goodsService.updateGoodsStatus(skuId, status));
    }

    /**
     * 删除商品
     */
    @DeleteMapping("/{spuId}")
    public ResponseEntity<Spu> deleteGoods(@PathVariable("spuId") Long spuId) {
        return ResponseEntity.ok(goodsService.deleteGoods(spuId));
    }
}
