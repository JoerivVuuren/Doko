<?php

//load and connect to MySQL database stuff
require_once("config.inc.php");

if (!empty($_POST) && !empty($_POST['username']) && !empty($_POST['password']) && 
	kite_check_login($db, $salt, $_POST['username'], $_POST['password'])) {
		
	// get all group requests of user
	$stmt = $db->prepare("SELECT group_name, group_id FROM group_requests WHERE receiver = ?");
	$stmt->bindValue(1, $_POST['username'], PDO::PARAM_STR);

	$response["group_name"] = [];
	$response["group_id"] = [];
	if ($stmt->execute()) {
		while ($row = $stmt->fetch(PDO::FETCH_NUM)) {
			$name = $row[0];
			$id = $row[1];
			$response["group_name"][] = $name;
			$response["group_id"][] = $id;
		}
	}
	die(json_encode($response));
}

$response["group_id"] = [];
$response["group_name"] = [];
die(json_encode($response));

?>