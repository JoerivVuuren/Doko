<?php

//load and connect to MySQL database stuff
require_once("config.inc.php");

if (!empty($_POST) && !empty($_POST['username']) && !empty($_POST['password']) && !empty($_POST['group_id']) && !empty($_POST['user']) &&
	kite_check_login($db, $salt, $_POST['username'], $_POST['password'])) {
	
	// check if debt exists
	$stmt = $db->prepare("SELECT creditor, debitor, debt, datetime, origin FROM debt_history WHERE (group_id = ? AND (creditor=? OR debitor=?))");
	$stmt->bindValue(1, $_POST['group_id'], PDO::PARAM_INT);
	$stmt->bindValue(2, $_POST['user'], PDO::PARAM_STR);
	$stmt->bindValue(3, $_POST['user'], PDO::PARAM_STR);

	$response["user"] = $_POST['user'];
	$response["h_opponent"] = [];
	$response["h_amount"] = [];
	$response["h_datetime"] = [];
	$response["h_reason"] = [];	
	if ($stmt->execute()) {
		
		while ($row = $stmt->fetch(PDO::FETCH_NUM)) {
			$creditor = $row[0];
			$debitor = $row[1];
			$debt = floatval(str_replace(',', '.', $row[2]));
			$datetime = $row[3];
			$origin = $row[4];
			
			if ($creditor == $_POST['user']) {
				$response["h_opponent"][] = $debitor;
				$response["h_amount"][] = $debt;
			}
			else {
				$response["h_opponent"][] = $creditor;
				$response["h_amount"][] = -$debt;
			}
			$response["h_datetime"][] = $datetime;
			$response["h_reason"][] = $origin;
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