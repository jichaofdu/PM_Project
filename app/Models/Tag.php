<?php
/**
 * Created by PhpStorm.
 * User: wsylvia
 * Date: 11/20/16
 * Time: 6:33 PM
 */

namespace App\Models;


class Tag extends CamelModel
{

    public $timestamps = false;

    public function getTagsByTaskId($taskId)
    {
        $tagsArray = Tag::where('task_id', $taskId)->get();
        $tags = [];
        foreach ($tagsArray as $tag) {
            $tags[] = $tag->tag;
        }

        return $tags;
    }
}