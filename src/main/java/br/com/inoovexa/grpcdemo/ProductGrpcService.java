package br.com.inoovexa.grpcdemo;

import br.com.inoovexa.grpc.*;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@GrpcService
public class ProductGrpcService extends ProductServiceGrpc.ProductServiceImplBase {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public void listProducts(ListProductsRequest request, StreamObserver<ListProductsResponse> responseObserver) {
        List<ProductEntity> products = productRepository.findAll();

        List<ProductProto> protoProducts = products.stream()
                .map(product -> ProductProto.newBuilder()
                        .setId(product.getId())
                        .setName(product.getName())
                        .setPrice(product.getPrice())
                        .build())
                .collect(Collectors.toList());

        ListProductsResponse response = ListProductsResponse.newBuilder()
                .addAllProducts(protoProducts)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
