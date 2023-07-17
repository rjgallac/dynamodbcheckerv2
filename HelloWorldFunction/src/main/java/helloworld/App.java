package helloworld;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;



import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.ListTablesResponse;

/**
 * Handler for requests to Lambda function.
 */
public class App implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
        try {
            DynamoDbClient client = DynamoDbClient.builder()
                    .endpointOverride(URI.create("http://192.168.1.154:8000"))
                    // The region is meaningless for local DynamoDb but required for client builder validation
//                    .region(Region.EU_WEST_2)
//                    .credentialsProvider(StaticCredentialsProvider.create(
//                            AwsBasicCredentials.create("dummykey", "dummysecret")))
                    .build();

            ListTablesResponse listTablesResponse = client.listTables();
            List<String> strings = listTablesResponse.tableNames();
            strings.stream().forEach(s ->{
                System.out.println("---" +s);
            });

            Map<String, AttributeValue> keysMap = new HashMap<>();
            AttributeValue artist = AttributeValue.builder().s("No One You Know").build();
            AttributeValue songTitle = AttributeValue.builder().s("Call Me Today").build();
            keysMap.put("Artist", artist);
            keysMap.put("SongTitle", songTitle);
            GetItemRequest request = GetItemRequest.builder().tableName("Music").key(keysMap).build();

            /* Setting Table Name */
            GetItemResponse item = client.getItem(request);
            System.out.println("======================================");
            System.out.println(item.item());
            System.out.println("======================================");
        }catch (Exception e){
            System.out.println(e);
        }

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Custom-Header", "application/json");

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
                .withHeaders(headers);
        try {
            final String pageContents = this.getPageContents("https://checkip.amazonaws.com");
            String output = String.format("{ \"message\": \"hello world\", \"location\": \"%s\" }", pageContents);

            return response
                    .withStatusCode(200)
                    .withBody(output);
        } catch (IOException e) {
            return response
                    .withBody("{}")
                    .withStatusCode(500);
        }

    }

    private String getPageContents(String address) throws IOException {
        URL url = new URL(address);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()))) {
            return br.lines().collect(Collectors.joining(System.lineSeparator()));
        }
    }
}
