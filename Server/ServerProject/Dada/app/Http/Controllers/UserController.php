<?php
/**
 * Created by PhpStorm.
 * User: wsylvia
 * Date: 11/19/16
 * Time: 11:30 PM
 */

namespace App\Http\Controllers;


use App\Models\User;
use Illuminate\Http\Response;
use Illuminate\Http\Request;

class UserController extends Controller
{
    public function register(Request $request)
    {
        $phone = $request->input('phone');
        $username = $request->input('username');
        $password = $request->input('password');

        $user = new User;
        $user->phone = $phone;
        $user->username = $username;
        $user->password = $password;

        if ($user->save()) {
            $result = 'succeed';
            return new Response(['result' => $result, 'user' => $user]);
        } else {
            $result = 'failed';
            $error = '';
            return new Response(['result' => $result, 'error' => $error]);
        }
    }

}