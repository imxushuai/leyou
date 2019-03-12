package com.leyou.item.service;

import com.leyou.common.enums.LyExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.item.mapper.CategoryMapper;
import com.leyou.pojo.Category;
import com.leyou.pojo.CategoryConstans;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 按分类父ID查询分类列表
     *
     * @param pid 父ID
     * @return 分类列表
     */
    public List<Category> queryCategoryListByPid(Long pid) {
        Category category = new Category();
        category.setParentId(pid);
        List<Category> categoryList = categoryMapper.select(category);
        if (CollectionUtils.isEmpty(categoryList)) {
            throw new LyException(LyExceptionEnum.CATEGORY_LIST_NOT_FOUND);
        }
        return categoryList;
    }

    /**
     * 新增分类
     *
     * @param category 分类
     * @return 分类
     */
    public Category addCategory(Category category) {
        // 判断其父节点是否为父节点
        if (category.getParentId() != CategoryConstans.FIRST_CATEGORY_PARENT_ID) {
            Category pCategory = categoryMapper.selectByPrimaryKey(category.getParentId());
            if (pCategory != null) {
                // 判断是否已经是父菜单
                if (!pCategory.getIsParent()) {// 不为父菜单
                    // 设置为父菜单并保存
                    pCategory.setIsParent(true);
                    categoryMapper.updateByPrimaryKey(pCategory);
                }
            } else {
                throw new LyException(LyExceptionEnum.PARENT_CATEGORY_NOT_FOUND);
            }
        }
        if (categoryMapper.insert(category) != 1) {
            throw new LyException(LyExceptionEnum.SAVE_FAILURE);
        }
        return category;
    }

    /**
     * 编辑分类
     *
     * @param category 分类
     * @return 分类
     */
    public Category saveCategory(Category category) {
        if (categoryMapper.updateByPrimaryKey(category) != 1) {
            throw new LyException(LyExceptionEnum.SAVE_FAILURE);
        }
        return category;
    }

    /**
     * 删除费雷
     *
     * @param categoryId 分类id
     * @return 被删除的分类
     */
    public Category deleteCategory(Long categoryId) {
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if (category == null) {
            throw new LyException(LyExceptionEnum.CATEGORY_NOT_FOUND);
        }
        // 判断该分类是否为父分类，若为父分类判断分类下是否有子分类
        if (category.getIsParent()) {
            Category param = new Category();
            param.setParentId(categoryId);
            if (categoryMapper.select(param).size() > 0) {
                throw new LyException(LyExceptionEnum.CATEGORY_DELETE_INVALID);
            }
        }
        if (categoryMapper.delete(category) != 1) {
            throw new LyException(LyExceptionEnum.SAVE_FAILURE);
        }
        return category;
    }

    /**
     * 按ID查询分类集合，id可以为多个
     *
     * @param ids id集合
     * @return List<Category>
     */
    public List<Category> queryByIds(List<Long> ids) {
        return categoryMapper.selectByIdList(ids);
    }
}
