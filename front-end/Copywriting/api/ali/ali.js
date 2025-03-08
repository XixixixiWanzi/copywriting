import request from '@/utils/request'

const base_servise = "ali_service/"

export const send4Verify = (params) => {
    return request({
        url: base_servise + 'aliService/msm/send4Verify',
        method: 'get',
		data: params
	})
}
// 'http://localhost:9091/TrailTrek/account/modifySecret/GetVerifyCode?phone=18161011898
export const send6Verify = (params) => {
    return request({
        url: base_servise + 'aliService/msm/send6Verify',
        method: 'get',
		data: params
	})
}

export const verify = (params) => {
    return request({
        url: base_servise + 'aliService/msm/verify',
        method: 'get',
		data: params
	})
}
