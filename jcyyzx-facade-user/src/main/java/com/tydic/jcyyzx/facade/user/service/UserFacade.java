package com.tydic.jcyyzx.facade.user.service;

import com.tydic.jcyyzx.facade.user.entity.User;

import java.util.List;

/**
 * @author: jhs
 * @desc:
 * @date: Create in 2018/8/16  10:57
 */
public interface UserFacade {
	User add(User user);
	
	User get(String id);
	
	List<User> list();
}
