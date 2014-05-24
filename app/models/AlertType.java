package models;

public enum AlertType {  
    INVITE("invite", 1), JOIN("join", 2);  
    private String name;  
    private int index;  
    
    private AlertType(String name, int index) {  
        this.name = name;  
        this.index = index;  
    }  
    // 普通方法  
    public static String getName(int index) {  
        for (AlertType a : AlertType.values()) {  
            if (a.getIndex() == index) {  
                return a.name;  
            }  
        }  
        return null;  
    }  
    // get set 方法  
    public String getName() {  
        return name;  
    }  
    public void setName(String name) {  
        this.name = name;  
    }  
    public int getIndex() {  
        return index;  
    }  
    public void setIndex(int index) {  
        this.index = index;  
    }  
}
