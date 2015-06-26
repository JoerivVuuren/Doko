<?php

//load and connect to MySQL database stuff
require_once("config.inc.php");

if (!empty($_POST) && !empty($_POST['username']) && !empty($_POST['password']) && !empty($_POST['group_id']) &&
	!empty($_POST['debitor']) && !empty($_POST['creditor']) && !empty($_POST['debt']) &&
	kite_check_login($db, $salt, $_POST['username'], $_POST['password'])) {
	
	if (empty($_POST['origin']))
		$origin = "";
	else
		$origin = $_POST['origin'];

	if ($_POST['creditor'] == $_POST['debitor']) {
		$response["success"] = 0;
		$response["message"] = "Error: can't be in debt with yourself!";
		die(json_encode($response));
	}
	
	// check if debt exists
	$stmt = $db->prepare("SELECT creditor, debitor, debt FROM debts WHERE (group_id = ? AND ((creditor=? AND debitor=?) OR (creditor=? AND debitor=?)))");
	$stmt->bindValue(1, $_POST['group_id'], PDO::PARAM_INT);
	$stmt->bindValue(2, $_POST['creditor'], PDO::PARAM_STR);
	$stmt->bindValue(3, $_POST['debitor'], PDO::PARAM_STR);
	$stmt->bindValue(4, $_POST['debitor'], PDO::PARAM_STR);
	$stmt->bindValue(5, $_POST['creditor'], PDO::PARAM_STR);
	$stmt->execute();
	$result = $stmt->fetch(PDO::FETCH_NUM);
	
	$creditor = "";
	$debitor = "";
	$debt = floatval(str_replace(',', '.', $_POST['debt']));
	$debt_add = 0.0;
	
	if (!$debt) {
		$response["success"] = 0;
		$response["message"] = "Error: invalid debt value!";
		die(json_encode($response));
	}
	
	if ($result) {
		// debt exists
		$debt_add = $debt;
		$creditor = $result[0];
		$debitor = $result[1];
		$debt = $result[2];
		
		if ($creditor != $_POST['creditor']) {
			// swap debt sign
			$debt_add = -$debt_add;
		}
		$debt += $debt_add;
		
		// update debt in db
		$stmt = $db->prepare("UPDATE debts SET debt=? WHERE (group_id=? AND creditor=? AND debitor=?)");
		$stmt->bindValue(1, $debt, PDO::PARAM_STR);
		$stmt->bindValue(2, $_POST['group_id'], PDO::PARAM_INT);
		$stmt->bindValue(3, $creditor, PDO::PARAM_STR);
		$stmt->bindValue(4, $debitor, PDO::PARAM_STR);
		$stmt->execute();
			
		$response["success"] = 1;
		$response["message"] = "Debt updated.";
	}
	else {
		// debt does not exist yet
		
		// set creditor to the lesser string value
		if (strcmp($_POST['creditor'], $_POST['debitor']) < 0) {
			$creditor = $_POST['creditor'];
			$debitor = $_POST['debitor'];
		}
		else {
			// swap debt sign
			$creditor = $_POST['debitor'];
			$debitor = $_POST['creditor'];
			$debt = -$debt;
		}
		
		// insert debt into db
		$stmt = $db->prepare("INSERT INTO debts ( group_id, creditor, debitor, debt ) VALUES ( ?, ?, ?, ? )");
		$stmt->bindValue(1, $_POST['group_id'], PDO::PARAM_INT);
		$stmt->bindValue(2, $creditor, PDO::PARAM_STR);
		$stmt->bindValue(3, $debitor, PDO::PARAM_STR);
		$stmt->bindValue(4, $debt, PDO::PARAM_STR);
		$stmt->execute();
			
		$response["success"] = 1;
		$response["message"] = "Debt added.";
	}
	
	// insert debt history into db
	if ($debt_add == 0.0)
		$debt_add = $debt;
	
	$stmt = $db->prepare("INSERT INTO debt_history ( group_id, creditor, debitor, debt, datetime, origin ) VALUES ( ?, ?, ?, ?, ?, ? )");
	$stmt->bindValue(1, $_POST['group_id'], PDO::PARAM_INT);
	$stmt->bindValue(2, $creditor, PDO::PARAM_STR);
	$stmt->bindValue(3, $debitor, PDO::PARAM_STR);
	$stmt->bindValue(4, $debt_add, PDO::PARAM_STR);
	$stmt->bindValue(5, date("Y-m-d H:i:s"), PDO::PARAM_STR);
	$stmt->bindValue(6, $origin, PDO::PARAM_STR);
	$stmt->execute();
	
	if (!empty($_POST['request_id']) && !empty($_POST['type'])) {

		// remove request from requests list
		if ($_POST['type'] == "debit") {
			$stmt = $db->prepare("DELETE FROM debit_requests WHERE id=?");
		}
		else {
			$stmt = $db->prepare("DELETE FROM credit_requests WHERE id=?");
		}
		$stmt->bindValue(1, $_POST['request_id'], PDO::PARAM_INT);
		$stmt->execute();
	}
	
	die(json_encode($response));
}
else {
	$response["success"] = 0;
	$response["message"] = "Error: invalid credentials!";
	die(json_encode($response));
}

?>