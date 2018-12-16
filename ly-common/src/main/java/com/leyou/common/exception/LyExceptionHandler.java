package com.leyou.common.exception;

import com.leyou.common.vo.LyExceptionResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class LyExceptionHandler {

    @ExceptionHandler(LyException.class)
    public ResponseEntity<LyExceptionResult> commonExceptionHandler(LyException e) {
        return ResponseEntity.status(e.getLyExceptionEnum().getCode())
                .body(new LyExceptionResult(e.getLyExceptionEnum()));
    }
}
