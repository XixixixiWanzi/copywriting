<template>
	<view class="content" :style="{ backgroundImage: 'linear-gradient(rgba(255, 255, 255, 0.6), rgba(255, 255, 255, 0.6)), url(' + background + ')'}">
		<view class="reback">
			<image v-if="!successed" src="/static/reback.png" @click="goback"></image>
		</view>
		<view class="body">
			<view class="promte">
				<view v-if="!successed" class="reg">注<span></span>册</view>
				<view v-else class="info">个人信息完善</view>
			</view>
			<view v-if="!successed" class="check-phone">
				<view class="phone-view">
					<image src="/static/phonenumber.png"></image>
					<input type="text" v-model="phone" placeholder="手机号" :readonly="code_pass">
					<view v-if="showText" class="sendcode-view" @click="code_pass ? null : sendCode()">{{!code_pass ? "获取" : "通过"}}</view>
					<view v-else class="sendcode-view">{{ second }}</view>
				</view>
				
				<view v-if="!code_pass" class="code-view">
					<view class="fill-code">
						<image src="../../static/verifycode.png"></image>
						<input type="text" v-model="code" placeholder="验证码">
					</view>
					<view class="virify-button" @click="verifyCode">验<span></span>证</view>
				</view>
				<view v-else class="secret-view">
					<view class="secret">
						<image src="../../static/secret.png"></image>
						<view class="input">
							<input :type="see1 ? 'text' : 'password'" v-model="secret1" placeholder="请输入密码8~16位">
							<image class="eye" :src="see1 ? '/static/cantsee.png' : '/static/cansee.png'" @click="see1 = !see1"></image>
						</view>
					</view>
					<view class="secret">
						<image src="../../static/secret.png"></image>
						<view class="input">
							<input :type="see2 ? 'text' : 'password'" v-model="secret2" placeholder="请确认密码">
							<image class="eye" :src="see2 ? '/static/cantsee.png' : '/static/cansee.png'" @click="see2 = !see2"></image>
						</view>
					</view>
					<view class="register-button" @click="register">注<span></span>册</view>
				</view>
				
			</view>
			
			<view v-else class="fill-info">
				<view class="nickname-view">
					<text class="label">用户名:</text>
					<input type="text" v-model="nickName" placeholder="请输入用户名">
				</view>
				<view class="avatar-view">
					<text class="label">头&nbsp;像&nbsp;:</text>
					<image class="avatar" :src="finalImageSrc"></image>
					<text class="select-indicator" @click="chooseImage">></text>
				</view>
				<view class="sex-view">
					<text class="label">性&nbsp;别&nbsp;:</text>
					<radio-group v-model="sex" @change="handleCheckboxChange">
						<radio value=0><image src="../../static/male.png"></image></radio>
						<radio value=1><image src="../../static/female.png"></image></radio>
					</radio-group>					    
				</view>
				<view class="birth-view">
					<text class="label">生&nbsp;日&nbsp;:</text>
					<picker mode="date" start="1900-01-01" end={getCurrentDate()} @change="handleDateChange">{{ birthday ? formattedBirthday : '选择生日' }}</picker>
				</view>
				<view class="submit-view" @click="submitInfo"> 提<span></span>交 </view>
			</view>
		</view>
	</view>	
</template>

<script setup>
import {ref, computed, onMounted} from "vue";
import{ send6Verify, verify} from "@/api/ali/ali.js"
import{verifycodeLog, registerByphone, updateInfo, updateAvatar, Registered} from "@/api/ucenter/ucenter.js";
	const background = ref("https://copywriting001.oss-cn-beijing.aliyuncs.com/default-resource/log.png");
	const code_pass = ref(false);
	const successed = ref(false);
	const phone = ref("");
	const code = ref("");
	const secret1 = ref();
	const secret2 = ref();
	const showText = ref(true);
	const second = ref(60);
	const nickName = ref(`匿名用户${Math.floor(Math.random() * 100000)}`);
	const sex = ref(2);
	const birthday = ref();
	const imagesrc = ref(null);
	const see1 = ref(false);
	const see2 = ref(false);
	const defaultimagesrc = computed(() => {
	    if (sex.value === '0') {
			console.log(sex.value);
	        return '/static/avatarboy.png';
	    } else if (sex.value === '1') {
	        return '/static/avatargirl.png';
	    } else {
	        return '/static/avatar.png';
	    }
	});
	const finalImageSrc = computed(() => imagesrc.value || defaultimagesrc.value);
	
	const formattedBirthday = computed(() => {
	  const date = new Date(birthday.value);
	  return `${date.getFullYear()}-${(date.getMonth() + 1).toString().padStart(2, '0')}-${date.getDate().toString().padStart(2, '0')}`;
	});
	
	function chooseImage() {
		selectImage();
		// 手机权限获取
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
	
	function selectImage() {
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
				    if (imageSize > 10 * 1024 * 1024) {
					    uni.showToast({title: '选择的图片超过 10MB，请选择小于 10MB 的图片。',icon: 'none'});
					    return;
				   }
				    imagesrc.value = tempFilePath;
					},
				})
			}
		});
	}
	
	function handleDateChange(event) {
		const selectedDate = new Date(event.detail.value);
		const formattedDate = `${selectedDate.getFullYear()}-${(selectedDate.getMonth() + 1).toString().padStart(2, '0')}-${selectedDate.getDate().toString().padStart(2, '0')} 00:00:00`;
		birthday.value = formattedDate;
	};
	
	function handleCheckboxChange(value) {
		sex.value = value.detail.value;
    };
	
	function getCurrentDate() {
      const date = new Date();
      const year = date.getFullYear();
      const month = date.getMonth() + 1;
      const day = date.getDate();
      return `${year}-${month < 10 ? '0' : ''}${month}-${day < 10 ? '0' : ''}${day}`;
    };
	
	async function sendCode(){
		// uni.removeStorageSync("token")
		if (!/^[1][3,4,5,7,8,9][0-9]{9}$/.test(phone.value)) {
			uni.showToast({ title: '请输入正确手机号', icon: 'none' });
			return;
			}
		showText.value = false;
		  const interval = setInterval(() => {
		    second.value -= 1;
		    if (second.value === 0) {
		      clearInterval(interval);
		      second.value = 60;
		      showText.value = true;
		    }
		  }, 1000);
		try {
			const params = {
				phone: phone.value
			};
		    const res = await send6Verify(params);
		    if (res.code === 2000) {
				uni.showToast({title: '发送成功',icon: 'none',duration:2000});
			} else {
				uni.showToast({title: '发送失败',icon: 'none',duration:2000});
				showText.value = true;
				second.value = 60;
			}
		} catch (error) {
			console.error(error);
			}
	}
	
	async function verifyCode(){
		if (!/^[1][3,4,5,7,8,9][0-9]{9}$/.test(phone.value)) {
			uni.showToast({ title: '请输入正确手机号', icon: 'none' });
			return;
		}
		if (!code.value || code.value.trim() === '') {
			uni.showToast({ title: '请输入验证码', icon: 'none' });
			return;
		}
		try {
		    const params = {
		      phone: phone.value,
		      verifyCode: code.value
		    };
		    const res = await verify(params);
		    if (res.code === 2000) {
				const res2 = await Registered0();
				second.value = 60;
				showText.value = true;
				if(res2){
					uni.showToast({title: "该账号已经注册", icon:"none", duration: 2000});
					return;
				}
				code_pass.value = true;
		    } else {
		      uni.showToast({title: res.message, icon:"none", duration: 2000});
		    }
		} catch (error) {
		    console.error(error);
		}
	}
	
	async function Registered0(){
		try{
			const res1 = await Registered(phone.value);
			if(res1.code === 2000){
				return true;
			}else{
				uni.showToast({ title: '该账号尚未注册', icon: 'none', duration: 2000 });
				return false;
			}
		}catch(error){
			console.error(error);
			uni.showToast({title: "网络异常", icon:"none", duration: 2000});
			return false;
		}
	}
	
	function goback(){
		uni.navigateBack({
			delta: 1
		})
	}
	
	
	async function register(){
		if (secret1.value !== secret2.value) {
			uni.showToast({ title: '两次输入的密码不一致', icon: 'none' });
		    return;
		  };
		if (secret1.value.length < 8 || secret1.value.length > 16) {
			uni.showToast({ title: '密码长度应在8~16之间', icon: 'none' });
		    return;
		  };
		const chineseRegex = /[\u4e00-\u9fa5]/;
		if (chineseRegex.test(secret1.value)) {
			uni.showToast({ title: '密码不允许包含中文字符', icon: 'none' });
		    return;
		  };
		try {
		    const params = {
		      phone: phone.value,
		      password: secret1.value,
		      info: {
		        nickName: nickName.value,
		        sex: sex.value.toString(), 
				}
			};
			const res = await registerByphone(params);
			if (res.code === 2000) {
				successed.value = true;
				login();
			} else {
			  uni.showToast({title: res.message, icon:"none", duration: 2000});
			}
		}catch (error) {
			console.error(error);
		}
	}
	
	async function login(){
		const params = {
			phone: phone.value
		};
		try{
			const res = await verifycodeLog(params);
			if (res.code === 2000) {
				console.log(res)
			    uni.setStorageSync("copywriting-token", res.data.token);
				return true;
			} else {
			    uni.showToast({title: res.message, icon:"none", duration: 2000});
				return false;
			}
		}catch(err){
			console.log(err)
		}
	}
	
	async function submitInfo() {
	    const avatarSuccess = await uploadAvatar0();
	    const infoSuccess = await uploadInfo0();
	
	    if (!avatarSuccess || !infoSuccess) {
	        uni.showToast({ title: "信息上传失败！", icon: "none", duration: 2000 });
	    }
		uni.redirectTo({
		    url: "/pages/home/home"
		})
	}
	
	async function uploadAvatar0(){
		try{
			console.log(imagesrc.value);
			const res = await updateAvatar(imagesrc.value);
			if(res.code == 2000){
				return true;
			}else {
				return false;
			}
		}catch(err){
			console.log(err);
			uni.showToast({title: "Network error", icon:"none", duration: 2000});
		}
	}
	async function uploadInfo0(){
		const params = {
			nickName: nickName.value,
			sex: sex.value,
			birthday: birthday.value
		};
		console.log(params);
		try{
			const res = await updateInfo(params);
			if(res.code == 2000){
				return ture;
			}else {
				return false;
			}
		}catch(err){
			console.log(err);
			uni.showToast({title: "Network error", icon:"none", duration: 2000});
		}
	}
</script>

<style lang="scss">
	.content{
		background-size: cover;
		background-position: center;
		display: flex;
		width: 100%;
		height: 100vh; 
		.reback{
			position: fixed;
			top: 80rpx;
			margin: 30rpx;
			height: 120rpx;
			image{
				width: 70rpx;
				height: 70rpx;
			}
			:hover{
				 transform: scale(1.15);
			}
		};
		.body{
			position: fixed;
			top: 250rpx;
			bottom: 250rpx;
			left: 60rpx;
			right: 60rpx;
			border-radius: 25rpx;
			background-color: rgba(240, 235, 235, 0.8);
			display: flex;
			justify-content: center;
			align-items: center;
			.promte{
				position: inherit;
				top: 320rpx;
				font-size: 52rpx;
				color:#522b24;
				letter-spacing: 10rpx;
				span{
					margin: 30rpx;
				};
			};
			image{
				width: 60rpx;
				height: 60rpx;
				margin: 10rpx ;
			};
			.check-phone{
				position: fixed;
				top: 550rpx;
				display: flex;
				align-items: center;
				justify-content: center;
				flex-direction: column;
				.phone-view{
					display: flex;
					align-items: center;
					justify-content: center;
					margin-bottom: 40rpx; ;
					input{
						border: 1px solid #a79b9b; /* 设置边框为1像素实线，颜色为 #a79b9b */
						border-radius: 6rpx; 
						width: 350rpx; 
						height: 100rpx; 
						margin: 10rpx;
						font-size: 30rpx;
						line-height: 100rpx;
						padding-left: 20rpx;
						box-sizing: border-box; 
					};
					.sendcode-view{
						height: 90rpx;
						width: 120rpx;
						margin-left: 10rpx;
						border: 1px solid #3b3021;
						border-radius: 12rpx;
						letter-spacing: 5rpx;
						line-height: 90rpx;
						font-size: 28rpx;
						text-align: center;
					}
					.sendcode-view:hover{
						transform: scale(1.1);
					}
				};
				.code-view{
					display: flex;
					flex-direction: column; /* 设置为纵向排列 */
					align-items: center; /* 在主轴上居中 */
					justify-content: center; /* 在交叉轴上居中 */
					.fill-code{
						display: flex;
						align-items: center;
						justify-content: center;
						margin-bottom: 60rpx;
						input{
							border: 1px solid #a79b9b; /* 设置边框为1像素实线，颜色为 #a79b9b */
							border-radius: 6rpx; 
							width: 480rpx; 
							height: 100rpx; 
							margin: 20rpx;
							font-size: 30rpx;
							line-height: 100rpx;
							padding-left: 20rpx;
							box-sizing: border-box; 
						}
					};
					.virify-button{
						display: flex;
						justify-content: center;
						align-items: center;
						border: 2px solid #3b1919;
						width: 250rpx;
						background: transparent;
						height: 90rpx;
						border-radius: 20rpx;
						color: #250808;
						font-weight: bold;
						font-size: 32rpx;
						span{
							margin: 10rpx;
						} ;											
					}
					.virify-button:hover{
						 transform: scale(1.1);
					}
				}
				.secret-view{
					display: flex;
					align-items: center; 
					justify-content: center;
					flex-direction: column;
					.secret{
						display: flex;
						align-items: center; 
						justify-content: center;
						flex-direction: row;
						margin-bottom: 20rpx;
						.input{
							display: flex;
							align-items: center;
							justify-content: center;
							flex-direction: row;
							border: 1px solid #a79b9b; /* 设置边框为1像素实线，颜色为 #a79b9b */
							border-radius: 6rpx; 
							width: 480rpx; 
							height: 100rpx; 
							margin: 20rpx;
							font-size: 30rpx;
							line-height: 100rpx;
							padding-left: 20rpx;
							box-sizing: border-box; 
							image{
								width: 40rpx;
								height: 40rpx;
							}
						};
					}
					.register-button{
						display: flex;
						margin: 20rpx;
						justify-content: center;
						align-items: center;
						border: 2px solid #3b1919;
						width: 250rpx;
						background: transparent;
						height: 90rpx;
						border-radius: 20rpx;
						color: #250808;
						font-weight: bold;
						font-size: 40rpx;
						letter-spacing: 10rpx; 
						span{
							margin: 10rpx;
						} ;
						:hover{
							 transform: scale(1.15);
						}
					}
				}
			};
			.fill-info{
				position: fixed;
				top: 550rpx;
				display: flex;
				align-items: center; 
				justify-content: center;
				flex-direction: column;
				.label{
					font-size: 32rpx;
					margin: 20rpx;
					line-height: 70rpx;
					position: fixed;
					left: 100rpx;
					font-weight: bold;
					
				};
				.nickname-view{
					display: flex;
					justify-content: center;
					flex-direction: column;
					flex-direction: row;
					input{
						border-bottom: 1px solid #a79b9b;
						width: 300rpx;
						height: 70rpx; 
						margin: 20rpx;
						font-size: 32rpx;
						line-height: 70rpx;
						margin-left: 100rpx;
					}
				};
				.avatar-view{
					display: flex;
					align-items: center; 
					justify-content: center;
					flex-direction: row;
					image{
						width: 200rpx;
						height: 200rpx;
						border-radius: 100rpx;
						margin: 20rpx;
						margin-left: 60rpx;
						margin-right: 10rpx;
					};
					.select-indicator{
						font-size: 48rpx;
					}
					.select-indicator:hover{
						color: blue;
						font-size: 64rpx;
					}						
				};
				.sex-view{
					display: flex;
					align-items: center;
					margin-left: 40rpx;
					justify-content: center;
					image{
						width: 50rpx;
						height: 50rpx;
						margin: 10rpx;
						margin-right: 50rpx
					};
					radio{
						margin-left: 40rpx;
					}
				};
				.birth-view{
					font-size: 32rpx;
					margin: 30rpx;
					display: flex;
					justify-content: center;
					align-items: center;
					flex-direction: row;
					picker{
						libe-height: 70rpx;
						margin-left: 40rpx;
						border-bottom: 1px solid #a79b9b;
						letter-spacing: 2px;
					};
					picker:hover{color: blue;}
				};
				.submit-view{
					display: flex;
					justify-content: center;
					align-items: center;
					border: 2px solid #3b1919;
					width: 250rpx;
					background: transparent;
					height: 90rpx;
					border-radius: 20rpx;
					color: #250808;
					font-weight: bold;
					font-size: 40rpx;
					span{
						margin: 20rpx;
					} ;
					:hover{
						 transform: scale(1.15);
					}
				}
			}
		}
	}

</style>
