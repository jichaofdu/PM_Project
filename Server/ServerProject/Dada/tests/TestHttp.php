<?php

/**
 * Created by PhpStorm.
 * User: wsylvia
 * Date: 11/20/16
 * Time: 7:41 PM
 */
class TestHttp extends TestCase
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
}