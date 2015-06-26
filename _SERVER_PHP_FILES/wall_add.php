<?php

//load and connect to MySQL database stuff
require_once("config.inc.php");

if (!empty($_POST) && !empty($_POST['username']) && !empty($_POST['password']) && !empty($_POST['group_id']) && !empty($_POST['type']) &&
	!empty($_POST['player1']) &&
	kite_check_login($db, $salt, $_POST['username'], $_POST['password'])) {
	
	if (empty($_POST['player2']))
		$player2 = "";
	else
		$player2 = $_POST['player2'];
		
	if (empty($_POST['amount']))
		$amount = 0.0;
	else
		$amount = floatval(str_replace(',', '.', $_POST['amount']));
	
	
	$stmt = $db->prepare("INSERT INTO wall ( group_id, player1, player2, amount, datetime, type ) VALUES ( ?, ?, ?, ?, ?, ? )");
	$stmt->bindValue(1, $_POST['group_id'], PDO::PARAM_INT);
	$stmt->bindValue(2, $_POST['player1'], PDO::PARAM_STR);
	$stmt->bindValue(3, $player2, PDO::PARAM_STR);
	$stmt->bindValue(4, $amount, PDO::PARAM_STR);
	$stmt->bindValue(5, date("Y-m-d H:i:s"), PDO::PARAM_STR);
	$stmt->bindValue(6, $_POST['type'], PDO::PARAM_INT);
	$stmt->execute();
	
	$response["success"] = 1;
	die(json_encode($response));
}
else {
	$response["success"] = 0;
	$response["message"] = "Error: invalid credentials!";
	die(json_encode($response));
}

?>