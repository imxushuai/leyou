package com.leyou.item.cotroller;

import com.leyou.common.enums.LyExceptionEnum;
import com.leyou.common.exception.LyException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestExceptionHandlerController {

    @PostMapping("/test")
    public ResponseEntity<String> test(String msg) {
        if (StringUtils.isBlank(msg)) {
            throw new LyException(LyExceptionEnum.PARAM_CANNOT_BE_NULL);
        }
        return ResponseEntity.ok(msg);
    }
}
