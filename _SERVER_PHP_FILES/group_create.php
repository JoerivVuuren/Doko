<?php

//load and connect to MySQL database stuff
require_once("config.inc.php");

if (!empty($_POST) && !empty($_POST['username']) && !empty($_POST['password']) && !empty($_POST['groupname']) && 
	kite_check_login($db, $salt, $_POST['username'], $_POST['password'])) {
	
	// create group in db
	$stmt = $db->prepare("INSERT INTO groups ( admin_name, name ) VALUES ( ?, ? )");
	$stmt->bindValue(1, $_POST['username'], PDO::PARAM_STR);
	$stmt->bindValue(2, $_POST['groupname'], PDO::PARAM_STR);
	$stmt->execute();
	
	$group_id = $db->lastInsertId();
	
	$stmt = $db->prepare("INSERT INTO groups_users ( group_id, username ) VALUES ( ?, ? )");
	$stmt->bindValue(1, $group_id, PDO::PARAM_INT);
	$stmt->bindValue(2, $_POST['username'], PDO::PARAM_STR);
	$stmt->execute();

	$response["group_name"] = $_POST['groupname'];
	$response["group_id"] = $group_id;
	$response["success"] = 1;
	$response["message"] = "Group created.";
	die(json_encode($response));
}
else {
	$response["success"] = 0;
	$response["message"] = "Error: invalid credentials!";
	die(json_encode($response));
}

?>