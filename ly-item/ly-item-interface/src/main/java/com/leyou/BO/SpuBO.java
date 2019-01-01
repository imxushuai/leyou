package com.leyou.BO;

import com.leyou.pojo.Sku;
import com.leyou.pojo.Spu;
import com.leyou.pojo.SpuDetail;
import lombok.Data;

import javax.persistence.Transient;
import java.util.List;

@Data
public class SpuBO extends Spu {
    // 分类名称
    @Transient
    private String cname;

    // 品牌名称
    @Transient
    private String bname;

    // 商品描述
    @Transient
    private SpuDetail spuDetail;

    // 商品列表
    @Transient
    private List<Sku> skuList;
}