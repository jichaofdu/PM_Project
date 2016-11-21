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
use Illuminate\Support\Facades\Crypt;

class UserController extends Controller
{
    public function register(Request $request)
    {
        $phone = $request->input('phone');
        $username = $request->input('username');
        $password = $request->input('password');

        if (empty($phone) || empty($username) || empty($password)) {
            abort(400);
        }

        $user = new User;
        $user->phone = $phone;
        $user->username = $username;
        $user->password = Crypt::encrypt($password);

        if ($user->save()) {
            $result = 'succeed';
            return new Response(['result' => $result, 'user' => $user]);
        } else {
            $result = 'failed';
            $error = '';
            return new Response(['result' => $result, 'error' => $error]);
        }
    }

    public function login(Request $request)
    {
        $phone = $request->input('phone');
        $password = $request->input('password');

        if (empty($phone) || empty($password)) {
            abort(400);
        }

        $user = User::where('phone', $phone)->first();
        if (empty($user)) {
            $result = 'failed';
            $error = 'No such user';
            return new Response(['result' => $result, 'error' => $error]);
        }

        try {
            $decrypted = Crypt::decrypt($user->password);
            if ($decrypted == $password) {
                $result = 'succeed';
                return new Response(['result' => $result, 'user' => $user]);
            } else {
                $result = 'failed';
                $error = 'Wrong password';
                return new Response(['result' => $result, 'error' => $error]);
            }
        } catch (DecryptException $e) {
            //
        }
    }

}