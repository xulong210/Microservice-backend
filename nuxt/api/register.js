import request from '@/utils/request'

//根据手机号发送验证码
export function sendCode(phone){
    return request({
        url: '/edumsm/msm/send/'+phone,
        method: 'get',
    })
}

//注册方法
export function registerUser(formItem){
    return request({
        url: '/eduuser/member/register',
        method: 'post',
        data: formItem
    })
}