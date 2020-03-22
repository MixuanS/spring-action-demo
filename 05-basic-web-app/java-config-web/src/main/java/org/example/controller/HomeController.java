package org.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * @author hc
 * @version 1.0.0
 * @date 2020/3/21
 */
@Controller
public class HomeController {

    @RequestMapping(value="/", method = GET)
    public String home() {
        return "home";
    }

}