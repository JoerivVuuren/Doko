<?php

//load and connect to MySQL database stuff
require_once("config.inc.php");

if (!empty($_POST) && !empty($_POST['username']) && !empty($_POST['password']) && !empty($_POST['group_id']) &&
	kite_check_login($db, $salt, $_POST['username'], $_POST['password'])) {

	$stmt = $db->prepare("SELECT player1, player2, amount, datetime, type FROM wall WHERE group_id = ?");
	$stmt->bindValue(1, $_POST['group_id'], PDO::PARAM_INT);

	$response["player1"] = [];
	$response["player2"] = [];
	$response["amount"] = [];
	$response["datetime"] = [];
	$response["type"] = [];
	if ($stmt->execute()) {
		while ($row = $stmt->fetch(PDO::FETCH_NUM)) {
			$player1 = $row[0];
			$player2 = $row[1];
			$amount = floatval($row[2]);
			$datetime = $row[3];
			$type = $row[4];
			$response["player1"][] = $player1;
			$response["player2"][] = $player2;
			$response["amount"][] = $amount;
			$response["datetime"][] = $datetime;
			$response["type"][] = $type;
		}
	}
	
	die(json_encode($response));
}
else {
	$response["success"] = 0;
	$response["message"] = "Error: invalid credentials!";
	die(json_encode($response));
}

?>