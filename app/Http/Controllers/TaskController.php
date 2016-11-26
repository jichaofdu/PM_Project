<?php
/**
 * Created by PhpStorm.
 * User: tao
 * Date: 2016/11/23
 * Time: 21:29
 */

namespace App\Http\Controllers;


use App\Models\Tag;
use App\Models\Task;
use App\Models\User;
use Exception;
use Illuminate\Http\Request;
use Illuminate\Http\Response;
use Illuminate\Support\Facades\DB;

require 'messageConstants.php';

class TaskController extends Controller
{

    /**
     *
     * Publish a task
     *
     * @param Request $request
     * @return Response
     */
    public function publishTask(Request $request)
    {
        $title = $request->input('title');
        $description = $request->input('description');
        $userId = $request->input('userId');
        $deadline = $request->input('deadline');
        $longitude = $request->input('longitude');
        $latitude = $request->input('latitude');
        $locationDscp = $request->input('locationDscp');
        $tags = $request->input('tags');

        if (empty($title) || empty($description) || empty($userId)) {
            abort(400);
        }

        $User = new User;
        $Tag = new Tag;
        $User->getUser($userId);

        $task = new Task();
        $task->title = $title;
        $task->description = $description;
        $task->publisher_id = $userId;
        $task->deadline = $deadline;
        $task->longitude = $longitude;
        $task->latitude = $latitude;
        $task->location_dscp = $locationDscp;

        try {
            DB::beginTransaction();
            $task->save();
            foreach ($tags as $tagItem) {
                $tag = new Tag;
                $tag->task_id = $task->task_id;
                $tag->tag = $tagItem;
            }
            DB::commit();

            $result = SUCCEED;
            return new Response(['result' => $result, 'task' => $task->formatTask($task, $User, $Tag)]);
        }
        catch (Exception $exception){
            DB::rollback();
            $result = FAILED;
            return new Response(['result' => $result, 'error' => $exception]);
        }
    }


    /**
     *
     * Return a task
     *
     * @param Request $request
     * @return Response
     */
    public function viewTask(Request $request)
    {
        $taskId = $request->input('taskId');
        if (empty($taskId)) {
            abort(400);
        }

        $Task = new Task;
        $task = $Task->getTaskByTaskId($taskId);

        $result = SUCCEED;
        return new Response(['result' => $result, 'task' => $task]);
    }

    /**
     *
     * Accept a task
     *
     * @param Request $request
     * @return Response
     */
    public function acceptTask(Request $request)
    {
        $taskId = $request->input('taskId');
        $userId = $request->input('userId');

        if (empty($taskId) || empty($userId)) {
            abort(400);
        }

        $Task = new Task;
        $task = $Task->getTaskByTaskIdRaw($taskId);

        if ($task->status != 1) {
            $result = FAILED;
            $error = 'Cannot be accepted';
            return new Response(['result' => $result, 'error' => $error]);
        }

        $task->status = 2;
        $task->accepter_id = $userId;

        try {
            $task->save();
            $result = SUCCEED;
            return new Response(['result' => $result]);
        } catch (Exception $exception) {
            $result = FAILED;
            $error = $exception;
            return new Response(['result' => $result, 'error' => $error]);
        }
    }


    /**
     *
     * Done a task
     *
     * @param Request $request
     * @return Response
     */
    public function doneTask(Request $request)
    {
        $taskId = $request->input('taskId');
        $userId = $request->input('userId');

        if (empty($taskId) || empty($userId)) {
            abort(400);
        }

        $Task = new Task;
        $task = $Task->getTaskByTaskIdRaw($taskId);

        if ($task->publisher_id != $userId) {
            abort(403);
        }

        if ($task->status != 2) {
            $result = FAILED;
            $error = 'Cannot be done';
            return new Response(['result' => $result, 'error' => $error]);
        }

        $task->status = 3;

        try {
            $task->save();
            $result = SUCCEED;
            return new Response(['result' => $result]);
        } catch (Exception $exception) {
            $result = FAILED;
            $error = $exception;
            return new Response(['result' => $result, 'error' => $error]);
        }
    }


    /**
     *
     * Cancel a task
     *
     * @param Request $request
     * @return Response
     */
    public function cancelTask(Request $request)
    {
        $taskId = $request->input('taskId');
        $userId = $request->input('userId');

        if (empty($taskId) || empty($userId)) {
            abort(400);
        }

        $Task = new Task;
        $task = $Task->getTaskByTaskIdRaw($taskId);

        if ($task->publisher_id != $userId) {
            abort(403);
        }

        if ($task->status != 1 && $task->status != 2) {
            $result = FAILED;
            $error = 'Cannot be canceled';
            return new Response(['result' => $result, 'error' => $error]);
        }

        $task->status = -1;

        try {
            $task->save();
            $result = SUCCEED;
            return new Response(['result' => $result]);
        } catch (Exception $exception) {
            $result = FAILED;
            $error = $exception;
            return new Response(['result' => $result, 'error' => $error]);
        }
    }


}