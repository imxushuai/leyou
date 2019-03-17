package com.leyou.api;

import com.leyou.common.enums.LyExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.pojo.Category;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 分类对外接口
 */
public interface CategoryApi {
    /**
     * 根据商品分类id查询分类集合
     *
     * @param ids 要查询的分类id集合
     * @return 多个名称的集合
     */
    @GetMapping("category/list/ids")
    List<Category> queryCategoryListByIds(@RequestParam("ids") List<Long> ids);

    /**
     * 根据商品分类id查询名称
     *
     * @param ids 要查询的分类id集合
     * @return 多个名称的集合
     */
    @GetMapping("category/names")
    List<String> queryNameByIds(@RequestParam("ids") List<Long> ids);
}
