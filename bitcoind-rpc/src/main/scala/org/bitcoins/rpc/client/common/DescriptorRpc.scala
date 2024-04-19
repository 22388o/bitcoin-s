package org.bitcoins.rpc.client.common

import org.bitcoins.commons.jsonmodels.bitcoind.{
  DeriveAddressesResult,
  DescriptorsResult,
  GetDescriptorInfoResult,
  ImportDescriptorResult
}
import org.bitcoins.commons.serializers.JsonSerializers._
import org.bitcoins.commons.serializers.JsonWriters.DescriptorWrites
import org.bitcoins.core.protocol.script.descriptor.Descriptor
import play.api.libs.json.Json

import scala.concurrent.Future

/** RPC calls in V18 that use descriptor to give us output information
  * @see [[https://bitcoincore.org/en/doc/0.18.0/rpc/util/deriveaddresses/]]
  * @see [[https://bitcoincore.org/en/doc/0.18.0/rpc/util/getdescriptorinfo/]]
  */
trait DescriptorRpc {
  self: Client =>

  def deriveAddresses(
      descriptor: Descriptor,
      range: Option[Vector[Double]]): Future[DeriveAddressesResult] = {
    val params =
      if (range.isDefined)
        List(DescriptorWrites.writes(descriptor), Json.toJson(range))
      else List(DescriptorWrites.writes(descriptor))
    bitcoindCall[DeriveAddressesResult]("deriveaddresses", params)
  }

  def getDescriptorInfo(
      descriptor: Descriptor): Future[GetDescriptorInfoResult] = {
    bitcoindCall[GetDescriptorInfoResult](
      "getdescriptorinfo",
      List(DescriptorWrites.writes(descriptor)))
  }

  /** https://bitcoincore.org/en/doc/22.0.0/rpc/wallet/importdescriptors/
    * @param imports
    * @return
    */
  def importDescriptors(imports: Vector[DescriptorsResult]): Future[
    Vector[ImportDescriptorResult]] = {
    bitcoindCall[Vector[ImportDescriptorResult]]("importdescriptors",
                                                 List(Json.toJson(imports)))
  }
}
