package cn.xu.eduservice.service.impl;

import cn.xu.eduservice.entity.EduCourse;
import cn.xu.eduservice.entity.EduCourseDescription;
import cn.xu.eduservice.entity.EduVideo;
import cn.xu.eduservice.entity.frontVo.CourseDetailVo;
import cn.xu.eduservice.entity.frontVo.CourseFrontVo;
import cn.xu.eduservice.entity.vo.CourseInfoVo;
import cn.xu.eduservice.entity.vo.CoursePublishVo;
import cn.xu.eduservice.mapper.EduCourseMapper;
import cn.xu.eduservice.service.EduChapterService;
import cn.xu.eduservice.service.EduCourseDescriptionService;
import cn.xu.eduservice.service.EduCourseService;
import cn.xu.eduservice.service.EduVideoService;
import cn.xu.servicebase.exceptionHandler.MyException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author xulong
 * @since 2022-02-09
 */
@Service
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {

    @Autowired
    private EduCourseDescriptionService descriptionService;

    @Autowired
    private EduChapterService eduChapterService;

    @Autowired
    private EduVideoService eduVideoService;

    //添加课程信息 （需要加入course表和description表）
    @Override
    public String saveCourseInfo(CourseInfoVo courseInfoVo) {
        //课程表添加信息
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo,eduCourse);
        int insert = baseMapper.insert(eduCourse);

        if(insert<=0){
            throw new MyException(20001,"添加课程信息出错了");
        }

        //获取课程id
        String pid = eduCourse.getId();

        //课程简介表添加信息
        EduCourseDescription eduCourseDescription = new EduCourseDescription();
        eduCourseDescription.setDescription(courseInfoVo.getDescription());
        eduCourseDescription.setId(pid);
        descriptionService.save(eduCourseDescription);

        return pid;
    }

    @Override
    public CourseInfoVo getCourseInfo(String courseId) {
        //1.查询课程表
        EduCourse eduCourse = this.baseMapper.selectById(courseId);
        //2.查询课程简介表
        EduCourseDescription description = descriptionService.getById(courseId);
        CourseInfoVo courseInfoVo = new CourseInfoVo();
        BeanUtils.copyProperties(eduCourse,courseInfoVo);
        courseInfoVo.setDescription(description.getDescription());
        return courseInfoVo;
    }

    @Override
    public void updateCourseInfo(CourseInfoVo courseInfoVo) {
        //1.修改课程表
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo,eduCourse);
        int flag = this.baseMapper.updateById(eduCourse);

        if(flag == 0){
            //修改失败
            throw new MyException(20001,"修改课程信息失败");
        }
        //2.修改简介表
        EduCourseDescription description = new EduCourseDescription();
        description.setId(courseInfoVo.getId());
        description.setDescription(courseInfoVo.getDescription());
        descriptionService.updateById(description);
        
    }

    @Override
    public CoursePublishVo publishCourseInfo(String id) {
        //调用mapper
        CoursePublishVo publishInfo = baseMapper.getPublishInfo(id);
        return publishInfo;
    }

    //删除课程
    @Override
    public void removeCourse(String courseId) {
        //1.根据课程id删除小节
        eduVideoService.removeByCourseId(courseId);

        //2.根据课程id删除章节
        eduChapterService.removeByCourseId(courseId);

        //3.根据课程id删除描述
        descriptionService.removeById(courseId);

        //4.根据课程id删除课程
        int i = this.baseMapper.deleteById(courseId);
        if(i==0){
            throw new MyException(20001,"删除失败");
        }

    }

    @Cacheable(value = "index",key = "'course'")
    @Override
    public List<EduCourse> getIndexList() {
        QueryWrapper<EduCourse> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("view_count");
        queryWrapper.last("limit 8");
        List<EduCourse> eduList = this.baseMapper.selectList(queryWrapper);
        return eduList;
    }

    @Override
    public Map<String, Object> getCourseFrontList(Page<EduCourse> page, CourseFrontVo courseQuery) {
        QueryWrapper<EduCourse> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(courseQuery.getSubjectParentId())) {
            queryWrapper.eq("subject_parent_id", courseQuery.getSubjectParentId());
        }

        if (!StringUtils.isEmpty(courseQuery.getSubjectId())) {
            queryWrapper.eq("subject_id", courseQuery.getSubjectId());
        }

        if (!StringUtils.isEmpty(courseQuery.getBuyCountSort())) {
            queryWrapper.orderByDesc("buy_count");
        }

        if (!StringUtils.isEmpty(courseQuery.getGmtCreateSort())) {
            queryWrapper.orderByDesc("gmt_create");
        }

        if (!StringUtils.isEmpty(courseQuery.getPriceSort())) {
            queryWrapper.orderByDesc("price");
        }

        this.baseMapper.selectPage(page,queryWrapper);
        List<EduCourse> records = page.getRecords();
        long current = page.getCurrent();
        long pages = page.getPages();
        long size = page.getSize();
        long total = page.getTotal();
        boolean hasNext = page.hasNext();
        boolean hasPrevious = page.hasPrevious();

        Map<String, Object> map = new HashMap<>();
        map.put("items", records);
        map.put("current", current);
        map.put("pages", pages);
        map.put("size", size);
        map.put("total", total);
        map.put("hasNext", hasNext);
        map.put("hasPrevious", hasPrevious);


        return map;
    }

    @Override
    public CourseDetailVo getBaseCourseInfo(String courseId) {

        return this.baseMapper.getBaseCourseInfo(courseId);
    }
}
