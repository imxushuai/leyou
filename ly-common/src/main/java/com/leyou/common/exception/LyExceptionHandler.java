/**
 * Copyright © 2019-Now imxushuai
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
