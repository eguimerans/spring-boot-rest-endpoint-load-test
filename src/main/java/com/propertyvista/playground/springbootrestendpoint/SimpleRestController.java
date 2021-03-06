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
 * Created on Oct. 15, 2020
 * @author ernestog
 */
package com.propertyvista.playground.springbootrestendpoint;

import java.io.IOException;
import java.time.LocalDateTime;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;

@RestController
public class SimpleRestController {

    static Logger log = LoggerFactory.getLogger(SimpleRestController.class);

    private static final String template = "echo, %s!";

    @RequestMapping("/")
    public void swaggerRedirect(HttpServletResponse response) throws IOException {
        response.sendRedirect("/swagger-ui.html?docExpansion=list");
    }

    @GetMapping("/status")
    @ApiResponse(code = 200, message = "this is message")
    @ApiOperation(code = 200, value = "overall status", notes = "overall status")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> status() {
        return ResponseEntity.ok(String.format("%s - Status OK", LocalDateTime.now()));
    }

    @GetMapping("/echo")
    @ApiOperation(code = 200, value = "single echo", notes = "single echo")
    public String echo(@RequestParam(value = "value", defaultValue = "empty echo <param 'value' is available>") String value) {
        log.info("echo for {}", value);
        return String.format(template, value);
    }

    @GetMapping("/timeConsumingOperation")
    @ApiOperation(code = 200, value = "simulate time consuming operation", notes = "simulate time consuming by sleeping thread for amount of time (default 100ms)")
    public ResponseEntity<String> timeConsuming(@RequestParam(value = "value", defaultValue = "100") Integer value) {
        StopWatch watch = new StopWatch();
        watch.start();
        try {
            Thread.sleep(value);
        } catch (InterruptedException e) {
            log.error("Error ", e);
        }
        watch.stop();

        double seconds = watch.getTotalTimeSeconds();
        String time = "";
        if ((seconds * 1000) > 1) {
            time = String.format("%s seconds", seconds);
        } else {
            double nanos = watch.getTotalTimeNanos();
            if ((nanos * 1000) > 1) {
                time = String.format("%s nanos", watch.getTotalTimeNanos());
            }
        }

        String message = String.format("consuming operation done in %s", time);
        log.info(message);
        return ResponseEntity.ok(message);
    }

    @GetMapping(path = "/primes", name = "this is the name")
    @ApiOperation(code = 200, value = "calculate number of primes", notes = "calculate number of prime numbers less than supplied value; if no value supplied, value is 100")
    public ResponseEntity<String> calculatePrimes(@RequestParam(value = "value", defaultValue = "100") Integer value) {
        StopWatch watch = new StopWatch();
        watch.start();
        int primesFound = 0;
        for (int i = 0; i <= value; i++) {
            if (isPrime(i)) {
                primesFound++;
            }
        }
        watch.stop();

        double seconds = watch.getTotalTimeSeconds();
        String time = "";
        if ((seconds * 1000) > 1) {
            time = String.format("%s seconds", seconds);
        } else {
            double nanos = watch.getTotalTimeNanos();
            if ((nanos * 1000) > 1) {
                time = String.format("%s nanos", watch.getTotalTimeNanos());
            }
        }

        String message = String.format("%s prime numbers found from ZERO to %s in %s", primesFound, value, time);
        log.info("{} prime numbers less than {} found in {}", primesFound, value, time);
        return ResponseEntity.ok(message);
    }

    private boolean isPrime(int number) {
        if ((number == 0) || (number == 1)) {
            return false;
        } else {
            for (int i = 2; i < number; i++) {
                if ((number % i) != 0) {
                    continue;
                }
                return false;
            }
        }
        return true;
    }

}