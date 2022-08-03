package cn.xu.eduservice.service.impl;

import cn.xu.eduservice.cloudClient.VideoClient;
import cn.xu.eduservice.entity.EduVideo;
import cn.xu.eduservice.mapper.EduVideoMapper;
import cn.xu.eduservice.service.EduVideoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author xulong
 * @since 2022-02-09
 */
@Service
public class EduVideoServiceImpl extends ServiceImpl<EduVideoMapper, EduVideo> implements EduVideoService {

    @Autowired
    private VideoClient videoClient;

    //批量删除视频
    @Override
    public void removeByCourseId(String courseId) {
        QueryWrapper<EduVideo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id",courseId);
        queryWrapper.select("video_source_id");
        List<EduVideo> eduVideoList = this.baseMapper.selectList(queryWrapper);
        List<String> videoIds = new ArrayList<>();
        for(EduVideo eduVideo:eduVideoList){
            String videoSourceId = eduVideo.getVideoSourceId();
            if(!StringUtils.isEmpty(videoSourceId)) {
                videoIds.add(videoSourceId);
            }
        }
        if(!videoIds.isEmpty()) {
            videoClient.deleteBatch(videoIds);
        }

        QueryWrapper<EduVideo> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("course_id",courseId);
        this.baseMapper.delete(queryWrapper1);
    }
}
