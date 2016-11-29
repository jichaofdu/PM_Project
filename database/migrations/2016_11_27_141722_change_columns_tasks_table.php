<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

class ChangeColumnsTasksTable extends Migration
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
            $table->double('longitude')->nullable()->change();
            $table->double('latitude')->nullable()->change();
            $table->string('location_dscp')->nullable()->change();
            $table->string('deadline')->nullable()->change();
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
            $table->double('longitude')->nullable(false)->change();
            $table->double('latitude')->nullable(false)->change();
            $table->string('location_dscp')->nullable(false)->change();
            $table->string('deadline')->nullable(false)->change();
        });
    }
}
