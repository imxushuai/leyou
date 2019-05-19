package com.leyou.item.conf;

import com.leyou.common.util.LeyouConstants;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;

//@Configuration
public class RabbitmqConfiguration {

//    @Bean
    public Exchange defalueExchange() {
        return new TopicExchange(LeyouConstants.EXCHANGE_DEFAULT_ITEM, true, false);
    }


//    @Bean
    public Queue updateItemQueue() {
        return new Queue(LeyouConstants.QUEUE_UPDATE_ITEM, true);
    }

//    @Bean
    public Queue insertItemQueue() {
        return new Queue(LeyouConstants.QUEUE_INSERT_ITEM, true);
    }

//    @Bean
    public Queue deleteItemQueue() {
        return new Queue(LeyouConstants.QUEUE_DELETE_ITEM, true);
    }

}
