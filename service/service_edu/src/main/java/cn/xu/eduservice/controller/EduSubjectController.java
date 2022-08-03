package cn.xu.eduservice.controller;


import cn.xu.commonutils.R;
import cn.xu.eduservice.entity.subject.OneSubject;
import cn.xu.eduservice.service.EduSubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author xulong
 * @since 2022-02-08
 */
@RestController
@RequestMapping("/eduservice/subject")
//@CrossOrigin
public class EduSubjectController {

    @Autowired
    private EduSubjectService eduSubjectService;


    /**
     * 获取上传过来的文件，把文件内容读取
     */
    @PostMapping("addSubject")
    public R addSubject(MultipartFile file){

        eduSubjectService.saveSubject(file,eduSubjectService);

        return R.ok();
    }


    @GetMapping("getAllSubject")
    public R getAllSubject(){
        List<OneSubject> list = eduSubjectService.getAllSubject();
        return R.ok().data("list",list);
    }
}



