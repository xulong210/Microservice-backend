package cn.xu.eduservice.service.impl;

import cn.xu.eduservice.entity.EduTeacher;
import cn.xu.eduservice.mapper.EduTeacherMapper;
import cn.xu.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 讲师 服务实现类
 * </p>
 *
 * @author xulong
 * @since 2022-01-23
 */
@Service
public class EduTeacherServiceImpl extends ServiceImpl<EduTeacherMapper, EduTeacher> implements EduTeacherService {

    @Cacheable(value = "index",key = "'teacher'")
    @Override
    public List<EduTeacher> getIndexList() {
        QueryWrapper<EduTeacher> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.orderByDesc("id");
        queryWrapper1.last("limit 4");
        List<EduTeacher> teacherList = this.baseMapper.selectList(queryWrapper1);
        return teacherList;
    }
}
