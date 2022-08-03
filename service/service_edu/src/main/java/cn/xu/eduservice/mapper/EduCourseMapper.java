package cn.xu.eduservice.mapper;

import cn.xu.eduservice.entity.EduCourse;
import cn.xu.eduservice.entity.frontVo.CourseDetailVo;
import cn.xu.eduservice.entity.vo.CoursePublishVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author xulong
 * @since 2022-02-09
 */
public interface EduCourseMapper extends BaseMapper<EduCourse> {
    CoursePublishVo getPublishInfo(String courseId);

    CourseDetailVo getBaseCourseInfo(String courseId);
}
