package com.example.user_service.test;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class CompletableFutureTest {

    void testCompletableFuture(){
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(0);
            } catch ( InterruptedException e ) {
                throw new RuntimeException( e );
            }
            return "Hello World";
        });

        future.thenAccept(result -> System.out.println("Result: " + result));
    }

    // 2 integer arrays sorted
    // []
    // []

    void sortArrays(){
        int []a = {1, 3, 7, 11, 17};
        int []b = {2, 5, 6, 15, 16};
        int indexA = 0;
        int indexB = 0;
        int i = 0;
        int [] result = new int[a.length + b.length];

        while( (indexA < a.length && indexB < b.length) ){
            if(a[indexA] < b[indexB]){
                result[i++] = a[indexA++];
            }
            else if(a[indexA] > b[indexB]){
                result[i++] = b[indexB++];
            }
        }
        while (indexA < a.length){
            result[i++] = a[indexA++];
        }
        while (indexB < b.length){
            result[i++] = b[indexB++];
        }

        Arrays.stream( result ).forEach(r -> System.out.printf("%d ", r));


    }

    public static void main(String[] args) {
        CompletableFutureTest test = new CompletableFutureTest();
        test.testCompletableFuture();
        test.sortArrays();
    }
}
