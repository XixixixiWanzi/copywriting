import request from '@/utils/request'

const base_servise = "dialogue_service/"

export const getTemplateList = () => {
    return request({
        url: base_servise + 'dialogueService/template/getTemplateList',
        method: 'get'
	})
}


export const getKeyWords = (params) => {
    return request({
        url: base_servise + 'dialogueService/dialogue/getKeyWorks?templateId=' + params.templateId + "&text=" + params.text,
        method: 'post'
	})
}

export const getWritingText = (params) => {
    return request({
        url: base_servise + 'dialogueService/dialogue/getWritingText?templateId=' + params.templateId,
        method: 'post',
		data: params.params
	})
}

export const getDialogue = (id) => {
    return request({
        url: base_servise + 'dialogueService/dialogue/getDialogue?id=' + id,
        method: 'get'
	})
}

export const getDialogueList = () => {
    return request({
        url: base_servise + '/dialogueService/dialogue/getDialogueList',
        method: 'get',
	})
}
