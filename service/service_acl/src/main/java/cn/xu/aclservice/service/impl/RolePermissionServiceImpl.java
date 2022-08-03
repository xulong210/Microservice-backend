package cn.xu.aclservice.service.impl;

import cn.xu.aclservice.entity.RolePermission;
import cn.xu.aclservice.mapper.RolePermissionMapper;

import cn.xu.aclservice.service.RolePermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 角色权限 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-01-12
 */
@Service
public class RolePermissionServiceImpl extends ServiceImpl<RolePermissionMapper, RolePermission> implements RolePermissionService {

    @Override
    public List<String> selectUsernameByRoleId(String role) {
        List<String> usernameList = baseMapper.selectUsernameByRoleId(role);
        return usernameList;
    }
}
