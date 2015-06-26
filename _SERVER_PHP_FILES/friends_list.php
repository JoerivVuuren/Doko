<?php

//load and connect to MySQL database stuff
require_once("config.inc.php");

if (!empty($_POST) && !empty($_POST['username']) && !empty($_POST['password']) && 
	kite_check_login($db, $salt, $_POST['username'], $_POST['password'])) {
		
	// get all friendships of user
	$stmt = $db->prepare("SELECT friend1 FROM friends WHERE friend2 = ? UNION ALL SELECT friend2 FROM friends WHERE friend1 = ?");
	$stmt->bindValue(1, $_POST['username'], PDO::PARAM_STR);
	$stmt->bindValue(2, $_POST['username'], PDO::PARAM_STR);
	$stmt->execute();
	$result = $stmt->fetchAll();

	if ($result) {
		$friends = [];
		foreach ($result as $f)
			$friends[] = current($f);

		$response["friends"] = $friends;
		die(json_encode($response));
	}
}

$response["friends"] = [];
die(json_encode($response));

?>