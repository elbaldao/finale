<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>   
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtda">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Italianos project</title>
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>
<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
<link rel="stylesheet" href="style.css">
<link rel="shortcut icon" href="italia.png" />
</head>

<body>

<div class="container-fluid">
	<!-- Modal -->
	<div class="modal fade" id="exampleModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
	  <div class="modal-dialog" role="document">
	    <div class="modal-content">
	      <div class="modal-header">
	        <h5 class="modal-title" id="exampleModalLabel">Operazione riuscita con successo</h5>
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
	          <span aria-hidden="true">&times;</span>
	        </button>
	      </div>
	      <div id="modal-body" class="modal-body">	      
	      </div>
	      <div class="modal-footer">
	      </div>
	    </div>
	  </div>
	</div>
	
	<!--  Jumbotron spiegazione-->
	<div id="jumbo" class="jumbotron">
	  <h1 class="display-4">Buongiorno visitatore!</h1>
	  <p class="lead">Benvenuto nel nostra webpage. Qui potrai eseguire una serie di azioni su un testo, tra cui la traduzione, analizzarlo grazie al test dei tratti della personalità dei Big 5, ottenere un file audio e molto altro.</p>
	  <hr class="my-4">
	  <p>Ti auguriamo una buona permanenza nel nostro sito, effettua il login qua sotto</p>
	</div>
	
	<!--  Login -->
	<div id="signin">
		<h3>Effettua il login: </h3>
		<form action="log" method="POST">
			<div class="form-group">
				<label>Inserire username: </label><input class="form-control" type="text" name="username">
				<label id="lbl2">Inserire password: </label><input class="form-control" type="password" name="password">
				<p id="erroreCredErr" class="error"></p>
			</div>
			<button class="btn btn-primary" type="submit" >Login</button>
		</form> 
		<div>
			<p>Non hai un account?<button type="button" id="btnSignUp" class="btn btn-link">Registrati!</button></p>
		</div>
	</div>
	
	<!--  Register -->
	<div id="signup">
	<h3>Registrati: </h3>
		<form action="add" method="POST" id="form">
			<div class="form-group">
				<label>Inserire username: </label>
				<input class="form-control inp" type="text" name="username" id="usr"> <p class="error" id="errorusr"></p>
				<label id="lbl">Inserire password (almeno 8 caratteri): </label><input id="psw" class="form-control inp" type="password" name="password" id="psw"> <p class="error" id="errorpsw1"></p>
				<label id="lbl">Reinserire password: </label><input id="psw2" class="form-control inp" type="password" name="password2" id="psw2"> <p class="error" id="errorpsw2"></p>
			</div>
			<button class="btn btn-primary" type="submit" value="Invia" id="invia" >Register</button>
			<p id="p" class="error"></p>
		</form>
	 </div>
	 
	 <!-- Button trigger modal -->
	<button type="button" id="modal" class="btn btn-primary" data-toggle="modal" data-target="#exampleModal"></button>
</div>

 </body>
 </html>
 
<script>
var query = window.location.search.substring(1);

var check1 = false;
var check2 = false;

$(document).ready(function(){
      	
		//Controllo parametri
		
		if (query=="e") $("#erroreCredErr").text("Credenziali errate!");
		if (query=="u"){ 
			$("#errorusr").text("Utente esistente");
			$("#signup").css("display", "block");
			$("#signin").css("display", "none");
		}
		if (query.split("=")[0]=="n"){
			$("#modal").trigger('click');
			var str = "Utente ";
			var res = str.concat(window.location.search.substring(1).split("=")[1], " registrato")
			$("#modal-body").text(res);
		}
				
		//Script pulsanti
		
		$("#psw").keyup(function(){
		    if ($(this).val().length < 8) {
				$("#errorpsw1").css('color', 'red')
		    	$("#errorpsw1").text("Password troppo corta");
				
		    	check1 = false;
		    }
		    else {
		    	$("#errorpsw1").text("Perfect");
				$("#errorpsw1").css('color', 'green')
		    	check1 = true
		    }
		  });
	  
		
	  	$("#psw2").keyup(function(){
		    if ($(this).val() != $("#psw").val()) {
				$("#errorpsw2").css('color', 'red')
		    	$("#errorpsw2").text("Password differenti");
		    	check2 = false;
		    }
		    else {
		    	$("#errorpsw2").text("Perfect");
				$("#errorpsw2").css('color', 'green')
		    	check2 = true
		    }
		  });
		  
	  	
	  	$("#usr").keyup(function(){
		     $("#errorusr").text("");
		  });  
	  
		
		$("#invia").mouseover(function(){
			 if( check1 && check2 && $("#usr").val().length > 0) {
				 $(this).attr("disabled", false);
				 $("#p").text("");
			 }
			 else {
				 $(this).attr("disabled", true);
				 $("#p").text("Compila tutti i campi correttamente! ");
			 }
	  	});  
	  	
	  
	 	$(".inp").keyup(function(){
			$("#invia").attr("disabled", false);
			$("#p").text("");
	  	}); 
	  
	 	
		$("#btnSignUp").click(function(){
			 $("#signup").css("display", "block");
			 $("#signin").css("display", "none");
	  	});
	  	  	  
});

</script>