<?php

//load and connect to MySQL database stuff
require_once("config.inc.php");

if (!empty($_POST) && !empty($_POST['username']) && !empty($_POST['password']) && !empty($_POST['game_id']) && 
	!empty($_POST['b']) && !empty($_POST['friend'] &&
    kite_check_login($db, $salt, $_POST['username'], $_POST['password'])) {
	

	$stmt = $db->prepare("INSERT INTO state ( game, v, h, user ) VALUES ( ?, ?, ?, ? )");
	$stmt->bindValue(1, $_POST['game_id'], PDO::PARAM_STR);
	$stmt->bindValue(2, $_POST['v'], PDO::PARAM_STR);
	$stmt->bindValue(3, $_POST['h'], PDO::PARAM_STR);
	$stmt->bindValue(4, $_POST['username'], PDO::PARAM_STR);
	$stmt->execute();
    
    
}
else {
	$response["success"] = 0;
	$response["message"] = "Error: invalid credentials!";
	die(json_encode($response));
}

?>