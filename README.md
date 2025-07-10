# api-mcp

A simple MCP Server and Client using Spring AI

It basically demonstrates how to implement a MCP server that can sit side dby side with api '/api/people' in this case.

# Server App (`com.apple.ai.mcp.api_mcp.ApiMcpApplication` )

Provides a single tool:

```text
Tool[name=getPeople, description=Get registered people, inputSchema=JsonSchema[type=object, properties={}, required=[], additionalProperties=false, defs=null, definitions=null]]
```

See [application-server.yaml](src/main/resource/application-server.yaml) for MCP server configuration.

# Client App ( `com.apple.ai.mcp.api_mcp.ApiMcpClientApplication` )

Create a MCP client that can talk to the MCP server above, list tools and call them.

See [application-client.yaml](src/main/resource/application-client.yaml) for MCP server configuration.
