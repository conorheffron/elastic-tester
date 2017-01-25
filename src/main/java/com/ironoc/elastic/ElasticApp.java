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
		JestClientFactory factory = new JestClientFactory();
		factory.setHttpClientConfig(new HttpClientConfig.Builder(serverUri).connTimeout(6000).readTimeout(6000)
				.multiThreaded(true).build());
		JestClient client = factory.getObject();

		// Index Creation
		// try {
		// client.execute(new CreateIndex.Builder("articles").build());
		// } catch (IOException e) {
		// e.printStackTrace();
		// }

		// Insert an article;
		// Article source = new Article();
		// source.setAuthour("JK Rowling");
		// source.setContent("Harry Potter");
		//
		//
		// Index index = new
		// Index.Builder(source).index("dev").type("article").build();
		// try {
		// client.execute(index);
		// } catch (IOException e) {
		// e.printStackTrace();
		// }

		// JEST Search
		// You can search indexed article as:
		String query = "{ \"query\": { \"match\" : { \"content\" : \"Lord\" } } }";
		Search search = (Search) new Search.Builder(query).addIndex("dev").addType("article").build();
		try {
			JestResult result = client.execute(search);

			System.out.println(result.getJsonObject());
		} catch (IOException e) {
			e.printStackTrace();
		}
		Search.Builder searchBuilder = new Search.Builder(query).addIndex("dev").addType("article");
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

		// elastic search
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.queryString("harry"));
		Search searchElastic = (Search) new Search.Builder(searchSourceBuilder.toString())
				// multiple index or types can be added.
				.addIndex("dev").addType("article").build();

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
		
		client.shutdownClient();
	}

}
