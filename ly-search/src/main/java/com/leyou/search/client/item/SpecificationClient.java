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
package com.leyou.search.client.item;

import com.leyou.api.SpecificationApi;
import com.leyou.common.util.LeyouConstans;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * 商品微服务 - 规格接口
 */
@FeignClient(LeyouConstans.SERVICE_ITEM)
public interface SpecificationClient extends SpecificationApi {
}
