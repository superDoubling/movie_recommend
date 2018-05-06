package com.yh.movie.recommend.controller;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@RequestMapping(value = "/save", method = RequestMethod.GET)
	public ModelAndView save(String name, String password) {
		System.out.println("接收到的用户信息：" + name);

		ModelAndView mov = new ModelAndView();
		mov.setViewName("saveUserSuccess");
		mov.addObject("msg", "保存成功了");

		return mov;
	}
	
	@RequestMapping("/test")
	public String test(){
		return "test";
	}
}