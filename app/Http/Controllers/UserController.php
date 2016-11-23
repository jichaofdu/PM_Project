<?php
/**
 * Created by PhpStorm.
 * User: wsylvia
 * Date: 11/19/16
 * Time: 11:30 PM
 */

namespace App\Http\Controllers;


use App\Models\User;
use Illuminate\Contracts\Logging\Log;
use Illuminate\Http\Response;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Crypt;
use Exception;

require 'messageConstants.php';

class UserController extends Controller
{


    /**
     *
     * User register
     *
     * @param Request $request
     * @return Response
     */
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

        try {
            $user->save();
        } catch (Exception $exception) {
            $result = FAILED;
            $error = 'User existed';
            return new Response(['result' => $result, 'error' => $error]);
        }

        $result = SUCCEED;
        return new Response(['result' => $result, 'user' => $user]);
    }

    /**
     *
     * User login
     *
     * @param Request $request
     * @return Response
     */
    public function login(Request $request)
    {
        $phone = $request->input('phone');
        $password = $request->input('password');

        if (empty($phone) || empty($password)) {
            abort(400);
        }

        $user = User::where('phone', $phone)->first();
        if (empty($user)) {
            $result = FAILED;
            $error = 'No such user';
            return new Response(['result' => $result, 'error' => $error]);
        }

        try {
            $decrypted = Crypt::decrypt($user->password);
            if ($decrypted == $password) {
                $result = SUCCEED;
                return new Response(['result' => $result, 'user' => $user]);
            } else {
                $result = FAILED;
                $error = 'Wrong password';
                return new Response(['result' => $result, 'error' => $error]);
            }
        } catch (DecryptException $e) {
            //
            $result = FAILED;
            $error = $e;
            return new Response(['result' => $result, 'error' => $error]);
        }
    }


    /**
     *
     * Get user by userId
     *
     * @param Request $request
     * @return Response
     */
    public function getUser(Request $request)
    {
        if (empty($userId = $request->input('userId'))) {
            abort(400);
        }

        $user = User::find($userId);
        if (empty($user)) {
            $result = FAILED;
            $error = 'No such user';
            return new Response(['result' => $result, 'error' => $error]);
        }

        $result = SUCCEED;
        return new Response(['result' => $result, 'user' => $user]);
    }

}