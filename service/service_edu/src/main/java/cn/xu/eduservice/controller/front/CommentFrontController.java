package cn.xu.eduservice.controller.front;

import cn.xu.commonutils.R;
import cn.xu.commonutils.vo.UMember;
import cn.xu.eduservice.cloudClient.MemberClient;
import cn.xu.eduservice.entity.EduComment;
import cn.xu.eduservice.service.EduCommentService;
import cn.xu.jwtUtils.JwtUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/eduservice/comment")
//@CrossOrigin
public class CommentFrontController {

    @Autowired
    private EduCommentService eduCommentService;

    @Autowired
    private MemberClient memberClient;

    @GetMapping("{current}/{limit}")
    public R index(@PathVariable long current,@PathVariable long limit,String courseId){
        Page<EduComment> page = new Page<>(current,limit);

        QueryWrapper<EduComment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id",courseId);
        eduCommentService.page(page,queryWrapper);
        List<EduComment> records = page.getRecords();

        Map<String,Object> map = new HashMap<>();
        map.put("items",records);
        map.put("current",current);
        map.put("pages", page.getPages());
        map.put("size", page.getSize());
        map.put("total", page.getTotal());
        map.put("hasNext", page.hasNext());
        map.put("hasPrevious", page.hasPrevious());
        return R.ok().data(map);

    }

    @PostMapping("auth/save")
    public R save(@RequestBody EduComment comment, HttpServletRequest request){
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        if(StringUtils.isEmpty(memberId)){
            return R.error().code(28004).message("请登录");
        }
        comment.setMemberId(memberId);
        UMember userInfo = memberClient.getInfo(memberId);
        comment.setAvatar(userInfo.getAvatar());
        comment.setNickname(userInfo.getNickname());

        eduCommentService.save(comment);
        return R.ok();
    }
}
