<template>
	<view class="content" :style="{ backgroundImage: 'linear-gradient(rgba(255, 255, 255, 0.7), rgba(255, 255, 255, 0.7)), url(' + imagesrc + ')'}">
		  <view class="top" :style="{ backgroundImage: 'linear-gradient(rgba(255, 255, 255, 0.7), rgba(255, 255, 255, 0.7)), url(' + imagesrc + ')'}">
		     	<span></span>
				<view class="tabar">
					<image src="../../static/reback2.png" @click="goback" ></image>
					<span></span>
					<image src="../../static/setting.png" @click="gosetting"></image>
				</view>
				<view class="avatar-body" @click="uploadAvater0">
		     		<image :src="info ? avatersrc : null" mode='aspectFill'></image>
		     	</view>
		  </view>      
		
		<view class="info-body">
			<view class="item nickname">
				<view class="label">用&nbsp;户&nbsp;名</view>
				<input class="center" type="text" v-model="nickName" :disabled="!modify_nickname"/>
				<image :src="modify_nickname ? cancel_img : revise_img" @click="modifyNickname"></image>
			</view>
			<view class="item secret">
				<view class="label">密<span class="one"></span>码</view>
				<view class="center">**************</view>
				<image :src="revise_img" @click="gochagesecret"></image>
			</view>
			<view class="item sex">
				<view class="label">性<span class="one"></span>别</view>
					<radio-group ref="radioGroup" class="center" v-model="sex" @change="handleCheckboxChange">
						<radio  :checked="sex === '0'" value="0" :disabled="!modify_sex"><image src="../../static/male.png"></image></radio>
						<radio  :checked="sex === '1'" value="1" :disabled="!modify_sex"><image src="../../static/female.png"></image></radio>
					</radio-group>	
				<image :src="modify_sex ? cancel_img : revise_img" @click="modifySex"></image>
			</view>
			<view class="item birthday">
				<view class="label">生<span class="one"></span>日</view>
				<picker class="center" mode="date" start="1900-01-01" 
					end="2030-01-01" @change="handleDateChange" :disabled="!modify_birthday">{{ birthday ? formattedBirthday : '-----'}}</picker>
				<image :src="modify_birthday ? cancel_img : revise_img"  @click="modifyBirthday"></image>
			</view>
			<view class="item phone">
				<view class="label">手机认证</view>
				<view class="center">{{ info ?  info.phoneActivatedTime ? info.phoneActivatedTime : '未认证' : ''}}</view>
				<view class="space"></view>
			</view>
			<view class="item wechat">
				<view class="label">微信认证</view>
				<view class="center">{{ info ? info.wechatActivatedTime ? info.wechatActivatedTime : "未认证" : '' }}</view>
				<view class="space"></view>
			</view>
		</view>
		<view v-if="shouldShowBottom" class= "bottom">
			<view class="btn"  @click="submit"> 提交 </view>
		</view>
	</view>
</template>

<script setup>
	import { getInfo,updateInfo ,updateAvatar} from "@/api/ucenter/ucenter.js"
	import { computed, onMounted, reactive, ref } from 'vue'
	
	const info = ref(null);
	const imagesrc = ref("https://copywriting001.oss-cn-beijing.aliyuncs.com/default-resource/background2.jpg")
	const avatersrc = ref(null);
	const modify_nickname = ref(false);
	const modify_birthday = ref(false);
	const modify_sex = ref(false);
	const revise_img = ref("../../static/revise.png");
	const cancel_img = ref("../../static/cancel.png");
	const nickName = ref(null);
	const sex = ref(null);
	const birthday = ref(null);
	
	const shouldShowBottom = computed(() => modify_nickname.value || modify_birthday.value || modify_sex.value);

	
	
	onMounted(async () => {
	   const res = await fetchData();
	   while(!res){
		   await new Promise(resolve => setTimeout(resolve, 3000));
		   res = await fetchData();
	   }
	   if (info.value) {
		   avatersrc.value = info.value.avatarUrl;
	       sex.value = info.value.sex ? info.value.sex : "未填写";
	       nickName.value = info.value.nickName;
	       birthday.value = info.value.birthday ? info.value.birthday : "未填写";
	     }
	});
	
	const formattedBirthday = computed(() => {
	  const date = new Date(birthday.value);
	  return `${date.getFullYear()}-${(date.getMonth() + 1).toString().padStart(2, '0')}-${date.getDate().toString().padStart(2, '0')}`;
	});
	
	async function uploadAvater0(){
		try{
			const sign = await chooseImage();
			if(!sign){
				return false;
			}
			const res = await updateAvatar(avatersrc.value);
			if(JSON.parse(res).code == 2000){
				info.value.avatarUrl = avatersrc.value;
			}else {
				avatersrc.value = info.value.avatarUrl;
				uni.showToast({title: "出错了", icon:"none", duration: 2000});
			}
		}catch(err){
			console.log(err);
			uni.showToast({title: "Network error", icon:"none", duration: 2000});
		}
	}
	
	function chooseImage() {
		return new Promise((resolve, reject) => {
		        selectImage(resolve, reject);
		    });
	    // uni.getSetting({
	    //     success: (res) => {
	    //       if (res.authSetting['scope.writePhotosAlbum']) {
	    //         selectImage();
	    //       } else {
	    //         uni.authorize({
	    //           scope: 'scope.writePhotosAlbum',
	    //           success: () => {
	    //             selectImage();
	    //           },
	    //           fail: (err) => {
	    //             console.error(err);
	    //             uni.showToast({title: '获取相册权限失败，请在设置中开启相册权限。',icon: 'none',});
	    //           },
	    //         });
	    //       }
	    //     },
	    // });
	};
	
	function selectImage(resolve, reject) {
	    uni.chooseImage({
	        count: 1,
	        sizeType: ['original', 'compressed'],
	        sourceType: ['album'],
	        success: (res) => {
	            const tempFilePath = res.tempFilePaths[0];
	            uni.getImageInfo({
	                src: tempFilePath,
	                success: (info) => {
	                    const imageSize = info.size;
	                    if (imageSize > 20 * 1024 * 1024) {
	                        uni.showToast({ title: '选择的图片超过 20MB，请选择小于 20MB 的图片。', icon: 'none' });
	                        reject(false);
	                    } else {
	                        avatersrc.value = tempFilePath;
	                        resolve(true);
	                    }
	                },
	            });
	        },
	        fail: () => {
	            reject(false);
	        }
	    });
	}
	
	
	async function submit(){
		const params = {
			nickName: nickName.value,
			sex: sex.value,
			birthday: birthday.value
		};
		try{
			const res = await updateInfo(params);
			if(res.code == 2000){
				modify_birthday.value = false;
				modify_nickname.value = false;
				modify_sex.value = false;
				info.value.birthday = birthday.value;
				info.value.sex = sex.value;
				info.value.nickName = nickName.value;
				return true;
			}else {
				uni.showToast({title: res.message, icon:"none", duration: 2000});
				return false;
			}
		}catch(err){
			console.log(err);
			uni.showToast({title: "Network error", icon:"none", duration: 2000});
		}
	}
	
	async function fetchData() {
		try {
			const res = await getInfo();
			if (res.code === 2000) {
			info.value = res.data.info;
			console.log(info.value);
			return true;
	    } else {
			uni.showToast({ title: '网络异常', duration: 2000, icon: "none" });
	        return false;
	       }
		} catch (e) {
			uni.showToast({ title: '网络异常', duration: 2000, icon: "none" });
			console.log(e);
			return false;
		}
	};
	
	function handleDateChange(event) {
		const selectedDate = new Date(event.detail.value);
		const formattedDate = `${selectedDate.getFullYear()}-${(selectedDate.getMonth() + 1).toString().padStart(2, '0')}-${selectedDate.getDate().toString().padStart(2, '0')} 00:00:00`;
		birthday.value = formattedDate;
	};
	function handleCheckboxChange(value) {
		sex.value = value.detail.value;
	};
	
	function modifyNickname(){
		if(modify_nickname.value){
			nickName.value = info.value.nickName;
		}
		modify_nickname.value = !modify_nickname.value;
	}
	function modifySex(){
		if(modify_sex.value){
			sex.value = info.value.sex;
		}
		modify_sex.value = !modify_sex.value;
	}
	function modifyBirthday(){
		if(modify_birthday.value){
			birthday.value = info.value.birthday;
		}
		modify_birthday.value = !modify_birthday.value;
	}
	
	
	function goback(){
		uni.navigateBack({delta: 1});
	};
	function gosetting(){
		// 待完成
	};
	function gochagesecret(){
		uni.navigateTo({
			url:"/pages/change-secret/change-secret?type=0",
		})
	}
</script>

<style lang="scss">
	.one{
		margin: 35rpx;
	}
	.content{
		background-size: cover;
		background-position: center;
		width:  100%;
		height: 100vh; 
		display: flex;
		align-items: center;
		flex-direction: column;
		.top{
			display: flex;
			flex-direction: column;
			align-items: center; 
			justify-content: flex-end;
			width: 100%;
			height: 500rpx;
			background-size: cover;
			span{
				margin: 30rpx
			};
			.tabar{
				image{
					width: 80rpx;
					height: 80rpx;
				};
				span{
					margin-left: 250rpx;
					margin-right: 250rpx;
				}
			}
			.avatar-body{
				image{
					width: 300rpx;
					height: 300rpx;
					border-radius: 30rpx;
					margin: 30rpx;
				}
			}	
		}
		.info-body{
			display: flex;
			align-items: center;
			flex-direction: column;
			background: linear-gradient(to bottom, rgba(250, 235, 235, 0.6), rgba(250, 250, 250, 0.3));
			height: auto;
			width: 100%;
			image{
				width: 40rpx;
				height: 40rpx;
				margin: 15rpx;
			}
			.space{
				width: 40rpx;
				height: 40rpx;
				margin: 15rpx;
			}
			.item{
				display: flex;
				align-items: center;
				flex-direction: row;
				display: flex;
				width: calc(100% - 40px); /* 子组件宽度减去 margin */
				margin: 15rpx;
				box-sizing: border-box; 
				height: 100rpx;
				.label {
				  font-size: 42rpx;
				  width: 200rpx;
				  height: 100rpx;
				  display: flex;
				  justify-content: center; /* 文字水平居中 */
				  align-items: center; /* 文字垂直居中 */
				  text-align: center; /* 文字水平居中（兼容性处理） */
				  margin: 0rpx 40rpx 0rpx 0rpx;
				}
				.center{
					width: 330rpx;
					margin-right: 20rpx ;
					padding-bottom: 5rpx;
					padding-left: 20rpx;
					font-size: 32rpx;
					border-bottom: 1rpx solid #AAAAAA;
					image{
						width: 65rpx;
						height: 65rpx;
						margin-right: 25rpx;
					}
				}
			}
		}
		.bottom{
			flex: 1;
			width: auto;
			display: flex;
			align-items: center; /* 在垂直方向上居中 */
			justify-content: center;
			margin: 10rpx;
			.btn{
				font-size: 48rpx;
				width: 300rpx;
				height: 140rpx;
				border-radius: 30rpx;
				background-color: rgba(220,200,200,0.9); /* 为了演示，添加背景色 */
				color: #ffffff; /* 文字颜色，可以根据需要调整 */
				display: flex;
				align-items: center; /* 在垂直方向上居中 */
				justify-content: center; /* 在水平方向上居中 */
			}
		}
		
	}

</style>
