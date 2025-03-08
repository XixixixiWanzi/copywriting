// 全局请求封装
const base_url = 'http://localhost:10001/copywriting/'
// 需要修改token，和根据实际修改请求头
export default (params) => {
    let url = params.url;
    let method = params.method || "get";
    let data = params.data || {};

    let head = {}
    if (method == "post") {
        head = {
            'Content-Type': 'application/json'
        };
	}
    
	// uni.removeStorageSync("token");
    if (uni.getStorageSync("copywriting-token")) {
        head['Authorization'] = 'Bearer ' + uni.getStorageSync("copywriting-token");
		
    }
    return new Promise((resolve, reject) => {
        uni.request({
            url: base_url + url,
            method: method,
            header: head,
            data: data,
            success(response) {
                const res = response;
                if (res.statusCode == 200) {
                    resolve(res.data);
                } else {
                    uni.clearStorageSync()
                    switch (res.statusCode) {
                        case 401:
                            uni.showModal({
                                title: "提示",
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
                        case 404:
                            uni.showToast({
                                title: '请求地址不存在...',
                                duration: 2000,
                            })
                            break;
                        default:
                            uni.showToast({
                                title: '请重试...',
                                duration: 2000,
                            })
                            break;
                    }
                }
            },
            fail(err) {
                console.log(err)
                if (err.errMsg.indexOf('request:fail') !== -1) {
                    uni.showToast({
                        title: '网络异常',
                        icon: "error",
                        duration: 2000
                    })
                } else {
                   uni.showToast({
                        title: '未知异常',
                        duration: 2000
                    })
                }
                reject(err);

            },
            complete() {
                // 不管成功还是失败都会执行
                uni.hideLoading();
                uni.hideToast();
            }
        });
    }).catch((e) => {});
};