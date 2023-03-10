package com.acme.acmemall.service;

import com.acme.acmemall.model.Counter;

import java.util.Optional;

public interface ICounterService {

  Optional<Counter> getCounter(Integer id);

  void upsertCount(Counter counter);

  void clearCount(Integer id);
}
