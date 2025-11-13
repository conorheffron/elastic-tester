package net.ironoc.elastic;

import java.io.IOException;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import io.searchbox.params.SearchType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import net.ironoc.elastic.model.Article;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.JestResult;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.indices.CreateIndex;

public class ElasticApp {

	public static void main(String[] args) {
        String allArgs = String.format("All arguments: %s", String.join(", ", args));
        System.out.println(allArgs);
		String serverUri = args[0];
		String index = args[1];
        String username = args[2];
        String testPassw = args[3];
        boolean saveNeeded = Boolean.parseBoolean(args[4]);

		// Create Client
		// Construct a new Jest client according to configuration via factory
		JestClient client = getClient(serverUri, username, testPassw);

		// Index Creation
		createIndex(index, client);

		// Insert articles
        if (saveNeeded) {
            Article jamesBond = new Article("John Gardner", "Goldeneye, Shaken, Not Stirred...");
            insertDocument(client, index, jamesBond);
            Article spyThriller = new Article("Robert Ludlum", "I cant remember...The Bourne Identity");
            insertDocument(client, index, spyThriller);
            Article space = new Article("Chris Hadfield", "An Astronauts Guide to Life on Earth, to infinity and...");
            insertDocument(client, index, space);
        }

		// JEST Search (Section 1)
        System.out.println("* START MATCH ALL QUERY FOR JEST RESULT SET---------------------------------------*");
        int pageSize = 20; // Batch size
        int from = 0;
        String queryMatchAll = String.format("{\"from\":%d,\"size\":%d,\"query\":{\"match_all\":{}}}", from, pageSize);
		jestSearch(client, index, queryMatchAll);
		System.out.println("* END MATCH ALL QUERY ---------------------------------------*");

		// JEST search with Model results (Section 2.1 - JSON Output & Section 2.2 - Direct Mapping of articles Output)
        System.out.println("* START DIRECT MATCH QUERY FOR MODEL RESULTS ---------------------------------------*");
        String queryMatchAllPojo = "{ \"query\": { \"match_all\": {} } }";
		jestSearchModel(client, index, queryMatchAllPojo);
        System.out.println("* END DIRECT MATCH QUERY ---------------------------------------*");

		// elastic search query by query String (Section 3)
        System.out.println("* START DIRECT MATCH QUERY FOR ES RESULTS ---------------------------------------*");
		elasticSearch(client, index, "Hadfield");
        System.out.println("* END DIRECT MATCH QUERY ---------------------------------------*");

		client.shutdownClient();
	}

	private static void elasticSearch(JestClient client, String indexName, String query) {
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		QueryStringQueryBuilder queryStringQueryBuilder = QueryBuilders.queryStringQuery(query);
		searchSourceBuilder.query(queryStringQueryBuilder);
		Search searchElastic = new Search.Builder(searchSourceBuilder.toString())
				// multiple index or types can be added.
				.addIndex(indexName).build();

		try {
			SearchResult elasticRs = client.execute(searchElastic);
            String jsonPrettyStr = prettyPrintJson(elasticRs);
            System.out.println("--------------\n" + jsonPrettyStr);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void jestSearchModel(JestClient client, String indexName, String query) {
		Search.Builder searchBuilder = new Search.Builder(query).addIndex(indexName);
		try {
			SearchResult result = client.execute(searchBuilder.build());
            String jsonPrettyStr = prettyPrintJson(result);
            System.out.println("--------------\n" + jsonPrettyStr);

            if (result.isSucceeded()) {
                // Directly map hits to POJO list
                List<Article> articles = result.getSourceAsObjectList(Article.class, false);
                // Print results
                articles.forEach(System.out::println);
            } else {
                System.err.println("Search failed: " + result.getErrorMessage());
            }
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void jestSearch(JestClient client, String indexName, String query) {
		Search search = new Search.Builder(query).addIndex(indexName).setSearchType(SearchType.QUERY_THEN_FETCH).build();
		try {
			JestResult result = client.execute(search);
            String jsonPrettyStr = prettyPrintJson(result);
            System.out.println("-------------- JEST Search raw JSON Object: \n" + jsonPrettyStr);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

    private static String prettyPrintJson(JestResult result) {
        JsonObject jsonObject = result.getJsonObject();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonPrettyStr = gson.toJson(jsonObject);
        return jsonPrettyStr;
    }

    private static void insertDocument(JestClient client, String indexName, Object source) {
		Index index = new Index.Builder(source).index(indexName).type("_doc").build();
		try {
			client.execute(index);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void createIndex(String indexName, JestClient client) {
		try {
			client.execute(new CreateIndex.Builder(indexName).build());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static JestClient getClient(String serverUri, String username, String testPassword) {
		JestClientFactory factory = new JestClientFactory();
		factory.setHttpClientConfig(new HttpClientConfig
                .Builder(serverUri)
                .defaultCredentials(username, testPassword)// basic auth
                .connTimeout(10000)// 10 secs
                .readTimeout(10000)
				.multiThreaded(true)
                .build());
		JestClient client = factory.getObject();
		return client;
	}
}
