<?php

//load and connect to MySQL database stuff
require_once("config.inc.php");

if (!empty($_POST) && !empty($_POST['username']) && !empty($_POST['password']) && !empty($_POST['group_id']) && 
	kite_check_login($db, $salt, $_POST['username'], $_POST['password'])) {
		
	$stmt = $db->prepare("DELETE FROM group_requests WHERE (group_id=? AND receiver=?)");
	$stmt->bindValue(1, $_POST['group_id'], PDO::PARAM_INT);

	$stmt->bindValue(2, $_POST['username'], PDO::PARAM_STR);
	$stmt->execute();
}
else {
	$response["success"] = 0;
	$response["message"] = "Error: invalid credentials!";
	die(json_encode($response));
}

?>