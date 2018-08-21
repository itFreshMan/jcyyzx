package com.tydic.jcyyzx.facade.user.service.impl;

import com.tydic.jcyyzx.facade.user.entity.User;
import com.tydic.jcyyzx.facade.user.service.UserFacade;
import com.tydic.jcyyzx.service.user.biz.UserBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author: jhs
 * @desc:
 * @date: Create in 2018/8/16  14:13
 */
@com.alibaba.dubbo.config.annotation.Service(timeout = 5000)
@Component
public class UserFacadeImpl implements UserFacade {
	@Autowired
	private UserBiz userBiz;
	
	@Override
	public User add(User user) {
		return userBiz.insertUser(user);
	}
	
	@Override
	public User get(String id) {
		return userBiz.getUser(id);
	}
	
	@Override
	public List<User> list() {
		return userBiz.listAll();
	}
}
