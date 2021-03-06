# 哒哒找人 - API


## 用户


### 用户注册
#### 请求方法：POST  
#### 请求url: /register
#### 请求参数

name | type | description | note 
---|---|---|---
phone | string | | required
username | string | | required
password | string | | required

#### 返回示例

```
//成功：
{
    "result":"succeed",
    "user":{
        "userId":1,
        "phone":"12345678912",
        "username":"xxx",
        //以及其他补充属性
    }
}

//失败：
{
    "result":"failed",
    "error":"Something happened"
}
```
---

### 用户登录
#### 请求方法：POST  
#### 请求url: /login
#### 请求参数

name | type | description | note 
---|---|---|---
phone | string | | required
password | string | | required

#### 返回示例

```
//成功：
{
    "result":"succeed",
    "user":{
        "userId":1,
        "phone":"12345678912",
        "username":"xxx",
        //以及其他补充属性
    }
}

//失败：
{
    "result":"failed",
    "error":"Wrong password"
}
```

---

### 获取某个用户信息
#### 请求方法：GET  
#### 请求url: /getUser
#### 请求参数

name | type | description | note 
---|---|---|---
userId | integer | | required

#### 返回示例

```
//成功：
{
    "result":"succeed",
    "user":{
        "userId":1,
        "phone":"12345678912",
        "username":"xxx",
        //以及其他补充属性
    }
}

//失败：
{
    "result":"failed",
    "error":"Something happened"
}
```
---

### 修改密码
#### 请求方法：POST  
#### 请求url: /changePassword
#### 请求参数

name | type | description | note 
---|---|---|---
userId | integer | | required
oldPassword | string | 原密码 | required
newPassword | string | 新密码 | required

#### 返回示例

```
//成功：
{
    "result":"succeed",
}

//失败：
{
    "result":"failed",
    "error":"Something happened"
}
```

---

### 修改个人信息
#### 请求方法：POST  
#### 请求url: /updateProfile
#### 请求参数

name | type | description | note 
---|---|---|---
userId | integer | | required
username | string | | optional
sex | string | | optional
avatar | string | | optional
bio | string | | optional

#### 返回示例

```
//成功：
{
    "result":"succeed",
    "user":{
        "userId":1,
        "phone":"12345678912",
        "username":"xxx",
        //以及其他补充属性
    }
}

//失败：
{
    "result":"failed",
    "error":"Something happened"
}
```
---

### 获取某个用户接受的任务列表（全部/进行中/已完成/确认/取消）
#### 请求方法：GET
#### 请求url: /getAcceptedTasks
#### 请求参数

name | type | description | note 
---|---|---|---
userId | integer | | required
status | integer | 不填写或填写0表示[全部],2表示[进行中],3表示[已完成],4表示[已确认],-1表示[取消]| optional
limit | integer | 限制获取的任务个数 | optional

#### 返回示例

```
//成功：
{
    "result":"succeed",
    "tasks":[
        {
            "taskId":1,
            "title":"求拿快递",
            "description":"啊啊啊求拿快递",
            "publishier":{
                "userId":2,
                "username":"xxx",
                "phone":"123456789012",
                //以及其他补充属性
            }
            "publishedTime":"2016-11-08 12:00:00",
            "deadline":"2016-11-08 18:00:00",
            "location":{
                "longitude":0.00,
                "latitude":0.00
                "description":"阿康烧烤"
            },
            "tags":[
                "拿快递",
                "救命",
            ],
            "credit":5,
            "status":2,
            "accepter":{
                "userId":3,
                "username":"yyy",
                "phone":"123456789012",
                //以及其他补充属性
            }
            //以及其他补充属性
        },
        ...
    ]
    
}

//失败：
{
    "result":"failed",
    "error":"Something happened"
}
```

---

### 获取某个用户发布的任务列表（全部/待接受/进行中/已完成/已确认/取消）
#### 请求方法：GET
#### 请求url: /getPublishedTasks
#### 请求参数

name | type | description | note 
---|---|---|---
userId | integer | | required
status | integer | 不填写或填写0表示[全部],1表示[待接受],2表示[进行中],3表示[已完成],4表示[已确认],-1表示[取消]| optional
limit | integer | 限制获取的任务个数 | optional

#### 返回示例

```
//成功：
{
    "result":"succeed",
    "tasks":[
        {
            "taskId":1,
            "title":"求拿快递",
            "description":"啊啊啊求拿快递",
            "publishier":{
                "userId":2,
                "username":"xxx",
                "phone":"123456789012",
                //以及其他补充属性
            },
            "publishedTime":"2016-11-08 12:00:00",
            "deadline":"2016-11-08 18:00:00",
            "location":{
                "longitude":0.00,
                "latitude":0.00,
                "description":"阿康烧烤"
            },
            "tags":[
                "拿快递",
                "救命",
            ],
            "credit":5,
            "status":2,
            "accepter":{
                "userId":3,
                "username":"yyy",
                "phone":"123456789012",
                //以及其他补充属性
            }
            //以及其他补充属性
        },
        ...
    ]
    
}

//失败：
{
    "result":"failed",
    "error":"Something happened"
}
```
---

## 任务

### 发布任务

#### 请求方法：POST  
#### 请求url: /publishTask
#### 请求参数

name | type | description | note 
---|---|---|---
title | string | | required
description | string | | required
userId | integer | | required
deadline | datetime | | optional
longitude | dobule | | optional
latitude | double | | optional
locationDscp | string | 任务地点的文字描述 | optional
tags | [string] |标签列表 | optional
credit | integer | 完成该任务可获得的信誉值 | optional
#### 返回示例

```
//成功：
{
    "result":"succeed",
    "task":{
        "taskId":1,
        "title":"求拿快递",
        "description":"啊啊啊求拿快递",
        "publishier":{
                "userId":2,
                "username":"xxx",
                "phone":"123456789012",
                //以及其他补充属性
        },
        "publishedTime":"2016-11-08 12:00:00",
        "deadline":"2016-11-08 18:00:00",
        "location":{
            "longitude":0.00,
            "latitude":0.00,
            "description":"阿康烧烤"
        },
        "tags":[
                "拿快递",
                "救命",
        ],
        "credit":5,
        "status":1,
        "accepter":{}
        //以及其他补充属性
    }
}

//失败：
{
    "result":"failed",
    "error":"Something happened"
}

```
---

### 查看某个任务

#### 请求方法：GET  
#### 请求url: /viewTask
#### 请求参数

name | type | description | note 
---|---|---|---
taskId | integer | | required

#### 返回示例

```
//成功：
{
    "result":"succeed",
    "task":{
        "taskId":1,
        "title":"求拿快递",
        "description":"啊啊啊求拿快递",
        "publishier":{
                "userId":2,
                "username":"xxx",
                "phone":"123456789012",
                //以及其他补充属性
        },
        "publishedTime":"2016-11-08 12:00:00",
        "deadline":"2016-11-08 18:00:00",
        "location":{
            "longitude":0.00,
            "latitude":0.00,
            "description":"阿康烧烤"
        },
        "tags":[
                "拿快递",
                "救命",
        ],
        "credit":5,
        "status":2,
        "accepter":{
                "userId":3,
                "username":"yyy",
                "phone":"123456789012",
                //以及其他补充属性
        }
        //以及其他补充属性
    }
}

//失败：
{
    "result":"failed",
    "error":"Something happened"
}

```
---

### 修改任务信息

#### 请求方法：POST  
#### 请求url: /editTask
#### 请求参数

name | type | description | note 
---|---|---|---
taskId | integer | | required
title | string | | optional
description | string | | optional
deadline | datetime | | optional
longitude | dobule | | optional
latitude | double | | optional
locationDscp | string | 任务地点的文字描述 | optional
tags | [string] |标签列表 | optional

#### 返回示例

```
//成功：
{
    "result":"succeed",
    "task":{
        "taskId":1,
        "title":"求拿快递",
        "description":"啊啊啊求拿快递",
        "publishier":{
                "userId":2,
                "username":"xxx",
                "phone":"123456789012",
                //以及其他补充属性
        },
        "publishedTime":"2016-11-08 12:00:00",
        "deadline":"2016-11-08 18:00:00",
        "location":{
            "longitude":0.00,
            "latitude":0.00,
            "description":"阿康烧烤"
        },
        "tags":[
                "拿快递",
                "救命",
        ],
        "credit":5,
        "status":2,
        "accepter":{
             "userId":3,
             "username":"yyy",
             "phone":"123456789012",
             //以及其他补充属性
        }
        //以及其他补充属性
    }
}

//失败：
{
    "result":"failed",
    "error":"Something happened"
}

```
---

### 接受任务
#### 请求方法：POST  
#### 请求url: /acceptTask
#### 请求参数

name | type | description | note 
---|---|---|---
taskId | integer | 要接受的任务id | required
userId | integer | 将要接受任务的用户id | required

#### 返回示例

```
//成功：
{
    "result":"succeed",
}

//失败：
{
    "result":"failed",
    "error":"Something happened"
}

```

---

### 完成任务
#### 请求方法：POST  
#### 请求url: /doneTask
#### 请求参数

name | type | description | note 
---|---|---|---
taskId | integer | 已接受的任务id | required
userId | integer | 接受任务的用户id | required

#### 返回示例

```
//成功：
{
    "result":"succeed",
}

//失败：
{
    "result":"failed",
    "error":"Something happened"
}

```

---

### 确认任务
#### 请求方法：POST  
#### 请求url: /confirmTask
#### 请求参数

name | type | description | note 
---|---|---|---
taskId | integer | 要确认的任务id | required
userId | integer | 发起者的用户id | required

#### 返回示例

```
//成功：
{
    "result":"succeed",
}

//失败：
{
    "result":"failed",
    "error":"Something happened"
}

```

---

### 取消任务
#### 请求方法：POST  
#### 请求url: /cancelTask
#### 请求参数

name | type | description | note 
---|---|---|---
taskId | integer | 要取消的任务id | required
userId | integer | 发起者的用户id | required

#### 返回示例

```
//成功：
{
    "result":"succeed",
}

//失败：
{
    "result":"failed",
    "error":"Something happened"
}

```

---

### 放弃任务
#### 请求方法：POST  
#### 请求url: /quitTask
#### 请求参数

name | type | description | note 
---|---|---|---
taskId | integer | 要放弃的任务id | required
userId | integer | 接受者的用户id | required

#### 返回示例

```
//成功：
{
    "result":"succeed",
}

//失败：
{
    "result":"failed",
    "error":"Something happened"
}

```
