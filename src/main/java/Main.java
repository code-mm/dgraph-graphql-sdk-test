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

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        GraphClient graphClient = GraphClient.builder().setHttpClient(new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build())
                .setHeaders(headers)
                .setUrl("http://localhost:8080/graphql")
                .build();

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
