<?php

//load and connect to MySQL database stuff
require_once("config.inc.php");

if (!empty($_POST) && !empty($_POST['username']) && !empty($_POST['password']) && !empty($_POST['group_id']) && 
	kite_check_login($db, $salt, $_POST['username'], $_POST['password'])) {
		
	// get all members of group_id
	$stmt = $db->prepare("SELECT groups_users.username, users.profile_picture FROM groups_users INNER JOIN users
						  ON groups_users.username = users.username WHERE group_id =?");
	$stmt->bindValue(1, $_POST['group_id'], PDO::PARAM_INT);

	$response["users"] = [];

	if ($stmt->execute()) {
		while ($row = $stmt->fetch(PDO::FETCH_NUM)) {
			$rowuser = $row[0];
			$response["users"][] = $rowuser;

			if (empty($row[1]))
				$response["profile_picture"][] = "";
			else
				$response["profile_picture"][] = $row[1];

			// check if debt exists
			$stmt2 = $db->prepare("SELECT creditor, debitor, debt FROM debts WHERE (group_id = ? AND (creditor=? OR debitor=?))");
			$stmt2->bindValue(1, $_POST['group_id'], PDO::PARAM_INT);
			$stmt2->bindValue(2, $rowuser, PDO::PARAM_STR);
			$stmt2->bindValue(3, $rowuser, PDO::PARAM_STR);
			
			$debt = 0.0;
			if ($stmt2->execute()) {
				
				/* accumulate all debts for this user */
				while ($row2 = $stmt2->fetch(PDO::FETCH_NUM)) {
					$creditor = $row2[0];
					$debitor = $row2[1];
					$thisdebt = floatval($row2[2]);

					if ($creditor != $rowuser)
						$thisdebt = -$thisdebt;
					
					$debt += $thisdebt;
				}
			}
			$response["debts"][] = $debt;
    	}
    }
	// get admin_name for group
	$stmt3 = $db->prepare("SELECT admin_name FROM groups WHERE id = ?");
	$stmt3->bindValue(1, $_POST['group_id'], PDO::PARAM_INT);
	$stmt3->execute();
	$result = $stmt3->fetch(PDO::FETCH_NUM);
	$response["admin_name"] = current($result);

	die(json_encode($response));
}

?>