package com.vice.bloodpressure.bean;

public class TemperatureListDataBean{
        private String datetime;
        private String temperature;//这两个返回的是int类型，不是string呀
        private String type;

        public String getDatetime() {
            return datetime;
        }

        public void setDatetime(String datetime) {
            this.datetime = datetime;
        }

        public String getTemperature() {
            return temperature;
        }

        public void setTemperature(String temperature) {
            this.temperature = temperature;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }