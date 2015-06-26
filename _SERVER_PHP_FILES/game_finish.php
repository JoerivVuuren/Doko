<?php

//load and connect to MySQL database stuff
require_once("config.inc.php");

if (!empty($_POST) && !empty($_POST['username']) && !empty($_POST['password']) && !empty($_POST['game_id']) && !empty($_POST['winner']) &&
	kite_check_login($db, $salt, $_POST['username'], $_POST['password'])) {

	// check if game already exists
	$stmt = $db->prepare("SELECT amount, group_id, player1, player2 FROM tictacstate WHERE id=?");
	$stmt->bindValue(1, $_POST['game_id'], PDO::PARAM_INT);
    	
    if ($stmt->execute()) {
		while ($row = $stmt->fetch(PDO::FETCH_NUM)) {
			$amount = $row[0];
            $group_id = $row[1];
            $player1 = $row[2];
            $player2 = $row[3];
			$response["group_id"] = $group_id;
			$response["player1"] = $player1;
			$response["player2"] = $player2;
			$response["amount"] = $amount;
        }
    }  
    
    // insert game update into db
	$stmt = $db->prepare("DELETE FROM tictacstate WHERE id=?");
    $stmt->bindValue(1, $_POST['game_id'], PDO::PARAM_INT);
	$stmt->execute();

    if($player1 == $_POST['winner']) {
        $loser = $player2;
        $winner = $player1;
        $response["winner"] = $winner;
        $response["loser"] = $loser;
    } else if($player2 == $_POST['winner']) {
        $loser = $player1;
        $winner = $player2;
        $response["winner"] = $winner;
        $response["loser"] = $loser;
    } else {
    	// Make sure we always return the other player (in loser for simplicity, handled in android code)
    	if($_POST['username'] == $player1) {
    		$loser = $player2;
    	} else {
    		$loser = $player1;
    	}
    	$response["success"] = 1;
		$response["message"] = "Game finished, debt restored to original value.";
		$winner = "none";
		$response["winner"] = $winner;
        $response["loser"] = $loser;
		die(json_encode($response));
    }

	$response["success"] = 1;
	$response["message"] = "Game finished, debt added.";
	die(json_encode($response));
}

$response["success"] = 0;
$response["message"] = "Error: invalid credentials!";
die(json_encode($response));

?>