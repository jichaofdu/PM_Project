## 哒哒找人 - Classes


### User

Name | Type | Description
---|---|---
userId | integer | 用户id
phone | string | 手机号
username | string | 用户名
password | string | 密码(hash)
credit | integer | 信用额度 
sex | integer | 性别(1,2,3)
avatar | string | 头像
bio | string | 个人简介



### Task
Name | Type | Description
---|---|---
taskId | integer | 任务id
title | string | 标题
description | string | 任务描述
publisher | user | 任务发起人
publishedTime | datetime | 任务发布时间
deadline | datetime | 任务截止时间
location | location | 任务目标地点
tags | [string] | 任务标签列表
credit | integer | 完成任务可以得到的信誉奖励
status | integer | 1:开放申请, 2:进行中, 3:已完成, -1:取消
accepter | user | 任务接受者


### Location
Name | Type | Description
---|---|---
longitude | double | 经度
latitude | double | 纬度
description | string | 地点描述

