package cn.xu.eduuser.service.impl;

import cn.xu.eduuser.entity.UcenterMember;
import cn.xu.eduuser.entity.vo.RegisterVo;
import cn.xu.eduuser.mapper.UcenterMemberMapper;
import cn.xu.eduuser.service.UcenterMemberService;
import cn.xu.jwtUtils.JwtUtils;
import cn.xu.jwtUtils.MD5;
import cn.xu.servicebase.exceptionHandler.MyException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author xulong
 * @since 2022-02-25
 */
@Service
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Override
    public String login(UcenterMember ucenterMember) {
        //获取登录手机号和密码
        String mobile = ucenterMember.getMobile();
        String password = ucenterMember.getPassword();
        if(StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)){
            throw new MyException(20001,"登陆失败");
        }

        //根据手机号查询
        QueryWrapper<UcenterMember> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mobile",mobile);
        UcenterMember user = baseMapper.selectOne(queryWrapper);
        //判断查询对象是否为空
        if(user == null){
            throw new MyException(20001,"手机号不存在");
        }

        //判断密码 (存储到数据库的密码是加密的 所以要先把password加密再对比)
        //MD5加密 不可逆解密
        if(!MD5.encrypt(password).equals(user.getPassword())){
            throw new MyException(20001,"密码错误");
        }

        //判断用户是否被禁用
        if(user.getIsDisabled()){
            throw new MyException(20001,"登陆失败");
        }

        //登陆成功 返回token
        String jwtToken = JwtUtils.getJwtToken(user.getId(), user.getMobile());

        return jwtToken;
    }

    @Override
    public void register(RegisterVo registerVo) {
        //获取数据
        String code = registerVo.getCode();
        String nickname = registerVo.getNickname();
        String mobile = registerVo.getMobile();
        String password = registerVo.getPassword();
        if(StringUtils.isEmpty(code) || StringUtils.isEmpty(nickname) || StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)){
            throw new MyException(20001,"注册失败");
        }


        //判断手机验证码 从redis中取 。。
        String redisCode = redisTemplate.opsForValue().get(mobile);
        if(!StringUtils.isEmpty(redisCode)){
            if(!code.equals(redisCode)){
                throw new MyException(20001,"注册失败,验证码错误");
            }
        }else{
            throw new MyException(20001,"注册失败,验证码过期");
        }

        //判断手机号是否已经存在
        QueryWrapper<UcenterMember> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mobile",mobile);
        Integer count = baseMapper.selectCount(queryWrapper);
        if(count > 0){
            throw new MyException(20001,"注册失败,手机号已存在");
        }

        //数据库添加数据
        UcenterMember ucenterMember = new UcenterMember();
        ucenterMember.setMobile(mobile);
        ucenterMember.setNickname(nickname);
        ucenterMember.setPassword(MD5.encrypt(password));
        ucenterMember.setIsDisabled(false);
        //设置默认值
        ucenterMember.setAvatar("http://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83eoj0hHXhgJNOTSOFsS4uZs8x1ConecaVOB8eIl115xmJZcT4oCicvia7wMEufibKtTLqiaJeanU2Lpg3w/132");
        baseMapper.insert(ucenterMember);
    }

    @Override
    public UcenterMember getOpenIdMember(String openId) {
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("openid",openId);
        UcenterMember ucenterMember = baseMapper.selectOne(wrapper);
        return ucenterMember;
    }

    @Override
    public Integer countRegister(String day) {

        return this.baseMapper.countRegister(day);
    }
}
