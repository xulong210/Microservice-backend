import request from '@/utils/request'

//分页讲师查询方法
export function getCourseList(current,limit,searchObj){

    return request({
        url: `/eduservice/courseFront/getFrontCourseList/${current}/${limit}`,
        method: 'post',
        data: searchObj
    })
}

//查询所有分类
export function getAllSubject(){
    return request({
        url: `/eduservice/subject/getAllSubject`,
        method: 'get',
    })
}

export function getCourseInfo(id){
    return request({
        url: `/eduservice/courseFront/getFrontCourseInfo/${id}`,
        method: 'get',
    })
}