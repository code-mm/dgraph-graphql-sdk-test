import com.dgraph.graphql.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bdlbsc.graphql.*;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;


public class Main {

    public static void main(String[] args) {
        GraphClient graphClient = GraphClient.getInstance();
        graphClient.setBaseUrl("http://dev.mhw828.com:8080/graphql");

        graphClient.mutateGraph(Operations.mutation(mutationQuery -> {
            List<AddProductInput> list = new ArrayList<>();
            for (int i = 0; i < 200; i++) {
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
