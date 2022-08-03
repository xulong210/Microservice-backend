import request from '@/utils/request'

export function saveVideo(video){

    return request({
        url: '/eduservice/video/addVideo',
        method: 'post',
        data: video
        })
}

export function getVideoInfo(videoId){

    return request({
        url: '/eduservice/video/getVideoInfo/'+videoId,
        method: 'get',
        })
}

export function updateVideo(video){

    return request({
        url: '/eduservice/video/updateVideo/',
        method: 'post',
        data: video
        })
}

export function deleteVideo(videoId){

    return request({
        url: '/eduservice/video/deleteVideo/'+videoId,
        method: 'delete',
        })
}

//删除视频
export function deleteAliyunVideo(id){

    return request({
        url: '/eduvideo/video/deleteVideo/'+id,
        method: 'delete',
        })
}