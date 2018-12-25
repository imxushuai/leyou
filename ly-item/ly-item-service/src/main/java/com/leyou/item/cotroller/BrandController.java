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

    @GetMapping("/page")
    public ResponseEntity<PageResult<Brand>> queryBrandByPage(@RequestParam(value = "page", defaultValue = "1") int page,
                                                              @RequestParam(value = "rows", defaultValue = "5") int rows,
                                                              @RequestParam("sortBy") String sortBy,
                                                              @RequestParam(value = "desc",defaultValue = "false") Boolean desc,
                                                              @RequestParam("key") String key) {
        return ResponseEntity.ok(brandService.queryBrandByPage(page, rows, sortBy, desc, key));
    }

    @PostMapping
    public ResponseEntity<Brand> addBrand(@RequestBody Brand brand,
                                          @RequestParam("categories") List<Long> categories) {
        if (CollectionUtils.isEmpty(categories)) {
            throw new LyException(LyExceptionEnum.PARAM_INVALID);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(brandService.addBrand(brand, categories));
    }

    @PutMapping
    public ResponseEntity<Brand> editBrand(@RequestBody Brand brand,
                                           @RequestParam("categories") List<Long> categories) {
        if (CollectionUtils.isEmpty(categories) || brand.getId() == null) {
            throw new LyException(LyExceptionEnum.PARAM_INVALID);
        }
        return ResponseEntity.ok(brandService.saveBrand(brand, categories));
    }

    @DeleteMapping("/{brandId}")
    public ResponseEntity<Brand> deleteBrand(@PathVariable("brandId") long brandId) {
        return ResponseEntity.ok(brandService.deleteBrand(brandId));
    }


}
