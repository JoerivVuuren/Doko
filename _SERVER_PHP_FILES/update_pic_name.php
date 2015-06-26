<?php
//load and connect to MySQL database stuff
require_once("config.inc.php");

if (!empty($_POST) && !empty($_POST['username']) && !empty($_POST['password']) && 
	kite_check_login($db, $salt, $_POST['username'], $_POST['password'])) {

    $stmt = $db->prepare("UPDATE users SET profile_picture=? WHERE username=?");
    $stmt->bindValue(1, $_POST['filename'], PDO::PARAM_STR);
    $stmt->bindValue(2, $_POST['username'], PDO::PARAM_STR);
    $stmt->execute();
	die();
}

?>