package br.ce.psrafael.rest;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

public class OlaMundoTest {

	@Test
	public void testOlaMundo() {
		Response response = RestAssured.request(Method.GET, "https://restapi.wcaquino.me/ola");
		Assert.assertTrue(response.getBody().asString().equals("Ola Mundo!"));
		Assert.assertTrue(response.statusCode() == 200);
		Assert.assertEquals(200, response.statusCode());

		ValidatableResponse validacao = response.then();
		validacao.statusCode(200);
	}

	@Test
	public void devoConhecerOutrasFormasRestAssured() {
		Response response = RestAssured.request(Method.GET, "https://restapi.wcaquino.me/ola");
		ValidatableResponse validacao = response.then();
		validacao.statusCode(200);
		
		get("https://restapi.wcaquino.me/ola").then().statusCode(200);
		
		given()//Pre condicoes
			
		 .when()//Açao
		 	.get("https://restapi.wcaquino.me/ola")
		 .then()//Assertiva
		    .statusCode(200);
		
	}
	
	@Test
	public void devoConhecerMatchersHamcrest() {
		assertThat("Maria", is("Maria"));
		assertThat(128, Matchers.is(128));
		assertThat(128d, Matchers.isA(Double.class));
		assertThat(128d, Matchers.greaterThan(120d));
		assertThat(128d, Matchers.lessThan(130d));
		
		List<Integer> impares = Arrays.asList(1,3,5,7,9);
		assertThat(impares, Matchers.hasSize(5));
		assertThat(impares, Matchers.contains(1,3,5,7,9));
		assertThat(impares, Matchers.containsInAnyOrder(1,3,5,9,7));
		assertThat(impares, Matchers.hasItem(1));
		assertThat(impares, Matchers.hasItems(1,5));
		
		//assertThat("Maria", Matchers.is(not("Joao")));
		Assert.assertThat("Maria", not("Joao"));
		assertThat("joaquina", anyOf(is("Maria"), is("joaquina")));
		assertThat("joaquina", anyOf(startsWith("ina"), containsString("qui")));
		
	}

	@Test
	public void devoValidarBody() {
		given()
		 .when()
		 	.get("https://restapi.wcaquino.me/ola")
		 .then()
		    .statusCode(200)
			.body(is("Ola Mundo!"))
			.body(containsString("Mundo"))
			.body(is(not(nullValue())));

		
	}

}
