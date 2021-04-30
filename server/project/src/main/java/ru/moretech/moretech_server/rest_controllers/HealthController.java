package ru.moretech.moretech_server.rest_controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest")
public class HealthController {
    private static final Logger LOG = LoggerFactory.getLogger(HealthController.class);

    @GetMapping(value = "/health")
    public @ResponseBody String health() {
        return "ok";
    }

}
