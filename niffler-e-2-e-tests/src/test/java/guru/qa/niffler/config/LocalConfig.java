package guru.qa.niffler.config;

import com.codeborne.selenide.Configuration;

public class LocalConfig implements Config {

  static final LocalConfig instance = new LocalConfig();

  static {
    Configuration.browserSize = "1980x1024";
  }

  private LocalConfig() {
  }

  @Override
  public String frontUrl() {
    return "http://127.0.0.1:3000";
  }

  @Override
  public String authUrl() {
    return "http://127.0.0.1:9000";
  }

  @Override
  public String spendUrl() {
    return "http://127.0.0.1:8093";
  }

  @Override
  public String userdataUrl() {
    return "http://127.0.0.1:8089";
  }

  @Override
  public String gatewayUrl() {
    return "http://127.0.0.1:8090";
  }

  @Override
  public String jdbcHost() {
    return "localhost";
  }

  @Override
  public String currencyGrpcHost() {
    return "localhost";
  }
}
