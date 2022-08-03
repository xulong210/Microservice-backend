import request from '@/utils/request'

//分页讲师查询方法
export function getTeacherList(current,limit){

    return request({
        url: `/eduservice/teacherFront/getTeacherFrontList/${current}/${limit}`,
        method: 'post',
    })
}

export function getTeacherInfo(id){
    return request({
        url: `/eduservice/teacherFront/getTeacherFrontInfo/${id}`,
        method: 'get',
    })
}

