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

require 'constants.php';

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
        $credit = $request->input('credit');

        if (empty($title) || empty($description) || empty($userId)) {
            abort(400);
        }

        if (empty($credit) || !is_int($credit)) {
            $credit = DEFAULT_TASK_CREDIT;
        }

        $User = new User;
        $Tag = new Tag;

        $user = $User->getUser($userId);

        $task = new Task();
        $task->title = $title;
        $task->description = $description;
        $task->publisher_id = $userId;
        $task->published_time = date("Y-m-d H:i:s");
        if (!is_null($deadline)) {
            $task->deadline = date("Y-m-d H:i:s", strtotime($deadline));
        }
        $task->longitude = $longitude;
        $task->latitude = $latitude;
        $task->location_dscp = $locationDscp;
        $task->credit = $credit;
        $task->status = STATUS_PENDING;


        //发布任务代价 = 悬赏分 + 基础发布代价
        $taskCost = $credit + COST_PUBLISH;

        //发布者的信誉值余额必须高于发布任务代价
        if ($user->credit < $taskCost) {
            $result = FAILED;
            $error = 'Credit insufficient!';
            return new Response(['result' => $result, 'error' => $error]);
        }

        //在用户的信誉值中减去发布任务代价
        $user->credit = $user->credit - $taskCost;

        try {
            DB::beginTransaction();
            $task->save();
            $user->save();

            if (!empty($tags)) {
                $tags = json_decode($tags);
                foreach ($tags as $tagItem) {
                    $tag = new Tag;
                    $tag->task_id = $task->task_id;
                    $tag->tag = $tagItem;
                    $tag->save();
                }
            }

            DB::commit();

            $result = SUCCEED;
            return new Response(['result' => $result, 'task' => $task->formatTask($task, $User, $Tag)]);
        } catch (Exception $exception) {
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

    //TODO: Complete this method
    /**
     *
     * Edit an existed task
     *
     * @param Request $request
     * @return Response
     */
    public function editTask(Request $request)
    {
        $taskId = $request->input('task_id');

        if (empty($taskId)) {
            abort(400);
        }

        $Task = new Task;
        $Tag = new Tag;
        $task = $Task->getTaskByTaskIdRaw($taskId);
        $originTags = $Tag->getTagsByTaskId($taskId);

        $title = $request->input('title', $task->title);
        $description = $request->input('description', $task->description);
        $deadline = $request->input('deadline', $task->deadline);
        $longitude = $request->input('longitude', $task->longitude);
        $latitude = $request->input('latitude', $task->latitude);
        $locationDscp = $request->input('locationDscp', $task->location_dscp);
        $tags = $request->input('tags', json_encode($originTags));

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

        if ($task->status != STATUS_PENDING) {
            $result = FAILED;
            $error = 'Cannot be accepted';
            return new Response(['result' => $result, 'error' => $error]);
        }

        $task->status = STATUS_UNDERWAY;
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

        if ($task->accepter_id != $userId) {
            abort(403);
        }

        if ($task->status != STATUS_UNDERWAY) {
            $result = FAILED;
            $error = 'Cannot be done';
            return new Response(['result' => $result, 'error' => $error]);
        }

        $task->status = STATUS_DONE;

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
     * Publisher confirm a task
     *
     * @param Request $request
     * @return Response
     */
    public function confirmTask(Request $request)
    {
        $taskId = $request->input('taskId');
        $userId = $request->input('userId');

        if (empty($taskId) || empty($userId)) {
            abort(400);
        }

        $Task = new Task;
        $User = new User;
        $task = $Task->getTaskByTaskIdRaw($taskId);
        $accepter = $User->getUser($task->accepter_id);

        if ($task->publisher_id != $userId) {
            abort(403);
        }

        if ($task->status != STATUS_DONE) {
            $result = FAILED;
            $error = 'Cannot be confirmed';
            return new Response(['result' => $result, 'error' => $error]);
        }

        $task->status = STATUS_CONFIRMED;

        //向任务接受者发放奖励: 系统奖励 + 任务悬赏
        $accepter->credit = REWARD_ACCEPT + $task->credit;

        try {
            DB::beginTransaction();

            $task->save();
            $accepter->save();

            DB::commit();
            $result = SUCCEED;
            return new Response(['result' => $result]);
        } catch (Exception $exception) {
            DB::rollback();
            $result = FAILED;
            $error = $exception;
            return new Response(['result' => $result, 'error' => $error]);
        }
    }


    /**
     *
     * Publisher cancel a task
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
        $User = new User;
        $task = $Task->getTaskByTaskIdRaw($taskId);
        $publisher = $User->getUser($userId);

        if ($task->publisher_id != $userId) {
            abort(403);
        }


        if ($task->status != STATUS_PENDING && $task->status != STATUS_UNDERWAY) {
            $result = FAILED;
            $error = 'Cannot be canceled';
            return new Response(['result' => $result, 'error' => $error]);
        }

        $penalty = 0;
        //若要取消的任务正在进行中，则进行惩罚
        if ($task->status == STATUS_UNDERWAY) {
            $penalty = PENALTY_CANCEL;
        }

        if ($publisher->credit + $task->credit < $penalty) {
            $result = FAILED;
            $error = 'Credit insufficient!';
            return new Response(['result' => $result, 'error' => $error]);
        }

        //返还悬赏分,并扣除惩罚
        $publisher->credit = $publisher->credit + $task->credit - $penalty;

        $task->status = STATUS_CANCELED;

        try {
            DB::beginTransaction();
            $task->save();
            $publisher->save();

            DB::commit();

            $result = SUCCEED;
            return new Response(['result' => $result]);
        } catch (Exception $exception) {
            DB::rollback();

            $result = FAILED;
            $error = $exception;
            return new Response(['result' => $result, 'error' => $error]);
        }
    }


    /**
     *
     * Accepter quit a task
     *
     * @param Request $request
     * @return Response
     */
    public function quitTask(Request $request)
    {
        $taskId = $request->input('taskId');
        $userId = $request->input('userId');

        if (empty($taskId) || empty($userId)) {
            abort(400);
        }

        $Task = new Task;
        $User = new User;
        $task = $Task->getTaskByTaskIdRaw($taskId);
        $accepter = $User->getUser($userId);

        if ($task->accepter_id != $userId) {
            abort(403);
        }

        if ($task->status != STATUS_UNDERWAY) {
            $result = FAILED;
            $error = 'Cannot quit!';
            return new Response(['result' => $result, 'error' => $error]);
        }

        $penalty = PENALTY_QUIT;
        //接受者的信誉值余额必须高于放弃任务的惩罚
        if ($accepter->credit < $penalty) {
            $result = FAILED;
            $error = 'Credit insufficient!';
            return new Response(['result' => $result, 'error' => $error]);
        }

        //扣除放弃任务的惩罚
        $accepter->credit = $accepter->credit - $penalty;

        $task->status = STATUS_CANCELED;

        try {
            DB::beginTransaction();
            $task->save();
            $accepter->save();

            DB::commit();

            $result = SUCCEED;
            return new Response(['result' => $result]);
        } catch (Exception $exception) {
            DB::rollback();

            $result = FAILED;
            $error = $exception;
            return new Response(['result' => $result, 'error' => $error]);
        }
    }

    public function getTasksAround(Request $request)
    {
        try {
            $lon = floatval($request->input('lon'));
            $lat = floatval($request->input('lat'));
            $coordRadius = floatval($request->input('radius')) / 111321;
        } catch (Exception $e) {
            $result = FAILED;
            $error = 'Invalid request: not numbers';
            return new Response(['result' => $result, 'error' => $error]);
        }
        if (empty($lon) || empty($lat) || empty($coordRadius)) {
            abort(400);
        }

        $Task = new Task;
        $tasks = $Task->getTasksAroundLocation($lat, $lon, $coordRadius);
        $result = SUCCEED;
        return new Response(['result' => $result, 'tasks' => $tasks]);
    }


}