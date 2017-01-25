package com.ironoc.elastic;

import java.io.IOException;
import java.util.List;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import com.ironoc.elastic.model.Article;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.JestResult;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.core.SearchResult.Hit;
import io.searchbox.indices.CreateIndex;

public class ElasticApp {

	public static void main(String[] args) {
		String serverUri = args[0];

		// Create Client
		// Construct a new Jest client according to configuration via factory
		JestClient client = getClient(serverUri);

		// Index Creation
//		createIndex("dev", client);

		// Insert an article;
//		insertDocument(client, "dev", "article");

		// JEST Search
		// You can search indexed article as:
		String query = "{ \"query\": { \"match\" : { \"content\" : \"Lord\" } } }";
		jestSearch(client, "dev", "article", query);
		
		// JEST search with Model results
		jestSearchModel(client, "dev", "article", query);

		// elastic search
		elasticSearch(client, "dev", "article", "harry");
		

		client.shutdownClient();
	}

	private static void elasticSearch(JestClient client, String indexName, String type, String query) {
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.queryString(query));
		Search searchElastic = (Search) new Search.Builder(searchSourceBuilder.toString())
				// multiple index or types can be added.
				.addIndex(indexName).addType(type).build();

		try {
			SearchResult elasticRs = client.execute(searchElastic);

			List<Hit<Article, Void>> hits = elasticRs.getHits(Article.class);
			for (Hit<Article, Void> hit : hits) {
				Article talk = hit.source;
				System.out.println(talk.toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void jestSearchModel(JestClient client, String indexName, String type, String query) {
		Search.Builder searchBuilder = new Search.Builder(query).addIndex(indexName).addType(type);
		try {
			SearchResult result = client.execute(searchBuilder.build());

			System.out.println(result.getJsonObject());

			List<Hit<Article, Void>> hits = result.getHits(Article.class);
			for (Hit<Article, Void> hit : hits) {
				Article talk = hit.source;
				System.out.println(talk.getAuthour());
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void jestSearch(JestClient client, String indexName, String type, String query) {
		Search search = (Search) new Search.Builder(query).addIndex(indexName).addType(type).build();
		try {
			JestResult result = client.execute(search);

			System.out.println(result.getJsonObject());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void insertDocument(JestClient client, String indexName, String type) {
		Article source = new Article("Dan Brown", "Da Vinci Code");
		Index index = new Index.Builder(source).index(indexName).type(type).build();
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

	private static JestClient getClient(String serverUri) {
		JestClientFactory factory = new JestClientFactory();
		factory.setHttpClientConfig(new HttpClientConfig.Builder(serverUri).connTimeout(6000).readTimeout(6000)
				.multiThreaded(true).build());
		JestClient client = factory.getObject();
		return client;
	}

}
