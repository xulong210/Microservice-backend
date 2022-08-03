package cn.xu.eduservice.service.impl;

import cn.xu.eduservice.entity.EduSubject;
import cn.xu.eduservice.entity.excel.SubjectData;
import cn.xu.eduservice.entity.subject.OneSubject;
import cn.xu.eduservice.entity.subject.TwoSubject;
import cn.xu.eduservice.listener.SubjectExcelListener;
import cn.xu.eduservice.mapper.EduSubjectMapper;
import cn.xu.eduservice.service.EduSubjectService;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author xulong
 * @since 2022-02-08
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {

    //添加课程分类
    @Override
    public void saveSubject(MultipartFile file,EduSubjectService eduSubjectService) {
        try {
            //文件输入流
            InputStream inputStream = file.getInputStream();
            EasyExcel.read(inputStream, SubjectData.class,new SubjectExcelListener(eduSubjectService)).sheet().doRead();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //课程分类列表（树形）
    @Override
    public List<OneSubject> getAllSubject() {
        //1.查询所有的一级分类
        QueryWrapper<EduSubject> wrapperOne = new QueryWrapper<>();
        wrapperOne.eq("parent_id","0");
        List<EduSubject> oneSubjectList =  baseMapper.selectList(wrapperOne);
        //2.所有的二级分类
        QueryWrapper<EduSubject> wrapperTwo = new QueryWrapper<>();
        wrapperTwo.ne("parent_id","0");
        List<EduSubject> twoSubjectList = baseMapper.selectList(wrapperTwo);

        //返回的list集合
        List<OneSubject> list = new ArrayList<>();

        //将二级分类封装进map
        Map<String, List<TwoSubject>> map = this.createMap(twoSubjectList);

        //3.封装一级分类
        for(int i = 0;i<oneSubjectList.size();i++){
            EduSubject eduSubject = oneSubjectList.get(i);
            OneSubject oneSubject = new OneSubject();
            BeanUtils.copyProperties(eduSubject,oneSubject);

            //添加二级分类  children
            List<TwoSubject> twoSubjects = map.get(eduSubject.getId());
            oneSubject.setChildren(twoSubjects);
            list.add(oneSubject);
        }
        //4.封装二级分类
        return list;
    }

    private Map<String,List<TwoSubject>> createMap(List<EduSubject> list){
        Map<String, List<TwoSubject>> map = new HashMap<>();
        for(EduSubject eduSubject:list){
            TwoSubject twoSubject = new TwoSubject();
            BeanUtils.copyProperties(eduSubject,twoSubject);
            if(map.get(eduSubject.getParentId()) != null){
                List<TwoSubject> ls = map.get(eduSubject.getParentId());
                ls.add(twoSubject);
            }else{
                List<TwoSubject> ls = new ArrayList<>();
                ls.add(twoSubject);
                map.put(eduSubject.getParentId(),ls);
            }
        }
        return map;
    }
}
