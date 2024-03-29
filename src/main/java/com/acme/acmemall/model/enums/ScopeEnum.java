package com.acme.acmemall.model.enums;

public enum ScopeEnum {
    ALL(0, "通用券"),
    GOODS(1, "商品券"),
    CATEGORY(2, "品类券");

    private final int scope;

    private final String title;

    ScopeEnum(int scope, String title) {
        this.scope = scope;
        this.title = title;
    }

    public int getScope() {
        return scope;
    }

    public String getTitle() {
        return title;
    }

    public static ScopeEnum parse(int scope) {
        for (ScopeEnum scopeEnum : ScopeEnum.values()) {
            if (scopeEnum.getScope() == scope) {
                return scopeEnum;
            }
        }
        return null;
    }

    public static Boolean verify(int scope) {
        for (ScopeEnum scopeEnum : ScopeEnum.values()) {
            if (scopeEnum.getScope() == scope) {
                return true;
            }
        }
        return false;
    }
}
