<?php

use Illuminate\Support\Facades\Schema;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreateTasksTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        //
        Schema::create('tasks', function (Blueprint $table) {
            $table->increments('task_id');
            $table->string('title');
            $table->string('description');
            $table->integer('publisher_id');
            $table->dateTime('published_time');
            $table->double('longitude');
            $table->double('latitude');
            $table->string('location_dscp');
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        //
        Schema::drop('tasks');
    }
}
