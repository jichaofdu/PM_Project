<?php
/**
 * Created by PhpStorm.
 * User: wsylvia
 * Date: 11/19/16
 * Time: 7:12 PM
 */

use Illuminate\Http\Response;
use Illuminate\Support\Facades\Route;


Route::post('/register', 'UserController@register');
Route::get('/', function () {
    return new Response(["user" => "you"]);
});

//$app->get('/test','ExampleController@test');
Route::get('/test', function () {
    return new Response(["user" => "user"]);
});