package com.apple.ai.mcp.api_mcp.client;

import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.spec.McpSchema;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.util.HashMap;
import java.util.List;

@SpringBootApplication
@Profile("client")
public class ApiMcpClientApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(ApiMcpClientApplication.class);

		// Set active profiles
		app.setAdditionalProfiles("client");
		app.setBanner((environment, sourceClass, out) -> out.println("API MCP Client Application"));
		ConfigurableApplicationContext context = app.run(args);
	}

	@Bean
	public CommandLineRunner clr(List<McpSyncClient> mcpSyncClients) {
        return (String... args) -> {
            mcpSyncClients.forEach((McpSyncClient mcpSyncClient) -> {
                System.out.print("Client: ");
                System.out.println(mcpSyncClient.getClientInfo());
                McpSchema.ListToolsResult listToolsResult = mcpSyncClient.listTools();
                listToolsResult.tools().forEach((McpSchema.Tool tool) -> {

                    HashMap<String, Object> arguments = new HashMap<>();
                    if (tool.name().equals("getPersonById")) {
                        arguments.put("id", 1);
                    }
                    System.out.println("Calling with args:  " + arguments + " " + tool);
                    McpSchema.CallToolResult callToolResult = mcpSyncClient.callTool(new McpSchema.CallToolRequest(tool.name(), arguments));
                    System.out.println("Tool result: ");
                    System.out.println(callToolResult.content());
                });
            });
            System.exit(0);
        };
    }
}
