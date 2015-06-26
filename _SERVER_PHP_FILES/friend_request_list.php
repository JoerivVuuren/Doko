<?php

//load and connect to MySQL database stuff
require_once("config.inc.php");

if (!empty($_POST) && !empty($_POST['username']) && !empty($_POST['password']) && 
	kite_check_login($db, $salt, $_POST['username'], $_POST['password'])) {
		
	// get all friendships of user
	$stmt = $db->prepare("SELECT sender FROM friend_requests WHERE receiver = ?");
	$stmt->bindValue(1, $_POST['username'], PDO::PARAM_STR);
	$stmt->execute();
	$result = $stmt->fetchAll();

	if ($result) {
		$senders = [];
		foreach ($result as $f)
			$senders[] = current($f);

		$response["senders"] = $senders;
		die(json_encode($response));
	}
}

$response["senders"] = [];
die(json_encode($response));

?>