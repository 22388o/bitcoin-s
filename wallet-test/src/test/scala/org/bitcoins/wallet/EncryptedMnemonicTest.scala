package org.bitcoins.wallet

import org.bitcoins.core.util.TimeUtil
import org.bitcoins.crypto.AesPassword
import org.bitcoins.keymanager._
import org.bitcoins.testkitcore.Implicits._
import org.bitcoins.testkitcore.gen.CryptoGenerators
import org.bitcoins.testkitcore.util.BitcoinSUnitTest

import scala.util.{Failure, Success}

class EncryptedMnemonicTest extends BitcoinSUnitTest {
  behavior of "EncryptedMnemonic"

  it must "fail to decrypt with a bad password" in {
    val password = AesPassword.fromNonEmptyString("good")
    val badPassword = AesPassword.fromNonEmptyString("bad")

    val mnemonicCode = CryptoGenerators.mnemonicCode.sampleSome
    val mnemonic = DecryptedMnemonic(mnemonicCode, TimeUtil.now, None, false)
    val encrypted = mnemonic.encrypt(password)

    val decrypted = encrypted.toMnemonic(badPassword)

    assert(decrypted.isFailure)
  }

  it must "have encryption/decryption symmetry" in {
    forAll(CryptoGenerators.mnemonicCode, CryptoGenerators.aesPassword) {
      (mnemonicCode, password) =>
        val mnemonic =
          DecryptedMnemonic(mnemonicCode, TimeUtil.now, None, false)
        val encrypted = mnemonic.encrypt(password)
        val decrypted = encrypted.toMnemonic(password) match {
          case Success(clear) => clear
          case Failure(exc)   => fail(exc)
        }
        assert(decrypted == mnemonicCode)
    }
  }
}
