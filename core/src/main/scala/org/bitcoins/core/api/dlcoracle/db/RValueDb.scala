package org.bitcoins.core.api.dlcoracle.db

import org.bitcoins.core.hd._
import org.bitcoins.crypto.SchnorrNonce

case class RValueDb(
    nonce: SchnorrNonce,
    eventName: String,
    purpose: HDPurpose,
    accountCoin: HDCoinType,
    accountIndex: Int,
    chainType: Int,
    keyIndex: Int) {

  val path: BIP32Path = BIP32Path.fromString(
    s"m/${purpose.constant}'/${accountCoin.toInt}'/$accountIndex'/$chainType'/$keyIndex'")
}

object RValueDbHelper {

  def apply(
      nonce: SchnorrNonce,
      eventName: String,
      account: HDAccount,
      chainType: Int,
      keyIndex: Int): RValueDb = {
    RValueDb(
      nonce = nonce,
      eventName = eventName,
      purpose = account.purpose,
      accountCoin = account.coin.coinType,
      accountIndex = account.index,
      chainType = chainType,
      keyIndex = keyIndex
    )
  }
}
