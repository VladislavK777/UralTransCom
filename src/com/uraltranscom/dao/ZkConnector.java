package com.uraltranscom.dao;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/*
*
* Класс соединения с ZooKeeper
*
* @author Vladislav Klochkov
* @version 2.0
* @create 13.11.2017
*
*/

public class ZkConnector {

    // Подключаем логгер
    private static Logger logger = LoggerFactory.getLogger(ZkConnector.class);

    ZooKeeper zookeeper;
    CountDownLatch connectedSignal = new CountDownLatch(1);

    // Подключаемся
    public void connect(String host) throws IOException, InterruptedException {
        zookeeper = new ZooKeeper(host, 5000,
                new Watcher() {
                    public void process(WatchedEvent event) {
                        if (event.getState() == Event.KeeperState.SyncConnected) {
                            connectedSignal.countDown();
                        }
                    }
                });
        connectedSignal.await();
    }

    // Закрываем соединение
    public void close() throws InterruptedException {
        zookeeper.close();
    }

    // Получаем инстанс Zoo
    public ZooKeeper getZooKeeper() {
        if (zookeeper == null || !zookeeper.getState().equals(ZooKeeper.States.CONNECTED)) {
            throw new IllegalStateException("ZooKeeper не подключен.");
        }
        return zookeeper;
    }
}
