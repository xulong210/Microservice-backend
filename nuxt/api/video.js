import request from '@/utils/request'

//分页讲师查询方法
export function getPlayAuth(id){
    return request({
        url: `/eduvideo/video/getPlayAuth/${id}`,
        method: 'get',
    })
}