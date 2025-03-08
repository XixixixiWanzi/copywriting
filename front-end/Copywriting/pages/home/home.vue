<template>
	<view class="content" :style="{ backgroundImage: 'linear-gradient(rgba(255, 255, 255, 0.6), rgba(255, 255, 255, 0.6)), url(' + imagesrc + ')'}">
		<view class="top">
			<image src="../../static/dict.png"  @click="showDrawer" ></image>
			<span></span>
			<image src="../../static/ucenter.png"  @click="gotoSetting"></image>		
		</view>
		<view class="drawer">
			<uni-drawer ref="showLeft" mode="left" :mask-click="true" :width="260">
				<view class="body" :style="{ backgroundImage: 'linear-gradient(rgba(255, 255, 255, 0.5), rgba(255, 255, 255, 0.5)), url(' + imagesrc + ')'}">
					<view class="title">模板列表</view>
					<scroll-view style="height: 70%;" scroll-y="true">
						<view v-for="item in promptList" :key="item.id" class="text-box">
						    <view class="sum">
								<view class="sum-text" @click="showText(item.id)" >{{ item.templateName }}</view>
								<image src="../../static/right.png" @click="selectId = item.id"></image>
							</view> 
							<p v-if="item.id === activeId" @click="showText(item.id)" >{{ item.content }}</p>
						</view>
					</scroll-view>
					<view class="bottom">
						<view class="view1">当前选项：</view>
						<view class="view2"><span></span>{{ getSelectedTemplateName() }}</view>
					</view>
				</view>
			</uni-drawer>
		</view>
		<view class="backgroundText"><p>{{getSelectedTemplateContent()}}</p></view>
		
		<scroll-view  :style="{height: `${windowHeight-inputHeight - 180}rpx`}" id="scrollview" scroll-y="true" :scroll-top="scrollTop" class="scroll-view">
			<!-- 聊天主体 -->
			<view id="msglistview" class="chat-body">
				<!-- 聊天记录 -->
				<view v-for="(item,index) in msgList" :key="index">
					<!-- 自己发的消息 -->
					<view class="item self" v-if="item.userContent != ''" >
						<view class="dialogue right">{{item.userContent}}</view>
						<image class="avatar" :src="info ? info.avatarUrl : null" mode='aspectFill'></image>
					</view>
					
					<!-- 机器人发的消息 -->
					<view class="item Ai" v-if="item.botContent != ''">   
						<image class="avatar" src="../../static/ai.png"></image>
						<view class="dialogue left">{{item.botContent}}</view>
					</view>
					
					<!-- 选择框 -->
					<view class="check" v-if="check && item.botContent === '' && item.userContent === ''">
						<view v-for="([key, value]) in Object.entries(keywordsList)" :key="key">
							<view class="box">
								<view class="key">{{ key }}</view>
								<radio-group :key="key" @change="handleRadioChange($event,key)" class="radio-group">
								  <radio v-for="(option, index) in value" :key="index" :value="option">{{ option }}</radio>
								</radio-group>
							</view>
						</view>
					</view>
				</view>
			</view>
		</scroll-view>
		<!-- 底部消息发送栏 -->
		<!-- 用来占位，防止聊天消息被发送框遮挡 -->
		<view class="chat-bottom" :style="{height: `${inputHeight}rpx`}">
			<view class="send-msg" :style="{bottom:`${keyboardHeight - 60}rpx`}">
				<view class="uni-textarea">
					<textarea v-model="chatMsg"
						maxlength="1000"
						confirm-type="send"
						@confirm="handleSend"
						placeholder="请输入"
						:show-confirm-bar="false"
						:adjust-position="false"
						@linechange="sendHeight"
						@focus="focus" @blur="blur"
					    auto-height>
					</textarea>
				</view>
				<button v-if="!check" @click="handleSend1" class="send-btn">发送</button>
				<button v-if="check" @click="handleSend2" class="send-btn">确认</button>
			</view>
		</view>	
	</view>
</template>


<style lang="scss" scoped>
	view,button,text,input,textarea {
		margin: 0;
		padding: 0;
		box-sizing: border-box;
	}
	.content{
		background-size: cover;
		background-position: center;
		width: 100%;
		height: 100vh; 
		display: flex;
		align-items: center;
		flex-direction: column;
		overflow: hidden;
		.top{
			margin: 60rpx;
			margin-bottom: 10rpx;
			display: flex;
			align-items: center;
			justify-content: center;
			flex-direction: row;
			border-bottom: 3rpx solid rgba(200, 200, 200,0.9);
			image{
				width: 50rpx;
				height: 50rpx;
				margin: 20rpx;
			};
			span{
				margin-left: 250rpx;
				margin-right: 250rpx;
			}
		}
		.drawer{
			.body{
				display: flex;
				background-size: cover;
				background-position: center;
				flex-direction: column;
				filter: none;
				width: 100%;
				height: 100%; 
				.title{
					font-size: 48rpx;
					margin: 50rpx;
					margin-top: 80rpx;					
					color: rgb(100, 100, 100);
					text-align: center; /* 文本水平居中对齐 */
				};
				scroll-view{
					::-webkit-scrollbar {
							    display: none;
							    width: 0 !important;
							    height: 0 !important;
							    -webkit-appearance: none;
							    background: transparent;
							    color: transparent;
							  }
					margin-left: 20rpx;
					margin-right: 20rpx;
					width: 450rpx;
					background-color: rgba(240, 240, 240,0.6);
					border-radius: 40rpx;
					.text-box {
						display: flex;
						flex-direction: column;
						margin-left: 20rpx;
						width: 450rpx;
						height: auto;
						.sum{
							margin-left: 40rpx;
							margin-right: 40rpx;
							border-bottom: 1px solid rgb(30, 30, 30);
							display: flex;
							height: 80rpx;
							justify-content: space-between;
							align-items: center; /* 如果需要垂直居中，可以添加这行 */
							.sum-text{
								font-size: 34rpx;
								color: rgb(80, 80, 80);
							};
							image{
								width: 32rpx;
								height: 20rpx;
							};
						};
						p{
							width: 340rpx;
							color: rgb(150, 150, 150);
							background-color: rgba(240, 230, 230);
							border-radius: 15rpx;
							padding: 30rpx;
							font-size: 24rpx;
							margin-bottom: 20rpx;
							margin-left: 30rpx;
						}
					}
				}
				.bottom{
					margin: 30rpx;
					letter-spacing: 3rpx;
					.view1{
						margin: 20rpx;
					};
					.view2{
						font-size: 38rpx;
						margin: 20rpx;
						span{
							margin-right: 60rpx;
						}
					};
					
				}
			}
		}
		.backgroundText{
			position: fixed;
			top:700rpx;
			left:50rpx;
			right:50rpx;
			height: auto;
			max-width: 80%;
			padding: 20rpx;
			background-color: rgba(240, 225, 225, 0.4);
			border-radius: 30rpx;
			text-align: center;
			p{
				margin: 40rpx;
				color: rgba(100, 100, 100, 0.5);
				font-size: 30rpx;
				line-height: 1.5;
			}
		}
		.scroll-view {
			::-webkit-scrollbar {
					    display: none;
					    width: 0 !important;
					    height: 0 !important;
					    -webkit-appearance: none;
					    background: transparent;
					    color: transparent;
					  }
			
			
			.chat-body {
				.self {
					justify-content: flex-end;
				}
				.item {
					display: flex;
					padding: 23rpx 30rpx;
					.right {
						background-color: rgb(240, 220, 210);
					}
					.left {
						background-color: #FFFFFF;
					}
					.center{
						background-color: rgba(210,220,220,0.8);
					}
		            // 聊天消息的三角形
					.right::after {
						position: absolute;
						display: inline-block;
						content: '';
						width: 0;
						height: 0;
						left: 100%;
						top: 10px;
						border: 12rpx solid transparent;
						border-left: 12rpx solid rgba(220, 180, 170, 0.6);
					}
		 
					.left::after {
						position: absolute;
						display: inline-block;
						content: '';
						width: 0;
						height: 0;
						top: 10px;
						right: 100%;
						border: 12rpx solid transparent;
						border-right: 12rpx solid #FFFFFF;
					}
		 
					.dialogue {
						position: relative;
						max-width: 486rpx;
						border-radius: 12rpx;
						word-break: break-all;
						word-wrap: break-word;
						white-space: pre-wrap; 
						padding: 24rpx;
						margin: 0 24rpx;
						border-radius: 5px;
						font-size: 32rpx;
						font-family: PingFang SC;
						font-weight: 500;
						color: #202020;
						line-height: 42rpx;
					}
		 
					.avatar {
						display: flex;
						justify-content: center;
						width: 100rpx;
						height: 100rpx;
						border-radius: 50rpx;
						overflow: hidden;
						
						image {
							align-self: center;
						}
		 
					}
				}
				.check{
					height: auto;
					display: flex;
					flex-direction: column;
					background-color: rgb(180, 180, 180);
					border-radius: 50rpx;
					margin: 30rpx;
					.box{
						width: max-content;
						margin: 20rpx;
						height: auto;
						display: flex;
						justify-content: center;
						align-items: center;
						font-size: 32rpx;
						.key{
							text-align: center;
							padding-left: 30rpx;
							padding-right: 30rpx;
							padding-top: 20rpx;
							padding-bottom: 20rpx;
							border-radius: 20rpx;
							background-color: rgb(240, 100, 100);
							color: #EEEEEE;
						};
						.radio-group{
							display: flex;
							justify-content: center;
							align-items: center;
							margin: 5rpx;
							overflow-x: auto;
							white-space: nowrap;
							radio{
								margin-left: 15rpx;
								margin-right: 15rpx;
							}
						};
						.radio-group::-webkit-scrollbar {
						  display: none; /* 隐藏滚动条 */
						}
					}
				}
			}
		}
		 
		/* 底部聊天发送栏 */
		.chat-bottom {
			width: 100%;
			height: 100rpx;
			background: #F4F5F7;
			transition: all 0.1s ease;
			
			.send-msg {
				display: flex;
				align-items: flex-end;
				padding: 16rpx 30rpx;
				width: 100%;
				min-height: 177rpx;
				position: fixed;
				bottom: 0;
				background: #fff;
				transition: all 0.1s ease;
			}
		 
			.uni-textarea {
				padding-bottom: 70rpx;  
				textarea {
					width: 537rpx;
					min-height: 75rpx;
					max-height: 500rpx;
					background: #f1f1f1;
					border-radius: 40rpx;
					font-size: 32rpx;
					font-family: PingFang SC;
					color: #333333;
					line-height: 74rpx;
					padding: 5rpx 8rpx;
					text-indent: 30rpx;
				}
			}
		    
			.send-btn {
				display: flex;
				align-items: center;
				justify-content: center;
				margin-bottom: 76rpx;
				margin-left: 25rpx;
				width: 120rpx;
				height: 75rpx;
				background: #ed5a65;
				border-radius: 50rpx;
				font-size: 28rpx;
				font-family: PingFang SC;
				font-weight: 500;
				color: #FFFFFF;
				line-height: 28rpx;
			}
		}
	}
					// display: flex;
					// align-items: center; /* 文本垂直居中对齐 */
					// justify-content: center; /* 文本水平居中对齐 */
</style>

<script>
import { getWritingText, getTemplateList, getKeyWords } from '@/api/dialogue/dialogue.js';
import { getInfo } from "@/api/ucenter/ucenter.js"
	export default {
		data(){
			return{
				imagesrc: "https://copywriting001.oss-cn-beijing.aliyuncs.com/default-resource/log.png",
				promptList: null,
				activeId: null,
				selectId: null,
				info: null,
				check: false,
				keywordsList: null,
				selectedValues: {},
				keyboardHeight:0,
				bottomHeight: 0,
				scrollTop: 0,
				userId:'',
				chatMsg:"",
				msgList:[
					{
						botContent: "你好啊，很高兴你可以关注我，请问我有什么可以帮助你的吗？",
						userContent: "",
					},
					{
						botContent: "",
						userContent: "你好呀，非常高兴认识你",
					},
				]	
			}
		},
		async created() {
			await this.loadTemplateList();
			await this.loadInfo();
		},
	
		computed: {
			selectedTemplate() {
				return this.promptList && this.promptList.find(item => item.id === this.selectId);
			},
			
			windowHeight() {
			    return this.rpxTopx(uni.getSystemInfoSync().windowHeight)
			},
			// 键盘弹起来的高度+发送框高度
			inputHeight(){
				return this.bottomHeight+this.keyboardHeight - 30
			}
			
		},
		onLoad(){
			uni.onKeyboardHeightChange(res => {
				this.keyboardHeight = this.rpxTopx(res.height)
				if(this.keyboardHeight<0)this.keyboardHeight = 0;
			})
		},
		onUnload(){
			uni.offKeyboardHeightChange()
		},
		methods: {
			gotoSetting(){
				uni.navigateTo({
					url:"/pages/user_detail/user_detail"
				})
			},
			async loadTemplateList() {
			    try {
			      const response = await getTemplateList();
			      if (response.code === 2000) {
			        this.promptList = response.data.templates;
			        this.selectId = this.promptList[0].id;
			      }
			    } catch (error) {
			      uni.showToast({ title: '模板列表请求失败', duration: 2000, icon: "none" });
			    }
			},
			async loadInfo() {
			    try {
			      const response = await getInfo();
			      if (response.code === 2000) {
			        this.info = response.data.info;
			        console.log(this.info);
			      }
			    } catch (error) {
			      uni.showToast({ title: '信息请求失败', duration: 2000, icon: "none" });
			    }
			},
			
			getSelectedTemplateName() {
			      return this.selectedTemplate ? this.selectedTemplate.templateName : "";
			},
			getSelectedTemplateContent() {
			      return this.selectedTemplate ? this.selectedTemplate.content : "";
			},
			showDrawer() {
				this.$refs.showLeft.open();
			},
			closeDrawer() {
				this.$refs.showLeft.close();
			},
			showText(id) {
			       this.activeId = this.activeId === id ? null : id;
			},
			
					
			focus(){
					this.scrollToBottom()
			},
			blur(){
				this.scrollToBottom()
			},
			rpxTopx(px){
				let deviceWidth = uni.getSystemInfoSync().windowWidth
				let rpx = ( 750 / deviceWidth ) * Number(px)
				return Math.floor(rpx)
			},
			sendHeight(){
				setTimeout(()=>{
					let query = uni.createSelectorQuery();
					query.select('.send-msg').boundingClientRect()
					query.exec(res =>{
						this.bottomHeight = this.rpxTopx(res[0].height)
					})
				},10)
			},
			scrollToBottom(e){
				setTimeout(()=>{
					let query = uni.createSelectorQuery().in(this);
					query.select('#scrollview').boundingClientRect();
					query.select('#msglistview').boundingClientRect();
					query.exec((res) =>{
						if(res[1].height > res[0].height){
							this.scrollTop = this.rpxTopx(res[1].height - res[0].height)
						}
					})
				},15)
			},

			handleRadioChange(event,key) {
				console.log(key);
			  const value = event.detail.value;
			  this.selectedValues[key] = value;
			  console.log(this.selectedValues);
			},
			// 冬至节到了，我想给我妈妈送去节日祝福。
			// 冬至节，春节，妈妈，爸爸，老师
			handleSend1() {
				//如果消息不为空
				if(!this.chatMsg||!/^\s+$/.test(this.chatMsg)){
					let obj = {
						botContent: "",
						userContent: this.chatMsg,
					}
					this.receiveMsg(this.chatMsg);  
					this.msgList.push(obj);
					this.chatMsg = '';
					this.scrollToBottom();
					
				}else {
					uni.showToast({ title: '发送信息不能为空', duration: 1000, icon: "none" });
				}
			},
			handleSend2() {
				const selectedValuesCount = Object.values(this.selectedValues).length;
				const keywordsListCount = Object.values(this.keywordsList).length;
				if (selectedValuesCount === keywordsListCount) {
					var result = "";
					for (var key in this.selectedValues) {
					    if (this.selectedValues.hasOwnProperty(key)) {
					        result += key + ": " + this.selectedValues[key] + ", ";
					    }
					}
					
					// 去除最后一个逗号和空格
					result = result.slice(0, -2);

					let obj = {
						botContent: "",
						userContent: result,
					}
					this.receiveMsg(this.selectedValues);
					this.msgList.push(obj);
					this.chatMsg = '';
					this.scrollToBottom();
				}else {
					uni.showToast({ title: '请选择完整', duration: 1000, icon: "none" });
				}
			},
			
			async receiveMsg(query){
				const params ={
					"templateId": this.selectId
				};
				try{
					if(!this.check){
						params["text"] = query;
						const res = await getKeyWords(params)
						if (res.code === 2000) {
							this.keywordsList = res.data.data;
							this.check = true;							
						}else{
							uni.showToast({ title: '请重试', duration: 1000, icon: "none" });
						}
						let obj = {
							botContent: "",
							userContent: "",
						}
						this.msgList.push(obj);
						this.scrollToBottom();
						
					}else{
						params["params"] = query;
						const res = await getWritingText(params);
						
						if (res.code === 2000) {
							console.log(res.data.text);
							let obj = {
								botContent: res.data.text,
								userContent: "",
							}
							this.msgList.push(obj);
							this.chatMsg = '';
							this.scrollToBottom();
							this.check = false;
						}else{
							uni.showToast({ title: '请重试2', duration: 1000, icon: "none" });
						}
					}
				}catch (error) {
				  uni.showToast({ title: '请检查网络', duration: 1000, icon: "none" });
				}
			}
			
			
		}
	}
</script>