import com.dgraph.graphql.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.bdlbsc.graphql.*;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import okhttp3.OkHttpClient;


public class Main {

    public static void main(String[] args) {
        GraphClient graphClient = GraphClient.getInstance();
        graphClient.setBaseUrl("http://localhost:8080/graphql");

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();
        // 设置客户端
        graphClient.setHttpClient(okHttpClient);

        // 设置请求头
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");
        graphClient.setHeaders(header);


        graphClient.mutateGraph(Operations.mutation(mutationQuery -> {
            List<AddProductInput> list = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                AddProductInput addProductInput = new AddProductInput();
                addProductInput.setName("测试商品" + i);
                list.add(addProductInput);
            }
            mutationQuery.addProduct(list, addProductPayloadQuery -> {
                addProductPayloadQuery
                        .numUids();
                addProductPayloadQuery.product(productQuery -> {
                    productQuery.name();
                    productQuery.productId();
                });
            });
        })).subscribe(new io.reactivex.SingleObserver<Mutation>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(Mutation mutation) {
                List<Product> product = mutation.getAddProduct().getProduct();
                for (Product it : product) {
                    System.out.println("插入成功 : id : " + it.getProductId() + " name : " + it.getName());
                }
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }
        });


        graphClient.queryGraph(Operations.query(queryRootQuery -> queryRootQuery.queryProduct(new ProductQueryDefinition() {
            @Override
            public void define(ProductQuery productQuery) {
                productQuery.name();
                productQuery.productId();
            }
        })))
                .subscribe(new SingleObserver<QueryRoot>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onSuccess(QueryRoot queryRoot) {
                        List<Product> queryProduct = queryRoot.getQueryProduct();
                        for (Product it : queryProduct) {
                            System.out.println("查询成功 : id : " + it.getProductId() + " name : " + it.getName());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        System.out.println(e.getMessage());
                    }
                });
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
