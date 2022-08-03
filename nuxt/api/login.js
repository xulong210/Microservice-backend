import request from '@/utils/request'

export function submitLogin(userInfo){

    return request({
        url: '/eduuser/member/login',
        method: 'post',
        data: userInfo
        })
}

export function getLoginUserInfo(){

    return request({
        url: '/eduuser/member/getUserInfo',
        method: 'get',
        })
}

