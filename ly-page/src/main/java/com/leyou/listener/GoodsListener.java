package com.leyou.listener;

import com.leyou.common.util.LeyouConstants;
import com.leyou.page.service.PageService;
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
    private PageService pageService;

    /**
     * 新增/更新 商品详情页
     *
     * @param id 商品id
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = LeyouConstants.QUEUE_SAVE_PAGE, durable = "true"),
            exchange = @Exchange(name = LeyouConstants.EXCHANGE_DEFAULT_ITEM),
            key = {LeyouConstants.QUEUE_INSERT_ITEM, LeyouConstants.QUEUE_UPDATE_ITEM}
    ))
    public void savePage(Long id) {
        if (id != null)
            pageService.asyncExcute(id);
    }

    /**
     * 删除指定商品的详情页
     *
     * @param id 商品id
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = LeyouConstants.QUEUE_DELETE_PAGE, durable = "true"),
            exchange = @Exchange(name = LeyouConstants.EXCHANGE_DEFAULT_ITEM),
            key = LeyouConstants.QUEUE_DELETE_ITEM
    ))
    public void deleteIndex(Long id) {
        if (id != null)
            pageService.deleteHtml(id);
    }

}