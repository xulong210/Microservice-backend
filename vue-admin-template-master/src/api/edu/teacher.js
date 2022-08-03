import request from '@/utils/request'

export function getTeacherList(current,limit,teacherQuery){

    return request({
        url: `/eduservice/teacher/pageTeacherCondition/${current}/${limit}`,
        method: 'post',
        //teacherQuery条件对象 后端使用requestBody获取数据
        //data表示把对象转换json进行传递到接口里面
        data: teacherQuery
        })
}

export function deleteTeacherById(id){

    return request({
        url: `/eduservice/teacher/delete/${id}`,
        method: 'delete',
        })
}

export function saveTeacher(teacher){

    return request({
        url: `/eduservice/teacher/addTeacher`,
        method: 'post',
        data: teacher
    })
}

export function getTeacherInfo(id){

    return request({
        url: `/eduservice/teacher/getTeacher/${id}`,
        method: 'get',
    })
}

export function updateTeacher(teacher){

    return request({
        url: `/eduservice/teacher/updateTeacher`,
        method: 'post',
        data: teacher
    })
}
