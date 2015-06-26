<?php

//load and connect to MySQL database stuff
require_once("config.inc.php");

if (!empty($_POST) && !empty($_POST['username']) && !empty($_POST['password']) && !empty($_POST['game_id']) && 
	kite_check_login($db, $salt, $_POST['username'], $_POST['password'])) {
	
	// check if debt exists
	$stmt = $db->prepare("SELECT veld0, veld1, veld2, veld3, veld4, veld5, veld6, veld7, veld8, player1, player2, amount, turn FROM tictacstate WHERE id=?");
	$stmt->bindValue(1, $_POST['game_id'], PDO::PARAM_INT);


	if ($stmt->execute()) {
		while ($row = $stmt->fetch(PDO::FETCH_NUM)) {
			$veld0 = $row[0];
			$veld1 = $row[1];
			$veld2 = $row[2];
			$veld3 = $row[3];
			$veld4 = $row[4];
			$veld5 = $row[5];
			$veld6 = $row[6];
			$veld7 = $row[7];
			$veld8 = $row[8];
			$player1 = $row[9];
			$player2 = $row[10];
			$amount = $row[11];
			$turn = $row[12];
			$response["veld0"] = $veld0;
			$response["veld1"] = $veld1;
			$response["veld2"] = $veld2;
			$response["veld3"] = $veld3;
			$response["veld4"] = $veld4;
			$response["veld5"] = $veld5;
			$response["veld6"] = $veld6;
			$response["veld7"] = $veld7;
			$response["veld8"] = $veld8;
			$response["game_id"] = $_POST['game_id'];
			$response["player1"] = $player1;
			$response["player2"] = $player2;
			$response["amount"] = $amount;
			$response["turn"] = $turn;
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