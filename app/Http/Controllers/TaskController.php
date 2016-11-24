<?php
/**
 * Created by PhpStorm.
 * User: tao
 * Date: 2016/11/23
 * Time: 21:29
 */

namespace App\Http\Controllers;


use App\Models\Task;
use App\Models\User;
use Illuminate\Contracts\Logging\Log;
use Illuminate\Http\Response;
use Illuminate\Http\Request;
use Exception;

require 'messageConstants.php';

class TaskController extends Controller
{

    /**
     *
     * Publish task
     *
     * @param Request $request
     * @return Response
     */
    public function publishTask(Request $request)
    {
        $title = $request->input('title');
        $description = $request->input('description');
        $userId = $request->input('userId');
        //$deadline = $request->input('deadline');
        $longitude = $request->input('longitude');
        $latitude = $request->input('latitude');
        $locationDscp = $request->input('locationDscp');
        //$tags = $request->input('tags');

        if (empty($title) || empty($description) || empty($userId)) {
            abort(400);
        }

        $user = User::find($userId);
        if (empty($user)) {
            $result = FAILED;
            $error = 'No such user';
            return new Response(['result' => $result, 'error' => $error]);
        }

        $task = new Task();
        $task->title = $title;
        $task->description = $description;
        $task->publisher_id = $userId;
        //$task->deadline = $deadline;
        $task->longitude = $longitude;
        $task->latitude = $latitude;
        $task->location_dscp = $locationDscp;

        try {
            $task->save();
            $result = SUCCEED;
            return new Response(['result' => $result, 'task' => $task]);
        }
        catch (Exception $exception){
            $result = FAILED;
            return new Response(['result' => $result, 'error' => $exception, 'task' => $task]);
        }


    }


}