<?php
use Illuminate\Database\Seeder;

/**
 * Created by PhpStorm.
 * User: wsylvia
 * Date: 11/30/16
 * Time: 11:30 PM
 */
class UsersTableSeeder extends Seeder
{
    /**
     * Run the database seeds.
     *
     * @return void
     */
    public function run()
    {
        for ($i = 0; $i < 10; $i++) {
            DB::table('users')->insert([
                'username' => str_random(6),
                'phone' => $this->randomPhone(),
                'password' => encrypt(123456),
                'credit' => mt_rand(0, 20),
                'sex' => mt_rand(0, 2),
            ]);
        }
    }

    function randomPhone()
    {
        $result = '';
        for ($i = 0; $i < 12; $i++) {
            $result .= mt_rand(0, 9);
        }

        return $result;
    }

}