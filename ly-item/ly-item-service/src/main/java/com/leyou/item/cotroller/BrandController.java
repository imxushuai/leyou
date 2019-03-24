package com.leyou.item.cotroller;

import com.leyou.common.enums.LyExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.vo.PageResult;
import com.leyou.item.service.BrandService;
import com.leyou.pojo.Brand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("brand")
public class BrandController {

    @Autowired
    private BrandService brandService;

    /**
     * 分页查询品牌集合
     *
     * @param page   当前页码
     * @param rows   每页记录数
     * @param sortBy 排序字段
     * @param desc   是否降序
     * @param key    查询关键字
     * @return List<Brand>
     */
    @GetMapping("/page")
    public ResponseEntity<PageResult<Brand>> queryBrandByPage(@RequestParam(value = "page", defaultValue = "1") int page,
                                                              @RequestParam(value = "rows", defaultValue = "5") int rows,
                                                              @RequestParam("sortBy") String sortBy,
                                                              @RequestParam(value = "desc", defaultValue = "false") Boolean desc,
                                                              @RequestParam("key") String key) {
        return ResponseEntity.ok(brandService.queryBrandByPage(page, rows, sortBy, desc, key));
    }

    /**
     * 新增品牌
     *
     * @param brand      品牌
     * @param categories 分类信息
     * @return Brand
     */
    @PostMapping
    public ResponseEntity<Brand> addBrand(@RequestBody Brand brand,
                                          @RequestParam("categories") List<Long> categories) {
        if (CollectionUtils.isEmpty(categories)) {
            throw new LyException(LyExceptionEnum.PARAM_INVALID);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(brandService.addBrand(brand, categories));
    }

    /**
     * 更新品牌
     *
     * @param brand      品牌
     * @param categories 分类信息
     * @return Brand
     */
    @PutMapping
    public ResponseEntity<Brand> editBrand(@RequestBody Brand brand,
                                           @RequestParam("categories") List<Long> categories) {
        if (CollectionUtils.isEmpty(categories) || brand.getId() == null) {
            throw new LyException(LyExceptionEnum.PARAM_INVALID);
        }
        return ResponseEntity.ok(brandService.saveBrand(brand, categories));
    }

    /**
     * 删除指定ID的品牌
     *
     * @param brandId 品牌ID
     * @return Brand
     */
    @DeleteMapping("/{brandId}")
    public ResponseEntity<Brand> deleteBrand(@PathVariable("brandId") long brandId) {
        return ResponseEntity.ok(brandService.deleteBrand(brandId));
    }

    /**
     * 查询指定分类的品牌集合
     *
     * @param categoryId 分类ID
     * @return List<Brand>
     */
    @GetMapping("/cid/{categoryId}")
    public ResponseEntity<List<Brand>> queryBrandByCategoryId(@PathVariable("categoryId") Long categoryId) {
        if (categoryId == null) {
            throw new LyException(LyExceptionEnum.PARAM_CANNOT_BE_NULL);
        }
        // 查询
        return ResponseEntity.ok(brandService.queryBrandByCategoryId(categoryId));
    }

    /**
     * 查询指定ID的品牌
     *
     * @param brandId 品牌ID
     * @return Brand
     */
    @GetMapping("/{brandId}")
    public ResponseEntity<Brand> queryBrandById(@PathVariable("brandId") long brandId) {
        return ResponseEntity.ok(brandService.queryById(brandId));
    }

    /**
     * 查询指定ID的品牌
     *
     * @param ids 品牌ID集合
     * @return Brand
     */
    @GetMapping("/ids")
    public ResponseEntity<List<Brand>> queryBrandByIds(@RequestParam("ids") List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new LyException(LyExceptionEnum.PARAM_CANNOT_BE_NULL);
        }
        return ResponseEntity.ok(brandService.queryByIds(ids));
    }


}
