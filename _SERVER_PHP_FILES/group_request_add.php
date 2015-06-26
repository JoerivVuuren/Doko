<?php

//load and connect to MySQL database stuff
require_once("config.inc.php");

if (!empty($_POST) && !empty($_POST['username']) && !empty($_POST['password']) && !empty($_POST['friend']) && !empty($_POST['group_id']) &&  !empty($_POST['group_name']) && 
	kite_check_login($db, $salt, $_POST['username'], $_POST['password'])) {
		
	// check if username exists
	$stmt = $db->prepare("SELECT 1 FROM users WHERE username=?");
	$stmt->bindValue(1, $_POST['friend'], PDO::PARAM_STR);
	$stmt->execute();
	$result = $stmt->fetch(PDO::FETCH_NUM);
	
	if (!$result || $_POST['username'] == $_POST['friend']) {
		$response["success"] = 0;
		$response["message"] = "Error: unknown username!";
		die(json_encode($response));
	}
	
	// check if group request already exists
	$stmt = $db->prepare("SELECT 1 FROM group_requests WHERE (receiver=? AND group_id=?)");
	$stmt->bindValue(1, $_POST['friend'], PDO::PARAM_STR);
	$stmt->bindValue(2, $_POST['group_id'], PDO::PARAM_STR);
	$stmt->execute();
	$result = $stmt->fetch(PDO::FETCH_NUM);
	
	if ($result) {
		$response["success"] = 0;
		$response["message"] = "Group request already exists!";
		die(json_encode($response));
	}
	
	// insert group request request into db
	// set friend1 to the lesser string value
	$stmt = $db->prepare("INSERT INTO group_requests (group_id, receiver, group_name ) VALUES ( ?, ?, ? )");
	$stmt->bindValue(1, $_POST['group_id'], PDO::PARAM_INT);
	$stmt->bindValue(2, $_POST['friend'], PDO::PARAM_STR);
	$stmt->bindValue(3, $_POST['group_name'], PDO::PARAM_STR);
	$stmt->execute();
		
	$response["success"] = 1;
	$response["message"] = "Group request sent.";
	die(json_encode($response));
}
else {
	$response["success"] = 0;
	$response["message"] = "Error: invalid credentials!";
	die(json_encode($response));
}

?>