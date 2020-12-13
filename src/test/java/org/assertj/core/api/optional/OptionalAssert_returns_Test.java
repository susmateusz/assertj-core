/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.api.optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.AssertionsUtil.assertThatAssertionErrorIsThrownBy;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.util.Optional;
import java.util.function.Function;

import org.assertj.core.test.Employee;
import org.assertj.core.test.Name;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("OptionalAssert returns")
class OptionalAssert_returns_Test {

  private Optional<Employee> optionalJack;

  @BeforeEach
  void setup() {
    optionalJack = Optional.of(new Employee(12, new Name("Jack"), 34));
  }

  @Test
  void should_fail_when_optional_is_null() {
    // given
    Optional<String> nullActual = null;
    // then
    assertThatAssertionErrorIsThrownBy(() -> assertThat(nullActual).returns("abcde",
                                                                            Function.identity())).withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_method_is_is_null() {
    // given
    Optional<String> something = Optional.of("something");

    // then
    assertThatNullPointerException()
                                    .isThrownBy(() -> assertThat(something).returns("any-value", null))
                                    .withMessage("The given getter method/Function must not be null");
  }

  @Test
  void should_pass_if_expected_value_is_null() {
    assertThat(optionalJack).returns(null, value -> null);
  }

  @Test
  void should_fail_when_value_mismatch() {
    // when
    AssertionError assertionError = expectAssertionError(() -> assertThat(optionalJack).returns(new Name("Other Name"),
                                                                                                Employee::getName));

    // then
    then(assertionError).hasMessageContainingAll("Expecting:", "to be equal to:", "but was not.");
  }

  @Test
  void should_perform_multiple_assertions() {
    assertThat(optionalJack)
                            .returns(34, Employee::getAge)
                            .returns(new Name("Jack"), Employee::getName)
                            .returns(optionalJack.get(), Function.identity());
  }

}
