package cn.xu.eduservice.service;

import cn.xu.eduservice.entity.EduCourse;
import cn.xu.eduservice.entity.frontVo.CourseDetailVo;
import cn.xu.eduservice.entity.frontVo.CourseFrontVo;
import cn.xu.eduservice.entity.vo.CourseInfoVo;
import cn.xu.eduservice.entity.vo.CoursePublishVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author xulong
 * @since 2022-02-09
 */
public interface EduCourseService extends IService<EduCourse> {

    String saveCourseInfo(CourseInfoVo courseInfoVo);

    CourseInfoVo getCourseInfo(String courseId);

    void updateCourseInfo(CourseInfoVo courseInfoVo);

    CoursePublishVo publishCourseInfo(String id);

    void removeCourse(String courseId);

    List<EduCourse> getIndexList();

    Map<String, Object> getCourseFrontList(Page<EduCourse> page, CourseFrontVo courseFrontVo);

    CourseDetailVo getBaseCourseInfo(String courseId);
}
