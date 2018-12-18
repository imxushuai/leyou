package com.leyou.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LyExceptionEnum {

    PARAM_CANNOT_BE_NULL(400, "参数不能为空"),
    CATEGORY_LIST_NOT_FOUND(404, "未查询到分类列表"),
    CATEGORY_NOT_FOUND(404, "该分类不存在"),
    PARENT_CATEGORY_NOT_FOUND(404, "该父分类不存在"),
    SAVE_FAILURE(500, "保存失败"),
    DELETE_FAILURE(500, "删除失败"),
    DELETE_INVALID(500, "该分类下含有子分类，请先删除其子分类"),
    ;
    private int code;
    private String message;
}
