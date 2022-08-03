package cn.xu.eduservice.controller.front;

import cn.xu.commonutils.R;
import cn.xu.eduservice.entity.EduCourse;
import cn.xu.eduservice.entity.EduTeacher;
import cn.xu.eduservice.service.EduCourseService;
import cn.xu.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/eduservice/teacherFront")
//@CrossOrigin
public class TeacherFrontController {
    @Autowired
    private EduTeacherService eduTeacherService;

    @Autowired
    private EduCourseService eduCourseService;

    @PostMapping("getTeacherFrontList/{current}/{limit}")
    public R pageListTeacher(@PathVariable("current") long current,
                             @PathVariable("limit") long limit){
        Page<EduTeacher> pageTeacher =  new Page<>(current,limit);
        //调用方法实现分页，把分页所有数据封装到pageTeacher中
        eduTeacherService.page(pageTeacher,null);

        long total = pageTeacher.getTotal();//总记录数
        List<EduTeacher> records = pageTeacher.getRecords(); //数据list集合
        boolean next = pageTeacher.hasNext();
        boolean pre = pageTeacher.hasPrevious();

        HashMap<String, Object> map = new HashMap<>();
        map.put("total",total);
        map.put("rows",records);
        map.put("hasNext",next);
        map.put("hasPre",pre);
        map.put("size",limit);
        map.put("current",current);
        map.put("pages",pageTeacher.getPages());

        return R.ok().data(map);
    }

    @GetMapping("getTeacherFrontInfo/{teacherId}")
    public R getTeacherFrontInfo(@PathVariable String teacherId){
        //查询讲师基本信息
        EduTeacher eduTeacher = eduTeacherService.getById(teacherId);

        //查询讲师所有课程信息
        QueryWrapper<EduCourse> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("teacher_id",teacherId);
        List<EduCourse> eduList = eduCourseService.list(queryWrapper);

        return R.ok().data("teacher",eduTeacher).data("courseList",eduList);
    }


}
