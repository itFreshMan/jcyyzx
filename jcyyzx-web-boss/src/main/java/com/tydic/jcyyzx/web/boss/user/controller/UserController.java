package com.tydic.jcyyzx.web.boss.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.tydic.jcyyzx.facade.user.entity.User;
import com.tydic.jcyyzx.facade.user.service.UserFacade;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author: jhs
 * @desc:
 * @date: Create in 2018/8/16  19:03
 */
@RestController
@RequestMapping("/user")
public class UserController {
	@Reference(check = false)
	private UserFacade userFacade;
	
	@RequestMapping("/add")
	@ResponseBody
	public User add(User user){
		userFacade.add(user);
		return user;
	}
	
	@RequestMapping("/get/{id}")
	@ResponseBody
	public User get(@PathVariable("id") String id){
		User user = userFacade.get(id);
		return user;
	}
	
	@RequestMapping("/list")
	@ResponseBody
	public List<User> list(){
		return userFacade.list();
	}
}
