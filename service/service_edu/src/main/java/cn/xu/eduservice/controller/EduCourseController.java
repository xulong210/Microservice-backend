package cn.xu.eduservice.controller;


import cn.xu.commonutils.R;
import cn.xu.eduservice.entity.EduCourse;
import cn.xu.eduservice.entity.vo.CourseInfoVo;
import cn.xu.eduservice.entity.vo.CoursePublishVo;
import cn.xu.eduservice.entity.vo.CourseQuery;
import cn.xu.eduservice.service.EduCourseService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author xulong
 * @since 2022-02-09
 */
@RestController
@RequestMapping("/eduservice/course")
//@CrossOrigin
public class EduCourseController {

    @Autowired
    private EduCourseService eduCourseService;

    //添加课程基本信息方法
    @PostMapping("addCourseInfo")
    public R addCourseInfo(@RequestBody CourseInfoVo courseInfoVo){
        System.out.println(courseInfoVo);
        String pid = eduCourseService.saveCourseInfo(courseInfoVo);

        return R.ok().data("courseId",pid);
    }

    //根据课程id查询信息
    @GetMapping("getCourseInfo/{courseId}")
    public R getCourseInfo(@PathVariable("courseId") String courseId){
        CourseInfoVo courseInfoVo =  eduCourseService.getCourseInfo(courseId);
        return R.ok().data("courseInfoVo",courseInfoVo);
    }

    //更新课程信息
    @PostMapping("updateCourseInfo")
    public R updateCourseInfo(@RequestBody CourseInfoVo courseInfoVo){
        eduCourseService.updateCourseInfo(courseInfoVo);
        return R.ok();
    }

    //根据课程id查询课程确认信息
    @GetMapping("getPublishCourseInfo/{id}")
    public R getPublishCourseInfo(@PathVariable String id){
        CoursePublishVo coursePublishVo = eduCourseService.publishCourseInfo(id);
        return R.ok().data("coursePublishVo",coursePublishVo);
    }

    //最终发布课程 修改课程状态
    @PostMapping("publishCourse/{id}")
    public R publishCourse(@PathVariable String id){
        EduCourse eduCourse = new EduCourse();
        eduCourse.setId(id);
        eduCourse.setStatus("Normal");
        eduCourseService.updateById(eduCourse);
        return R.ok();
    }


    //获取课程列表
    @GetMapping("/findAll")
    public R getCourseList(){
        List<EduCourse> list = eduCourseService.list(null);
        return R.ok().data("list",list);
    }

    //带条件分页查询的课程列表
    @PostMapping("/pageQueryCourse/{current}/{limit}")
    public R pageQueryCourse(@PathVariable("current") long current,
                             @PathVariable("limit") long limit,
                             @RequestBody(required = false) CourseQuery courseQuery){

        Page<EduCourse> pageCourse = new Page<>(current,limit);

        QueryWrapper<EduCourse> queryWrapper = new QueryWrapper<>();
        String status = courseQuery.getStatus();
        String title = courseQuery.getTitle();
        String begin = courseQuery.getBegin();
        String end = courseQuery.getEnd();
        if(!StringUtils.isEmpty(status)){
            queryWrapper.eq("status",status);
        }
        if(!StringUtils.isEmpty(title)){
            queryWrapper.like("title",title);
        }
        if(!StringUtils.isEmpty(begin)){
            queryWrapper.ge("gmt_create",begin);
        }
        if(!StringUtils.isEmpty(end)){
            queryWrapper.le("gmt_end",end);
        }

        eduCourseService.page(pageCourse, queryWrapper);
        long total = pageCourse.getTotal();
        List<EduCourse> records = pageCourse.getRecords();

        return R.ok().data("total",total).data("rows",records);

    }


    //删除课程
    @DeleteMapping("deleteCourse/{courseId}")
    public R deleteCourse(@PathVariable String courseId){

        eduCourseService.removeCourse(courseId);

        return R.ok();

    }
}

