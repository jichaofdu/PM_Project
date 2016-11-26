<?php
/**
 * Created by PhpStorm.
 * User: wsylvia
 * Date: 11/26/16
 * Time: 5:34 PM
 */

namespace App\Models;


use Illuminate\Database\Eloquent\Model;

class CamelModel extends Model
{
    public function toArrayCamel()
    {
        $array = $this->toArray();

        foreach ($array as $key => $value) {
            $return[camel_case($key)] = $value;
        }

        return $return;
    }
}