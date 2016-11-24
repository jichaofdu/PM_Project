<?php

/*
|--------------------------------------------------------------------------
| Application Routes
|--------------------------------------------------------------------------
|
| Here is where you can register all of the routes for an application.
| It is a breeze. Simply tell Lumen the URIs it should respond to
| and give it the Closure to call when that URI is requested.
|
*/

use Illuminate\Http\Response;

$app->get('/', function () {
    return '哒哒找人v0.0';
});

//register
$app->post('/register', 'UserController@register');
//login
$app->post('/login', 'UserController@login');
//Get user by userId
$app->get('/getUser', 'UserController@getUser');
//Change user password
$app->post('/changePassword', 'UserController@changePassword');
//Update user profile
$app->post('/updateProfile', 'UserController@updateProfile');
//Get tasks by publisher_id
$app->get('/getPublishedTasks', 'UserController@getPublishedTasks');
//Publish task
$app->post('/publishTask', 'TaskController@publishTask');



//tests
$app->get('/test', function () {
    return 'GET succeed';
});

$app->post('/test', function () {
    return 'Post succeed';
});
