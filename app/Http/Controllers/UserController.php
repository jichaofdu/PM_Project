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

    /**
     *
     * Change user password
     *
     * @param Request $request
     * @return Response
     */
    public function changePassword(Request $request)
    {
        $userId = $request->input('userId');
        $oldPassword = $request->input('oldPassword');
        $newPassword = $request->input('newPassword');

        if (empty($userId) || empty($oldPassword) || empty($newPassword)) {
            abort(400);
        }

        $user = User::find($userId);
        if (empty($user)) {
            $result = FAILED;
            $error = 'No such user';
            return new Response(['result' => $result, 'error' => $error]);
        }

        try {
            $decrypted = Crypt::decrypt($user->password);
            if ($decrypted == $oldPassword) {
                $user->password = Crypt::encrypt($newPassword);
                $user->save();
                $result = SUCCEED;
                return new Response(['result' => $result]);
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
     * Update user profile
     *
     * @param Request $request
     * @return Response
     */
    public function updateProfile(Request $request)
    {
        $userId = $request->input('userId');
        $username = $request->input('username');
        $sex = $request->input('sex');
        $avatar = $request->input('avatar');
        $bio = $request->input('bio');

        if (empty($userId)) {
            abort(400);
        }

        $user = User::find($userId);
        $user->username = $username;
        $user->sex = $sex;
        $user->avatar = $avatar;
        $user->bio = $bio;

        try{
            $user->save();
            $result = SUCCEED;
            return new Response(['result' => $result, 'user' => $user]);
        }
        catch (Exception $exception) {
            $result = FAILED;
            $error = $exception;
            return new Response(['result' => $result, 'error' => $error]);
        }
    }

    /**
     *
     * Get tasks by publisher_id
     *
     * @param Request $request
     * @return Response
     */
    public function getPublishedTasks(Request $request)
    {
        $userId = $request->input('userId');
        $limit = $request->input('limit');

        $user = User::find($userId);
        if (empty($user)) {
            $result = FAILED;
            $error = 'No such user';
            return new Response(['result' => $result, 'error' => $error]);
        }

        $tasks = Task::where('publisher_id', $userId)->take($limit)->get();
        $result = SUCCEED;
        return new Response(['result' => $result, 'tasks' => $tasks]);
    }

}