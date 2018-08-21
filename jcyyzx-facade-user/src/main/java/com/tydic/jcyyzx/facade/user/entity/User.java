package com.tydic.jcyyzx.facade.user.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author: jhs
 * @desc:
 * @date: Create in 2018/8/16  10:57
 */
@Entity
@Table(name = "TB_USER")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
	@Id
	@KeySql(sql = "select sys_guid() from dual")
	private String id;
	
	private String name;
	
	private String gender;
}
