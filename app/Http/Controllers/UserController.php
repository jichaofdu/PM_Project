<?php
/**
 * Created by PhpStorm.
 * User: wsylvia
 * Date: 11/19/16
 * Time: 11:30 PM
 */

namespace App\Http\Controllers;


use App\Models\Tag;
use App\Models\Task;
use App\Models\User;
use Exception;
use Illuminate\Contracts\Encryption\DecryptException;
use Illuminate\Http\Request;
use Illuminate\Http\Response;
use Illuminate\Support\Facades\Crypt;
use Symfony\Component\Filesystem\Filesystem;

use Intervention\Image\ImageManagerStatic as Image;

//require 'constants.php';

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
        $user->credit = DEFAULT_USER_CREDIT;
        $user->sex = DEFAULT_USER_SEX;
        $user->avatar = DEFAULT_USER_AVATAR;
        $user->bio = DEFAULT_USER_BIO;

        try {
            $user->save();
        } catch (Exception $exception) {
            $result = FAILED;
            $error = 'User existed';
            return new Response(['result' => $result, 'error' => $error]);
        }

        $result = SUCCEED;
        return new Response(['result' => $result, 'user' => $user->toArrayCamel()]);
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

        $User = new User;
        $user = $User->getUserByPhone($phone);

        try {
            $decrypted = Crypt::decrypt($user->password);
            if ($decrypted == $password) {
                $result = SUCCEED;
                return new Response(['result' => $result, 'user' => $user->toArrayCamel()]);
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

        $User = new User;
        $user = $User->getUserCamel($userId);

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

        if ($oldPassword == $newPassword) {
            $result = FAILED;
            $error = 'New password cannot be same as the old one';
            return new Response(['result' => $result, 'error' => $error]);
        }

        $User = new User;
        $user = $User->getUser($userId);

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
        if (empty($userId)) {
            abort(400);
        }

        $User = new User;
        $user = $User->getUser($userId);
        $user->username = $request->input('username', $user->username);
        $user->sex = $request->input('sex', $user->sex);
        $user->bio = $request->input('bio', $user->bio);

        if ($request->hasFile('avatar')){
            $photo=$request->file('avatar');
            if (!$photo->isValid()){
                $result = FAILED;
                $error = "Avatar upload failed";
                return new Response(['result' => $result, 'error' => $error]);
            }
            try {
                $rootDir='avatars/';

                $fs = new Filesystem();
                if (!$fs->exists($rootDir)) $fs->mkdir($rootDir);
                do $imgName=uniqid().'.jpg'; while ($fs->exists($rootDir.$imgName));

                Image::make($photo)->fit(200, 200)->save($rootDir.$imgName);

                if (!empty($user->avatar) && $user->avatar!='default.jpg' && $fs->exists($rootDir.$user->avatar))
                    $fs->remove($rootDir.$user->avatar);
                $user->avatar=$imgName;
            }
            catch (Exception $e){
                $result = FAILED;
                $error = $e;
                return new Response(['result' => $result, 'error' => $error]);
            }
        }

        try {
            $user->save();
            $result = SUCCEED;
            return new Response(['result' => $result, 'user' => $user->toArrayCamel()]);
        } catch (Exception $exception) {
            $result = FAILED;
            $error = $exception;
            return new Response(['result' => $result, 'error' => $error]);
        }
    }


    /**
     *
     * Get tasks by accepter_id
     *
     * @param Request $request
     * @return Response
     */
    public function getAcceptedTasks(Request $request)
    {
        $userId = $request->input('userId');
        $status = $request->input('status', 0);
        $limit = $request->input('limit', 0);

        if (empty($userId)) {
            abort(400);
        }

        if (!is_int($limit)) {
            $limit = 0;
        }

        $User = new User;
        $Tag = new Tag;
        $Task = new Task;

        $User->getUser($userId);

        $tasksQuery = Task::where('accepter_id', $userId);
        if ($status != 0) {
            $tasksQuery = $tasksQuery->where('status', $status);
        }

        if ($limit > 0) {
            $tasksQuery = $tasksQuery->take($limit);
        }

        $taskArray = $tasksQuery->get();

        $tasks = [];
        foreach ($taskArray as $taskItem) {
            $tasks[] = $Task->formatTask($taskItem, $User, $Tag);
        }

        $result = SUCCEED;
        return new Response(['result' => $result, 'tasks' => $tasks]);
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
        $status = $request->input('status', 0);
        $limit = $request->input('limit', 0);

        if (empty($userId)) {
            abort(400);
        }

        if (!is_int($limit)) {
            $limit = 0;
        }

        $User = new User;
        $Tag = new Tag;
        $Task = new Task;

        $User->getUserCamel($userId);

        $tasksQuery = Task::where('publisher_id', $userId);
        if ($status != 0) {
            $tasksQuery = $tasksQuery->where('status', $status);
        }

        if ($limit > 0) {
            $tasksQuery = $tasksQuery->take($limit);
        }

        $taskArray = $tasksQuery->get();

        $tasks = [];
        foreach ($taskArray as $taskItem) {
            $tasks[] = $Task->formatTask($taskItem, $User, $Tag);
        }

        $result = SUCCEED;
        return new Response(['result' => $result, 'tasks' => $tasks]);
    }

}