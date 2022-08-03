package cn.xu.eduservice.mapper;

import cn.xu.eduservice.entity.EduTeacher;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 讲师 Mapper 接口
 * </p>
 *
 * @author xulong
 * @since 2022-01-23
 */
public interface EduTeacherMapper extends BaseMapper<EduTeacher> {
    List<EduTeacher> getIndexList();
}
