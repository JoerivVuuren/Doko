<?php

//load and connect to MySQL database stuff
require_once("config.inc.php");

if (!empty($_POST) && !empty($_POST['username']) && !empty($_POST['password']) && 
	!empty($_POST['group_id']) && kite_check_login($db, $salt, $_POST['username'], $_POST['password'])) {
		
	/* check if username is admin of group_id
	$stmt = $db->prepare("SELECT 1 FROM groups WHERE id=? AND admin_name=?");
	$stmt->bindValue(1, $_POST['group_id'], PDO::PARAM_INT);
	$stmt->bindValue(2, $_POST['username'], PDO::PARAM_STR);
	$stmt->execute();
	$result = $stmt->fetch(PDO::FETCH_NUM);
	
	if (!$result) {
		$response["message"] = "Error: invalid privileges!";
		$response["success"] = 0;
		die(json_encode($response));
	}*/
	
	// check if user already exists in group
	$stmt = $db->prepare("SELECT 1 FROM groups_users WHERE group_id=? AND username=?");
	$stmt->bindValue(1, $_POST['group_id'], PDO::PARAM_INT);
	$stmt->bindValue(2, $_POST['username'], PDO::PARAM_STR);
	$stmt->execute();
	$result = $stmt->fetch(PDO::FETCH_NUM);
	
	if ($result) {
		$response["success"] = 0;
		$response["message"] = "User already exists in group!";
		die(json_encode($response));
	}
	
	// add user to group
	$stmt = $db->prepare("INSERT INTO groups_users ( group_id, username ) VALUES ( ?, ? )");
	$stmt->bindValue(1, $_POST['group_id'], PDO::PARAM_INT);
	$stmt->bindValue(2, $_POST['username'], PDO::PARAM_STR);
	$stmt->execute();
	
	$stmt = $db->prepare("DELETE FROM group_requests WHERE (group_id=? AND receiver=?)");
	$stmt->bindValue(1, $_POST['group_id'], PDO::PARAM_STR);
	$stmt->bindValue(2, $_POST['username'], PDO::PARAM_STR);
	$stmt->execute();
		
	$response["success"] = 1;
	$response["message"] = "Added user to group.";
	die(json_encode($response));
}
else {
	$response["success"] = 0;
	$response["message"] = "Error: invalid credentials!";
	die(json_encode($response));
}

?>