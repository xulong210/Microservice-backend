package cn.xu.eduservice.controller.front;

import cn.xu.commonutils.R;
import cn.xu.eduservice.entity.EduCourse;
import cn.xu.eduservice.entity.EduTeacher;
import cn.xu.eduservice.service.EduCourseService;
import cn.xu.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/eduservice/indexFront")
//@CrossOrigin
public class IndexFrontController {

    @Autowired
    private EduCourseService eduCourseService;

    @Autowired
    private EduTeacherService eduTeacherService;


    //查询前8条热门课程,前4条名师
    @GetMapping("index")
    public R index(){


        List<EduCourse> eduList = eduCourseService.getIndexList();
        List<EduTeacher> teacherList = eduTeacherService.getIndexList();

        return R.ok().data("eduList",eduList).data("teacherList",teacherList);
    }
}
