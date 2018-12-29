package com.leyou.pojo;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "tb_specification")
@Data
public class Specification {

    @Id
    private Long categoryId;
    private String specifications;
}