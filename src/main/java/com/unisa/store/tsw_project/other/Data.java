package com.unisa.store.tsw_project.other;

public interface Data {
    enum Type{
        PHYSICAL(false), DIGITAL(true);
        public final boolean val;
        Type(boolean val){
            this.val=val;
        }
    }

    enum Condition{
        X(0, "Digitale"),
        A(1, "Nuovo"),
        B(2, "Quasi Nuovo"),
        C(3, "Buono"),
        D(4, "Sufficiente"),
        E(5, "Danneggiato");

        public final String description;
        public final int dbValue;

        Condition(int dbValue, String description){
            this.dbValue = dbValue;
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


    enum Platform{
        Atari2600, C64, GameBoy, GameCube, N64, PC, ps1, ps2, SegaMegaDrive
    }
}
