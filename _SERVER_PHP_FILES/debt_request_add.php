<?php

//load and connect to MySQL database stuff
require_once("config.inc.php");

if (!empty($_POST) && !empty($_POST['username']) && !empty($_POST['password']) && !empty($_POST['friend']) &&
    !empty($_POST['debt']) && !empty($_POST['group_id']) && !empty($_POST['type']) && 
	kite_check_login($db, $salt, $_POST['username'], $_POST['password'])) {
	
	$origin = "";
	if (!empty($_POST['origin']))
		$origin = $_POST['origin'];
		
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
	
	$type = "credit_requests";
	if ($_POST['type'] == "debit")
		$type = "debit_requests";
	
	// insert debit request into db
	$stmt = $db->prepare("INSERT INTO " . $type . " (sender, receiver, amount, group_id, origin) VALUES ( ?, ?, ?, ?, ? )");
	$stmt->bindValue(1, $_POST['username'], PDO::PARAM_STR);
	$stmt->bindValue(2, $_POST['friend'], PDO::PARAM_STR);
	$stmt->bindValue(3, floatval(str_replace(',', '.', $_POST['debt'])), PDO::PARAM_STR);
	$stmt->bindValue(4, $_POST['group_id'], PDO::PARAM_INT);
	$stmt->bindValue(5, $origin, PDO::PARAM_STR);
	$stmt->execute();
		
	$response["success"] = 1;
	$response["message"] = ucfirst($_POST['type']) . " request added.";
	die(json_encode($response));
}
else {
	$response["success"] = 0;
	$response["message"] = "Error: invalid credentials!";
	die(json_encode($response));
}

?>