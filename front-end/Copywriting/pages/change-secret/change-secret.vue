<template>
	<view class="content" :style="{ backgroundImage: 'linear-gradient(rgba(255, 255, 255, 0.7), rgba(255, 255, 255, 0.7)), url(' + imagesrc + ')'}">
	    <view class="top">
	    	<image src="../../static/home.png" @click="goback" ></image>
	    </view>
		<view class="body">
			<view class="promte">
				<view  class="reg">密码修改</view>
			</view>
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
				<view class="sure-button" @click="modifySecret">确认修改</view>
			</view>
			
		</view>		
	</view>
</template>

<script >
	import{ send6Verify, verify} from "@/api/ali/ali.js"
	import{changePassWord, verifyPhone, Registered, verifycodeLog} from "@/api/ucenter/ucenter.js";
	export default {
		data() {
			return{
				type: null,
				successed: false,
				imagesrc: "https://copywriting001.oss-cn-beijing.aliyuncs.com/default-resource/background2.jpg",
				code_pass: false,
				phone : null,
				code : null,
				secret1 : null,
				secret2 : null,
				showText : true,
				second: 60,
				see1: false,
				see2: false,
				
			}
		},
		onLoad: function (option) {
			console.log(option.type); 
			this.type  = option.type;
		},
		methods: {
			goback(){
				if(this.type === 0){
					uni.navigateBack({delta: 1});
				}else{
					if(this.successed){
						uni.redirectTo({
							url:"/pages/home/home"
						})
					}else{
						uni.navigateBack({delta: 1});
					}
					
				}
			},
			
			async sendCode(){
				if (!/^[1][3,4,5,7,8,9][0-9]{9}$/.test(this.phone)) {
					uni.showToast({ title: '请输入正确手机号', icon: 'none' });
					return;
				}
				if(this.type === '0'){
					const res1 = await this.verifyPhoneSame();
					if( !res1){
						return;
					}
				}else{
					const res2 = await this.Registered0();
					if( !res2){
						return;
					}
				}
				try{
					this.showText = false;
					const interval = setInterval(() => {
						 this.second -= 1;
						if (this.second === 0) {
							clearInterval(interval);
							this.second = 60;
							this.showText = true;
							}
						}, 1000);
					const params = {
						phone: this.phone,
					};
					const res = await send6Verify(params);
					if (res.code === 2000) {
						uni.showToast({title: '发送成功',icon: 'none',duration:2000});
					} else {
						uni.showToast({title: '发送失败',icon: 'none',duration:2000});
						this.showText= true;
						this.second = 60;
					}
				}catch(error){
					console.error(error);
					uni.showToast({title: "网络异常", icon:"none", duration: 2000});
				}
			},
			
			async verifyPhoneSame(){
				try{
					const res1 = await verifyPhone(this.phone);
					if(res1.code === 2000){
						return true;
					}else{
						uni.showToast({ title: '与账号不一致', icon: 'none', duration: 2000 });
						return false;
					}
				}catch(error){
					console.error(error);
					uni.showToast({title: "网络异常", icon:"none", duration: 2000});
					return false;
				}
			},
			async Registered0(){
				try{
					const res1 = await Registered(this.phone);
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
			},
			
			async verifyCode(){
				if (!/^[1][3,4,5,7,8,9][0-9]{9}$/.test(this.phone)) {
					uni.showToast({ title: '请输入正确手机号', icon: 'none' });
					return;
				}
				if (!this.code || this.code.trim() === '') {
					uni.showToast({ title: '请输入验证码', icon: 'none' });
					return;
				}
				try {
				    const params = {
				      phone: this.phone,
				      verifyCode: this.code
				    };
				    const res = await verify(params);
				    if (res.code === 2000) {
						this.showText = true;
						this.code_pass = true;
				    } else {
				      uni.showToast({title: "验证码错误或失效", icon:"none", duration: 2000});
				    }
				  } catch (error) {
				    console.error(error);
					uni.showToast({title: "网络异常", icon:"none", duration: 2000});
				  }
			},
			
			async modifySecret(){
				if (this.secret1 !== this.secret2) {
					uni.showToast({ title: '两次输入的密码不一致', icon: 'none' });
				    return;
				  };
				if (this.secret1.length < 8 || this.secret1.length > 16) {
					uni.showToast({ title: '密码长度应在8~16之间', icon: 'none' });
				    return;
				  };
				const chineseRegex = /[\u4e00-\u9fa5]/;
				if (chineseRegex.test(this.secret1)) {
					uni.showToast({ title: '密码不允许包含中文字符', icon: 'none' });
				    return;
				  };
				try {
					const params ={
						newSecret: this.secret1,
						phone: this.phone,
					};
					const res = await changePassWord(params);
					console.log(res);
					if(res.code === 2000){
						this.successed = true;
						if( this.type === 1){
							let res2 = await this.login();
							let num = 1;
							while(!res2 && num < 4){
								res2 = await this.login();
								await new Promise(resolve => setTimeout(resolve, 5000));
								num = num + 1;
							}
							if(num > 3){
								uni.showToast({ title: '系统错误，修改失败', icon: 'none' });
								return ;
							}
						}
						this.goback();
					}
					else{
						uni.showToast({ title: '系统错误，修改失败', icon: 'none' });
					}
				}catch (error) {
					console.error(error);
				}
			},
			async login(){
				const params = {
					phone: phone.value
				};
				try{
					const res = await verifycodeLog(params);
					if (res.code === 2000) {
						console.log(res)
						uni.setStorageSync("token", res.data.token);
						return true;
					} else {
						uni.showToast({title: res.message, icon:"none", duration: 2000});
						return false;
					}
				}catch(err){
					return false;
					console.log(err)
				}
			},
		}
		
	}
</script>

<style lang="scss">
.content{
	background-size: cover;
	background-position: center;
	width:  100%;
	height: 100vh;
	.top{
		position: fixed;
		top: 80rpx;
		left: 50rpx;
		margin: 30rpx;
		height: 120rpx;
		image{
			width: 70rpx;
			height: 70rpx;
		}
	}
	.body{
		position: fixed;
		display: flex;
		flex-direction: column;
		align-items: center;
		top: 300rpx;
		bottom: 500rpx;
		left: 70rpx;
		right: 70rpx;
		border-radius: 50rpx;
		background-color: rgba(250,240,240,0.8);
		.promte{
			font-size: 52rpx;
			color:#522b24;
			letter-spacing: 10rpx;
			margin: 40rpx;
			margin-bottom: 100rpx; 
		};
		image{
			width: 60rpx;
			height: 60rpx;
			margin: 10rpx ;
		};
		.phone-view{
			display: flex;
			align-items: center;
			justify-content: center;
			margin-bottom: 40rpx; ;
			display: flex;
			justify-content: center;
			align-items: center;
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
			.sure-button{
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
	}
}
</style>
