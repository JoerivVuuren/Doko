<?php

//load and connect to MySQL database stuff
require_once("config.inc.php");

if (!empty($_POST) && !empty($_POST['username']) && !empty($_POST['password']) && !empty($_POST['request_id']) && 
	kite_check_login($db, $salt, $_POST['username'], $_POST['password'])) {
	
	// remove game request from request list
	$stmt = $db->prepare("DELETE FROM game_requests WHERE id=?");
	$stmt->bindValue(1, $_POST['request_id'], PDO::PARAM_INT);
	$stmt->execute();
	
	$response["success"] = 1;
	$response["message"] = "Game request removed.";
	die(json_encode($response));
}
else {
	$response["success"] = 0;
	$response["message"] = "Error: invalid credentials!";
	die(json_encode($response));
}

?>