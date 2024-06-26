package org.bitcoins.core.protocol.dlc.models

import org.bitcoins.core.protocol.script.ScriptWitnessV0
import org.bitcoins.core.protocol.tlv.FundingSignaturesV0TLV
import org.bitcoins.core.protocol.transaction.TransactionOutPoint
import org.bitcoins.core.util.{Indexed, SeqWrapper}
import org.bitcoins.crypto.{ECAdaptorSignature, ECPublicKey}

sealed trait DLCSignatures

case class FundingSignatures(
    sigs: Vector[(TransactionOutPoint, ScriptWitnessV0)])
    extends SeqWrapper[(TransactionOutPoint, ScriptWitnessV0)]
    with DLCSignatures {

  require(sigs.nonEmpty, s"FundingSignatures.sigs cannot be empty")

  override protected def wrapped
      : Vector[(TransactionOutPoint, ScriptWitnessV0)] = sigs

  def get(outPoint: TransactionOutPoint): Option[ScriptWitnessV0] = {
    sigs.find(_._1 == outPoint).map(_._2)
  }

  def apply(outPoint: TransactionOutPoint): ScriptWitnessV0 = {
    get(outPoint).get
  }

  def merge(other: FundingSignatures): FundingSignatures = {
    FundingSignatures(sigs ++ other.sigs)
  }

  def toTLV: FundingSignaturesV0TLV = {
    FundingSignaturesV0TLV(sigs.map(_._2))
  }
}

case class CETSignatures(outcomeSigs: Vector[(ECPublicKey, ECAdaptorSignature)])
    extends DLCSignatures {

  require(outcomeSigs.nonEmpty,
          s"CETSignatures cannot have outcomeSigs be empty")
  lazy val keys: Vector[ECPublicKey] = outcomeSigs.map(_._1)
  lazy val adaptorSigs: Vector[ECAdaptorSignature] = outcomeSigs.map(_._2)

  def indexedOutcomeSigs: Vector[(Indexed[ECPublicKey], ECAdaptorSignature)] = {
    outcomeSigs.zipWithIndex.map { case ((adaptorPoint, sig), index) =>
      (Indexed(adaptorPoint, index), sig)
    }
  }

  def apply(key: ECPublicKey): ECAdaptorSignature = {
    outcomeSigs
      .find(_._1 == key)
      .map(_._2)
      .getOrElse(
        throw new IllegalArgumentException(s"No signature found for $key"))
  }
}
