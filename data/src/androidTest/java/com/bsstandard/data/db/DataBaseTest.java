package com.bsstandard.data.db;


import android.content.Context;

import androidx.test.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.bsstandard.data.db.dao.TestDAO;
import com.bsstandard.data.db.dao.TestDao;
import com.bsstandard.data.entity.ReposResponse;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.subscribers.TestSubscriber;

/**
 * packageName    : com.bsstandard.data.db
 * fileName       : DataBaseTest
 * author         : piecejhm
 * date           : 2022/04/29
 * description    : Room Database Test
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/04/29        piecejhm       최초 생성
 */


@RunWith(AndroidJUnit4.class)
public class DataBaseTest {

    private TestDAO testDao;
    private AppDatabase database;

    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getTargetContext();
        database = AppDatabase.getInstanceInMemory(context);
        testDao = database.testDao();
    }

    @After
    public void closeDb() throws IOException {
        database.close();
    }

    @Test
    public void writeRepoAndReadInList() throws Exception {

        TestResponse repos = new TestResponse();
        repos.setId("1");
        repos.setName("Test name");
        repos.setFullName("Test full name");

        List<TestResponse> list = new ArrayList<>();
        list.add(repos);
        testDao.insert(list);

        TestSubscriber<List<TestResponse>> subscriber = new TestSubscriber<>();
        testDao.get().subscribe(subscriber);
        subscriber.assertNoErrors();
    }
}