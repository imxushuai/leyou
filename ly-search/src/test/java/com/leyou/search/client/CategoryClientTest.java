package com.leyou.search.client;

import com.leyou.pojo.Category;
import com.leyou.search.client.item.CategoryClient;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CategoryClientTest {

    @Autowired
    private CategoryClient categoryClient;


    @Test
    public void testQueryCategoryNames() {
        List<Category> categoryNames = categoryClient.queryCategoryListByIds(Arrays.asList(1L, 2L, 3L));
        Assert.assertEquals(3, categoryNames.size());
    }
}