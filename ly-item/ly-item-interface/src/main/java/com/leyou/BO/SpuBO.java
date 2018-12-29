package com.leyou.BO;

import com.leyou.pojo.Spu;
import lombok.Data;

@Data
public class SpuBO extends Spu {
    // 分类名称
    private String cname;
    // 品牌名称
    private String bname;
}

