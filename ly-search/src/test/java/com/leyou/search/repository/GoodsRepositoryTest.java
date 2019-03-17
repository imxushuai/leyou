package com.leyou.search.repository;

import com.leyou.BO.SpuBO;
import com.leyou.common.vo.PageResult;
import com.leyou.search.client.item.GoodsClient;
import com.leyou.search.pojo.Goods;
import com.leyou.search.service.SearchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GoodsRepositoryTest {

    @Autowired
    private ElasticsearchTemplate esClient;

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private SearchService searchService;

    @Test
    public void testCreateIndex() {
        esClient.createIndex(Goods.class);
        esClient.putMapping(Goods.class);
    }

    @Test
    public void loadData() {
        int page = 1;
        int rows = 100;
        int size;
        do {
            // 查询spu集合
            PageResult<SpuBO> spuList = goodsClient.querySpuByPage(page, rows, null, null, null, true);
            if (spuList == null || spuList.getItems().isEmpty()) {
                break;
            }
            size = spuList.getItems().size();

            List<Goods> goodsList = spuList.getItems().stream()
                    .map(searchService::buildGoods).filter(Objects::nonNull).collect(Collectors.toList());

            // 保存至索引库
            goodsRepository.saveAll(goodsList);
            page++;
        } while (size == 100);
    }
}