<?php
/**
 * Created by PhpStorm.
 * User: wsylvia
 * Date: 11/20/16
 * Time: 6:27 PM
 */

namespace App\Models;

use Exception;
use Illuminate\Support\Facades\DB;

class Task extends CamelModel
{
    protected $primaryKey = 'task_id';

    /**
     * The attributes that are mass assignable.
     *
     * @var array
     */
    protected $fillable = [
        'title', 'description', 'publisher_id', 'published_time', 'longitude', 'latitude', 'location_dscp', 'deadline', 'credit', 'status', 'accepter_id'
    ];

    protected $hidden = [
        'created_at', 'updated_at'
    ];

    public function getTaskByTaskIdRaw($taskId)
    {
        return Task::findOrFail($taskId);
    }

    public function getTaskByTaskId($taskId)
    {
        $task = Task::findOrFail($taskId);
        return $this->formatTask($task, new User, new Tag);
    }

    public function getTasksAroundLocation($lat, $lon, $coordRadius)
    {
        $tasks = Task::query()->whereBetween("latitude", [$lat - $coordRadius, $lat + $coordRadius])
            ->whereBetween("longitude", [$lon - $coordRadius, $lon + $coordRadius])
            ->where('status', STATUS_PENDING)
            ->get();
        foreach ($tasks as $i => $task) {
            if (sqrt(pow($task->latitude - $lat, 2) + pow($task->longitude - $lon, 2)) > $coordRadius)
                unset($tasks[$i]);
        }
        $ret = [];
        foreach ($tasks as $task)
            $ret[] = $this->formatTask($task, new User, new Tag);
        return $ret;
    }

    public static function formatTask($taskItem, $User, $Tag)
    {

        $task['taskId'] = $taskItem->task_id;
        $task['title'] = $taskItem->title;
        $task['description'] = $taskItem->description;
        $task['publisher'] = $User->getUserCamelNullable($taskItem->publisher_id);
        $task['publishedTime'] = $taskItem->published_time;
        $task['deadline'] = $taskItem->deadline;
        $task['location'] = array(
            'longitude' => $taskItem->longitude,
            'latitude' => $taskItem->latitude,
            'description' => $taskItem->location_dscp
        );
        $task['tags'] = $Tag->getTagsByTaskId($taskItem->task_id);
        $task['credit'] = $taskItem->credit;
        $task['status'] = $taskItem->status;
        $task['accepter'] = $User->getUserCamelNullable($taskItem->accepter_id);

        return $task;
    }


    public static function expireTasks()
    {
        $pendingTasks = Task::where('status', STATUS_PENDING)->get();
        $underwayTasks = Task::where('status', STATUS_UNDERWAY)->get();

        $User = new User;

        //对于未被接受的任务，过期后向发布者返还扣除的信誉值
        foreach ($pendingTasks as $task) {
            $now = date("Y-m-d H:i");
            $deadline = $task->deadline;

            if (is_null($deadline)) {
                continue;
            }

//            echo $deadline . "\n";
            if ($now >= $deadline) {
                $publisher = $User->getUser($task->publisher_id);
                $publisher->credit = $publisher->credit + COST_PUBLISH + $task->credit;
                $task->status = STATUS_CANCELED;

                try {
                    DB::beginTransaction();

                    $task->save();
                    $publisher->save();

                    DB::commit();

                } catch (Exception $exception) {
                    DB::rollback();
                }
            }
        }


        //对于正在进行中的任务，过期后对接受者进行惩罚
        foreach ($underwayTasks as $task) {
            $now = date("Y-m-d H:i");
            $deadline = $task->deadline;

            if (is_null($deadline)) {
                continue;
            }

//            echo $deadline . "\n";
            if ($now >= $deadline) {
                $accepter = $User->getUser($task->accepter_id);
                $penalty = PENALTY_EXPIRE;
                //若接受者的信誉值足够，则扣除惩罚，否则扣成0
                if ($accepter->credit > $penalty) {
                    $accepter->credit = $accepter->credit - $penalty;
                } else {
                    $accepter->credit = 0;
                }
                $task->status = STATUS_CANCELED;
                try {
                    DB::beginTransaction();

                    $task->save();
                    $accepter->save();

                    DB::commit();

                } catch (Exception $exception) {
                    DB::rollback();
                }

            }
        }
    }

}