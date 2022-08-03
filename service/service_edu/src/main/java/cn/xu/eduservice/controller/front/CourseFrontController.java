package cn.xu.eduservice.controller.front;

import cn.xu.commonutils.R;
import cn.xu.eduservice.entity.EduCourse;
import cn.xu.eduservice.entity.chapter.ChapterVo;
import cn.xu.eduservice.entity.frontVo.CourseDetailVo;
import cn.xu.eduservice.entity.frontVo.CourseFrontVo;
import cn.xu.eduservice.entity.vo.CourseQuery;
import cn.xu.eduservice.service.EduChapterService;
import cn.xu.eduservice.service.EduCourseService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/eduservice/courseFront")
//@CrossOrigin
public class CourseFrontController {

    @Autowired
    private EduCourseService courseService;

    @Autowired
    private EduChapterService chapterService;

    @PostMapping("getFrontCourseList/{current}/{limit}")
    public R getFrontCourseList(@PathVariable long current, @PathVariable long limit,
                                @RequestBody(required = false)CourseFrontVo courseFrontVo){
        Page<EduCourse> page = new Page<>(current,limit);
        Map<String,Object> map = courseService.getCourseFrontList(page,courseFrontVo);

        return R.ok().data(map);

    }

    //获取课程的详细信息
    @GetMapping("getFrontCourseInfo/{courseId}")
    public R getFrontCourseInfo(@PathVariable String courseId){
        //根据课程id 编写sql 连接查询获取课程基础信息
        CourseDetailVo courseDetailVo = courseService.getBaseCourseInfo(courseId);

        //根据课程id 获取章节小节
        List<ChapterVo> chapterVideoList = chapterService.getChapterVideoByCourseId(courseId);

        return R.ok().data("courseDetailVo",courseDetailVo).data("chapterVideoList",chapterVideoList);
    }

}
