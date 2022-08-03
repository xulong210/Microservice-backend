package cn.xu.eduservice.service;

import cn.xu.eduservice.entity.EduTeacher;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 讲师 服务类
 * </p>
 *
 * @author xulong
 * @since 2022-01-23
 */
public interface EduTeacherService extends IService<EduTeacher> {
    List<EduTeacher> getIndexList();
}
