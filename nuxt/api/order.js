import request from '@/utils/request'

//生成订单
export function createOrder(courseInfo){

    return request({
        url: '/eduorder/order/createOrder',
        method: 'post',
        data: courseInfo
    })
}

//获取订单状态
export function getOrderInfo(orderNo){

    return request({
        url: `/eduorder/order/getOrderInfo/${orderNo}`,
        method: 'get',
    })
}

//生成二维码
export function createNative(orderNo){

    return request({
        url: `/eduorder/payLog/createNative/${orderNo}`,
        method: 'get',
    })
}

//查询支付状态
export function queryPayStatus(orderNo){

    return request({
        url: `/eduorder/payLog/queryPayStatus/${orderNo}`,
        method: 'get',
    })
}

//查询课程是否被购买
export function isBuyCourse(courseId,memberId){

    return request({
        url: `/eduorder/order/isBuy/${courseId}/${memberId}`,
        method: 'get',
    })
}