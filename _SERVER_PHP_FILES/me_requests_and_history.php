<?php

//load and connect to MySQL database stuff
require_once("config.inc.php");

if (!empty($_POST) && !empty($_POST['username']) && !empty($_POST['password']) && !empty($_POST['group_id']) &&
	kite_check_login($db, $salt, $_POST['username'], $_POST['password'])) {

	// get game requests
	$stmt = $db->prepare("SELECT id, sender, amount FROM game_requests WHERE receiver = ? AND group_id = ?");
	$stmt->bindValue(1, $_POST['username'], PDO::PARAM_STR);
	$stmt->bindValue(2, $_POST['group_id'], PDO::PARAM_INT);

	$response["game_id"] = [];
	$response["game_sender"] = [];
	$response["game_amount"] = [];
	if ($stmt->execute()) {
		while ($row = $stmt->fetch(PDO::FETCH_NUM)) {
			$id = $row[0];
			$sender = $row[1];
			$amount = floatval($row[2]);
			$response["game_id"][] = $id;
			$response["game_sender"][] = $sender;
			$response["game_amount"][] = $amount;
		}
	}
	
	// get debit requests
	$stmt = $db->prepare("SELECT id, sender, amount, origin FROM debit_requests WHERE receiver = ? AND group_id = ?");
	$stmt->bindValue(1, $_POST['username'], PDO::PARAM_STR);
	$stmt->bindValue(2, $_POST['group_id'], PDO::PARAM_INT);

	$response["debit_id"] = [];
	$response["debit_sender"] = [];
	$response["debit_debt"] = [];
	$response["debit_reason"] = [];
	if ($stmt->execute()) {
		while ($row = $stmt->fetch(PDO::FETCH_NUM)) {
			$id = $row[0];
			$sender = $row[1];
			$amount = floatval($row[2]);
			$reason = $row[3];
			$response["debit_id"][] = $id;
			$response["debit_sender"][] = $sender;
			$response["debit_debt"][] = $amount;
			$response["debit_reason"][] = $reason;
		}
	}

	// get credit requests
	$stmt = $db->prepare("SELECT id, sender, amount, origin FROM credit_requests WHERE receiver = ? AND group_id = ?");
	$stmt->bindValue(1, $_POST['username'], PDO::PARAM_STR);
	$stmt->bindValue(2, $_POST['group_id'], PDO::PARAM_INT);

	$response["credit_id"] = [];
	$response["credit_sender"] = [];
	$response["credit_debt"] = [];
	$response["credit_reason"] = [];
	if ($stmt->execute()) {
		while ($row = $stmt->fetch(PDO::FETCH_NUM)) {
			$id = $row[0];
			$sender = $row[1];
			$amount = floatval($row[2]);
			$reason = $row[3];
			$response["credit_id"][] = $id;
			$response["credit_sender"][] = $sender;
			$response["credit_debt"][] = $amount;
			$response["credit_reason"][] = $reason;
		}
	}
	
	// get debt history
	$stmt = $db->prepare("SELECT group_id, creditor, debitor, debt, datetime, origin FROM debt_history WHERE ((creditor=? OR debitor=?) AND group_id=?)");
	$stmt->bindValue(1, $_POST['username'], PDO::PARAM_STR);
	$stmt->bindValue(2, $_POST['username'], PDO::PARAM_STR);
	$stmt->bindValue(3, $_POST['group_id'], PDO::PARAM_INT);
	
	$response["h_group_id"] = [];
	$response["h_opponent"] = [];
	$response["h_amount"] = [];
	$response["h_datetime"] = [];
	$response["h_reason"] = [];
	if ($stmt->execute()) {

		while ($row = $stmt->fetch(PDO::FETCH_NUM)) {
			$group_id = $row[0];
			$creditor = $row[1];
			$debitor = $row[2];
			$debt = floatval(str_replace(',', '.', $row[3]));
			$datetime = $row[4];
			$origin = $row[5];
			
			$response["h_group_id"][] = $group_id;
			if ($creditor == $_POST['username']) {
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

$response["debit_id"] = [];
$response["debit_sender"] = [];
$response["debit_debt"] = [];
$response["debit_reason"] = [];
$response["credit_id"] = [];
$response["credit_sender"] = [];
$response["credit_debt"] = [];
$response["credit_reason"] = [];
$response["game_id"] = [];
$response["game_sender"] = [];
$response["game_amount"] = [];
die(json_encode($response));

?>