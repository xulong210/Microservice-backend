package cn.xu.eduservice.service;

import cn.xu.eduservice.entity.EduVideo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程视频 服务类
 * </p>
 *
 * @author xulong
 * @since 2022-02-09
 */
public interface EduVideoService extends IService<EduVideo> {

    void removeByCourseId(String courseId);
}
