<?php

//load and connect to MySQL database stuff
require_once("config.inc.php");

if (!empty($_POST) && !empty($_POST['username']) && !empty($_POST['password']) && !empty($_POST['player1']) && !empty($_POST['amount']) && !empty($_POST['request_id']) && !empty($_POST['group_id']) && 
	kite_check_login($db, $salt, $_POST['username'], $_POST['password'])) {

	$stmt = $db->prepare("DELETE FROM game_requests WHERE id=?");
	$stmt->bindValue(1, $_POST['request_id'], PDO::PARAM_INT);
	$stmt->execute();

	// check if game already exists
	$stmt = $db->prepare("SELECT 1 FROM tictacstate WHERE ((player1=? AND player2=?) OR (player1=? AND player2=?)) AND group_id=?");
	$stmt->bindValue(1, $_POST['player1'], PDO::PARAM_STR);
	$stmt->bindValue(2, $_POST['username'], PDO::PARAM_STR);
	$stmt->bindValue(3, $_POST['username'], PDO::PARAM_STR);
	$stmt->bindValue(4, $_POST['player1'], PDO::PARAM_STR);
	$stmt->bindValue(5, $_POST['group_id'], PDO::PARAM_INT);
	$stmt->execute();
	$result = $stmt->fetch(PDO::FETCH_NUM);
	
	if ($result) {
		$response["success"] = 0;
		$response["message"] = "Game already exists!";
		die(json_encode($response));
	}
	
	// insert game into db
	// set friend1 to the lesser string value
	$stmt = $db->prepare("INSERT INTO tictacstate ( player1, player2, amount, turn, group_id ) VALUES ( ?, ?, ?, ?, ? )");
	$stmt->bindValue(1, $_POST['player1'], PDO::PARAM_STR);
	$stmt->bindValue(2, $_POST['username'], PDO::PARAM_STR);
	$stmt->bindValue(3, floatval($_POST['amount']), PDO::PARAM_STR);
	$stmt->bindValue(4, $_POST['username'], PDO::PARAM_STR);
	$stmt->bindValue(5, $_POST['group_id'], PDO::PARAM_INT);
	$stmt->execute();

	$game_id = $db->lastInsertId();
	$response["game_id"] = $game_id;
		
	$response["success"] = 1;
	$response["message"] = "Game added.";
	die(json_encode($response));
}

$response["success"] = 0;
$response["message"] = "Error: invalid credentials!";
die(json_encode($response));

?>