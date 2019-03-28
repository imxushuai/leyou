/**
 * Copyright © 2019-2019 imxushuai
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

import com.leyou.common.enums.LyExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.item.service.CategoryService;
import com.leyou.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 按父ID查询分类集合
     *
     * @param pid 父id
     * @return List<Category>
     */
    @GetMapping("/list")
    public ResponseEntity<List<Category>> queryCategoryListByPid(@RequestParam(value = "pid", defaultValue = "0") Long pid) {
        return ResponseEntity.ok(categoryService.queryCategoryListByPid(pid));
    }

    /**
     * 新增分类
     *
     * @param category 分类
     * @return Category
     */
    @PostMapping
    public ResponseEntity<Category> addCategory(@RequestBody Category category) {
        if (category == null) {
            throw new LyException(LyExceptionEnum.PARAM_CANNOT_BE_NULL);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.addCategory(category));
    }

    /**
     * 更新分类
     *
     * @param category 分类
     * @return Category
     */
    @PutMapping
    public ResponseEntity<Category> saveCategory(@RequestBody Category category) {
        if (category == null) {
            throw new LyException(LyExceptionEnum.PARAM_CANNOT_BE_NULL);
        }
        return ResponseEntity.ok(categoryService.saveCategory(category));
    }

    /**
     * 删除指定ID的分类
     *
     * @param categoryId 分类ID
     * @return Category 被删除的分类对象
     */
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Category> deleteCategory(@PathVariable("categoryId") Long categoryId) {
        if (categoryId == null) {
            throw new LyException(LyExceptionEnum.PARAM_CANNOT_BE_NULL);
        }
        return ResponseEntity.ok(categoryService.deleteCategory(categoryId));
    }

    /**
     * 根据商品分类id查询分类集合
     *
     * @param ids 要查询的分类id集合
     * @return 多个名称的集合
     */
    @GetMapping("/list/ids")
    public ResponseEntity<List<Category>> queryCategoryListByIds(@RequestParam("ids") List<Long> ids) {
        if (ids.isEmpty()) {
            throw new LyException(LyExceptionEnum.PARAM_CANNOT_BE_NULL);
        }
        return ResponseEntity.ok(categoryService.queryByIds(ids));
    }

    /**
     * 根据商品分类id查询名称
     *
     * @param ids 要查询的分类id集合
     * @return 多个名称的集合
     */
    @GetMapping("names")
    public ResponseEntity<List<String>> queryNameByIds(@RequestParam("ids") List<Long> ids) {
        if (ids.isEmpty()) {
            throw new LyException(LyExceptionEnum.PARAM_CANNOT_BE_NULL);
        }
        List<String> list = this.categoryService.queryNameByIds(ids);
        if (list == null || list.size() < 1) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(list);
    }


}
