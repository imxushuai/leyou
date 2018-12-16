package com.leyou.common.vo;

import com.leyou.common.enums.LyExceptionEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LyExceptionResult {
    private int status;
    private String message;
    private long timestamp;

    public LyExceptionResult(LyExceptionEnum lyExceptionEnum) {
        this.status = lyExceptionEnum.getCode();
        this.message = lyExceptionEnum.getMessage();
        this.timestamp = System.currentTimeMillis();
    }
}
