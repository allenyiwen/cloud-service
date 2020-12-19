package com.cloud.backend.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.cloud.backend.model.Menu;

@Mapper
public interface MenuDao {

	@Insert("insert into menu(parentId, name, url, css, sort, createTime, updateTime) "
			+ "values (#{parentId}, #{name}, #{url}, #{css}, #{sort}, #{createTime}, #{updateTime})")
	int save(Menu menu);

	int update(Menu menu);

	@Select("select * from menu t where t.id = #{id}")
	Menu findById(Long id);

	@Delete("delete from menu where id = #{id}")
	int delete(Long id);

	@Delete("delete from menu where parentId = #{id}")
	int deleteByParentId(Long id);

	@Select("select * from menu t order by t.sort")
	List<Menu> findAll();
}
