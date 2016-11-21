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

$app->get('/', function () use ($app) {
    return $app->version();
});


//register
$app->post('/register', 'UserController@register');
//login
$app->post('/login', 'UserController@login');


$app->get('/keyfortest', function () {
    return str_random(32);
});