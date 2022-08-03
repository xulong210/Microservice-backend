import request from '@/utils/request'

export function getAllChapterVideo(courseId){

    return request({
        url: `/eduservice/chapter/getChapterVideo/${courseId}`,
        method: 'get',
        })
}

export function addChapter(chapter){

    return request({
        url: `/eduservice/chapter/addChapter`,
        method: 'post',
        data: chapter
        })
}

export function getChapterInfo(courseId){

    return request({
        url: `/eduservice/chapter/getChapterInfo/${courseId}`,
        method: 'get',
        })
}

export function updateChapter(chapter){

    return request({
        url: `/eduservice/chapter/updateChapter`,
        method: 'post',
        data: chapter
        })
}

export function deleteChapter(chapterId){

    return request({
        url: `/eduservice/chapter/deleteChapter/${chapterId}`,
        method: 'delete',
        })
}


