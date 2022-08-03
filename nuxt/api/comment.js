import request from '@/utils/request'

//评论查询方法
export function getPageList(current,limit,courseId){

    return request({
        url: `/eduservice/comment/${current}/${limit}`,
        method: 'get',
        params: {courseId}
    })
}

export function addComment(comment){
    return request({
        url: `/eduservice/comment/auth/save`,
        method: 'post',
        data: comment
    })
}