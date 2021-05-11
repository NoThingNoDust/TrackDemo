package com.example.god.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.god.model.process.tree.model.RunTimeNode;
import com.example.god.model.trace.handler.RunTimeHandler;
import com.example.god.model.trace.model.SystemStatistic;
import com.example.god.model.trace.util.Context;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 调用链追踪入口
 * @author liule
 */
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
        Map<String, Map<String, RunTimeNode>> traceMap = RunTimeHandler.traceMap;
        Set<String> strings = traceMap.keySet();

        List<RunTimeNode> list = new ArrayList<>();
        for (String string : strings) {
            Map<String, RunTimeNode> map = traceMap.get(string);
            RunTimeNode runTimeNode = map.get(string);
            list.add(runTimeNode);
        }

        //获取所有调用链
        model.addAttribute("list", list);
        SystemStatistic systemStatistic = new SystemStatistic();
        if (!CollectionUtils.isEmpty(list)) {
            int delayNum = (int) list.stream().filter(controllerApi -> controllerApi.getExecuteTime().getAvgRunTime() >= Context.getConfig().getTimeThreshold()).count();
            systemStatistic.setDelayNum(delayNum);
            int normalNum = (int) list.stream().filter(controllerApi -> controllerApi.getExecuteTime().getAvgRunTime() < Context.getConfig().getTimeThreshold()).count();
            systemStatistic.setNormalNum(normalNum);
            int totalNum = list.size();
            systemStatistic.setTotalNum(totalNum);
            Double max = list.stream().map(api -> api.getExecuteTime().getAvgRunTime()).max(Double::compareTo).get();
            Double min = list.stream().map(api -> api.getExecuteTime().getAvgRunTime()).min(Double::compareTo).get();
            Double avg = list.stream().map(api -> api.getExecuteTime().getAvgRunTime()).collect(Collectors.averagingDouble(Double::doubleValue));
            systemStatistic.setMaxRunTime(max);
            systemStatistic.setMinRunTime(min);
            systemStatistic.setAvgRunTime(avg);

        }

        model.addAttribute("system",systemStatistic);
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
        Map<String, RunTimeNode> map = RunTimeHandler.traceMap.get(methodName);
        RunTimeNode runTimeNode = map.get(methodName);
        String s = JSON.toJSONString(runTimeNode, SerializerFeature.DisableCircularReferenceDetect);
        return JSON.parseObject(s);
    }

}
