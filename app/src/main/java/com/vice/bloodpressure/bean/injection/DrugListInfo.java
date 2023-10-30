package com.vice.bloodpressure.bean.injection;

import java.util.List;

/**
 * 作者: beauty
 * 类名:
 * 传参:
 * 描述:
 */
public class DrugListInfo {
    private String drug_name;
    private List<String>drug_data;

    public String getDrug_name() {
        return drug_name;
    }

    public void setDrug_name(String drug_name) {
        this.drug_name = drug_name;
    }

    public List<String> getDrug_data() {
        return drug_data;
    }

    public void setDrug_data(List<String> drug_data) {
        this.drug_data = drug_data;
    }
}
