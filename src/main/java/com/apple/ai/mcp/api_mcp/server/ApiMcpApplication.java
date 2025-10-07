package com.apple.ai.mcp.api_mcp.server;

import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootApplication
@Profile("server")
public class ApiMcpApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(ApiMcpApplication.class);

		// Set active profiles
		app.setAdditionalProfiles("server");
		app.setBanner((environment, sourceClass, out) -> out.println("API MCP Server Application"));
		ConfigurableApplicationContext context = app.run(args);
	}

	record Person(int id, String name){}

	@Service
	public static class PersonService {
		private final List<Person> people = new ArrayList<>();

		public PersonService() {
			people.add(new Person(1, "Sandip"));
			people.add(new Person(2, "Tim"));
		}

		@Tool(description = "Get registered people")
		public List<Person> getPeople() {
			return people;
		}

		@Tool(description = "Get person by given id")
		public Optional<Person> getPersonById(int id) {
			return people
					.stream()
					.filter((Person person) -> {
						return person.id() == id;
					})
					.findFirst();
		}
	}

	@RestController
	@RequestMapping("/api")
	public static class PersonController {
		private final PersonService personService;

        public PersonController(PersonService personService) {
            this.personService = personService;
        }

        @GetMapping("/people")
		public List<Person> getPeople() {
			return personService.getPeople();
		}

		@GetMapping("/people/{id}")
		public ResponseEntity<Person> getPersonById(@RequestParam int id) {
			return ResponseEntity.of(personService.getPersonById(id));
		}
	}

	@Configuration
	public static class PeopleMCP {

		@Bean
		public ToolCallbackProvider peopleTools(PersonService personService) {
			return MethodToolCallbackProvider.builder().toolObjects(personService).build();
		}
	}
}
