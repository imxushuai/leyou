package com.leyou.item.cotroller;

import com.leyou.BO.SpuBO;
import com.leyou.common.vo.PageResult;
import com.leyou.item.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @GetMapping("/spu/page")
    public ResponseEntity<PageResult<SpuBO>> querySpuByPage(@RequestParam(value = "page", defaultValue = "1") int page,
                                                            @RequestParam(value = "rows", defaultValue = "5") int rows,
                                                            @RequestParam(value = "sortBy", required = false) String sortBy,
                                                            @RequestParam(value = "desc", defaultValue = "false") Boolean desc,
                                                            @RequestParam("key") String key,
                                                            @RequestParam(value = "saleable", required = false, defaultValue = "true") Boolean saleable) {
        return ResponseEntity.ok(goodsService.querySpuByPage(page, rows, sortBy, desc, key, saleable));
    }
}
