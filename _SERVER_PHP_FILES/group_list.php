<?php

//load and connect to MySQL database stuff
require_once("config.inc.php");

if (!empty($_POST) && !empty($_POST['username']) && !empty($_POST['password']) && 
	kite_check_login($db, $salt, $_POST['username'], $_POST['password'])) {
		
	// get all group_id's of user
	$stmt = $db->prepare("SELECT group_id FROM groups_users WHERE username = ?");
	$stmt->bindValue(1, $_POST['username'], PDO::PARAM_STR);
	$stmt->execute();
	$result = $stmt->fetchAll();

	if ($result) {
		$group_ids = [];
		foreach ($result as $r)
			$group_ids[] = current($r);
	}
	else {
		$response["groups"] = [];
		die(json_encode($response));
	}
	
	$in = join(',', $group_ids);
	$stmt = $db->prepare("SELECT id, name FROM groups WHERE id IN ( ".$in." )");
	$stmt->execute();
	$result = $stmt->fetchAll();
	
	if ($result) {
		$groups = [];
		foreach ($result as $r)
			$groups[] = $r;

		$response["groups"] = $groups;
		die(json_encode($response));
	}
}

$response["groups"] = [];
die(json_encode($response));

?>