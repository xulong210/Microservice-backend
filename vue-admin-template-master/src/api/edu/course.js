import request from '@/utils/request'

export function addCourseInfo(courseInfo){

    return request({
        url: '/eduservice/course/addCourseInfo',
        method: 'post',
        data: courseInfo
        })
}

export function getListTeacher(){

    return request({
        url: '/eduservice/teacher/findAll',
        method: 'get',
        })
}

//根据id查询课程信息
export function getCourseInfoById(courseId){

    return request({
        url: '/eduservice/course/getCourseInfo/'+courseId,
        method: 'get',
        })
}

export function updateCourseInfo(courseInfo){

    return request({
        url: '/eduservice/course/updateCourseInfo',
        method: 'post',
        data: courseInfo
        })
}

//课程确认信息的显示
export function getPublishCourseInfo(courseId){

    return request({
        url: '/eduservice/course/getPublishCourseInfo/'+courseId,
        method: 'get',
        })
}


//课程最终发布
export function publishCourse(courseId){

    return request({
        url: '/eduservice/course/publishCourse/'+courseId,
        method: 'post',
        })
}


//获取所有course
export function getCourseList(current,limit,courseQuery){

    return request({
        url: `/eduservice/course/pageQueryCourse/${current}/${limit}`,
        method: 'post',
        //teacherQuery条件对象 后端使用requestBody获取数据
        //data表示把对象转换json进行传递到接口里面
        data: courseQuery
        })
}

//删除课程 
export function deleteCourse(courseId){

    return request({
        url: '/eduservice/course/deleteCourse/'+courseId,
        method: 'delete',
        })
}
