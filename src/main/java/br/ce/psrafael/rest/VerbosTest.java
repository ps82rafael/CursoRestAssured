package br.ce.psrafael.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import io.restassured.http.ContentType;

public class VerbosTest {
	
	@Test
	public void deveSalvaUsuario() {
		given()
			.log().all()
			.contentType("application/json")
			.body("{ \"name\": \"Jose\", \"age\": 50}")
		.when()
			.post("https://restapi.wcaquino.me/users")
		.then()
			.log().all()
			.statusCode(201)
			.body("id", is(notNullValue()))
			.body("name", is("Jose"))
			.body("age", is(50))
		;
		
	}
	
	@Test
	public void deveSalvaUsuarioUsandoObjeto() {
		User user = new User("Usuario via objeto", 35);
		
		given()
			.log().all()
			.contentType("application/json")
			.body(user)
		.when()
			.post("https://restapi.wcaquino.me/users")
		.then()
			.log().all()
			.statusCode(201)
			.body("id", is(notNullValue()))
			.body("name", is("Usuario via objeto"))
			.body("age", is(35))
		;
		
	}
	
	@Test
	public void deveDeserializarObjetoAoSalvaUsuarioUsandoObjeto() {
		User user = new User("Usuario deserializado", 35);
		
		User usuarioInserindo = given()
			.log().all()
			.contentType("application/json")
			.body(user)
		.when()
			.post("https://restapi.wcaquino.me/users")
		.then()
			.log().all()
			.statusCode(201)
			.extract().body().as(User.class)
			
		;
		
		System.out.println(usuarioInserindo);
		Assert.assertThat(usuarioInserindo.getId(), notNullValue());
		Assert.assertEquals("Usuario deserializado", usuarioInserindo.getName());
		Assert.assertThat(usuarioInserindo.getAge(), is(35));
	}
	
	@Test
	public void deveSalvaUsuarioUsandoMap() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", "Usuario via map");
		params.put("age", 25);
		
		given()
			.log().all()
			.contentType("application/json")
			.body(params)
		.when()
			.post("https://restapi.wcaquino.me/users")
		.then()
			.log().all()
			.statusCode(201)
			.body("id", is(notNullValue()))
			.body("name", is("Usuario via map"))
			.body("age", is(25))
		;
		
	}
	
	@Test
	public void naoDevoSalvarUsuarioSemNome() {
		given()
			.log().all()
			.contentType("application/json")
			.body("{\"age\": 50}")
		.when()
			.post("https://restapi.wcaquino.me/users")
		.then()
			.log().all()
			.statusCode(400)
			.body("id", is(nullValue())) 
			.body("error", is("Name é um atributo obrigatório"))
	;

	}
	
	@Test
	public void deveSalvaUsuarioViaXML() {
		given()
			.log().all()
			.contentType(ContentType.XML)
			.body("<user><name>Jose</name><age>50</age></user>")
		.when()
			.post("https://restapi.wcaquino.me/usersXML")
		.then()
			.log().all()
			.statusCode(201)
			.body("user.@id", is(notNullValue())) 
			.body("user.name", is("Jose")) 
			.body("user.age",is("50"))
		 
		;
		
	}
	
	@Test
	public void deveSalvaUsuarioViaXMLUsandoObjeto() {
		User user = new User("Usuario XML", 40);
		
		given()
			.log().all()
			.contentType(ContentType.XML)
			.body(user)
		.when()
			.post("https://restapi.wcaquino.me/usersXML")
		.then()
			.log().all()
			.statusCode(201)
			.body("user.@id", is(notNullValue())) 
			.body("user.name", is("Usuario XML")) 
			.body("user.age",is("40"))
		 
		;
		
	}
	
	@Test
	public void deveDeserializarXMLAoSalvarObjeto() {
		User user = new User("Usuario XML", 40);
		
		User usuarioInserido = given()
			.log().all()
			.contentType(ContentType.XML)
			.body(user)
		.when()
			.post("https://restapi.wcaquino.me/usersXML")
		.then()
			.log().all()
			.statusCode(201)
			.extract().body().as(User.class)
		 
		;
		
		assertThat(usuarioInserido.getId(), notNullValue());
		assertThat(usuarioInserido.getName(), is("Usuario XML"));
		assertThat(usuarioInserido.getAge(), is(40));
		assertThat(usuarioInserido.getSalary(), nullValue());
	}
	
	
	@Test
	public void deveAlterarUsuario() {
		given()
			.log().all()
			.contentType("application/json")
			.body("{ \"name\": \"Usuario alterado\", \"age\": 80}")
		.when()
			.put("https://restapi.wcaquino.me/users/1")
		.then()
			.log().all()
			.statusCode(200)
			.body("id", is(1))
			.body("name", is("Usuario alterado"))
			.body("age", is(80))
			.body("salary", is(1234.5678f))
		;
		
	}
	
	@Test
	public void devoCustomizarURL() {
		given()
			.log().all()
			.contentType("application/json")
			.body("{ \"name\": \"Usuario alterado\", \"age\": 80}")
		.when()
			.put("https://restapi.wcaquino.me/{entidade}/{userId}", "users", "1")
		.then()
			.log().all()
			.statusCode(200)
			.body("id", is(1))
			.body("name", is("Usuario alterado"))
			.body("age", is(80))
			.body("salary", is(1234.5678f))
		;
		
	}
	
	@Test
	public void devoCustomizarURLParte2() {
		given()
			.log().all()
			.contentType("application/json")
			.body("{ \"name\": \"Usuario alterado\", \"age\": 80}")
			.pathParam("entidade", "users")
			.pathParam("userId", 1)
		.when()
			.put("https://restapi.wcaquino.me/{entidade}/{userId}")
		.then()
			.log().all()
			.statusCode(200)
			.body("id", is(1))
			.body("name", is("Usuario alterado"))
			.body("age", is(80))
			.body("salary", is(1234.5678f))
		;
		
	}
	
	@Test
	public void devoRemoverUsuario() {
		given()
			.log().all()
		 .when()
		 	.delete("https://restapi.wcaquino.me/users/1")
		 .then()
		 	.log().all()
		 	.statusCode(204)
		 ;
	}
	
	@Test
	public void naoDevoRemoverUsuarioInexistente() {
		given()
			.log().all()
		 .when()
		 	.delete("https://restapi.wcaquino.me/users/1000")
		 .then()
		 	.log().all()
		 	.statusCode(400)
		 	.body("error", is("Registro inexistente"))
		 ;
	}
}


	

