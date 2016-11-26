<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

class AddColumnsToTasksTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::table('tasks', function (Blueprint $table) {
            //
//            $table->engine = 'InnoDB';

            $table->dateTime('deadline');
            $table->integer('credit')->default(1);
            $table->integer('status')->default(1);
            $table->integer('accepter_id')->nullable();

//            $table->foreign('accepter_id')
//                ->references('user_id')
//                ->on('users')
//                ->onDelete('cascade')
//                ->unsigned();
//
//            $table->foreign('publisher_id')
//                ->references('user_id')
//                ->on('users')
//                ->onDelete('cascade')
//                ->unsigned()
//                ->change();
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::table('tasks', function (Blueprint $table) {
            //
            $table->dropColumn(['deadline', 'credit', 'status', 'accepter_id']);
//            $table->dropForeign('publisher_id');
        });
    }
}
