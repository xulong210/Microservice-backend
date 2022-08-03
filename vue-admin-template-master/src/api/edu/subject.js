import request from '@/utils/request'

export function getSubjectList(current,limit,teacherQuery){

    return request({
        url: '/eduservice/subject/getAllSubject',
        method: 'get',
        })
}