package com.webchat.common.util;

import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/****
 * 事务后置处理
 */
public class TransactionSyncManagerUtil {

    private static ExecutorService executorService = Executors.newFixedThreadPool(10);


    public static void registerSynchronization(final Runnable task) {

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void afterCommit() {
                executorService.execute(task);
            }
        });
    }

}
