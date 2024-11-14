package br.com.inoovexa.grpcdemo.client;

import br.com.inoovexa.grpc.ListProductsRequest;
import br.com.inoovexa.grpc.ListProductsResponse;
import br.com.inoovexa.grpc.ProductServiceGrpc;
import br.com.inoovexa.grpcdemo.ProductEntity;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class PerformanceTestClient {

    public static void main(String[] args) {
        testRestApi();
        testGrpcApi();
    }

    private static void testRestApi() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/api/products";

        long startTime = System.currentTimeMillis();
        ResponseEntity<List<ProductEntity>> response = restTemplate.exchange(
                url, HttpMethod.GET, null, new ParameterizedTypeReference<List<ProductEntity>>() {}
        );
        long endTime = System.currentTimeMillis();

        System.out.println("REST API:");
        System.out.println("- Response Time: " + (endTime - startTime) + " ms");
        System.out.println("- Total products: " + response.getBody().size());
    }

    private static void testGrpcApi() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("127.0.0.1", 9090)
                .usePlaintext()
                .build();

        ProductServiceGrpc.ProductServiceBlockingStub stub = ProductServiceGrpc.newBlockingStub(channel);

        long startTime = System.currentTimeMillis();
        ListProductsResponse response = stub.listProducts(ListProductsRequest.newBuilder().build());
        long endTime = System.currentTimeMillis();

        System.out.println("gRPC API:");
        System.out.println("- Response Time: " + (endTime - startTime) + " ms");
        System.out.println("- Total products: " + response.getProductsCount());

        channel.shutdown();
    }

}
