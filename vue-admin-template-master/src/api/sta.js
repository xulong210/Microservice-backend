import request from '@/utils/request'

//生成统计数据
export function createStaData(day){

    return request({
        url: `/edustatistics/sta/registerCount/${day}`,
        method: 'post',
        })
}

//展示图表数据
export function showData(searchObj){

    return request({
        url: `/edustatistics/sta/showData/${searchObj.type}/${searchObj.begin}/${searchObj.end}`,
        method: 'get',
        })
}


