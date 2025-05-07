package com.ChatBot.ChatBot.chat_configuration;

import com.ChatBot.ChatBot.rag_service.HospitalInfoRag;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.allminilml6v2.AllMiniLmL6V2EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.retriever.EmbeddingStoreRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.pgvector.PgVectorEmbeddingStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PgVector {


    @Value("${api.key}")
    private String api_key;

    @Bean
    public OpenAiChatModel getAIModelForRAG() {
        return OpenAiChatModel.builder()
                .apiKey(api_key)
                .modelName("gpt-4o-mini")
                .build();
    }

    @Bean
    public OpenAiEmbeddingModel embeddingModel() {
        return OpenAiEmbeddingModel.builder()
                .apiKey(api_key)
                .modelName("gpt-4o-mini")
                .build();
    }

    @Bean
    public EmbeddingModel allminiLmL6V2EmbeddingModel() {
        return new AllMiniLmL6V2EmbeddingModel();
    }

    @Bean
    public PgVectorEmbeddingStore pgVectorEmbeddingStore() {
        return PgVectorEmbeddingStore.builder()
                .host("127.0.0.1")
                .port(5432)
                .database("mydatabase")
                .user("postgres")
                .password("postgres")
                .table("hospital")
                .dimension(384)
                .build();
    }

    @Bean
    public EmbeddingStoreIngestor embeddingStoreIngestor() {
        return EmbeddingStoreIngestor.builder()
                .embeddingModel(allminiLmL6V2EmbeddingModel())
                .embeddingStore(pgVectorEmbeddingStore())
                .documentSplitter(DocumentSplitters.recursive(500, 50)) // 500 tokens with 50 overlap
                .build();
    }

    @Bean
    public ContentRetriever contentRetriever() {
        return EmbeddingStoreContentRetriever.builder()
                .embeddingStore(pgVectorEmbeddingStore())
                .embeddingModel(allminiLmL6V2EmbeddingModel())
                .maxResults(2) // on each interaction we will retrieve the 2 most relevant segments
                .minScore(0.5) // we want to retrieve segments at least somewhat similar to user query
                .build();
    }

    @Bean
    public HospitalInfoRag hospitalInfoRag() {
        return AiServices.builder(HospitalInfoRag.class)
                .chatLanguageModel(getAIModelForRAG())
                .contentRetriever(contentRetriever())
                .build();
    }
}


