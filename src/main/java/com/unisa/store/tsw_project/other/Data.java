package com.unisa.store.tsw_project.other;

public interface Data {
    public enum Type{
        PHYSICAL(false), DIGITAL(true);
        public final boolean val;
        Type(boolean val){
            this.val=val;
        }
    }

    public enum Condition{
        X(0, "Digitale", 0),
        A(1, "Nuovo", 0),
        B(2, "Quasi Nuovo", 5),
        C(3, "Buono", 8),
        D(4, "Sufficiente", 12),
        E(5, "Danneggiato",20);

        public final String description;
        public final int dbValue;
        public final int discount;

        Condition(int dbValue, String description, int discount){
            this.dbValue = dbValue;
            this.description=description;
            this.discount=discount;
        }

        public static Condition getEnum(int dbValue){
            for(Condition c : Condition.values()){
                if (c.dbValue==dbValue){return c;}
            }
            return null;
        }

        public int getDbValue() {
            return this.dbValue;
        }

        public String getDescription() {
            return description;
        }

        public int getDiscount() {
            return discount;
        }
    }

    public enum OrderStatus{
        INPROCESS("in process"),
        SHIPPED("shipped"),
        delivering("delivering"),
        delivered("delivered"),
        refunded("refunded"),
        canceled("canceled");

        public final String value;

        public static OrderStatus getEnum(String value){
            for(OrderStatus os : OrderStatus.values()){
                if (os.value.equals(value)){return os;}
            }
            return null;
        }

        OrderStatus(String value){
            this.value=value;
        }
    }


    public enum Platform{
        Atari2600, C64, GameBoy, GameCube, N64, PC, ps1, ps2, SegaMegaDrive
    }
}
