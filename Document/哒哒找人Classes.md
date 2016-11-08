## 哒哒找人 - Classes


### User

Name | Type | Description
---|---|---
userId | integer | 用户id
phone | string | 手机号
username | string | 用户名
password | string | 密码(hash)
credit | integer | 信用额度 
sex | string | 性别
bio | string | 个人简介



### Task
Name | Type | Description
---|---|---
taskId | integer | 任务id
title | string | 标题
description | string | 任务描述
userId | integer | 任务发起人id
publishedTime | datetime | 任务发布时间
deadline | datetime | 任务截止时间
location | location | 任务目标地点
tags | [string] | 任务标签列表


### Location
Name | Type | Description
---|---|---
locationId | integer | 地点id
longtitude | double | 经度
latitude | double | 纬度
description | string | 地点描述

---

//不确定以下是否需要
### Tag
Name | Type | Description
---|---|---
tagId | integer | 标签id
name | string | 标签名
