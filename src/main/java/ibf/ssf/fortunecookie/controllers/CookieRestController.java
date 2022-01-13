package ibf.ssf.fortunecookie.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ibf.ssf.fortunecookie.service.FortuneCookie;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObjectBuilder;

@RestController
@RequestMapping(path = "/cookies", produces = MediaType.APPLICATION_JSON_VALUE)
public class CookieRestController {

	@Autowired
	FortuneCookie fCookieService;

	@GetMapping
	public ResponseEntity<String> getCookie(@RequestParam(defaultValue = "1") Integer count) {
		if ((count < 1) || (count > 10)) {
			JsonObjectBuilder jBuilder = Json.createObjectBuilder();
			String jString = jBuilder.add("error", "number should be between 1 and 10 inclusive!").build().toString();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jString);
		}
		JsonObjectBuilder jBuilder2 = Json.createObjectBuilder();
		JsonArrayBuilder jArray2 = Json.createArrayBuilder();
		Set<String> cookies = fCookieService.getCookies(count);
		// For loop
		// for (String cookie : fCookieService.getCookies(count)) {
		// jArray2.add(cookie);
		// }
		// Streams way
		cookies.stream().forEach(v -> jArray2.add(v));
		// Streams with reduce
		cookies.stream().reduce(
				jArray2,
				(ab, item) -> ab.add(item),
				(ab0, ab1) -> {
					JsonArray a = ab1.build();
					for (int i = 0; i < a.size(); i++)
						ab0.add(a.get(i));
					return ab0;
				});

		jBuilder2.add("Cookie", jArray2).add("timestamp", System.currentTimeMillis());
		String jString2 = jBuilder2.build().toString();
		return ResponseEntity.status(HttpStatus.OK).body(jString2);

	}
}
