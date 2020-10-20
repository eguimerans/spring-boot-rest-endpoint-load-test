/*
 * Pyx4j framework
 * Copyright (C) 2008-2019 pyx4j.com.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 * Created on Oct. 20, 2020
 * @author ernestog
 */
package com.propertyvista.playground.springbootrestendpoint;

import static com.google.common.base.Predicates.*;
import static springfox.documentation.builders.PathSelectors.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.base.Predicate;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket postsApi() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("default")
                .apiInfo(apiInfo())
                .select().paths(getPaths())
                .build();
    }

    private Predicate<String> getPaths() {
        return or(regex("/echo*"), regex("/status*"), regex("/primes*"), regex("/timeConsumingOperation*"));
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("Test and metrics Utility REST Endpoints")
                .description("Utility REST endpoints to start and gather different metrics for different tests(load, performance, volume, etc)")
                .license("MIT License")
                .version("1.0")
                .build();
    }

}
