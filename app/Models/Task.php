<?php
/**
 * Created by PhpStorm.
 * User: wsylvia
 * Date: 11/20/16
 * Time: 6:27 PM
 */

namespace App\Models;

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
        foreach ($tasks as $i => $task){
            if (sqrt(pow($task->latitude-$lat, 2)+pow($task->longitude-$lon, 2))>$coordRadius)
                unset($tasks[$i]);
        }
        $ret=[];
        foreach ($tasks as $task)
            $ret[]=$this->formatTask($task, new User, new Tag);
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


}