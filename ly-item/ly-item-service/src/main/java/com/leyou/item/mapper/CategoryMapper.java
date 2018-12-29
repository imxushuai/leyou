package com.leyou.item.mapper;

import com.leyou.pojo.Category;
import tk.mybatis.mapper.additional.idlist.SelectByIdListMapper;
import tk.mybatis.mapper.common.Mapper;

public interface CategoryMapper extends Mapper<Category>, SelectByIdListMapper<Category, Long> {
}
