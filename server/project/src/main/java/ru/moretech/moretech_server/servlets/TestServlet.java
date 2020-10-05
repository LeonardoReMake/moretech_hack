package ru.moretech.moretech_server.servlets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class TestServlet {
    private static final Logger LOG = LoggerFactory.getLogger(TestServlet.class);

    @GetMapping(value = "/test")
    public @ResponseBody String test() {
        LOG.debug("Recieved request");
        return "Hello World";
    }

    @GetMapping(value = "/test2")
    public @ResponseBody String testAnother() {
        LOG.debug("Recieved request");
        return "<h3>Another text</h3>";
    }

}
