<?php

//load and connect to MySQL database stuff
require_once("config.inc.php");

if (!empty($_POST)) {
    // If the user logged in successfully, then we send them to the private members-only page 
    // Otherwise, we display a login failed message and show the login form again 
    if (!empty($_POST['username']) && !empty($_POST['password']) &&
		kite_check_login($db, $salt, $_POST['username'], $_POST['password'])) {
        $response["success"] = 1;
        $response["message"] = "Login successful!";
        die(json_encode($response));
    }
	else {
        $response["success"] = 0;
        $response["message"] = "Invalid credentials!";
        die(json_encode($response));
    }
} else {
?>
		<h1>Login</h1> 
		<form action="login.php" method="post"> 
		    Username:<br /> 
		    <input type="text" name="username" placeholder="username" /> 
		    <br /><br /> 
		    Password:<br /> 
		    <input type="password" name="password" placeholder="password" value="" /> 
		    <br /><br /> 
		    <input type="submit" value="Login" /> 
		</form> 
		<a href="register.php">Register</a>
	<?php
}

?> 
