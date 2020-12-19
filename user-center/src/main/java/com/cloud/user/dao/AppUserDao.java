package com.cloud.user.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import com.cloud.model.user.AppUser;

@Mapper
public interface AppUserDao {

	@Options(useGeneratedKeys = true, keyProperty = "id")
	@Insert("insert into app_user(username, password, nickname, headImgUrl, phone, sex, enabled, type, createTime, updateTime) "
			+ "values(#{username}, #{password}, #{nickname}, #{headImgUrl}, #{phone}, #{sex}, #{enabled}, #{type}, #{createTime}, #{updateTime})")
	int save(AppUser appUser);

	int update(AppUser appUser);

	@Deprecated
	@Select("select * from app_user t where t.username = #{username}")
	AppUser findByUsername(String username);

	@Select("select * from app_user t where t.id = #{id}")
	AppUser findById(Long id);

	int count(Map<String, Object> params);

	List<AppUser> findData(Map<String, Object> params);

}
