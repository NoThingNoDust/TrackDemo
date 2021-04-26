package com.example.track.track.controller;

import com.example.track.track.model.RunTimeNode;
import com.example.track.track.model.SystemStatistic;
import com.example.track.track.service.RunTimeNodeService;
import com.example.track.track.util.Context;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/koTime")
public class KoTimeController {
    @Value("${koTime.ui.template:freemarker}")
    private String showTemplate;

    @GetMapping
    public String index(Model model, HttpServletRequest request) {
        List<RunTimeNode> list = RunTimeNodeService.getControllers();
        model.addAttribute("list",list);
        SystemStatistic system = RunTimeNodeService.getRunStatistic();
        model.addAttribute("system",system);
        model.addAttribute("config", Context.getConfig());
        String template = "index-freemarker";
        if ("thymeleaf".equals(showTemplate)) {
            template = "index-thymeleaf";
        }
        return template   ;
    }

    @GetMapping("/getTree")
    @ResponseBody
    public RunTimeNode getTree(@RequestParam("methodName") String methodName) {
        return RunTimeNodeService.getGraph(methodName);
    }

}
