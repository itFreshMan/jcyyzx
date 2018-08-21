package com.tydic.jcyyzx.service.user.dao;

import com.tydic.core.plugin.mybatis.dao.BaseDao;
import com.tydic.jcyyzx.facade.user.entity.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

/**
 * @author: jhs
 * @desc:
 * @date: Create in 2018/8/16  14:18
 */
@Mapper
@Repository
public interface UserDao  extends BaseDao<User> {
	
	
	@Select("select * from tb_user where id = #{id}")
	@Results({
			@Result(column = "ID", property = "id"),
			@Result(column = "NAME", property = "name"),
			@Result(column = "GENDER", property = "gender")
	})
	User getByAnno(@Param("id") String id);
	
	User getByMapper(String id);
}
