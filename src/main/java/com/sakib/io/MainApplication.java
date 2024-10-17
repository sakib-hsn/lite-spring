package com.sakib.io;

import com.sakib.io.litespring.ApplicationContext;
import com.sakib.io.litespring.LiteSpringApplication;
import com.sakib.io.litespring.annotation.PackageScan;


@PackageScan(scanPackages = {"com.sakib.io"})
public class MainApplication {

    public static void main(String[] args) throws Exception {

        ApplicationContext applicationContext = LiteSpringApplication.run(MainApplication.class);

    }
}


