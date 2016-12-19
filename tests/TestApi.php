<?php

use Laravel\Lumen\Testing\DatabaseTransactions;

require 'test_config.php';
/**
 * Created by PhpStorm.
 * User: wsylvia
 * Date: 11/20/16
 * Time: 7:41 PM
 */
class TestApi extends TestCase
{

    use DatabaseTransactions;

    public function testRegister()
    {

        //用新手机号注册
        $this->post('/register', ['phone' => USER_PHONE_NEW, 'username' => 'test', 'password' => 'test'])
            ->seeJsonStructure(['result', 'user'])
            ->seeJson(['result' => SUCCEED]);

        //用被使用过的手机号注册
        $this->post('/register', ['phone' => USER_PHONE_NEW, 'username' => 'test', 'password' => 'test'])
            ->seeJsonStructure(['result', 'error'])
            ->seeJson(['result' => FAILED]);

        //注册信息不全
        $this->post('/register', ['username' => 'test', 'password' => 'test'])
            ->assertResponseStatus(400);

        //注册信息项错误
        $this->post('/register', ['phone' => '222222', 'name' => 'test', 'password' => 'test'])
            ->assertResponseStatus(400);
    }

    public function testLogin()
    {
        //通过正确的账号密码登录
        $this->post('/login', ['phone' => USER_PHONE_VALID, 'password' => USER_PASSWORD_VALID])
            ->seeJsonStructure(['result', 'user'])
            ->seeJson(['result' => SUCCEED]);

        //通过不存在的账号登录
        $this->post('/login', ['phone' => USER_PHONE_INVALID, 'password' => 'test'])
            ->assertResponseStatus(404);

        //通过错误的密码登录
        $this->post('/login', ['phone' => USER_PHONE_VALID, 'password' => 'wrong'])
            ->seeJsonStructure(['result', 'error'])
            ->seeJson(['result' => FAILED]);

        //登录参数不全
        $this->post('/login', ['password' => 'test'])
            ->assertResponseStatus(400);

        //登录参数错误
        $this->post('/login', ['phoneNumber' => USER_PHONE_VALID, 'password' => 'test'])
            ->assertResponseStatus(400);
    }

    public function testGetUser()
    {
        //请求存在的用户信息
        $this->json('GET', '/getUser', ['userId' => USER_ID_VALID])
            ->seeJsonStructure(['result', 'user'])
            ->seeJson(['result' => SUCCEED]);

        //请求不存在的用户信息
        $this->json('GET', '/getUser', ['userId' => USER_ID_INVALID])
            ->assertResponseStatus(404);

        //请求参数错误
        $this->json('GET', '/getUser')
            ->assertResponseStatus(400);
    }

    public function testChangePassword()
    {
        //使用正确的原密码
        $this->post('/changePassword', ['userId' => USER_ID_VALID, 'oldPassword' => USER_PASSWORD_VALID, 'newPassword' => 'newtest'])
            ->seeJsonEquals(['result' => SUCCEED]);

        //使用错误的原密码
        $this->post('/changePassword', ['userId' => USER_ID_VALID, 'oldPassword' => 'wrong', 'newPassword' => 'newtest'])
            ->seeJsonStructure(['result', 'error'])
            ->seeJson(['result' => FAILED]);

        //不存在的用户
        $this->post('/changePassword', ['userId' => USER_ID_INVALID, 'oldPassword' => USER_PASSWORD_VALID, 'newPassword' => 'newtest'])
            ->assertResponseStatus(404);

        //请求参数错误
        $this->post('/changePassword', ['userId' => USER_ID_VALID, 'oldPassword' => USER_PASSWORD_VALID])
            ->assertResponseStatus(400);

        //新密码与旧密码重复
        $this->post('/changePassword', ['userId' => USER_ID_VALID, 'oldPassword' => 'test123', 'newPassword' => 'test123'])
            ->seeJsonStructure(['result', 'error'])
            ->seeJson(['result' => FAILED]);

    }

    public function testUpdateProfile()
    {
        //正确修改资料
        $this->post('/updateProfile',
            ['userId' => USER_ID_VALID, 'username' => 'test', 'sex' => '1', 'avatar' => 'smile', 'bio' => '我是炮灰'])
            ->seeJsonStructure(['result', 'user'])
            ->seeJson(['result' => SUCCEED]);

        //参数错误
        $this->post('/updateProfile',
            ['name' => 'test', 'sex' => '1', 'avatar' => 'smile', 'bio' => '我是炮灰'])
            ->assertResponseStatus(400);

        //不存在的用户ID
        $this->post('/updateProfile',
            ['userId' => USER_ID_INVALID, 'username' => 'test', 'sex' => '1', 'avatar' => 'smile', 'bio' => '我是炮灰'])
            ->assertResponseStatus(404);
    }

    public function testGetAcceptedTasks()
    {
        //正确请求（不限制返回数量）
        $this->json('GET', '/getAcceptedTasks', ['userId' => USER_ID_VALID, 'status' => '1'])
            ->seeJsonStructure(['result', 'tasks'])
            ->seeJson(['result' => SUCCEED]);

        //正确请求（限制返回数量）
        $this->json('GET', '/getAcceptedTasks', ['userId' => USER_ID_VALID, 'status' => '1', 'limit' => '5'])
            ->seeJsonStructure(['result', 'tasks'])
            ->seeJson(['result' => SUCCEED]);

        //不存在的用户
        $this->json('GET', '/getAcceptedTasks', ['userId' => USER_ID_INVALID, 'limit' => '5'])
            ->assertResponseStatus(404);

        //参数错误
        $this->json('GET', '/getAcceptedTasks', ['user' => USER_ID_VALID, 'limit' => '5'])
            ->assertResponseStatus(400);

        //参数类型不正确
        $this->json('GET', '/getAcceptedTasks', ['userId' => USER_ID_VALID, 'limit' => 'afweofuoweu'])
            ->seeJsonStructure(['result', 'tasks'])
            ->seeJson(['result' => SUCCEED]);

    }

    public function testGetPublishedTasks()
    {
        //正确请求（不限制返回数量）
        $this->json('GET', '/getPublishedTasks', ['userId' => USER_ID_VALID, 'status' => '1'])
            ->seeJsonStructure(['result', 'tasks'])
            ->seeJson(['result' => SUCCEED]);

        //正确请求（限制返回数量）
        $this->json('GET', '/getPublishedTasks', ['userId' => USER_ID_VALID, 'status' => '1', 'limit' => '5'])
            ->seeJsonStructure(['result', 'tasks'])
            ->seeJson(['result' => SUCCEED]);

        //不存在的用户
        $this->json('GET', '/getPublishedTasks', ['userId' => USER_ID_INVALID, 'limit' => '5'])
            ->assertResponseStatus(404);

        //参数错误
        $this->json('GET', '/getPublishedTasks', ['user' => USER_ID_VALID, 'limit' => '5'])
            ->assertResponseStatus(400);

        //参数类型不正确
        $this->json('GET', '/getPublishedTasks', ['userId' => USER_ID_VALID, 'limit' => 'afweofuoweu'])
            ->seeJsonStructure(['result', 'tasks'])
            ->seeJson(['result' => SUCCEED]);

    }

    public function testGetTasksAround()
    {
        //正确请求
        $this->json('GET', '/getTasksAround', ['lon' => '116.5', 'lat' => '39.5', 'radius' => '33396'])
            ->seeJsonStructure(['result', 'tasks'])
            ->seeJson(['result' => SUCCEED]);

        //参数错误
        $this->json('GET', '/getTasksAround', ['lat' => '39.5', 'radius' => '33396'])
            ->assertResponseStatus(400);

        //参数类型不正确
        $this->json('GET', '/getTasksAround', ['lon' => 'fofwe', 'lat' => '39.5', 'radius' => '33396'])
            ->assertResponseStatus(400);
    }

    public function testPublishTask()
    {
        //正确请求(信誉值充足)
        $this->post('/publishTask', ['title' => 'test', 'description' => 'test', 'userId' => USER_ID_VALID,
            'deadline' => "2016-12-01 00:00:00", 'longitude' => '0', 'latitude' => '0', 'locationDscp' => 'test',
            'tags' => '["xx","yy"]'])
            ->seeJsonStructure(['result', 'task'])
            ->seeJson(['result' => SUCCEED]);

        //正确请求(信誉值不足)
        $this->post('/publishTask', ['title' => 'test', 'description' => 'test', 'userId' => USER_ID_VALID,
            'deadline' => "2016-12-01 00:00:00", 'longitude' => '0', 'latitude' => '0', 'locationDscp' => 'test',
            'tags' => '["xx","yy"]', 'credit' => 100])
            ->seeJsonStructure(['result', 'error'])
            ->seeJson(['result' => FAILED]);

        //tag类型不正确
        $this->post('/publishTask', ['title' => 'test', 'description' => 'test', 'userId' => USER_ID_VALID,
            'longitude' => '0', 'latitude' => '0', 'locationDscp' => 'test',
            'tags' => '[jojoj]'])
            ->seeJsonStructure(['result', 'error'])
            ->seeJson(['result' => FAILED]);

        //deadline和credit类型不正确
        $this->post('/publishTask', ['title' => 'test', 'description' => 'test', 'userId' => USER_ID_VALID,
            'deadline' => "fweojw", 'longitude' => '0', 'latitude' => '0', 'locationDscp' => 'test',
            'tags' => '["xx","yy"]', 'credit' => 'faeojf'])
            ->seeJsonStructure(['result', 'task'])
            ->seeJson(['result' => SUCCEED]);

        //参数错误
        $this->post('/publishTask', ['description' => 'test', 'userId' => USER_ID_VALID,
            'deadline' => "2016-12-01 00:00:00", 'longitude' => '0', 'latitude' => '0', 'locationDscp' => 'test',
            'tags' => '["xx","yy"]'])
            ->assertResponseStatus(400);

        //用户不存在
        $this->post('/publishTask', ['title' => 'test', 'description' => 'test', 'userId' => USER_ID_INVALID,
            'deadline' => "2016-12-01 00:00:00", 'longitude' => '0', 'latitude' => '0', 'locationDscp' => 'test',
            'tags' => '["xx","yy"]'])
            ->assertResponseStatus(404);
    }

    public function testViewTask()
    {
        //正确请求
        $this->json('GET', '/viewTask', ['taskId' => TASK_ID_VALID])
            ->seeJsonStructure(['result', 'task'])
            ->seeJson(['result' => SUCCEED]);

        //请求不存在的任务
        $this->json('GET', '/viewTask', ['taskId' => TASK_ID_INVALID])
            ->assertResponseStatus(404);

        //请求参数错误
        $this->json('GET', '/viewTask')
            ->assertResponseStatus(400);
    }

    public function testAcceptTask()
    {
        //接受开放申请的任务
        $this->post('/acceptTask', ['taskId' => TASK_ID_PENDING, 'userId' => USER_ID_VALID])
            ->seeJsonEquals(['result' => SUCCEED]);

        //接受非开放申请的任务
        $this->post('/acceptTask', ['taskId' => TASK_ID_UNDERWAY, 'userId' => USER_ID_VALID])
            ->seeJsonStructure(['result', 'error'])
            ->seeJson(['result' => FAILED]);

        //任务不存在
        $this->post('/acceptTask', ['taskId' => TASK_ID_INVALID, 'userId' => USER_ID_VALID])
            ->assertResponseStatus(404);

        //错误参数
        $this->post('/acceptTask', ['taskId' => TASK_ID_PENDING])
            ->assertResponseStatus(400);

        //错误用户
        $this->post('/acceptTask', ['taskId' => TASK_ID_PENDING, 'userId' => USER_ID_INVALID])
            ->assertResponseStatus(404);

        //接受者与发布者一致
        $this->post('/acceptTask', ['taskId' => TASK_ID_PENDING, 'userId' => PUBLISHER_PENDING])
            ->assertResponseStatus(403);
    }

    public function testDoneTask()
    {
        //完成进行中的任务
        $this->post('/doneTask', ['taskId' => TASK_ID_UNDERWAY, 'userId' => ACCEPTER_UNDERWAY])
            ->seeJsonEquals(['result' => SUCCEED]);

        //完成非进行中的任务
        $this->post('/doneTask', ['taskId' => TASK_ID_DONE, 'userId' => ACCEPTER_DONE])
            ->seeJsonStructure(['result', 'error'])
            ->seeJson(['result' => FAILED]);

        //非接受者完成任务
        $this->post('/doneTask', ['taskId' => TASK_ID_UNDERWAY, 'userId' => ACCEPTER_DONE])
            ->assertResponseStatus(403);

        //任务不存在
        $this->post('/doneTask', ['taskId' => TASK_ID_INVALID, 'userId' => USER_ID_VALID])
            ->assertResponseStatus(404);

        //不存在的用户
        $this->post('/doneTask', ['taskId' => TASK_ID_UNDERWAY, 'userId' => USER_ID_INVALID])
            ->assertResponseStatus(403);

        //错误参数
        $this->post('/doneTask', ['taskId' => TASK_ID_UNDERWAY])
            ->assertResponseStatus(400);
    }

    public function testConfirmTask()
    {
        //确认已完成的任务
        $this->post('/confirmTask', ['taskId' => TASK_ID_DONE, 'userId' => PUBLISHER_DONE])
            ->seeJsonEquals(['result' => SUCCEED]);

        //确认非已完成的任务
        $this->post('/confirmTask', ['taskId' => TASK_ID_UNDERWAY, 'userId' => PUBLISHER_UNDERWAY])
            ->seeJsonStructure(['result', 'error'])
            ->seeJson(['result' => FAILED]);

        //任务不存在
        $this->post('/confirmTask', ['taskId' => TASK_ID_INVALID, 'userId' => USER_ID_VALID])
            ->assertResponseStatus(404);

        //非发布者确认任务
        $this->post('/confirmTask', ['taskId' => TASK_ID_DONE, 'userId' => ACCEPTER_DONE])
            ->assertResponseStatus(403);

        //不存在的用户
        $this->post('/confirmTask', ['taskId' => TASK_ID_DONE, 'userId' => USER_ID_INVALID])
            ->assertResponseStatus(403);

        //错误参数
        $this->post('/confirmTask', ['taskId' => TASK_ID_DONE])
            ->assertResponseStatus(400);


    }

    public function testCancelTask()
    {
        //取消开放申请的任务
        $this->post('/cancelTask', ['taskId' => TASK_ID_PENDING, 'userId' => PUBLISHER_PENDING])
            ->seeJsonEquals(['result' => SUCCEED]);

        //取消进行中的任务
        $this->post('/cancelTask', ['taskId' => TASK_ID_UNDERWAY, 'userId' => PUBLISHER_UNDERWAY])
            ->seeJsonEquals(['result' => SUCCEED]);

        //取消已完成的任务
        $this->post('/cancelTask', ['taskId' => TASK_ID_DONE, 'userId' => PUBLISHER_DONE])
            ->seeJsonStructure(['result', 'error'])
            ->seeJson(['result' => FAILED]);

        //不存在的任务
        $this->post('/cancelTask', ['taskId' => TASK_ID_INVALID, 'userId' => USER_ID_VALID])
            ->assertResponseStatus(404);

        //非发布者取消任务
        $this->post('/cancelTask', ['taskId' => TASK_ID_UNDERWAY, 'userId' => ACCEPTER_UNDERWAY])
            ->assertResponseStatus(403);

        //不存在的用户
        $this->post('/cancelTask', ['taskId' => TASK_ID_UNDERWAY, 'userId' => USER_ID_INVALID])
            ->assertResponseStatus(404);

        //参数错误
        $this->post('/cancelTask', ['taskId' => TASK_ID_PENDING])
            ->assertResponseStatus(400);
    }

    public function testQuitTask()
    {
        //放弃进行中的任务(信誉值不足)
        $this->post('/quitTask', ['taskId' => TASK_ID_UNDERWAY, 'userId' => ACCEPTER_UNDERWAY])
            ->seeJsonStructure(['result', 'error'])
            ->seeJson(['result' => FAILED]);

        //放弃已完成的任务
        $this->post('/quitTask', ['taskId' => TASK_ID_DONE, 'userId' => ACCEPTER_DONE])
            ->seeJsonStructure(['result', 'error'])
            ->seeJson(['result' => FAILED]);

        //不存在的任务
        $this->post('/quitTask', ['taskId' => TASK_ID_INVALID, 'userId' => USER_ID_VALID])
            ->assertResponseStatus(404);

        //非接受者放弃任务
        $this->post('/quitTask', ['taskId' => TASK_ID_UNDERWAY, 'userId' => PUBLISHER_UNDERWAY])
            ->assertResponseStatus(403);

        //不存在的用户
        $this->post('/quitTask', ['taskId' => TASK_ID_UNDERWAY, 'userId' => USER_ID_INVALID])
            ->assertResponseStatus(404);

        //参数错误
        $this->post('/quitTask', ['taskId' => TASK_ID_UNDERWAY])
            ->assertResponseStatus(400);
    }

}
