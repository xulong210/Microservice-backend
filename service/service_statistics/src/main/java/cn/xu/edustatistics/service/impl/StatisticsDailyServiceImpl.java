package cn.xu.edustatistics.service.impl;

import cn.xu.commonutils.R;
import cn.xu.edustatistics.cloud.UcenterClient;
import cn.xu.edustatistics.entity.StatisticsDaily;
import cn.xu.edustatistics.mapper.StatisticsDailyMapper;
import cn.xu.edustatistics.service.StatisticsDailyService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author xulong
 * @since 2022-04-22
 */
@Service
public class StatisticsDailyServiceImpl extends ServiceImpl<StatisticsDailyMapper, StatisticsDaily> implements StatisticsDailyService {

    @Autowired
    private UcenterClient ucenterClient;

    @Override
    public void registerCount(String day) {
        QueryWrapper<StatisticsDaily> dayQueryWrapper = new QueryWrapper<>();
        dayQueryWrapper.eq("date_calculated", day);
        baseMapper.delete(dayQueryWrapper);

        R result = ucenterClient.countRegister(day);
        Integer registerNum = (Integer) result.getData().get("countRegister");
        Integer loginNum = RandomUtils.nextInt(100, 200);//TODO
        Integer videoViewNum = RandomUtils.nextInt(100, 200);//TODO
        Integer courseNum = RandomUtils.nextInt(100, 200);//TODO

        StatisticsDaily daily = new StatisticsDaily();
        daily.setRegisterNum(registerNum);
        daily.setLoginNum(loginNum);
        daily.setVideoViewNum(videoViewNum);
        daily.setCourseNum(courseNum);
        daily.setDateCalculated(day);

        baseMapper.insert(daily);

    }

    @Override
    public Map<String, Object> getShowData(String type, String begin, String end) {
        QueryWrapper<StatisticsDaily> queryWrapper = new QueryWrapper<>();
        queryWrapper.between("date_calculated",begin,end);
        queryWrapper.select("date_calculated",type);
        List<StatisticsDaily> list = baseMapper.selectList(queryWrapper);

        List<String> dateList = new ArrayList<>();
        List<Integer> numData = new ArrayList<>();

        for(int i = 0;i<list.size();i++){
            StatisticsDaily daily = list.get(i);
            dateList.add(daily.getDateCalculated());
            switch (type){
                case "login_num":
                    numData.add(daily.getLoginNum());
                    break;
                case "register_num":
                    numData.add(daily.getRegisterNum());
                    break;
                case "video_view_num":
                    numData.add(daily.getVideoViewNum());
                    break;
                case "course_num":
                    numData.add(daily.getCourseNum());
                    break;
                default:
                    break;
            }

        }
        Map<String,Object> map = new HashMap<>();
        map.put("dateList",dateList);
        map.put("numData",numData);

        return map;

    }
}
