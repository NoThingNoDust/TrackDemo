package com.example.god.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.god.model.trace.model.RunTimeNode;
import com.example.god.model.trace.model.SystemStatistic;
import com.example.god.model.trace.service.RunTimeNodeService;
import com.example.god.model.trace.util.Context;
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
@RequestMapping("/trace")
public class TraceController {

    /**
     * 此处静态资源访问
     * @param model
     * @param request
     * @return
     */
    @GetMapping
    public String index(Model model, HttpServletRequest request) {
        List<RunTimeNode> list = RunTimeNodeService.getControllers();
        model.addAttribute("list",list);
        SystemStatistic system = RunTimeNodeService.getRunStatistic();
        model.addAttribute("system",system);
        model.addAttribute("config", Context.getConfig());
        return "index-thymeleaf";
    }

    /**
     * 获取调用链信息
     * @param methodName
     * @return
     */
    @GetMapping("/getTree")
    @ResponseBody
    public JSONObject getTree(@RequestParam("methodName") String methodName) {
        RunTimeNode graph = RunTimeNodeService.getGraph(methodName);
        String s = JSON.toJSONString(graph, SerializerFeature.DisableCircularReferenceDetect);
        return JSON.parseObject(s);
    }

}
