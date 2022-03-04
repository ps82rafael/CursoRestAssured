package br.ce.psrafael.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import io.restassured.http.ContentType;
import io.restassured.path.xml.XmlPath;
import io.restassured.path.xml.XmlPath.CompatibilityMode;

public class AuthTest {
	
	@Test
	public void deveAcessarSWPI() {
		
		given()
			.log().all()
		.when()
			.get("https://swapi.dev/api/people/1/")
		.then()
			.log().all()
			.statusCode(200)
			.body("name", is("Luke Skywalker"))
		
		;
	}
	
	@Test
	public void deveObterClima() {
		given()
			.log().all()
			.queryParam("q","fortaleza,br")
			.queryParam("appid", "5ad1c2270521b5c88b6746ed32aa55ae")
			.queryParam("units", "metric")
		.when()
			.get("https://api.openweathermap.org/data/2.5/weather")
		.then()
			.log().all()
			.statusCode(200)
			.body("name", is("Fortaleza"))
			.body("main.temp", is(29.07f))
			.body("weather.main", hasItem("Clouds"))
		
		;
	}
	
	@Test
	public void naoDeveAcessarSemSenha() {
		given()
			.log().all()
		.when()
			.get("https://restapi.wcaquino.me/basicauth")
		.then()
			.log().all()
			.statusCode(401)
		
		;
	}
	
	@Test
	public void DeveFazerAutenticacaoBasica() {
		given()
			.log().all()
		.when()
			.get("https://admin:senha@restapi.wcaquino.me/basicauth")
		.then()
			.log().all()
			.statusCode(200)
			.body("status", is("logado"))
		
		;
	}
	

	@Test
	public void DeveFazerAutenticacaoBasica2() {
		given()
			.log().all()
			.auth().basic("admin", "senha")
		.when()
			.get("https://restapi.wcaquino.me/basicauth")
		.then()
			.log().all()
			.statusCode(200)
			.body("status", is("logado"))
		
		;
	}
	
	@Test
	public void DeveFazerAutenticacaoBasicaChallenge() {
		given()
			.log().all()
			.auth().preemptive().basic("admin", "senha")
		.when()
			.get("https://restapi.wcaquino.me/basicauth2")
		.then()
			.log().all()
			.statusCode(200)
			.body("status", is("logado"))
		
		;
	}
	
	@Test
	public void deveFazerAutenticacaoComTokenJWT() {
		
		//Login na api
		Map<String,String> login = new HashMap<String, String>();
		login.put("email", "ps.rafael@rafael");
		login.put("senha", "12345");
				
		//Receber o token
		//Login na api
		String token = given()
			.log().all()
			.body(login)
			.contentType(ContentType.JSON)
		.when()
			.post("http://barrigarest.wcaquino.me/signin")
		.then()
			.log().all()
			.statusCode(200)
			.extract().path("token")
			
		; 
		
		
		//Obter as Contas
		given()
			.log().all()
			.header("Authorization", "JWT " + token )
		.when()
			.get("http://barrigarest.wcaquino.me/contas")
		.then()
			.log().all()
			.statusCode(200)
			.body("nome", hasItem("Conta teste"))
		
		;
	}
	
	@Test
	public void deveAcessarAplicacaoWeb() {
		//Login
		String cookie = given()
			.log().all()
			.formParam("email", "ps.rafael@rafael")
			.formParam("senha", 12345)
			.contentType(ContentType.URLENC.withCharset("UTF-8"))
		.when()
			.post("http://seubarriga.wcaquino.me/logar")
		.then()
			.log().all()
			.statusCode(200)
			.extract().header("set-cookie")
		;
		
		cookie = cookie.split("=")[1].split(";")[0];
		System.out.println(cookie); 
		
		//Obter Contas	
		String body = given()
			.log().all()
			.cookie("connect.sid", cookie)
		.when()
			.get("http://seubarriga.wcaquino.me/contas")
		.then()
			.log().all()
			.statusCode(200) 
			.body("html.body.table.tbody.tr[0].td[0]", is("Conta teste"))
			.extract().body().asString();
		;
		
		System.out.println("--------------------");
		XmlPath xmlPath = new XmlPath(CompatibilityMode.HTML, body);
		System.out.println(xmlPath.getString("html.body.table.tbody.tr[0].td[0]"));
		
	}
	
	

}


