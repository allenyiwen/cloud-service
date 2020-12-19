package com.cloud.file.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.cloud.file.model.FileInfo;

@Mapper
public interface FileDao {

	@Select("select * from file_info t where t.id = #{id}")
	FileInfo getById(String id);

	@Insert("insert into file_info(id, name, isImg, contentType, size, path, url, source, createTime) "
			+ "values(#{id}, #{name}, #{isImg}, #{contentType}, #{size}, #{path}, #{url}, #{source}, #{createTime})")
	int save(FileInfo fileInfo);

	@Delete("delete from file_info where id = #{id}")
	int delete(String id);

	int count(Map<String, Object> params);

	List<FileInfo> findData(Map<String, Object> params);
}
