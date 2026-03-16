package com.ym.orderservice.infrastructure.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/debug")
@RequiredArgsConstructor
public class DebugController {
  private final JdbcTemplate jdbcTemplate;

  @DeleteMapping("/clear-database")
  public String clearDatabase() {
    jdbcTemplate.execute("DELETE FROM order_items");
    jdbcTemplate.execute("DELETE FROM orders");
    jdbcTemplate.execute("DELETE FROM addresses");
    jdbcTemplate.execute("DELETE FROM customers");
    return "Database cleared!";
  }
}
