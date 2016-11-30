<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

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
//            $table->foreign('publisher_id')->references('user_id')->on('users')->onDelete('cascade');
            $table->dateTime('published_time');
            $table->dateTime('deadline')->nullable();
            $table->double('longitude')->nullable();
            $table->double('latitude')->nullable();
            $table->string('location_dscp')->nullable();
            $table->integer('credit');
            $table->integer('status');
            $table->integer('accepter_id')->nullable();
//            $table->foreign('accepter_id')->references('user_id')->on('users')->onDelete('cascade');
            $table->timestamps();
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
