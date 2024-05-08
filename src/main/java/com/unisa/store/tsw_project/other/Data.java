package com.unisa.store.tsw_project.other;

public interface Data {

    enum Condition{
        A("Nuovo"),
        B("Quasi Nuovo"),
        C("Buono"),
        D("Sufficiente"),
        E("Danneggiato");

        public final String description;

        Condition(String description){
            this.description=description;
        }
    }

    enum OrderStatus{
        INPROCESS("in process"),
        SHIPPED("shipped"),
        delivering("delivering"),
        delivered("delivered"),
        refunded("refunded"),
        canceled("canceled");

        public final String value;
        OrderStatus(String value){
            this.value=value;
        }
    }
}