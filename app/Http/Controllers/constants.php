<?php
/**
 * Created by PhpStorm.
 * User: wsylvia
 * Date: 11/23/16
 * Time: 11:17 AM
 */

//results
define('SUCCEED', 'succeed');
define('FAILED', 'failed');

//default user values
define('DEFAULT_USER_CREDIT', 5);
define('DEFAULT_USER_SEX', 0);
define('DEFAULT_USER_AVATAR', '');
define('DEFAULT_USER_BIO', '好像没有什么想说的');

//default task value
define('DEFAULT_TASK_CREDIT', 0);


//Task status
define('STATUS_PENDING', 1);
define('STATUS_UNDERWAY', 2);
define('STATUS_DONE', 3);
define('STATUS_CONFIRMED', 4);
define('STATUS_CANCELED', -1);

//cost & reward
define('COST_PUBLISH', 2);
define('REWARD_ACCEPT', 3);

//penalty
define('PENALTY_CANCEL', 6);
define('PENALTY_QUIT', 5);
define('PENALTY_EXPIRE', 6);