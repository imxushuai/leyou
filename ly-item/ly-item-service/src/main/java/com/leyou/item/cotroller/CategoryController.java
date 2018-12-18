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

    @GetMapping("/list")
    public ResponseEntity<List<Category>> queryCategoryListByPid(@RequestParam(value = "pid", defaultValue = "0") Long pid) {
        return ResponseEntity.ok(categoryService.queryCategoryListByPid(pid));
    }

    @PostMapping()
    public ResponseEntity<Category> addCategory(@RequestBody Category category) {
        if (category == null) {
            throw new LyException(LyExceptionEnum.PARAM_CANNOT_BE_NULL);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.addCategory(category));
    }

    @PutMapping()
    public ResponseEntity<Category> saveCategory(@RequestBody Category category) {
        if (category == null) {
            throw new LyException(LyExceptionEnum.PARAM_CANNOT_BE_NULL);
        }
        return ResponseEntity.ok(categoryService.saveCategory(category));
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Category> deleteCategory(@PathVariable("categoryId") Long categoryId) {
        if (categoryId == null) {
            throw new LyException(LyExceptionEnum.PARAM_CANNOT_BE_NULL);
        }
        return ResponseEntity.ok(categoryService.deleteCategory(categoryId));
    }
}
