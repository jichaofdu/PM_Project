<?php

/**
 * Created by PhpStorm.
 * User: wsylvia
 * Date: 11/20/16
 * Time: 7:41 PM
 */
class TestApi extends TestCase
{
    public function testRegister()
    {
        $response = $this->call('POST', '/register', ['phone' => '1234', 'username' => 'test', 'password' => 'test']);
        echo $response;
    }

    public function testLogin()
    {
        $response = $this->call('POST', '/login', ['phone' => '12345', 'password' => 'test123']);
        echo $response;
    }

    public function testGetUser()
    {
        $response = $this->call('GET', '/getUser', ['userId' => '2']);
        echo $response;
    }

    public function testChangePassword()
    {
        $response = $this->call('POST', '/changePassword', ['userId' => '1', 'oldPassword' => 'test', 'newPassword' => 'newtest']);
        echo $response;
    }

    public function testUpdateProfile()
    {
        $response = $this->call('POST', '/updateProfile', ['userId' => '1', 'username' => 'test', 'sex' => '1', 'avatar' => 'smile', 'bio' => '呵呵']);
        echo $response;
    }

    public function testGetPublishedTasks()
    {
        $response = $this->call('GET', '/getPublishedTasks', ['userId' => '1', 'limit' => '2']);
        echo $response;
    }

    public function testPublishedTask()
    {
        $response = $this->call('POST', '/publishTask', ['title' => 'test', 'description' => 'test', 'userId' => '1', 'longitude' => '0', 'latitude' => '0', 'locationDscp' => 'test']);
        echo $response;
    }

}
