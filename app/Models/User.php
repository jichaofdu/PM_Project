<?php

namespace App\Models;

use Illuminate\Auth\Authenticatable;
use Illuminate\Contracts\Auth\Access\Authorizable as AuthorizableContract;
use Illuminate\Contracts\Auth\Authenticatable as AuthenticatableContract;
use Laravel\Lumen\Auth\Authorizable;

class User extends CamelModel implements AuthenticatableContract, AuthorizableContract
{
    use Authenticatable, Authorizable;

    protected $primaryKey = 'user_id';

    /**
     * The attributes that are mass assignable.
     *
     * @var array
     */
    protected $fillable = [
        'phone', 'username', 'credit', 'sex', 'avatar', 'bio'
    ];

    /**
     * The attributes excluded from the model's JSON form.
     *
     * @var array
     */
    protected $hidden = [
        'password', 'created_at', 'updated_at'
    ];


    /**
     *
     * Get an user by userId
     *
     * @param integer $userId
     * @return User $user
     */
    public function getUser($userId)
    {
        return $user = User::findOrFail($userId);

    }

    /**
     *
     * Get an user by userId in camel case
     *
     * @param integer $userId
     * @return User $user
     */
    public function getUserCamel($userId)
    {
        return $user = User::findOrFail($userId)->toArrayCamel();
    }


    /**
     *
     * Get a nullable user by userId in camel case
     *
     * @param $userId
     * @return mixed
     */
    public function getUserCamelNullable($userId)
    {
        $user = User::find($userId);
        if (isset($user)) {
            $user = $user->toArrayCamel();
        }

        return $user;
//        return User::find($userId);
    }

    /**
     *
     * Get an user by phone
     *
     * @param $phone
     * @return mixed
     */
    public function getUserByPhone($phone)
    {
        return $user = User::where('phone', $phone)->firstOrFail();
    }

    /**
     *
     * Get an user by phone in camel case
     *
     * @param $phone
     * @return mixed
     */
    public function getUserCamelByPhone($phone)
    {
        return $user = User::where('phone', $phone)->firstOrFail()->toArrayCamel();
    }
}
