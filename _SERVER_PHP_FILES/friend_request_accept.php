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
	$stmt = $db->prepare("SELECT 1 FROM friends WHERE (friend1=? AND friend2=?) OR (friend1=? AND friend2=?)");
	$stmt->bindValue(1, $_POST['friend'], PDO::PARAM_STR);
	$stmt->bindValue(2, $_POST['username'], PDO::PARAM_STR);
	$stmt->bindValue(3, $_POST['username'], PDO::PARAM_STR);
	$stmt->bindValue(4, $_POST['friend'], PDO::PARAM_STR);
	$stmt->execute();
	$result = $stmt->fetch(PDO::FETCH_NUM);
	
	if ($result) {
		$response["success"] = 0;
		$response["message"] = "Friendship already exists!";
		die(json_encode($response));
	}
	
	// insert friendship into db
	// set friend1 to the lesser string value
	if (strcmp($_POST['username'], $_POST['friend']) < 0) {
		$friend1 = $_POST['friend'];
		$friend2 = $_POST['username'];
	}
	else {
		$friend1 = $_POST['username'];
		$friend2 = $_POST['friend'];
	}
	$stmt = $db->prepare("INSERT INTO friends ( friend1, friend2 ) VALUES ( ?, ? )");
	$stmt->bindValue(1, $friend1, PDO::PARAM_STR);
	$stmt->bindValue(2, $friend2, PDO::PARAM_STR);
	$stmt->execute();
	
	$stmt = $db->prepare("DELETE FROM friend_requests WHERE (sender=? AND receiver=?)");
	$stmt->bindValue(1, $_POST['friend'], PDO::PARAM_STR);
	$stmt->bindValue(2, $_POST['username'], PDO::PARAM_STR);
	$stmt->execute();
		
	$response["success"] = 1;
	$response["message"] = "Friendship added.";
	die(json_encode($response));
}
else {
	$response["success"] = 0;
	$response["message"] = "Error: invalid credentials!";
	die(json_encode($response));
}

?>