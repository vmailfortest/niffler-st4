package guru.qa.niffler.controller;

import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.DataFilterValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.StatisticJson;
import guru.qa.niffler.service.StatisticAggregator;
import guru.qa.niffler.service.UserDataClient;
import guru.qa.niffler.service.api.RestSpendClient;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class SpendController {

  private final RestSpendClient restSpendClient;
  private final UserDataClient userDataClient;
  private final StatisticAggregator statisticAggregator;

  @Autowired
  public SpendController(RestSpendClient restSpendClient, UserDataClient userDataClient, StatisticAggregator statisticAggregator) {
    this.restSpendClient = restSpendClient;
    this.userDataClient = userDataClient;
    this.statisticAggregator = statisticAggregator;
  }

  @GetMapping("/spends")
  public List<SpendJson> getSpends(@AuthenticationPrincipal Jwt principal,
                                   @RequestParam(required = false) DataFilterValues filterPeriod,
                                   @RequestParam(required = false) CurrencyValues filterCurrency) {
    String username = principal.getClaim("sub");
    return restSpendClient.getSpends(username, filterPeriod, filterCurrency);
  }

  @PostMapping("/addSpend")
  @ResponseStatus(HttpStatus.CREATED)
  public SpendJson addSpend(@Valid @RequestBody SpendJson spend,
                            @AuthenticationPrincipal Jwt principal) {
    String username = principal.getClaim("sub");
    CurrencyValues userCurrency = userDataClient.currentUser(username).currency();
    if (userCurrency != spend.currency()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Spending currency should be same with user currency");
    }
    return restSpendClient.addSpend(spend.addUsername(username));
  }

  @PatchMapping("/editSpend")
  public SpendJson editSpend(@Valid @RequestBody SpendJson spend,
                             @AuthenticationPrincipal Jwt principal) {
    if (spend.id() == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id should be present");
    }
    String username = principal.getClaim("sub");
    return restSpendClient.editSpend(spend.addUsername(username));
  }

  @GetMapping("/statistic")
  public List<StatisticJson> getTotalStatistic(@AuthenticationPrincipal Jwt principal,
                                               @RequestParam(required = false) CurrencyValues filterCurrency,
                                               @RequestParam(required = false) DataFilterValues filterPeriod) {
    String username = principal.getClaim("sub");
    return statisticAggregator.enrichStatisticRequest(username, filterCurrency, filterPeriod);
  }

  @DeleteMapping("/deleteSpends")
  public ResponseEntity<Void> deleteSpends(@AuthenticationPrincipal Jwt principal,
                                           @RequestParam List<String> ids) {
    String username = principal.getClaim("sub");
    return new ResponseEntity<>(restSpendClient.deleteSpends(username, ids));
  }
}
