package ru.moretech.moretech_server.datasource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CalculatorDatasource extends Datasource {
    private static final Logger LOG = LoggerFactory.getLogger(CalculatorDatasource.class);

    private CalculatorSettings settings;

    public void saveSettings(String name, String language) {
        if (settings == null) {
            settings = new CalculatorSettings();
        }
        settings.setLanguage(language);
        settings.setName(name);
    }

    static class CalculatorSettings {
        private String name;
        private String language;

        public CalculatorSettings() {
        }

        public CalculatorSettings(String name, String language) {
            this.name = name;
            this.language = language;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }
    }
}
