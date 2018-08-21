package com.tydic.jcyyzx.service.user.biz;

import com.tydic.core.plugin.mybatis.service.BaseServiceImpl;
import com.tydic.jcyyzx.facade.user.entity.User;
import com.tydic.jcyyzx.service.user.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: jhs
 * @desc:
 * @date: Create in 2018/8/16  14:53
 */
@Service
public class UserBiz extends BaseServiceImpl  {
	@Autowired
	private UserDao userDao;
	
	public User insertUser(User user){
		userDao.insert(user);
		return user;
	}
	
	public User getUser(String id){
		return userDao.getByAnno(id);
	}
	
	public List<User> listAll() {
		return userDao.selectAll();
	}
}
