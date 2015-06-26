<?php

//load and connect to MySQL database stuff
require_once("config.inc.php");

if (!empty($_POST) && !empty($_POST['username']) && !empty($_POST['password']) && !empty($_POST['group_id']) && 
	kite_check_login($db, $salt, $_POST['username'], $_POST['password'])) {
		
	// get all group_id's of user
	$stmt = $db->prepare("SELECT player1, player2, amount, id FROM tictacstate WHERE (player1=? OR player2=?) AND group_id=?");
	$stmt->bindValue(1, $_POST['username'], PDO::PARAM_STR);
	$stmt->bindValue(2, $_POST['username'], PDO::PARAM_STR);
	$stmt->bindValue(3, $_POST['group_id'], PDO::PARAM_INT);

	$response["opponent"] = [];
	$response["wager"] = [];
	$response["game_id"] = [];

	if ($stmt->execute()) {
		while ($row = $stmt->fetch(PDO::FETCH_NUM)) {
			$player1 = $row[0];
			$player2 = $row[1];
			$amount = floatval(str_replace(',', '.', $row[2]));
			$game_id = $row[3];

			if ($player1 == $_POST['username']) {
				$response["opponent"][] = $player2;
				$response["wager"][] = $amount;
				$response["game_id"][] = $game_id;
			}
			else {
				$response["opponent"][] = $player1;
				$response["wager"][] = $amount;
				$response["game_id"][] = $game_id;
			}
		}
		die(json_encode($response));
	}
	else {
		$response["opponent"] = [];
		$response["wager"] = [];
		$response["game_id"] = [];
		die(json_encode($response));
	}
}

$response["opponent"] = [];
$response["wager"] = [];
$response["game_id"] = [];
die(json_encode($response));

?>