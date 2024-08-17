package com.verygoodbank.tes.service.processor;

public interface Processor<OUT, IN> {
    OUT process(IN data);
}
