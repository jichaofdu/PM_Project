<?php

use Illuminate\Support\Facades\Schema;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class AddStatusAccepterIdToTasksTable extends Migration
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

            $table->integer('credit')->default(0);
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
            $table->dropColumn(['status', 'accepter_id']);
            $table->dropForeign('publisher_id');
        });
    }
}
