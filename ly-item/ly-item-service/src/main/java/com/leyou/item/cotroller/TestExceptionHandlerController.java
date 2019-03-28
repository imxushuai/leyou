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
