 //Ověření na ostatních stránkách
<?php
session_start();
if(!isset($_SESSION['jmeno']) or !isset($_SESSION['heslo'])){
echo'<form action="./prihlaseni.php" method="post">
<input type="text" name="jmeno" placeholder="Nick na serveru">
<input type="password" name="heslo" placeholder="Heslo" >
<button>Přihlásit se!</button>
</form>
';}  
elseif(isset($_SESSION['jmeno']) && isset($_SESSION['heslo']))
    {
        $username = $_SESSION['jmeno'];
        $result = mysqli_query($mysqlXs, "SELECT password FROM authme WHERE username='$username'");
        $vysledek = mysqli_fetch_assoc($result);
   
    if($vysledek['password'] == $_SESSION['heslo'])
    {
        echo"Jsi přihlášený jako ". $_SESSION['jmeno'] ."";
    }
    else {
        echo"Byl jsi odhlášený";
         }
    }
?>
//přihlášení.php
<?php 
$serverXw = "..."; // jméno serveru
$loginXw = "..."; // přihlašovací jméno
$hesloXw = "..."; // heslo
$databazeXw = "..."; // název databáze
$mysqlXw = new mysqli("$serverXw", "$loginXw", "$hesloXw", "$databazeXw"); // Připojení k databázy


session_start();
if(empty($_POST['jmeno']) or empty($_POST['heslo'])){
echo'Ouha! Musíš vyplnit obě pole!';}  
if(isset($_POST['jmeno']) && isset($_POST['heslo']))
    {
        $username = $_POST['jmeno'];
        $result = mysqli_query($mysqlXs, "SELECT password FROM authme WHERE username='$username'");
        $vysledek = mysqli_fetch_assoc($result);
   
    if($vysledek['password'] == md5($_POST['heslo']))
    {
         $_SESSION['jmeno'] = $_POST['jmeno'];
  	 $_SESSION['heslo'] = md5($_POST['heslo']);
		 header('location: index.php');
    }
    else {
        echo"Ouha! Zadal jsi špatné heslo!";
         }
    }
?>
<form action="./prihlaseni.php" method="post">
<input type="text" name="jmeno" placeholder="Přezdívka">
<input type="password" name="heslo" placeholder="Heslo" >
<button>Přihlásit se!</button>
</form>

