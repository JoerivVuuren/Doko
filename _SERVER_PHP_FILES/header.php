<div class="div_shadowed">
    <div id="logo"><a href="/index.php"><img id="companylogo" src="/img/logo.png" alt="" border="0"><span id="companyname"><?=$company_name?></span></a></div>
    <div id="nav">
    <span class="navbtn">
        <a href="/index.php">Home</a>
        <?

        # Display appropriate buttons
        if ($logged_in) {

            echo "<a href=\"/profile.php\">My Profile</a> ";
            
            # Display link to admin panel if user is admin
            if ($user_role == 3) {
                echo "<a href=\"/admin/admin.php\">Admin Panel</a> ";
            }
            
            echo "<a href=\"/logout.php\">Logout</a>";
        
        } else {
            
            echo "<a href=\"/register.php\">Register</a> ";
            echo "<a href=\"/login.php\">Login</a>";
        
        }

        ?>
    </span>
    </div>
</div>