<template>
	<view class="content" :style="{ backgroundImage: 'linear-gradient(rgba(255, 255, 255, 0.6), rgba(255, 255, 255, 0.6)), url(' + imagesrc + ')'}">
		<view class="body">
			<view class="prompt">
				<text class="sign-method" @click="changeStatus">{{secretlog ? "验证码登录&nbsp;>" : "&nbsp;密&nbsp;码&nbsp;登&nbsp;录&nbsp;>"}}</text>
			</view>

			<form v-if="secretlog" class="secret-form">
				<view class="account">
					<image src="/static/account.png" mode=""></image>
					<input clatype="text" v-model="account" placeholder="账号" />
				</view>
				<view class="secret">
					<image src="../../static/secret.png"></image>
					<view class="input">
						<input :type="see ? 'text' : 'password'" v-model="password" placeholder="密码">
						<image class="eye" :src="see ? '/static/cantsee.png' : '/static/cansee.png'" @click="see = !see"></image>
					</view>
				</view>
				<button type="submit" @click="secretLogin">
					<view class="login">登录</view>
				</button>
			</form>
			
			<form v-else class="virifycode-form">
				<view class="phonenumber">
					<image src="/static/phonenumber.png" mode=""></image>
					<input clatype="text" v-model="phonenumber" placeholder="手机号" />
					<view v-if="showText" class="for-verify" @click="sendCode">获取</view>
					<view v-else class="for-verify">{{ second }}</view>
				</view>
				<view class="verify-code">
					<image src="/static/verifycode.png" mode=""></image>
					<input clatype="text" v-model="verifycode" placeholder="验证码" />
				</view>
				<button type="submit" @click="verifycodeLogin">
					<view class="login">登录</view>
				</button>
			</form>
			
			<view class="selution">
				<text :class="{ 'forget-secret': secretlog, 'disable-forget-secret': !secretlog }" @click="secretlog ? fogetSecret() : null">忘记密码&nbsp;></text>
				<text class="go-register" @click="goRegister">注册&nbsp;></text>
			</view>
		</view>
	</view>	
</template>

<script setup>
import { onMounted, ref} from "vue";
import{secretLog, verifycodeLog, registerByphone} from "@/api/ucenter/ucenter.js";
import{send6Verify, verify} from "@/api/ali/ali.js"
const imagesrc = ref("https://copywriting001.oss-cn-beijing.aliyuncs.com/default-resource/log.png");
const secretlog = ref(true);
const account = ref("");
const password = ref("");
const phonenumber = ref("");
const verifycode = ref("");
const showText = ref(true);
const second =ref(60);
const see = ref(false);

function changeStatus() {
	secretlog.value = !secretlog.value;
	account.value = "";
	password.value = "";
	phonenumber.value = "";
	verifycode.value = "";
	second.value = 60;
	showText.value = true;
}

function fogetSecret() {
	uni.navigateTo({
		url:"/pages/change-secret/change-secret?type=1",
	})
}


function goRegister() {
	uni.navigateTo({
		url:"/pages/register/register"
	})
}

async function sendCode(){
	if (!/^[1][3,4,5,7,8,9][0-9]{9}$/.test(phonenumber.value)) {
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
			phone: phonenumber.value
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

async function secretLogin() {
	if (!account.value || account.value.trim() === '') {
		uni.showToast({ title: '请输入账号', icon: 'none' });
		return;
	}
	if (!password.value || password.value.trim() === '') {
		uni.showToast({ title: '请输入密码', icon: 'none' });
		return;
	}
  try {
    const params = {
      phone: account.value,
      secret: password.value,
    };
    const res = await secretLog(params);
	 if (res.code === 2000) {
		  console.log(res)
	      uni.setStorageSync("copywriting-token", res.data.token)
		  uni.redirectTo({
		  	url:"/pages/home/home"
		  })
	    } else {
	      uni.showToast({title: '${res.message}',icon: 'none',duration:2000
	      });
	    }
  } catch (error) {
	  console.error(error);
  }
}


async function verifycodeLogin() {
	if (!/^[1][3,4,5,7,8,9][0-9]{9}$/.test(phonenumber.value)) {
		uni.showToast({ title: '请输入正确手机号', icon: 'none' });
		return;
	}
	if (!verifycode.value || verifycode.value.trim() === '') {
		uni.showToast({ title: '请输入验证码', icon: 'none' });
		return;
	}
	try {
	    const params = {
	      phone: phonenumber.value,
	      verifyCode: verifycode.value
	    };
	    const res1 = await verify(params);
		const params2 = {
			phone: phonenumber.value,
		};
	    if (res1.code === 2000) {
	      const res2 = await verifycodeLog(params2);
	      if (res2.code === 2000) {
			  console.log(res1)
	          uni.setStorageSync("copywriting-token", res2.data.token);
			  uni.redirectTo({
			  	url:"/pages/home/home"
			  })
	      } else {
	          uni.showToast({title: res2.message, icon:"none", duration: 2000});
	      }
	    } else {
	      uni.showToast({title: res1.message, icon:"none", duration: 2000});
	    }
	  } catch (error) {
	    console.error(error);
	  };
	}
</script>

<style lang="scss">
.content{	
	background-size: cover;
	background-position: center;
	width: 100%;
	height: 100vh; 
	.body{
		position: fixed;
		top: 450rpx;
		height: 600rpx;
		margin: 40rpx;
		width: 670rpx;
		border-radius: 25rpx;
		background-color: rgba(240, 235, 235, 0.7);
		.prompt{
			position: fixed;
			left: 470rpx;
			margin: 10rpx;
			top: 550rpx;
			.sign-method{
				 font-size: 24rpx;
				 cursor: pointer;
				 color: black;
				 transition: color 0.5s ease;
				 &:hover {
				 	 font-size: 30rpx;
				    color: #0b207f;
				   }
			};	    
		};
		.secret-form{
			position: fixed;
			top: 580rpx;
			margin: 20rpx;
			.account{
				display: flex; 
				align-items: center; 
				justify-content: space-between;
				input{
					border: 1px solid #a79b9b; /* 设置边框为1像素实线，颜色为 #a79b9b */
					border-radius: 6rpx; 
					width: 450rpx; 
					height: 85rpx; 
					margin: 20rpx;
					font-size: 30rpx;
					line-height: 1.5;
					padding-left: 20rpx;
					box-sizing: border-box; 
				};
			};
			.secret{
				display: flex; 
				align-items: center;
				justify-content: space-between;
				.input{
					display: flex;
					align-items: center;
					justify-content: center;
					flex-direction: row;
					border: 1px solid #a79b9b; /* 设置边框为1像素实线，颜色为 #a79b9b */
					border-radius: 6rpx; 
					width: 450rpx; 
					height: 85rpx; 
					margin: 20rpx;
					font-size: 30rpx;
					line-height: 1.5;
					padding-left: 20rpx;
					box-sizing: border-box; 
					image{
						width: 35rpx;
						height: 35rpx;
					}
				};
			};
			image{
				width: 50rpx;
				height: 50rpx;
				margin: 30rpx ;
			}
			button {
			  display: flex;
			  align-items: center;
			  justify-content: space-around; 
			  border: 1px solid #3c3131;
			  width: 250rpx;
			  background: transparent;
			  height: 90rpx;
			  border-radius: 20rpx;
			  margin-top: 20rpx;
			  left: 10rpx;
			  .login {
				  color: #250808;
				  font-weight: bold;
			      font-size: 40rpx;
			      letter-spacing: 10rpx; /* 调整 letter-spacing 的值 */
			  }
			}
		};
		.virifycode-form{
			position: fixed;
			top: 580rpx;
			margin: 20rpx;
			.phonenumber{
				display: flex;
				align-items: center; 
				justify-content: space-between;
				input{
					border: 1px solid #a79b9b; /* 设置边框为1像素实线，颜色为 #a79b9b */
					border-radius: 6rpx; 
					width: 320rpx; 
					height: 85rpx; 
					margin: 20rpx;
					font-size: 30rpx;
					line-height: 1.5;
					padding-left: 20rpx;
					box-sizing: border-box; 
				};
				.for-verify{
					margin: 10rpx;
					height: 75rpx;
					width: 100rpx;
					margin-right: 18rpx;
					border: 1px solid #3b3021;
					border-radius: 12rpx;
					letter-spacing: 5rpx;
					line-height: 75rpx;
					font-size: 25rpx;
					text-align: center;
				};
				.for-verify:hover{
					transform: scale(1.25);
				}
			};
			.verify-code{
				display: flex;
				align-items: center; 
				justify-content: space-between;
				input{
					border: 1px solid #a79b9b; /* 设置边框为1像素实线，颜色为 #a79b9b */
					border-radius: 6rpx; 
					width: 450rpx; 
					height: 85rpx; 
					margin: 20rpx;
					font-size: 30rpx;
					line-height: 1.5;
					padding-left: 20rpx;
					box-sizing: border-box; 
				}
			};
			image{
				width: 50rpx;
				height: 50rpx;
				margin: 30rpx ;
			};
			button {
			  display: flex;
			  align-items: center;
			  justify-content: space-around; 
			  border: 2px solid #3b1919;
			  width: 300rpx;
			  background: transparent;
			  height: 90rpx;
			  border-radius: 20rpx;
			  margin-top: 10rpx;
			  left: 10rpx;
			  .login {
				  color: #250808;
				  font-weight: bold;
			      font-size: 40rpx;
			      letter-spacing: 10rpx; /* 调整 letter-spacing 的值 */
			  }
			}
		}
		.selution{
			position: fixed;
			margin: 20rpx;
			top: 930rpx;
			font-size: 24rpx;
			line-height: 1.5;
			.forget-secret{
				position: fixed;
				margin: 40rpx;
				left: 60rpx;
				cursor: pointer;
				color: black;
				transition: color 0.5s ease;
				&:hover {
					 font-size: 30rpx;
				     color: #0b207f;
				  }
			};
			.disable-forget-secret{
				position: fixed;
				margin: 40rpx;
				left: 60rpx;
				color: slategray;
			};
			.go-register{
				position: fixed;
				margin: 40rpx;
				left: 500rpx;
				cursor: pointer;
				color: black;
				transition: color 0.5s ease;
				&:hover {
					 font-size: 30rpx;
				     color: #0b207f;
				  }
			};
			
		};
	}
}

</style>
