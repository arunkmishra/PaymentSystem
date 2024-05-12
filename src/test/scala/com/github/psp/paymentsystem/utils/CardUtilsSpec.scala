package com.github.psp.paymentsystem.utils

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import com.github.psp.paymentsystem.utils.CardUtils.validateCardNumber

class CardUtilsSpec extends AnyFlatSpec with Matchers {
  import CardUtilsSpec._

  it should "be true for valid card number" in {
    validateCardNumber(ValidCardNumber) shouldBe true
  }

  it should "be false for invalid card number" in {
    validateCardNumber(InValidCardNumber) shouldBe false
  }
}

object CardUtilsSpec {
  private val ValidCardNumber = "3301025789989305"
  private val InValidCardNumber = "3301022210769124"
}
