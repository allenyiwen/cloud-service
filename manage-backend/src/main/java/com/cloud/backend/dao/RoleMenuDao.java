package com.cloud.backend.dao;

import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.cloud.backend.model.Menu;

/**
 * 角色菜单关系<br>
 * 角色和菜单是多对多关系，role_menu是中间表
 *
 * @author allen
 */
@Mapper
public interface RoleMenuDao {

	@Insert("insert into role_menu(roleId, menuId) values(#{roleId}, #{menuId})")
	int save(@Param("roleId") Long roleId, @Param("menuId") Long menuId);

	int delete(@Param("roleId") Long roleId, @Param("menuId") Long menuId);

	@Select("select t.menuId from role_menu t where t.roleId = #{roleId}")
	Set<Long> findMenuIdsByRoleId(Long roleId);

	List<Menu> findMenusByRoleIds(@Param("roleIds") Set<Long> roleIds);
}
