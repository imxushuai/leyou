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
package com.leyou.search.pojo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@Document(indexName = "goods", type = "docs", shards = 1, replicas = 0)
public class Goods {
    @Id
    private Long id; // spuId

    @Field(type = FieldType.text, analyzer = "ik_max_word")
    private String all; // 所有需要被搜索的信息，包含标题，分类，甚至品牌

    @Field(type = FieldType.keyword, index = false)
    private String subTitle;// 卖点

    private Long brandId;// 品牌id

    private Long cid1;// 1级分类id

    private Long cid2;// 2级分类id

    private Long cid3;// 3级分类id

    private Date createTime;// 创建时间

    private List<Long> price;// 价格

    @Field(type = FieldType.keyword, index = false)
    private String skus;// sku信息的json结构

    private Map<String, Object> specs;// 可搜索的规格参数，key是参数名，值是参数值
}
