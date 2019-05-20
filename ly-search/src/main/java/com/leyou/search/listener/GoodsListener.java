package com.leyou.search.listener;

import com.leyou.common.util.LeyouConstants;
import com.leyou.search.service.SearchService;
import com.leyou.search.util.SearchAppConstants;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 处理商品微服务发送的消息
 */
@Component
public class GoodsListener {

    @Autowired
    private SearchService searchService;

    /**
     * 新增/更新 索引库
     *
     * @param id 商品id
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = SearchAppConstants.QUEUE_SAVE_SEARCH, durable = "true"),
            exchange = @Exchange(name = LeyouConstants.EXCHANGE_DEFAULT_ITEM),
            key = {LeyouConstants.ROUTING_KEY_INSERT_ITEM, LeyouConstants.ROUTING_KEY_UPDATE_ITEM}
    ))
    public void saveIndex(Long id) {
        if (id != null)
            searchService.saveIndex(id);
    }

    /**
     * 删除指定商品的索引
     *
     * @param id 商品id
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = SearchAppConstants.QUEUE_DELETE_SEARCH, durable = "true"),
            exchange = @Exchange(name = LeyouConstants.EXCHANGE_DEFAULT_ITEM),
            key = LeyouConstants.ROUTING_KEY_DELETE_ITEM
    ))
    public void deleteIndex(Long id) {
        if (id != null)
            searchService.deleteIndex(id);
    }

}
