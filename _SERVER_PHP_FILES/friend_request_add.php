<?php

//load and connect to MySQL database stuff
require_once("config.inc.php");

if (!empty($_POST) && !empty($_POST['username']) && !empty($_POST['password']) && !empty($_POST['friend']) && 
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
	
	// check if friendship already exists
	$stmt = $db->prepare("SELECT 1 FROM friend_requests WHERE (sender=? AND receiver=?)");
	$stmt->bindValue(1, $_POST['username'], PDO::PARAM_STR);
	$stmt->bindValue(2, $_POST['friend'], PDO::PARAM_STR);
	$stmt->execute();
	$result = $stmt->fetch(PDO::FETCH_NUM);
	
	if ($result) {
		$response["success"] = 0;
		$response["message"] = "Friendship request already exists!";
		die(json_encode($response));
	}
	
	// insert friendship request into db
	// set friend1 to the lesser string value
	$stmt = $db->prepare("INSERT INTO friend_requests (sender, receiver ) VALUES ( ?, ? )");
	$stmt->bindValue(1, $_POST['username'], PDO::PARAM_STR);
	$stmt->bindValue(2, $_POST['friend'], PDO::PARAM_STR);
	$stmt->execute();
		
	$response["success"] = 1;
	$response["message"] = "Friend request added.";
	die(json_encode($response));
}
else {
	$response["success"] = 0;
	$response["message"] = "Error: invalid credentials!";
	die(json_encode($response));
}

?>