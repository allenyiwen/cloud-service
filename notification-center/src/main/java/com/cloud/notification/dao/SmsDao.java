package com.cloud.notification.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import com.cloud.notification.model.Sms;

@Mapper
public interface SmsDao {

	@Options(useGeneratedKeys = true, keyProperty = "id")
	@Insert("insert into t_sms(phone, signName, templateCode, params, day, createTime, updateTime) "
			+ "values(#{phone}, #{signName}, #{templateCode}, #{params}, #{day}, #{createTime}, #{updateTime})")
	int save(Sms sms);

	@Select("select * from t_sms t where t.id = #{id}")
	Sms findById(Long id);

	int update(Sms sms);

	int count(Map<String, Object> params);

	List<Sms> findData(Map<String, Object> params);
}
