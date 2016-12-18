<?php

use Laravel\Lumen\Testing\DatabaseTransactions;

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
        $this->post('/register', ['phone' => '111111', 'username' => 'test', 'password' => 'test'])
            ->seeJsonStructure(['result', 'user'])
            ->seeJson(['result' => SUCCEED]);

        //用被使用过的手机号注册
        $this->post('/register', ['phone' => '111111', 'username' => 'test', 'password' => 'test'])
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
        $this->post('/login', ['phone' => '123456', 'password' => 'test'])
            ->seeJsonStructure(['result', 'user'])
            ->seeJson(['result' => SUCCEED]);

        //通过不存在的账号登录
        $this->post('/login', ['phone' => '1234567', 'password' => 'test'])
            ->assertResponseStatus(404);

        //通过错误的密码登录
        $this->post('/login', ['phone' => '123456', 'password' => 'wrong'])
            ->seeJsonStructure(['result', 'error'])
            ->seeJson(['result' => FAILED]);

        //登录参数不全
        $this->post('/login', ['password' => 'test'])
            ->assertResponseStatus(400);

        //登录参数错误
        $this->post('/login', ['phoneNumber' => '123456', 'password' => 'test'])
            ->assertResponseStatus(400);
    }

    public function testGetUser()
    {
        //请求存在的用户信息
        $this->json('GET', '/getUser', ['userId' => 5])
            ->seeJsonStructure(['result', 'user'])
            ->seeJson(['result' => SUCCEED]);

        //请求不存在的用户信息
        $this->json('GET', '/getUser', ['userId' => -1])
            ->assertResponseStatus(404);

        //请求参数错误
        $this->json('GET', '/getUser')
            ->assertResponseStatus(400);
    }

    public function testChangePassword()
    {
        //使用正确的原密码
        $this->post('/changePassword', ['userId' => '5', 'oldPassword' => 'test', 'newPassword' => 'newtest'])
            ->seeJsonEquals(['result' => SUCCEED]);

        //使用错误的原密码
        $this->post('/changePassword', ['userId' => '5', 'oldPassword' => 'wrong', 'newPassword' => 'newtest'])
            ->seeJsonStructure(['result', 'error'])
            ->seeJson(['result' => FAILED]);

        //不存在的用户
        $this->post('/changePassword', ['userId' => '-1', 'oldPassword' => 'test', 'newPassword' => 'newtest'])
            ->assertResponseStatus(404);

        //请求参数错误
        $this->post('/changePassword', ['userId' => '5', 'oldPassword' => 'test'])
            ->assertResponseStatus(400);

        //新密码与旧密码重复
        $this->post('/changePassword', ['userId' => '5', 'oldPassword' => 'test', 'newPassword' => 'test'])
            ->seeJsonStructure(['result', 'error'])
            ->seeJson(['result' => FAILED]);

    }

    public function testUpdateProfile()
    {
        //正确修改资料
        $this->post('/updateProfile',
            ['userId' => '5', 'username' => 'test', 'sex' => '1', 'avatar' => 'smile', 'bio' => '我是炮灰'])
            ->seeJsonStructure(['result', 'user'])
            ->seeJson(['result' => SUCCEED]);

        //参数错误
        $this->post('/updateProfile',
            ['name' => 'test', 'sex' => '1', 'avatar' => 'smile', 'bio' => '我是炮灰'])
            ->assertResponseStatus(400);

        //不存在的用户ID
        $this->post('/updateProfile',
            ['userId' => '-1', 'username' => 'test', 'sex' => '1', 'avatar' => 'smile', 'bio' => '我是炮灰'])
            ->assertResponseStatus(404);
    }

    public function testGetAcceptedTasks()
    {
        //正确请求（不限制返回数量）
        $this->json('GET', '/getAcceptedTasks', ['userId' => '5', 'status' => '1'])
            ->seeJsonStructure(['result', 'tasks'])
            ->seeJson(['result' => SUCCEED]);

        //正确请求（限制返回数量）
        $this->json('GET', '/getAcceptedTasks', ['userId' => '5', 'status' => '1', 'limit' => '5'])
            ->seeJsonStructure(['result', 'tasks'])
            ->seeJson(['result' => SUCCEED]);

        //不存在的用户
        $this->json('GET', '/getAcceptedTasks', ['userId' => '-1', 'limit' => '5'])
            ->assertResponseStatus(404);

        //参数错误
        $this->json('GET', '/getAcceptedTasks', ['user' => '5', 'limit' => '5'])
            ->assertResponseStatus(400);

        //参数类型不正确
        $this->json('GET', '/getAcceptedTasks', ['userId' => '5', 'limit' => 'afweofuoweu'])
            ->seeJsonStructure(['result', 'tasks'])
            ->seeJson(['result' => SUCCEED]);

    }

    public function testGetPublishedTasks()
    {
        //正确请求（不限制返回数量）
        $this->json('GET', '/getPublishedTasks', ['userId' => '5', 'status' => '1'])
            ->seeJsonStructure(['result', 'tasks'])
            ->seeJson(['result' => SUCCEED]);

        //正确请求（限制返回数量）
        $this->json('GET', '/getPublishedTasks', ['userId' => '5', 'status' => '1', 'limit' => '5'])
            ->seeJsonStructure(['result', 'tasks'])
            ->seeJson(['result' => SUCCEED]);

        //不存在的用户
        $this->json('GET', '/getPublishedTasks', ['userId' => '-1', 'limit' => '5'])
            ->assertResponseStatus(404);

        //参数错误
        $this->json('GET', '/getPublishedTasks', ['user' => '5', 'limit' => '5'])
            ->assertResponseStatus(400);

        //参数类型不正确
        $this->json('GET', '/getPublishedTasks', ['userId' => '5', 'limit' => 'afweofuoweu'])
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
        $this->post('/publishTask', ['title' => 'test', 'description' => 'test', 'userId' => '5',
            'deadline' => "2016-12-01 00:00:00", 'longitude' => '0', 'latitude' => '0', 'locationDscp' => 'test',
            'tags' => '["xx","yy"]'])
            ->seeJsonStructure(['result', 'task'])
            ->seeJson(['result' => SUCCEED]);

        //正确请求(信誉值不足)
        $this->post('/publishTask', ['title' => 'test', 'description' => 'test', 'userId' => '5',
            'deadline' => "2016-12-01 00:00:00", 'longitude' => '0', 'latitude' => '0', 'locationDscp' => 'test',
            'tags' => '["xx","yy"]', 'credit' => 100])
            ->seeJsonStructure(['result', 'error'])
            ->seeJson(['result' => FAILED]);


        //参数错误
        $this->post('/publishTask', ['description' => 'test', 'userId' => '5',
            'deadline' => "2016-12-01 00:00:00", 'longitude' => '0', 'latitude' => '0', 'locationDscp' => 'test',
            'tags' => '["xx","yy"]'])
            ->assertResponseStatus(400);

        //参数类型不正确
        $this->post('/publishTask', ['title' => 'test', 'description' => 'test', 'userId' => '5',
            'deadline' => "fweojw", 'longitude' => '0', 'latitude' => '0', 'locationDscp' => 'test',
            'tags' => '["xx","yy"]', 'credit' => 'faeojf'])
            ->seeJsonStructure(['result', 'task'])
            ->seeJson(['result' => SUCCEED]);


        //用户不存在
        $this->post('/publishTask', ['title' => 'test', 'description' => 'test', 'userId' => '-1',
            'deadline' => "2016-12-01 00:00:00", 'longitude' => '0', 'latitude' => '0', 'locationDscp' => 'test',
            'tags' => '["xx","yy"]'])
            ->assertResponseStatus(404);
    }

    public function testViewTask()
    {
        $response = $this->call('GET', '/viewTask', ['taskId' => 1]);
        echo $response;
    }

    public function testAcceptTask()
    {
        $response = $this->call('POST', '/acceptTask', ['taskId' => 1, 'userId' => 5]);
        echo $response;
    }

    public function testDoneTask()
    {
        $response = $this->call('POST', '/doneTask', ['taskId' => 1, 'userId' => 2]);
        echo $response;
    }

    public function testCancelTask()
    {
        $response = $this->call('POST', '/cancelTask', ['taskId' => 39, 'userId' => 35]);
        echo $response;
    }

}
