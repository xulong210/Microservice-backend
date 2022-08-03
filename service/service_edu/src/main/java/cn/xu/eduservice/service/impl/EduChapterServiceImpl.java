package cn.xu.eduservice.service.impl;

import cn.xu.eduservice.entity.EduChapter;
import cn.xu.eduservice.entity.EduVideo;
import cn.xu.eduservice.entity.chapter.ChapterVo;
import cn.xu.eduservice.entity.chapter.VideoVo;
import cn.xu.eduservice.mapper.EduChapterMapper;
import cn.xu.eduservice.service.EduChapterService;
import cn.xu.eduservice.service.EduVideoService;
import cn.xu.servicebase.exceptionHandler.MyException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {

    @Autowired
    private EduVideoService eduVideoService;

    @Override
    public List<ChapterVo> getChapterVideoByCourseId(String courseId) {
        //获取该课程的所有章节
        QueryWrapper<EduChapter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id", courseId);
        List<EduChapter> chapters = this.baseMapper.selectList(queryWrapper);

        //获取每个章节的所有小节
        QueryWrapper<EduVideo> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("course_id", courseId);
        List<EduVideo> videos = this.eduVideoService.list(queryWrapper1);

        //返回的结果
        List<ChapterVo> list = new ArrayList<>();

        //封装小节到map中去
        Map<String, List<VideoVo>> map = this.createMap(videos);

        //封装章节
        for (EduChapter eduChapter : chapters) {
            ChapterVo chapterVo = new ChapterVo();
            BeanUtils.copyProperties(eduChapter, chapterVo);

            //添加二级分类
            List<VideoVo> videoVos = map.get(eduChapter.getId());
            chapterVo.setChildren(videoVos);
            list.add(chapterVo);
        }

        return list;
    }

    private Map<String,List<VideoVo>> createMap(List<EduVideo> videos){
        Map<String,List<VideoVo>> map = new HashMap<>();
        for(EduVideo eduVideo:videos){
            VideoVo videoVo = new VideoVo();
            BeanUtils.copyProperties(eduVideo,videoVo);
            if(map.get(eduVideo.getChapterId()) != null){
                List<VideoVo> ls = map.get(eduVideo.getChapterId());
                ls.add(videoVo);
            }else{
                List<VideoVo> list = new ArrayList<>();
                list.add(videoVo);
                map.put(eduVideo.getChapterId(),list);
            }
        }
        return map;
    }

    //删除章节方法
    @Override
    public boolean deleteChapter(String chapterId) {
        //根据chapter查询 video小节 如果有 章节不允许删除
        QueryWrapper<EduVideo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("chapter_id",chapterId);
        int count = eduVideoService.count(queryWrapper);

        if(count>0){
            throw new MyException(20001,"不能删除");
        }

        int result = this.baseMapper.deleteById(chapterId);

        return result>0;

    }

    @Override
    public void removeByCourseId(String courseId) {
        QueryWrapper<EduChapter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id",courseId);
        this.baseMapper.delete(queryWrapper);
    }
}
