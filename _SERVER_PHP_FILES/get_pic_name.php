<?php
//load and connect to MySQL database stuff
require_once("config.inc.php");

if (!empty($_POST) && !empty($_POST['username']) && !empty($_POST['password']) && 
	kite_check_login($db, $salt, $_POST['username'], $_POST['password'])) {

    $stmt = $db->prepare("SELECT profile_picture FROM users WHERE username=?");
    $stmt->bindValue(1, $_POST['username'], PDO::PARAM_STR);
    $stmt->execute();
   	$result = $stmt->fetch();

   	if($result) {
   		$response["profile_picture"] = current($result);
   		die(json_encode($response));
   	}
   	die("No filename found!");
}
else die();

?>