package com.kuznetsov.linoleumShopRest.entity;

import org.hibernate.envers.RevisionListener;

public class MyRevisionListener implements RevisionListener {

    @Override
    public void newRevision(Object o) {
        ((Revision)o).setUserr("denis.denis.kuznecov@mail.ru");
    }
}
