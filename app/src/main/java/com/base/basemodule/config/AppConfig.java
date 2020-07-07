package com.base.basemodule.config;

public class AppConfig {
    public NetConfig getNetConfig() {
        return netConfig;
    }

    public void setNetConfig(NetConfig netConfig) {
        this.netConfig = netConfig;
    }

    private NetConfig netConfig;


    private AppConfig() {
    }


    public static final class AppConfigBuilder {
        private NetConfig netConfig;

        private AppConfigBuilder() {
        }

        public static AppConfigBuilder anAppConfig() {
            return new AppConfigBuilder();
        }

        public AppConfigBuilder withNetConfig(NetConfig netConfig) {
            this.netConfig = netConfig;
            return this;
        }

        public AppConfig build() {
            AppConfig appConfig = new AppConfig();
            appConfig.setNetConfig(netConfig);
            return appConfig;
        }
    }
}
