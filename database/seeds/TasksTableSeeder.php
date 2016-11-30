<?php
use Illuminate\Database\Seeder;
use Illuminate\Support\Facades\DB;

/**
 * Created by PhpStorm.
 * User: wsylvia
 * Date: 11/30/16
 * Time: 11:59 PM
 */
class TasksTableSeeder extends Seeder
{

    /**
     * Run the database seeds.
     *
     * @return void
     */
    public function run()
    {
        for ($i = 0; $i < 10; $i++) {
            DB::table('tasks')->insert([
                'title' => str_random(10),
                'description' => str_random(10),
                'publisher_id' => $publisherId = mt_rand(1, 10),
                'published_time' => date("Y-m-d h:i:sa"),
                'status' => mt_rand(2, 3),
                'accepter_id' => $this->randomId(15, $publisherId)
            ]);
        }
    }

    function randomId($maxId, $except)
    {
        $result = mt_rand(1, $maxId);
        while ($result == $except) {
            $result = mt_rand(1, $maxId);
        }

        return $result;
    }
}