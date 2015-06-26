<?php

//load and connect to MySQL database stuff
require_once("config.inc.php");

if (!empty($_POST) && !empty($_POST['username']) && !empty($_POST['password']) && !empty($_POST['game_id']) && !empty($_POST['turn']) &&
	kite_check_login($db, $salt, $_POST['username'], $_POST['password'])) {

	// check if game already exists
	$stmt = $db->prepare("SELECT 1 FROM tictacstate WHERE id=?");
	$stmt->bindValue(1, $_POST['game_id'], PDO::PARAM_INT);
	$stmt->execute();
	$result = $stmt->fetch(PDO::FETCH_NUM);
	
	if (!$result) {
		$response["success"] = 0;
		$response["message"] = "Game does not exist!";
		die(json_encode($response));
	}
	
	// insert game update into db
	$stmt = $db->prepare("UPDATE tictacstate SET veld0 = ?, veld1 = ?, veld2 = ?, veld3 = ?, veld4 = ?, veld5 = ?, veld6 = ?, veld7 = ?, veld8 = ?, turn = ? WHERE id=?");
	$stmt->bindValue(1, $_POST['veld0'], PDO::PARAM_INT);
	$stmt->bindValue(2, $_POST['veld1'], PDO::PARAM_INT);
	$stmt->bindValue(3, $_POST['veld2'], PDO::PARAM_INT);
	$stmt->bindValue(4, $_POST['veld3'], PDO::PARAM_INT);
	$stmt->bindValue(5, $_POST['veld4'], PDO::PARAM_INT);
	$stmt->bindValue(6, $_POST['veld5'], PDO::PARAM_INT);
	$stmt->bindValue(7, $_POST['veld6'], PDO::PARAM_INT);
	$stmt->bindValue(8, $_POST['veld7'], PDO::PARAM_INT);
	$stmt->bindValue(9, $_POST['veld8'], PDO::PARAM_INT);
	$stmt->bindValue(10, $_POST['turn'], PDO::PARAM_STR);
	$stmt->bindValue(11, $_POST['game_id'], PDO::PARAM_INT);
	$stmt->execute();
		
	$response["success"] = 1;
	$response["message"] = "Move made.";
	die(json_encode($response));
} 
else {
	$response["success"] = 0;
	$response["message"] = "Error: invalid credentials!";
	die(json_encode($response));
}



?>