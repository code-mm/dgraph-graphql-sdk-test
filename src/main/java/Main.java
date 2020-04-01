import com.dgraph.graphql.*;

import java.util.*;
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

        // 异步
//        graphClient.mutateGraph(Operations.mutation(mutationQuery -> {
//            AddUserInput addUserInput = new AddUserInput("hahah002", "hahah002", "hahah002");
//            mutationQuery.addUser(Arrays.asList(addUserInput), addUserPayloadQuery -> {
//                addUserPayloadQuery.numUids();
//                addUserPayloadQuery.user(userQuery -> {
//                    userQuery.id();
//                    userQuery.name();
//                    userQuery.username();
//                    userQuery.password();
//                });
//            });
//        })).subscribe(new io.reactivex.SingleObserver<Mutation>() {
//            @Override
//            public void onSubscribe(Disposable d) {
//            }
//            @Override
//            public void onSuccess(Mutation mutation) {
//                Integer numUids = mutation.getAddUser().getNumUids();
//                System.out.println(numUids);
//                List<User> user = mutation.getAddUser().getUser();
//                for (User it : user) {
//                    System.out.println("name : " + it.getName() + " username : " + it.getUsername() + " password : " + it.getPassword());
//                }
//            }
//            @Override
//            public void onError(Throwable e) {
//                e.printStackTrace();
//            }
//        });


        try {
            // 同步
            Mutation mutation = graphClient.mutateGraphSynchronize(Operations.mutation(mutationQuery -> {
                AddUserInput addUserInput = new AddUserInput("hahah003", "hahah003", "hahah003");
                mutationQuery.addUser(Arrays.asList(addUserInput), addUserPayloadQuery -> {
                    addUserPayloadQuery.numUids();
                    addUserPayloadQuery.user(userQuery -> {
                        userQuery.id();
                        userQuery.name();
                        userQuery.username();
                        userQuery.password();
                    });
                });
            }));
            List<User> user = mutation.getAddUser().getUser();
            for (User it : user) {
                System.out.println("name : " + it.getName() + " username : " + it.getUsername() + " password : " + it.getPassword());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
