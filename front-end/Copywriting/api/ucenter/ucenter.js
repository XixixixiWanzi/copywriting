import request from '@/utils/request'

const base_servise = "ucenter_service/"

export const secretLog = (params) => {
    return request({
        url: base_servise + 'ucenter/forLogin/bySecret',
        method: 'get',
        data: params
	})
}


export const verifycodeLog = (params) => {
    return request({
        url: base_servise + 'ucenter/forLogin/byVerifyCode',
        method: 'get',
		data: params
	})
}

export const checkToken = () => {
    return request({
        url: base_servise + 'ucenter/forLogin/checkToken',
        method: 'get'
	})
}


export const getInfo = () => {
    const res =  request({         
        url: base_servise + 'ucenter/getInfo/getInfo',
        method: 'get'
	});
	console.log(res)
	return res;
}

export const changePassWord = (params) => {
    return request({
        url: base_servise + 'ucenter/forPassword/passwordChange?newPassword=' + params.newSecret + "&phone=" + params.phone,
        method: 'post'
	})
}

export const Registered = (phone) => {
    return request({
        url: base_servise + 'ucenter/forPassword/Registered?phone=' + phone,
        method: 'get'
	})
}


export const registerByphone = (params) =>{
	return request({
		url: base_servise + 'ucenter/forRegister/registerByPhone?phone=' + params.phone + '&password=' + params.password,
		method: 'post',
		data: params.info
	})
}


export const updateAvatar = (path) =>{
	return new Promise((resolve, reject) => {
	        uni.uploadFile({
	            url: 'http://localhost:10001/copywriting/' + base_servise + 'ucenter/forRevise/updateAvatar',
	            filePath: path, // 文件临时路径
	            name: 'file', // 服务器定义的文件字段名称
				fileType: 'image',
	            header: {
	                'Authorization': 'Bearer ' + uni.getStorageSync("copywriting-token")
	            },
	            success(response) {
	                const res = response;
	                if (res.statusCode == 200) {
	                    resolve(res.data);
	                }else {
					   uni.clearStorageSync()
					   switch (res.statusCode) {
						   case 401:
							   uni.showModal({title: "提示",
								   content: "请登录",
								   showCancel: false,
								   success(res) {
									   setTimeout(() => {
										   uni.navigateTo({
											   url: "/pages/login/login",
										   })
									   }, 1000);
								   },
							   });
							   break;
						   default:
							   uni.showToast({title: '请重试...',duration: 2000,})
							   break;
					   }
				   }
	            },
	            fail(error) {
	                reject(error);
	            }
	        });
	    });
}

export const updateInfo = (params) =>{
	return request({
		url: base_servise + 'ucenter/forRevise/updateInfo',
		method: 'post',
		data: params
	})
}

export const verifyPhone = (phone) =>{
	return request({
		url: base_servise + 'ucenter/forPassword/verifyPhone?phone=' + phone,
		method: 'get'
	})
}





