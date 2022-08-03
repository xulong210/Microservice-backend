import request from '@/utils/request'

export function getIndexData(){

    return request({
        url: '/eduservice/indexFront/index',
        method: 'get',
        })
}