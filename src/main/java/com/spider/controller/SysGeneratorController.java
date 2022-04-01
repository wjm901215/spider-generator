package com.spider.controller;

import com.alibaba.fastjson.JSON;
import com.spider.utils.Query;
import com.spider.utils.R;
import com.spider.service.SysGeneratorService;
import com.spider.utils.PageUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 代码生成器
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年12月19日 下午9:12:58
 */
@Controller
@RequestMapping("/sys/generator")
public class SysGeneratorController {
    @Autowired
    private SysGeneratorService sysGeneratorService;

    /**
     * 列表
     */
    @ResponseBody
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);
        List<Map<String, Object>> list = sysGeneratorService.queryList(query);
        int total = sysGeneratorService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(list, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }

    /**
     * 生成代码
     */
    @RequestMapping("/code")
    public void code(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String[] tableNames = new String[]{};
        String tables = request.getParameter("tables");
        String mainPath = request.getParameter("mainPath");
        String pkg = request.getParameter("pkg");
        String moduleName = request.getParameter("moduleName");
        String author = request.getParameter("author");
        String email = request.getParameter("email");
        String tablePrefix = request.getParameter("tablePrefix");
        Map map = new HashMap() {{
            put("mainPath", mainPath);
            put("pkg", pkg);
            put("moduleName", moduleName);
            put("author", author);
            put("email", email);
            put("tablePrefix", tablePrefix);
        }};
        cfg.set(map);
        tableNames = JSON.parseArray(tables).toArray(tableNames);

        byte[] data = sysGeneratorService.generatorCode(tableNames);
        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\"spider.zip\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");

        IOUtils.write(data, response.getOutputStream());
    }

    public static ThreadLocal<Map> cfg = new ThreadLocal();


}
