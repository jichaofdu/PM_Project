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
        $response = $this->call('POST', '/login', ['phone' => '1234', 'password' => 'test123']);
        echo $response;
    }

    public function testGetUser()
    {
        $response = $this->call('GET', '/getUser', ['userId' => '2']);
        echo $response;
    }

    public function testChangePassword()
    {
        $response = $this->call('POST', '/changePassword', ['userId' => '2', 'oldPassword' => '123456', 'newPassword' => 'newtest']);
        echo $response;
    }

    public function testUpdateProfile()
    {
        $response = $this->call('POST', '/updateProfile', ['userId' => '2', 'username' => 'test', 'sex' => '1', 'avatar' => 'smile', 'bio' => '呵呵']);
        echo $response;
    }

    public function testGetAcceptedTasks()
    {
        $response = $this->call('GET', '/getAcceptedTasks', ['userId' => '2', 'limit' => '2']);
        echo $response;
    }

    public function testGetPublishedTasks()
    {
        $response = $this->call('GET', '/getPublishedTasks', ['userId' => '2', 'limit' => '2']);
        echo $response;
    }

    public function testGetTasksAround(){
        /*$this->call('POST', '/publishTask', ['title' => 'test', 'description' => 'test', 'userId' => '2',
            'deadline' => "2016-12-01 00:00:00", 'longitude' => '116.11111', 'latitude' => '39.11111', 'locationDscp' => 'test',
            'tags' => '["haha", "yoyo"]']);
        $this->call('POST', '/publishTask', ['title' => 'test', 'description' => 'test', 'userId' => '2',
            'deadline' => "2016-12-01 00:00:00", 'longitude' => '115.22222', 'latitude' => '39.22222', 'locationDscp' => 'test',
            'tags' => '["haha", "yoyo"]']);
        $this->call('POST', '/publishTask', ['title' => 'test', 'description' => 'test', 'userId' => '2',
            'deadline' => "2016-12-01 00:00:00", 'longitude' => '116.33333', 'latitude' => '39.33333', 'locationDscp' => 'test',
            'tags' => '["haha", "yoyo"]']);
        $this->call('POST', '/publishTask', ['title' => 'test', 'description' => 'test', 'userId' => '2',
            'deadline' => "2016-12-01 00:00:00", 'longitude' => '116.44444', 'latitude' => '38.44444', 'locationDscp' => 'test',
            'tags' => '["haha", "yoyo"]']);
        $this->call('POST', '/publishTask', ['title' => 'test', 'description' => 'test', 'userId' => '2',
            'deadline' => "2016-12-01 00:00:00", 'longitude' => '116.55555', 'latitude' => '39.55555', 'locationDscp' => 'test',
            'tags' => '["haha", "yoyo"]']);
        $this->call('POST', '/publishTask', ['title' => 'test', 'description' => 'test', 'userId' => '2',
            'deadline' => "2016-12-01 00:00:00", 'longitude' => '116.66666', 'latitude' => '39.66666', 'locationDscp' => 'test',
            'tags' => '["haha", "yoyo"]']);
        $this->call('POST', '/publishTask', ['title' => 'test', 'description' => 'test', 'userId' => '2',
            'deadline' => "2016-12-01 00:00:00", 'longitude' => '116.77777', 'latitude' => '39.77777', 'locationDscp' => 'test',
            'tags' => '["haha", "yoyo"]']);
        $this->call('POST', '/publishTask', ['title' => 'test', 'description' => 'test', 'userId' => '2',
            'deadline' => "2016-12-01 00:00:00", 'longitude' => '116.88888', 'latitude' => '39.88888', 'locationDscp' => 'test',
            'tags' => '["haha", "yoyo"]']);
        $this->call('POST', '/publishTask', ['title' => 'test', 'description' => 'test', 'userId' => '2',
            'deadline' => "2016-12-01 00:00:00", 'longitude' => '116.99999', 'latitude' => '39.99999', 'locationDscp' => 'test',
            'tags' => '["haha", "yoyo"]']);
        $this->call('POST', '/publishTask', ['title' => 'test', 'description' => 'test', 'userId' => '2',
            'deadline' => "2016-12-01 00:00:00", 'longitude' => '116.00000', 'latitude' => '39.00000', 'locationDscp' => 'test',
            'tags' => '["haha", "yoyo"]']);*/
        $response = $this->call('GET', '/getTasksAround', ['lon' => '116.5', 'lat' => '39.5', 'radius' => '33396']);
        echo $response;
    }

    public function testPublishTask()
    {
        $response = $this->call('POST', '/publishTask', ['title' => 'test', 'description' => 'test', 'userId' => '2',
            'deadline' => "2016-12-01 00:00:00", 'longitude' => '0', 'latitude' => '0', 'locationDscp' => 'test',
            'tags' => '["haha", "yoyo"]']);
        echo $response;
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
        $response = $this->call('POST', '/cancelTask', ['taskId' => 1, 'userId' => 2]);
        echo $response;
    }

}
