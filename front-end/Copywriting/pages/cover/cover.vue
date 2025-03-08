<template>
	<view>
		<video id="myVideo" class="content" object-fit="fill" 
		:enable-play-gesture="false" :enable-progress-gesture="false" 
		:show-loading="false" :show-center-play-btn="false" 
		src="https://copywriting001.oss-cn-beijing.aliyuncs.com/default-resource/index.mp4" 
		:autoplay="true" :loop="true" :controls="false"
		>
			<cover-view class="xz-title"  ><p v-html="text"></p></cover-view>

			<view v-if="checkedToken && !effectToken">
				<cover-view class="xz-button-1" @click="goToLoginPage">登录</cover-view>
				<cover-view class="xz-button-2" @click="goToRegisterPage">注册</cover-view>
			</view>
			<view v-if="checkedToken && effectToken">
				<cover-view class="xz-button-3" @click="goToHomePage">进<span></span>入</cover-view>
			</view>
		</video>
	</view>
</template>

<script setup>
import { onMounted, ref } from "vue";
import{checkToken} from "@/api/ucenter/ucenter.js"

	const text = ref("我途径一场花的盛放，<br>想将它说给你看")
	const checkedToken = ref(false);
	const effectToken = ref (null);
	
	
	const goToLoginPage = () => {
	  uni.navigateTo({
	    url: '/pages/login/login'
	  });
	};
	
	const goToRegisterPage = () => {
	  uni.navigateTo({
	    url: '/pages/register/register'
	  });
	};
	
	const goToHomePage = () => {
		uni.redirectTo({
		  url: '/pages/home/home'
		});
	} 
	onMounted(async () => { 
	  uni.removeStorageSync("copywriting-token");
	  try {
	    await new Promise(resolve => setTimeout(resolve, 3000));
	    await checkToken0();
	  } catch (e) {
	    console.log(e);
	    uni.showToast({ duration: 2000, title: "加载失败" });
	  }
	  checkedToken.value = true;
	});
	async function checkToken0 (){
		try{
			const res = await checkToken();
			console.log(res);
			if(res.code == 2000){
				effectToken.value = true;
			}else{
				effectToken.value = false;
			}		
		}catch(e){
			console.log(e);
			effectToken.value = false;
			uni.showToast({duration:2000,title:"加载失败"});
		}
	}
	
</script>



<style lang="scss">
.content{
	width: 100%;
	height: 100%;
	position: fixed;
	top: 5rpx;
	left: 5rpx;
	bottom: 5rpx;
	right: 5rpx;
	.xz-title{
		position: fixed;
		width: 600rpx;
		height: auto;
		bottom: 520rpx;
		margin-left: 40rpx;
		border-radius: 12rpx;
		line-height: 1.5;
		color: #FFFFFF;
		font-size:36rpx;
		font-family: 'Open Sans', sans-serif;
	}

	.xz-button-1{
		width: 240rpx;
		height: 90rpx;
		position: fixed;
		bottom: 300rpx;
		margin-left: 40rpx;
		background-color: #FFFFFF;
		border-radius: 12rpx;
		display: flex;
		justify-content: center;
		text-align: center;
		font-size: 32rpx;
		line-height: 90rpx;
	}
	.xz-button-1:hover {
	  transform: scale(1.15); /* 放大 10% */
	}
	.xz-button-1:active {
	  transform: scale(0.9); 
	}
	.xz-button-2{
		width: 240rpx;
		height: 90rpx;
		position: fixed;
		bottom: 300rpx;
		margin-left: 320rpx;
		border:4rpx solid #FFFFFF;
		box-sizing: border-box;
		border-radius: 12rpx;
		text-align: center;
		line-height: 90rpx;
		color: #FFFFFF;
		font-size: 32rpx;
	}
	.xz-button-2:hover {
	  transform: scale(1.15); /* 放大 10% */
	}
	.xz-button-2:active {
	  transform: scale(0.9); 
	}
	.xz-button-3{
		display: flex;
		align-items: center;
		justify-content: center;
		flex-direction: row;
		border-top: 2rpx solid #FFFFFF;
		border-bottom: 2rpx solid #FFFFFF;
		color: #FFFFFF;
		width: 400rpx;
		height: 90rpx;
		line-height: 90rpx;
		font-size: 32rpx;
		position: fixed;
		bottom: 300rpx;
		left: 145rpx;
		margin: 30rpx;
		span{
			margin: 30rpx;
		}
	}
	.xz-button-3:hover {
	  transform: scale(1.15); /* 放大 10% */
	}
}
</style>
