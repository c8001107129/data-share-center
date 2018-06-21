package com.gw.controller;

import com.gw.entity.ResponseStatement;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 通用HTTP请求错误控制器
 */
@RestController
public class ErrorPageController {
    private ResponseStatement responseStatement=new ResponseStatement();

    @RequestMapping("/e/400")
    public ResponseStatement get400Error(){
        responseStatement.setMessage("400,错误请求！");
        responseStatement.setStatus("FAIL");
        return responseStatement;
    }

    @RequestMapping(value = "/e/404")
    public ResponseStatement get404Error(){
        responseStatement.setMessage("404,未找到请求地址！");
        responseStatement.setStatus("FAIL");
        return responseStatement;
    }

    @RequestMapping("/e/500")
    public ResponseStatement get500Error(){
        responseStatement.setMessage("500,服务器内部错误！");
        responseStatement.setStatus("FAIL");
        return responseStatement;
    }
}
