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


    public function formatTask($taskItem, $User, $Tag)
    {

        $task['taskId'] = $taskItem->task_id;
        $task['title'] = $taskItem->title;
        $task['description'] = $taskItem->description;
        $task['publisher'] = $User->getUserNullable($taskItem->publisher_id);
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
        $task['accepter'] = $User->getUserNullable($taskItem->accepter_id);

        return $task;
    }


}