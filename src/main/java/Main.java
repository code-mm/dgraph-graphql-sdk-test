import com.dgraph.graphql.*;

import java.util.ArrayList;
import java.util.List;

import com.bdlbsc.graphql.*;
import com.shopify.graphql.support.ID;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public class Main {

    public static void main(String[] args) {

//        MutationQuery mutationQuery = Operations.mutation(new MutationQueryDefinition() {
//            @Override
//            public void define(MutationQuery mutationQuery) {
//
//
//                List<AddProductInput> list = new ArrayList<>();
//
//                for (int i = 0; i < 200; i++) {
//                    AddProductInput addProductInput = new AddProductInput();
//                    addProductInput.setName("测试商品" + i);
//                    list.add(addProductInput);
//                }
//
//                mutationQuery.addProduct(list, new AddProductPayloadQueryDefinition() {
//                    @Override
//                    public void define(AddProductPayloadQuery addProductPayloadQuery) {
//
//
//                        addProductPayloadQuery
//                                .numUids();
//                        addProductPayloadQuery.product(new ProductQueryDefinition() {
//                            @Override
//                            public void define(ProductQuery productQuery) {
//
//                                productQuery.name();
//                                productQuery.productId();
//
//                            }
//                        });
//
//                    }
//                });
//
//            }
//        });
//
//        String s = mutationQuery.toString();
//        System.out.println(s);
//
//        GraphClient.getInstance().setBaseUrl("http://dev.mhw828.com:8080/graphql");
//
//        GraphClient.getInstance().mutateGraph(mutationQuery).subscribe(new io.reactivex.SingleObserver<Mutation>() {
//            @Override
//            public void onSubscribe(Disposable d) {
//
//            }
//
//            @Override
//            public void onSuccess(Mutation mutation) {
//
//                List<Product> product = mutation.getAddProduct().getProduct();
//
//                for (Product it : product) {
//                    System.out.println("id : " + it.getProductId() + " name : " + it.getName());
//                }
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                e.printStackTrace();
//            }
//        });

        GraphClient.getInstance().setBaseUrl("http://dev.mhw828.com:8080/graphql");
        QueryRootQuery queryRootQuery = Operations.query(new QueryRootQueryDefinition() {
            @Override
            public void define(QueryRootQuery queryRootQuery) {

                queryRootQuery.queryProduct(new ProductQueryDefinition() {
                    @Override
                    public void define(ProductQuery productQuery) {

                        productQuery.name();
                        productQuery.productId();

                    }
                });


            }
        });

        System.out.println(queryRootQuery.toString());

        GraphClient.getInstance().queryGraph(queryRootQuery)
                .subscribe(new SingleObserver<QueryRoot>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(QueryRoot queryRoot) {

                        List<Product> queryProduct = queryRoot.getQueryProduct();

                        for (Product it : queryProduct) {
                            System.out.println("id : " + it.getProductId() + " name : " + it.getName());
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
