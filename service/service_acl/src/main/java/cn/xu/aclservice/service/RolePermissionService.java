package cn.xu.aclservice.service;


import cn.xu.aclservice.entity.RolePermission;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 角色权限 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-01-12
 */
public interface RolePermissionService extends IService<RolePermission> {
    List<String> selectUsernameByRoleId(String role);
}
