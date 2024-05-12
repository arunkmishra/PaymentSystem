package com.github.psp.paymentsystem.service.validator

import java.time.YearMonth

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class ValidatorSpec extends AnyFlatSpec with Matchers {
  import ValidatorSpec._
  "validateCardNumber" should "return card number when the card number is valid" in {
    Validator.validateCardNumber(ValidCardNumber) shouldEqual Right(ValidCardNumber)
  }

  it should "return InvalidCardNumber error when the card number is invalid" in {
    Validator.validateCardNumber(InValidCardNumber) shouldEqual Left(InvalidCardNumber())
  }

  "validateExpiryDate" should "return Right when the expiry date is in the future" in {
    Validator.validateExpiryDate(YearMonth.now().plusMonths(1)).isRight shouldBe true
  }

  it should "return Left with CardExpired when the expiry date is in the past" in {
    Validator.validateExpiryDate(YearMonth.now().minusMonths(1)) shouldEqual Left(CardExpired())
  }

  "validateAmount" should "return Right when the amount is greater than zero" in {
    Validator.validateAmount(ValidAmount) shouldEqual Right(ValidAmount)
  }

  it should "return Left with InvalidAmount when the amount is zero or negative" in {
    Validator.validateAmount(InValidAmount1) shouldEqual Left(InvalidAmount())
    Validator.validateAmount(InValidAmount2) shouldEqual Left(InvalidAmount())
  }

  "validateMerchantId" should "return Right when the merchant ID is exactly 15 characters long" in {
    Validator.validateMerchantId(ValidMerchantId) shouldEqual Right(ValidMerchantId)
  }

  it should "return Left with InvalidMerchantId when the merchant ID is not 15 characters long" in {
    Validator.validateMerchantId(InValidMerchantId) shouldEqual Left(InvalidMerchantId())
  }

  "validateCvv" should "return Right when the CVV is between 100 and 9999 (inclusive)" in {
    Validator.validateCvv(ValidCvv) shouldEqual Right(ValidCvv)
    Validator.validateCvv(ValidCvv1) shouldEqual Right(ValidCvv1)
  }

  it should "return Left with InvalidCvv when the CVV is not between 100 and 9999" in {
    Validator.validateCvv(InValidCvv) shouldEqual Left(InvalidCvv())
    Validator.validateCvv(InValidCvv1) shouldEqual Left(InvalidCvv())
  }

  "validateCurrency" should "return Right when the currency is valid" in {
    Validator
      .ValidCurrencies
      .forall(currency => Validator.validateCurrency(currency).isRight) shouldBe true
  }

  it should "return Left with InvalidCurrency when the currency is invalid" in {
    Validator.validateCurrency("XXX") shouldEqual Left(InvalidCurrency())
  }
}
object ValidatorSpec {
  private val ValidCardNumber = "3301025789989305"
  private val InValidCardNumber = "3301022210769124"
  private val ValidMerchantId = "123456789012345"
  private val InValidMerchantId = "1234567890"
  private val ValidAmount: BigDecimal = 100.123
  private val InValidAmount1: BigDecimal = -232.00
  private val InValidAmount2: BigDecimal = 0.00
  private val ValidCvv = 111
  private val ValidCvv1 = 1234
  private val InValidCvv = 99
  private val InValidCvv1 = 10001
}
